package tr.controller;

import tr.dao.KullaniciDAO;
import tr.model.Kullanici;
import tr.view.GirisEkrani;
import tr.view.AnaEkran;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class GirisController {
    private GirisEkrani view;
    private KullaniciDAO model;

    public GirisController(GirisEkrani view, KullaniciDAO model) {
        this.view = view;
        this.model = model;

        this.view.setGirisButtonListener(new GirisListener());
    }

    class GirisListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String kullaniciAdi = view.getKullaniciAdi();
            String sifre = view.getSifre();

            view.setDurumMesaji("Giriş yapılıyor...");

            // SwingWorker ile arka planda giriş işlemi
            new SwingWorker<Kullanici, Void>() {
                @Override
                protected Kullanici doInBackground() throws Exception {
                    return model.girisYap(kullaniciAdi, sifre);
                }

                @Override
                protected void done() {
                    try {
                        Kullanici kullanici = get();
                        if (kullanici != null) {
                            view.setDurumMesaji("Giriş başarılı!");
                            view.ekranKapat();
                            
                            // Ana ekranı başlat
                            AnaEkran anaEkran = new AnaEkran();
                            new AnaController(anaEkran, kullanici);
                            anaEkran.setVisible(true);
                        } else {
                            view.setDurumMesaji("Hatalı kullanıcı adı veya şifre!");
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        view.setDurumMesaji("Bir hata oluştu: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }.execute();
        }
    }
}
