package entidades;

public class EmpleadoContratado implements IEmpleado {
    private static int siguienteLegajo = 1;
    private String nombre;
    private int legajo;
    private double valorHora;
    private boolean disponible;
    private int cantidadRetrasos;

    public EmpleadoContratado(String nombre, double valorHora) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        if (valorHora <= 0) {
            throw new IllegalArgumentException("El valor por hora debe ser mayor que 0");
        }
        this.nombre = nombre;
        this.legajo = siguienteLegajo++;
        this.valorHora = valorHora;
        this.disponible = true;
        this.cantidadRetrasos = 0;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getLegajo() {
        return legajo;
    }

    @Override
    public boolean estaDisponible() {
        return disponible;
    }

    @Override
    public void marcarComoAsignado() {
        disponible = false;
    }

    @Override
    public void marcarComoDisponible() {
        disponible = true;
    }

    @Override
    public void incrementarRetrasos() {
        cantidadRetrasos++;
    }

    @Override
    public double calcularCosto(double dias) {
        // Cada día tiene 8 horas, medio día es 4 horas
        double horas = dias >= 1 ? dias * 8 : 4;
        return valorHora * horas;
    }

    @Override
    public boolean tuvoRetrasos() {
        return cantidadRetrasos > 0;
    }

    @Override
    public int getCantidadRetrasos() {
        return cantidadRetrasos;
    }

    @Override
    public String toString() {
        return "EmpleadoContratado{" +
                "nombre='" + nombre + '\'' +
                ", legajo=" + legajo +
                ", valorHora=" + valorHora +
                ", retrasos=" + cantidadRetrasos +
                '}';
    }
}
