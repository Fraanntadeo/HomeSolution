package entidades;

import java.time.LocalDate;
import java.util.*;

public class HomeSolution implements IHomeSolution {
    private Map<Integer, IEmpleado> empleados;
    private Map<Integer, Proyecto> proyectos;

    public HomeSolution() {
        this.empleados = new HashMap<>();
        this.proyectos = new HashMap<>();
    }

    @Override
    public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
        IEmpleado empleado = new EmpleadoContratado(nombre, valor);
        empleados.put(empleado.getLegajo(), empleado);
    }

    @Override
    public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {
        IEmpleado empleado;
        if ("EXPERTO".equalsIgnoreCase(categoria)) {
            empleado = new EmpleadoPlanta(nombre, valor, "EXPERTO");
        } else if ("TÉCNICO".equalsIgnoreCase(categoria)) {
            empleado = new EmpleadoPlanta(nombre, valor, "TÉCNICO");
        } else {
            empleado = new EmpleadoPlanta(nombre, valor, "INICIAL");
        }
        empleados.put(empleado.getLegajo(), empleado);
    }

    @Override
    public void registrarProyecto(String[] titulos, String[] descripciones, double[] duraciones,
            String domicilio, String[] cliente, String fechaInicio, String fechaFin) {
        if (titulos == null || descripciones == null || duraciones == null ||
                domicilio == null || cliente == null || fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo");
        }

        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);

        Proyecto proyecto = new Proyecto(domicilio, cliente[0], inicio, fin);

        for (int i = 0; i < titulos.length; i++) {
            if (titulos[i] != null && !titulos[i].trim().isEmpty()) {
                Tarea tarea = new Tarea(titulos[i],
                        i < descripciones.length ? descripciones[i] : "",
                        i < duraciones.length ? duraciones[i] : 0);
                proyecto.agregarTarea(tarea);
            }
        }

        proyecto.actualizarCostoTotal();
        proyecto.actualizarEstado();
        proyectos.put(proyecto.getNumero(), proyecto);
    }

    @Override
    public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }
        if (proyecto.getEstado().equals(Estado.finalizado)) {
            throw new Exception("No se pueden asignar tareas en un proyecto finalizado");
        }

        // Buscar la tarea
        Tarea tareaEncontrada = null;
        for (Tarea tarea : proyecto.getTareas()) {
            if (tarea.getTitulo().equals(titulo)) {
                tareaEncontrada = tarea;
                break;
            }
        }

        if (tareaEncontrada == null) {
            throw new IllegalArgumentException("Tarea no encontrada");
        }

        if (tareaEncontrada.tieneEmpleadoAsignado()) {
            throw new Exception("La tarea ya tiene un empleado asignado");
        }

        // Buscar primer empleado disponible
        IEmpleado empleadoDisponible = empleados.values().stream()
                .filter(IEmpleado::estaDisponible)
                .findFirst()
                .orElse(null);

        if (empleadoDisponible == null) {
            proyecto.setEstado(Estado.pendiente);
            throw new Exception("No hay empleados disponibles");
        }

        tareaEncontrada.setEmpleadoAsignado(empleadoDisponible);
        proyecto.registrarEmpleadoEnTarea(tareaEncontrada, empleadoDisponible);
        proyecto.actualizarEstado();
    }

    @Override
    public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        if (proyecto.getEstado().equals(Estado.finalizado)) {
            throw new Exception("No se pueden reasignar empleados en un proyecto finalizado");
        }

        // Verificar si el empleado existe
        IEmpleado nuevoEmpleado = empleados.get(legajo);
        if (nuevoEmpleado == null) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }

        // Buscar la tarea
        Tarea tareaEncontrada = null;
        for (Tarea tarea : proyecto.getTareas()) {
            if (tarea.getTitulo().equals(titulo)) {
                tareaEncontrada = tarea;
                break;
            }
        }

        if (tareaEncontrada == null) {
            throw new IllegalArgumentException("Tarea no encontrada");
        }

        IEmpleado empleadoActual = tareaEncontrada.getEmpleadoAsignado();
        if (empleadoActual == null) {
            throw new Exception("La tarea no tiene un empleado asignado previamente");
        }

        // Liberar al empleado actual y asignar el nuevo
        empleadoActual.marcarComoDisponible();
        nuevoEmpleado.marcarComoAsignado();
        tareaEncontrada.setEmpleadoAsignado(nuevoEmpleado);
        proyecto.registrarEmpleadoEnTarea(tareaEncontrada, nuevoEmpleado);
    }

    @Override
    public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        if (proyecto.getEstado().equals(Estado.finalizado)) {
            throw new Exception("No se pueden reasignar empleados en un proyecto finalizado");
        }

        // Encontrar la tarea
        Tarea tareaEncontrada = null;
        for (Tarea tarea : proyecto.getTareas()) {
            if (tarea.getTitulo().equals(titulo)) {
                tareaEncontrada = tarea;
                break;
            }
        }

        if (tareaEncontrada == null) {
            throw new IllegalArgumentException("Tarea no encontrada");
        }

        IEmpleado empleadoActual = tareaEncontrada.getEmpleadoAsignado();
        if (empleadoActual == null) {
            throw new Exception("La tarea no tiene un empleado asignado previamente");
        }

        // Buscar el empleado con menos retrasos
        IEmpleado mejorEmpleado = null;
        int menorRetrasos = Integer.MAX_VALUE;

        for (IEmpleado empleado : empleados.values()) {
            if (empleado.estaDisponible() && empleado.getCantidadRetrasos() < menorRetrasos) {
                mejorEmpleado = empleado;
                menorRetrasos = empleado.getCantidadRetrasos();
            }
        }

        if (mejorEmpleado == null) {
            throw new Exception("No hay empleados disponibles");
        }

        // Liberar al empleado actual y asignar el nuevo
        empleadoActual.marcarComoDisponible();
        mejorEmpleado.marcarComoAsignado();
        tareaEncontrada.setEmpleadoAsignado(mejorEmpleado);
        proyecto.registrarEmpleadoEnTarea(tareaEncontrada, mejorEmpleado);
    }

    @Override
    public void registrarRetrasoEnTarea(Integer nroProyecto, String tituloTarea, double retraso)
            throws IllegalArgumentException {
        Proyecto proyecto = proyectos.get(nroProyecto);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        if (proyecto.getEstado().equals(Estado.finalizado)) {
            throw new IllegalArgumentException("No se pueden registrar retrasos en un proyecto finalizado");
        }

        // Encontrar la tarea
        for (Tarea tarea : proyecto.getTareas()) {
            if (tarea.getTitulo().equals(tituloTarea)) {
                IEmpleado empleado = tarea.getEmpleadoAsignado();
                if (empleado != null) {
                    empleado.incrementarRetrasos();
                }
                tarea.agregarRetraso(retraso);

                // Actualizar fecha real del proyecto
                int diasRetraso = (int) Math.ceil(retraso);
                proyecto.setFechaFin(proyecto.getFechaRealFin().plusDays(diasRetraso));
                proyecto.actualizarCostoTotal();
                return;
            }
        }
        throw new IllegalArgumentException("Tarea no encontrada");
    }

    @Override
    public void finalizarTarea(Integer numero, String titulo) throws Exception {
        if (numero == null) {
            throw new IllegalArgumentException("El número de proyecto no puede ser null");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede ser vacío");
        }

        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        if (proyecto.getEstado().equals(Estado.finalizado)) {
            throw new Exception("No se pueden modificar tareas en un proyecto finalizado");
        }

        for (Tarea tarea : proyecto.getTareas()) {
            if (tarea.getTitulo().equals(titulo)) {
                if (tarea.isTerminada()) {
                    throw new Exception("La tarea ya está finalizada");
                }

                // Liberar al empleado asignado si existe
                IEmpleado empleado = tarea.getEmpleadoAsignado();
                if (empleado != null) {
                    empleado.marcarComoDisponible();
                }

                tarea.setTerminada(true);
                return;
            }
        }
        throw new IllegalArgumentException("Tarea no encontrada");
    }

    public List<IEmpleado> getEmpleados() {
        return new ArrayList<IEmpleado>(empleados.values());
    }

    public IEmpleado getEmpleado(int legajo) {
        return empleados.get(legajo);
    }

    @Override
    public void finalizarProyecto(Integer numero, String fechaFin) throws IllegalArgumentException {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        LocalDate fecha = LocalDate.parse(fechaFin);
        if (fecha.isBefore(proyecto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio");
        }

        for (Tarea tarea : proyecto.getTareas()) {
            if (!tarea.isTerminada()) {
                throw new IllegalStateException("No se puede finalizar un proyecto con tareas pendientes");
            }
        }

        proyecto.setFechaFin(fecha);
        proyecto.actualizarCostoTotal();
        proyecto.setEstado(Estado.finalizado);
    }

    @Override
    public double costoProyecto() {
        double costoTotal = 0;
        for (Proyecto proyecto : proyectos.values()) {
            proyecto.actualizarCostoTotal();
            costoTotal += proyecto.getCostoCalculado();
        }
        return costoTotal;
    }

    @Override
    public List<Tupla<Integer, String>> proyectosFinalizados() {
        List<Tupla<Integer, String>> resultado = new ArrayList<>();
        for (Proyecto proyecto : proyectos.values()) {
            if (Estado.finalizado.equals(proyecto.getEstado())) {
                resultado.add(new Tupla<>(proyecto.getNumero(), proyecto.getDomicilio()));
            }
        }
        return resultado;
    }

    @Override
    public List<Tupla<Integer, String>> proyectosPendientes() {
        List<Tupla<Integer, String>> resultado = new ArrayList<>();
        for (Proyecto proyecto : proyectos.values()) {
            if (Estado.pendiente.equals(proyecto.getEstado())) {
                resultado.add(new Tupla<>(proyecto.getNumero(), proyecto.getDomicilio()));
            }
        }
        return resultado;
    }

    @Override
    public List<Tupla<Integer, String>> proyectosActivos() {
        List<Tupla<Integer, String>> resultado = new ArrayList<>();
        for (Proyecto proyecto : proyectos.values()) {
            if (Estado.activo.equals(proyecto.getEstado())) {
                resultado.add(new Tupla<>(proyecto.getNumero(), proyecto.getDomicilio()));
            }
        }
        return resultado;
    }

    @Override
    public Object[] tareasDeUnProyecto(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            return new Object[0];
        }
        return proyecto.getTareas().stream()
                .map(Tarea::getTitulo)
                .toArray(String[]::new);
    }

    @Override
    public boolean estaFinalizado(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        return proyecto != null && Estado.finalizado.equals(proyecto.getEstado());
    }

    @Override
    public String consultarDomicilioProyecto(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }
        return proyecto.getDomicilio();
    }

    @Override
    public String consultarProyecto(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }
        return proyecto.toString();
    }

    @Override
    public Object[] empleadosNoAsignados() {
        return empleados.values().stream()
                .filter(IEmpleado::estaDisponible)
                .map(e -> String.valueOf(e.getLegajo()))
                .toArray();
    }

    @Override
    public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        List<Tupla<Integer, String>> empleadosAsignados = new ArrayList<>();
        for (Tarea tarea : proyecto.getTareas()) {
            IEmpleado empleado = tarea.getEmpleadoAsignado();
            if (empleado != null) {
                empleadosAsignados.add(new Tupla<>(empleado.getLegajo(), empleado.getNombre()));
            }
        }
        return empleadosAsignados;
    }

    @Override
    public List<Tupla<Integer, String>> empleados() {
        List<Tupla<Integer, String>> resultado = new ArrayList<>();
        for (IEmpleado empleado : empleados.values()) {
            resultado.add(new Tupla<>(empleado.getLegajo(), empleado.getNombre()));
        }
        return resultado;
    }

    @Override
    public int consultarCantidadRetrasosEmpleado(Integer legajo) {
        IEmpleado empleado = empleados.get(legajo);
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }
        return empleado.getCantidadRetrasos();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HomeSolution - Estado del Sistema\n");
        sb.append("===================================\n\n");

        for (Proyecto proyecto : proyectos.values()) {
            sb.append("Proyecto #").append(proyecto.getNumero()).append("\n");
            sb.append("Domicilio: ").append(proyecto.getDomicilio()).append("\n");
            sb.append("Cliente: ").append(proyecto.getCliente()).append("\n");
            sb.append("Estado: ").append(proyecto.getEstado()).append("\n");
            sb.append("Tareas:\n");

            for (Tarea tarea : proyecto.getTareas()) {
                // Como se especifica, toString de Tarea solo debe devolver el título
                sb.append("- ").append(tarea.toString()).append("\n");
            }

            sb.append("Costo final: $").append(String.format("%.2f", proyecto.getCostoCalculado())).append("\n");
            sb.append("Estado: ").append(proyecto.getEstado()).append("\n");
            sb.append("Tuvo retrasos: ").append(proyecto.getTareas().stream()
                    .anyMatch(t -> t.getDiasRetraso() > 0) ? "Sí" : "No").append("\n");
            sb.append("-----------------------------------\n\n");
        }

        return sb.toString();
    }

    @Override
    public Object[] tareasProyectoNoAsignadas(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            return new Object[0];
        }
        return proyecto.getTareas().stream()
                .filter(tarea -> !tarea.tieneEmpleadoAsignado())
                .map(Tarea::toString)
                .toArray();
    }

    @Override
    public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        if (proyecto.getEstado().equals(Estado.finalizado)) {
            throw new Exception("No se pueden asignar tareas en un proyecto finalizado");
        }

        // Buscar la tarea
        Tarea tareaEncontrada = null;
        for (Tarea tarea : proyecto.getTareas()) {
            if (tarea.getTitulo().equals(titulo)) {
                tareaEncontrada = tarea;
                break;
            }
        }

        if (tareaEncontrada == null) {
            throw new IllegalArgumentException("Tarea no encontrada");
        }

        if (tareaEncontrada.tieneEmpleadoAsignado()) {
            throw new Exception("La tarea ya tiene un empleado asignado");
        }

        // Buscar el empleado con menos retrasos
        IEmpleado mejorEmpleado = null;
        int menorRetrasos = Integer.MAX_VALUE;

        for (IEmpleado empleado : empleados.values()) {
            if (empleado.estaDisponible() && empleado.getCantidadRetrasos() < menorRetrasos) {
                mejorEmpleado = empleado;
                menorRetrasos = empleado.getCantidadRetrasos();
            }
        }

        if (mejorEmpleado == null) {
            proyecto.setEstado(Estado.pendiente);
            throw new Exception("No hay empleados disponibles");
        }

        tareaEncontrada.setEmpleadoAsignado(mejorEmpleado);
        mejorEmpleado.marcarComoAsignado();
        proyecto.registrarEmpleadoEnTarea(tareaEncontrada, mejorEmpleado);
        proyecto.actualizarEstado();
    }

    @Override
    public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias)
            throws IllegalArgumentException {
        Proyecto proyecto = proyectos.get(numero);
        if (proyecto == null) {
            throw new IllegalArgumentException("Proyecto no encontrado");
        }

        if (proyecto.getEstado().equals(Estado.finalizado)) {
            throw new IllegalArgumentException("No se pueden agregar tareas a un proyecto finalizado");
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }

        if (dias <= 0) {
            throw new IllegalArgumentException("Los días deben ser un número positivo");
        }

        Tarea nuevaTarea = new Tarea(titulo, descripcion, dias);
        proyecto.agregarTarea(nuevaTarea);
        proyecto.actualizarEstado();
    }

    @Override
    public boolean tieneRestrasos(String legajo) {
        try {
            int leg = Integer.parseInt(legajo);
            IEmpleado empleado = empleados.get(leg);
            if (empleado == null) {
                return false;
            }
            return empleado.getCantidadRetrasos() > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
