package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.mapper.UserMapper;
import fr.sirine.stock_management_back.payload.response.MessageResponse;
import fr.sirine.stock_management_back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "User Management", description = "Admin operations for managing users")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AdminController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) throws IOException {

        User user = userService.findById(Integer.parseInt(id));
        return ResponseEntity.ok(this.userMapper.toDto(user));
    }
    @Operation(summary = "Update user", description = "Update a user's details by their ID")
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
