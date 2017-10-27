package ryhmatyo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ryhmatyo.database.Database;
import ryhmatyo.domain.AnnosRaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
    }
    
    

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from annosRaakaAine where id = ?");
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }
            return new AnnosRaakaAine(rs.getInt("id"), rs.getInt("annos_id"), rs.getInt("raakaAine_id"), rs.getInt("maara"),
                rs.getString("ohje"));
        }
        
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {
        List<AnnosRaakaAine> a = new ArrayList<>();
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from AnnosRaakaAine");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                a.add(new AnnosRaakaAine(rs.getInt("id"), rs.getInt("annos_id"), rs.getInt("raakaAine_id"), rs.getInt("maara"),
                rs.getString("ohje")));
            }
        }
        return a;
    }

    @Override
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine obj) throws SQLException {
        
        AnnosRaakaAine a = findByName(obj.getRaakaAine_id());
        
        if (a != null) {
            try (Connection conn = this.database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("update AnnosRaakaAine set annos_id = ?, raakaAine_id = ?, maara = ?,"
                        + " ohje = ? where id = ?");
                stmt.setInt(1, a.getAnnos_id());
                stmt.setInt(2, a.getRaakaAine_id());
                stmt.setInt(3, a.getMaara());
                stmt.setString(4, a.getOhje());
                stmt.setInt(5, a.getId());
                
            }
        } else {
            try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("insert into AnnosRaakaAine (annos_id, raakaAine_id, maara, ohje) "
                    + "values (?, ?, ?, ?);");
            stmt.setInt(1, obj.getAnnos_id());
            stmt.setInt(2, obj.getRaakaAine_id());
            stmt.setInt(3, obj.getMaara());
            stmt.setString(4, obj.getOhje());
            stmt.executeUpdate();
        }
        }
        
        
        return findOne(obj.getId());
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("delete from AnnosRaakaAine where id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }
    
    public List<String> tulostaAnnoksenOhjeet(Integer key) throws SQLException {
        List<String> ohjeet = new ArrayList<>();
        
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select annosRaakaAine.maara, annosRaakaAine.ohje, raakaAine.nimi from"
                    + " annosRaakaAine, annos, raakaAine where annos.id = ? and annosRaakaAine.annos_id = annos.id and "
                    + "annosRaakaAine.raakaAine_id = raakaAine.id");
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ohjeet.add(rs.getString("nimi") + ", " + rs.getInt("maara") + " " + rs.getString("ohje"));
            }
        }
        return ohjeet;
        
    }
    
    public AnnosRaakaAine findByName(Integer raakaid) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM annosRaakaAine WHERE raakaAine_id = ?");
            stmt.setInt(1, raakaid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new AnnosRaakaAine(rs.getInt("id"), rs.getInt("annos_id"), rs.getInt("raakaAine_id"), rs.getInt("maara"),
                rs.getString("ohje"));
                
            }
        }
    }

}
