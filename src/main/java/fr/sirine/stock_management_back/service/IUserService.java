package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.User;

import java.util.List;

public interface IUserService {
    User findById(Integer id);
    List<UserDto> getAllUsers();
    void updateUser(UserDto userDto, String password);
    void deleteUser(Integer id);
    List<UserDto> findByRole(String role);
}
