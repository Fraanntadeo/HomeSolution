package entidades;

/**
 * Representa un empleado de planta permanente en el sistema.
 * Los empleados de planta cobran por día y tienen una categoría asignada.
 * 
 * Características:
 * - Cobra por día trabajado
 * - Tiene una categoría (INICIAL, TÉCNICO, EXPERTO)
 * - Recibe un bonus del 2% si no tiene retrasos
 * - Puede estar disponible o asignado a una tarea
 * 
 * Invariantes:
 * - nombre no puede ser null ni vacío
 * - legajo debe ser > 0
 * - valorDia debe ser > 0
 * - categoria debe ser una de las tres permitidas
 * - cantidadRetrasos debe ser >= 0
 */
public class EmpleadoPlanta implements IEmpleado {
    private static int siguienteLegajo = 1;
    private String nombre;
    private int legajo;
    private double valorDia;
    private String categoria;
    private boolean disponible;
    private int cantidadRetrasos;
    private static final double BONUS_SIN_RETRASOS = 0.02; // 2% de bonus

    // IREP:
    // - nombre no puede ser null ni vacío
    // - legajo debe ser > 0
    // - valorDia debe ser > 0
    // - categoria debe ser "INICIAL", "TÉCNICO" o "EXPERTO"
    // - cantidadRetrasos debe ser >= 0

    /**
     * Constructor para crear un nuevo empleado de planta.
     * 
     * Proceso de inicialización:
     * 1. Valida el nombre y valor por día
     * 2. Verifica que la categoría sea válida (INICIAL, TÉCNICO o EXPERTO)
     * 3. Asigna un número de legajo único
     * 4. Inicializa como disponible sin retrasos
     *
     * Categorías permitidas:
     * - INICIAL: Para empleados nuevos
     * - TÉCNICO: Para empleados con experiencia media
     * - EXPERTO: Para empleados con alta experiencia
     *
     * @param nombre El nombre del empleado
     * @param valorDia El valor que cobra por día trabajado
     * @param categoria La categoría del empleado (INICIAL/TÉCNICO/EXPERTO)
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    public EmpleadoPlanta(String nombre, double valorDia, String categoria) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        if (valorDia <= 0) {
            throw new IllegalArgumentException("El valor por día debe ser mayor que 0");
        }
        if (categoria == null
                || (!categoria.equals("INICIAL") && !categoria.equals("TÉCNICO") && !categoria.equals("EXPERTO"))) {
            throw new IllegalArgumentException("La categoría debe ser INICIAL, TÉCNICO o EXPERTO");
        }
        this.nombre = nombre;
        this.legajo = siguienteLegajo++;
        this.valorDia = valorDia;
        this.categoria = categoria;
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
        // Medio día cuenta como día completo
        double diasReales = Math.ceil(dias);
        double costo = valorDia * diasReales;

        // Si no tiene retrasos, aplica bonus del 2%
        if (!tuvoRetrasos()) {
            costo *= (1 + BONUS_SIN_RETRASOS);
        }

        return costo;
    }

    @Override
    public boolean tuvoRetrasos() {
        return cantidadRetrasos > 0;
    }

    @Override
    public int getCantidadRetrasos() {
        return cantidadRetrasos;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "EmpleadoPlanta{" +
                "nombre='" + nombre + '\'' +
                ", legajo=" + legajo +
                ", valorDia=" + valorDia +
                ", categoria='" + categoria + '\'' +
                ", retrasos=" + cantidadRetrasos +
                '}';
    }
}
