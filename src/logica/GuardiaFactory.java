package logica;

import java.time.LocalDate;
import java.time.LocalTime;
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
	private List<Guardia> guardiasCumplidas;
	private int nextId = 1;

	//Constructor
	public GuardiaFactory() {
		this.guardias = new ArrayList<>(); 
		this.guardiasCumplidas = new ArrayList<>();
	}

	//Setters y Getters
	public List<Guardia> getGuardiasCumplidas() {
		return guardiasCumplidas;
	}

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

	// --- CRUD MEJORADO --- //

	// MÉTODO UNIFICADO PARA CREAR GUARDIAS
	public boolean crearGuardia(TipoGuardia tipo, Persona persona, Horario horario) {
		// Validación básica
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
			// Validar máximo de 3 guardias festivas por persona
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

		// Si es festivo, aumentar contador
		if (tipo == TipoGuardia.FESTIVO) {
			persona.setCantidadGuardiasFestivo(persona.getCantidadGuardiasFestivo() + 1);
		} else {
			// Si el día es festivo según el calendario, también cuenta como festivo
			if (calendario != null && calendario.existeDiaFestivo(horario.getDia())) {
				persona.setCantidadGuardiasFestivo(persona.getCantidadGuardiasFestivo() + 1);
			}
		}

		// Contador específico para estudiantes
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

			// Si es guardia de recuperación, reducir pendientes
			if (g.getTipo() == TipoGuardia.RECUPERACION) {
				est.setGuardiasAsignadas(est.getGuardiasAsignadas() - 1);
			}
		}
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
			// Validación básica
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

	/*
	  Planifica automáticamente las guardias para un mes y año dados.
	  Devuelve una lista de guardias con el resultado de la planificación.
	 */
	public List<Guardia> planificarGuardiasMes(Facultad facultad, int anio, int mes) {
		ArrayList<Guardia> guardias = new ArrayList<>();
		List<Persona> personas = new ArrayList<>(facultad.getPersonas());
		List<Trabajador> trabajadores = new ArrayList<>();
		List<Estudiante> estudiantesM = new ArrayList<>();
		List<Estudiante> estudiantesF = new ArrayList<>();

		// --- NUEVO: Asignar guardias de recuperación antes de planificar normales ---
		planificarGuardiasRecuperacion(anio, mes);

		// Clasificar personas
		for (Persona p : personas) {
			if (p instanceof Trabajador && p.getActivo()) {
				trabajadores.add((Trabajador) p);
			} else if (p instanceof Estudiante && p.getActivo()) {
				Estudiante est = (Estudiante) p;
				// Solo agregar estudiantes que NO deben guardias de recuperación
				if (est.calcularGuardiasPendientes() <= 0) {
					if (est.getSexo() == Sexo.MASCULINO) {
						estudiantesM.add(est);
					} else if (est.getSexo() == Sexo.FEMENINO) {
						estudiantesF.add(est);
					}
				}
			}
		}

		java.time.YearMonth yearMonth = java.time.YearMonth.of(anio, mes);

		boolean finSemanaMujer = true;
		int idxTrabajador = 0, idxEstudianteM = 0, idxEstudianteF = 0;

		for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
			LocalDate fecha = LocalDate.of(anio, mes, day);
			int dow = fecha.getDayOfWeek().getValue(); // 1=Lunes ... 7=Domingo

			// --- NUEVO: Si el día es festivo, asignar a la persona con menos guardias festivas ---
			if (calendario != null && calendario.existeDiaFestivo(fecha)) {
				// Buscar persona con menor cantidad de guardias festivas (de todos los activos)
				Persona menosFestivos = null;
				int minFestivos = Integer.MAX_VALUE;
				for (Persona p : personas) {
					if (p.getActivo() && p.getCantidadGuardiasFestivo() < minFestivos) {
						minFestivos = p.getCantidadGuardiasFestivo();
						menosFestivos = p;
					}
				}
				if (menosFestivos != null) {
					// Asignar guardia festiva (tipo FESTIVO)
					Horario hFestivo = new Horario(fecha, LocalTime.of(8, 0), LocalTime.of(20, 0));
					if (menosFestivos.puedeHacerGuardia(hFestivo)) {
						guardias.add(new Guardia(nextId++, TipoGuardia.FESTIVO, menosFestivos, hFestivo));
						menosFestivos.setCantidadGuardiasFestivo(menosFestivos.getCantidadGuardiasFestivo() + 1);
						menosFestivos.setGuardiasAsignadas(menosFestivos.getGuardiasAsignadas() + 1);
					}
				}
				continue; // No asignar guardias normales ese día
			}

			// Guardias de estudiantes hombres: todos los días, 20:00-08:00
			if (!estudiantesM.isEmpty()) {
				Estudiante est = estudiantesM.get(idxEstudianteM % estudiantesM.size());
				Horario h = new Horario(fecha, LocalTime.of(20, 0), LocalTime.of(8, 0));
				if (est.puedeHacerGuardia(h)) {
					guardias.add(new Guardia(nextId++, TipoGuardia.NORMAL, est, h));
					idxEstudianteM++;
				}
			}

			// Guardias de estudiantes mujeres: solo fines de semana, alternando con trabajadores
			if (dow == 6 || dow == 7) { // Sábado o Domingo
				if (finSemanaMujer && !estudiantesF.isEmpty()) {
					Estudiante est = estudiantesF.get(idxEstudianteF % estudiantesF.size());
					Horario h = new Horario(fecha, LocalTime.of(8, 0), LocalTime.of(20, 0));
					if (est.puedeHacerGuardia(h)) {
						guardias.add(new Guardia(nextId++, TipoGuardia.NORMAL, est, h));
						idxEstudianteF++;
					}
				} else if (!finSemanaMujer && !trabajadores.isEmpty()) {
					// Trabajadores: 9-14 y 14-19
					Trabajador t1 = trabajadores.get(idxTrabajador % trabajadores.size());
					Horario h1 = new Horario(fecha, LocalTime.of(9, 0), LocalTime.of(14, 0));
					if (t1.puedeHacerGuardia(h1)) {
						guardias.add(new Guardia(nextId++, TipoGuardia.NORMAL, t1, h1));
						idxTrabajador++;
					}
					Trabajador t2 = trabajadores.get(idxTrabajador % trabajadores.size());
					Horario h2 = new Horario(fecha, LocalTime.of(14, 0), LocalTime.of(19, 0));
					if (t2.puedeHacerGuardia(h2)) {
						guardias.add(new Guardia(nextId++, TipoGuardia.NORMAL, t2, h2));
						idxTrabajador++;
					}
				}
				// Alternar para el próximo fin de semana
				if (dow == 7) finSemanaMujer = !finSemanaMujer;
			}
		}

		return guardias;
	}

	/**
	 * Planifica guardias de recuperación para estudiantes con guardias pendientes
	 * que hayan pasado al menos dos meses desde la asignación de la guardia pendiente.
	 * Se asigna la guardia de recuperación en el mes siguiente disponible.
	 */
	public void planificarGuardiasRecuperacion(int anio, int mes) {
		// Buscar estudiantes con guardias pendientes
		List<Persona> personas = new ArrayList<>();
		if (personas.isEmpty()) {
			personas = logica.PlanificadorGuardias.getInstancia().getFacultad().getPersonas();
		}

		for (Persona p : personas) {
			if (p instanceof logica.Estudiante) {
				logica.Estudiante est = (logica.Estudiante) p;
				int pendientes = est.calcularGuardiasPendientes();
				if (pendientes > 0) {
					// Buscar la última guardia asignada no cumplida
					LocalDate ultimaAsignada = null;
					for (Guardia g : guardias) {
						if (g.getPersona().equals(est)) {
							LocalDate fecha = g.getHorario().getDia();
							// Solo considerar guardias normales (no recuperación)
							if (g.getTipo() == utiles.TipoGuardia.NORMAL) {
								if (ultimaAsignada == null || fecha.isAfter(ultimaAsignada)) {
									ultimaAsignada = fecha;
								}
							}
						}
					}
					if (ultimaAsignada != null) {
						// Revisar si han pasado al menos dos meses desde la última asignación
						LocalDate fechaRecuperacion = ultimaAsignada.plusMonths(2).withDayOfMonth(1);
						LocalDate fechaPlanificada = LocalDate.of(anio, mes, 1);
						if (!fechaPlanificada.isBefore(fechaRecuperacion)) {
							// Asignar guardias de recuperación para nivelar
							for (int i = 0; i < pendientes; i++) {
								// Buscar un día hábil en el mes para la guardia de recuperación
								LocalDate dia = fechaPlanificada.plusDays(i);
								if (dia.getMonthValue() != mes) break;
								// Horario nocturno para hombres, diurno para mujeres
								Horario horario;
								if (est.getSexo() == utiles.Sexo.MASCULINO) {
									horario = new Horario(dia, java.time.LocalTime.of(20, 0), java.time.LocalTime.of(8, 0));
								} else {
									// Buscar fin de semana
									while (dia.getDayOfWeek() != java.time.DayOfWeek.SATURDAY &&
											dia.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
										dia = dia.plusDays(1);
										if (dia.getMonthValue() != mes) break;
									}
									horario = new Horario(dia, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(20, 0));
								}
								// Solo si puede hacer guardia
								if (est.puedeHacerGuardia(horario)) {
									crearGuardia(utiles.TipoGuardia.RECUPERACION, est, horario);
								}
							}
						}
					}
				}
			}
		}
	}
}

