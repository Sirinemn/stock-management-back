package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.User;

import java.util.List;

public interface IUserService {
    User findById(Integer id);
    List<UserDto> getAllUsers();
    void updateUser(String firstname, String lastname, String password, String email, Integer id);
    void deleteUser(Integer id);
}
