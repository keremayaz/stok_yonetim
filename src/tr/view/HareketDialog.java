package tr.view;

import tr.model.Musteri;
import tr.model.Urun;
import tr.util.ModernUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class HareketDialog extends JDialog {
    private JComboBox<Urun> cmbUrun;
    private JComboBox<Musteri> cmbMusteri;
    private JSpinner spnAdet;
    private JButton btnKaydet;
    private JButton btnIptal;
    private boolean kaydetTiklandi = false;

    public HareketDialog(Frame owner, List<Urun> urunler, List<Musteri> musteriler) {
        super(owner, "Yeni Sipariş Oluştur", true);
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

        // Ürün Seçimi
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUrun = new JLabel("Ürün Seçiniz:");
        lblUrun.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblUrun, gbc);
        
        gbc.gridx = 1;
        cmbUrun = new JComboBox<>();
        cmbUrun.setFont(ModernUI.FONT_NORMAL);
        cmbUrun.setBackground(Color.WHITE);
        for (Urun u : urunler) {
            cmbUrun.addItem(u);
        }
        pnlForm.add(cmbUrun, gbc);

        // Müşteri Seçimi
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblMusteri = new JLabel("Müşteri Seçiniz:");
        lblMusteri.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblMusteri, gbc);
        
        gbc.gridx = 1;
        cmbMusteri = new JComboBox<>();
        cmbMusteri.setFont(ModernUI.FONT_NORMAL);
        cmbMusteri.setBackground(Color.WHITE);
        for (Musteri m : musteriler) {
            cmbMusteri.addItem(m);
        }
        pnlForm.add(cmbMusteri, gbc);

        // Adet
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblAdet = new JLabel("Adet:");
        lblAdet.setFont(ModernUI.FONT_BOLD);
        pnlForm.add(lblAdet, gbc);
        
        gbc.gridx = 1;
        spnAdet = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spnAdet.setFont(ModernUI.FONT_NORMAL);
        pnlForm.add(spnAdet, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Buton Paneli
        JPanel pnlButonlar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButonlar.setBackground(ModernUI.BACKGROUND_COLOR);
        pnlButonlar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        btnKaydet = new JButton("Siparişi Tamamla");
        ModernUI.styleButton(btnKaydet);
        
        btnIptal = new JButton("İptal");
        ModernUI.styleDangerButton(btnIptal);
        
        pnlButonlar.add(btnKaydet);
        pnlButonlar.add(btnIptal);
        add(pnlButonlar, BorderLayout.SOUTH);

        // Olaylar
        btnKaydet.addActionListener(e -> {
            Urun seciliUrun = (Urun) cmbUrun.getSelectedItem();
            int adet = (int) spnAdet.getValue();
            
            if (seciliUrun.getStok() < adet) {
                JOptionPane.showMessageDialog(this, "Yetersiz Stok! Mevcut Stok: " + seciliUrun.getStok(), "Hata", JOptionPane.ERROR_MESSAGE);
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

    public Urun getSeciliUrun() {
        return (Urun) cmbUrun.getSelectedItem();
    }

    public Musteri getSeciliMusteri() {
        return (Musteri) cmbMusteri.getSelectedItem();
    }

    public int getAdet() {
        return (int) spnAdet.getValue();
    }
}
