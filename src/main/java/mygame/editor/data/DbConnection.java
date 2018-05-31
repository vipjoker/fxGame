package mygame.editor.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by oleh on 30.04.18.
 */
public class DbConnection {

    private static Connection connection;
    public static Connection getDbConnection(){
        try {
            if(connection != null && !connection.isClosed())return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");
        } catch ( Exception e ) {
            throw new RuntimeException("Cannot create connection");
        }
        return connection;
    }
}
