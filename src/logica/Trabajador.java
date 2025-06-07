package logica;

import java.time.LocalDate;
import utiles.Sexo;

public class Trabajador extends Persona {
    private LocalDate fechaDeIncorporacion;


    private boolean voluntarioVacaciones;
    //Constructor
    public Trabajador(String ci,String nombre,Sexo sexo,boolean activo,LocalDate fechaDeIncorporacion,int cantidadGuardias,int grupo){
        super(ci, nombre, sexo, activo, cantidadGuardias, 0); // cantidadGuardiasFestivo no aplica
        setFechaDeIncorporacion(fechaDeIncorporacion);
        // No llamar a setGrupo(grupo) aquí, grupo solo aplica a Estudiante
    }
    //Getters y setters
    public void setFechaDeIncorporacion(LocalDate fechaDeIncorporacion){
        this.fechaDeIncorporacion = fechaDeIncorporacion;

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
            throw new IllegalArgumentException("Fecha de incorporaci�n no puede ser nula");
        }
        if (fechaDeIncorporacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de incorporaci�n no puede ser futura");
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