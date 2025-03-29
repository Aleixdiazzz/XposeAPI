package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.User;
import com.aleix.XposeAPI.repository.UserRepository;
import com.aleix.XposeAPI.specification.UserSpecifications;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPasswordHash(userDetails.getPasswordHash());
            user.setName(userDetails.getName());
            user.setSurname(userDetails.getSurname());
            return userRepository.save(user);
        });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<User> loginUser(String email, String password) {
        return userRepository.findByEmailAndPasswordHash(email, password);
    }

    public List<User> filterUsers (String name, String surname, String email){
        return userRepository.findAll(UserSpecifications.filterUsers(name, surname, email));
    }
}

