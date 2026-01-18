package tr.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ModernUI {

    // --- RENK PALETİ ---
    public static final Color PRIMARY_COLOR = new Color(63, 81, 181);    // İndigo Mavisi
    public static final Color SECONDARY_COLOR = new Color(48, 63, 159);  // Koyu İndigo
    public static final Color ACCENT_COLOR = new Color(255, 64, 129);    // Pembe (Vurgu)
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250); // Açık Gri Arkaplan
    public static final Color SURFACE_COLOR = Color.WHITE;               // Beyaz Kartlar
    public static final Color TEXT_COLOR = new Color(33, 33, 33);        // Koyu Gri Yazı
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117); // Gri Yazı
    public static final Color DANGER_COLOR = new Color(220, 53, 69);     // Kırmızı

    // --- FONTLAR ---
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // --- BİLEŞEN STİLLERİ ---

    // 1. Buton Stili
    public static void styleButton(JButton btn) {
        btn.setFont(FONT_BOLD);
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20)); // İç boşluk
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover efekti (Basit)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(PRIMARY_COLOR);
            }
        });
    }

    // 2. Tehlikeli Buton Stili (Sil/Çıkış)
    public static void styleDangerButton(JButton btn) {
        styleButton(btn);
        btn.setBackground(DANGER_COLOR);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(DANGER_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(DANGER_COLOR);
            }
        });
    }

    // 3. Tablo Stili
    public static void styleTable(JTable table) {
        table.setFont(FONT_NORMAL);
        table.setRowHeight(35); // Satır yüksekliği
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 234, 246)); // Seçim rengi (Açık İndigo)
        table.setSelectionForeground(TEXT_COLOR);
        
        // Başlık Stili
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(SURFACE_COLOR);
        header.setForeground(TEXT_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        
        // Hücreleri Ortala (Opsiyonel)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        centerRenderer.setBorder(new EmptyBorder(0, 10, 0, 0)); // Sol boşluk
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    // 4. TextField Stili
    public static void styleTextField(JTextField txt) {
        txt.setFont(FONT_NORMAL);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)));
    }
    
    // 5. Panel Stili (Kart Görünümü)
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE_COLOR);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        return panel;
    }
}
