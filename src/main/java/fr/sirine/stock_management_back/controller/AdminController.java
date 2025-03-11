package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.mapper.UserMapper;
import fr.sirine.stock_management_back.payload.response.MessageResponse;
import fr.sirine.stock_management_back.service.CategoryService;
import fr.sirine.stock_management_back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Admin Management", description = "Admin operations for managing users and categories")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(UserService userService, UserMapper userMapper, CategoryService categoryService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.categoryService = categoryService;
        logger.info("AdminController initialized");
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        logger.info("Test Logger Info");
        return userService.getAllUsers();
    }
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) throws IOException {

        User user = userService.findById(Integer.parseInt(id));
        return ResponseEntity.ok(this.userMapper.toDto(user));
    }
    @Operation(summary = "Update user", description = "Update a user's details by their ID")
    @PutMapping("/users/{id}")
    public ResponseEntity<MessageResponse> updateUser(
                                                      @RequestParam("firstname") @NotBlank @Size(max = 63) String firstname,
                                                      @RequestParam("lastname") @NotBlank @Size(max = 63) String lastname,
                                                      @RequestParam("password") @NotBlank @Size(max = 63) String password,
                                                      @RequestParam("email") @NotBlank @Size(max = 63) String email,
                                                      @PathVariable Integer id) {
        userService.updateUser(firstname, lastname, password, email, id);
        MessageResponse messageResponse = new MessageResponse("Updated with success!");
        return new ResponseEntity<>( messageResponse, HttpStatus.OK);
    }
    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Delete a category", description = "Admin can delete a category by ID")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @Operation(summary = "Add a category", description = "Admin can add a new category")
    @PostMapping("/category")
    public ResponseEntity<MessageResponse> addCategory(@RequestParam("name") @NotBlank @Size(max = 63) String name) {
        categoryService.addCategory(name);
        MessageResponse messageResponse = new MessageResponse("Category added with success!");
        return new ResponseEntity<>( messageResponse, HttpStatus.CREATED);
    }
}
