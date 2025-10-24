package entidades;

import java.util.*;

/**
 * Clase que mantiene el registro histórico de un proyecto específico.
 * Almacena información sobre los empleados que han participado y sus tareas.
 * 
 * Responsabilidades:
 * - Registrar asignaciones de empleados a tareas
 * - Mantener lista de empleados que han participado
 * - Mantener registro de tareas realizadas por cada empleado
 * - Proveer métodos de consulta del historial
 * 
 * Estructuras de datos:
 * - Set<IEmpleado>: Para registro rápido de empleados únicos
 * - Map<IEmpleado, List<Tarea>>: Para registro de tareas por empleado
 */
public class HistorialProyecto {
    private int numeroProyecto;
    private Set<IEmpleado> empleadosAsignados;
    private Map<IEmpleado, List<Tarea>> tareasRealizadas;

    public HistorialProyecto(int numeroProyecto) {
        if (numeroProyecto <= 0) {
            throw new IllegalArgumentException("El número de proyecto debe ser mayor que 0");
        }
        this.numeroProyecto = numeroProyecto;
        this.empleadosAsignados = new HashSet<>();
        this.tareasRealizadas = new HashMap<>();
    }

    /**
     * Registra la asignación de un empleado a una tarea en el historial.
     * 
     * Proceso:
     * 1. Valida que ni empleado ni tarea sean nulos
     * 2. Agrega el empleado al conjunto de empleados participantes
     * 3. Agrega la tarea a la lista de tareas del empleado
     * 
     * @param empleado El empleado asignado
     * @param tarea La tarea asignada
     * @throws IllegalArgumentException si empleado o tarea son nulos
     */
    public void registrarEmpleadoEnTarea(IEmpleado empleado, Tarea tarea) {
        if (empleado == null || tarea == null) {
            throw new IllegalArgumentException("Empleado y tarea no pueden ser nulos");
        }

        empleadosAsignados.add(empleado);

        tareasRealizadas.computeIfAbsent(empleado, k -> new ArrayList<>()).add(tarea);
    }

    /**
     * Verifica si un empleado ha participado en el proyecto.
     * 
     * @param empleado El empleado a verificar
     * @return true si el empleado ha sido asignado a alguna tarea
     */
    public boolean participoEmpleado(IEmpleado empleado) {
        return empleadosAsignados.contains(empleado);
    }

    /**
     * Obtiene la cantidad total de empleados que han participado.
     * 
     * @return El número de empleados distintos que han sido asignados
     */
    public int cantidadEmpleados() {
        return empleadosAsignados.size();
    }

    /**
     * Obtiene una copia del conjunto de empleados asignados.
     * 
     * @return Un nuevo Set con los empleados que han participado
     */
    public Set<IEmpleado> getEmpleadosAsignados() {
        return new HashSet<>(empleadosAsignados);
    }

    public List<Tarea> getTareasRealizadas(IEmpleado empleado) {
        return tareasRealizadas.containsKey(empleado) ? new ArrayList<>(tareasRealizadas.get(empleado))
                : new ArrayList<>();
    }

    public Map<IEmpleado, List<Tarea>> getTareasRealizadas() {
        Map<IEmpleado, List<Tarea>> copia = new HashMap<>();
        for (Map.Entry<IEmpleado, List<Tarea>> entry : tareasRealizadas.entrySet()) {
            copia.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copia;
    }
}
