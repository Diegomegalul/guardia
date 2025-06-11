package logica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utiles.TipoGuardia;
import logica.Horario;
import logica.Calendario;

public class GuardiaFactory {
	//Atributos
	private Horario horario;
	private Calendario calendario;
	private List<Guardia> guardias;
	private int nextId = 1;

	//Constructor
	public GuardiaFactory() {
		this.guardias = new ArrayList<>(); 
	}

	//Setters y Getters
	public Calendario getCalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public List<Guardia> getGuardias() {
		return guardias;
	}

	public void setGuardias(List<Guardia> guardias) {
		this.guardias = new ArrayList<>(guardias);  // Copia defensiva
	}
	//Metodos
	//--- CRUD COMPLETO ---//

	// --- CRUD MEJORADO --- //

	// M�TODO UNIFICADO PARA CREAR GUARDIAS
	public boolean crearGuardia(TipoGuardia tipo, Persona persona, Horario horario) {
		// Validaci�n b�sica
		if (!persona.puedeHacerGuardia(horario)) {
			return false;
		}

		// Validaciones específicas por tipo
		switch(tipo) {
		case RECUPERACION:
			if (!(persona instanceof Estudiante) || 
					!((Estudiante)persona).tieneGuardiasPendientes()) {
				return false;
			}
			break;

		case VOLUNTARIA:
			if (!(persona instanceof Trabajador) || 
					!((Trabajador)persona).getVoluntario()) {
				return false;
			}
			break;

		case FESTIVO:
			if (persona.getCantidadGuardiasFestivo() >= 3) {
				return false;
			}
			break;
		default:
			break;
		}

		// Crear guardia
		Guardia nuevaGuardia = new Guardia(nextId++, tipo, persona, horario);
		guardias.add(nuevaGuardia);

		// Actualizar contadores
		persona.setGuardiasAsignadas(persona.getGuardiasAsignadas() + 1);

		if (tipo == TipoGuardia.FESTIVO) {
			persona.setCantidadGuardiasFestivo(persona.getCantidadGuardiasFestivo() + 1);
		}

		// Contador espec�fico para estudiantes
		if (persona instanceof Estudiante && tipo == TipoGuardia.NORMAL) {
			((Estudiante)persona).incrementarGuardiasAsignadas();
		}

		return true;
	}

	// REGISTRAR CUMPLIMIENTO MEJORADO
	public boolean registrarCumplimientoGuardia(int idGuardia) {
		Guardia g = buscarGuardiaPorId(idGuardia);
		if (g == null) return false;

		Persona p = g.getPersona();

		if (p instanceof Estudiante) {
			Estudiante est = (Estudiante) p;
			est.registrarGuardiaCumplida();

			// Si es guardia de recuperaci�n, reducir pendientes
			if (g.getTipo() == TipoGuardia.RECUPERACION) {
				est.setGuardiasAsignadas(est.getGuardiasAsignadas() - 1);
			}
		}
		// Para trabajadores podr�as a�adir l�gica similar si es necesario
		return true;
	}

	// BUSCAR POR ID (compatible)
	public Guardia buscarGuardiaPorId(int id) {
		Guardia encontrada = null;
		for (Guardia guardia : guardias) {
			if (guardia.getId() == id) {
				encontrada = guardia;
				break;
			}
		}
		return encontrada;
	}

	public boolean actualizarGuardia(int id, TipoGuardia nuevoTipo, Persona nuevaPersona, Horario nuevoHorario) {
		boolean actualizado = false;
		Guardia guardia = buscarGuardiaPorId(id);
		if (guardia != null) {
			// Validaci�n b�sica
			if (nuevaPersona.puedeHacerGuardia(nuevoHorario)) {
				// Actualizar
				guardia.setTipo(nuevoTipo);
				guardia.setPersona(nuevaPersona);
				guardia.setHorario(nuevoHorario);
				actualizado = true;
			}
		}
		return actualizado;
	}

	// ELIMINAR GUARDIA MEJORADO
	public boolean eliminarGuardia(int id) {
		boolean eliminar = false;
		Iterator<Guardia> it = guardias.iterator();
		while (it.hasNext()) {
			Guardia g = it.next();
			if (g.getId() == id) {
				Persona p = g.getPersona();

				// Actualizar contadores generales
				p.setGuardiasAsignadas(p.getGuardiasAsignadas() - 1);

				if (g.getTipo() == TipoGuardia.FESTIVO) {
					p.setCantidadGuardiasFestivo(p.getCantidadGuardiasFestivo() - 1);
				}

				// Contador espec�fico para estudiantes
				if (p instanceof Estudiante && g.getTipo() == TipoGuardia.NORMAL) {
					((Estudiante)p).setGuardiasAsignadas(((Estudiante)p).getGuardiasAsignadas() - 1);
				}

				it.remove();
				eliminar = true;
			}
		}
		return eliminar;
	}
}

