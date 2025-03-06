package fr.sirine.stock_management_back.integration;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.mapper.UserMapper;
import fr.sirine.stock_management_back.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerIT {

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .firstname("firstname")
                .lastname("lastname")
                .email("john@doe.fr")
                .password("password")
                .build();
        userDto = UserDto.builder()
                .id(1)
                .firstname("firstname")
                .lastname("lastname")
                .email("john@doe.fr")
                .build();
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/admin");
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnUserById() throws Exception {
        when(userService.findById(1)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/admin/1");
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateUser() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/admin/1")
                .param("firstname", "updatedName")
                .param("lastname", "updatedLastname")
                .param("password", "newPassword")
                .param("email", "updated@mail.fr");
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteUser() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/admin/1");
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

}
