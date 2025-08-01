package ua.pp.watermint.backend.mapper;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.entity.UserAccount;

@Mapper
public interface UserAccountMapper {
    UserAccountResponseDto userAccountToUserAccountDto(UserAccount user);
}
