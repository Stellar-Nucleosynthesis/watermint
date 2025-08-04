package ua.pp.watermint.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
        return userAccountRepository.findById(id)
                .map(userAccountResponseMapper::userAccountToDto).orElseThrow(() ->
                        new EntityNotFoundException("User account with id " + id + " not found!"));
    }

    @Override
    public UserAccountResponseDto getByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .map(userAccountResponseMapper::userAccountToDto).orElseThrow(() ->
                        new EntityNotFoundException("User account with username " + username + " not found!"));
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
        UserAccount account = userAccountRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User account with id " + id + " not found!"));
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
        if(!userAccountRepository.existsById(id))
            throw new EntityNotFoundException("User account with id " + id + " not found!");
        userAccountRepository.deleteById(id);
    }
}
