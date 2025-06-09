package logica;
import java.time.LocalDate;

public class Guardia {
	//Atributos
	private final int id;
	private LocalDate fecha;
	private final Persona persona;
	private Horario horario;
	private final boolean esRecuperacion;
	private final String motivo;

	//Constructor
	public Guardia(int id, LocalDate fecha, Persona persona, Horario horario, boolean esRecuperacion, String motivo) {
		if (persona == null || fecha == null || horario == null || motivo == null) {
			throw new IllegalArgumentException("Ningún parámetro puede ser nulo");
		}
		this.id = id;
		this.fecha = fecha;
		this.persona = persona;
		this.horario = horario;
		this.esRecuperacion = esRecuperacion;
		this.motivo = motivo;
	}

	// Getters y Setters
	public int getId() { 
		return id; 
	}
	public LocalDate getFecha() { 
		return fecha; 
	}
	public Persona getPersona() { 
		return persona; 
	}
	public Horario getHorario() { 
		return horario; 
	}
	public boolean isEsRecuperacion() { 
		return esRecuperacion; 
	}
	public String getMotivo() { 
		return motivo; 
	}
	public void setFecha(LocalDate fecha) {
		if (fecha == null) {
			throw new IllegalArgumentException("La fecha no puede ser nula");
		}
		this.fecha = fecha;
	}

	public void setHorario(Horario horario) {
		if (horario == null) {
			throw new IllegalArgumentException("El horario no puede ser nulo");
		}
		this.horario = horario;
	}

	//Metodos
}