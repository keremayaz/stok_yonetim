import tr.controller.GirisController;
import tr.dao.KullaniciDAO;
import tr.util.VeritabaniBaslatici;
import tr.view.GirisEkrani;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        // 1. Veritabanını Başlat (Tabloları oluştur)
        VeritabaniBaslatici.baslat();

        // 2. Arayüzü Başlat
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", java.awt.Font.PLAIN, 14));
            } catch (Exception e) {
                e.printStackTrace();
            }

            GirisEkrani girisEkrani = new GirisEkrani();
            KullaniciDAO kullaniciDAO = new KullaniciDAO();
            new GirisController(girisEkrani, kullaniciDAO);
            
            girisEkrani.ekranGoster();
        });
    }
    
    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
