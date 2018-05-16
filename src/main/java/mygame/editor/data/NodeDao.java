package mygame.editor.data;

import mygame.editor.data.entities.EntityNode;

import javax.xml.soap.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 30.04.18.
 */
public class NodeDao {
    Connection connection;

    public NodeDao(Connection connection) throws Exception {
        this.connection = connection;
        init(connection);
    }

    private void init(Connection connection) throws Exception {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Node " +
                "(id INTEGER  PRIMARY KEY ," +
                "name      TEXT  ," +
                "x         REAL    NOT NULL, " +
                "y         REAL    NOT NULL, " +
                "width     REAL, " +
                "height    REAL," +
                "parent_id REAL," +
                "FOREIGN KEY (parent_id) REFERENCES Node(id))";
        stmt.executeUpdate(sql);
        stmt.close();

    }

    public List<EntityNode> getAll() throws Exception {
        String sql = "SELECT * FROM Node;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<EntityNode> nodes = new ArrayList<>();
        while (resultSet.next()) {
            EntityNode node = new EntityNode();
            node.setId(resultSet.getInt("id"));
            node.setX(resultSet.getFloat("x"));
            node.setY(resultSet.getFloat("y"));
            node.setName(resultSet.getString("name"));
            node.setWidth(resultSet.getFloat("width"));
            node.setHeight(resultSet.getFloat("height"));
            node.setParentId(resultSet.getInt("parent_id"));
            nodes.add(node);
        }
        return nodes;


    }

    public void insert(EntityNode node) throws Exception {
        String sql = "INSERT INTO Node (id,name,x,y,width,height,parent_id) " +
                "VALUES (?,?,?,?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int column = 1;
        preparedStatement.setInt(column++,node.getId());
        preparedStatement.setString(column++,node.getName());
        preparedStatement.setDouble(column++, node.getX());
        preparedStatement.setDouble(column++, node.getY());
        preparedStatement.setDouble(column++, node.getWidth());
        preparedStatement.setDouble(column++, node.getHeight());
        preparedStatement.setInt(column++, node.getParentId());
        preparedStatement.execute();

    }

    public void update(EntityNode node) throws Exception {

        String sql = "UPDATE Node SET x = ? , y = ? ,width = ? ,height = ?, parent_id = ? WHERE ID = ?;";
        int column = 1;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDouble(column++, node.getX());
        preparedStatement.setDouble(column++, node.getY());
        preparedStatement.setDouble(column++, node.getWidth());
        preparedStatement.setDouble(column++, node.getHeight());
        preparedStatement.setInt(column++, node.getId());
        preparedStatement.setInt(column++, node.getParentId());
        preparedStatement.executeUpdate();
    }

    public void delete(EntityNode node) throws Exception {
        String sql = "DELETE FROM Node WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, node.getId());
        preparedStatement.execute();
    }

    public void deleteAll() throws Exception{
        connection.createStatement().execute("DELETE FROM Node");
    }

}
