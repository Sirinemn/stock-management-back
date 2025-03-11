package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.exceptions.custom.UserNotFoundException;
import fr.sirine.stock_management_back.mapper.UserMapper;
import fr.sirine.stock_management_back.repository.UserRepository;
import fr.sirine.stock_management_back.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("Users found: " + users);
        logger.info("Users found: " + users);

        List<User> nonAdminUsers = users.stream()
                .filter(user -> user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN")))
                .toList();
        logger.info("Non-admin users: " + nonAdminUsers);

        List<UserDto> userDtos = nonAdminUsers.stream()
                .map(userMapper::toDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        logger.info("User DTOs: " + userDtos);
        return userDtos;
    }
    public void updateUser( String firstname, String lastname, String password, String email, Integer id){
        User initialUser = findById(id);
        LocalDateTime now = LocalDateTime.now();
        if (initialUser!= null) {
            initialUser.setFirstname(firstname);
            initialUser.setLastname(lastname);
            initialUser.setPassword(passwordEncoder.encode(password));
            initialUser.setEmail(email);
            initialUser.setLastModifiedDate(now);
            userRepository.save(initialUser);
        }
    }
    public void deleteUser(Integer id) {

        this.userRepository.deleteById(id);
    }
}
