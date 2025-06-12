package logica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utiles.Sexo;
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

	/**
	 * Planifica automáticamente las guardias para un mes y año dados.
	 * Devuelve una lista de strings con el resultado de la planificación.
	 */
	public List<String> planificarGuardiasMes(Facultad facultad, int anio, int mes) {
		List<String> resultado = new ArrayList<>();
		YearMonth yearMonth = YearMonth.of(anio, mes);

		List<Persona> personas = facultad.getPersonas();

		List<Trabajador> trabajadores = new ArrayList<>();
		List<Estudiante> estudiantesM = new ArrayList<>();
		List<Estudiante> estudiantesF = new ArrayList<>();

		for (Persona p : personas) {
			if (p instanceof Trabajador && p.getActivo()) {
				trabajadores.add((Trabajador) p);
			} else if (p instanceof Estudiante && p.getActivo()) {
				Estudiante est = (Estudiante) p;
				if (est.getSexo() == Sexo.MASCULINO) {
					estudiantesM.add(est);
				} else {
					estudiantesF.add(est);
				}
			}
		}

		boolean finSemanaMujer = true;
		int idxTrabajador = 0, idxEstudianteM = 0, idxEstudianteF = 0;

		for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
			LocalDate fecha = LocalDate.of(anio, mes, day);
			int dow = fecha.getDayOfWeek().getValue(); // 1=Lunes ... 7=Domingo

			// Guardias de estudiantes hombres: todos los días, 20:00-08:00
			if (!estudiantesM.isEmpty()) {
				Estudiante est = estudiantesM.get(idxEstudianteM % estudiantesM.size());
				Horario h = new Horario(fecha, LocalTime.of(20, 0), LocalTime.of(8, 0));
				if (est.puedeHacerGuardia(h)) {
					resultado.add(String.format("%s: Estudiante (M) %s %s - 20:00 a 08:00", fecha, est.getNombre(), est.getApellidos()));
					idxEstudianteM++;
				}
			}

			// Guardias de estudiantes mujeres: solo fines de semana, alternando con trabajadores
			if (dow == 6 || dow == 7) { // Sábado o Domingo
				if (finSemanaMujer && !estudiantesF.isEmpty()) {
					Estudiante est = estudiantesF.get(idxEstudianteF % estudiantesF.size());
					Horario h = new Horario(fecha, LocalTime.of(8, 0), LocalTime.of(20, 0));
					if (est.puedeHacerGuardia(h)) {
						resultado.add(String.format("%s: Estudiante (F) %s %s - 08:00 a 20:00", fecha, est.getNombre(), est.getApellidos()));
						idxEstudianteF++;
					}
				} else if (!finSemanaMujer && !trabajadores.isEmpty()) {
					// Trabajadores: 9-14 y 14-19
					Trabajador t1 = trabajadores.get(idxTrabajador % trabajadores.size());
					Horario h1 = new Horario(fecha, LocalTime.of(9, 0), LocalTime.of(14, 0));
					if (t1.puedeHacerGuardia(h1)) {
						resultado.add(String.format("%s: Trabajador %s %s - 09:00 a 14:00", fecha, t1.getNombre(), t1.getApellidos()));
						idxTrabajador++;
					}
					Trabajador t2 = trabajadores.get(idxTrabajador % trabajadores.size());
					Horario h2 = new Horario(fecha, LocalTime.of(14, 0), LocalTime.of(19, 0));
					if (t2.puedeHacerGuardia(h2)) {
						resultado.add(String.format("%s: Trabajador %s %s - 14:00 a 19:00", fecha, t2.getNombre(), t2.getApellidos()));
						idxTrabajador++;
					}
				}
				// Alternar para el próximo fin de semana
				if (dow == 7) finSemanaMujer = !finSemanaMujer;
			}
		}
		return resultado;
	}
}

