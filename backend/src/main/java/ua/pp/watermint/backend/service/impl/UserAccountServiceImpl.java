package ua.pp.watermint.backend.service.impl;

import org.springframework.stereotype.Service;
import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.service.UserAccountService;

import java.util.List;
import java.util.UUID;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Override
    public UserAccountResponseDto getById(UUID id) {
        return null;
    }

    @Override
    public UserAccountResponseDto getByUsername(String username) {
        return null;
    }

    @Override
    public List<UserAccountResponseDto> search(String name) {
        return List.of();
    }

    @Override
    public UserAccountResponseDto create(UserAccountRequestDto dto) {
        return null;
    }

    @Override
    public UserAccountResponseDto update(UUID id, UserAccountRequestDto dto) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}
