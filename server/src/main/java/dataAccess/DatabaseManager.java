package dataAccess;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    private static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    public static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {
                        if (p.isEmpty()) {
                            ps.setString(i + 1, ""); // or set it to NULL if that's your desired behavior
                        } else {
                            ps.setString(i + 1, p);
                        }
                    } else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ArrayList<?>) ps.setString(i + 1, new Gson().toJson(param));
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                int affectedRows = ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return affectedRows;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()), 500);
        }
    }

    private static final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth_data (
              `auth_id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256),
              `auth_token` varchar(256),
              PRIMARY KEY (`auth_id`)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS  user_data (
              `user_id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256),
              `password` varchar(256),
              `email` varchar(256),
              PRIMARY KEY (`user_id`)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS  game_data (
              `game_id` int NOT NULL AUTO_INCREMENT,
              `white_username` varchar(256),
              `black_username` varchar(256),
              `game_name` varchar(256),
              `chess_game` varchar(256),
              `player_color` varchar(256),
              `observer_list` varchar(256),
              PRIMARY KEY (`game_id`)
            );
            """
    };

    public static void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
//        var statement = "SELECT COUNT(*) AS count FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?";
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()), 500);
        }
    }


}
