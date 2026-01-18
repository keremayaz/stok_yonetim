package tr.view;

import tr.model.Talep;
import tr.util.ModernUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TalepDialog extends JDialog {
    private JTextArea txtAciklama;
    private JButton btnGonder;
    private JButton btnIptal;
    private boolean gonderTiklandi = false;
    private Talep talep;

    public TalepDialog(Frame owner) {
        super(owner, "Yeni Talep Oluştur", true);
        setLayout(new BorderLayout());
        setSize(450, 350);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(ModernUI.BACKGROUND_COLOR);

        JPanel pnlMerkez = new JPanel(new BorderLayout());
        pnlMerkez.setBackground(ModernUI.SURFACE_COLOR);
        pnlMerkez.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblAciklama = new JLabel("Talep Açıklaması:");
        lblAciklama.setFont(ModernUI.FONT_BOLD);
        lblAciklama.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlMerkez.add(lblAciklama, BorderLayout.NORTH);
        
        txtAciklama = new JTextArea();
        txtAciklama.setLineWrap(true);
        txtAciklama.setFont(ModernUI.FONT_NORMAL);
        txtAciklama.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlMerkez.add(new JScrollPane(txtAciklama), BorderLayout.CENTER);
        
        add(pnlMerkez, BorderLayout.CENTER);

        JPanel pnlButonlar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButonlar.setBackground(ModernUI.BACKGROUND_COLOR);
        pnlButonlar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        btnGonder = new JButton("Gönder");
        ModernUI.styleButton(btnGonder);
        
        btnIptal = new JButton("İptal");
        ModernUI.styleDangerButton(btnIptal);
        
        pnlButonlar.add(btnGonder);
        pnlButonlar.add(btnIptal);
        add(pnlButonlar, BorderLayout.SOUTH);

        btnGonder.addActionListener(e -> {
            if (txtAciklama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Açıklama boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            gonderTiklandi = true;
            setVisible(false);
        });

        btnIptal.addActionListener(e -> setVisible(false));
    }

    public boolean isGonderTiklandi() {
        return gonderTiklandi;
    }

    public String getAciklama() {
        return txtAciklama.getText().trim();
    }
}
