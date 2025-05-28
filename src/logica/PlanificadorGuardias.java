package logica;
import java.util.ArrayList;
import logica.Persona;
import utiles.Sexo;
public class PlanificadorGuardias {
	private ArrayList<Persona> personas;
	private ArrayList<Guardia> guardias;
	private ArrayList<Horario> horarios;
	
	public PlanificadorGuardias() {
		setPersonas(new ArrayList<Persona>());
		setGuardias(new ArrayList<Guardia>());
		setHorarios(new ArrayList<Horario>());
	}

	public ArrayList<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(ArrayList<Horario> horarios) {
		this.horarios = horarios;
	}

	public ArrayList<Guardia> getGuardias() {
		return guardias;
	}

	public void setGuardias(ArrayList<Guardia> guardias) {
		this.guardias = guardias;
	}

	public ArrayList<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(ArrayList<Persona> personas) {
		this.personas = personas;
	}

	public void crearPersona(String ci, String nombre, Sexo sexo, boolean activo, int cantidadGuardias, int cantidadGuardiasFestivo) {
	    Persona nuevaPersona = new Estudiante(ci, nombre, sexo, activo, cantidadGuardias, cantidadGuardiasFestivo);
	    personas.add(nuevaPersona);
	}

	// Sobrecarga para Trabajador
	public void crearPersona(String ci, String nombre, Sexo sexo, boolean activo, java.time.LocalDate fechaDeIncorporacion, int cantidadGuardias) {
	    Persona nuevaPersona = new Trabajador(ci, nombre, sexo, activo, fechaDeIncorporacion, cantidadGuardias);
	    personas.add(nuevaPersona);
	}
	
	// Nuevo método para crear guardia
	public void crearGuardia(Horario horario, Persona persona) {
	    Guardia nuevaGuardia = new Guardia(horario, persona);
	    guardias.add(nuevaGuardia);
	}
}
