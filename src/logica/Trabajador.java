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
        // 1. Verificar estado activo
        if (!getActivo()) {
            return false;
        }
        
        // Obtener fecha y hora del horario
        LocalDate fecha = horario.getDia();
        LocalTime inicio = horario.getHoraInicio();
        LocalTime fin = horario.getHoraFin();
        
        // 2. Verificar fecha de incorporación
        if (fecha.isBefore(fechaDeIncorporacion)) {
            return false;
        }
        
        // 3. Verificar que es fin de semana
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
            return false;
        }
        
        // 4. Verificar turnos válidos
        boolean turnoValido = false;
        
        // Turno mañana: 9:00 - 14:00
        if (inicio.equals(LocalTime.of(9, 0)) && fin.equals(LocalTime.of(14, 0))) {
            turnoValido = true;
        }
        // Turno tarde: 14:00 - 19:00
        else if (inicio.equals(LocalTime.of(14, 0)) && fin.equals(LocalTime.of(19, 0))) {
            turnoValido = true;
        }
        
        if (!turnoValido) {
            return false;
        }
        
        // 5. Verificar periodo vacacional (julio-agosto)
        Month mes = fecha.getMonth();
        if (mes == Month.JULY || mes == Month.AUGUST) {
            // En vacaciones solo voluntarios
            return voluntario;
        }
        
        // 6. Si no es vacaciones, puede hacer la guardia
        return true;
    } 
}


