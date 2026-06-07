package gui;

import general.GestorUsuarios;
import general.GestorUsuarios.ResultadoLogin;
import general.GestorUsuarios.ResultadoRegistro;
import general.Usuario;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class VentanaLogin extends JFrame {

    private GestorUsuarios gestor;
    private CardLayout cardLayout;
    private JPanel panelTarjetas;

    public VentanaLogin(GestorUsuarios gestor) {
        this.gestor = gestor; 
        setTitle("SmartBudget");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 580);
        setMinimumSize(new Dimension(780, 500));
        setLocationRelativeTo(null);

        cardLayout    = new CardLayout();
        panelTarjetas = new JPanel(cardLayout);
        panelTarjetas.add(crearPanelLogin(),    "login");
        panelTarjetas.add(crearPanelRegistro(), "registro");
        add(panelTarjetas);
        cardLayout.show(panelTarjetas, "login");
    }

    private JPanel crearPanelLogin() {
        PanelFondo fondo = new PanelFondo();
        fondo.setLayout(new BorderLayout());
        fondo.add(Tema.navbar(""), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(10, 25, 40, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.BORDER_C, 1),
            BorderFactory.createEmptyBorder(40, 60, 40, 60)));
        card.setPreferredSize(new Dimension(500, 320));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; g.gridx = 0;

        g.gridy = 0; g.insets = new Insets(0, 0, 6, 0);
        JLabel lblErr = Tema.label("", 11, new Color(255, 100, 100));
        lblErr.setVisible(false);
        card.add(lblErr, g);

        g.gridy = 1; g.insets = new Insets(0, 0, 4, 0);
        card.add(Tema.label("Cuenta:", 11, Tema.TEXT_MUTED), g);
        g.gridy = 2; g.insets = new Insets(0, 0, 18, 0);
        JTextField fCuenta = Tema.campo();
        card.add(fCuenta, g);

        g.gridy = 3; g.insets = new Insets(0, 0, 4, 0);
        card.add(Tema.label("Contraseña:", 11, Tema.TEXT_MUTED), g);
        g.gridy = 4; g.insets = new Insets(0, 0, 30, 0);
        JPasswordField fPass = Tema.password();
        card.add(fPass, g);

        g.gridy = 5; g.insets = new Insets(0, 0, 16, 0);
        JButton btnLogin = Tema.boton("Login");
        btnLogin.setPreferredSize(new Dimension(380, 36));
        card.add(btnLogin, g);

        g.gridy = 6; g.insets = new Insets(0, 0, 0, 0);
        JLabel linkReg = new JLabel("<html><center>¿No tiene cuenta? <font color='#00CFCF'>Regístrate ahora</font></center></html>");
        linkReg.setFont(new Font("SansSerif", Font.PLAIN, 11));
        linkReg.setForeground(Tema.TEXT_DIM);
        linkReg.setHorizontalAlignment(SwingConstants.CENTER);
        linkReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkReg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { mostrarRegistroLimpio(); }
        });
        card.add(linkReg, g);

        btnLogin.addActionListener(e -> {
            String cuenta = fCuenta.getText().trim();
            String pass   = new String(fPass.getPassword());
            lblErr.setVisible(false);

            if (gestor.esAdministrador(cuenta, pass)) {
                abrirAdmin(); return;
            }
            ResultadoLogin res = gestor.login(cuenta, pass);
            if (res == ResultadoLogin.usuarioInexistente) {
                lblErr.setText("Usuario no registrado."); lblErr.setVisible(true); return;
            }
            if (res == ResultadoLogin.contyraseñaIncorrecta) {
                lblErr.setText("Contraseña incorrecta."); lblErr.setVisible(true); return;
            }
            abrirUsuario(gestor.buscarPorCuenta(cuenta));
        });
        fPass.addActionListener(e -> btnLogin.doClick());

        centro.add(card, new GridBagConstraints());
        fondo.add(centro, BorderLayout.CENTER);
        return fondo;
    }

    private JPanel crearPanelRegistro() {
        PanelFondo fondo = new PanelFondo();
        fondo.setLayout(new BorderLayout());
        fondo.add(Tema.navbar(""), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(10, 25, 40, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.BORDER_C, 1),
            BorderFactory.createEmptyBorder(26, 44, 26, 44)));
        card.setPreferredSize(new Dimension(620, 430));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);

        g.gridx = 0; g.gridy = 0; g.gridwidth = 3;
        card.add(Tema.label("Registro de cuenta", 20, Tema.TEXT), g);
        g.gridy = 1;
        JSeparator sep = new JSeparator(); sep.setForeground(Tema.BORDER_C);
        card.add(sep, g);

        JLabel lblErr = Tema.label("", 11, new Color(255, 100, 100));
        lblErr.setVisible(false);
        g.gridy = 2; card.add(lblErr, g);

        g.gridy = 3; g.gridwidth = 3; g.gridx = 0;
        card.add(Tema.label("*Cuenta:", 11, Tema.TEXT_MUTED), g);
        g.gridy = 4;
        JTextField fUser = Tema.campo(); card.add(fUser, g);

        g.gridy = 5; g.gridwidth = 3; g.gridx = 0;
        card.add(Tema.label("*Nombre:", 11, Tema.TEXT_MUTED), g);
        g.gridy = 6; g.insets = new Insets(3, 5, 10, 5);
        JTextField fN1 = Tema.campo();
        fN1.setPreferredSize(new Dimension(0, 42));
        fN1.setFont(new Font("SansSerif", Font.PLAIN, 15));
        soloLetras(fN1); card.add(fN1, g);

        g.gridy = 7; g.gridwidth = 1; g.insets = new Insets(3, 5, 3, 5);
        g.gridx = 0; card.add(Tema.label("*Apellido Paterno:", 11, Tema.TEXT_MUTED), g);
        g.gridx = 1; card.add(Tema.label("*Apellido Materno:", 11, Tema.TEXT_MUTED), g);
        g.gridx = 2; card.add(new JLabel(""), g);
        g.gridy = 8;
        JTextField fAP = Tema.campo(); fAP.setPreferredSize(new Dimension(0, 36)); soloLetras(fAP);
        JTextField fAM = Tema.campo(); fAM.setPreferredSize(new Dimension(0, 36)); soloLetras(fAM);
        g.gridx = 0; card.add(fAP, g);
        g.gridx = 1; card.add(fAM, g);
        g.gridx = 2; card.add(new JLabel(""), g);

        g.gridy = 9; g.gridwidth = 1;
        g.gridx = 0; card.add(Tema.label("*Edad:", 11, Tema.TEXT_MUTED), g);
        g.gridx = 1; card.add(Tema.label("*Contraseña:", 11, Tema.TEXT_MUTED), g);
        g.gridx = 2; card.add(Tema.label("*Confirmación:", 11, Tema.TEXT_MUTED), g);
        g.gridy = 10;
        JTextField fEdad = Tema.campo();
        JPasswordField fP = Tema.password(), fP2 = Tema.password();
        g.gridx = 0; card.add(fEdad, g);
        g.gridx = 1; card.add(fP, g);
        g.gridx = 2; card.add(fP2, g);

        g.gridy = 11; g.gridwidth = 3; g.gridx = 0; g.insets = new Insets(18, 5, 4, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        JButton btnReg = Tema.botonGrande("Terminar registro");
        card.add(btnReg, g);

        g.gridy = 12; g.insets = new Insets(3, 5, 3, 5);
        JLabel linkL = new JLabel("<html><center>¿Ya tienes cuenta? <font color='#00CFCF'>Iniciar sesión</font></center></html>");
        linkL.setFont(new Font("SansSerif", Font.PLAIN, 11));
        linkL.setForeground(Tema.TEXT_DIM);
        linkL.setHorizontalAlignment(SwingConstants.CENTER);
        linkL.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkL.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { cardLayout.show(panelTarjetas, "login"); }
        });
        card.add(linkL, g);

        btnReg.addActionListener(e -> {
            ResultadoRegistro res = gestor.registrar(
                fUser.getText().trim(), fN1.getText().trim(),
                fAP.getText().trim(),  fAM.getText().trim(),
                fEdad.getText().trim(),
                new String(fP.getPassword()), new String(fP2.getPassword())
            );
            if (res != ResultadoRegistro.ok) {
                lblErr.setText(gestor.mensajeRegistro(res));
                lblErr.setVisible(true);
                return;
            }
            lblErr.setVisible(false);
            abrirUsuario(gestor.buscarPorCuenta(fUser.getText().trim()));
        });

        centro.add(card, new GridBagConstraints());
        fondo.add(centro, BorderLayout.CENTER);
        return fondo;
    }

    private void mostrarRegistroLimpio() {
        panelTarjetas.remove(1);
        panelTarjetas.add(crearPanelRegistro(), "registro", 1);
        panelTarjetas.revalidate();
        cardLayout.show(panelTarjetas, "registro");
    }

    private void abrirUsuario(Usuario u) {
        VentanaUsuario vu = new VentanaUsuario(u, gestor);
        vu.setVisible(true); setVisible(false);
        vu.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                gestor.cargarDatos();
                setVisible(true);
                mostrarRegistroLimpio();
                cardLayout.show(panelTarjetas, "login");
            }
        });
    }

    private void abrirAdmin() {
         gestor.cargarDatos();
        VentanaAdmin va = new VentanaAdmin(gestor);
        va.setVisible(true); setVisible(false);
        va.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) { setVisible(true); }
        });
    }

    private void soloLetras(JTextField campo) {
        ((javax.swing.text.AbstractDocument) campo.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                public void insertString(FilterBypass fb, int off, String txt,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (txt != null && txt.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]*"))
                        super.insertString(fb, off, txt, a);
                }
                public void replace(FilterBypass fb, int off, int len, String txt,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (txt != null && txt.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]*"))
                        super.replace(fb, off, len, txt, a);
                }
            });
    }
}
