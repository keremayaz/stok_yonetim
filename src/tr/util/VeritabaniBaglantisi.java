package tr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeritabaniBaglantisi {
    // SQLite veritabanı dosyası proje klasöründe oluşacak
    private static final String URL = "jdbc:sqlite:stok.db";

    public static Connection baglantiGetir() throws SQLException {
        try {
            // SQLite sürücüsünü yükle
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Sürücüsü bulunamadı. Lütfen kütüphaneyi eklediğinizden emin olun.", e);
        }
    }
}
