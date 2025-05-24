package com.example.Food.delivery.service;

import com.example.Food.delivery.dto.GoogleLoginDto;
import com.example.Food.delivery.dto.UserLoginDto;
import com.example.Food.delivery.dto.UserSignupDto;
import com.example.Food.delivery.Entity.User;
import com.example.Food.delivery.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Utility method to validate student email format
    private void validateStudentEmail(String email) throws Exception {
        // Regex pattern: 'u' followed by exactly 7 characters, then '@student.cuet.ac.bd'
        String pattern = "^u.{7}@student\\.cuet\\.ac\\.bd$";
        if (!email.matches(pattern)) {
            throw new Exception("Student email must be in the format: u*******@student.cuet.ac.bd");
        }
    }

    public User signup(UserSignupDto dto) throws Exception {
        // Check if email already exists
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }

        // Validate password presence
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new Exception("Password is required");
        }

        // If role is student, validate student email pattern
        if ("student".equalsIgnoreCase(dto.getRole())) {
            validateStudentEmail(dto.getEmail());
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        user.setRole(dto.getRole().toLowerCase());  // Store role in lowercase for consistency

        // Vendor-specific fields
        if ("vendor".equalsIgnoreCase(dto.getRole())) {
            user.setSchedule(dto.getSchedule());       // accept schedule from request
            user.setAvailabilityStatus("ON");          // automatically set availabilityStatus to "ON"
        }

        return userRepository.save(user);
    }

    public User login(UserLoginDto dto) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) throw new Exception("Invalid credentials");

        User user = userOpt.get();

        // If password is null, likely Google login user
        if (user.getPassword() == null) {
            throw new Exception("Please login with Google");
        }

        // If student, validate student email pattern again (optional, can be skipped if already validated at signup)
        if ("student".equalsIgnoreCase(user.getRole())) {
            validateStudentEmail(user.getEmail());
        }

        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        return user;
    }

    public User googleLogin(String googleId, String email, String name) throws Exception {
        Optional<User> userOpt = userRepository.findByGoogleId(googleId);

        User user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            Optional<User> byEmail = userRepository.findByEmail(email);
            if (byEmail.isPresent()) {
                user = byEmail.get();
                user.setGoogleId(googleId); // Link Google ID to existing user

                // If user is student, validate email format
                if ("student".equalsIgnoreCase(user.getRole())) {
                    validateStudentEmail(user.getEmail());
                }

            } else {
                // New user - default role set to student, adjust if needed
                if (!email.matches("^u.{7}@student\\.cuet\\.ac\\.bd$")) {
                    throw new Exception("Student email must be in the format: u*******@student.cuet.ac.bd");
                }

                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setGoogleId(googleId);
                user.setRole("student"); // Default role
            }
            user = userRepository.save(user);
        }

        return user;
    }
}
