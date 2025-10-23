package entidades;

import java.util.*;

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

    public void registrarEmpleadoEnTarea(IEmpleado empleado, Tarea tarea) {
        if (empleado == null || tarea == null) {
            throw new IllegalArgumentException("Empleado y tarea no pueden ser nulos");
        }

        empleadosAsignados.add(empleado);

        tareasRealizadas.computeIfAbsent(empleado, k -> new ArrayList<>()).add(tarea);
    }

    public boolean participoEmpleado(IEmpleado empleado) {
        return empleadosAsignados.contains(empleado);
    }

    public int cantidadEmpleados() {
        return empleadosAsignados.size();
    }

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
