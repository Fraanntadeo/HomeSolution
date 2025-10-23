package entidades;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Fecha {
    private LocalDate fecha;

    public Fecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        this.fecha = fecha;
    }

    public static Fecha crearFecha(int anio, int mes, int dia) {
        return new Fecha(LocalDate.of(anio, mes, dia));
    }

    public static Fecha crearFechaDesdeFormato(int aaaammdd) {
        int anio = aaaammdd / 10000;
        int mes = (aaaammdd % 10000) / 100;
        int dia = aaaammdd % 100;
        return crearFecha(anio, mes, dia);
    }

    public Fecha agregarDias(int dias) {
        return new Fecha(this.fecha.plusDays(dias));
    }

    public boolean esAnterior(Fecha otra) {
        return this.fecha.isBefore(otra.fecha);
    }

    public boolean esPosterior(Fecha otra) {
        return this.fecha.isAfter(otra.fecha);
    }

    public boolean esIgual(Fecha otra) {
        return this.fecha.isEqual(otra.fecha);
    }

    public int diasEntre(Fecha otra) {
        return (int) ChronoUnit.DAYS.between(this.fecha, otra.fecha);
    }

    public int aFormato() {
        return fecha.getYear() * 10000 + fecha.getMonthValue() * 100 + fecha.getDayOfMonth();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return fecha.toString();
    }
}
