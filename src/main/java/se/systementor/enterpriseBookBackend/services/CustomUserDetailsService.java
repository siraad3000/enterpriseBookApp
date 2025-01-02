package se.systementor.enterpriseBookBackend.services;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.systementor.enterpriseBookBackend.config.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DatabaseConnection dbConnection = new DatabaseConnection();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try (Connection connection = dbConnection.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                boolean enabled = true;

                return new org.springframework.security.core.userdetails.User(
                        username,
                        password,
                        enabled,
                        true, true, true,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}