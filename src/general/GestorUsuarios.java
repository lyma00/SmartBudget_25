package general;

import java.util.ArrayList;
import persistencia.BaseDatos;

public class GestorUsuarios {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Movimiento> movimientos;
    private BaseDatos basedatos;

    public GestorUsuarios() {
    this.basedatos   = new BaseDatos(); 
    this.usuarios    = new ArrayList<>();
    this.movimientos = new ArrayList<>();
    cargarDatos();                     
}

public GestorUsuarios(ArrayList<Usuario> usuarios) {
    this.basedatos   = new BaseDatos(); 
    this.usuarios    = usuarios;
    this.movimientos = new ArrayList<>();
}

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public boolean esAdministrador(String cuenta, String pass) {
        return cuenta.equalsIgnoreCase("administrador")
            && pass.equalsIgnoreCase("administrador");
    }

    public enum ResultadoLogin { ok, usuarioInexistente, contyraseñaIncorrecta }

   public ResultadoLogin login(String cuenta, String pass) {
    Usuario u = basedatos.consultarUsuario(cuenta);
    if (u == null) return ResultadoLogin.usuarioInexistente;
    return u.getContrasena().equals(pass)
        ? ResultadoLogin.ok
        : ResultadoLogin.contyraseñaIncorrecta;
    }

    public Usuario buscarPorCuenta(String cuenta) {
        Usuario u = basedatos.consultarUsuario(cuenta);
            if (u != null) {
            ArrayList<Movimiento> movs = basedatos.consultarMovimientosDeUsuario(cuenta);
            u.setMovimientos(movs);
        }
        return u;
    }

    public enum ResultadoRegistro {
        ok, espacioBasio, usuarioDuplicado, contraseñasIncorrecta,
        contraseCorta, edadInvalida
    }

    public ResultadoRegistro registrar(String cuenta, String nombre, String apellidoP,
            String apellidoM, String edadStr, String pass, String pass2) {

        if (cuenta.isEmpty() || nombre.isEmpty() || apellidoP.isEmpty() || pass.isEmpty())
            return ResultadoRegistro.espacioBasio;

        if (basedatos.consultarUsuario(cuenta) != null)
            return ResultadoRegistro.usuarioDuplicado;

        if (!pass.equals(pass2))
            return ResultadoRegistro.contraseñasIncorrecta;

        if (pass.length() < 8)
            return ResultadoRegistro.contraseCorta;

        int edad = 0;
        try { edad = Integer.parseInt(edadStr); } catch (Exception ignored) {}

        Usuario nuevo = new Usuario(nombre, apellidoP, apellidoM, edad, cuenta, pass);
        
        int idTitular = basedatos.agregarUsuario(nuevo);
        if (idTitular != -1) {
            basedatos.crearEstadoCuenta(idTitular, 0.0);
        }
        cargarDatos(); // refresca lista local
        return ResultadoRegistro.ok;
    }

    public String mensajeRegistro(ResultadoRegistro r) {
        switch (r) {
            case espacioBasio: return "Completa los campos obligatorios (*)";
            case usuarioDuplicado: return "Ese nombre de usuario ya está registrado";
            case contraseñasIncorrecta: return "Las contraseñas no coinciden";
            case contraseCorta: return "Mínimo 8 caracteres en la contraseña";
            default:return "";
        }
    }

    public ArrayList<Usuario> buscarPorNombre(String texto) {
        ArrayList<Usuario> resultado = new ArrayList<>();
        String q = texto.trim().toLowerCase();
        if (q.isEmpty()) return new ArrayList<>(usuarios);
        for (Usuario u : usuarios) {
            String nombreCompleto = (u.getNombre() + " " + u.getApellidoP()
                + " " + u.getApellidoM()).toLowerCase();
            if (nombreCompleto.contains(q)) resultado.add(u);
        }
        return resultado;
    }
    
    public void cargarDatos(){
        usuarios = basedatos.ConsultarTodosLosUsuarios();
        movimientos = basedatos.consultarTodosLosMovimientos();
        for (Usuario u : usuarios) {
            ArrayList<Movimiento> movs = basedatos.consultarMovimientosDeUsuario(u.getNombreUsuario());
            u.setMovimientos(movs);
        }
    }
    
    public void agregarUsuarioEnBD(Usuario usuario) {
        int idTitular = basedatos.agregarUsuario(usuario);
        if (idTitular != -1) {
            basedatos.crearEstadoCuenta(idTitular, 0.0);
        }
        cargarDatos();
    }
    
    public void modificarUsuario( String nombre, String apellidoP, String apellidoM, int edad, String nuevoNombreUsuario, String contrasena,String nombreUsuarioActual){
        basedatos.modificarUsuario(nombre, apellidoP, apellidoM, edad, nuevoNombreUsuario, contrasena, nombreUsuarioActual);
        cargarDatos();
    }
    
    public ArrayList<Usuario> consultarTodosLosUsuarios(){
        return basedatos.ConsultarTodosLosUsuarios();
    }
    
    public Usuario consultarUsuario(String nombreUsuario){
        return basedatos.consultarUsuario(nombreUsuario);
    }
    
    public void eliminarUsuario(Usuario usuario){
        basedatos.eliminarUsuario(usuario);
        cargarDatos();
    }
    
     public void agregarMovimiento(Movimiento movimiento, int idEstadoCuenta){
        movimientos.add(movimiento);
        basedatos.agregarMovimiento(movimiento, idEstadoCuenta);    
    }
     
    public void modificarMovimiento(String nuevadescripcion, double nuevomonto, String nuevotipo, boolean esencial, String descripcionanterior, double montoanterior){
        basedatos.modificarMovimiento(nuevadescripcion, nuevomonto, nuevotipo, esencial, descripcionanterior, montoanterior);
        cargarDatos();
    }
    
    public void eliminarMovimento(Movimiento movimientos){
        basedatos.eliminarMovimiento(movimientos);
        cargarDatos();
    }
    
    public ArrayList<Movimiento> consultarTodosLosMovimientos(){
        return basedatos.consultarTodosLosMovimientos();
    }


}
