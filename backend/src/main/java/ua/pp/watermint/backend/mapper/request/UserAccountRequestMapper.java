package ua.pp.watermint.backend.mapper.request;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.entity.UserAccount;

@Component
@RequiredArgsConstructor
public class UserAccountRequestMapper {
    private final PasswordEncoder passwordEncoder;

    public UserAccount dtoToUserAccount(UserAccountRequestDto dto) {
        return UserAccount.builder()
                .email(dto.getEmail())
                .verified(false)
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .profilePicture(dto.getProfilePicture())
                .build();
    }
}
