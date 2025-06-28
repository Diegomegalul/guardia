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
	// Atributos
	private Horario horario;
	private Calendario calendario;
	private List<Guardia> guardias;
	private List<Guardia> guardiasCumplidas;
	private List<Guardia> guardiasIncumplidas;
	private int nextId = 1;
	// Nuevo atributo: último mes planificado
	private LocalDate ultimoMesPlanificado = null;
	// Nuevo atributo: alternancia de fin de semana (true = mujeres, false =
	// trabajadores)
	private boolean finSemanaMujerActual = false;

	// Constructor
	public GuardiaFactory() {
		this.guardias = new ArrayList<>();
		this.guardiasCumplidas = new ArrayList<>();
		this.guardiasIncumplidas = new ArrayList<>();
		this.calendario = Calendario.getInstancia(); // Usar singleton
		this.finSemanaMujerActual = false; // El primer fin de semana es de estudiantes mujer
	}

	// Setters y Getters
	public LocalDate getUltimoMesPlanificado() {
		return ultimoMesPlanificado;
	}

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
		this.guardias = new ArrayList<>(guardias); //
	}
	// Metodos

	// --- CRUD MEJORADO --- //

	// MÉTODO UNIFICADO PARA CREAR GUARDIAS
	public boolean crearGuardia(TipoGuardia tipo, Persona persona, Horario horario) {
		// Validación básica
		if (!persona.puedeHacerGuardia(horario)) {
			return false;
		}

		// Detectar si es día festivo
		boolean esFestivo = (calendario != null && calendario.existeDiaFestivo(horario.getDia()));

		// Ajustar tipo según el día festivo y tipo original
		if (tipo == TipoGuardia.VOLUNTARIA && esFestivo) {
			tipo = TipoGuardia.VOLUNTARIA_FESTIVO;
		} else if (tipo == TipoGuardia.RECUPERACION && esFestivo) {
			tipo = TipoGuardia.RECUPERACION_FESTIVO;
		}

		// Validaciones específicas por tipo
		boolean valido = true;
		switch (tipo) {
			case RECUPERACION:
			case RECUPERACION_FESTIVO:
				valido = (persona instanceof Estudiante) && ((Estudiante) persona).tieneGuardiasPendientes();
				break;
			case VOLUNTARIA:
			case VOLUNTARIA_FESTIVO:
				valido = (persona instanceof Trabajador) && ((Trabajador) persona).getVoluntario();
				break;
			case FESTIVO:
				valido = persona.getCantidadGuardiasFestivo() < 3;
				break;
			default:
				break;
		}
		if (!valido) {
			return false;
		}

		// Crear guardia
		Guardia nuevaGuardia = new Guardia(nextId++, tipo, persona, horario);
		guardias.add(nuevaGuardia);

		// Actualizar contadores
		persona.setGuardiasPlanificadas(persona.getGuardiasPlanificadas() + 1);

		// Si es festivo, aumentar contador
		if (tipo == TipoGuardia.FESTIVO || tipo == TipoGuardia.RECUPERACION_FESTIVO
				|| tipo == TipoGuardia.VOLUNTARIA_FESTIVO) {
			persona.setCantidadGuardiasFestivo(persona.getCantidadGuardiasFestivo() + 1);
		} else {
			// Si el día es festivo según el calendario, también cuenta como festivo
			if (esFestivo) {
				persona.setCantidadGuardiasFestivo(persona.getCantidadGuardiasFestivo() + 1);
			}
		}

		// Contador específico para estudiantes
		if (persona instanceof Estudiante && (tipo == TipoGuardia.NORMAL || tipo == TipoGuardia.RECUPERACION
				|| tipo == TipoGuardia.RECUPERACION_FESTIVO)) {
			((Estudiante) persona).incrementarGuardiasAsignadas();
		}

		return true;
	}

	// REGISTRAR CUMPLIMIENTO MEJORADO
	public boolean registrarCumplimientoGuardia(int idGuardia) {
		Guardia g = buscarGuardiaPorId(idGuardia);
		boolean resultado = false;
		if (g != null) {
			Persona p = g.getPersona();
			// Disminuir guardias planificadas
			p.setGuardiasPlanificadas(p.getGuardiasPlanificadas() - 1);
			// Aumentar guardias cumplidas
			p.setGuardiasCumplidas(p.getGuardiasCumplidas() + 1);
			// Marcar la guardia como cumplida y pasarla al grupo de guardias cumplidas
			g.setCumplida(true);
			if (!guardiasCumplidas.contains(g)) {
				guardiasCumplidas.add(g);
			}
			guardias.remove(g); // Eliminar de la lista de guardias planificadas
			resultado = true;
		}
		return resultado;
	}

	// Marcar guardia como incumplida: la elimina de guardias y la pasa a
	// incumplidas
	public boolean registrarIncumplimientoGuardia(int idGuardia) {
		Guardia g = buscarGuardiaPorId(idGuardia);
		boolean resultado = false;
		if (g != null) {
			Persona p = g.getPersona();
			p.setGuardiasPlanificadas(p.getGuardiasPlanificadas() - 1);
			if (p instanceof Estudiante) {
				Estudiante est = (Estudiante) p;
				est.setGuardiasIncumplidas(est.getGuardiasIncumplidas() + 1);
				est.setGuardiasRecuperacion(est.getGuardiasRecuperacion() + 1);
			}
			if (!guardiasIncumplidas.contains(g)) {
				guardiasIncumplidas.add(g);
			}
			guardias.remove(g);
			resultado = true;
		}
		return resultado;
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
				p.setGuardiasPlanificadas(p.getGuardiasPlanificadas() - 1);

				if (g.getTipo() == TipoGuardia.FESTIVO) {
					p.setCantidadGuardiasFestivo(p.getCantidadGuardiasFestivo() - 1);
				}

				// Contador especifico para estudiantes
				if (p instanceof Estudiante && g.getTipo() == TipoGuardia.NORMAL) {
					((Estudiante) p).setGuardiasPlanificadas(((Estudiante) p).getGuardiasPlanificadas() - 1);
				}

				it.remove();
				eliminar = true;
			}
		}
		return eliminar;
	}

	/*
	 * Planifica automáticamente las guardias para un mes y año dados.
	 * La primera vez planifica dos meses completos.
	 * A partir de la segunda vez, revisa el primer mes planificado y asigna
	 * guardias de recuperación a quienes no cumplieron,
	 * incrementando guardiasIncumplidas.
	 */
	// Método principal: planificar guardias para uno o dos meses
	public List<Guardia> planificarGuardiasMes(Facultad facultad, int anio, int mes) {
		ArrayList<Guardia> nuevasGuardias = new ArrayList<>();
		List<Persona> personas = new ArrayList<>(facultad.getPersonas());
		List<Trabajador> trabajadores = new ArrayList<>();
		List<Estudiante> estudiantesM = new ArrayList<>();
		List<Estudiante> estudiantesF = new ArrayList<>();

		// Clasificar personas
		clasificarPersonas(personas, trabajadores, estudiantesM, estudiantesF);

		boolean primeraVez = guardias.isEmpty();
		int mesesAPlanificar = primeraVez ? 2 : 1;

		for (int offset = 0; offset < mesesAPlanificar; offset++) {
			int mesPlan = mes + offset;
			int anioPlan = anio;
			while (mesPlan > 12) {
				mesPlan -= 12;
				anioPlan += 1;
			}
			boolean esJA = (mesPlan == 7 || mesPlan == 8);

			LocalDate mesActual = LocalDate.of(anioPlan, mesPlan, 1);
			boolean finSemanaMujer = finSemanaMujerActual;
			if (ultimoMesPlanificado == null || !mesActual.getMonth().equals(ultimoMesPlanificado.getMonth())
					|| mesActual.getYear() != ultimoMesPlanificado.getYear()) {
				// Si es el primer mes o un mes nuevo, usar el valor actual de
				// finSemanaMujerActual
			} else {
				// Si es el mismo mes, no cambiar alternancia
			}

			if (!primeraVez && offset == 0) {
				revisarGuardiasIncumplidas(mes, anio);
			}

			if (esJA) {
				planificarGuardiasJulioAgosto(trabajadores, nuevasGuardias, anioPlan, mesPlan);
			} else {
				java.time.YearMonth ym = java.time.YearMonth.of(anioPlan, mesPlan);
				for (int day = 1; day <= ym.lengthOfMonth(); day++) {
					LocalDate fecha = LocalDate.of(ym.getYear(), ym.getMonthValue(), day);
					int dow = fecha.getDayOfWeek().getValue(); // 1=Lunes ... 7=Domingo
					boolean esFestivo = (calendario != null && calendario.existeDiaFestivo(fecha));
					// Guardias de estudiantes hombres: todos los días, 20:00-08:00
					if (!estudiantesM.isEmpty()) {
						Estudiante est = seleccionarEstudianteRecuperacionPrimero(estudiantesM);
						Horario h = new Horario(fecha, LocalTime.of(20, 0), LocalTime.of(8, 0));
						boolean existe = existeGuardiaParaPersonaEnHorario(est, fecha, LocalTime.of(20, 0),
								LocalTime.of(8, 0), null);
						if (!existe && est != null && est.puedeHacerGuardia(h)) {
							TipoGuardia tipo;
							if (est.getGuardiasRecuperacion() > 0) {
								tipo = esFestivo ? TipoGuardia.RECUPERACION_FESTIVO : TipoGuardia.RECUPERACION;
								est.setGuardiasRecuperacion(est.getGuardiasRecuperacion() - 1);
								est.setGuardiasRecuperacionAsignadas(est.getGuardiasRecuperacionAsignadas() + 1);
							} else {
								tipo = esFestivo ? TipoGuardia.FESTIVO : TipoGuardia.NORMAL;
							}
							nuevasGuardias.add(new Guardia(nextId++, tipo, est, h));
							if (tipo == TipoGuardia.NORMAL) {
								est.setGuardiasPlanificadas(est.getGuardiasPlanificadas() + 1);
							}else if (tipo == TipoGuardia.FESTIVO) {
								est.setCantidadGuardiasFestivo(est.getCantidadGuardiasFestivo() + 1);
								est.setGuardiasPlanificadas(est.getGuardiasPlanificadas() + 1);
							}else if (tipo == TipoGuardia.RECUPERACION_FESTIVO) {
								est.setCantidadGuardiasFestivo(est.getCantidadGuardiasFestivo() + 1);
							}
						}
					}
					// Guardias de estudiantes mujeres: solo fines de semana, alternando con
					// trabajadores
					if (dow == 6 || dow == 7) {
						if (finSemanaMujer && !estudiantesF.isEmpty()) {
							Estudiante est = seleccionarEstudianteRecuperacionPrimero(estudiantesF);
							Horario h = new Horario(fecha, LocalTime.of(8, 0), LocalTime.of(20, 0));
							boolean existe = existeGuardiaParaPersonaEnHorario(est, fecha, LocalTime.of(8, 0),
									LocalTime.of(20, 0), null);
							if (!existe && est != null && est.puedeHacerGuardia(h)) {
								TipoGuardia tipo;
								if (est.getGuardiasRecuperacion() > 0) {
									tipo = esFestivo ? TipoGuardia.RECUPERACION_FESTIVO : TipoGuardia.RECUPERACION;
									est.setGuardiasRecuperacion(est.getGuardiasRecuperacion() - 1);
								} else {
									tipo = esFestivo ? TipoGuardia.FESTIVO : TipoGuardia.NORMAL;
								}
								nuevasGuardias.add(new Guardia(nextId++, tipo, est, h));
								est.setGuardiasPlanificadas(est.getGuardiasPlanificadas() + 1);
								if (tipo == TipoGuardia.FESTIVO || tipo == TipoGuardia.RECUPERACION_FESTIVO) {
									est.setCantidadGuardiasFestivo(est.getCantidadGuardiasFestivo() + 1);
								}
							}
						} else if (!finSemanaMujer && !trabajadores.isEmpty()) {
							for (int turno = 0; turno < 2; turno++) {
								LocalTime ini = (turno == 0) ? LocalTime.of(9, 0) : LocalTime.of(14, 0);
								LocalTime fin = (turno == 0) ? LocalTime.of(14, 0) : LocalTime.of(19, 0);
								Horario h = new Horario(fecha, ini, fin);
								Trabajador t = seleccionarTrabajadorParaGuardia(trabajadores);
								boolean existe = existeGuardiaParaPersonaEnHorario(t, fecha, ini, fin, null);
								if (!existe && t != null && t.puedeHacerGuardia(h)) {
									TipoGuardia tipo = esFestivo ? TipoGuardia.FESTIVO : TipoGuardia.NORMAL;
									nuevasGuardias.add(new Guardia(nextId++, tipo, t, h));
									t.setGuardiasPlanificadas(t.getGuardiasPlanificadas() + 1);
									if (tipo == TipoGuardia.FESTIVO) {
										t.setCantidadGuardiasFestivo(t.getCantidadGuardiasFestivo() + 1);
									}
								}
							}
						}
						if (dow == 7) {
							finSemanaMujer = !finSemanaMujer;
							finSemanaMujerActual = finSemanaMujer;
						}
					}
				}
			}
			// Al finalizar el mes, actualizar el último mes planificado
			ultimoMesPlanificado = mesActual;
		}
		agregarGuardiasNoDuplicadas(nuevasGuardias);
		return nuevasGuardias;
	}

	// Clasifica personas en listas de trabajadores y estudiantes por sexo
	private void clasificarPersonas(List<Persona> personas, List<Trabajador> trabajadores,
			List<Estudiante> estudiantesM, List<Estudiante> estudiantesF) {
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
	}

	// Revisa guardias incumplidas del mes N-2 (excepto si es septiembre, que revisa
	// junio)
	private void revisarGuardiasIncumplidas(int mes, int anio) {
		int mesARevisar;
		int anioARevisar = anio;
		if (mes == 10) {
			mesARevisar = 6; // Junio
		}
		if (mes == 9) {
			mesARevisar = 5; // Mayo
		} else {
			mesARevisar = mes - 2;
			while (mesARevisar < 1) {
				mesARevisar += 12;
				anioARevisar -= 1;
			}
		}
		java.time.YearMonth ymARevisar = java.time.YearMonth.of(anioARevisar, mesARevisar);

		for (Guardia g : new ArrayList<>(guardias)) {
			if (g.getHorario().getDia().getYear() == ymARevisar.getYear() &&
					g.getHorario().getDia().getMonthValue() == ymARevisar.getMonthValue()) {
				boolean cumplida = false;
				for (Guardia gc : guardiasCumplidas) {
					if (gc.getId() == g.getId()) {
						cumplida = true;
						break;
					}
				}
				boolean yaIncumplida = false;
				for (Guardia gi : guardiasIncumplidas) {
					if (gi.getId() == g.getId()) {
						yaIncumplida = true;
						break;
					}
				}
				if (!cumplida && !yaIncumplida) {
					registrarIncumplimientoGuardia(g.getId());
				}
			}
		}
	}

	// Planifica guardias para julio/agosto (solo voluntarios)
	private void planificarGuardiasJulioAgosto(List<Trabajador> trabajadores, List<Guardia> nuevasGuardias,
			int anioPlan, int mesPlan) {
		java.util.Map<LocalDate, Integer> guardiasPorDia = new java.util.HashMap<LocalDate, Integer>();
		for (Trabajador t : trabajadores) {
			if (t.getVoluntario() && t.getFechaGuardiaVoluntaria() != null) {
				LocalDate fecha = t.getFechaGuardiaVoluntaria();
				if (fecha.getYear() == anioPlan && fecha.getMonthValue() == mesPlan) {
					int cantidad = guardiasPorDia.containsKey(fecha) ? guardiasPorDia.get(fecha) : 0;
					boolean esFestivo = (calendario != null && calendario.existeDiaFestivo(fecha));
					if (cantidad == 0) {
						Horario h1 = new Horario(fecha, LocalTime.of(9, 0), LocalTime.of(14, 0));
						if (t.puedeHacerGuardia(h1)) {
							TipoGuardia tipo = esFestivo ? TipoGuardia.VOLUNTARIA_FESTIVO : TipoGuardia.VOLUNTARIA;
							Guardia g = new Guardia(nextId++, tipo, t, h1);
							nuevasGuardias.add(g);
							guardias.add(g);
							t.setGuardiasPlanificadas(t.getGuardiasPlanificadas() + 1);
							if (esFestivo) {
								t.setCantidadGuardiasFestivo(t.getCantidadGuardiasFestivo() + 1);
							}
							guardiasPorDia.put(fecha, 1);
						}
					} else if (cantidad == 1) {
						Horario h2 = new Horario(fecha, LocalTime.of(14, 0), LocalTime.of(19, 0));
						if (t.puedeHacerGuardia(h2)) {
							TipoGuardia tipo = esFestivo ? TipoGuardia.VOLUNTARIA_FESTIVO : TipoGuardia.VOLUNTARIA;
							Guardia g = new Guardia(nextId++, tipo, t, h2);
							nuevasGuardias.add(g);
							guardias.add(g);
							t.setGuardiasPlanificadas(t.getGuardiasPlanificadas() + 1);
							if (esFestivo) {
								t.setCantidadGuardiasFestivo(t.getCantidadGuardiasFestivo() + 1);
							}
							guardiasPorDia.put(fecha, 2);
						}
					}
				}
			}
		}
	}

	/**
	 * Planifica guardias de recuperación para estudiantes con guardias pendientes
	 * que hayan pasado al menos dos meses desde la asignación de la guardia
	 * pendiente.
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
				int pendientes = est.getGuardiasRecuperacion();
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
								if (dia.getMonthValue() != mes)
									break;
								// Horario nocturno para hombres, diurno para mujeres
								Horario horario;
								if (est.getSexo() == utiles.Sexo.MASCULINO) {
									horario = new Horario(dia, java.time.LocalTime.of(20, 0),
											java.time.LocalTime.of(8, 0));
								} else {
									// Buscar fin de semana
									while (dia.getDayOfWeek() != java.time.DayOfWeek.SATURDAY &&
											dia.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
										dia = dia.plusDays(1);
										if (dia.getMonthValue() != mes)
											break;
									}
									horario = new Horario(dia, java.time.LocalTime.of(8, 0),
											java.time.LocalTime.of(20, 0));
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

	// Selecciona primero un estudiante con guardias de recuperación, si no, el de
	// menos guardias asignadas y si hay empate, el de menos guardias cumplidas
	private Estudiante seleccionarEstudianteRecuperacionPrimero(List<Estudiante> lista) {
		Estudiante seleccionado = null;
		int maxRecuperacion = -1;
		int minAsignadas = Integer.MAX_VALUE;
		int minCumplidas = Integer.MAX_VALUE;
		// Primero buscar el que más guardias de recuperación tiene
		for (Estudiante e : lista) {
			int rec = e.getGuardiasRecuperacion();
			if (rec > maxRecuperacion) {
				seleccionado = e;
				maxRecuperacion = rec;
				minAsignadas = e.getGuardiasPlanificadas();
				minCumplidas = e.getGuardiasCumplidas();
			} else if (rec == maxRecuperacion && rec > 0) {
				// Si hay empate en recuperación, elegir el de menos asignadas
				int asignadas = e.getGuardiasPlanificadas();
				if (asignadas < minAsignadas) {
					seleccionado = e;
					minAsignadas = asignadas;
					minCumplidas = e.getGuardiasCumplidas();
				} else if (asignadas == minAsignadas) {
					// Si también hay empate en asignadas, elegir el de menos cumplidas
					int cumplidas = e.getGuardiasCumplidas();
					if (cumplidas < minCumplidas) {
						seleccionado = e;
						minCumplidas = cumplidas;
					}
				}
			}
		}
		// Si nadie tiene guardias de recuperación, buscar el de menos asignadas y menos
		// cumplidas
		if (maxRecuperacion <= 0) {
			seleccionado = null;
			minAsignadas = Integer.MAX_VALUE;
			minCumplidas = Integer.MAX_VALUE;
			for (Estudiante e : lista) {
				int asignadas = e.getGuardiasPlanificadas();
				int cumplidas = e.getGuardiasCumplidas();
				if (asignadas < minAsignadas) {
					seleccionado = e;
					minAsignadas = asignadas;
					minCumplidas = cumplidas;
				} else if (asignadas == minAsignadas) {
					if (cumplidas < minCumplidas) {
						seleccionado = e;
						minCumplidas = cumplidas;
					}
				}
			}
		}
		return seleccionado;
	}

	// Selecciona el trabajador con menos guardias asignadas y menos guardias
	// cumplidas
	private Trabajador seleccionarTrabajadorParaGuardia(List<Trabajador> lista) {
		Trabajador seleccionado = null;
		int minAsignadas = Integer.MAX_VALUE;
		int minCumplidas = Integer.MAX_VALUE;
		for (Trabajador t : lista) {
			int asignadas = t.getGuardiasPlanificadas();
			int cumplidas = 0;
			// Buscar cuántas guardias cumplidas tiene este trabajador
			for (Guardia g : guardiasCumplidas) {
				if (g.getPersona() != null && g.getPersona().equals(t)) {
					cumplidas++;
				}
			}
			if (seleccionado == null ||
					asignadas < minAsignadas ||
					(asignadas == minAsignadas && cumplidas < minCumplidas)) {
				seleccionado = t;
				minAsignadas = asignadas;
				minCumplidas = cumplidas;
			}
		}
		return seleccionado;
	}

	// Devuelve una lista de personas con guardias del mismo tipo y clase
	// (Estudiante/Trabajador)
	public List<Persona> obtenerPersonasMismoTipoYGuardia(Persona persona, utiles.TipoGuardia tipo) {
		List<Persona> lista = new ArrayList<>();
		for (Guardia g : guardias) {
			if (g.getTipo() == tipo && g.getPersona() != null) {
				if (persona instanceof Estudiante && g.getPersona() instanceof Estudiante) {
					lista.add(g.getPersona());
				} else if (persona instanceof Trabajador && g.getPersona() instanceof Trabajador) {
					lista.add(g.getPersona());
				}
			}
		}
		return lista;
	}

	// Busca una guardia de una persona y tipo
	public Guardia buscarGuardiaDePersona(Persona persona, utiles.TipoGuardia tipo) {
		for (Guardia g : guardias) {
			if (g.getPersona() != null && g.getPersona().getCi().equals(persona.getCi()) && g.getTipo() == tipo) {
				return g;
			}
		}
		return null;
	}

	// Verifica si existe una guardia para una persona en una fecha y horario,
	// ignorando una guardia específica
	public boolean existeGuardiaParaPersonaEnHorario(Persona persona, java.time.LocalDate fecha,
			java.time.LocalTime inicio, java.time.LocalTime fin, Guardia ignorar) {
		for (Guardia g : guardias) {
			if (g != ignorar && g.getPersona() != null && g.getPersona().getCi().equals(persona.getCi()) &&
					g.getHorario().getDia().equals(fecha) &&
					g.getHorario().getHoraInicio().equals(inicio) &&
					g.getHorario().getHoraFin().equals(fin)) {
				return true;
			}
		}
		return false;
	}

	// Agrega las guardias nuevas a la lista global si no están duplicadas
	private void agregarGuardiasNoDuplicadas(List<Guardia> nuevasGuardias) {
		for (Guardia nueva : nuevasGuardias) {
			boolean existe = false;
			for (Guardia existente : guardias) {
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
	}

	// Busca si en el fin de semana dado (sábado y domingo) hubo guardia de mujeres
	// o trabajadores
	private Boolean buscarTipoFinDeSemana(LocalDate fecha, List<Guardia> guardias, List<Estudiante> estudiantesF,
			List<Trabajador> trabajadores) {
		boolean hayMujer = false;
		boolean hayTrabajador = false;
		LocalDate sabado = fecha.with(java.time.DayOfWeek.SATURDAY);
		LocalDate domingo = fecha.with(java.time.DayOfWeek.SUNDAY);
		for (Guardia g : guardias) {
			LocalDate dia = g.getHorario().getDia();
			if (dia.equals(sabado) || dia.equals(domingo)) {
				if (g.getPersona() instanceof Estudiante && estudiantesF.contains(g.getPersona())) {
					hayMujer = true;
				}
				if (g.getPersona() instanceof Trabajador && trabajadores.contains(g.getPersona())) {
					hayTrabajador = true;
				}
			}
		}
		if (hayMujer && !hayTrabajador)
			return true;
		if (!hayMujer && hayTrabajador)
			return false;
		return null; // No hay guardias o hay de ambos tipos
	}

	// Planifica guardias solo en los huecos disponibles del mes dado (verifica
	// horarios y alternancia)
	public int planificarGuardiasDisponiblesMes(Facultad facultad, int anio, int mes) {
		int guardiasAgregadas = 0;
		List<Persona> personas = new ArrayList<>(facultad.getPersonas());
		List<Trabajador> trabajadores = new ArrayList<>();
		List<Estudiante> estudiantesM = new ArrayList<>();
		List<Estudiante> estudiantesF = new ArrayList<>();
		clasificarPersonas(personas, trabajadores, estudiantesM, estudiantesF);
		java.time.YearMonth ym = java.time.YearMonth.of(anio, mes);
		LocalDate mesActual = LocalDate.of(anio, mes, 1);
		boolean finSemanaMujer = finSemanaMujerActual;
		for (int day = 1; day <= ym.lengthOfMonth(); day++) {
			LocalDate fecha = LocalDate.of(ym.getYear(), ym.getMonthValue(), day);
			int dow = fecha.getDayOfWeek().getValue();
			boolean esFinDeSemana = (dow == 6 || dow == 7);
			boolean esFestivo = (calendario != null && calendario.existeDiaFestivo(fecha));

			// Al comenzar cada sábado, decidir alternancia real según guardias ya asignadas
			if (dow == 6) {
				Boolean tipoFinSemana = buscarTipoFinDeSemana(fecha, guardias, estudiantesF, trabajadores);
				if (tipoFinSemana != null) {
					finSemanaMujer = tipoFinSemana;
				}
			}

			boolean ocupadoNocturno = false;
			boolean ocupadoDiurno = false;
			boolean ocupadoTrab1 = false;
			boolean ocupadoTrab2 = false;
			for (Guardia g : guardias) {
				if (g.getHorario().getDia().equals(fecha)) {
					LocalTime ini = g.getHorario().getHoraInicio();
					LocalTime fin = g.getHorario().getHoraFin();
					if (ini.equals(LocalTime.of(20, 0)) && fin.equals(LocalTime.of(8, 0)))
						ocupadoNocturno = true;
					if (ini.equals(LocalTime.of(8, 0)) && fin.equals(LocalTime.of(20, 0)))
						ocupadoDiurno = true;
					if (ini.equals(LocalTime.of(9, 0)) && fin.equals(LocalTime.of(14, 0)))
						ocupadoTrab1 = true;
					if (ini.equals(LocalTime.of(14, 0)) && fin.equals(LocalTime.of(19, 0)))
						ocupadoTrab2 = true;
				}
			}

			// Turno nocturno de estudiante varón (todos los días)
			if (!ocupadoNocturno && !estudiantesM.isEmpty()) {
				Estudiante seleccionado = null;
				int minCumplidas = Integer.MAX_VALUE;
				int minPlanificadas = Integer.MAX_VALUE;
				for (Estudiante est : estudiantesM) {
					Horario h = new Horario(fecha, LocalTime.of(20, 0), LocalTime.of(8, 0));
					if (est.puedeHacerGuardia(h)) {
						int cumplidas = est.getGuardiasCumplidas();
						int planificadas = est.getGuardiasPlanificadas();
						if (cumplidas < minCumplidas || (cumplidas == minCumplidas && planificadas < minPlanificadas)) {
							seleccionado = est;
							minCumplidas = cumplidas;
							minPlanificadas = planificadas;
						}
					}
				}
				if (seleccionado != null) {
					TipoGuardia tipo = esFestivo ? TipoGuardia.FESTIVO : TipoGuardia.NORMAL;
					Horario h = new Horario(fecha, LocalTime.of(20, 0), LocalTime.of(8, 0));
					crearGuardia(tipo, seleccionado, h);
					guardiasAgregadas++;
				}
			}

			if (!esFinDeSemana) {
				// Entresemana: ya se asignó el turno nocturno arriba
				continue;
			} else {
				// Fin de semana: máximo dos guardias (diurna mujer y dos turnos trabajador)
				if (!ocupadoDiurno && finSemanaMujer && !estudiantesF.isEmpty()) {
					Estudiante seleccionado = null;
					int minCumplidas = Integer.MAX_VALUE;
					int minPlanificadas = Integer.MAX_VALUE;
					for (Estudiante est : estudiantesF) {
						Horario h = new Horario(fecha, LocalTime.of(8, 0), LocalTime.of(20, 0));
						if (est.puedeHacerGuardia(h)) {
							int cumplidas = est.getGuardiasCumplidas();
							int planificadas = est.getGuardiasPlanificadas();
							if (cumplidas < minCumplidas
									|| (cumplidas == minCumplidas && planificadas < minPlanificadas)) {
								seleccionado = est;
								minCumplidas = cumplidas;
								minPlanificadas = planificadas;
							}
						}
					}
					if (seleccionado != null) {
						TipoGuardia tipo = esFestivo ? TipoGuardia.FESTIVO : TipoGuardia.NORMAL;
						Horario h = new Horario(fecha, LocalTime.of(8, 0), LocalTime.of(20, 0));
						crearGuardia(tipo, seleccionado, h);
						guardiasAgregadas++;
					}
				}
				if (!finSemanaMujer && !trabajadores.isEmpty()) {
					if (!ocupadoTrab1) {
						Trabajador seleccionado = null;
						int minCumplidas = Integer.MAX_VALUE;
						int minPlanificadas = Integer.MAX_VALUE;
						for (Trabajador t : trabajadores) {
							Horario h = new Horario(fecha, LocalTime.of(9, 0), LocalTime.of(14, 0));
							if (t.puedeHacerGuardia(h)) {
								int cumplidas = t.getGuardiasCumplidas();
								int planificadas = t.getGuardiasPlanificadas();
								if (cumplidas < minCumplidas
										|| (cumplidas == minCumplidas && planificadas < minPlanificadas)) {
									seleccionado = t;
									minCumplidas = cumplidas;
									minPlanificadas = planificadas;
								}
							}
						}
						if (seleccionado != null) {
							TipoGuardia tipo = esFestivo ? TipoGuardia.FESTIVO : TipoGuardia.NORMAL;
							Horario h = new Horario(fecha, LocalTime.of(9, 0), LocalTime.of(14, 0));
							crearGuardia(tipo, seleccionado, h);
							guardiasAgregadas++;
						}
					}
					if (!ocupadoTrab2) {
						Trabajador seleccionado = null;
						int minCumplidas = Integer.MAX_VALUE;
						int minPlanificadas = Integer.MAX_VALUE;
						for (Trabajador t : trabajadores) {
							Horario h = new Horario(fecha, LocalTime.of(14, 0), LocalTime.of(19, 0));
							if (t.puedeHacerGuardia(h)) {
								int cumplidas = t.getGuardiasCumplidas();
								int planificadas = t.getGuardiasPlanificadas();
								if (cumplidas < minCumplidas
										|| (cumplidas == minCumplidas && planificadas < minPlanificadas)) {
									seleccionado = t;
									minCumplidas = cumplidas;
									minPlanificadas = planificadas;
								}
							}
						}
						if (seleccionado != null) {
							TipoGuardia tipo = esFestivo ? TipoGuardia.FESTIVO : TipoGuardia.NORMAL;
							Horario h = new Horario(fecha, LocalTime.of(14, 0), LocalTime.of(19, 0));
							crearGuardia(tipo, seleccionado, h);
							guardiasAgregadas++;
						}
					}
				}
				if (dow == 7) {
					finSemanaMujer = !finSemanaMujer;
					finSemanaMujerActual = finSemanaMujer;
				}
			}
		}
		// Al finalizar, actualizar el último mes planificado solo si es posterior
		if (ultimoMesPlanificado == null || mesActual.isAfter(ultimoMesPlanificado)) {
			ultimoMesPlanificado = mesActual;
		}
		return guardiasAgregadas;
	}

}
