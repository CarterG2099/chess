package dataAccess;

public interface AuthDAO {
    void deleteAuthData() throws DataAccessException;
    String getAuthToken();
    void createAuthToken(String username);
}
