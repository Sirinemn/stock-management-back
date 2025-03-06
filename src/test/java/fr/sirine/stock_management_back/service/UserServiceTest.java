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
                .email("john@oefr")
                .build();
    }
    @Test
    void should_find_user_by_id() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        userService.findById(1);
        verify(userRepository, times(1)).findById(1);
    }
}
