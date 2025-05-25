package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.LoginRequest;
import com.aleix.XposeAPI.model.User;
import com.aleix.XposeAPI.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of {@link User} objects
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a {@link ResponseEntity} containing the {@link User} if found,
     *         or 404 Not Found if the user does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Filters users based on optional query parameters.
     *
     * @param name the name of the user (optional)
     * @param surname the surname of the user (optional)
     * @param email the email of the user (optional)
     * @return a list of {@link User} objects matching the filter criteria
     */
    @GetMapping("/filter")
    public List<User> filterUsers(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String surname,
                                  @RequestParam(required = false) String email) {
        return userService.filterUsers(name, surname, email);
    }

    /**
     * Creates a new user.
     *
     * @param user the {@link User} object to create
     * @return a {@link ResponseEntity} containing the created {@link User}
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Updates an existing user by ID.
     *
     * @param id the ID of the user to update
     * @param user the updated {@link User} data
     * @return a {@link ResponseEntity} containing the updated {@link User} if successful,
     *         or 404 Not Found if the user does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> updatedUser = userService.updateUser(id, user);
        return updatedUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return a {@link ResponseEntity} with 204 No Content if the deletion was successful,
     *         or 404 Not Found if the user does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param loginRequest the login request containing email and password
     * @return a {@link ResponseEntity} containing the {@link User} if authentication is successful,
     *         or 404 Not Found if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> loggedUser = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return loggedUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


}
