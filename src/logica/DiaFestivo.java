package logica;

import java.time.LocalDate;
import java.util.Objects;

public class DiaFestivo {
    private LocalDate fecha;
    private String descripcion;

    public DiaFestivo(LocalDate fecha, String descripcion) {
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaString() {
        return fecha.toString();
    }

    @Override
    public String toString() {
        return getFechaString() + " - " + descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DiaFestivo other = (DiaFestivo) obj;
        return Objects.equals(fecha, other.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha);
    }
}
