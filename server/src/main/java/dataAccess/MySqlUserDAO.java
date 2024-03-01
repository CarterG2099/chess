package dataAccess;

import model.UserData;

public class MySqlUserDAO implements UserDAO {
    @Override
    public void deleteUserData() throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {

    }
}
