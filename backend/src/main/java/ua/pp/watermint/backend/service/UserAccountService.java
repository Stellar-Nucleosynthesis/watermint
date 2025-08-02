package ua.pp.watermint.backend.service;

import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserAccountService {
    UserAccountResponseDto getById(UUID id);
    UserAccountResponseDto getByUsername(String username);
    List<UserAccountResponseDto> search(String name);
    UserAccountResponseDto create(UserAccountRequestDto dto);
    UserAccountResponseDto update(UUID id, UserAccountRequestDto dto);
    void delete(UUID id);
}
