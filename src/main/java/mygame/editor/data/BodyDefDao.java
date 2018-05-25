package mygame.editor.data;

import com.badlogic.gdx.physics.box2d.BodyDef;
import mygame.editor.data.entities.EntityBody;
import mygame.editor.data.entities.EntityNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 11.05.18.
 */
public class BodyDefDao {

    Connection connection;

    public BodyDefDao(Connection connection) throws Exception {
        this.connection = connection;
        init(connection);
    }

    private void init(Connection connection) throws Exception {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Body " +
                "(id                INTEGER  PRIMARY KEY   AUTOINCREMENT ," +
                "parent_id          INTEGER  ," +
                "type               INTEGER  ," +
                "position_x         REAL ," +
                "position_y         REAL ," +
                "active             INTEGER ," +
                "allow_sleep        INTEGER, " +
                "awake              INTEGER," +
                "bullet             INTEGER," +
                "fixed_rotation     INTEGER," +
                "angle              REAL," +
                "angular_damping    REAL," +
                "angular_velocity   REAL," +
                "gravity_scale      REAL," +
                "linear_velocity_x  REAL," +
                "linear_velocity_y  REAL," +
                "linear_damping     REAL);";
        stmt.executeUpdate(sql);
        stmt.close();

    }

    public List<EntityBody> getAll() throws Exception {
        String sql = "SELECT * FROM Body;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<EntityBody> nodes = new ArrayList<>();
        while (resultSet.next()) {
            EntityBody node = createEntity(resultSet);
            nodes.add(node);
        }
        return nodes;
    }

    public EntityBody getByParentId(long parendId) throws Exception {
        String sql = "SELECT * FROM Body WHERE parent_id = " + parendId + ";";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            EntityBody node = createEntity(resultSet);
            return node;
        }else{
            return null;
        }
    }

    private EntityBody createEntity(ResultSet resultSet){
        EntityBody node = new EntityBody();
        try {
            node.setId(resultSet.getInt("id"));
            node.setId(resultSet.getInt("parent_id"));
            node.setType(resultSet.getInt("type"));
            node.setPositionX(resultSet.getFloat("position_x"));
            node.setPositionY(resultSet.getFloat("position_y"));
            node.setActive(resultSet.getBoolean("active"));
            node.setAllowSleep(resultSet.getBoolean("allow_sleep"));
            node.setAwake(resultSet.getBoolean("awake"));
            node.setBullet(resultSet.getBoolean("bullet"));
            node.setFixedRotation(resultSet.getBoolean("fixed_rotation"));
            node.setAngle(resultSet.getFloat("angle"));
            node.setAngularDumping(resultSet.getFloat("angular_damping"));
            node.setAngularVelocity(resultSet.getFloat("angular_velocity"));
            node.setGravityScale(resultSet.getFloat("gravity_scale"));
            node.setLinearVelocityX(resultSet.getFloat("linear_velocity_x"));
            node.setLinearVelocityY(resultSet.getFloat("linear_velocity_y"));
            node.setLinearDamping(resultSet.getFloat("linear_damping"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return node;
    }


    public void insert(EntityBody body) throws Exception {
        String sql = "INSERT INTO Body (type," +
                "position_x," +
                "position_y," +
                "active," +
                "allow_sleep," +
                "awake," +
                "bullet," +
                "fixed_rotation," +
                "angle," +
                "angular_damping," +
                "angular_velocity," +
                "gravity_scale," +
                "linear_velocity_x," +
                "linear_velocity_y," +
                "linear_damping) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, body.getType());
        preparedStatement.setFloat(2, body.getPositionX());
        preparedStatement.setFloat(3, body.getPositionY());
        preparedStatement.setBoolean(4, body.isActive());
        preparedStatement.setBoolean(5,body.isAllowSleep());
        preparedStatement.setBoolean(6,body.isAwake());
        preparedStatement.setBoolean(7,body.isBullet());
        preparedStatement.setBoolean(8,body.isFixedRotation());
        preparedStatement.setFloat(9,body.getAngle());
        preparedStatement.setFloat(10,body.getAngularDumping());
        preparedStatement.setFloat(11,body.getGravityScale());
        preparedStatement.setFloat(12,body.getLinearVelocityX());
        preparedStatement.setFloat(13,body.getLinearVelocityY());
        preparedStatement.setFloat(14,body.getLinearDamping());
        preparedStatement.execute();

    }

    public void update(EntityBody body) throws Exception {

        String sql = "UPDATE Body SET " +
                "type = ? ," +
                "position_x = ?," +
                "position_y = ?," +
                "active = ?," +
                "allow_sleep = ?," +
                "awake = ?," +
                "bullet = ?," +
                "fixed_rotation = ?," +
                "angle = ?," +
                "angular_damping = ?," +
                "angular_velocity = ?," +
                "gravity_scale = ?," +
                "linear_velocity_x = ?," +
                "linear_velocity_y = ?," +
                "linear_damping  = ? " +
                "WHERE ID = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, body.getType());
        preparedStatement.setFloat(2, body.getPositionX());
        preparedStatement.setFloat(3, body.getPositionY());
        preparedStatement.setBoolean(4, body.isActive());
        preparedStatement.setBoolean(5,body.isAllowSleep());
        preparedStatement.setBoolean(6,body.isAwake());
        preparedStatement.setBoolean(7,body.isBullet());
        preparedStatement.setBoolean(8,body.isFixedRotation());
        preparedStatement.setFloat(9,body.getAngle());
        preparedStatement.setFloat(10,body.getAngularDumping());
        preparedStatement.setFloat(11,body.getGravityScale());
        preparedStatement.setFloat(12,body.getLinearVelocityX());
        preparedStatement.setFloat(13,body.getLinearVelocityY());
        preparedStatement.setFloat(14,body.getLinearDamping());
        preparedStatement.setInt(15,body.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(EntityBody entityBody) throws Exception {
        String sql = "DELETE FROM Node WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, entityBody.getId());
        preparedStatement.execute();
    }


}
