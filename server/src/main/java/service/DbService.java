package service;

import dataAccess.*;

public class DbService {

    public void clearData() throws DataAccessException {
        deleteAuthData();
        deleteUserData();
        deleteGameData();
    }
    private void deleteAuthData() throws DataAccessException {
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
