package logica;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import utiles.Sexo;

public class Trabajador extends Persona {
	//Atributos
	private LocalDate fechaDeIncorporacion;
	private boolean voluntario;
	private LocalDate fechaGuardiaVoluntaria; // Nueva fecha para guardia voluntaria

	//Constructor
	public Trabajador(String ci,String nombre,String apellidos,Sexo sexo,boolean activo,LocalDate fechaDeIncorporacion,int guardiasAsignadas,int cantidadGuardiasFestivo,boolean voluntario,LocalDate fechaGuardiaVoluntaria) {
		super(ci, nombre,apellidos, sexo, activo, guardiasAsignadas,cantidadGuardiasFestivo); 
		setFechaDeIncorporacion(fechaDeIncorporacion);
		setVoluntario(voluntario);
		setFechaGuardiaVoluntaria(fechaGuardiaVoluntaria); 
	}

	//Getters y setters
	public void setFechaGuardiaVoluntaria(LocalDate fechaGuardiaVoluntaria) {
		this.fechaGuardiaVoluntaria = fechaGuardiaVoluntaria;
	}

	public LocalDate getFechaGuardiaVoluntaria() {
		return fechaGuardiaVoluntaria;
	}

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
		if (getActivo() && horario != null) {
			LocalDate fecha = horario.getDia();
			LocalTime inicio = horario.getHoraInicio();
			LocalTime fin = horario.getHoraFin();
			Month mes = fecha.getMonth();
			boolean turnoValido = (inicio.equals(LocalTime.of(9, 0)) && fin.equals(LocalTime.of(14, 0)))
				|| (inicio.equals(LocalTime.of(14, 0)) && fin.equals(LocalTime.of(19, 0)));
			// Permitir si no hay restricción de antigüedad
			boolean incorporacionOk = (fechaDeIncorporacion == null) || !fecha.isBefore(fechaDeIncorporacion);
			if (incorporacionOk) {
				if (mes == Month.JULY || mes == Month.AUGUST) {
					// En julio/agosto, cualquier día, solo si es voluntario y turno válido
					puede = voluntario && turnoValido;
				} else {
					// Resto del año: solo fines de semana y turno válido
					DayOfWeek diaSemana = fecha.getDayOfWeek();
					if ((diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) && turnoValido) {
						puede = true;
					}
				}
			}
		}
		return puede;
	} 
	// No hay métodos con dos returns en caminos alternativos ni uso de break fuera de switch.
}

