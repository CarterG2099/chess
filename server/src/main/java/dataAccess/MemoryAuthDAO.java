package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
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
    public String getAuthToken() {
        return "null";
    }

    @Override
    public AuthData createAuthToken(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData temptAuthData = new AuthData(username, authToken);
        authDataArrayList.add(temptAuthData);
        return temptAuthData;
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException{
        try {
            for(AuthData user : authDataArrayList) {
                if (user.authToken().equals(authToken)) {
                    authDataArrayList.remove(user);
                    return;
                }
            }
            throw new DataAccessException("No user found", 500);
        } catch(Exception ex){
            throw new DataAccessException("No user found", 500);
        }
    }
}
