package logica;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import utiles.Sexo;

public class Trabajador extends Persona {
    private LocalDate fechaDeIncorporacion;
    private boolean voluntario;
    //Constructor
    public Trabajador(String ci,String nombre,String apellidos,Sexo sexo,boolean activo,LocalDate fechaDeIncorporacion,int guardiasAsignadas,int cantidadGuardiasFestivo,boolean voluntario){
        super(ci, nombre,apellidos, sexo, activo, guardiasAsignadas,cantidadGuardiasFestivo); 
        setFechaDeIncorporacion(fechaDeIncorporacion);
        setVoluntario(voluntario);
    }
    //Getters y setters
    
    public void setFechaDeIncorporacion(LocalDate fechaDeIncorporacion) {
		this.fechaDeIncorporacion = fechaDeIncorporacion;		
	}
    
    public LocalDate getFechaDeIncorporacion(){
    	return fechaDeIncorporacion;
    }

	public boolean getVoluntario() {
		return voluntario;
	}

	public void setVoluntario(boolean voluntario) {
		this.voluntario = voluntario;
	}

	//Metodos
	@Override
	public boolean puedeHacerGuardia(Horario horario) {
        boolean puede = false;
        if (getActivo()) {
            LocalDate fecha = horario.getDia();
            LocalTime inicio = horario.getHoraInicio();
            LocalTime fin = horario.getHoraFin();
            if (!fecha.isBefore(fechaDeIncorporacion)) {
                DayOfWeek diaSemana = fecha.getDayOfWeek();
                if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
                    boolean turnoValido = false;
                    if (inicio.equals(LocalTime.of(9, 0)) && fin.equals(LocalTime.of(14, 0))) {
                        turnoValido = true;
                    } else if (inicio.equals(LocalTime.of(14, 0)) && fin.equals(LocalTime.of(19, 0))) {
                        turnoValido = true;
                    }
                    if (turnoValido) {
                        Month mes = fecha.getMonth();
                        if (mes == Month.JULY || mes == Month.AUGUST) {
                            puede = voluntario;
                        } else {
                            puede = true;
                        }
                    }
                }
            }
        }
        return puede;
    } 
}


