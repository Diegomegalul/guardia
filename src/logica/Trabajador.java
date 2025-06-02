package logica;

import java.time.LocalDate;
import utiles.Sexo;

public class Trabajador extends Persona {
    private LocalDate fechaDeIncorporacion;
    private boolean voluntarioGuardia;
    
    public Trabajador(int id, String nombre, String apellidos, Sexo sexo,
                    boolean activo, LocalDate fechaDeIncorporacion, boolean voluntarioGuardia) {
        super(id, nombre, apellidos, sexo, activo);
        setFechaDeIncorporacion(fechaDeIncorporacion);
        setVoluntarioGuardia(voluntarioGuardia);
    }

    @Override
    public boolean puedeHacerGuardia(LocalDate fecha, Horario horario) {
        boolean puedeHacerla = false;
        
        if (isActivo()) {
            if (voluntarioGuardia) {
                puedeHacerla = true;
            } else {
                int diaSemana = fecha.getDayOfWeek().getValue();
                boolean esFinDeSemana = diaSemana >= 6;
                String tipoHorario = horario.getTipo();
                
                if (getSexo() == Sexo.MASCULINO) {
                    puedeHacerla = tipoHorario.equals("Nocturno");
                } else {
                    puedeHacerla = esFinDeSemana && tipoHorario.equals("Diurno");
                }
            }
        }
        
        return puedeHacerla;
    }

    public LocalDate getFechaDeIncorporacion() {
        return fechaDeIncorporacion;
    }

    public void setFechaDeIncorporacion(LocalDate fechaDeIncorporacion) {
        if (fechaDeIncorporacion == null) {
            throw new IllegalArgumentException("Fecha de incorporación no puede ser nula");
        }
        if (fechaDeIncorporacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de incorporación no puede ser futura");
        }
        this.fechaDeIncorporacion = fechaDeIncorporacion;
    }

    public boolean isVoluntarioGuardia() {
        return voluntarioGuardia;
    }

    public void setVoluntarioGuardia(boolean voluntarioGuardia) {
        this.voluntarioGuardia = voluntarioGuardia;
    }
}