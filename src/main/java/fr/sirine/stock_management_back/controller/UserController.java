package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.payload.response.MessageResponse;
import fr.sirine.stock_management_back.service.impl.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("${id}")
    public ResponseEntity<UserDto> getUserById(Integer id) {
        return ResponseEntity.ok(userService.getById(id));
    }
    @PutMapping("${id}")
    public ResponseEntity<MessageResponse> updateUser(Integer id, UserDto userDto) {
        userService.updateUserById(id, userDto );
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }
}

