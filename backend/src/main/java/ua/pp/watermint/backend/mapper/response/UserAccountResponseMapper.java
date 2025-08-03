package ua.pp.watermint.backend.mapper.response;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.UserAccountResponseDto;
import ua.pp.watermint.backend.entity.UserAccount;

@Mapper
public interface UserAccountResponseMapper {
    UserAccountResponseDto userAccountToDto(UserAccount user);
}
