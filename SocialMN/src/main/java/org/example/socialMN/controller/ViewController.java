package org.example.socialMN.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller class responsible for handling view-related requests.
 */
@Controller
@RequestMapping("/user")
public class ViewController {

    /**
     * Handles requests for the index page.
     * return The name of the index page view.
     */
    @RequestMapping("/index")
    public String indexPage() {
        return "index";
    }

    /**
     * Handles requests for the login page.
     * return The name of the login page view.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Handles requests for the signup page.
     * return The name of the signup page view.
     */
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    /**
     * Handles requests for the dashboard page.
     * return The name of the dashboard page view.
     */
    @GetMapping("/userdashboard")
    public String dashboardPage() {
        return "userdashboard";
    }


}
