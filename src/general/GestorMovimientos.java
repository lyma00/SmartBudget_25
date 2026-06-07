package general;

import java.util.ArrayList;
import persistencia.BaseDatos;

public class GestorMovimientos {
    
    private BaseDatos basedatos = new BaseDatos();
    private Usuario usuario;

    public GestorMovimientos(Usuario usuario) {
        this.basedatos = new BaseDatos();
        this.usuario   = usuario;
    }

    public enum ResultadoAgregar { ok, espacioBasio, montoInvalido, montoNegativo, monto_exedido }

    public ResultadoAgregar agregar(String descripcion, String montoStr, String tipo, boolean esencial) {
        if (descripcion.isEmpty()) return ResultadoAgregar.espacioBasio;
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            return ResultadoAgregar.montoInvalido;
        }
        if (monto <= 0) return ResultadoAgregar.montoNegativo;
        if (monto > 10_000_000) return ResultadoAgregar.monto_exedido;
        
        Movimiento nuevo = new Movimiento(descripcion, monto, tipo, esencial);
        usuario.agregarMovimiento(nuevo);

        int idEstadoCuenta = basedatos.obtenerIdEstadoCuenta(usuario.getNombreUsuario());
        if (idEstadoCuenta != -1) {
            basedatos.agregarMovimiento(nuevo, idEstadoCuenta);
        }
        return ResultadoAgregar.ok;
    }

    public String mensajeAgregar(ResultadoAgregar r) {
        switch (r) {
            case espacioBasio: return "La descripción no puede estar vacía.";
            case montoInvalido: return "El monto no es un número válido.";
            case montoNegativo: return "El monto debe ser mayor a cero.";
            case monto_exedido: return "El monto no puede ser mayor a $10,000,000.";
            default: return "";
        }
    }

    public boolean eliminar(int indice) {
        ArrayList<Movimiento> movs = usuario.getMovimientos();
        if (indice < 0 || indice >= movs.size()) return false;
        Movimiento m = movs.get(indice);
        basedatos.eliminarMovimiento(m); 
        movs.remove(indice);             
        return true;
    }

    public void cambiarNivel(int indice, boolean esencial) {
        ArrayList<Movimiento> movs = usuario.getMovimientos();
        if (indice >= 0 && indice < movs.size()){
            Movimiento m = movs.get(indice);
            m.setEsencial(esencial);
            basedatos.actualizarEsencialMovimiento(m);
        }
    }

    public double totalIngresos() {
        return usuario.getMovimientos().stream()
            .filter(m -> m.getTipo().equalsIgnoreCase("ingreso"))
            .mapToDouble(Movimiento::getMonto).sum();
    }

    public double totalGastos() {
        return usuario.getMovimientos().stream()
            .filter(m -> m.getTipo().equalsIgnoreCase("gasto"))
            .mapToDouble(Movimiento::getMonto).sum();
    }

    public double saldo() { return totalIngresos() - totalGastos(); }

    public double gastoMaximo() {
        return usuario.getMovimientos().stream()
            .filter(m -> m.getTipo().equalsIgnoreCase("gasto"))
            .mapToDouble(Movimiento::getMonto).max().orElse(0);
    }

    public double gastoMinimo() {
        return usuario.getMovimientos().stream()
            .filter(m -> m.getTipo().equalsIgnoreCase("gasto"))
            .mapToDouble(Movimiento::getMonto).min().orElse(0);
    }

    public double totalNoEsenciales() {
        return usuario.getMovimientos().stream()
            .filter(m -> m.getTipo().equalsIgnoreCase("gasto") && !m.isEsencial())
            .mapToDouble(Movimiento::getMonto).sum();
    }

    public double porcentajeGasto() {
        double ing = totalIngresos();
        return ing > 0 ? (totalGastos() / ing) * 100 : 0;
    }

    public ArrayList<Movimiento> soloGastos() {
        ArrayList<Movimiento> result = new ArrayList<>();
        for (Movimiento m : usuario.getMovimientos())
            if (m.getTipo().equalsIgnoreCase("gasto")) result.add(m);
        return result;
    }

    public ArrayList<Movimiento> gastosPorMonto() {
        ArrayList<Movimiento> gastos = soloGastos();
        gastos.sort((a, b) -> Double.compare(b.getMonto(), a.getMonto()));
        return gastos;
    }

    public ArrayList<Movimiento> ultimos(int n) {
        ArrayList<Movimiento> movs = usuario.getMovimientos();
        ArrayList<Movimiento> result = new ArrayList<>();
        for (int i = movs.size() - 1; i >= Math.max(0, movs.size() - n); i--)
            result.add(movs.get(i));
        return result;
    }
}
