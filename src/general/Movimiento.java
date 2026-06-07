package general;

public class Movimiento{
    private String descripcion;
    private double monto;
    private String tipo;
    private boolean esencial;

    public Movimiento(){
        this.descripcion = "";
        this.monto = 0.0;
        this.tipo = "";
        this.esencial = false;
    }

    public Movimiento(String descripcion, double monto, String tipo, boolean esencial){
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipo = tipo;
        this.esencial = esencial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isEsencial() {
        return esencial;
    }

    public void setEsencial(boolean esencial) {
        this.esencial = esencial;
    }
}
