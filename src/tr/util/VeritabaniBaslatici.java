package tr.util;

import java.sql.Connection;
import java.sql.Statement;

public class VeritabaniBaslatici {

    public static void baslat() {
        try (Connection conn = VeritabaniBaglantisi.baglantiGetir();
             Statement stmt = conn.createStatement()) {

            // 1. TABLOLARI OLUŞTUR (Eğer yoksa)
            String sqlTablolar = """
                CREATE TABLE IF NOT EXISTS roller (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT NOT NULL UNIQUE
                );

                CREATE TABLE IF NOT EXISTS kategoriler (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT NOT NULL UNIQUE
                );

                CREATE TABLE IF NOT EXISTS musteriler (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad_soyad TEXT NOT NULL,
                    telefon TEXT,
                    adres TEXT
                );

                CREATE TABLE IF NOT EXISTS kullanicilar (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT NOT NULL,
                    soyad TEXT NOT NULL,
                    kullanici_adi TEXT NOT NULL UNIQUE,
                    sifre TEXT NOT NULL,
                    rol_id INTEGER REFERENCES roller(id)
                );

                CREATE TABLE IF NOT EXISTS urunler (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT NOT NULL,
                    kategori_id INTEGER REFERENCES kategoriler(id),
                    stok INTEGER DEFAULT 0,
                    fiyat REAL
                );

                CREATE TABLE IF NOT EXISTS hareketler (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    urun_id INTEGER REFERENCES urunler(id),
                    musteri_id INTEGER REFERENCES musteriler(id),
                    adet INTEGER NOT NULL,
                    tarih TEXT DEFAULT CURRENT_TIMESTAMP
                );

                CREATE TABLE IF NOT EXISTS talepler (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    kullanici_id INTEGER REFERENCES kullanicilar(id),
                    aciklama TEXT NOT NULL,
                    durum TEXT DEFAULT 'Beklemede'
                );
            """;
            stmt.executeUpdate(sqlTablolar);

            // 2. VERİLERİ YÜKLE (Senin yedeğindeki veriler)

            // ROLLER
            stmt.executeUpdate("INSERT OR IGNORE INTO roller (id, ad) VALUES (1, 'Yönetici'), (2, 'Personel'), (3, 'Depo Sorumlusu');");

            // KATEGORİLER
            stmt.executeUpdate("INSERT OR IGNORE INTO kategoriler (id, ad) VALUES (1, 'Bilgisayar'), (2, 'Kırtasiye'), (3, 'Beyaz Eşya'), (4, 'Telefon');");

            // MÜŞTERİLER
            stmt.executeUpdate("INSERT OR IGNORE INTO musteriler (id, ad_soyad, telefon, adres) VALUES " +
                    "(1, 'Ahmet Yılmaz', '05551112233', 'İstanbul'), " +
                    "(2, 'Ayşe Demir', '05442223344', 'Ankara'), " +
                    "(3, 'Mehmet Kaya', '05334445566', 'İzmir'), " +
                    "(4, 'Kerem Ayaz', '05441559115', 'İstanbul'), " +
                    "(5, 'Fahri Gültekin', '05432175565', 'Köy');");

            // KULLANICILAR
            stmt.executeUpdate("INSERT OR IGNORE INTO kullanicilar (id, ad, soyad, kullanici_adi, sifre, rol_id) VALUES " +
                    "(1, 'Ali', 'Patron', 'admin', '123', 1), " +
                    "(2, 'Veli', 'Çalışkan', 'personel', '123', 2), " +
                    "(3, 'Can', 'Taşıyıcı', 'depocu', '123', 3);");

            // ÜRÜNLER
            stmt.executeUpdate("INSERT OR IGNORE INTO urunler (id, ad, kategori_id, stok, fiyat) VALUES " +
                    "(1, 'Laptop', 1, 19, 25000.00), " +
                    "(2, 'A4 Kağıt', 2, 100, 150.00), " +
                    "(3, 'Buzdolabı', 3, 5, 15000.00), " +
                    "(4, 'Çamaşır Makinesi', 3, 40, 13000.00), " +
                    "(5, 'Iphone 17 Pro Max', 4, 20, 90000.00), " +
                    "(6, 'Bulaşık Makinesi', 3, 9, 17000.00);");

            // TALEPLER
            stmt.executeUpdate("INSERT OR IGNORE INTO talepler (id, kullanici_id, aciklama, durum) VALUES " +
                    "(1, 2, 'Laptop stokları azalıyor.', 'Beklemede'), " +
                    "(2, 3, 'Depo ışıkları bozuldu.', 'Onaylandı'), " +
                    "(3, 2, 'Yeni yazıcı lazım.', 'Reddedildi'), " +
                    "(4, 1, 'Yeni markalar getirlemeli', 'Onaylandı');");

            // HAREKETLER
            stmt.executeUpdate("INSERT OR IGNORE INTO hareketler (id, urun_id, musteri_id, adet, tarih) VALUES " +
                    "(1, 1, 1, 1, '2026-01-18 23:04:26'), " +
                    "(2, 2, 2, 5, '2026-01-18 23:04:26'), " +
                    "(3, 3, 3, 1, '2026-01-18 23:04:26'), " +
                    "(4, 1, 2, 10, '2026-01-18 23:55:43'), " +
                    "(5, 4, 4, 1, '2026-01-19 00:05:19'), " +
                    "(6, 4, 5, 9, '2026-01-19 01:01:48'), " +
                    "(7, 1, 4, 1, '2026-01-19 01:08:32'), " +
                    "(8, 6, 4, 1, '2026-01-19 01:35:58');");

            System.out.println("Veritabanı kontrolü tamamlandı (SQLite).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
