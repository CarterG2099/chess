package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void deleteAuthData() throws DataAccessException;

    AuthData getAuthToken(String authToken);

    AuthData createAuthToken(String username);
    void deleteAuthToken(String authToken) throws DataAccessException;
}
