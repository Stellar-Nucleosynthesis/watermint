package ua.pp.watermint.backend.mapper.request;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.dto.request.PrivateChatRequestDto;
import ua.pp.watermint.backend.entity.ChatContent;
import ua.pp.watermint.backend.entity.PrivateChat;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.repository.UserAccountRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PrivateChatRequestMapper {
    private final UserAccountRepository userAccountRepository;

    public PrivateChat dtoToPrivateChat(PrivateChatRequestDto dto){
        UUID account1Id = dto.getUserAccount1Id();
        UserAccount userAccount1 = userAccountRepository.findById(account1Id).orElseThrow(
                () -> new EntityNotFoundException("User account with id " + account1Id + " not found!"));
        UUID account2Id = dto.getUserAccount2Id();
        UserAccount userAccount2 = userAccountRepository.findById(account2Id).orElseThrow(
                () -> new EntityNotFoundException("User account with id " + account2Id + " not found!"));
        return PrivateChat.builder()
                .userAccount1(userAccount1)
                .userAccount2(userAccount2)
                .chatContent(new ChatContent())
                .build();
    }
}
