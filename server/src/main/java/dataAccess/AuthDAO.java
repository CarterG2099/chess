package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void deleteAuthData() throws DataAccessException;
    String getAuthToken();
    AuthData createAuthToken(String username);
    void deleteAuthToken(String authToken) throws DataAccessException;
}
