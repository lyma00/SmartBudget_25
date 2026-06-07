package clasesmenu;

import general.Usuario;
import general.Validaciones;
import general.Movimiento;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuInicio{
    private ArrayList<Usuario> usuarios;
    private Scanner sc;

    public MenuInicio(){
        this.usuarios = new ArrayList<Usuario>();
        this.sc = new Scanner(System.in);
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void mostrarBienvenida() {
        System.out.println("Bienvenido a SmartBudget");
    }

    public void ejecutar(){
        char continuar = 'S';

        do{
            mostrarBienvenida();
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Crear cuenta");
            System.out.println("3. Salir");
            int opcion = Validaciones.leerOpcion(sc, 1, 3);

            switch(opcion){
                case 1:
                    iniciarSesion();
                    break;
                    
                case 2:
                    crearCuenta();
                    break;

                case 3:
                    System.out.println("Saliendo...");
                    continuar = 'N';
                    break;
            }
        }
        while(continuar == 'S');
    }

    private void iniciarSesion(){
        System.out.println("Iniciar sesion");

        String nombreUsuario = Validaciones.leerTextoNoVacio(sc, "Usuario: ");
        String contrasena = Validaciones.leerTextoNoVacio(sc, "Contrasena: ");

        if(nombreUsuario.trim().isEmpty() || contrasena.trim().isEmpty()){
            System.out.println("No se puede ingresar datos vacios");
            return;
        }

        if(nombreUsuario.equalsIgnoreCase("administrador") && contrasena.equalsIgnoreCase("administrador")){
            MenuAdministrador menuAdmin = new MenuAdministrador(usuarios, sc);
            menuAdmin.ejecutar();
            return;
        }

        Usuario usuarioEncontrado = null;
        boolean usuarioExiste = false;

        for(int i = 0; i < usuarios.size(); i++){
            Usuario u = usuarios.get(i);
            String uNombre = u.getNombreUsuario();

            if(uNombre.equals(nombreUsuario)){
                usuarioExiste = true;
                String uContrasena = u.getContrasena();

                if(uContrasena.equals(contrasena)){
                    usuarioEncontrado = u;
                }
            }
        }

        if(!usuarioExiste){
            System.out.println("Usuario no registrado");
            return;
        }

        if(usuarioEncontrado == null){
            System.out.println("Datos incorrectos");
            return;
        }

        MenuUsuario menuUsuario = new MenuUsuario(usuarioEncontrado, sc);
        menuUsuario.ejecutar();
    }

    private void crearCuenta(){
        System.out.println("Crear Cuenta");

        String nombre = Validaciones.leerNombre(sc, "Nombre", false);

        String apellidoP = Validaciones.leerNombre(sc, "Apellido paterno", false);

        String apellidoM = Validaciones.leerNombre(sc, "Apellido materno", false);

        int edad = Validaciones.leerEdad(sc);

        String nombreUsuario = "";
        boolean usuarioDisponible = false;

        do{
            nombreUsuario = Validaciones.leerNombreUsuario(sc);
            boolean existe = false;

            for(int i = 0; i < usuarios.size(); i++){
                Usuario u = usuarios.get(i);
                String uNombre = u.getNombreUsuario();

                if(uNombre.equals(nombreUsuario)){
                    existe = true;
                }
            }

            if(existe){
                System.out.println("Usuario ya registrado, elige otro nombre");
                
            }else{
                usuarioDisponible = true;
            }
        }
        while(!usuarioDisponible);

        String contrasena = Validaciones.leerContrasena(sc);

        boolean contrasenaConfirmada = false;

        do{
            System.out.print("Confirma tu contrasena: ");
            String confirmacion = sc.nextLine();

            if(confirmacion.equals(contrasena)){
                contrasenaConfirmada = true;
                
            }else{
                System.out.println("La contraseña esta mal");
            }
        }
        while(!contrasenaConfirmada);

        Usuario nuevoUsuario = new Usuario(nombre, apellidoP, apellidoM, edad, nombreUsuario, contrasena);

        System.out.println("Registro de movimientos");
        System.out.println("Ingrese sus ingresos y gastos del mes");

        char agregarMov = 'S';

        do{
            System.out.println("Nuevo movimiento");
            System.out.println("1. Ingreso");
            System.out.println("2. Gasto");
            int tipoOpcion = Validaciones.leerOpcion(sc, 1, 2);
            String tipo = "";

            if(tipoOpcion == 1){
                tipo = "ingreso";
                
            }else{
                tipo = "gasto";
            }

            String descripcion = Validaciones.leerTextoNoVacio(sc, "Descripcion: ");
            double monto = Validaciones.leerMonto(sc, "Monto: $");
            Movimiento mov = new Movimiento(descripcion, monto, tipo, false);
            nuevoUsuario.agregarMovimiento(mov);
            System.out.println("Movimiento registrado.");
            agregarMov = Validaciones.leerSN(sc, "Desea agregar otro movimiento? (S/N): ");
        }
        while(agregarMov == 'S');

        System.out.println("Clasificacion de gastos");
        System.out.println("Indique cuales son gastos de pirmer nivel");

        ArrayList<Movimiento> movimientos = nuevoUsuario.getMovimientos();

        for(int i = 0; i < movimientos.size(); i++){
            Movimiento mov = movimientos.get(i);
            String tipo = mov.getTipo();

            if(tipo.equalsIgnoreCase("gasto")){
                String descripcion = mov.getDescripcion();
                double monto = mov.getMonto();
                System.out.println((i + 1) + ". " + descripcion + " - $" + monto);
                char esEsencial = Validaciones.leerSN(sc, "Es un gasto de primer nivel? (S/N): ");

                if(esEsencial == 'S'){
                    mov.setEsencial(true);
                    
                }else{
                    mov.setEsencial(false);
                }
            }
        }

        usuarios.add(nuevoUsuario);
        System.out.println("Registro exitoso. Bienvenido " + nombre + " " + apellidoP + ".");
        MenuUsuario menuUsuario = new MenuUsuario(nuevoUsuario, sc);
        menuUsuario.ejecutar();
    }

}