package ejecucion;

import general.GestorUsuarios;
import gui.VentanaLogin;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> {
        GestorUsuarios gestor = new GestorUsuarios();
            new VentanaLogin(gestor).setVisible(true);
        });
    }
}
