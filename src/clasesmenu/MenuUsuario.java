package clasesmenu;

import general.Movimiento;

import general.Usuario;
import general.Validaciones;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuUsuario{
    private Usuario usuario;
    private Scanner sc;

    public MenuUsuario(Usuario usuario, Scanner sc){
        this.usuario = usuario;
        this.sc = sc;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void ejecutar(){
        char continuar = 'S';
        do{
            mostrarResumen();
            System.out.println("1. Cambiar clasificacion de gastos de primer o segundo nivel");
            System.out.println("2. Estadisticas del mes");
            System.out.println("3. Consejos de ahorro");
            System.out.println("4. Estado de cuenta y movimientos del mes");
            System.out.println("5. Cerrar sesion");
            int opcion = Validaciones.leerOpcion(sc, 1, 6);
            switch (opcion){
                case 1:
                    cambiarClasificacion();
                    break;
                
                case 2:
                    mostrarEstadisticas();
                    break;
                    
                case 3:
                    mostrarConsejos();
                    break;
                
                case 4:                
                    mostrarMovimientos();
                    break;
                
                case 5:                
                    System.out.println("Sesion cerrada.");
                    continuar = 'N';
                    break;
                
            }
        }
        while (continuar == 'S');
    }

    private void mostrarResumen(){
        ArrayList<Movimiento> movimientos = usuario.getMovimientos();
        double ingresoTotal = 0.0;
        double gastoTotal = 0.0;
        
        for (int i = 0; i < movimientos.size(); i++){
            Movimiento mov = movimientos.get(i);
            String tipo = mov.getTipo();
            double monto = mov.getMonto();
            
            if (tipo.equalsIgnoreCase("ingreso")){
                ingresoTotal = ingresoTotal + monto;
            }
            else{
                gastoTotal = gastoTotal + monto;
            }
            
        }
        double saldo = ingresoTotal - gastoTotal;
        System.out.println("Hola, " + usuario.getNombre());
        System.out.println("Ingreso total del mes:  $" + ingresoTotal);
        System.out.println("Gasto mensual: $" + gastoTotal);
        System.out.println("Saldo actual: $" + saldo);
    }

    private void cambiarClasificacion(){
        ArrayList<Movimiento> movimientos = usuario.getMovimientos();
        boolean hayGastos = false;
        System.out.println("Clasificacion de Gastos");
        
        for (int i = 0; i < movimientos.size(); i++){
            Movimiento mov = movimientos.get(i);
            String tipo = mov.getTipo();
            
            if (tipo.equalsIgnoreCase("gasto")){
                hayGastos = true;
                String clasificacion = "";
                
                if (mov.isEsencial()){
                    clasificacion = "Gastos de primer nivel";
                }
                else{
                    clasificacion = "Gastas de segundo nivel";
                }
                
                String descripcion = mov.getDescripcion();
                double monto = mov.getMonto();
                System.out.println((i + 1) + ". " + descripcion + " - $" + monto + " (" + clasificacion + ")");
            }
        }
        if (!hayGastos){
            System.out.println("No tiene gastos registrados.");
            return;
        }
        
        System.out.println("Ingrese el numero del gasto que desea cambiar o 0 para cancelar: ");
        int numero = Validaciones.leerOpcion(sc, 0, movimientos.size());
        if (numero == 0){
            return;
        }
        Movimiento movSeleccionado = movimientos.get(numero - 1);
        String tipoMov = movSeleccionado.getTipo();
        
        if (!tipoMov.equalsIgnoreCase("gasto")){
            System.out.println("El mobimiento seleccionado no es un gasto.");
            return;
        }
        
        boolean esEsencialActual = movSeleccionado.isEsencial();
        
        if (esEsencialActual){
            movSeleccionado.setEsencial(false);
            System.out.println("Cambiado a gastos de sugundo nivel");
        }
        else{
            movSeleccionado.setEsencial(true);
            System.out.println("Cambiado a gastos de primer nivel");
        }
    }

    private void mostrarEstadisticas(){
        ArrayList<Movimiento> movimientos = usuario.getMovimientos();
        double gastoMaximo = 0.0;
        double gastoMinimo = Double.MAX_VALUE;
        double gastoTotal = 0.0;
        boolean primerGasto = true;
        System.out.println("Estadisticas del mes");
        
        for (int i = 0; i < movimientos.size(); i++){
            Movimiento mov = movimientos.get(i);
            String tipo = mov.getTipo();
            double monto = mov.getMonto();
            
            if (tipo.equalsIgnoreCase("gasto")){
                gastoTotal = gastoTotal + monto;
                
                if (primerGasto){
                    gastoMaximo = monto;
                    gastoMinimo = monto;
                    primerGasto = false;
                }
                else{
                    if (monto > gastoMaximo){
                        gastoMaximo = monto;
                    }
                    if (monto < gastoMinimo){
                        gastoMinimo = monto;
                    }
                }
            }
        }
        if (primerGasto){
            System.out.println("No tiene gastos registrados este mes.");
        }
        else{
            System.out.println("Gasto maximo del mes: $" + gastoMaximo);
            System.out.println("Gasto minimo del mes: $" + gastoMinimo);
            System.out.println("Gasto total del mes: $" + gastoTotal);
        }
    }

    private void mostrarConsejos(){
        ArrayList<Movimiento> movimientos = usuario.getMovimientos();
        double ingresoTotal = 0.0;
        double gastoTotal = 0.0;
        double gastoNoEsencial = 0.0;
        
        for (int i = 0; i < movimientos.size(); i++){
            Movimiento mov = movimientos.get(i);
            String tipo = mov.getTipo();
            
            double monto = mov.getMonto();
            
            if (tipo.equalsIgnoreCase("ingreso")){
                ingresoTotal = ingresoTotal + monto;
            }
            else{
                gastoTotal = gastoTotal + monto;
                boolean esencial = mov.isEsencial();
                
            if (!esencial){
                gastoNoEsencial = gastoNoEsencial + monto;
                
                }
            }
        }
        double saldo = ingresoTotal - gastoTotal;
        double porcentajeGasto = 0.0;
        
        if (ingresoTotal > 0){
            porcentajeGasto = (gastoTotal / ingresoTotal) * 100;
        }
        
        System.out.println("Consejos de ahorro");
        if (porcentajeGasto > 80){
            System.out.println("1. Alerta: Estas gastando el " + (int) porcentajeGasto + "% de tus ingresos.");
            
        }
        if (gastoNoEsencial > 0){
            System.out.println("2. Tus gastos no esenciales son $" + gastoNoEsencial);
        }
        if (saldo > 0){
            double sugerenciaAhorro = saldo * 0.20;
            System.out.println("3. Con tu saldo actual de $" + saldo + ", podrías ahorrar al menos $" + sugerenciaAhorro);
        }
    }

    private void mostrarMovimientos(){
        ArrayList<Movimiento> movimientos = usuario.getMovimientos();
        System.out.println("Estado de cuenta y movimientos del mes");
        
        if (movimientos.isEmpty()){
            System.out.println("No hay movimientos registrados");
            return;
        }
        System.out.println("Tipo");
        System.out.println("Descripcion");
        System.out.println("Monto");
        System.out.println("Clasificacion");
        
        for (int i = 0; i < movimientos.size(); i++){
            Movimiento mov = movimientos.get(i);
            String tipo = mov.getTipo();
            String descripcion = mov.getDescripcion();
            double monto = mov.getMonto();
            String clasificacion = "";
            
            if (tipo.equalsIgnoreCase("gasto")){
                
                if (mov.isEsencial()){
                    clasificacion = "Gastos de primer nivel";
                }
                else{
                    clasificacion = "Gastos de segundo nivel";
                }
            }
            else{
                clasificacion = "-";
            }
            String numStr = String.valueOf(i + 1);
            while (numStr.length() < 3){
                numStr = " " + numStr;
            }
            
            String tipoStr = tipo;
            while (tipoStr.length() < 14){
                tipoStr = tipoStr + " ";
            }
            
            String descStr = descripcion;
            if (descStr.length() > 16){
                descStr = descStr.substring(0, 13) + "...";
            }
            
            while (descStr.length() < 16){
                descStr = descStr + " ";
            }
            
            String montoStr = "$" + monto;
            
            while (montoStr.length() < 10){
                montoStr = montoStr + " ";
            }
            
            while (clasificacion.length() < 13){
                clasificacion = clasificacion + " ";
            }
            
            System.out.println(numStr);
            System.out.println(tipoStr);
            System.out.println(descStr);
            System.out.println(montoStr);
            System.out.println(clasificacion);
        }

        System.out.println("Desea agregar un nuevo movimiento? (S/N): ");
        char agregar = Validaciones.leerSN(sc, "");
        if (agregar == 'S'){
            System.out.println("1. Ingreso");
            System.out.println("2. Gasto");
            int tipoOpcion = Validaciones.leerOpcion(sc, 1, 2);
            String tipoNuevo = "";
            
            if (tipoOpcion == 1){
                tipoNuevo = "ingreso";
            }
            else{
                tipoNuevo = "gasto";
            }
            
            String descripcionNueva = Validaciones.leerTextoNoVacio(sc, "Descripcion: ");
            double montoNuevo = Validaciones.leerMonto(sc, "Monto: $");
            boolean esEsencialNuevo = false;
            if (tipoNuevo.equalsIgnoreCase("gasto")){
                char resp = Validaciones.leerSN(sc, "Es un gasto esencial? (S/N): ");
                
                if (resp == 'S'){
                    esEsencialNuevo = true;
                }
            }
            Movimiento nuevoMov = new Movimiento(descripcionNueva, montoNuevo, tipoNuevo, esEsencialNuevo);
            usuario.agregarMovimiento(nuevoMov);
            System.out.println("Movimiento agregado.");
        }
    }
}
