package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    public String message;
    public int statusCode;

    public DataAccessException(String message, int statusCode) {
        this.message = "Error: " + message;
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "Error: " + message;
    }
}
