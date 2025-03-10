package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.mapper.UserMapper;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN")))
                .map(userMapper::toDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public void updateUser( String firstname, String lastname, String password, String email, Integer id){
        User initialUser = userRepository.findById(id).orElse(null);
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
