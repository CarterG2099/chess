package service;

import dataAccess.*;

public class DbService {

    public void clearData() throws DataAccessException {
        deleteAuthToken();
        deleteUserData();
        deleteGameData();
    }
    private void deleteAuthToken() throws DataAccessException {
        AuthDAO authDao = new MemoryAuthDAO();
        authDao.deleteAuthData();
    }

    private void deleteUserData() throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.deleteUserData();
    }

    private void deleteGameData() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        gameDAO.deleteGameData();
    }
}
