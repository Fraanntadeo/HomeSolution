package entidades;

/**
 * Representa un empleado contratado en el sistema.
 * Los empleados contratados cobran por hora trabajada.
 * 
 * Características:
 * - Cobra por hora (8 horas por día completo, 4 horas por medio día)
 * - Mantiene registro de sus retrasos
 * - Puede estar disponible o asignado a una tarea
 * 
 * Invariantes:
 * - nombre no puede ser null ni vacío
 * - legajo debe ser > 0
 * - valorHora debe ser > 0
 * - cantidadRetrasos debe ser >= 0
 */
public class EmpleadoContratado implements IEmpleado {
    private static int siguienteLegajo = 1;
    private String nombre;
    private int legajo;
    private double valorHora;
    private boolean disponible;
    private int cantidadRetrasos;

    /**
     * Constructor para crear un nuevo empleado contratado.
     * 
     * Proceso de inicialización:
     * 1. Valida los parámetros de entrada
     * 2. Asigna un número de legajo único
     * 3. Inicializa el empleado como disponible
     * 4. Establece el contador de retrasos en 0
     *
     * @param nombre El nombre del empleado
     * @param valorHora El valor que cobra por hora trabajada
     * @throws IllegalArgumentException si el nombre es inválido o el valor es <= 0
     */
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

    /**
     * Calcula el costo del trabajo realizado por el empleado contratado.
     * - Un día completo se considera como 8 horas
     * - Medio día se considera como 4 horas
     * 
     * @param dias La cantidad de días trabajados (puede ser fracción)
     * @return El costo total calculado (valorHora * horas trabajadas)
     */
    @Override
    public double calcularCosto(double dias) {
        if (dias < 0) {
            throw new IllegalArgumentException("Los días no pueden ser negativos");
        }
        // Para medio día o menos se consideran 4 horas
        // Para más de medio día se calculan 8 horas por día
        double horas = dias <= 0.5 ? 4 : dias * 8;
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
