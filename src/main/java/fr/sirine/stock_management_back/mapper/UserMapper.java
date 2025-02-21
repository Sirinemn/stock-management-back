package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.Role;
import fr.sirine.stock_management_back.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringList")
    UserDto toDto(User user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "stringListToRoles")
    User toEntity(UserDto userDto);

    @Named("rolesToStringList")
    default List<String> rolesToStringList(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    @Named("stringListToRoles")
    default List<Role> stringListToRoles(List<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toList());
    }
}
