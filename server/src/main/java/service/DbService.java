package service;

import DataAccessException.DataAccessException;
import server.Server;

public class DbService {
    public void clearData() throws DataAccessException {
        Server.userDAO.deleteUserData();
        Server.authDAO.deleteAuthData();
        Server.gameDAO.deleteGameData();
    }
}
