package tr.dao;

import tr.model.Kategori;
import tr.util.VeritabaniBaglantisi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {

    public List<Kategori> tumKategorileriGetir() {
        List<Kategori> kategoriler = new ArrayList<>();
        String sql = "SELECT * FROM kategoriler ORDER BY ad";
        
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                kategoriler.add(new Kategori(
                    rs.getInt("id"),
                    rs.getString("ad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kategoriler;
    }

    public void kategoriEkle(String ad) {
        String sql = "INSERT INTO kategoriler (ad) VALUES (?)";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ad);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void kategoriGuncelle(Kategori k) {
        String sql = "UPDATE kategoriler SET ad = ? WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, k.getAd());
            pstmt.setInt(2, k.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void kategoriSil(int id) {
        String sql = "DELETE FROM kategoriler WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
