package tr.view;

import tr.model.Kategori;
import tr.model.Urun;
import tr.util.ModernUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class UrunDialog extends JDialog {
    private JTextField txtAd;
    private JComboBox<Kategori> cmbKategori;
    private JSpinner spnStok;
    private JTextField txtFiyat;
    private JButton btnKaydet;
    private JButton btnIptal;
    private boolean kaydetTiklandi = false;
    private Urun urun;

    public UrunDialog(Frame owner, List<Kategori> kategoriler, Urun duzenlenecekUrun) {
        super(owner, duzenlenecekUrun == null ? "Yeni Ürün Ekle" : "Ürün Düzenle", true);
        this.urun = duzenlenecekUrun;
        
        setLayout(new BorderLayout());
        setSize(450, 350);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(ModernUI.BACKGROUND_COLOR);

        // Form Paneli
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(ModernUI.SURFACE_COLOR);
        pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ad
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblAd = new JLabel("Ürün Adı:");
        lblAd.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblAd, gbc);
        
        gbc.gridx = 1;
        txtAd = new JTextField(20);
        ModernUI.styleTextField(txtAd);
        pnlForm.add(txtAd, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblKat = new JLabel("Kategori:");
        lblKat.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblKat, gbc);
        
        gbc.gridx = 1;
        cmbKategori = new JComboBox<>();
        cmbKategori.setFont(ModernUI.FONT_NORMAL);
        cmbKategori.setBackground(Color.WHITE);
        for (Kategori k : kategoriler) {
            cmbKategori.addItem(k);
        }
        pnlForm.add(cmbKategori, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblStok = new JLabel("Stok Adedi:");
        lblStok.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblStok, gbc);
        
        gbc.gridx = 1;
        spnStok = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        spnStok.setFont(ModernUI.FONT_NORMAL);
        pnlForm.add(spnStok, gbc);

        // Fiyat
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblFiyat = new JLabel("Fiyat (TL):");
        lblFiyat.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblFiyat, gbc);
        
        gbc.gridx = 1;
        txtFiyat = new JTextField();
        ModernUI.styleTextField(txtFiyat);
        pnlForm.add(txtFiyat, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Buton Paneli
        JPanel pnlButonlar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButonlar.setBackground(ModernUI.BACKGROUND_COLOR);
        pnlButonlar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        btnKaydet = new JButton("Kaydet");
        ModernUI.styleButton(btnKaydet);
        
        btnIptal = new JButton("İptal");
        ModernUI.styleDangerButton(btnIptal); // İptal butonu kırmızı olsun
        
        pnlButonlar.add(btnKaydet);
        pnlButonlar.add(btnIptal);
        add(pnlButonlar, BorderLayout.SOUTH);

        // Verileri Doldur
        if (urun != null) {
            txtAd.setText(urun.getAd());
            spnStok.setValue(urun.getStok());
            txtFiyat.setText(String.valueOf(urun.getFiyat()));
            
            for (int i = 0; i < cmbKategori.getItemCount(); i++) {
                if (cmbKategori.getItemAt(i).getId() == urun.getKategoriId()) {
                    cmbKategori.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Olaylar
        btnKaydet.addActionListener(e -> {
            if (validasyon()) {
                kaydetTiklandi = true;
                setVisible(false);
            }
        });

        btnIptal.addActionListener(e -> setVisible(false));
    }

    private boolean validasyon() {
        if (txtAd.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ürün adı boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(txtFiyat.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Geçerli bir fiyat giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isKaydetTiklandi() {
        return kaydetTiklandi;
    }

    public Urun getUrun() {
        if (urun == null) urun = new Urun();
        urun.setAd(txtAd.getText().trim());
        urun.setKategoriId(((Kategori) cmbKategori.getSelectedItem()).getId());
        urun.setStok((Integer) spnStok.getValue());
        urun.setFiyat(Double.parseDouble(txtFiyat.getText()));
        return urun;
    }
}
