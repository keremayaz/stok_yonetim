package tr.view;

import tr.util.ModernUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class AnaEkran extends JFrame {
    private JTabbedPane tabbedPane;
    
    // Üst Panel Bileşenleri
    private JLabel lblKullaniciBilgi;
    private JButton btnCikis;

    // Ürünler Bileşenleri
    private JTable tblUrunler;
    private DefaultTableModel urunTableModel;
    private JButton btnUrunEkle, btnUrunDuzenle, btnUrunSil, btnUrunYenile;
    private JTextField txtUrunAra;
    
    // İstatistik Paneli Bileşenleri
    private JLabel lblToplamUrunCesidi;
    private JLabel lblToplamSermaye;
    private JTextArea txtKategoriStok;
    
    // Personel Bileşenleri
    private JTable tblPersonel;
    private DefaultTableModel personelTableModel;
    private JButton btnPersonelEkle, btnPersonelDuzenle, btnPersonelSil, btnPersonelYenile;

    // Müşteri Bileşenleri
    private JTable tblMusteriler;
    private DefaultTableModel musteriTableModel;
    private JButton btnMusteriEkle, btnMusteriDuzenle, btnMusteriSil, btnMusteriYenile;

    // Talepler Bileşenleri
    private JTable tblTalepler;
    private DefaultTableModel talepTableModel;
    private JButton btnTalepEkle, btnTalepYenile;

    // Hareketler Bileşenleri
    private JTable tblHareketler;
    private DefaultTableModel hareketTableModel;
    private JButton btnHareketEkle, btnHareketYenile;

    // Kategori Ağacı Bileşenleri
    private JTree treeKategoriler;
    private DefaultMutableTreeNode rootNode;
    private JPopupMenu popupKategori;
    private JMenuItem itemKategoriEkle;
    private JMenuItem itemKategoriDuzenle;
    private JMenuItem itemKategoriSil;

    public AnaEkran() {
        setTitle("Stok Yönetim Sistemi");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(ModernUI.BACKGROUND_COLOR);

        // --- ÜST PANEL ---
        JPanel pnlUst = new JPanel(new BorderLayout());
        pnlUst.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlUst.setBackground(ModernUI.PRIMARY_COLOR);
        
        JLabel lblBaslik = new JLabel("\uD83D\uDCE6 STOK YÖNETİM SİSTEMİ");
        lblBaslik.setFont(ModernUI.FONT_TITLE);
        lblBaslik.setForeground(Color.WHITE);
        pnlUst.add(lblBaslik, BorderLayout.WEST);
        
        // Sağ üst köşe
        JPanel pnlSagUst = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSagUst.setOpaque(false);
        
        lblKullaniciBilgi = new JLabel("Kullanıcı: ");
        lblKullaniciBilgi.setFont(ModernUI.FONT_BOLD);
        lblKullaniciBilgi.setForeground(Color.WHITE);
        lblKullaniciBilgi.setBorder(new EmptyBorder(0, 0, 0, 15));
        
        btnCikis = new JButton("Çıkış Yap");
        ModernUI.styleDangerButton(btnCikis);
        btnCikis.setFont(ModernUI.FONT_SMALL);
        
        pnlSagUst.add(lblKullaniciBilgi);
        pnlSagUst.add(btnCikis);
        
        pnlUst.add(pnlSagUst, BorderLayout.EAST);
        add(pnlUst, BorderLayout.NORTH);

        // --- SOL PANEL (Kategoriler) ---
        rootNode = new DefaultMutableTreeNode("Kategoriler");
        treeKategoriler = new JTree(rootNode);
        treeKategoriler.setRowHeight(25);
        treeKategoriler.setFont(ModernUI.FONT_NORMAL);
        
        // Ağaç İkonlarını Özelleştirme (Opsiyonel)
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) treeKategoriler.getCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        
        JScrollPane scrollTree = new JScrollPane(treeKategoriler);
        scrollTree.setPreferredSize(new Dimension(250, 0));
        scrollTree.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));
        scrollTree.getViewport().setBackground(ModernUI.SURFACE_COLOR);
        add(scrollTree, BorderLayout.WEST);
        
        // Kategori Sağ Tık Menüsü
        popupKategori = new JPopupMenu();
        itemKategoriEkle = new JMenuItem("Yeni Kategori Ekle");
        itemKategoriDuzenle = new JMenuItem("Kategori Düzenle");
        itemKategoriSil = new JMenuItem("Kategori Sil");
        
        popupKategori.add(itemKategoriEkle);
        popupKategori.addSeparator();
        popupKategori.add(itemKategoriDuzenle);
        popupKategori.add(itemKategoriSil);
        
        treeKategoriler.setComponentPopupMenu(popupKategori);

        // --- MERKEZ PANEL (TabbedPane) ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ModernUI.FONT_BOLD);
        tabbedPane.setBackground(ModernUI.BACKGROUND_COLOR);

        // 1. Sekme: Ürünler
        tabbedPane.addTab("Ürün Yönetimi", createUrunPanel());

        // 2. Sekme: Müşteri Yönetimi
        tabbedPane.addTab("Müşteri Yönetimi", createMusteriPanel());

        // 3. Sekme: Personel Yönetimi
        tabbedPane.addTab("Personel Yönetimi", createPersonelPanel());

        // 4. Sekme: Talepler
        tabbedPane.addTab("Talepler", createTalepPanel());

        // 5. Sekme: Hareketler
        tabbedPane.addTab("Stok Hareketleri", createHareketPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createUrunPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // İstatistik Paneli
        JPanel pnlIstatistik = new JPanel(new GridLayout(1, 3, 15, 0));
        pnlIstatistik.setOpaque(false);
        pnlIstatistik.setPreferredSize(new Dimension(0, 120));
        
        pnlIstatistik.add(createStatCard("Toplam Çeşit", lblToplamUrunCesidi = new JLabel("0"), new Color(232, 245, 233)));
        pnlIstatistik.add(createStatCard("Toplam Sermaye", lblToplamSermaye = new JLabel("0.00 TL"), new Color(227, 245, 254)));
        
        // Kategori Stok Kartı (Özel)
        JPanel pnlKutu3 = ModernUI.createCardPanel();
        pnlKutu3.setLayout(new BorderLayout());
        JLabel lblBaslik3 = new JLabel("Kategori Dağılımı");
        lblBaslik3.setFont(ModernUI.FONT_BOLD);
        lblBaslik3.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        txtKategoriStok = new JTextArea();
        txtKategoriStok.setEditable(false);
        txtKategoriStok.setOpaque(false);
        txtKategoriStok.setFont(ModernUI.FONT_SMALL);
        txtKategoriStok.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        pnlKutu3.add(lblBaslik3, BorderLayout.NORTH);
        pnlKutu3.add(new JScrollPane(txtKategoriStok), BorderLayout.CENTER);
        pnlIstatistik.add(pnlKutu3);
        
        panel.add(pnlIstatistik, BorderLayout.NORTH);

        // Alt Panel (Tablo ve Butonlar)
        JPanel pnlAlt = ModernUI.createCardPanel();
        pnlAlt.setLayout(new BorderLayout());
        pnlAlt.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Toolbar
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlToolbar.setBackground(ModernUI.SURFACE_COLOR);
        
        btnUrunEkle = new JButton("Yeni Ürün"); ModernUI.styleButton(btnUrunEkle);
        btnUrunDuzenle = new JButton("Düzenle"); ModernUI.styleButton(btnUrunDuzenle);
        btnUrunSil = new JButton("Sil"); ModernUI.styleDangerButton(btnUrunSil);
        btnUrunYenile = new JButton("Yenile"); ModernUI.styleButton(btnUrunYenile);
        
        pnlToolbar.add(btnUrunEkle);
        pnlToolbar.add(btnUrunDuzenle);
        pnlToolbar.add(btnUrunSil);
        pnlToolbar.add(btnUrunYenile);
        
        pnlToolbar.add(Box.createHorizontalStrut(20));
        pnlToolbar.add(new JLabel("Ara: "));
        txtUrunAra = new JTextField(20);
        ModernUI.styleTextField(txtUrunAra);
        pnlToolbar.add(txtUrunAra);
        
        pnlAlt.add(pnlToolbar, BorderLayout.NORTH);

        // Tablo
        String[] kolonlar = {"ID", "Ürün Adı", "Kategori", "Stok", "Fiyat (TL)"};
        urunTableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblUrunler = new JTable(urunTableModel);
        ModernUI.styleTable(tblUrunler);
        pnlAlt.add(new JScrollPane(tblUrunler), BorderLayout.CENTER);
        
        // İstatistik paneli ile tablo arasına boşluk
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setOpaque(false);
        pnlWrapper.add(Box.createVerticalStrut(15), BorderLayout.NORTH);
        pnlWrapper.add(pnlAlt, BorderLayout.CENTER);
        
        panel.add(pnlWrapper, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Yardımcı Metot: İstatistik Kartı Oluşturucu
    private JPanel createStatCard(String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor); // Hafif renkli arkaplan
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(ModernUI.FONT_BOLD);
        lblTitle.setForeground(ModernUI.TEXT_SECONDARY);
        lblTitle.setBorder(new EmptyBorder(15, 15, 5, 15));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(ModernUI.PRIMARY_COLOR);
        valueLabel.setBorder(new EmptyBorder(0, 15, 15, 15));
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createMusteriPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel pnlCard = ModernUI.createCardPanel();
        pnlCard.setLayout(new BorderLayout());
        
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlToolbar.setBackground(ModernUI.SURFACE_COLOR);
        
        btnMusteriEkle = new JButton("Yeni Müşteri"); ModernUI.styleButton(btnMusteriEkle);
        btnMusteriDuzenle = new JButton("Düzenle"); ModernUI.styleButton(btnMusteriDuzenle);
        btnMusteriSil = new JButton("Sil"); ModernUI.styleDangerButton(btnMusteriSil);
        btnMusteriYenile = new JButton("Yenile"); ModernUI.styleButton(btnMusteriYenile);
        
        pnlToolbar.add(btnMusteriEkle);
        pnlToolbar.add(btnMusteriDuzenle);
        pnlToolbar.add(btnMusteriSil);
        pnlToolbar.add(btnMusteriYenile);
        
        pnlCard.add(pnlToolbar, BorderLayout.NORTH);

        String[] kolonlar = {"ID", "Ad Soyad", "Telefon", "Adres"};
        musteriTableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMusteriler = new JTable(musteriTableModel);
        ModernUI.styleTable(tblMusteriler);
        pnlCard.add(new JScrollPane(tblMusteriler), BorderLayout.CENTER);
        
        panel.add(pnlCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPersonelPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel pnlCard = ModernUI.createCardPanel();
        pnlCard.setLayout(new BorderLayout());
        
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlToolbar.setBackground(ModernUI.SURFACE_COLOR);
        
        btnPersonelEkle = new JButton("Yeni Personel"); ModernUI.styleButton(btnPersonelEkle);
        btnPersonelDuzenle = new JButton("Düzenle"); ModernUI.styleButton(btnPersonelDuzenle);
        btnPersonelSil = new JButton("Sil"); ModernUI.styleDangerButton(btnPersonelSil);
        btnPersonelYenile = new JButton("Yenile"); ModernUI.styleButton(btnPersonelYenile);
        
        pnlToolbar.add(btnPersonelEkle);
        pnlToolbar.add(btnPersonelDuzenle);
        pnlToolbar.add(btnPersonelSil);
        pnlToolbar.add(btnPersonelYenile);
        
        pnlCard.add(pnlToolbar, BorderLayout.NORTH);

        String[] kolonlar = {"ID", "Ad", "Soyad", "Kullanıcı Adı", "Rol ID"};
        personelTableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPersonel = new JTable(personelTableModel);
        ModernUI.styleTable(tblPersonel);
        pnlCard.add(new JScrollPane(tblPersonel), BorderLayout.CENTER);
        
        panel.add(pnlCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTalepPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel pnlCard = ModernUI.createCardPanel();
        pnlCard.setLayout(new BorderLayout());
        
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlToolbar.setBackground(ModernUI.SURFACE_COLOR);
        
        btnTalepEkle = new JButton("Yeni Talep Oluştur"); ModernUI.styleButton(btnTalepEkle);
        btnTalepYenile = new JButton("Yenile"); ModernUI.styleButton(btnTalepYenile);
        
        pnlToolbar.add(btnTalepEkle);
        pnlToolbar.add(btnTalepYenile);
        
        pnlCard.add(pnlToolbar, BorderLayout.NORTH);

        String[] kolonlar = {"ID", "Kullanıcı", "Açıklama", "Durum"};
        talepTableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblTalepler = new JTable(talepTableModel);
        ModernUI.styleTable(tblTalepler);
        pnlCard.add(new JScrollPane(tblTalepler), BorderLayout.CENTER);
        
        panel.add(pnlCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHareketPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel pnlCard = ModernUI.createCardPanel();
        pnlCard.setLayout(new BorderLayout());
        
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlToolbar.setBackground(ModernUI.SURFACE_COLOR);
        
        btnHareketEkle = new JButton("Sipariş Oluştur"); ModernUI.styleButton(btnHareketEkle);
        btnHareketYenile = new JButton("Yenile"); ModernUI.styleButton(btnHareketYenile);
        
        pnlToolbar.add(btnHareketEkle);
        pnlToolbar.add(btnHareketYenile);
        
        pnlCard.add(pnlToolbar, BorderLayout.NORTH);

        String[] kolonlar = {"ID", "Ürün", "Müşteri", "Adet", "Tarih"};
        hareketTableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblHareketler = new JTable(hareketTableModel);
        ModernUI.styleTable(tblHareketler);
        pnlCard.add(new JScrollPane(tblHareketler), BorderLayout.CENTER);
        
        panel.add(pnlCard, BorderLayout.CENTER);
        return panel;
    }

    // Getter ve Setterlar
    public void setKullaniciBilgi(String bilgi) { lblKullaniciBilgi.setText(bilgi); }
    
    public DefaultTableModel getUrunTableModel() { return urunTableModel; }
    public JTable getTblUrunler() { return tblUrunler; }
    
    public DefaultTableModel getPersonelTableModel() { return personelTableModel; }
    public JTable getTblPersonel() { return tblPersonel; }
    
    public DefaultTableModel getMusteriTableModel() { return musteriTableModel; }
    public JTable getTblMusteriler() { return tblMusteriler; }
    
    public DefaultTableModel getTalepTableModel() { return talepTableModel; }
    public JTable getTblTalepler() { return tblTalepler; }
    
    public DefaultTableModel getHareketTableModel() { return hareketTableModel; }
    
    public DefaultMutableTreeNode getRootNode() { return rootNode; }
    public JTree getTreeKategoriler() { return treeKategoriler; }
    
    public JTextField getTxtUrunAra() { return txtUrunAra; }

    // Listener Ekleme Metotları
    public void addUrunEkleListener(ActionListener l) { btnUrunEkle.addActionListener(l); }
    public void addUrunDuzenleListener(ActionListener l) { btnUrunDuzenle.addActionListener(l); }
    public void addUrunSilListener(ActionListener l) { btnUrunSil.addActionListener(l); }
    public void addUrunYenileListener(ActionListener l) { btnUrunYenile.addActionListener(l); }
    
    public void addPersonelEkleListener(ActionListener l) { btnPersonelEkle.addActionListener(l); }
    public void addPersonelDuzenleListener(ActionListener l) { btnPersonelDuzenle.addActionListener(l); }
    public void addPersonelSilListener(ActionListener l) { btnPersonelSil.addActionListener(l); }
    public void addPersonelYenileListener(ActionListener l) { btnPersonelYenile.addActionListener(l); }
    
    public void addMusteriEkleListener(ActionListener l) { btnMusteriEkle.addActionListener(l); }
    public void addMusteriDuzenleListener(ActionListener l) { btnMusteriDuzenle.addActionListener(l); }
    public void addMusteriSilListener(ActionListener l) { btnMusteriSil.addActionListener(l); }
    public void addMusteriYenileListener(ActionListener l) { btnMusteriYenile.addActionListener(l); }
    
    public void addTalepEkleListener(ActionListener l) { btnTalepEkle.addActionListener(l); }
    public void addTalepYenileListener(ActionListener l) { btnTalepYenile.addActionListener(l); }
    
    public void addHareketEkleListener(ActionListener l) { btnHareketEkle.addActionListener(l); }
    public void addHareketYenileListener(ActionListener l) { btnHareketYenile.addActionListener(l); }
    
    public void addCikisListener(ActionListener l) { btnCikis.addActionListener(l); }
    
    // Kategori Listenerları
    public void addKategoriEkleListener(ActionListener l) { itemKategoriEkle.addActionListener(l); }
    public void addKategoriDuzenleListener(ActionListener l) { itemKategoriDuzenle.addActionListener(l); }
    public void addKategoriSilListener(ActionListener l) { itemKategoriSil.addActionListener(l); }
    
    // Mouse Listener Ekleme
    public void addUrunTabloMouseListener(MouseListener l) { tblUrunler.addMouseListener(l); }
    public void addPersonelTabloMouseListener(MouseListener l) { tblPersonel.addMouseListener(l); }
    public void addTalepTabloMouseListener(MouseListener l) { tblTalepler.addMouseListener(l); }
    public void addMusteriTabloMouseListener(MouseListener l) { tblMusteriler.addMouseListener(l); }

    // YETKİLENDİRME METOTLARI
    public void setUrunYonetimiAktif(boolean aktif) {
        btnUrunEkle.setEnabled(aktif);
        btnUrunDuzenle.setEnabled(aktif);
        btnUrunSil.setEnabled(aktif);
        itemKategoriEkle.setEnabled(aktif);
        itemKategoriDuzenle.setEnabled(aktif);
        itemKategoriSil.setEnabled(aktif);
    }
    
    public void setPersonelSekmesiGorunur(boolean gorunur) {
        if (!gorunur) {
            int index = tabbedPane.indexOfTab("Personel Yönetimi");
            if (index != -1) {
                tabbedPane.removeTabAt(index);
            }
        }
    }
    
    public void setMusteriYonetimiAktif(boolean aktif) {
        btnMusteriEkle.setEnabled(aktif);
        btnMusteriDuzenle.setEnabled(aktif);
        btnMusteriSil.setEnabled(aktif);
    }
    
    public void setSiparisOlusturmaAktif(boolean aktif) {
        btnHareketEkle.setEnabled(aktif);
    }
    
    public void setToplamUrunCesidi(int sayi) {
        lblToplamUrunCesidi.setText(String.valueOf(sayi));
    }
    
    public void setToplamSermaye(double tutar) {
        lblToplamSermaye.setText(String.format("%.2f TL", tutar));
    }
    
    public void setKategoriStokBilgisi(String bilgi) {
        txtKategoriStok.setText(bilgi);
    }
}
