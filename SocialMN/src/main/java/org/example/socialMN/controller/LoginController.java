package org.example.socialMN.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.socialMN.exceptions.AuthenticationException;
import org.example.socialMN.exceptions.SignupValidationException;
import org.example.socialMN.handler.LoginHandler;
import org.example.socialMN.model.Login;
import org.example.socialMN.model.User;
import org.example.socialMN.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class LoginController {
    private static final Logger logger = LogManager.getLogger(IService.class);

    @Autowired
    private LoginHandler loginHandler;

    /**
     * Handles logout requests.
     *
     * @return ResponseEntity with a success message if logout is successful.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request ) {
        logger.info("Received logout request");

//        request.getSession().invalidate();

        return ResponseEntity.ok("Logout successful");
    }

    /**
     * Handles user registration requests.
     * The User object containing registration details.
     */
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRegisteredData(@RequestBody User user) throws SignupValidationException {

        logger.info("Received registration request for user: " + user.getUsername());
        return loginHandler.handleRegistrationRequest(user);
    }


    /**
     * @param username The username to check for existence.
     * @return ResponseEntity with a boolean indicating whether the username exists.
     */
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExistence(@RequestHeader String username) {
        logger.info("Received request to check the existence of username: " + username);

        boolean exists = loginHandler.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * @param email The email to check for existence.
     * @return ResponseEntity with a boolean indicating whether the email exists.
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExistence(@RequestHeader String email) {
        logger.info("Received request to check the existence of email: " + email);

        boolean exists = loginHandler.useremailExists(email);
        return ResponseEntity.ok(exists);
    }

    /**
     * Handles user login requests.
     * login The Login object containing user login credentials.
     * validate the user
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Login login) throws AuthenticationException {
        String username = login.getUsername();
        String password = login.getPassword();
        logger.info("Received login request - Username: " + username);
        return loginHandler.validateUser(username, password);
    }
}
