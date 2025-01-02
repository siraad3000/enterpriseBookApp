
package se.systementor.enterpriseBookBackend.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "M3cJ93nFyP2k6vLzV5sDh2kTeG9Rb8QyN1xT3rW8bNpX6pZ3tQmR9sJfL6dN7hQ8M3cJ93nFyP2k6vLzV5sDh2kTeG9Rb8QyN1xT3rW8bNpX6pZ3tQmR9sJfL6dN7hQ8"; // Use a secure key
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public static String generateToken(String username, String userId, String role) {
        return Jwts.builder()
                .setSubject(username) // Subject claim
                .claim("userId", userId) // Custom claim for userId
                .claim("role", role) // Custom claim for role
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token expiration
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // Signing with HS512 and secret key
                .compact();
    }

}

