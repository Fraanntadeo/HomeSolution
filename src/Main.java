import entidades.HomeSolution;
import gui.PanelManager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        HomeSolution homeSolution = new HomeSolution();
        String[] titulos = { "Pintar", "Instalación eléctrica", "Trabajos jardinería", "Instalar AA" };
        String[] descripciones = {
                "Pintar paredes interiores",
                "Instalación de tomas e iluminación",
                "Poda y mantenimiento de jardín",
                "Instalación de aire acondicionado"
        };
        double[] duracion = { 4, 2, 1, 0.5 };
        String[] cliente = { "Pedro Gomez", "Pedro Gomez", "Pedro Gomez", "Pedro Gomez" };
        homeSolution.registrarProyecto(titulos, descripciones, duracion, "San Martin 1000", cliente, "2025-11-01",
                "2025-11-05");
        homeSolution.registrarEmpleado("Juan", 15000);
        homeSolution.registrarEmpleado("Luis", 80000, "EXPERTO");
        homeSolution.registrarEmpleado("Julieta", 15000);
        PanelManager panelManager = new PanelManager(homeSolution);
        panelManager.mostrar(1);
    }
}
