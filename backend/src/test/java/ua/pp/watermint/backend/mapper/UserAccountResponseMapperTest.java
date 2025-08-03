package ua.pp.watermint.backend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.entity.UserAccount;
import ua.pp.watermint.backend.mapper.response.UserAccountResponseMapper;
import ua.pp.watermint.backend.mapper.response.UserAccountResponseMapperImpl;
import ua.pp.watermint.backend.util.DtoAssertions;
import ua.pp.watermint.backend.util.TestFixtures;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserAccountResponseMapperImpl.class)
class UserAccountResponseMapperTest {
    @Autowired
    private UserAccountResponseMapper userAccountResponseMapper;

    @Test
    void userAccountToDtoTest() {
        UserAccount user = TestFixtures.getExampleUserAccount();
        UserAccountResponseDto dto = userAccountResponseMapper.userAccountToDto(user);
        DtoAssertions.assertUserAccountEquals(user, dto);
    }
}