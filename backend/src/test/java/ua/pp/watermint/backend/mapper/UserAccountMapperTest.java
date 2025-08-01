package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.util.DtoAssertions;
import ua.pp.watermint.backend.util.TestFixtures;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserAccountMapperImpl.class)
class UserAccountMapperTest {
    @Autowired
    private UserAccountMapper userAccountMapper;

    @Test
    void userAccountToDtoTest() {
        UserAccount user = TestFixtures.getExampleUserAccount();
        UserAccountResponseDto dto = userAccountMapper.userAccountToDto(user);
        DtoAssertions.assertUserAccountEquals(user, dto);
    }
}