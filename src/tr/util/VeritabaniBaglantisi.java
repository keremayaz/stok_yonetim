package tr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeritabaniBaglantisi {
    private static final String URL = "jdbc:postgresql://localhost:5432/stock_db";
    private static final String KULLANICI = "postgres"; // Varsayılan, gerekirse değiştirin
    private static final String SIFRE = "123456789"; // Varsayılan, gerekirse değiştirin

    public static Connection baglantiGetir() throws SQLException {
        try {
            // PostgreSQL sürücüsünü yükle
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, KULLANICI, SIFRE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Sürücüsü bulunamadı.", e);
        }
    }
}
