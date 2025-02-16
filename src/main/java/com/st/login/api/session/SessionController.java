package com.st.login.api.session;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SessionController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
        if ("admin".equals(user.getUsername()) && "123".equals(user.getPassword())) {
            session.setAttribute("user", "Sajid");
            session.setAttribute("loggedIn", true);
            return ResponseEntity.ok().body(new Response(true, "Login successful"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(false, "Invalid credentials"));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (Boolean.TRUE.equals(loggedIn)) {
            String user = (String) session.getAttribute("user");
            return ResponseEntity.ok().body(new UserResponse(true, user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(false, "Not logged in"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(new Response(true, "Logged out successfully"));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://127.0.0.1:5500")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}

class User {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class Response {
    private boolean success;
    private String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

class UserResponse extends Response {
    private String name;

    public UserResponse(boolean success, String name) {
        super(success, "User retrieved");
        this.name = name;
    }

    public String getName() { return name; }
}
