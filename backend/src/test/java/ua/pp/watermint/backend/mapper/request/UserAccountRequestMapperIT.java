package ua.pp.watermint.backend.mapper.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.request.UserAccountRequestDto;
import ua.pp.watermint.backend.entity.UserAccount;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserAccountRequestMapper.class)
public class UserAccountRequestMapperIT {
    @Autowired
    private UserAccountRequestMapper userAccountRequestMapper;

    @Test
    void dtoToUserAccount_withValidDto_returnsEntity() {
        UserAccountRequestDto dto = UserAccountRequestDto.builder()
                .email("email@example.com")
                .username("username")
                .password("password")
                .name("name")
                .birthDate(LocalDate.now())
                .profilePicture(new byte[]{2, 3, 4})
                .build();
        UserAccount entity = userAccountRequestMapper.dtoToUserAccount(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
        assertThat(entity.getVerified()).isFalse();
        assertThat(entity.getUsername()).isEqualTo(dto.getUsername());
        assertThat(entity.getPassword()).isEqualTo(dto.getPassword());
        assertThat(entity.getName()).isEqualTo(dto.getName());
        assertThat(entity.getBirthDate()).isEqualTo(dto.getBirthDate());
        assertThat(entity.getProfilePicture()).isEqualTo(dto.getProfilePicture());
    }
}
