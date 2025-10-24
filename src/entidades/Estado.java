package entidades;

/**
 * Clase que define las constantes de estado para los proyectos.
 * 
 * Estados disponibles:
 * - ACTIVO: El proyecto está en curso y tiene todas sus tareas asignadas
 * - PENDIENTE: El proyecto está creado pero tiene tareas sin asignar
 * - FINALIZADO: El proyecto ha sido completado
 */
public class Estado {
    public static final String activo = "ACTIVO";
    public static final String pendiente = "PENDIENTE";
    public static final String finalizado = "FINALIZADO";
}
