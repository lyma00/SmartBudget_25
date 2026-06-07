package general;

import java.util.ArrayList;

public class Usuario{
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private int edad;
    private String nombreUsuario;
    private String contrasena;
    private ArrayList<Movimiento> movimientos;
    private ArrayList<Usuario> usuarios;

    public Usuario(){
        this.nombre = "";
        this.apellidoP = "";
        this.apellidoM = "";
        this.edad = 0;
        this.nombreUsuario = "";
        this.contrasena = "";
        this.movimientos = new ArrayList<Movimiento>();
        this.usuarios = new ArrayList<Usuario>();
    }

    public Usuario(String nombre, String apellidoP, String apellidoM, int edad, String nombreUsuario, String contrasena){
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.edad = edad;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.movimientos = new ArrayList<Movimiento>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(ArrayList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public void agregarMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
    }
    
}