package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.entity.UserAccount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserAccountMapperImpl.class)
class UserAccountMapperTest {
    @Autowired
    private UserAccountMapper userAccountMapper;

    @Test
    void userAccountToDtoTest() {
        UserAccount user = getExampleUserAccount();
        UserAccountResponseDto dto =
                userAccountMapper.userAccountToDto(user);
        assertThat(areEqual(user, dto)).isTrue();
    }

    public static UserAccount getExampleUserAccount(){
        return UserAccount.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .email("email@example.com")
                .verified(true)
                .username("username")
                .password("password")
                .name("name")
                .birthDate(LocalDate.EPOCH)
                .profilePicture(new byte[]{3, 4, 5, 6, 7, 8})
                .build();
    }

    public static boolean areEqual(UserAccount user, UserAccountResponseDto dto){
        if(user == null && dto == null)
            return true;
        return user != null && dto != null
                && user.getId().equals(dto.getId())
                && user.getCreateTime().equals(dto.getCreateTime())
                && user.getUpdateTime().equals(dto.getUpdateTime())
                && user.getEmail().equals(dto.getEmail())
                && user.getVerified().equals(dto.getVerified())
                && user.getUsername().equals(dto.getUsername())
                && user.getPassword().equals(dto.getPassword())
                && user.getName().equals(dto.getName())
                && user.getBirthDate().equals(dto.getBirthDate())
                && Arrays.equals(user.getProfilePicture(), dto.getProfilePicture());
    }
}