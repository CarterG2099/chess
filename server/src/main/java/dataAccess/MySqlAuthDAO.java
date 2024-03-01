package dataAccess;

import model.AuthData;

public class MySqlAuthDAO implements AuthDAO{
    @Override
    public void deleteAuthData() throws DataAccessException {

    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return null;
    }

    @Override
    public AuthData createAuthToken(String username) {
        return null;
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {

    }
}
