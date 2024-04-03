package org.example.socialMN.controller;

import org.example.socialMN.model.User;
import org.example.socialMN.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class EmailController {

    @Autowired
    private IService service;


    @PostMapping("/register")
    @ResponseBody
    public String sendConfirmationMail(@RequestBody User user) {

        // Send registration confirmation email
        String toEmail = user.getEmail();
        String subject = "Registration Confirmation";
        String text = "Thank you for registering on our platform. Your registration is successful!";
        service.sendMail(toEmail, subject, text);
        return "Registration successful. Confirmation email sent!";
    }
}
