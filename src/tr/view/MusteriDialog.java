package tr.view;

import tr.model.Musteri;
import tr.util.ModernUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MusteriDialog extends JDialog {
    private JTextField txtAdSoyad;
    private JTextField txtTelefon;
    private JTextArea txtAdres;
    private JButton btnKaydet;
    private JButton btnIptal;
    private boolean kaydetTiklandi = false;
    private Musteri musteri;

    public MusteriDialog(Frame owner, Musteri duzenlenecekMusteri) {
        super(owner, duzenlenecekMusteri == null ? "Yeni Müşteri Ekle" : "Müşteri Düzenle", true);
        this.musteri = duzenlenecekMusteri;
        
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

        // Ad Soyad
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblAd = new JLabel("Ad Soyad:");
        lblAd.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblAd, gbc);
        
        gbc.gridx = 1;
        txtAdSoyad = new JTextField(20);
        ModernUI.styleTextField(txtAdSoyad);
        pnlForm.add(txtAdSoyad, gbc);

        // Telefon
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTel = new JLabel("Telefon:");
        lblTel.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblTel, gbc);
        
        gbc.gridx = 1;
        txtTelefon = new JTextField(20);
        ModernUI.styleTextField(txtTelefon);
        pnlForm.add(txtTelefon, gbc);

        // Adres
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblAdres = new JLabel("Adres:");
        lblAdres.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblAdres, gbc);
        
        gbc.gridx = 1;
        txtAdres = new JTextArea(4, 20);
        txtAdres.setLineWrap(true);
        txtAdres.setFont(ModernUI.FONT_NORMAL);
        txtAdres.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlForm.add(new JScrollPane(txtAdres), gbc);

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

        // Verileri Doldur
        if (musteri != null) {
            txtAdSoyad.setText(musteri.getAdSoyad());
            txtTelefon.setText(musteri.getTelefon());
            txtAdres.setText(musteri.getAdres());
        }

        // Olaylar
        btnKaydet.addActionListener(e -> {
            if (txtAdSoyad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ad Soyad boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            kaydetTiklandi = true;
            setVisible(false);
        });

        btnIptal.addActionListener(e -> setVisible(false));
    }

    public boolean isKaydetTiklandi() {
        return kaydetTiklandi;
    }

    public Musteri getMusteri() {
        if (musteri == null) musteri = new Musteri();
        musteri.setAdSoyad(txtAdSoyad.getText().trim());
        musteri.setTelefon(txtTelefon.getText().trim());
        musteri.setAdres(txtAdres.getText().trim());
        return musteri;
    }
}
