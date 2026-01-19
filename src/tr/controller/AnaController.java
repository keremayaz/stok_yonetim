package tr.controller;

import tr.dao.*;
import tr.model.*;
import tr.view.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AnaController {
    private AnaEkran view;
    private Kullanici kullanici;
    
    private UrunDAO urunDAO;
    private KategoriDAO kategoriDAO;
    private TalepDAO talepDAO;
    private HareketDAO hareketDAO;
    private KullaniciDAO kullaniciDAO;
    private RolDAO rolDAO;
    private MusteriDAO musteriDAO;

    private List<Kategori> kategoriListesi; // Cache
    private List<Rol> rolListesi; // Cache
    private List<Urun> urunListesi; // Cache (Sipariş ekranı için)
    private List<Musteri> musteriListesi; // Cache (Sipariş ekranı için)
    
    // Arama için Sorter
    private TableRowSorter<javax.swing.table.TableModel> urunSorter;

    public AnaController(AnaEkran view, Kullanici kullanici) {
        this.view = view;
        this.kullanici = kullanici;
        
        this.urunDAO = new UrunDAO();
        this.kategoriDAO = new KategoriDAO();
        this.talepDAO = new TalepDAO();
        this.hareketDAO = new HareketDAO();
        this.kullaniciDAO = new KullaniciDAO();
        this.rolDAO = new RolDAO();
        this.musteriDAO = new MusteriDAO();

        initView();
        initListeners();
        verileriYukle();
    }

    private void initView() {
        view.setKullaniciBilgi("Kullanıcı: " + kullanici.getAd() + " " + kullanici.getSoyad() + " (" + kullanici.getKullaniciAdi() + ")");
        
        // YETKİLENDİRME (RBAC)
        // 1: Yönetici, 2: Personel, 3: Depocu
        
        if (kullanici.getRolId() == 3) { // Depocu
            // Sadece ürünleri görebilir (düzenleyemez) ve talep oluşturabilir
            view.setUrunYonetimiAktif(false); // Ekle/Sil/Düzenle butonlarını pasif yap
            view.setPersonelSekmesiGorunur(false); // Personel sekmesini gizle
            
            // YENİ KISITLAMALAR
            view.setSiparisOlusturmaAktif(false); // Sipariş oluşturamaz
            view.setMusteriYonetimiAktif(false); // Müşteri ekleyemez/düzenleyemez
        } 
        else if (kullanici.getRolId() == 2) { // Personel
            // Personel yönetimi hariç her şeyi yapabilir
            view.setPersonelSekmesiGorunur(false); // Personel sekmesini gizle
        }
        // Yönetici (1) için kısıtlama yok
        
        // ARAMA SORTER KURULUMU
        urunSorter = new TableRowSorter<>(view.getUrunTableModel());
        view.getTblUrunler().setRowSorter(urunSorter);
    }

    private void initListeners() {
        // Ürün İşlemleri
        view.addUrunEkleListener(e -> urunEkle());
        view.addUrunDuzenleListener(e -> urunDuzenle());
        view.addUrunSilListener(e -> urunSil());
        view.addUrunYenileListener(e -> urunleriYukle());

        // Personel İşlemleri
        view.addPersonelEkleListener(e -> personelEkle());
        view.addPersonelDuzenleListener(e -> personelDuzenle());
        view.addPersonelSilListener(e -> personelSil());
        view.addPersonelYenileListener(e -> personelleriYukle());
        
        // Müşteri İşlemleri (YENİ)
        view.addMusteriEkleListener(e -> musteriEkle());
        view.addMusteriDuzenleListener(e -> musteriDuzenle());
        view.addMusteriSilListener(e -> musteriSil());
        view.addMusteriYenileListener(e -> musterileriYukle());

        // Talep İşlemleri
        view.addTalepEkleListener(e -> talepEkle());
        view.addTalepYenileListener(e -> talepleriYukle());

        // Hareket İşlemleri
        view.addHareketEkleListener(e -> hareketEkle());
        view.addHareketYenileListener(e -> hareketleriYukle());
        
        // Çıkış İşlemi
        view.addCikisListener(e -> cikisYap());
        
        // Kategori Ekleme
        view.addKategoriEkleListener(e -> kategoriEkle());
        view.addKategoriDuzenleListener(e -> kategoriDuzenle());
        view.addKategoriSilListener(e -> kategoriSil());
        
        // ÇİFT TIKLAMA (Double Click) İŞLEMLERİ
        view.addUrunTabloMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Çift tıklama
                    if (kullanici.getRolId() != 3) {
                        urunDuzenle();
                    } else {
                        JOptionPane.showMessageDialog(view, "Bu işlem için yetkiniz yok!", "Yetki Hatası", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        
        view.addPersonelTabloMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (kullanici.getRolId() == 1) {
                        personelDuzenle();
                    }
                }
            }
        });
        
        view.addTalepTabloMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (kullanici.getRolId() == 1) {
                        talepDurumDegistir();
                    }
                }
            }
        });
        
        view.addMusteriTabloMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (kullanici.getRolId() != 3) { // Depocu hariç herkes düzenleyebilir
                        musteriDuzenle();
                    }
                }
            }
        });
        
        // ARAMA KUTUSU DİNLEYİCİSİ (YENİ)
        view.getTxtUrunAra().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrele(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrele(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrele(); }
            
            private void filtrele() {
                String text = view.getTxtUrunAra().getText();
                if (text.trim().length() == 0) {
                    urunSorter.setRowFilter(null);
                } else {
                    // (?i) -> Case-insensitive (Büyük/küçük harf duyarsız)
                    urunSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
    }
    
    private void cikisYap() {
        int confirm = JOptionPane.showConfirmDialog(view, "Çıkış yapmak istediğinize emin misiniz?", "Çıkış", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose(); // Ana ekranı kapat
            
            // Giriş ekranını yeniden aç
            GirisEkrani girisEkrani = new GirisEkrani();
            new GirisController(girisEkrani, new KullaniciDAO());
            girisEkrani.ekranGoster();
        }
    }

    private void verileriYukle() {
        kategorileriYukle();
        rolleriYukle();
        urunleriYukle();
        musterileriYukle();
        if (kullanici.getRolId() == 1) { 
            personelleriYukle();
        }
        talepleriYukle();
        hareketleriYukle();
    }

    // --- KATEGORİ İŞLEMLERİ ---
    private void kategorileriYukle() {
        new SwingWorker<List<Kategori>, Void>() {
            @Override
            protected List<Kategori> doInBackground() throws Exception {
                return kategoriDAO.tumKategorileriGetir();
            }

            @Override
            protected void done() {
                try {
                    kategoriListesi = get();
                    DefaultMutableTreeNode root = view.getRootNode();
                    root.removeAllChildren();
                    
                    for (Kategori kat : kategoriListesi) {
                        root.add(new DefaultMutableTreeNode(kat));
                    }
                    ((DefaultTreeModel) view.getTreeKategoriler().getModel()).reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void kategoriEkle() {
        String yeniKategoriAdi = JOptionPane.showInputDialog(view, "Yeni Kategori Adı:", "Kategori Ekle", JOptionPane.PLAIN_MESSAGE);
        
        if (yeniKategoriAdi != null && !yeniKategoriAdi.trim().isEmpty()) {
            kategoriDAO.kategoriEkle(yeniKategoriAdi.trim());
            kategorileriYukle(); // Ağacı yenile
            JOptionPane.showMessageDialog(view, "Kategori eklendi.");
        }
    }
    
    private void kategoriDuzenle() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) view.getTreeKategoriler().getLastSelectedPathComponent();
        
        if (selectedNode == null || !selectedNode.isLeaf()) {
            JOptionPane.showMessageDialog(view, "Lütfen düzenlemek için bir kategori seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Object userObject = selectedNode.getUserObject();
        if (userObject instanceof Kategori) {
            Kategori seciliKategori = (Kategori) userObject;
            String yeniAd = JOptionPane.showInputDialog(view, "Kategori Adı:", seciliKategori.getAd());
            
            if (yeniAd != null && !yeniAd.trim().isEmpty()) {
                seciliKategori.setAd(yeniAd.trim());
                kategoriDAO.kategoriGuncelle(seciliKategori);
                kategorileriYukle();
                urunleriYukle(); // Ürün tablosundaki kategori isimleri de güncellensin
                JOptionPane.showMessageDialog(view, "Kategori güncellendi.");
            }
        }
    }
    
    private void kategoriSil() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) view.getTreeKategoriler().getLastSelectedPathComponent();
        
        if (selectedNode == null || !selectedNode.isLeaf()) {
            JOptionPane.showMessageDialog(view, "Lütfen silmek için bir kategori seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Object userObject = selectedNode.getUserObject();
        if (userObject instanceof Kategori) {
            Kategori seciliKategori = (Kategori) userObject;
            int confirm = JOptionPane.showConfirmDialog(view, 
                "'" + seciliKategori.getAd() + "' kategorisini silmek istediğinize emin misiniz?\nBu kategoriye ait ürünler silinmeyecek ancak kategorisiz kalacaktır.", 
                "Kategori Sil", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                kategoriDAO.kategoriSil(seciliKategori.getId());
                kategorileriYukle();
                urunleriYukle();
                JOptionPane.showMessageDialog(view, "Kategori silindi.");
            }
        }
    }

    // --- ROL İŞLEMLERİ ---
    private void rolleriYukle() {
        new SwingWorker<List<Rol>, Void>() {
            @Override
            protected List<Rol> doInBackground() throws Exception {
                return rolDAO.tumRolleriGetir();
            }

            @Override
            protected void done() {
                try {
                    rolListesi = get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    // --- ÜRÜN İŞLEMLERİ ---
    private void urunleriYukle() {
        new SwingWorker<List<Urun>, Void>() {
            @Override
            protected List<Urun> doInBackground() throws Exception {
                return urunDAO.tumUrunleriGetir();
            }

            @Override
            protected void done() {
                try {
                    urunListesi = get(); // Listeyi cache'e al
                    view.getUrunTableModel().setRowCount(0);
                    
                    double toplamSermaye = 0;
                    Map<String, Integer> kategoriStokMap = new HashMap<>();
                    
                    for (Urun u : urunListesi) {
                        view.getUrunTableModel().addRow(new Object[]{
                            u.getId(), 
                            u.getAd(), 
                            u.getKategoriAd(), 
                            u.getStok(), 
                            u.getFiyat()
                        });
                        
                        toplamSermaye += (u.getStok() * u.getFiyat());
                        
                        String katAd = u.getKategoriAd() != null ? u.getKategoriAd() : "Diğer";
                        kategoriStokMap.put(katAd, kategoriStokMap.getOrDefault(katAd, 0) + u.getStok());
                    }
                    
                    view.setToplamUrunCesidi(urunListesi.size());
                    view.setToplamSermaye(toplamSermaye);
                    
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : kategoriStokMap.entrySet()) {
                        sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" adet\n");
                    }
                    view.setKategoriStokBilgisi(sb.toString());
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void urunEkle() {
        if (kategoriListesi == null || kategoriListesi.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Kategoriler yüklenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UrunDialog dialog = new UrunDialog(view, kategoriListesi, null);
        dialog.setVisible(true);

        if (dialog.isKaydetTiklandi()) {
            Urun yeniUrun = dialog.getUrun();
            urunDAO.urunEkle(yeniUrun);
            urunleriYukle();
            JOptionPane.showMessageDialog(view, "Ürün başarıyla eklendi.");
        }
    }

    private void urunDuzenle() {
        int selectedRow = view.getTblUrunler().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Lütfen düzenlenecek bir ürün seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Sorter kullanıldığı için model index'ini dönüştürmemiz lazım
        int modelRow = view.getTblUrunler().convertRowIndexToModel(selectedRow);

        int id = (int) view.getUrunTableModel().getValueAt(modelRow, 0);
        String ad = (String) view.getUrunTableModel().getValueAt(modelRow, 1);
        
        String kategoriAdi = (String) view.getUrunTableModel().getValueAt(modelRow, 2);
        int katId = -1;
        if (kategoriListesi != null) {
            for (Kategori k : kategoriListesi) {
                if (k.getAd().equals(kategoriAdi)) {
                    katId = k.getId();
                    break;
                }
            }
        }
        
        int stok = (int) view.getUrunTableModel().getValueAt(modelRow, 3);
        double fiyat = (double) view.getUrunTableModel().getValueAt(modelRow, 4);

        Urun seciliUrun = new Urun(id, ad, katId, stok, fiyat);
        
        UrunDialog dialog = new UrunDialog(view, kategoriListesi, seciliUrun);
        dialog.setVisible(true);

        if (dialog.isKaydetTiklandi()) {
            Urun guncelUrun = dialog.getUrun();
            guncelUrun.setId(id); // ID'yi koru
            urunDAO.urunGuncelle(guncelUrun);
            urunleriYukle();
            JOptionPane.showMessageDialog(view, "Ürün güncellendi.");
        }
    }

    private void urunSil() {
        int selectedRow = view.getTblUrunler().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Lütfen silinecek bir ürün seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Sorter kullanıldığı için model index'ini dönüştürmemiz lazım
        int modelRow = view.getTblUrunler().convertRowIndexToModel(selectedRow);

        int id = (int) view.getUrunTableModel().getValueAt(modelRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Bu ürünü silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            urunDAO.urunSil(id);
            urunleriYukle();
        }
    }
    
    // --- MÜŞTERİ İŞLEMLERİ ---
    private void musterileriYukle() {
        new SwingWorker<List<Musteri>, Void>() {
            @Override
            protected List<Musteri> doInBackground() throws Exception {
                return musteriDAO.tumMusterileriGetir();
            }

            @Override
            protected void done() {
                try {
                    musteriListesi = get();
                    view.getMusteriTableModel().setRowCount(0);
                    for (Musteri m : musteriListesi) {
                        view.getMusteriTableModel().addRow(new Object[]{
                            m.getId(), m.getAdSoyad(), m.getTelefon(), m.getAdres()
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void musteriEkle() {
        MusteriDialog dialog = new MusteriDialog(view, null);
        dialog.setVisible(true);
        
        if (dialog.isKaydetTiklandi()) {
            musteriDAO.musteriEkle(dialog.getMusteri());
            musterileriYukle();
            JOptionPane.showMessageDialog(view, "Müşteri eklendi.");
        }
    }
    
    private void musteriDuzenle() {
        int selectedRow = view.getTblMusteriler().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Lütfen düzenlenecek bir müşteri seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) view.getTblMusteriler().getValueAt(selectedRow, 0);
        String adSoyad = (String) view.getTblMusteriler().getValueAt(selectedRow, 1);
        String telefon = (String) view.getTblMusteriler().getValueAt(selectedRow, 2);
        String adres = (String) view.getTblMusteriler().getValueAt(selectedRow, 3);
        
        Musteri seciliMusteri = new Musteri(id, adSoyad, telefon, adres);
        MusteriDialog dialog = new MusteriDialog(view, seciliMusteri);
        dialog.setVisible(true);
        
        if (dialog.isKaydetTiklandi()) {
            Musteri guncelMusteri = dialog.getMusteri();
            guncelMusteri.setId(id);
            musteriDAO.musteriGuncelle(guncelMusteri);
            musterileriYukle();
            JOptionPane.showMessageDialog(view, "Müşteri güncellendi.");
        }
    }
    
    private void musteriSil() {
        int selectedRow = view.getTblMusteriler().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Lütfen silinecek bir müşteri seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) view.getTblMusteriler().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Bu müşteriyi silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            musteriDAO.musteriSil(id);
            musterileriYukle();
        }
    }

    // --- PERSONEL İŞLEMLERİ ---
    private void personelleriYukle() {
        new SwingWorker<List<Kullanici>, Void>() {
            @Override
            protected List<Kullanici> doInBackground() throws Exception {
                return kullaniciDAO.tumKullanicilariGetir();
            }

            @Override
            protected void done() {
                try {
                    List<Kullanici> kullanicilar = get();
                    view.getPersonelTableModel().setRowCount(0);
                    for (Kullanici k : kullanicilar) {
                        view.getPersonelTableModel().addRow(new Object[]{
                            k.getId(), k.getAd(), k.getSoyad(), k.getKullaniciAdi(), k.getRolAd() // Rol ID yerine Rol Adı
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void personelEkle() {
        if (rolListesi == null || rolListesi.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Roller yüklenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        KullaniciDialog dialog = new KullaniciDialog(view, rolListesi, null);
        dialog.setVisible(true);

        if (dialog.isKaydetTiklandi()) {
            Kullanici yeniKullanici = dialog.getKullanici();
            kullaniciDAO.kullaniciEkle(yeniKullanici);
            personelleriYukle();
            JOptionPane.showMessageDialog(view, "Personel başarıyla eklendi.");
        }
    }

    private void personelDuzenle() {
        int selectedRow = view.getTblPersonel().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Lütfen düzenlenecek bir personel seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) view.getTblPersonel().getValueAt(selectedRow, 0);
        String ad = (String) view.getTblPersonel().getValueAt(selectedRow, 1);
        String soyad = (String) view.getTblPersonel().getValueAt(selectedRow, 2);
        String kAdi = (String) view.getTblPersonel().getValueAt(selectedRow, 3);
        
        // Rol Adı'ndan Rol ID'yi bulmamız lazım (Düzenleme penceresi için)
        String rolAdi = (String) view.getTblPersonel().getValueAt(selectedRow, 4);
        int rolId = -1;
        if (rolListesi != null) {
            for (Rol r : rolListesi) {
                if (r.getAd().equals(rolAdi)) {
                    rolId = r.getId();
                    break;
                }
            }
        }

        // Şifreyi güvenlik nedeniyle tablodan almıyoruz, boş gönderiyoruz (değiştirilmezse eski kalır mantığı eklenebilir ama şimdilik basit tutalım)
        Kullanici seciliKullanici = new Kullanici(id, ad, soyad, kAdi, "", rolId);
        
        KullaniciDialog dialog = new KullaniciDialog(view, rolListesi, seciliKullanici);
        dialog.setVisible(true);

        if (dialog.isKaydetTiklandi()) {
            Kullanici guncelKullanici = dialog.getKullanici();
            guncelKullanici.setId(id);
            kullaniciDAO.kullaniciGuncelle(guncelKullanici);
            personelleriYukle();
            JOptionPane.showMessageDialog(view, "Personel güncellendi.");
        }
    }

    private void personelSil() {
        int selectedRow = view.getTblPersonel().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Lütfen silinecek bir personel seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) view.getTblPersonel().getValueAt(selectedRow, 0);
        
        // Kendini silmeyi engelle
        if (id == kullanici.getId()) {
            JOptionPane.showMessageDialog(view, "Kendi hesabınızı silemezsiniz!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, "Bu personeli silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            kullaniciDAO.kullaniciSil(id);
            personelleriYukle();
        }
    }

    // --- TALEP İŞLEMLERİ ---
    private void talepleriYukle() {
        new SwingWorker<List<Talep>, Void>() {
            @Override
            protected List<Talep> doInBackground() throws Exception {
                return talepDAO.tumTalepleriGetir();
            }

            @Override
            protected void done() {
                try {
                    List<Talep> talepler = get();
                    view.getTalepTableModel().setRowCount(0);
                    for (Talep t : talepler) {
                        view.getTalepTableModel().addRow(new Object[]{
                            t.getId(), 
                            t.getKullaniciAdSoyad(), // Artık ID yerine İsim
                            t.getAciklama(), 
                            t.getDurum()
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void talepEkle() {
        TalepDialog dialog = new TalepDialog(view);
        dialog.setVisible(true);

        if (dialog.isGonderTiklandi()) {
            Talep yeniTalep = new Talep();
            yeniTalep.setKullaniciId(kullanici.getId());
            yeniTalep.setAciklama(dialog.getAciklama());
            yeniTalep.setDurum("Beklemede");
            
            talepDAO.talepEkle(yeniTalep);
            talepleriYukle();
            JOptionPane.showMessageDialog(view, "Talep oluşturuldu.");
        }
    }
    
    private void talepDurumDegistir() {
        int selectedRow = view.getTblTalepler().getSelectedRow();
        if (selectedRow == -1) return;

        int id = (int) view.getTblTalepler().getValueAt(selectedRow, 0);
        String mevcutDurum = (String) view.getTblTalepler().getValueAt(selectedRow, 3);
        
        String[] secenekler = {"Beklemede", "Onaylandı", "Reddedildi"};
        
        String yeniDurum = (String) JOptionPane.showInputDialog(
            view, 
            "Talep Durumunu Seçiniz:", 
            "Durum Güncelle", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            secenekler, 
            mevcutDurum
        );
        
        if (yeniDurum != null && !yeniDurum.equals(mevcutDurum)) {
            talepDAO.talepDurumGuncelle(id, yeniDurum);
            talepleriYukle();
            JOptionPane.showMessageDialog(view, "Talep durumu güncellendi.");
        }
    }

    // --- HAREKET İŞLEMLERİ ---
    private void hareketleriYukle() {
        new SwingWorker<List<Hareket>, Void>() {
            @Override
            protected List<Hareket> doInBackground() throws Exception {
                return hareketDAO.tumHareketleriGetir();
            }

            @Override
            protected void done() {
                try {
                    List<Hareket> hareketler = get();
                    view.getHareketTableModel().setRowCount(0);
                    for (Hareket h : hareketler) {
                        view.getHareketTableModel().addRow(new Object[]{
                            h.getId(), 
                            h.getUrunAd(), // Artık ID yerine İsim
                            h.getMusteriAdSoyad(), // Artık ID yerine İsim
                            h.getAdet(), 
                            h.getTarih()
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void hareketEkle() {
        if (urunListesi == null || urunListesi.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ürünler yüklenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (musteriListesi == null || musteriListesi.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Müşteriler yüklenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HareketDialog dialog = new HareketDialog(view, urunListesi, musteriListesi);
        dialog.setVisible(true);

        if (dialog.isKaydetTiklandi()) {
            Urun seciliUrun = dialog.getSeciliUrun();
            Musteri seciliMusteri = dialog.getSeciliMusteri();
            int adet = dialog.getAdet();
            
            // 1. Hareketi Kaydet
            Hareket yeniHareket = new Hareket();
            yeniHareket.setUrunId(seciliUrun.getId());
            yeniHareket.setMusteriId(seciliMusteri.getId());
            yeniHareket.setAdet(adet);
            yeniHareket.setTarih(new Timestamp(System.currentTimeMillis()));
            
            hareketDAO.hareketEkle(yeniHareket);
            
            // 2. Stoktan Düş
            urunDAO.stokDus(seciliUrun.getId(), adet);
            
            // 3. Ekranları Yenile
            hareketleriYukle();
            urunleriYukle(); // Stok güncellendiği için ürün listesini de yenile
            
            JOptionPane.showMessageDialog(view, "Sipariş başarıyla oluşturuldu ve stok güncellendi.");
        }
    }
}
