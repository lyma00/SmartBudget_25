/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import general.*;
/**
 *
 * 
 */
public class BaseDatos {
    
    private Connection getConnection() throws SQLException {
     
             String url= "jdbc:mysql://localhost:3306/smartbudget?"
                     +"useSSL=false"
                     +"&allowPublicKeyRetrevial=true"
                     +"&serverTimezone=UTC";
             String user = "root";
             String pass = "r31ra";
             return DriverManager.getConnection(url,user,pass);
    }
    
    
    public int agregarUsuario(Usuario usuario) {
    String sql = "INSERT INTO mtitular(nombreUsuario, nombre, apellidoP, apellidoM, edad, contrasena, Id_Administrador) VALUES (?,?,?,?,?,?,?)";
    try(Connection conn = getConnection();PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
        pstmt.setString(1, usuario.getNombreUsuario());
        pstmt.setString(2, usuario.getNombre());
        pstmt.setString(3, usuario.getApellidoP());
        pstmt.setString(4, usuario.getApellidoM());
        pstmt.setInt(5, usuario.getEdad());
        pstmt.setString(6, usuario.getContrasena());
        pstmt.setString(7, "administrador");
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()) return rs.getInt(1);
    } catch(SQLException e){
        System.out.println("Error al agregar usuario: " + e.getMessage());
    }
    return -1;
    }
    
    public int crearEstadoCuenta(int idTitular, double saldoInicial) {
    String sql = "INSERT INTO eestadocuenta(Total_cargo, Saldoinicial, Id_Titular) VALUES (?,?,?)";
    try(Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
        pstmt.setDouble(1, 0);
        pstmt.setDouble(2, saldoInicial);
        pstmt.setInt(3, idTitular);
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()) return rs.getInt(1); // retorna el Id_EstadoCuenta generado
    } catch(SQLException e){
        System.out.println("Error al crear estado de cuenta: " + e.getMessage());
    }
    return -1;
    }
    
    public int obtenerIdEstadoCuenta(String nombreUsuario) {
    String sql = "SELECT e.Id_EstadoCuenta FROM eestadocuenta e " +
                 "JOIN mtitular t ON e.Id_Titular = t.Id_Titular " +
                 "WHERE t.nombreUsuario = ?";
    try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setString(1, nombreUsuario);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) return rs.getInt("Id_EstadoCuenta");
    } catch(SQLException e){
        System.out.println("Error al obtener estado de cuenta: " + e.getMessage());
       
    }
    return -1;
    }
    
    public ArrayList<Usuario> ConsultarTodosLosUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM mtitular";
         try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                Usuario u = new Usuario(rs.getString("nombre"), rs.getString("apellidoP"), rs.getString("apellidoM"), rs.getInt("edad"), rs.getString("nombreUsuario"), rs.getString("contrasena"));
                lista.add(u);
            }
        } catch(SQLException e){
            System.out.println("Error al consultar a los usuarios" + e.getMessage());
        }
        return lista;
    }
    
    public Usuario consultarUsuario(String nombreUsuario){
    Usuario us = null;
    String sql = "SELECT * FROM mtitular WHERE nombreUsuario = ?";
    try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setString(1, nombreUsuario); // ahora usa el valor real
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            us = new Usuario(
                rs.getString("nombre"),
                rs.getString("apellidoP"),
                rs.getString("apellidoM"),
                rs.getInt("edad"),
                rs.getString("nombreUsuario"),
                rs.getString("contrasena")
            );
        }
    } catch(SQLException e){
        System.out.println("Error al consultar al usuario: " + e.getMessage());
    }
    return us;
    }
    
    public void modificarUsuario( String nombre, String apellidoP, String apellidoM, int edad, String nuevoNombreUsuario, String contrasena, String nombreUsuarioActual){
        String sql = "UPDATE mtitular SET nombre = ?, apellidoP = ?, apellidoM = ?, edad = ?, nombreUsuario = ?, contrasena = ? WHERE nombreUsuario = ?";
        try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidoP);
            pstmt.setString(3, apellidoM);
            pstmt.setInt(4, edad);
            pstmt.setString(5, nuevoNombreUsuario);
            pstmt.setString(6, contrasena);
            pstmt.setString(7, nombreUsuarioActual);
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Usuario actualizado correctamente");
            } else {
                System.out.println("usuario no encontrado");
            }
        }catch(SQLException e){
            System.out.println("Error al modificar al usuario " + e.getMessage());
        }
    }
    
    public void eliminarUsuario(Usuario usuario){
       String sql = "DELETE FROM mtitular WHERE nombreUsuario=?";
       try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
           pstmt.setString(1, usuario.getNombreUsuario());
           pstmt.executeUpdate();
       } catch(SQLException e){
           System.out.println("Error al eliminar usuario " + e.getMessage());
       }
    }
    
    public ArrayList<Movimiento> consultarMovimientosDeUsuario(String nombreUsuario) {
    ArrayList<Movimiento> lista = new ArrayList<>();
    String sql = "SELECT d.descripcion, d.monto, d.tipo, d.categoriagasto " +
                 "FROM destadocuenta d " +
                 "JOIN eestadocuenta e ON d.Id_eEstadoCuenta = e.Id_EstadoCuenta " +
                 "JOIN mtitular t ON e.Id_Titular = t.Id_Titular " +
                 "WHERE t.nombreUsuario = ?";
    try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setString(1, nombreUsuario);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            Movimiento m = new Movimiento(
                rs.getString("descripcion"),
                rs.getDouble("monto"),
                rs.getString("tipo"),
                rs.getBoolean("categoriagasto")
            );
            lista.add(m);
        }
    } catch(SQLException e){
        System.out.println("Error al consultar movimientos: " + e.getMessage());
    }
    return lista;
    }
    
    public ArrayList<Movimiento> consultarTodosLosMovimientos (){
        ArrayList<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM destadocuenta";
        try(Connection conn= getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()){
            while(rs.next()){
            Movimiento m = new Movimiento();
            m.setMonto(rs.getDouble("monto"));
            m.setTipo(rs.getString("tipo"));
            rs.getString("tipo");
            rs.getBoolean("categoriagasto");
            lista.add(m);
            }
        }catch(SQLException e){
            System.out.println("Error al eliminar el monto " + e.getMessage());
            
        }
        return lista;
    }
    
    public Movimiento consultarMovimientoMinimo() {
        Movimiento move = null;
        String sql = "SELECT descripcion, monto, tipo, categoriagasto FROM destadocuenta WHERE monto = (SELECT MIN(monto) FROM destadocuenta)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                move = new Movimiento(rs.getString("descripcion"), rs.getDouble("monto"), rs.getString("tipo"), rs.getBoolean("escencial"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar movimiento minimo: " + e.getMessage());
        }
        return move;
    }
    
    public Movimiento consultarMovimientoMaximo(){
        Movimiento move = null;
        String sql = "SELECT descripcion, monto, tipo, categoriagasto FROM destadocuenta WHERE monto = (SELECT MAX(monto) FROM destadocuenta)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                move = new Movimiento (rs.getString("descripcion"), rs.getDouble("monto"), rs.getString("tipo"), rs.getBoolean("escencial"));
            }
        } catch (SQLException e){
            System.out.println("Error al consultar movimiento maximo " + e.getMessage());
        }
        return move;
    }
    
    public void modificarMovimiento(String nuevadescripcion, double nuevomonto, String nuevotipo, boolean categoriagasto, String descripcionanterior, double montoanterior){
        String sql = "UPDATE destadocuenta SET descripcion = ?, monto = ?, tipo = ?, categoriagasto = ? WHERE monto = ? AND descripcion = ?";
        try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nuevadescripcion);
            pstmt.setDouble(2, nuevomonto);
            pstmt.setString(3, nuevotipo);
            pstmt.setBoolean(4, categoriagasto);
            pstmt.setDouble(5, montoanterior);
            pstmt.setString(6, descripcionanterior);
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Monto actualizado correctamente");
            } else {
                System.out.println("monto y descripcion no encontrado");
            }
        }catch(SQLException e){
            System.out.println("Error al modificar el movimiento " + e.getMessage());
        }
    }
    
    public void eliminarMovimiento(Movimiento movimiento){
        String sql= "DELETE FROM destadocuenta WHERE monto=? AND descripcion = ?";
        try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setDouble(1, movimiento.getMonto());
            pstmt.setString(2, movimiento.getDescripcion());
            pstmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error al eliminar el movimiento" + e.getMessage());
        }
    }
    
    public void agregarMovimiento(Movimiento movimiento, int idEstadoCuenta) {
    String sql = "INSERT INTO destadocuenta(descripcion, monto, tipo, Id_eEstadoCuenta) VALUES (?,?,?,?)";
    try(Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
        
        conn.setAutoCommit(true);
        
        pstmt.setString(1, movimiento.getDescripcion());
        pstmt.setDouble(2, movimiento.getMonto());
        pstmt.setString(3, movimiento.getTipo());
        pstmt.setInt(4, idEstadoCuenta);
        
        pstmt.executeUpdate();
    } catch(SQLException e){
        System.out.println("Error al agregar movimiento: " + e.getMessage());
    }
    }
    
    public void actualizarEsencialMovimiento(Movimiento movimiento) {
    String sql = "UPDATE destadocuenta SET categoriagasto = ? WHERE descripcion = ? AND monto = ?";
    try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setBoolean(1, movimiento.isEsencial());
        pstmt.setString(2, movimiento.getDescripcion());
        pstmt.setDouble(3, movimiento.getMonto());
        pstmt.executeUpdate();
    } catch(SQLException e){
        System.out.println("Error al actualizar clasificacion: " + e.getMessage());
    }
    }
    
}
