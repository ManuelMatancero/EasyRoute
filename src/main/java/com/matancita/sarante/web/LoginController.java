package com.matancita.sarante.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
            Model model) {
        if (error != null) {
            // The "error" parameter is present in the URL
            model.addAttribute("errorMessage", "Invalid username or password"); // Custom error message
        }
        return "login";
    }
}
