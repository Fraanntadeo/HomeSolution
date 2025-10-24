package entidades;

/**
 * Interfaz que define el comportamiento común de todos los empleados.
 * 
 * Métodos:
 * - getNombre(): Obtiene el nombre del empleado
 * - getLegajo(): Obtiene el número de legajo único
 * - estaDisponible(): Verifica si puede ser asignado a una tarea
 * - marcarComoAsignado(): Indica que está trabajando en una tarea
 * - marcarComoDisponible(): Indica que puede recibir nuevas tareas
 * - incrementarRetrasos(): Registra un nuevo retraso del empleado
 * - calcularCosto(): Calcula el costo del trabajo según los días
 * - tuvoRetrasos(): Indica si tiene retrasos registrados
 * - getCantidadRetrasos(): Obtiene el número total de retrasos
 */
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
