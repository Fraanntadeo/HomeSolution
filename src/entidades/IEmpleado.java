package entidades;

public interface IEmpleado {
    String getNombre();
    int getLegajo();
    boolean estaDisponible();
    void marcarComoAsignado();
    void marcarComoDisponible();
    void incrementarRetrasos();
    double calcularCosto(double dias);
    boolean tuvoRetrasos();
    int getCantidadRetrasos();
}
