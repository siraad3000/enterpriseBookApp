package se.systementor.enterpriseBookBackend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class SessionController {

    @GetMapping("/session")
    public ResponseEntity<String> getSession(HttpSession session) {
        if (session != null) {
            return ResponseEntity.ok("Session ID: " + session.getId());
        } else {
            return ResponseEntity.status(401).body("No session found");
        }
    }
}
