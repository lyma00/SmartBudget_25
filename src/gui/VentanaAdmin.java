package gui;

import general.GestorUsuarios;
import general.Movimiento;
import general.Usuario;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class VentanaAdmin extends JFrame {

    private GestorUsuarios gestor;

    public VentanaAdmin(GestorUsuarios gestor) {
        this.gestor = gestor;
        setTitle("SmartBudget — Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setMinimumSize(new Dimension(720, 420));
        setLocationRelativeTo(null);
        construirUI();
    }

    private void construirUI() {
        PanelFondo root = new PanelFondo();
        root.setLayout(new BorderLayout());
        root.add(crearNavbar(), BorderLayout.NORTH);
        root.add(crearContenido(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel crearNavbar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 7));
        nav.setBackground(new Color(8, 15, 25, 240));
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER_C));
        JLabel logo = new JLabel("🪙"); logo.setFont(new Font("SansSerif", Font.PLAIN, 18)); nav.add(logo);
        nav.add(Tema.labelBold("SmartBudget", 14, Tema.CYAN));
        nav.add(Tema.label("—", 12, Tema.TEXT_DIM));
        nav.add(Tema.label("Panel Administrador", 12, Tema.TEXT_MUTED));
        JLabel salir = Tema.label("   Salir", 11, Tema.TEXT_DIM);
        salir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        salir.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); }
            public void mouseEntered(MouseEvent e) { salir.setForeground(Tema.CYAN); }
            public void mouseExited(MouseEvent e)  { salir.setForeground(Tema.TEXT_DIM); }
        });
        nav.add(salir);
        return nav;
    }

    private JPanel crearContenido() {
        JPanel p = new JPanel(new BorderLayout(0, 10)); p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JPanel topRow = new JPanel(new BorderLayout(10, 0)); topRow.setOpaque(false);
        topRow.add(Tema.label("Usuarios registrados — " + gestor.getUsuarios().size() + " en total", 18, Tema.TEXT), BorderLayout.WEST);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); searchRow.setOpaque(false);
        searchRow.add(Tema.label("Buscar por nombre:", 11, Tema.TEXT_MUTED));
        JTextField fBuscar = Tema.campo(); fBuscar.setPreferredSize(new Dimension(200, 30));
        searchRow.add(fBuscar);
        topRow.add(searchRow, BorderLayout.EAST);
        p.add(topRow, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout()); card.setBackground(new Color(10, 25, 40, 200));
        card.setBorder(BorderFactory.createLineBorder(Tema.BORDER_C, 1));
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 7)); h.setBackground(new Color(26, 51, 72, 180));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDER_C));
        h.add(Tema.label("Panel de Administrador", 12, Tema.TEXT_MUTED)); card.add(h, BorderLayout.NORTH);

        String[] cols = {"Usuario", "Nombre completo", "Edad", "Movimientos", "Saldo"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        llenarTabla(model, gestor.getUsuarios());

        JTable tabla = crearTabla(model);
        JScrollPane sp = new JScrollPane(tabla); sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Tema.NAVY_ROW);
        card.add(sp, BorderLayout.CENTER);
        p.add(card, BorderLayout.CENTER);

        fBuscar.addActionListener(e -> {
            gestor.cargarDatos();
            ArrayList<Usuario> resultado = gestor.buscarPorNombre(fBuscar.getText());
            model.setRowCount(0);
            llenarTabla(model, resultado);
        });
        
        fBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { buscar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { buscar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            private void buscar() {
                gestor.cargarDatos();
                ArrayList<Usuario> resultado = gestor.buscarPorNombre(fBuscar.getText());
                model.setRowCount(0);
                llenarTabla(model, resultado);
            }
        });

        return p;
    }

    private void llenarTabla(DefaultTableModel model, ArrayList<Usuario> lista) {
        for (Usuario u : lista) {
            double ing = 0, gas = 0;
            for (Movimiento mv : u.getMovimientos())
                if (mv.getTipo().equalsIgnoreCase("ingreso")) ing += mv.getMonto(); else gas += mv.getMonto();
            double sal = ing - gas;
            model.addRow(new Object[]{
                "@" + u.getNombreUsuario(),
                u.getNombre() + " " + u.getApellidoP() + " " + u.getApellidoM(),
                u.getEdad(), u.getMovimientos().size(),
                (sal >= 0 ? "+" : "") + "$" + String.format("%.2f", sal)
            });
        }
        if (lista.isEmpty())
            model.addRow(new Object[]{"—", "Sin resultados", "—", "—", "—"});
    }

    private JTable crearTabla(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setBackground(Tema.NAVY_ROW); t.setForeground(Tema.TEXT);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setRowHeight(24); t.setGridColor(new Color(0, 100, 100, 40));
        t.setSelectionBackground(new Color(0, 150, 150, 60)); t.setSelectionForeground(Tema.CYAN);
        t.setShowHorizontalLines(true); t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(Tema.NAVY_LIGHT); t.getTableHeader().setForeground(Tema.TEXT_MUTED);
        t.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(tb, v, s, f, r, c);
                String val = v != null ? v.toString() : "";
                if (c == 0) setForeground(Tema.CYAN);
                else if (c == 4) setForeground(val.startsWith("-") ? Tema.EXPENSE : Tema.INCOME);
                else setForeground(s ? Tema.CYAN : Tema.TEXT);
                setBackground(s ? new Color(0,120,120,60) : (r%2==0 ? Tema.NAVY_ROW : Tema.NAVY_ROW2));
                if (c == 4) setHorizontalAlignment(RIGHT); else setHorizontalAlignment(LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8)); return this;
            }
        });
        return t;
    }
}
