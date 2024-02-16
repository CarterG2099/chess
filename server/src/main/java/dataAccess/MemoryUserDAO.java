package dataAccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO{
    private final ArrayList<UserData> userDataArrayList = new ArrayList<>();
    @Override
    public void deleteUserData() throws DataAccessException{
        try {
            userDataArrayList.clear();
        } catch (Exception ex) {
            throw new DataAccessException("User Data");
        }
    }
}
