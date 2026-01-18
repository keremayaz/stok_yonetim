package tr.dao;

import tr.model.Musteri;
import tr.util.VeritabaniBaglantisi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusteriDAO {

    public List<Musteri> tumMusterileriGetir() {
        List<Musteri> musteriler = new ArrayList<>();
        String sql = "SELECT * FROM musteriler ORDER BY ad_soyad";
        
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                musteriler.add(new Musteri(
                    rs.getInt("id"),
                    rs.getString("ad_soyad"),
                    rs.getString("telefon"),
                    rs.getString("adres")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return musteriler;
    }

    public void musteriEkle(Musteri m) {
        String sql = "INSERT INTO musteriler (ad_soyad, telefon, adres) VALUES (?, ?, ?)";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, m.getAdSoyad());
            pstmt.setString(2, m.getTelefon());
            pstmt.setString(3, m.getAdres());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void musteriGuncelle(Musteri m) {
        String sql = "UPDATE musteriler SET ad_soyad = ?, telefon = ?, adres = ? WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, m.getAdSoyad());
            pstmt.setString(2, m.getTelefon());
            pstmt.setString(3, m.getAdres());
            pstmt.setInt(4, m.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void musteriSil(int id) {
        String sql = "DELETE FROM musteriler WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
