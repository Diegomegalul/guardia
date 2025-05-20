package logica;
import java.util.ArrayList;
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
}
