package com.project2.backemd.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.project2.backemd.dto.UserDto;
import com.project2.backemd.model.Role;
import com.project2.backemd.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Map UserDto to User
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "roles", source = "role", qualifiedByName = "mapListToSetOfRoles")
    User userDtoToUser(UserDto userDto);

    // Map User to UserDto
    //TODO: UserDto userToUserDto(User user);

    @Named("mapListToSetOfRoles")
    default Set<Role> mapListToSetOfRoles(List<String> value) {
        return value.stream()
                .map(role -> {
                    Role r = new Role();
                    r.setDescription(role);
                    return r;
                })
                .collect(Collectors.toSet());
    }
}
