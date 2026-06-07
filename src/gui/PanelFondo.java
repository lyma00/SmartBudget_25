package gui;

import java.awt.*;
import javax.swing.*;

public class PanelFondo extends JPanel {

    public PanelFondo() {
        setBackground(Tema.NAVY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        g2.setColor(Tema.NAVY);
        g2.fillRect(0, 0, w, h);

        int[] x1 = { w / 2, w, w, (int)(w * 0.05) };
        int[] y1 = { 0, 0, h, (int)(h * 0.88) };
        g2.setPaint(new GradientPaint(w / 2, 0, new Color(10, 58, 90), w, h, new Color(8, 24, 40)));
        g2.fillPolygon(x1, y1, 4);

        int[] x2 = { (int)(w * 0.58), w, w, (int)(w * 0.18) };
        int[] y2 = { 0, 0, (int)(h * 0.82), (int)(h * 0.75) };
        g2.setPaint(new GradientPaint((int)(w * 0.7), 0, new Color(13, 69, 107, 160), w, h, new Color(9, 38, 60, 80)));
        g2.fillPolygon(x2, y2, 4);

        g2.dispose();
    }
}
