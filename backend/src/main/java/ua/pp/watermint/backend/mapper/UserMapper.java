package ua.pp.watermint.backend.mapper;

import org.mapstruct.Mapper;
import ua.pp.watermint.backend.dto.response.UserResponseDto;
import ua.pp.watermint.backend.entity.User;

@Mapper
public interface UserMapper {
    UserResponseDto userToUserDto(User user);
}
