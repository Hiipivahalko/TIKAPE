
package ryhmatyo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ryhmatyo.database.Database;
import ryhmatyo.domain.RaakaAine;


public class RaakaAineDao extends AbstractNamedObjectDao<RaakaAine>{

    public RaakaAineDao(Database database, String tableName) {
        super(database, tableName);
    }

    @Override
    public RaakaAine createFromRow(ResultSet rs) throws SQLException {
        return new RaakaAine(rs.getInt("id"), rs.getString("nimi"));
    }
    
    public List<RaakaAine> AnnoksenAineOsat(Integer key) throws SQLException {
        List<RaakaAine> r = new ArrayList<>();
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select raakaAine.id, raakaAine.nimi from raakaAine, Annos, AnnosRaakaAine"
                    + " where raakaAine.id = annosRaakaAine.raakaAine_id "
                    + "and annos.id = annosRaakaAine.annos_id and "
                    + "annos.id = ?");
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                r.add(createFromRow(rs));
            }
        }
        
        return r;
    }
    
    
    
}
