package tr.view;

import tr.model.Kullanici;
import tr.model.Rol;
import tr.util.ModernUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class KullaniciDialog extends JDialog {
    private JTextField txtAd;
    private JTextField txtSoyad;
    private JTextField txtKullaniciAdi;
    private JPasswordField txtSifre;
    private JComboBox<Rol> cmbRol;
    private JButton btnKaydet;
    private JButton btnIptal;
    private boolean kaydetTiklandi = false;
    private Kullanici kullanici;

    public KullaniciDialog(Frame owner, List<Rol> roller, Kullanici duzenlenecekKullanici) {
        super(owner, duzenlenecekKullanici == null ? "Yeni Personel Ekle" : "Personel Düzenle", true);
        this.kullanici = duzenlenecekKullanici;
        
        setLayout(new BorderLayout());
        setSize(450, 400);
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
        JLabel lblAd = new JLabel("Ad:");
        lblAd.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblAd, gbc);
        
        gbc.gridx = 1;
        txtAd = new JTextField(20);
        ModernUI.styleTextField(txtAd);
        pnlForm.add(txtAd, gbc);

        // Soyad
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblSoyad = new JLabel("Soyad:");
        lblSoyad.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblSoyad, gbc);
        
        gbc.gridx = 1;
        txtSoyad = new JTextField(20);
        ModernUI.styleTextField(txtSoyad);
        pnlForm.add(txtSoyad, gbc);

        // Kullanıcı Adı
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblKadi = new JLabel("Kullanıcı Adı:");
        lblKadi.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblKadi, gbc);
        
        gbc.gridx = 1;
        txtKullaniciAdi = new JTextField(20);
        ModernUI.styleTextField(txtKullaniciAdi);
        pnlForm.add(txtKullaniciAdi, gbc);

        // Şifre
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblSifre = new JLabel("Şifre:");
        lblSifre.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblSifre, gbc);
        
        gbc.gridx = 1;
        txtSifre = new JPasswordField(20);
        ModernUI.styleTextField(txtSifre);
        pnlForm.add(txtSifre, gbc);

        // Rol
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblRol, gbc);
        
        gbc.gridx = 1;
        cmbRol = new JComboBox<>();
        cmbRol.setFont(ModernUI.FONT_NORMAL);
        cmbRol.setBackground(Color.WHITE);
        for (Rol r : roller) {
            cmbRol.addItem(r);
        }
        pnlForm.add(cmbRol, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Buton Paneli
        JPanel pnlButonlar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButonlar.setBackground(ModernUI.BACKGROUND_COLOR);
        pnlButonlar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        btnKaydet = new JButton("Kaydet");
        ModernUI.styleButton(btnKaydet);
        
        btnIptal = new JButton("İptal");
        ModernUI.styleDangerButton(btnIptal);
        
        pnlButonlar.add(btnKaydet);
        pnlButonlar.add(btnIptal);
        add(pnlButonlar, BorderLayout.SOUTH);

        // Düzenleme modu ise doldur
        if (kullanici != null) {
            txtAd.setText(kullanici.getAd());
            txtSoyad.setText(kullanici.getSoyad());
            txtKullaniciAdi.setText(kullanici.getKullaniciAdi());
            txtSifre.setText(kullanici.getSifre());
            
            for (int i = 0; i < cmbRol.getItemCount(); i++) {
                if (cmbRol.getItemAt(i).getId() == kullanici.getRolId()) {
                    cmbRol.setSelectedIndex(i);
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
        if (txtAd.getText().trim().isEmpty() || txtKullaniciAdi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ad ve Kullanıcı Adı boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isKaydetTiklandi() {
        return kaydetTiklandi;
    }

    public Kullanici getKullanici() {
        if (kullanici == null) kullanici = new Kullanici();
        kullanici.setAd(txtAd.getText().trim());
        kullanici.setSoyad(txtSoyad.getText().trim());
        kullanici.setKullaniciAdi(txtKullaniciAdi.getText().trim());
        kullanici.setSifre(new String(txtSifre.getPassword()));
        kullanici.setRolId(((Rol) cmbRol.getSelectedItem()).getId());
        return kullanici;
    }
}
