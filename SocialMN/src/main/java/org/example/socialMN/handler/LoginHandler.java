package org.example.socialMN.handler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.socialMN.exceptions.AuthenticationException;
import org.example.socialMN.exceptions.SignupValidationException;
import org.example.socialMN.model.User;
import org.example.socialMN.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler {

    private static final Logger logger = LogManager.getLogger(IService.class);

    @Autowired
    private IService iService;


    /**
     * Validates the provided username based on certain criteria.
     * username The username to be validated.
     *
     * @throws SignupValidationException If the username is not valid based on the specified criteria.
     */
    static void validateUsername(String username) throws SignupValidationException {
        logger.info("Validating username: " + username);

        if (username == null || username.trim().isEmpty() || "null".equals(username.trim())) {
            throw new SignupValidationException("Username cannot be null");
        }
        if (!username.matches("[a-zA-Z0-9_]+")) {
            throw new SignupValidationException("Username can only contain letters, numbers, and underscores");
        }
        logger.info("Username validation successful for: " + username);


    }

    /**
     * Validates the provided password based on certain criteria.
     * <p>
     * password The password to be validated.
     *
     * @throws SignupValidationException If the password is not valid based on the specified criteria.
     */
    static void validatePassword(String password) throws SignupValidationException {
        logger.info("Validating password");

        if (password == null || password.trim().isEmpty() || "null".equals(password.trim())) {
            throw new SignupValidationException("password cannot be null");
        }
        if (password.length() <= 8) {
            throw new SignupValidationException("Password must be at least 8 characters long");
        }
        // Check for at least one capital letter
        if (!password.matches(".*[A-Z].*")) {
            throw new SignupValidationException("Password must contain at least one capital letter");
        }
        // Check for at least two numbers
        if (!password.matches(".*\\d.*\\d.*")) {
            throw new SignupValidationException("Password must contain at least two numbers");
        }
        // Check for at least one symbol
        if (!password.matches(".*[!@#$%^&*()-_+=].*")) {
            throw new SignupValidationException("Password must contain at least one symbol");
        }
        logger.info("Password validation successful");

    }

    /**
     * Validates the provided email based on certain criteria.
     * email The email to be validated.
     *
     * @throws SignupValidationException If the email is not valid based on the specified criteria.
     */
    static void validateEmail(String email) throws SignupValidationException {
        logger.info("Validating email");


        if (email == null || email.trim().isEmpty() || "null".equals(email.trim())) {
            throw new SignupValidationException("Email cannot be null");
        }
        if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            throw new SignupValidationException("Invalid email format");
        }
        logger.info("Email validation successful");

    }


    /**
     * Handles the registration request for a new user.
     * user The user object containing registration details.
     *
     * @return ResponseEntity indicating the result of the registration process.
     */
    public ResponseEntity<String> handleRegistrationRequest(User user) throws SignupValidationException {

        logger.info("validating the username , password, email");
        try {

            String toEmail = user.getEmail();
            String subject = "Registration Confirmation";
            String text = "Thank you for registering on our platform. Your registration is successful!";
            iService.sendMail(toEmail, subject, text);

            validateUsername(user.getUsername());

            // Check if the username already exists
            if (usernameExists(user.getUsername())) {
                throw new SignupValidationException("Username is already taken. Please choose another username.");
            }

            validatePassword(user.getPassword());
            validateEmail(user.getEmail());

            // Check if the email already exists
            if (useremailExists(user.getEmail())) {
                throw new SignupValidationException("Email is already taken. Please choose another email.");
            }
            iService.addUser(user);

            logger.info("User registered successfully - Username: " + user.getUsername());

            return ResponseEntity.ok("Registered Successfully");
        } catch (SignupValidationException e) {
            logger.error("Registration failed: ");
            e.printStackTrace();
            throw new SignupValidationException("Error occured during sign up");
        }
    }

    public ResponseEntity<String> validateUser(String username, String password) throws AuthenticationException {
        logger.info("Received login request - Username: " + username);


        try {
            if (iService.getValidateUser(username, password)) {
                logger.info("Login successful - Username: " + username);
                return ResponseEntity.ok("Login Successfully");
            } else {
                logger.warn("Login failed for user: " + username);
                throw new AuthenticationException("Login Failed");
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public boolean usernameExists(String username) {
        logger.debug("Checking if username exists: " + username);

        return iService.existsByUsername(username);

    }

    public boolean useremailExists(String email) {
        logger.debug("Checking if email exists: " + email);

        return iService.existsByEmail(email);
    }
}
