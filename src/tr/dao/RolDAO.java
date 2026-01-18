package tr.dao;

import tr.model.Rol;
import tr.util.VeritabaniBaglantisi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    public List<Rol> tumRolleriGetir() {
        List<Rol> roller = new ArrayList<>();
        String sql = "SELECT * FROM roller";
        
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                roller.add(new Rol(
                    rs.getInt("id"),
                    rs.getString("ad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roller;
    }
}
