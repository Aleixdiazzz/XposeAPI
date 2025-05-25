package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.User;
import com.aleix.XposeAPI.repository.UserRepository;
import com.aleix.XposeAPI.specification.UserSpecifications;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


/**
 * Service class for managing User entities.
 * Provides methods for CRUD operations, user authentication, and additional business logic related to Users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     * 
     * @param userRepository Repository for User entity operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the database.
     * 
     * @return List of all User entities
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a specific user by its ID.
     * 
     * @param id The ID of the user to retrieve
     * @return Optional containing the User if found, empty otherwise
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Creates a new user in the database.
     * 
     * @param user The User entity to create
     * @return The saved User entity with generated ID
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user with new details.
     * 
     * @param id The ID of the user to update
     * @param userDetails The updated User entity data
     * @return Optional containing the updated User if found, empty otherwise
     */
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

    /**
     * Deletes a user by its ID.
     * 
     * @param id The ID of the user to delete
     * @return true if the user was deleted, false if it wasn't found
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Authenticates a user with email and password.
     * 
     * @param email The email of the user
     * @param password The password of the user
     * @return Optional containing the User if authentication is successful, empty otherwise
     */
    public Optional<User> loginUser(String email, String password) {
        return userRepository.findByEmailAndPasswordHash(email, password);
    }

    /**
     * Filters users based on provided criteria.
     * 
     * @param name Optional name filter
     * @param surname Optional surname filter
     * @param email Optional email filter
     * @return List of User entities matching the filter criteria
     */
    public List<User> filterUsers (String name, String surname, String email){
        return userRepository.findAll(UserSpecifications.filterUsers(name, surname, email));
    }
}
