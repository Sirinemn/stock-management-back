package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}
