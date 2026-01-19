package tr.dao;

import tr.model.Hareket;
import tr.util.VeritabaniBaglantisi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HareketDAO {

    public List<Hareket> tumHareketleriGetir() {
        List<Hareket> hareketler = new ArrayList<>();
        
        // JOIN sorgusu ile ilişkili tablolardan isimleri çekiyoruz
        String sql = "SELECT h.id, h.urun_id, h.musteri_id, h.adet, h.tarih, " +
                     "u.ad AS urun_adi, m.ad_soyad AS musteri_adi " +
                     "FROM hareketler h " +
                     "LEFT JOIN urunler u ON h.urun_id = u.id " +
                     "LEFT JOIN musteriler m ON h.musteri_id = m.id " +
                     "ORDER BY h.tarih DESC";
        
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                // SQLite tarihleri bazen string, bazen long olarak dönebilir.
                // getTimestamp genellikle string formatını ("yyyy-MM-dd HH:mm:ss") veya long'u destekler.
                Timestamp tarih = null;
                try {
                    tarih = rs.getTimestamp("tarih");
                } catch (Exception e) {
                    // Eğer format hatası olursa (örn: sadece string gelirse), manuel parse denenebilir
                    // Şimdilik null bırakıyoruz veya o anı atıyoruz
                    tarih = new Timestamp(System.currentTimeMillis());
                }

                hareketler.add(new Hareket(
                    rs.getInt("id"),
                    rs.getInt("urun_id"),
                    rs.getString("urun_adi"), // Ürün Adı
                    rs.getInt("musteri_id"),
                    rs.getString("musteri_adi"), // Müşteri Adı
                    rs.getInt("adet"),
                    tarih
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hareketler;
    }

    public void hareketEkle(Hareket hareket) {
        String sql = "INSERT INTO hareketler (urun_id, musteri_id, adet, tarih) VALUES (?, ?, ?, ?)";
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hareket.getUrunId());
            pstmt.setInt(2, hareket.getMusteriId());
            pstmt.setInt(3, hareket.getAdet());
            // SQLite için Timestamp'i string veya long olarak kaydetmek daha güvenli olabilir
            // Ancak JDBC sürücüsü genellikle bunu halleder.
            pstmt.setTimestamp(4, hareket.getTarih());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
