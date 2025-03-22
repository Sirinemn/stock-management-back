package fr.sirine.stock_management_back.integration;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.entities.Role;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.mapper.UserMapper;
import fr.sirine.stock_management_back.service.impl.CategoryService;
import fr.sirine.stock_management_back.service.impl.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerIT {

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private UserDto userDto;
    private Category category;
    private CategoryDto categoryDto;
    private User admin;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        adminRole = Role.builder()
                .name("ADMIN")
                .build();
        admin = User.builder()
                .id(2)
                .roles(List.of(adminRole))
                .firstname("admin")
                .email("admin@mail.fr")
                .build();
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
        category = Category.builder()
                .id(1)
                .name("category")
                .build();
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllUsers() throws Exception {
        when(userService.getUsersByAdmin(admin.getId())).thenReturn(List.of(userDto));
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
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteCategory() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/admin/categories/1");
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(categoryDto));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/admin/categories");
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void should_add_category() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/categories")
                .param("name", "newCategory");
        mockMvc.perform(request)
                .andExpect(jsonPath("$.message").value("Category added with success!"))
                .andExpect(status().isCreated());
    }
}
