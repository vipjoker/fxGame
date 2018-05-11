package mygame.editor.data;

import mygame.editor.data.entities.EntityFixture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 11.05.18.
 */
public class FixtureDefDao {


    Connection connection;

    public FixtureDefDao(Connection connection) throws Exception {
        this.connection = connection;
        init(connection);
    }

    private void init(Connection connection) throws Exception {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Fixture " +
                "(id                INTEGER  PRIMARY KEY   AUTOINCREMENT ," +
                "shape              INTEGER  ," +
                "friction           REAL ," +
                "restitution        REAL ," +
                "density            REAL," +
                "is_sensor         INTEGER);";
        stmt.executeUpdate(sql);
        stmt.close();

    }

    public List<EntityFixture> getAll() throws Exception {
        String sql = "SELECT * FROM Fixture;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<EntityFixture> nodes = new ArrayList<>();
        while (resultSet.next()) {
            EntityFixture node = new EntityFixture();
            node.setId(resultSet.getInt("id"));
            node.setShape(resultSet.getInt("shape"));
            node.setFriction(resultSet.getFloat("friction"));
            node.setRestitution(resultSet.getFloat("restitution"));
            node.setDensity(resultSet.getFloat("density"));
            node.setSensor(resultSet.getBoolean("is_sensor") );
            nodes.add(node);
        }
        return nodes;


    }

    public void insert(EntityFixture body) throws Exception {
        String sql = "INSERT INTO Fixture (shape," +
                "friction," +
                "restitution," +
                "density," +
                "is_sensor ) " +
                "VALUES (?,?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, body.getShape());
        preparedStatement.setFloat(2, body.getFriction());
        preparedStatement.setFloat(3, body.getRestitution());
        preparedStatement.setFloat(4, body.getDensity());
        preparedStatement.setBoolean(5,body.isSensor());
        preparedStatement.execute();
    }

    public void update(EntityFixture body) throws Exception {

        String sql = "UPDATE Fixture SET " +
                "shape = ? ," +
                "friction = ? ," +
                "restitution = ?," +
                "density = ?," +
                "is_sensor = ? " +
                "WHERE ID = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, body.getShape());
        preparedStatement.setFloat(2, body.getFriction());
        preparedStatement.setFloat(3, body.getRestitution());
        preparedStatement.setFloat(4, body.getDensity());
        preparedStatement.setBoolean(5,body.isSensor());
        preparedStatement.setInt(6,body.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(EntityFixture entityFixture) throws Exception {
        String sql = "DELETE FROM Node WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, entityFixture.getId());
        preparedStatement.execute();
    }
}
