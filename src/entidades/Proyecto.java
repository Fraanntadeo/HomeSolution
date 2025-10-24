package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un proyecto en el sistema HomeSolution.
 * Un proyecto contiene múltiples tareas y mantiene información sobre su estado,
 * fechas y costos.
 * 
 * Características:
 * - Identificado por un número único autogenerado
 * - Mantiene lista de tareas
 * - Controla fechas de inicio, fin estimado y fin real
 * - Gestiona estados (pendiente, en_progreso, finalizado)
 * - Calcula costos totales
 * - Mantiene un historial de asignaciones
 * 
 * Invariantes:
 * - domicilio no puede ser null ni vacío
 * - cliente no puede ser null ni vacío
 * - fechas no pueden ser null
 * - fechaFin no puede ser anterior a fechaInicio
 */
public class Proyecto {
    private static int nextNumero = 1;
    private int numero;
    private String domicilio;
    private String cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaEstimadaFin;
    private LocalDate fechaRealFin;
    private List<Tarea> tareas;
    private String estado;
    private double costoCalculado;
    private HistorialProyecto historial;

    public Proyecto(String domicilio, String cliente, LocalDate fechaInicio, LocalDate fechaFin) {
        if (domicilio == null || domicilio.trim().isEmpty()) {
            throw new IllegalArgumentException("El domicilio no puede ser nulo o vacío");
        }
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El cliente no puede ser nulo o vacío");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        this.numero = nextNumero++;
        this.domicilio = domicilio;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaEstimadaFin = fechaFin;
        this.fechaRealFin = fechaFin;
        this.tareas = new ArrayList<>();
        this.estado = Estado.pendiente;
        this.costoCalculado = 0;
        this.historial = new HistorialProyecto(numero);
    }

    public int getNumero() {
        return numero;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getCliente() {
        return cliente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaEstimadaFin() {
        return fechaEstimadaFin;
    }

    public LocalDate getFechaRealFin() {
        return fechaRealFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaRealFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Tarea> getTareas() {
        return new ArrayList<>(tareas);
    }

    public void agregarTarea(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
        tareas.add(tarea);

        // Actualizar fechas
        int diasAAgregar = (int) Math.ceil(tarea.getDuracionEstimada());
        this.fechaEstimadaFin = this.fechaEstimadaFin.plusDays(diasAAgregar);
        if (this.fechaRealFin != null) {
            this.fechaRealFin = this.fechaRealFin.plusDays(diasAAgregar);
        }

        actualizarCostoTotal();
    }

    public void registrarEmpleadoEnTarea(Tarea tarea, IEmpleado empleado) {
        if (tarea == null || empleado == null) {
            throw new IllegalArgumentException("La tarea y el empleado no pueden ser nulos");
        }
        historial.registrarEmpleadoEnTarea(empleado, tarea);
    }

    public void actualizarCostoTotal() {
        double costoBase = 0;
        for (Tarea tarea : tareas) {
            if (tarea.getEmpleadoAsignado() != null) {
                costoBase += tarea.getEmpleadoAsignado().calcularCosto(tarea.getDuracionEstimada());
            }
        }

        // Aplicar margen según retrasos
        boolean tieneRetrasos = false;
        for (Tarea tarea : tareas) {
            if (tarea.getDiasRetraso() > 0) {
                tieneRetrasos = true;
                break;
            }
        }

        double margen = tieneRetrasos ? 1.25 : 1.35;
        this.costoCalculado = costoBase * margen;
    }

    public double getCostoCalculado() {
        return costoCalculado;
    }

    public boolean tieneRetrasos() {
        return fechaRealFin != null && fechaRealFin.isAfter(fechaEstimadaFin);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proyecto #").append(numero).append("\n");
        sb.append("Cliente: ").append(cliente).append("\n");
        sb.append("Domicilio: ").append(domicilio).append("\n");
        sb.append("Fecha inicio: ").append(fechaInicio).append("\n");
        sb.append("Fecha estimada fin: ").append(fechaEstimadaFin).append("\n");
        if (fechaRealFin != null) {
            sb.append("Fecha real fin: ").append(fechaRealFin).append("\n");
        }
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Costo calculado: $").append(String.format("%.2f", costoCalculado)).append("\n");
        sb.append("Tareas:\n");
        for (Tarea tarea : tareas) {
            sb.append("- ").append(tarea.toString()).append("\n");
        }
        return sb.toString();
    }

    public boolean tieneDemora() {
        return fechaRealFin != null && fechaRealFin.isAfter(fechaEstimadaFin);
    }

    public void actualizarEstado() {
        if (this.estado.equals(Estado.finalizado)) {
            return;
        }

        boolean tieneTareasSinAsignar = false;
        for (Tarea tarea : tareas) {
            if (!tarea.tieneEmpleadoAsignado()) {
                tieneTareasSinAsignar = true;
                break;
            }
        }

        if (tieneTareasSinAsignar) {
            this.estado = Estado.pendiente;
        } else {
            this.estado = Estado.activo;
        }
    }
}
