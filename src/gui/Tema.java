package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Tema {
    public static final Color NAVY = new Color(13, 27, 42);
    public static final Color NAVY_CARD = new Color(15, 32, 48);
    public static final Color NAVY_LIGHT = new Color(26, 51, 72);
    public static final Color NAVY_ROW = new Color(11, 28, 44);
    public static final Color NAVY_ROW2 = new Color(14, 32, 53);
    public static final Color CYAN = new Color(0, 207, 207);
    public static final Color CYAN_DIM = new Color(0, 100, 120);
    public static final Color BORDER_C = new Color(0, 150, 150, 60);
    public static final Color TEXT = new Color(200, 232, 240);
    public static final Color TEXT_MUTED = new Color(106, 154, 176);
    public static final Color TEXT_DIM = new Color(58, 96, 112);
    public static final Color INCOME = new Color(0, 207, 207);
    public static final Color EXPENSE = new Color(255, 107, 157);
    public static final Color ESSENTIAL = new Color(78, 205, 196);

    public static JLabel label(String texto, int size, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, size));
        l.setForeground(color);
        return l;
    }

    public static JLabel labelBold(String texto, int size, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        l.setForeground(color);
        return l;
    }

    public static JTextField campo() {
        JTextField tf = new JTextField();
        estilizarCampo(tf);
        return tf;
    }

    public static JPasswordField password() {
        JPasswordField pf = new JPasswordField();
        estilizarCampo(pf);
        return pf;
    }

    public static void estilizarCampo(JTextField tf) {
        tf.setBackground(new Color(0, 35, 50));
        tf.setForeground(TEXT);
        tf.setCaretColor(CYAN);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 150, 60), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
    }

    public static JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(0, 80, 80, 120));
        b.setForeground(CYAN);
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        b.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 120), 1));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton botonGrande(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(0, 120, 120, 180));
        b.setForeground(CYAN);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 180, 180), 2),
            BorderFactory.createEmptyBorder(8, 22, 8, 22)));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JPanel navbar(String titulo) {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        nav.setBackground(new Color(8, 15, 25, 240));
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C));
        JLabel logo = new JLabel("");//Recordatori de logo
        logo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        nav.add(logo);
        nav.add(labelBold("SmartBudget", 14, CYAN));
        if (!titulo.isEmpty()) {
            nav.add(label("—", 12, TEXT_DIM));
            nav.add(label(titulo, 12, TEXT_MUTED));
        }
        return nav;
    }

    public static JPanel footer() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 5));
        f.setBackground(new Color(8, 15, 25, 200));
        f.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0, 80, 80, 40)));
        return f;
    }
}
