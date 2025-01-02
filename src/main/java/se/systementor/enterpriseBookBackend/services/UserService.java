package se.systementor.enterpriseBookBackend.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.systementor.enterpriseBookBackend.config.DatabaseConnection;
import java.sql.*;

@Service
public class UserService {
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public boolean registerUser(String username, String password) {
        String encryptedPassword = passwordEncoder.encode(password);

        try (Connection conn = dbConnection.getConnection()) {
            String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, username);
                stmt.setString(2, encryptedPassword);
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    String assignRoleQuery = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
                    try (PreparedStatement roleStmt = conn.prepareStatement(assignRoleQuery)) {
                        // Assuming "USER" role has id 1
                        roleStmt.setInt(1, userId);
                        roleStmt.setInt(2, 2); // Assuming "USER" role has id 1 in `roles` table
                        roleStmt.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserIdByUsername(String username) {
        String query = "SELECT user_id FROM users WHERE username = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Retrieve the user ID from the result set
                return Integer.toString(rs.getInt("user_id"));
            } else {

                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRoleFromUserId(String userId){
        String query = "SELECT role_id FROM user_roles WHERE user_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role_id = Integer.toString(rs.getInt("role_id"));
                    if(role_id.equals("1"))
                        return "ADMIN";
                    else if(role_id.equals("2"))
                        return "USER";
                    else return null;
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public boolean authenticateUser(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password");

                // Use the password encoder to verify the password
                return passwordEncoder.matches(password, storedPasswordHash);
            } else {

                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
