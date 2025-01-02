package se.systementor.enterpriseBookBackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.systementor.enterpriseBookBackend.config.JwtUtil;
import se.systementor.enterpriseBookBackend.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> userPayload) {
        String username = userPayload.get("username");
        String password = userPayload.get("password");

        if (userService.registerUser(username, password)) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            // Authenticate the user
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authResult = authenticationManager.authenticate(authToken);


            // Generate JWT
            String userId =  userService.getUserIdByUsername(username);
            String role = userService.getRoleFromUserId(userId);
            String token = JwtUtil.generateToken(username,userId,role);

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Specific error handling
            String errorMessage = determineErrorType(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }

    private String determineErrorType(AuthenticationException e) {
        // Determine the type of authentication failure
        if (e instanceof BadCredentialsException) {
            return "Invalid username or password.";
        } else if (e instanceof DisabledException) {
            return "User account is disabled. Please contact support.";
        } else if (e instanceof LockedException) {
            return "User account is locked. Please contact support.";
        } else if (e instanceof CredentialsExpiredException) {
            return "User credentials have expired. Please reset your password.";
        } else if (e instanceof AccountExpiredException) {
            return "User account has expired. Please contact support.";
        } else {
            return "Authentication failed. Please try again.";
        }


    }
}

