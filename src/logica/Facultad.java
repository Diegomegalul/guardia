package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logica.Persona;

public class Facultad {
	// Atributos
	private final List<Persona> personas = new ArrayList<>();
	private String nombre;

	// Constructor
	public Facultad(String nombre) {
		setNombre(nombre);
	}

	// Getters y setters
	public List<Persona> getPersonas() {
		return Collections.unmodifiableList(personas);
	}

	// Metodos
	public void agregarPersona(Persona persona) {
		personas.add(persona);
	}

	public boolean eliminarPersona(Persona p) {
		// Eliminar todas las guardias asociadas a la persona (sin usar ->)
		PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();
		if (planificador != null && planificador.getGuardiaFactory() != null) {
			logica.GuardiaFactory factory = planificador.getGuardiaFactory();

			// Guardias planificadas
			java.util.Iterator<logica.Guardia> it1 = factory.getGuardias().iterator();
			while (it1.hasNext()) {
				logica.Guardia g = it1.next();
				if (g.getPersona() != null && g.getPersona().equals(p)) {
					it1.remove();
				}
			}
			// Guardias cumplidas
			java.util.Iterator<logica.Guardia> it2 = factory.getGuardiasCumplidas().iterator();
			while (it2.hasNext()) {
				logica.Guardia g = it2.next();
				if (g.getPersona() != null && g.getPersona().equals(p)) {
					it2.remove();
				}
			}
			// Guardias incumplidas
			java.util.Iterator<logica.Guardia> it3 = factory.getGuardiasIncumplidas().iterator();
			while (it3.hasNext()) {
				logica.Guardia g = it3.next();
				if (g.getPersona() != null && g.getPersona().equals(p)) {
					it3.remove();
				}
			}
		}
		// Eliminar de la lista de personas
		return personas.remove(p);
	}

	public Persona buscarPersonaPorCI(String ci) {
		Persona encontrada = null;
		for (Persona p : personas) {
			if (p.getCi().equals(ci)) {
				encontrada = p;
				break;
			}
		}
		return encontrada;
	}

	public boolean actualizarPersona(String ci, Persona nuevaPersona) {
		boolean actualizado = false;
		for (int i = 0; i < personas.size(); i++) {
			if (personas.get(i).getCi().equals(ci)) {
				personas.set(i, nuevaPersona);
				actualizado = true;
				break;
			}
		}
		return actualizado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
