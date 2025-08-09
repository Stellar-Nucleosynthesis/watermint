package ua.pp.watermint.backend.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.filter.PrivateChatFilterDto;
import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.dto.response.PrivateChatResponseDto;
import ua.pp.watermint.backend.entity.PrivateChat;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.mapper.request.PrivateChatRequestMapper;
import ua.pp.watermint.backend.mapper.response.PrivateChatResponseMapper;
import ua.pp.watermint.backend.repository.PrivateChatRepository;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.security.service.AuthorizationService;
import ua.pp.watermint.backend.service.PrivateChatService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivateChatServiceImpl implements PrivateChatService {
    private final PrivateChatRepository privateChatRepository;
    private final UserAccountRepository userAccountRepository;
    private final PrivateChatRequestMapper privateChatRequestMapper;
    private final PrivateChatResponseMapper privateChatResponseMapper;
    private final AuthorizationService authorizationService;

    @Override
    public PrivateChatResponseDto getById(UUID id) {
        PrivateChat chat = privateChatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Private chat with id " + id + " not found!"));
        authorizationService.checkAnyAuthorized(chat.getUserAccount1(), chat.getUserAccount2());
        return privateChatResponseMapper.privateChatToDto(chat);
    }

    @Override
    public List<PrivateChatResponseDto> search(PrivateChatFilterDto filter) {
        UUID userId = filter.getUserAccount1Id();
        UserAccount account = userAccountRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User with id " + userId + " not found!"));
        authorizationService.checkAuthorized(account);
        String otherName = filter.getUserAccount2Name();

        Specification<PrivateChat> caseA = (root, query, cb) -> {
            Join<PrivateChat, UserAccount> user1Join = root.join("userAccount1");
            Join<PrivateChat, UserAccount> user2Join = root.join("userAccount2");

            Predicate user1Match = cb.equal(user1Join.get("id"), userId);
            Predicate user2NameMatch = cb.conjunction();

            if (otherName != null && !otherName.isBlank()) {
                user2NameMatch = cb.like(cb.lower(user2Join.get("username")), "%" + otherName.toLowerCase() + "%");
            }

            return cb.and(user1Match, user2NameMatch);
        };

        Specification<PrivateChat> caseB = (root, query, cb) -> {
            Join<PrivateChat, UserAccount> user1Join = root.join("userAccount1");
            Join<PrivateChat, UserAccount> user2Join = root.join("userAccount2");

            Predicate user2Match = cb.equal(user2Join.get("id"), userId);
            Predicate user1NameMatch = cb.conjunction();

            if (otherName != null && !otherName.isBlank()) {
                user1NameMatch = cb.like(cb.lower(user1Join.get("username")), "%" + otherName.toLowerCase() + "%");
            }

            return cb.and(user2Match, user1NameMatch);
        };

        Specification<PrivateChat> spec = caseA.or(caseB);
        return privateChatRepository.findAll(spec)
                .stream()
                .map(privateChatResponseMapper::privateChatToDto)
                .toList();
    }

    @Override
    public PrivateChatResponseDto create(PrivateChatRequestDto dto) {
        UUID user1Id = dto.getUserAccount1Id();
        UserAccount user1 = userAccountRepository.findById(user1Id).orElseThrow(() ->
                new EntityNotFoundException("User with id " + user1Id + " not found!"));
        UUID user2Id = dto.getUserAccount2Id();
        UserAccount user2 = userAccountRepository.findById(user2Id).orElseThrow(() ->
                new EntityNotFoundException("User with id " + user2Id + " not found!"));
        authorizationService.checkAnyAuthorized(user1, user2);
        if(privateChatRepository.existsByUserAccount1_IdAndUserAccount2_Id(user1Id, user2Id)
                || privateChatRepository.existsByUserAccount1_IdAndUserAccount2_Id(user2Id, user1Id)){
            throw new EntityExistsException("Private chat between users already exists");
        }
        return privateChatResponseMapper.privateChatToDto(
                privateChatRepository.save(privateChatRequestMapper.dtoToPrivateChat(dto)));
    }

    @Override
    public void delete(UUID id) {
        PrivateChat chat = privateChatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Private chat with id " + id + " not found!"));
        authorizationService.checkAnyAuthorized(chat.getUserAccount1(), chat.getUserAccount2());
        privateChatRepository.deleteById(id);
    }
}
