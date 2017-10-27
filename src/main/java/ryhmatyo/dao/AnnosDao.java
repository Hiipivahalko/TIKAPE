package ryhmatyo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ryhmatyo.database.Database;
import ryhmatyo.domain.Annos;

public class AnnosDao extends AbstractNamedObjectDao<Annos> {

    public AnnosDao(Database database, String tableName) {
        super(database, tableName);
    }

    @Override
    public Annos createFromRow(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String tarina = "";
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from Annos where id = ?");
            stmt.setInt(1, id);
            ResultSet st = stmt.executeQuery();
            tarina = st.getString("tarina");
        }
        return new Annos(id, rs.getString("nimi"), tarina);
    }

    public void talletaTarina(String tarina, String drinkki) throws SQLException {

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("update Annos set tarina = ? where nimi = ?");
            stmt.setString(1, tarina);
            stmt.setString(2, drinkki);
            stmt.executeUpdate();
        }
    }

}
