package tr.dao;

import tr.model.Talep;
import tr.util.VeritabaniBaglantisi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TalepDAO {

    public List<Talep> tumTalepleriGetir() {
        List<Talep> talepler = new ArrayList<>();
        
        // JOIN ile kullanıcı ad ve soyadını çekiyoruz
        String sql = "SELECT t.id, t.kullanici_id, t.aciklama, t.durum, " +
                     "k.ad || ' ' || k.soyad AS kullanici_tam_ad " +
                     "FROM talepler t " +
                     "LEFT JOIN kullanicilar k ON t.kullanici_id = k.id " +
                     "ORDER BY t.id DESC";
        
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                talepler.add(new Talep(
                    rs.getInt("id"),
                    rs.getInt("kullanici_id"),
                    rs.getString("kullanici_tam_ad"), // Ad Soyad
                    rs.getString("aciklama"),
                    rs.getString("durum")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return talepler;
    }

    public void talepEkle(Talep talep) {
        String sql = "INSERT INTO talepler (kullanici_id, aciklama, durum) VALUES (?, ?, ?)";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, talep.getKullaniciId());
            pstmt.setString(2, talep.getAciklama());
            pstmt.setString(3, talep.getDurum());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void talepDurumGuncelle(int id, String yeniDurum) {
        String sql = "UPDATE talepler SET durum = ? WHERE id = ?";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, yeniDurum);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
