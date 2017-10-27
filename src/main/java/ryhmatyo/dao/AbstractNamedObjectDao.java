package ryhmatyo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ryhmatyo.database.Database;
import ryhmatyo.domain.AbstractNamedObject;

public abstract class AbstractNamedObjectDao<T extends AbstractNamedObject> implements Dao<T, Integer> {

    protected Database database;
    protected String tableName;

    public AbstractNamedObjectDao(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public T findOne(Integer key) throws SQLException {
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from " + this.tableName + " where "
                    + "id = ?");
            stmt.setInt(1, key);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return createFromRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error when looking for a row in " + tableName + " with id " + key);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll() throws SQLException {
        List<T> tasks = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement("SELECT id, nimi FROM " + tableName).executeQuery()) {

            while (result.next()) {
                tasks.add(createFromRow(result));
            }
        }

        return tasks;
    }

    @Override
    public T saveOrUpdate(T object) throws SQLException {
        T byName = findByName(object.getNimi());

        if (byName != null) {
            
            
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());

    }
    
    public T findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM " + tableName + " WHERE nimi = ?");
            stmt.setString(1, name);

            try (ResultSet result = stmt.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                return createFromRow(result);
            }
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("delete from " + this.tableName + " where id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

    /*public Integer findbyName(String nimi) throws SQLException {
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from " + this.tableName + " where "
                    + "nimi = ?");
            stmt.setString(1, nimi);
            ResultSet rs = stmt.executeQuery();
            System.out.println("tehdään rs");
            if (!rs.next()) {
                return null;
            }
            Integer i = rs.getInt("id");
            System.out.println("saadaan i");
            return i;
        }
    }*/

    public abstract T createFromRow(ResultSet resultSet) throws SQLException;

}
