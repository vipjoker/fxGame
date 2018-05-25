package mygame.editor.data;

import mygame.editor.data.entities.EntitySprite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 25.05.18.
 */
public class SpriteDao {
    Connection connection;

    public SpriteDao(Connection connection) throws Exception {
        this.connection = connection;
        init(connection);
    }

    private void init(Connection connection) throws Exception {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Sprite " +
                "(id INTEGER  PRIMARY KEY ," +
                "name      TEXT  ," +
                "url    TEXT," +
                "parent_id INTEGER," +
                "FOREIGN KEY (parent_id) REFERENCES Node(id))";
        stmt.executeUpdate(sql);
        stmt.close();

    }

    public List<EntitySprite> getAll() throws Exception {
        String sql = "SELECT * FROM Sprite;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<EntitySprite> nodes = new ArrayList<>();
        while (resultSet.next()) {
            EntitySprite node = new EntitySprite();
            node.setId(resultSet.getInt("id"));
            node.setName(resultSet.getString("name"));
            node.setUrl(resultSet.getString("url"));
            node.setParentId(resultSet.getInt("parent_id"));
            nodes.add(node);
        }
        return nodes;
    }

    public EntitySprite getById(int id) throws Exception {
        String sql = "SELECT * FROM Sprite WHERE id = " + id + ";";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return createNode(resultSet);
    }



    public EntitySprite getByParentId(int parentId) throws Exception {
        String sql = "SELECT * FROM Sprite WHERE parent_id = " + parentId + ";";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return createNode(resultSet);
    }

    private EntitySprite createNode(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            EntitySprite node = new EntitySprite();
            node.setId(resultSet.getInt("id"));
            node.setName(resultSet.getString("name"));
            node.setUrl(resultSet.getString("url"));
            node.setParentId(resultSet.getInt("parent_id"));
            return node;
        } else {
            return null;
        }
    }

    public void insert(EntitySprite node) throws Exception {
        String sql = "INSERT INTO Sprite (id,name,url,parent_id) " +
                "VALUES (?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int column = 1;
        preparedStatement.setInt(column++, node.getId());
        preparedStatement.setString(column++, node.getName());
        preparedStatement.setString(column++, node.getUrl());
        preparedStatement.setInt(column++, node.getParentId());
        preparedStatement.execute();

    }

    public void update(EntitySprite node) throws Exception {

        String sql = "UPDATE Sprite SET name = ? , url = ? ,parent_id= ? ,parent_id = ? WHERE ID = ?;";
        int column = 1;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(column++, node.getName());
        preparedStatement.setString(column++, node.getUrl());
        preparedStatement.setInt(column++, node.getParentId());
        preparedStatement.setInt(column++, node.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(EntitySprite node) throws Exception {
        String sql = "DELETE FROM Sprite WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, node.getId());
        preparedStatement.execute();
    }

    public void deleteAll() throws Exception {
        connection.createStatement().execute("DELETE FROM Node");
    }

    public int count() {
        int count = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT count(*) FROM Sprite");
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


}
