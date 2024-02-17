package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.Server;

import java.util.Objects;


public class UserService {
    public AuthData register(UserData user) throws DataAccessException{
        if(Server.userDAO.getUser(user.username()) == null) {
            Server.userDAO.createUser(user.username(), user.password(), user.email());
            return Server.authDAO.createAuthToken(user.username());
        }
        throw new DataAccessException("User Already Exists");
    }
    public AuthData login(UserData user) throws DataAccessException{
        UserData tempUser = Server.userDAO.getUser(user.username());
        if (tempUser != null) {
            if (verifyLogin(tempUser, user.username(), user.password())) {
                return Server.authDAO.createAuthToken(user.username());
            }
        }
        throw new DataAccessException("Incorrect Login");
    }
    public void logout(UserData user) {}

    private boolean verifyLogin(UserData user, String username, String password){
        if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)) {
            return true;
        }
        return false;
    }
}
