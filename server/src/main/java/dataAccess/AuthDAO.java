package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void deleteAuthData() throws DataAccessException;

    AuthData getAuthToken(String authToken) throws DataAccessException;

    AuthData createAuthToken(String username) throws DataAccessException;

    void deleteAuthToken(String authToken) throws DataAccessException;
}
