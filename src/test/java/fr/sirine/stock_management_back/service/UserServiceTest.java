package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .password("password")
                .email("john@oefr")
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

        userService.updateUser("Jane", "Doe", "password", "jane@oefr", 1);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
        assertEquals("Jane", user.getFirstname());
    }
}
