package entidades;

/**
 * Representa una tarea dentro de un proyecto.
 * Las tareas son las unidades de trabajo que pueden ser asignadas a empleados.
 * 
 * Características:
 * - Tiene un título y descripción
 * - Registra duración estimada y retrasos
 * - Puede tener un empleado asignado
 * - Puede estar terminada o en progreso
 * - Calcula su costo basado en el empleado asignado
 * 
 * Invariantes:
 * - título no puede ser null ni vacío
 * - descripción no puede ser null
 * - diasNecesarios debe ser > 0
 * - diasRetraso debe ser >= 0
 */
public class Tarea {
    private String titulo;
    private String descripcion;
    private double diasNecesarios;
    private double diasRetraso;
    private IEmpleado empleadoAsignado;
    private boolean terminada;

    public Tarea(String titulo, String descripcion, double diasNecesarios) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        if (descripcion == null) {
            throw new IllegalArgumentException("La descripción no puede ser nula");
        }
        if (diasNecesarios <= 0) {
            throw new IllegalArgumentException("Los días necesarios deben ser mayores que 0");
        }
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.diasNecesarios = diasNecesarios;
        this.diasRetraso = 0;
        this.terminada = false;
    }

    /**
     * @return El título identificativo de la tarea
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @return La descripción detallada del trabajo a realizar
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @return La cantidad de días estimados necesarios para completar la tarea
     */
    public double getDuracionEstimada() {
        return diasNecesarios;
    }

    /**
     * @return La cantidad de días de retraso acumulados en la tarea
     */
    public double getDiasRetraso() {
        return diasRetraso;
    }

    public IEmpleado getEmpleadoAsignado() {
        return empleadoAsignado;
    }

    public void setEmpleadoAsignado(IEmpleado empleado) {
        // Si ya hay un empleado asignado, marcarlo como disponible
        if (this.empleadoAsignado != null) {
            this.empleadoAsignado.marcarComoDisponible();
        }
        this.empleadoAsignado = empleado;
        if (empleado != null) {
            empleado.marcarComoAsignado();
        }
    }

    public void agregarRetraso(double dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de retraso deben ser positivos");
        }
        this.diasRetraso += dias;
        if (empleadoAsignado != null) {
            empleadoAsignado.incrementarRetrasos();
        }
    }

    public boolean isTerminada() {
        return terminada;
    }

    public void setTerminada(boolean terminada) {
        this.terminada = terminada;
        if (terminada && empleadoAsignado != null) {
            empleadoAsignado.marcarComoDisponible();
        }
    }

    /**
     * Verifica si la tarea tiene un empleado asignado actualmente.
     * 
     * @return true si hay un empleado asignado, false si no
     */
    public boolean tieneEmpleadoAsignado() {
        return empleadoAsignado != null;
    }

    /**
     * Calcula el costo total de la tarea.
     * El cálculo incluye:
     * - Los días necesarios originales
     * - Los días de retraso acumulados
     * - La tarifa del empleado asignado (por hora o por día según el tipo)
     * 
     * @return El costo total de la tarea, o 0 si no hay empleado asignado
     */
    public double getCostoTarea() {
        if (empleadoAsignado == null) {
            return 0;
        }
        return empleadoAsignado.calcularCosto(diasNecesarios + diasRetraso);
    }

    /**
     * Representación en string de la tarea.
     * Se utiliza el título como identificador principal.
     * 
     * @return El título de la tarea
     */
    @Override
    public String toString() {
        return titulo;
    }
}
