package dataAccess;

import DataAccessException.DataAccessException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlAuthDAO implements AuthDAO {
    @Override
    public void deleteAuthData() throws DataAccessException {
        var statement = "DELETE FROM auth_data";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth_data WHERE auth_token = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    @Override
    public AuthData createAuthToken(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData tempAuthData = new AuthData(username, authToken);
        var statement = "INSERT INTO auth_data (username, auth_token) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, username, authToken);
        return tempAuthData;
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth_data WHERE auth_token = ?";
        if (DatabaseManager.executeUpdate(statement, authToken) == 0) {
            throw new DataAccessException("No user found", 401);
        }
    }

    private AuthData readAuthData(ResultSet rs) throws SQLException {
        var authToken = rs.getString("auth_token");
        var username = rs.getString("username");
        return new AuthData(username, authToken);
    }
}
