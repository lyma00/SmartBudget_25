package clasesmenu;

import general.Usuario;
import general.Validaciones;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuAdministrador{
    private ArrayList<Usuario> usuarios;
    private Scanner sc;

    public MenuAdministrador(ArrayList<Usuario> usuarios, Scanner sc){
        this.usuarios = usuarios;
        this.sc = sc;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void ejecutar(){
        char continuar = 'S';
        do{
            System.out.println("Menu del administrador");
            System.out.println("1. Ver todos los usuarios");
            System.out.println("2. Cerrar secion de administrador");
            int opcion = Validaciones.leerOpcion(sc, 1, 4);
            switch (opcion){
                case 1:
                    System.out.println(getUsuarios());
                    break;

                case 2:
                    System.out.println("Saliendo...");
                    continuar = 'N';
                    break;
            }
        }
        while (continuar == 'S');
    }

}

