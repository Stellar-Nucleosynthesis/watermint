package ua.pp.watermint.backend.mapper.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.entity.UserAccount;

@Component
@RequiredArgsConstructor
public class UserAccountRequestMapper {
    public UserAccount dtoToUserAccount(UserAccountRequestDto dto) {
        return UserAccount.builder()
                .email(dto.getEmail())
                .verified(false)
                .username(dto.getUsername())
                .password(dto.getPassword())
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .profilePicture(dto.getProfilePicture())
                .build();
    }
}
