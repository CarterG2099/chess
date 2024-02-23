package dataAccess;

import model.AuthData;
import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final ArrayList<AuthData> authDataArrayList = new ArrayList<>();

    @Override
    public void deleteAuthData() throws DataAccessException {
        try {
            authDataArrayList.clear();
        } catch (Exception ex) {
            throw new DataAccessException("Auth Data", 500);
        }
    }

    @Override
    public AuthData getAuthToken(String authtoken) {
        for (AuthData user : authDataArrayList) {
            if (user.authToken().equals(authtoken)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public AuthData createAuthToken(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData tempAuthData = new AuthData(username, authToken);
        authDataArrayList.add(tempAuthData);
        return tempAuthData;
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        AuthData userToDeleteToken = getAuthToken(authToken);
        if (userToDeleteToken == null) {
            throw new DataAccessException("No user found", 401);
        }
        authDataArrayList.removeIf(user -> user.equals(userToDeleteToken));
    }
}

