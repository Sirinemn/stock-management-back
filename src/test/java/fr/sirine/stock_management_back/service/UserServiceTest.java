package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.entities.Role;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1)
                .name("ADMIN")
                .build();
        user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .password("password")
                .email("john@oefr")
                .roles(List.of(role))
                .build();
    }
    @Test
    void should_find_user_by_id() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        userService.findById(1);
        verify(userRepository, times(1)).findById(1);
    }
    @Test
    void should_update_user() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.encode("password")).thenReturn("password");

        userService.updateUser("Jane", "Doe", "password", "jane@oefr", 1);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
        assertEquals("Jane", user.getFirstname());
    }
    @Test
    void should_delete_user() {
        userService.deleteUser(1);
        verify(userRepository, times(1)).deleteById(1);
    }
    @Disabled("Reason for disabling the test")
    @Test
    void should_get_all_users() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }
}
