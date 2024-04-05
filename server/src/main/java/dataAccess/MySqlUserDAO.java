package dataAccess;

import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    @Override
    public void deleteUserData() throws DataAccessException {
        var statement = "DELETE FROM user_data";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user_data WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUserData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;

    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO user_data (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, username, password, email);

    }

    private UserData readUserData(ResultSet rs) throws DataAccessException, SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }
}
