package tr.dao;

import tr.model.Kullanici;
import tr.util.VeritabaniBaglantisi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KullaniciDAO {

    public Kullanici girisYap(String kullaniciAdi, String sifre) {
        String sql = "SELECT * FROM kullanicilar WHERE kullanici_adi = ? AND sifre = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kullaniciAdi);
            pstmt.setString(2, sifre);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKullanici(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Kullanici> tumKullanicilariGetir() {
        List<Kullanici> kullanicilar = new ArrayList<>();
        // JOIN ile rol adını çekiyoruz
        String sql = "SELECT k.id, k.ad, k.soyad, k.kullanici_adi, k.sifre, k.rol_id, r.ad AS rol_adi " +
                     "FROM kullanicilar k " +
                     "LEFT JOIN roller r ON k.rol_id = r.id " +
                     "ORDER BY k.id";
        
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                kullanicilar.add(new Kullanici(
                    rs.getInt("id"),
                    rs.getString("ad"),
                    rs.getString("soyad"),
                    rs.getString("kullanici_adi"),
                    rs.getString("sifre"),
                    rs.getInt("rol_id"),
                    rs.getString("rol_adi") // Rol Adı
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kullanicilar;
    }

    public void kullaniciEkle(Kullanici k) {
        String sql = "INSERT INTO kullanicilar (ad, soyad, kullanici_adi, sifre, rol_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, k.getAd());
            pstmt.setString(2, k.getSoyad());
            pstmt.setString(3, k.getKullaniciAdi());
            pstmt.setString(4, k.getSifre());
            pstmt.setInt(5, k.getRolId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void kullaniciGuncelle(Kullanici k) {
        String sql = "UPDATE kullanicilar SET ad = ?, soyad = ?, kullanici_adi = ?, sifre = ?, rol_id = ? WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, k.getAd());
            pstmt.setString(2, k.getSoyad());
            pstmt.setString(3, k.getKullaniciAdi());
            pstmt.setString(4, k.getSifre());
            pstmt.setInt(5, k.getRolId());
            pstmt.setInt(6, k.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void kullaniciSil(int id) {
        String sql = "DELETE FROM kullanicilar WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Kullanici mapResultSetToKullanici(ResultSet rs) throws SQLException {
        return new Kullanici(
            rs.getInt("id"),
            rs.getString("ad"),
            rs.getString("soyad"),
            rs.getString("kullanici_adi"),
            rs.getString("sifre"),
            rs.getInt("rol_id")
        );
    }
}
