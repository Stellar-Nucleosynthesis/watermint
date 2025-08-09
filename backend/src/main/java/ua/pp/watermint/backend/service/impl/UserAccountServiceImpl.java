package ua.pp.watermint.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.mapper.request.UserAccountRequestMapper;
import ua.pp.watermint.backend.mapper.response.UserAccountResponseMapper;
import ua.pp.watermint.backend.repository.UserAccountRepository;
import ua.pp.watermint.backend.service.UserAccountService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final UserAccountRequestMapper userAccountRequestMapper;
    private final UserAccountResponseMapper userAccountResponseMapper;

    @Override
    public UserAccountResponseDto getById(UUID id) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User account with id " + id + " not found!"));
        checkAuthorization(account);
        return userAccountResponseMapper.userAccountToDto(account);
    }

    @Override
    public UserAccountResponseDto getByUsername(String username) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User account with username " + username + " not found!"));
        checkAuthorization(account);
        return userAccountResponseMapper.userAccountToDto(account);
    }

    @Override
    public List<UserAccountResponseDto> search(String name) {
        return userAccountRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .map(userAccountResponseMapper::userAccountToDto)
                .toList();
    }

    @Override
    public UserAccountResponseDto create(UserAccountRequestDto dto) {
        return userAccountResponseMapper.userAccountToDto(
                userAccountRepository.save(userAccountRequestMapper.dtoToUserAccount(dto)));
    }

    @Override
    public UserAccountResponseDto update(UUID id, UserAccountRequestDto dto) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User account with id " + id + " not found!"));
        checkAuthorization(account);
        if(dto.getEmail() != null){
            account.setEmail(dto.getEmail());
            account.setVerified(false);
        }
        if(dto.getUsername() != null)
            account.setUsername(dto.getUsername());
        if(dto.getPassword() != null)
            account.setPassword(dto.getPassword());
        if(dto.getName() != null)
            account.setName(dto.getName());
        if(dto.getBirthDate() != null)
            account.setBirthDate(dto.getBirthDate());
        if(dto.getProfilePicture() != null)
            account.setProfilePicture(dto.getProfilePicture());

        return userAccountResponseMapper.userAccountToDto(userAccountRepository.saveAndFlush(account));
    }

    @Override
    public void delete(UUID id) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User account with id " + id + " not found!"));
        checkAuthorization(account);
        userAccountRepository.deleteById(id);
    }

    private void checkAuthorization(UserAccount account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null){
            throw new AccessDeniedException("Access denied: no authenticated user!");
        }
        String username = authentication.getName();
        if(!username.equals(account.getUsername())){
            throw new AccessDeniedException(
                    String.format("Access denied: tried to access account '%s' as '%s'!",
                            account.getUsername(), username)
            );
        }
    }
}
