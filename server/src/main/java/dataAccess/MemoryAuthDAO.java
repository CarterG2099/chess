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
            throw new DataAccessException("Auth Data");
        }
    }

    @Override
    public String getAuthToken() {
        return "null";
    }

    @Override
    public void createAuthToken(String username){
        String authToken = UUID.randomUUID().toString();
        authDataArrayList.add(new AuthData(username,authToken));
    }
}
