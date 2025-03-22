package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.payload.response.MessageResponse;
import fr.sirine.stock_management_back.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(Integer id) {
        return ResponseEntity.ok(userService.getById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Integer id, @RequestBody @Valid UserDto userDto) {
        userService.updateUserById(id, userDto );
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }
}

