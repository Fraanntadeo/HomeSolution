package entidades;

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

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getDuracionEstimada() {
        return diasNecesarios;
    }

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

    public boolean tieneEmpleadoAsignado() {
        return empleadoAsignado != null;
    }

    public double getCostoTarea() {
        if (empleadoAsignado == null) {
            return 0;
        }
        return empleadoAsignado.calcularCosto(diasNecesarios + diasRetraso);
    }

    @Override
    public String toString() {
        return titulo;
    }
}
