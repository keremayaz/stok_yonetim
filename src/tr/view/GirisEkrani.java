package tr.view;

import tr.util.ModernUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class GirisEkrani extends JFrame {
    private JTextField txtKullaniciAdi;
    private JPasswordField txtSifre;
    private JButton btnGiris;
    private JLabel lblDurum;

    public GirisEkrani() {
        setTitle("Giriş Yap");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Ana Panel (Arkaplan)
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBackground(ModernUI.BACKGROUND_COLOR);
        setContentPane(pnlMain);

        // Kart Paneli (Beyaz Kutu)
        JPanel pnlCard = new JPanel();
        pnlCard.setLayout(new BoxLayout(pnlCard, BoxLayout.Y_AXIS));
        pnlCard.setBackground(ModernUI.SURFACE_COLOR);
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));
        pnlCard.setPreferredSize(new Dimension(350, 400));

        // Logo / Başlık
        JLabel lblIcon = new JLabel("\uD83D\uDCE6"); // Kutu ikonu
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel("Stok Yönetimi");
        lblTitle.setFont(ModernUI.FONT_TITLE);
        lblTitle.setForeground(ModernUI.PRIMARY_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Lütfen giriş yapınız");
        lblSubtitle.setFont(ModernUI.FONT_NORMAL);
        lblSubtitle.setForeground(ModernUI.TEXT_SECONDARY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Alanları
        JLabel lblUser = new JLabel("Kullanıcı Adı");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblUser.setFont(ModernUI.FONT_BOLD);
        
        txtKullaniciAdi = new JTextField();
        ModernUI.styleTextField(txtKullaniciAdi);
        txtKullaniciAdi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lblPass = new JLabel("Şifre");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPass.setFont(ModernUI.FONT_BOLD);
        
        txtSifre = new JPasswordField();
        ModernUI.styleTextField(txtSifre); // JPasswordField JTextField'den türediği için çalışır
        txtSifre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Buton
        btnGiris = new JButton("GİRİŞ YAP");
        ModernUI.styleButton(btnGiris);
        btnGiris.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnGiris.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Durum Mesajı
        lblDurum = new JLabel(" ");
        lblDurum.setFont(ModernUI.FONT_SMALL);
        lblDurum.setForeground(ModernUI.DANGER_COLOR);
        lblDurum.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bileşenleri Ekleme
        pnlCard.add(lblIcon);
        pnlCard.add(Box.createVerticalStrut(10));
        pnlCard.add(lblTitle);
        pnlCard.add(lblSubtitle);
        pnlCard.add(Box.createVerticalStrut(30));
        
        pnlCard.add(lblUser);
        pnlCard.add(Box.createVerticalStrut(5));
        pnlCard.add(txtKullaniciAdi);
        pnlCard.add(Box.createVerticalStrut(15));
        
        pnlCard.add(lblPass);
        pnlCard.add(Box.createVerticalStrut(5));
        pnlCard.add(txtSifre);
        pnlCard.add(Box.createVerticalStrut(30));
        
        pnlCard.add(btnGiris);
        pnlCard.add(Box.createVerticalStrut(15));
        pnlCard.add(lblDurum);

        pnlMain.add(pnlCard);
    }

    public String getKullaniciAdi() {
        return txtKullaniciAdi.getText();
    }

    public String getSifre() {
        return new String(txtSifre.getPassword());
    }

    public void setGirisButtonListener(ActionListener listener) {
        btnGiris.addActionListener(listener);
    }

    public void setDurumMesaji(String mesaj) {
        lblDurum.setText(mesaj);
    }
    
    public void ekranGoster() {
        setVisible(true);
    }
    
    public void ekranKapat() {
        dispose();
    }
}
