package service;

import DataAccessException.DataAccessException;
import model.AuthData;
import model.UserData;
import server.Server;

import java.util.Objects;


public class UserService {
    public AuthData register(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Bad Request", 400);
        }
        if (Server.userDAO.getUser(user.username()) == null) {
            Server.userDAO.createUser(user.username(), user.password(), user.email());
            return Server.authDAO.createAuthToken(user.username());
        }
        throw new DataAccessException("Already Taken", 403);
    }

    public AuthData login(UserData user) throws DataAccessException {
        UserData tempUser = Server.userDAO.getUser(user.username());
        if (tempUser != null) {
            if (validUser(tempUser, user.username(), user.password())) {
                return Server.authDAO.createAuthToken(user.username());
            }
        }
        throw new DataAccessException("Unauthorized", 401);
    }

    public void logout(String authToken) throws DataAccessException {
        Server.authDAO.deleteAuthToken(authToken);
    }

    private boolean validUser(UserData user, String username, String password) throws DataAccessException {
        if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)) {
            return true;
        }
        throw new DataAccessException("Unauthorized", 401);
    }

    public boolean validAuthToken(String authToken) throws DataAccessException {
        AuthData user = Server.authDAO.getAuthToken(authToken);
        if (user == null) {
            throw new DataAccessException("Unauthorized", 401);
        }
        return true;
    }

    public UserData getUser(String authToken) throws DataAccessException {
        AuthData userAuthData = Server.authDAO.getAuthToken(authToken);
        return Server.userDAO.getUser(userAuthData.username());
    }
}
