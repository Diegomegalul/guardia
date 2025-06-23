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
	private List<Guardia> guardiasIncumplidas;
	private int nextId = 1;

	//Constructor
	public GuardiaFactory() {
		this.guardias = new ArrayList<>(); 
		this.guardiasCumplidas = new ArrayList<>();
		this.guardiasIncumplidas = new ArrayList<>();
	}

	//Setters y Getters
	public List<Guardia> getGuardiasIncumplidas() {
		return guardiasIncumplidas;
	}

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
		// Marcar la guardia como cumplida y pasarla al grupo de guardias cumplidas
		g.setCumplida(true);
		if (!guardiasCumplidas.contains(g)) {
			guardiasCumplidas.add(g);
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
	La primera vez planifica dos meses completos. 
	A partir de la segunda vez, revisa el primer mes planificado y asigna guardias de recuperación a quienes no cumplieron, 
	incrementando guardiasIncumplidas.
	*/
	public List<Guardia> planificarGuardiasMes(Facultad facultad, int anio, int mes) {
		ArrayList<Guardia> nuevasGuardias = new ArrayList<>();
		List<Persona> personas = new ArrayList<>(facultad.getPersonas());
		List<Trabajador> trabajadores = new ArrayList<>();
		List<Estudiante> estudiantesM = new ArrayList<>();
		List<Estudiante> estudiantesF = new ArrayList<>();

		// Clasificar personas
		for (Persona p : personas) {
			if (p instanceof Trabajador && p.getActivo()) {
				trabajadores.add((Trabajador) p);
			} else if (p instanceof Estudiante && p.getActivo()) {
				Estudiante est = (Estudiante) p;
				if (est.getSexo() == Sexo.MASCULINO) {
					estudiantesM.add(est);
				} else if (est.getSexo() == Sexo.FEMENINO) {
					estudiantesF.add(est);
				}
			}
		}

		boolean primeraVez = guardias.isEmpty();
		int mesesAPlanificar = primeraVez ? 2 : 1;

		// Revisar solo guardias del mes N-2 para asignar recuperaciones
		if (!primeraVez) {
			int mesARevisar = mes - 2;
			int anioARevisar = anio;
			while (mesARevisar < 1) {
				mesARevisar += 12;
				anioARevisar -= 1;
			}
			java.time.YearMonth ymARevisar = java.time.YearMonth.of(anioARevisar, mesARevisar);

			for (Guardia g : new ArrayList<>(guardias)) {
				if (g.getHorario().getDia().getYear() == ymARevisar.getYear() &&
					g.getHorario().getDia().getMonthValue() == ymARevisar.getMonthValue() &&
					g.getTipo() == TipoGuardia.NORMAL) {
					Persona p = g.getPersona();
					boolean cumplida = false;
					for (Guardia gc : guardiasCumplidas) {
						if (gc.getId() == g.getId()) {
							cumplida = true;
							break;
						}
					}
					if (!cumplida && p instanceof Estudiante) {
						Estudiante est = (Estudiante) p;
						// Solo si no está ya en guardiasIncumplidas
						boolean yaIncumplida = false;
						for (Guardia gi : guardiasIncumplidas) {
							if (gi.getId() == g.getId()) {
								yaIncumplida = true;
								break;
							}
						}
						if (!yaIncumplida) {
							guardiasIncumplidas.add(g);
							est.setGuardiasRecuperacion(est.getGuardiasRecuperacion() + 1);
							est.setGuardiasIncumplidas(est.getGuardiasIncumplidas() + 1);
						}
					}
				}
			}
		}

		for (int m = 0; m < mesesAPlanificar; m++) {
			java.time.YearMonth ym = java.time.YearMonth.of(anio, mes + m);
			boolean finSemanaMujer = true;
			for (int day = 1; day <= ym.lengthOfMonth(); day++) {
				LocalDate fecha = LocalDate.of(ym.getYear(), ym.getMonthValue(), day);
				int dow = fecha.getDayOfWeek().getValue(); // 1=Lunes ... 7=Domingo

				// Guardias de estudiantes hombres: todos los días, 20:00-08:00
				if (!estudiantesM.isEmpty()) {
					Estudiante est = seleccionarEstudianteRecuperacionPrimero(estudiantesM);
					Horario h = new Horario(fecha, LocalTime.of(20, 0), LocalTime.of(8, 0));
					// Verificar si ya existe una guardia para este estudiante, día y horario
					boolean existe = false;
					for (int i = 0; i < guardias.size(); i++) {
						Guardia existente = guardias.get(i);
						if (existente.getPersona().equals(est) &&
							existente.getHorario().getDia().equals(h.getDia()) &&
							existente.getHorario().getHoraInicio().equals(h.getHoraInicio()) &&
							existente.getHorario().getHoraFin().equals(h.getHoraFin())) {
							existe = true;
							break;
						}
					}
					if (!existe) {
						if (est != null && est.puedeHacerGuardia(h)) {
							if (est.getGuardiasRecuperacion() > 0) {
								nuevasGuardias.add(new Guardia(nextId++, TipoGuardia.RECUPERACION, est, h));
								est.setGuardiasRecuperacion(est.getGuardiasRecuperacion() - 1);
							} else {
								nuevasGuardias.add(new Guardia(nextId++, TipoGuardia.NORMAL, est, h));
								est.setGuardiasAsignadas(est.getGuardiasAsignadas() + 1);
							}
						}
					}
				}

				// Guardias de estudiantes mujeres: solo fines de semana, alternando con trabajadores
				if (dow == 6 || dow == 7) {
					if (finSemanaMujer && !estudiantesF.isEmpty()) {
						Estudiante est = seleccionarEstudianteRecuperacionPrimero(estudiantesF);
						Horario h = new Horario(fecha, LocalTime.of(8, 0), LocalTime.of(20, 0));
						boolean existe = false;
						for (int i = 0; i < guardias.size(); i++) {
							Guardia existente = guardias.get(i);
							if (existente.getPersona().equals(est) &&
								existente.getHorario().getDia().equals(h.getDia()) &&
								existente.getHorario().getHoraInicio().equals(h.getHoraInicio()) &&
								existente.getHorario().getHoraFin().equals(h.getHoraFin())) {
								existe = true;
								break;
							}
						}
						if (!existe) {
							if (est != null && est.puedeHacerGuardia(h)) {
								if (est.getGuardiasRecuperacion() > 0) {
									nuevasGuardias.add(new Guardia(nextId++, TipoGuardia.RECUPERACION, est, h));
									est.setGuardiasRecuperacion(est.getGuardiasRecuperacion() - 1);
								} else {
									nuevasGuardias.add(new Guardia(nextId++, TipoGuardia.NORMAL, est, h));
									est.setGuardiasAsignadas(est.getGuardiasAsignadas() + 1);
								}
							}
						}
					} else if (!finSemanaMujer && !trabajadores.isEmpty()) {
						// Trabajadores: 9-14 y 14-19
						for (int turno = 0; turno < 2; turno++) {
							Horario h = (turno == 0)
								? new Horario(fecha, LocalTime.of(9, 0), LocalTime.of(14, 0))
								: new Horario(fecha, LocalTime.of(14, 0), LocalTime.of(19, 0));
							Trabajador t = seleccionarTrabajadorParaGuardia(trabajadores);
							boolean existe = false;
							for (int i = 0; i < guardias.size(); i++) {
								Guardia existente = guardias.get(i);
								if (existente.getPersona().equals(t) &&
									existente.getHorario().getDia().equals(h.getDia()) &&
									existente.getHorario().getHoraInicio().equals(h.getHoraInicio()) &&
									existente.getHorario().getHoraFin().equals(h.getHoraFin())) {
									existe = true;
									break;
								}
							}
							if (!existe) {
								if (t != null && t.puedeHacerGuardia(h)) {
									nuevasGuardias.add(new Guardia(nextId++, TipoGuardia.NORMAL, t, h));
									t.setGuardiasAsignadas(t.getGuardiasAsignadas() + 1);
								}
							}
						}
					}
					// Alternar para el próximo fin de semana
					if (dow == 7) finSemanaMujer = !finSemanaMujer;
				}
			}
		}

		// Solo agregar guardias nuevas que no estén ya en la lista global
		for (int i = 0; i < nuevasGuardias.size(); i++) {
			Guardia nueva = nuevasGuardias.get(i);
			boolean existe = false;
			for (int j = 0; j < guardias.size(); j++) {
				Guardia existente = guardias.get(j);
				if (existente.getPersona().equals(nueva.getPersona()) &&
					existente.getHorario().getDia().equals(nueva.getHorario().getDia()) &&
					existente.getHorario().getHoraInicio().equals(nueva.getHorario().getHoraInicio()) &&
					existente.getHorario().getHoraFin().equals(nueva.getHorario().getHoraFin())) {
					existe = true;
					break;
				}
			}
			if (!existe) {
				guardias.add(nueva);
			}
		}
		return nuevasGuardias;
	}

	// Selecciona primero con guardias de recuperación, si no, el de menos guardias asignadas
	private Estudiante seleccionarEstudianteRecuperacionPrimero(List<Estudiante> lista) {
		Estudiante conRec = null;
		int minAsignadas = Integer.MAX_VALUE;
		Estudiante minEst = null;
		for (Estudiante e : lista) {
			if (e.getGuardiasRecuperacion() > 0) {
				conRec = e;
				break;
			}
			if (e.getGuardiasAsignadas() < minAsignadas) {
				minAsignadas = e.getGuardiasAsignadas();
				minEst = e;
			}
		}
		return (conRec != null) ? conRec : minEst;
	}

	// Selecciona trabajador con menos guardias asignadas
	private Trabajador seleccionarTrabajadorParaGuardia(List<Trabajador> lista) {
		int minAsignadas = Integer.MAX_VALUE;
		Trabajador minTrab = null;
		for (Trabajador t : lista) {
			if (t.getGuardiasAsignadas() < minAsignadas) {
				minAsignadas = t.getGuardiasAsignadas();
				minTrab = t;
			}
		}
		return minTrab;
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

	public static class GuardiasPorPersona {
		public List<Guardia> asignadas;
		public List<Guardia> cumplidas;
		public List<Guardia> incumplidas;
		public GuardiasPorPersona(List<Guardia> asignadas, List<Guardia> cumplidas, List<Guardia> incumplidas) {
			this.asignadas = asignadas;
			this.cumplidas = cumplidas;
			this.incumplidas = incumplidas;
		}
	}

	public GuardiasPorPersona obtenerGuardiasPorPersona(Persona persona) {
		List<Guardia> asignadas = new ArrayList<>();
		List<Guardia> cumplidas = new ArrayList<>();
		List<Guardia> incumplidas = new ArrayList<>();
		for (Guardia g : getGuardias()) {
			if (g.getPersona() != null && g.getPersona().equals(persona)) {
				asignadas.add(g);
			}
		}
		for (Guardia g : getGuardiasCumplidas()) {
			if (g.getPersona() != null && g.getPersona().equals(persona)) {
				cumplidas.add(g);
			}
		}
		for (Guardia g : getGuardiasIncumplidas()) {
			if (g.getPersona() != null && g.getPersona().equals(persona)) {
				incumplidas.add(g);
			}
		}
		return new GuardiasPorPersona(asignadas, cumplidas, incumplidas);
	}
}
