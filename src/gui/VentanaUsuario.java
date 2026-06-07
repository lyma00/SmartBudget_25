package gui;

import general.GestorMovimientos;
import general.GestorUsuarios;
import general.GestorMovimientos.ResultadoAgregar;
import general.Movimiento;
import general.Usuario;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class VentanaUsuario extends JFrame {

    private Usuario usuario;
    private GestorMovimientos gestor;
   private GestorUsuarios gestorUsuarios;
    private JPanel panelContenido;
    private CardLayout cardLayout;

    public VentanaUsuario(Usuario usuario, GestorUsuarios gestorUsuarios) {
        this.usuario  = usuario;
        this.gestor         = new GestorMovimientos(usuario);
        this.gestorUsuarios = gestorUsuarios;
        setTitle("SmartBudget - " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(960, 640);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);
        construirUI();
    }

    private void construirUI() {
        PanelFondo root = new PanelFondo();
        root.setLayout(new BorderLayout());
        root.add(crearNavbar(), BorderLayout.NORTH);
        cardLayout     = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setOpaque(false);
        cargarTabs();
        root.add(panelContenido, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void cargarTabs() {
        panelContenido.removeAll();
        panelContenido.add(tabHome(),     "home");
        panelContenido.add(tabClasif(),   "clasif");
        panelContenido.add(tabStats(),    "stats");
        panelContenido.add(tabConsejos(), "consejos");
        panelContenido.add(tabEstado(),   "estado");
        cardLayout.show(panelContenido, "home");
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void ir(String id) { cargarTabs(); cardLayout.show(panelContenido, id); }

    private JPanel crearNavbar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
        nav.setBackground(new Color(8, 15, 25, 240));
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER_C));
        JLabel logo = new JLabel("");//Recordatorio de logo
        logo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        nav.add(logo);

        String[][] links = {
            {"Consejos de ahorro","consejos"}, {"Estadísticas","stats"},
            {"Clasificación","clasif"},        {"Estado de cuenta","estado"}
        };
        for (String[] l : links) {
            final String id = l[1];
            JLabel link = Tema.label(l[0], 11, Tema.TEXT_MUTED);
            link.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
            link.setCursor(new Cursor(Cursor.HAND_CURSOR));
            link.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { ir(id); }
                public void mouseEntered(MouseEvent e) { link.setForeground(Tema.CYAN); }
                public void mouseExited(MouseEvent e)  { link.setForeground(Tema.TEXT_MUTED); }
            });
            nav.add(link);
        }
        nav.add(Box.createHorizontalGlue());
        nav.add(Tema.label(usuario.getNombre() + " " + usuario.getApellidoP(), 11, Tema.CYAN));
        JLabel salir = Tema.label("  Salir", 11, Tema.TEXT_DIM);
        salir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        salir.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); }
            public void mouseEntered(MouseEvent e) { salir.setForeground(Tema.CYAN); }
            public void mouseExited(MouseEvent e)  { salir.setForeground(Tema.TEXT_DIM); }
        });
        nav.add(salir);
        return nav;
    }

    private JPanel tabHome() {
        JPanel p = wrap("Bienvenido a " + usuario.getNombre() + " " + usuario.getApellidoP());
        JPanel centro = new JPanel(new BorderLayout(0, 10)); centro.setOpaque(false);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER)); bp.setOpaque(false);
        JButton bE = Tema.boton("Estado de Cuenta"); bE.setPreferredSize(new Dimension(150, 28));
        bE.addActionListener(e -> ir("estado")); bp.add(bE);
        centro.add(bp, BorderLayout.NORTH);

        JPanel met = new JPanel(new GridLayout(1, 3, 8, 0)); met.setOpaque(false);
        met.add(metrica("Saldo Mensual:", "$" + f2(gestor.totalIngresos())));
        met.add(metrica("Ingresos Mensuales:", "$" + f2(gestor.totalIngresos())));
        met.add(metrica("Saldo actual:", "$" + f2(gestor.saldo())));
        centro.add(met, BorderLayout.CENTER);

        JPanel tw = new JPanel(new BorderLayout(0, 5)); tw.setOpaque(false);
        tw.add(Tema.label("Últimos movimientos", 11, Tema.TEXT_MUTED), BorderLayout.NORTH);
        DefaultTableModel m = modelo(new String[]{"Período", "Descripción", "Monto"});
        for (Movimiento mv : gestor.ultimos(6)) {
            String sg = mv.getTipo().equalsIgnoreCase("ingreso") ? "+" : "-";
            m.addRow(new Object[]{"MM/AAAA", mv.getDescripcion(), sg + "$" + f2(mv.getMonto())});
        }
        if (usuario.getMovimientos().isEmpty())
            m.addRow(new Object[]{"—", "Sin movimientos aún", "—"});
        JTable t = tabla(m);
        t.getColumnModel().getColumn(2).setCellRenderer(montoRenderer());
        JPanel bagg = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bagg.setOpaque(false);
        JButton bAg = Tema.botonGrande("+ Agregar movimiento");
        bAg.addActionListener(e -> { dlgMovimiento(); ir("home"); }); bagg.add(bAg);
        tw.add(scroll(t), BorderLayout.CENTER);
        tw.add(bagg, BorderLayout.SOUTH);
        centro.add(tw, BorderLayout.SOUTH);
        p.add(centro, BorderLayout.CENTER);
        return p;
    }

    private JPanel tabClasif() {
        JPanel p = wrap("Clasificación de gastos");
        p.setLayout(new BorderLayout(0, 10));

        // Botón info
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        topBar.setOpaque(false);
        JButton btnInfo = new JButton("ℹ  ¿Qué es primer y segundo nivel?");
        btnInfo.setBackground(new Color(0, 60, 80, 200));
        btnInfo.setForeground(new Color(0, 207, 207));
        btnInfo.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 150), 2),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        btnInfo.setFocusPainted(false); btnInfo.setOpaque(true);
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInfo.addActionListener(e -> mostrarDialogoNiveles());
        topBar.add(btnInfo);
        p.add(topBar, BorderLayout.NORTH);

        JPanel cardTabla = card("Procura que tus gastos sean menores a tus ingresos");
        ArrayList<Movimiento> gastos = gestor.soloGastos();

        DefaultTableModel m = modelo(new String[]{"Descripción", "Monto", "Nivel"});
        for (Movimiento g : gastos)
            m.addRow(new Object[]{g.getDescripcion(), "$" + f2(g.getMonto()),
                g.isEsencial() ? "Primer nivel" : "Segundo nivel"});
        if (gastos.isEmpty()) m.addRow(new Object[]{"Sin gastos registrados", "—", "—"});

        JTable t = tabla(m);
        t.setRowHeight(28);
        t.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(tb, v, s, f, r, c);
                String val = v != null ? v.toString() : "";
                setForeground(val.equals("Primer nivel") ? Tema.ESSENTIAL : Tema.EXPENSE);
                setFont(new Font("SansSerif", Font.BOLD, 12));
                setBackground(r % 2 == 0 ? Tema.NAVY_ROW : Tema.NAVY_ROW2); return this;
            }
        });
        cardTabla.add(scroll(t), BorderLayout.CENTER);

        
        JPanel botonesFila = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        botonesFila.setBackground(new Color(10, 25, 40, 180));
        botonesFila.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDER_C));
        botonesFila.add(Tema.label("Selecciona un gasto y cambia su nivel:", 11, Tema.TEXT_MUTED));

        JButton btnPrimero = botonNivel("⭐  Primer nivel (Esencial)", Tema.ESSENTIAL, new Color(0, 60, 50, 200));
        btnPrimero.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row < 0 || row >= gastos.size()) {
                JOptionPane.showMessageDialog(this, "Selecciona un gasto primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            gestor.cambiarNivel(usuario.getMovimientos().indexOf(gastos.get(row)), true);
            m.setValueAt("Primer nivel", row, 2); t.repaint();
        });

        JButton btnSegundo = botonNivel("○  Segundo nivel (No esencial)", Tema.EXPENSE, new Color(60, 0, 20, 200));
        btnSegundo.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row < 0 || row >= gastos.size()) {
                JOptionPane.showMessageDialog(this, "Selecciona un gasto primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            gestor.cambiarNivel(usuario.getMovimientos().indexOf(gastos.get(row)), false);
            m.setValueAt("Segundo nivel", row, 2); t.repaint();
        });

        botonesFila.add(btnPrimero); botonesFila.add(btnSegundo);
        cardTabla.add(botonesFila, BorderLayout.SOUTH);
        p.add(cardTabla, BorderLayout.CENTER);
        return p;
    }

    private JPanel tabStats() {
        JPanel p = wrap("Estadísticas");
        p.setLayout(new BorderLayout(0, 10));

        JPanel metricas = new JPanel(new GridLayout(1, 3, 10, 0)); metricas.setOpaque(false);
        metricas.add(metrica("Gasto máximo del mes:", "$" + f2(gestor.gastoMaximo())));
        metricas.add(metrica("Gasto mínimo del mes:", "$" + f2(gestor.gastoMinimo())));
        metricas.add(metrica("Gasto total del mes:",  "$" + f2(gestor.totalGastos())));
        p.add(metricas, BorderLayout.NORTH);

        JPanel cardGraf = card("Ingresos vs Gastos por movimiento");
        JPanel grafPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ArrayList<Movimiento> movs = usuario.getMovimientos();
                if (movs.isEmpty()) {
                    g2.setColor(new Color(106,154,176)); g2.setFont(new Font("SansSerif",Font.PLAIN,13));
                    g2.drawString("Sin movimientos registrados", getWidth()/2 - 100, getHeight()/2);
                    g2.dispose(); return;
                }
                int n=Math.min(movs.size(),10), pad=40;
                int w=getWidth()-pad*2, h=getHeight()-pad*2;
                int barW=Math.max(12,(w/n)-10), gap=(w-barW*n)/(n+1);
                double maxVal=movs.stream().mapToDouble(Movimiento::getMonto).max().orElse(1);
                for (int i=0; i<n; i++) {
                    Movimiento mv=movs.get(i);
                    boolean esIng=mv.getTipo().equalsIgnoreCase("ingreso");
                    int barH=(int)((mv.getMonto()/maxVal)*h);
                    int x=pad+gap+i*(barW+gap), y=pad+h-barH;
                    g2.setColor(esIng?new Color(0,207,207,200):new Color(255,107,157,200));
                    g2.fillRoundRect(x,y,barW,barH,4,4);
                    g2.setColor(esIng?new Color(0,240,240):new Color(255,140,180));
                    g2.fillRoundRect(x,y,barW,3,2,2);
                    g2.setColor(new Color(200,232,240)); g2.setFont(new Font("SansSerif",Font.PLAIN,9));
                    String ms="$"+(mv.getMonto()>=1000?(int)(mv.getMonto()/1000)+"k":(int)mv.getMonto());
                    int sw=g2.getFontMetrics().stringWidth(ms);
                    g2.drawString(ms,x+barW/2-sw/2,y-4);
                    g2.setColor(new Color(106,154,176)); g2.setFont(new Font("SansSerif",Font.PLAIN,9));
                    String desc=mv.getDescripcion().length()>6?mv.getDescripcion().substring(0,6)+".":mv.getDescripcion();
                    int dw=g2.getFontMetrics().stringWidth(desc);
                    g2.drawString(desc,x+barW/2-dw/2,pad+h+14);
                }
                g2.setColor(new Color(0,150,150,60)); g2.drawLine(pad,pad+h,pad+w,pad+h);
                g2.setColor(new Color(0,207,207)); g2.fillRect(pad,8,10,10);
                g2.setColor(new Color(200,232,240)); g2.setFont(new Font("SansSerif",Font.PLAIN,10));
                g2.drawString("Ingresos",pad+14,18);
                g2.setColor(new Color(255,107,157)); g2.fillRect(pad+80,8,10,10);
                g2.setColor(new Color(200,232,240)); g2.drawString("Gastos",pad+94,18);
                g2.dispose();
            }
        };
        grafPanel.setOpaque(false); grafPanel.setPreferredSize(new Dimension(0, 220));
        cardGraf.add(grafPanel, BorderLayout.CENTER);
        p.add(cardGraf, BorderLayout.CENTER);
        return p;
    }

    private JPanel tabConsejos() {
        JPanel p = wrap("Consejos de ahorro");
        JPanel card = card("Consejos de ahorro");
        JPanel inner = new JPanel(new BorderLayout(0, 10)); inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inner.add(Tema.label("Gastos que pueden eliminarse", 11, Tema.TEXT_MUTED), BorderLayout.NORTH);

        DefaultTableModel m = modelo(new String[]{"Gasto", "Monto", "Ahorro"});
        for (Movimiento mv : usuario.getMovimientos())
            if (mv.getTipo().equalsIgnoreCase("gasto") && !mv.isEsencial())
                m.addRow(new Object[]{mv.getDescripcion(), "$"+f2(mv.getMonto()), "$"+f2(mv.getMonto())});
        while (m.getRowCount() < 8) m.addRow(new Object[]{"--------------", "$00.00", "$00.00"});

        JTable t = tabla(m);
        t.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(tb, v, s, f, r, c);
                String val = v != null ? v.toString() : "";
                setForeground(val.equals("$00.00") ? Tema.TEXT_DIM : Tema.INCOME);
                setBackground(r%2==0 ? Tema.NAVY_ROW : Tema.NAVY_ROW2); return this;
            }
        });
        inner.add(scroll(t), BorderLayout.CENTER);

        double pct    = gestor.porcentajeGasto();
        double saldo  = gestor.saldo();
        double noEse  = gestor.totalNoEsenciales();
        JPanel tips = new JPanel(); tips.setLayout(new BoxLayout(tips, BoxLayout.Y_AXIS));
        tips.setOpaque(false); tips.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        if (pct > 80)    tips.add(tipAlerta("Estás gastando el "+(int)pct+"% de tus ingresos (más del 80%).", new Color(255,80,80), new Color(60,10,10)));
        if (noEse > 0)   tips.add(tipAlerta("Gastos no esenciales: $"+f2(noEse)+". Considera reducirlos.", new Color(255,200,0), new Color(50,40,0)));
        if (saldo > 0)   tips.add(tipAlerta("Podrías ahorrar $"+f2(saldo*0.20)+" (20% de tu saldo de $"+f2(saldo)+").", new Color(0,207,207), new Color(0,40,40)));
        if (tips.getComponentCount()==0) tips.add(tipAlerta("¡Tus finanzas se ven saludables!", new Color(0,207,207), new Color(0,40,40)));
        inner.add(tips, BorderLayout.SOUTH);
        card.add(inner, BorderLayout.CENTER);
        p.add(card, BorderLayout.CENTER);
        return p;
    }

    private JPanel tabEstado() {
        JPanel p = wrap("Estado de cuenta");
        JPanel card = card("Movimientos del mes");

        ArrayList<Movimiento> movs = usuario.getMovimientos();
        DefaultTableModel m = modelo(new String[]{"#", "Tipo", "Descripción", "Monto", "Clasificación"});
        for (int i = 0; i < movs.size(); i++) {
            Movimiento mv = movs.get(i);
            String cl = mv.getTipo().equalsIgnoreCase("gasto")
                ? (mv.isEsencial() ? "Primer nivel" : "Segundo nivel") : "—";
            String sg = mv.getTipo().equalsIgnoreCase("ingreso") ? "+" : "-";
            m.addRow(new Object[]{i+1, mv.getTipo(), mv.getDescripcion(), sg+"$"+f2(mv.getMonto()), cl});
        }
        if (movs.isEmpty()) m.addRow(new Object[]{"—","—","Sin movimientos","—","—"});

        JTable t = tabla(m);
        t.getColumnModel().getColumn(3).setCellRenderer(montoRenderer());
        card.add(scroll(t), BorderLayout.CENTER);

        JPanel badd = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8)); badd.setOpaque(false);

        JButton bDel = Tema.botonGrande("🗑  Eliminar movimiento");
        bDel.setForeground(Tema.EXPENSE);
        bDel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.EXPENSE, 2),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        bDel.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un movimiento primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el movimiento seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                gestor.eliminar(row);
                ir("estado");
            }
        });

        JButton bAg = Tema.botonGrande("+ Nuevo movimiento");
        bAg.addActionListener(e -> { dlgMovimiento(); ir("estado"); });

        badd.add(bDel); badd.add(bAg);
        card.add(badd, BorderLayout.SOUTH);
        p.add(card, BorderLayout.CENTER);
        return p;
    }

    private void dlgMovimiento() {
        JDialog dlg = new JDialog(this, "Nuevo movimiento", true);
        dlg.setSize(400, 295); dlg.setLocationRelativeTo(this);
        JPanel pan = new JPanel(); pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.setBackground(Tema.NAVY_CARD); pan.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));
        pan.add(Tema.labelBold("Nuevo movimiento", 14, Tema.TEXT)); pan.add(Box.createVerticalStrut(12));

        JRadioButton rbI = new JRadioButton("Ingreso", true), rbG = new JRadioButton("Gasto");
        rbI.setForeground(Tema.INCOME); rbI.setBackground(Tema.NAVY_CARD); rbI.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rbG.setForeground(Tema.EXPENSE); rbG.setBackground(Tema.NAVY_CARD); rbG.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ButtonGroup bg = new ButtonGroup(); bg.add(rbI); bg.add(rbG);
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); tr.setOpaque(false);
        tr.add(rbI); tr.add(rbG); tr.setAlignmentX(0f); pan.add(tr); pan.add(Box.createVerticalStrut(10));

        pan.add(Tema.label("Descripción:", 11, Tema.TEXT_MUTED));
        JTextField fD = Tema.campo(); fD.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); fD.setAlignmentX(0f); pan.add(fD);
        pan.add(Box.createVerticalStrut(8));
        pan.add(Tema.label("Monto ($):", 11, Tema.TEXT_MUTED));
        JTextField fM = Tema.campo(); fM.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); fM.setAlignmentX(0f); pan.add(fM);
        pan.add(Box.createVerticalStrut(8));

        JCheckBox chk = new JCheckBox("Gasto de primer nivel (esencial)");
        chk.setForeground(Tema.TEXT_MUTED); chk.setBackground(Tema.NAVY_CARD); chk.setFont(new Font("SansSerif", Font.PLAIN, 11));
        chk.setAlignmentX(0f); chk.setVisible(false);
        rbG.addActionListener(e -> chk.setVisible(true));
        rbI.addActionListener(e -> chk.setVisible(false));
        pan.add(chk); pan.add(Box.createVerticalStrut(12));

        JLabel lblErr = Tema.label("", 11, new Color(255, 100, 100));
        lblErr.setAlignmentX(0f); pan.add(lblErr);

        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); acc.setOpaque(false);
        JButton bC = Tema.boton("Cancelar"); bC.addActionListener(e -> dlg.dispose());
        JButton bA = Tema.boton("Agregar");
        bA.addActionListener(e -> {
            String tipo = rbI.isSelected() ? "ingreso" : "gasto";
            boolean esencial = tipo.equals("gasto") && chk.isSelected();
            ResultadoAgregar res = gestor.agregar(fD.getText().trim(), fM.getText().trim(), tipo, esencial);
            if (res != ResultadoAgregar.ok) {
                lblErr.setText(gestor.mensajeAgregar(res)); return;
            }
            dlg.dispose();
        });
        acc.add(bC); acc.add(bA); acc.setAlignmentX(0f); pan.add(acc);
        dlg.getContentPane().setBackground(Tema.NAVY_CARD); dlg.add(pan); dlg.setVisible(true);
    }

    private void mostrarDialogoNiveles() {
        JDialog dlg = new JDialog(this, "Niveles de gasto", true);
        dlg.setSize(520, 340); dlg.setLocationRelativeTo(this); dlg.setResizable(false);
        JPanel root = new JPanel(new BorderLayout()); root.setBackground(Tema.NAVY_CARD);
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setBackground(new Color(10,40,55)); header.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Tema.BORDER_C));
        header.add(Tema.labelBold("Niveles de gasto — ¿Cómo clasificar?", 14, Tema.CYAN));
        root.add(header, BorderLayout.NORTH);
        JPanel contenido = new JPanel(new GridLayout(1,2,10,0));
        contenido.setBackground(Tema.NAVY_CARD); contenido.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        contenido.add(cardNivel("⭐  Primer nivel","Gastos ESENCIALES",new Color(0,45,35),Tema.ESSENTIAL,
            "<html>Son los gastos <b>necesarios</b> para<br>vivir. No puedes eliminarlos sin<br>afectar tu calidad de vida básica.</html>",
            "<html><br><b>Ejemplos:</b><br>• Renta / Hipoteca<br>• Supermercado<br>• Luz, Agua, Gas<br>• Transporte al trabajo<br>• Medicamentos</html>"));
        contenido.add(cardNivel("○  Segundo nivel","Gastos NO ESENCIALES",new Color(55,0,25),Tema.EXPENSE,
            "<html>Son gastos <b>opcionales</b> o de<br>entretenimiento. Puedes reducirlos<br>para mejorar tu ahorro.</html>",
            "<html><br><b>Ejemplos:</b><br>• Netflix / Spotify<br>• Restaurantes<br>• Gym / Hobbies<br>• Ropa y accesorios<br>• Viajes de placer</html>"));
        root.add(contenido, BorderLayout.CENTER);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER,0,10));
        footer.setBackground(new Color(10,40,55)); footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Tema.BORDER_C));
        JButton bCer = Tema.boton("Cerrar"); bCer.setPreferredSize(new Dimension(120,32)); bCer.addActionListener(e->dlg.dispose());
        footer.add(bCer); root.add(footer, BorderLayout.SOUTH);
        dlg.setContentPane(root); dlg.setVisible(true);
    }

    private JPanel cardNivel(String titulo, String subt, Color bg, Color color, String desc, String ej) {
        JPanel c = new JPanel(); c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBackground(bg); c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color,2),BorderFactory.createEmptyBorder(14,16,14,16)));
        JLabel t=new JLabel(titulo); t.setFont(new Font("SansSerif",Font.BOLD,14)); t.setForeground(color); t.setAlignmentX(0f);
        JLabel s=new JLabel(subt); s.setFont(new Font("SansSerif",Font.BOLD,11)); s.setForeground(new Color(color.getRed(),color.getGreen(),color.getBlue(),180)); s.setAlignmentX(0f);
        JLabel d=new JLabel(desc); d.setFont(new Font("SansSerif",Font.PLAIN,11)); d.setForeground(Tema.TEXT_MUTED); d.setAlignmentX(0f);
        JLabel e=new JLabel(ej); e.setFont(new Font("SansSerif",Font.PLAIN,11)); e.setForeground(Tema.TEXT_MUTED); e.setAlignmentX(0f);
        c.add(t); c.add(Box.createVerticalStrut(4)); c.add(s); c.add(Box.createVerticalStrut(10)); c.add(d); c.add(e);
        return c;
    }

    private static String f2(double v) { return String.format("%.2f", v); }

    private JPanel wrap(String titulo) {
        JPanel p = new JPanel(new BorderLayout(0,10)); p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(14,18,14,18));
        p.add(Tema.label(titulo,18,Tema.TEXT), BorderLayout.NORTH); return p;
    }
    private JPanel card(String titulo) {
        JPanel c = new JPanel(new BorderLayout()); c.setBackground(new Color(10,25,40,200));
        c.setBorder(BorderFactory.createLineBorder(Tema.BORDER_C,1));
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT,10,7)); h.setBackground(new Color(26,51,72,180));
        h.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Tema.BORDER_C));
        h.add(Tema.label(titulo,12,Tema.TEXT_MUTED)); c.add(h,BorderLayout.NORTH); return c;
    }
    private JPanel metrica(String label, String val) {
        JPanel p = new JPanel(new BorderLayout(0,3)); p.setBackground(new Color(10,25,40,160));
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Tema.BORDER_C,1),BorderFactory.createEmptyBorder(7,11,7,11)));
        p.add(Tema.label(label,10,Tema.TEXT_MUTED),BorderLayout.NORTH);
        p.add(Tema.label(val,13,Tema.TEXT),BorderLayout.CENTER); return p;
    }
    private JPanel tipAlerta(String texto, Color ct, Color cf) {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(cf);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ct.darker(),1),BorderFactory.createEmptyBorder(10,14,10,14)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
        JLabel l = new JLabel(texto); l.setFont(new Font("SansSerif",Font.BOLD,13)); l.setForeground(ct);
        p.add(l,BorderLayout.CENTER); p.setAlignmentX(0f);
        JPanel w = new JPanel(); w.setLayout(new BoxLayout(w,BoxLayout.Y_AXIS)); w.setOpaque(false); w.setAlignmentX(0f);
        w.add(p); w.add(Box.createVerticalStrut(6)); return w;
    }
    private JButton botonNivel(String texto, Color fg, Color bg) {
        JButton b = new JButton(texto); b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("SansSerif",Font.BOLD,12));
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(fg,2),BorderFactory.createEmptyBorder(7,14,7,14)));
        b.setFocusPainted(false); b.setOpaque(true); b.setCursor(new Cursor(Cursor.HAND_CURSOR)); return b;
    }
    private DefaultTableModel modelo(String[] cols) {
        return new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c) { return false; } };
    }
    private JTable tabla(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setBackground(Tema.NAVY_ROW); t.setForeground(Tema.TEXT); t.setFont(new Font("SansSerif",Font.PLAIN,11));
        t.setRowHeight(22); t.setGridColor(new Color(0,100,100,40));
        t.setSelectionBackground(new Color(0,150,150,60)); t.setSelectionForeground(Tema.CYAN);
        t.setShowHorizontalLines(true); t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(Tema.NAVY_LIGHT); t.getTableHeader().setForeground(Tema.TEXT_MUTED);
        t.getTableHeader().setFont(new Font("SansSerif",Font.PLAIN,11));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb,Object v,boolean s,boolean f,int r,int c) {
                super.getTableCellRendererComponent(tb,v,s,f,r,c);
                setForeground(s?Tema.CYAN:Tema.TEXT);
                setBackground(s?new Color(0,120,120,60):(r%2==0?Tema.NAVY_ROW:Tema.NAVY_ROW2));
                setBorder(BorderFactory.createEmptyBorder(0,7,0,7)); return this;
            }
        });
        return t;
    }
    private DefaultTableCellRenderer montoRenderer() {
        return new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c) {
                super.getTableCellRendererComponent(t,v,s,f,r,c);
                String val=v!=null?v.toString():"";
                setForeground(val.startsWith("+")?Tema.INCOME:(val.startsWith("-")?Tema.EXPENSE:Tema.TEXT_MUTED));
                setBackground(r%2==0?Tema.NAVY_ROW:Tema.NAVY_ROW2); setHorizontalAlignment(RIGHT); return this;
            }
        };
    }
    private JScrollPane scroll(JTable t) {
        JScrollPane sp = new JScrollPane(t); sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Tema.NAVY_ROW); return sp;
    }
}
