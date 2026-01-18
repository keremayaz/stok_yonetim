package tr.dao;

import tr.model.Urun;
import tr.util.VeritabaniBaglantisi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UrunDAO {

    public List<Urun> tumUrunleriGetir() {
        List<Urun> urunler = new ArrayList<>();
        // JOIN ile kategori adını çekiyoruz
        String sql = "SELECT u.id, u.ad, u.kategori_id, u.stok, u.fiyat, k.ad AS kategori_adi " +
                     "FROM urunler u " +
                     "LEFT JOIN kategoriler k ON u.kategori_id = k.id " +
                     "ORDER BY u.id";
        
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                urunler.add(new Urun(
                    rs.getInt("id"),
                    rs.getString("ad"),
                    rs.getInt("kategori_id"),
                    rs.getString("kategori_adi"), // Kategori Adı
                    rs.getInt("stok"),
                    rs.getDouble("fiyat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return urunler;
    }

    public void urunEkle(Urun urun) {
        String sql = "INSERT INTO urunler (ad, kategori_id, stok, fiyat) VALUES (?, ?, ?, ?)";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, urun.getAd());
            pstmt.setInt(2, urun.getKategoriId());
            pstmt.setInt(3, urun.getStok());
            pstmt.setDouble(4, urun.getFiyat());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void urunGuncelle(Urun urun) {
        String sql = "UPDATE urunler SET ad = ?, kategori_id = ?, stok = ?, fiyat = ? WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, urun.getAd());
            pstmt.setInt(2, urun.getKategoriId());
            pstmt.setInt(3, urun.getStok());
            pstmt.setDouble(4, urun.getFiyat());
            pstmt.setInt(5, urun.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void urunSil(int id) {
        String sql = "DELETE FROM urunler WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void stokDus(int urunId, int adet) {
        String sql = "UPDATE urunler SET stok = stok - ? WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adet);
            pstmt.setInt(2, urunId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
