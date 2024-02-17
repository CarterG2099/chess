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

    @Override
    public UserData getUser(String username) {
        for (UserData user : userDataArrayList) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        userDataArrayList.add(new UserData(username, password, email));
    }
}
