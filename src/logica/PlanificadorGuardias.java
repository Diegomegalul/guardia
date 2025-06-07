package logica;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import logica.DiaFestivo;
import utiles.Sexo;
import java.util.Set;

public class PlanificadorGuardias {
    private final Calendario calendario;
    private final GuardiaFactory guardiaFactory;
    private final Facultad facultad;


	public PlanificadorGuardias() {
		this.guardias = new ArrayList<Guardia>();
		this.diasFestivos = new ArrayList<DiaFestivo>();
		// Días festivos nacionales de Cuba (puedes agregar más si lo deseas)
		int year = java.time.LocalDate.now().getYear();
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 1, 1), "Triunfo de la Revolución"));
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 1, 2), "Victoria de la Revolución"));
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 5, 1), "Día Internacional de los Trabajadores"));
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 7, 25), "Día de la Rebeldía Nacional"));
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 7, 26), "Asalto al Cuartel Moncada"));
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 7, 27), "Conmemoración del 26 de Julio"));
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 10, 10), "Inicio de las Guerras de Independencia"));
		diasFestivos.add(new DiaFestivo(java.time.LocalDate.of(year, 12, 25), "Navidad"));
		// Puedes agregar más días festivos relevantes para Cuba aquí
	}

    /**
     * Planifica guardias para todos los d�as del mes actual
     * que no sean festivos, asignando turnos seg�n el d�a de la semana
     */
    public void planificarMes() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
        
        List<Trabajador> trabajadoresDisponibles = obtenerTrabajadoresDisponibles();
        LocalDate fechaActual = inicioMes;
        
        while (!fechaActual.isAfter(finMes)) {
            if (!calendario.esFestivo(fechaActual)) {
                planificarDia(fechaActual, trabajadoresDisponibles);
            }
            fechaActual = fechaActual.plusDays(1);
        }
    }

    private List<Trabajador> obtenerTrabajadoresDisponibles() {
        List<Trabajador> disponibles = new ArrayList<>();
        for (Persona persona : facultad.getPersonas()) {
            if (persona instanceof Trabajador && persona.isActivo()) {
                disponibles.add((Trabajador) persona);
            }
        }
        return disponibles;
    }

    private void planificarDia(LocalDate fecha, List<Trabajador> trabajadores) {
        int diaSemana = fecha.getDayOfWeek().getValue();
        boolean esFinDeSemana = diaSemana >= 6;
        Horario horario = crearHorarioParaDia(diaSemana, esFinDeSemana);
        
        for (Trabajador trabajador : trabajadores) {
            if (puedeAsignarGuardia(trabajador, fecha, horario)) {
                asignarGuardia(trabajador, fecha, horario);
            }
        }
    }

	public void setDiasFestivos(ArrayList<DiaFestivo> diasFestivos) {
		this.diasFestivos = diasFestivos;
	}

	// Métodos para acceder a personas a través de Facultad
	public List<Persona> getPersonas() {
		return facultad.getPersonas();
	}

	public void agregarPersona(Persona persona) {
		facultad.agregarPersona(persona);
	}

	public void eliminarPersona(String ci) {
		facultad.eliminarPersona(ci);
	}

	public Persona buscarPersonaPorCi(String ci) {
		return facultad.buscarPersonaPorCi(ci);
	}

	public void actualizarPersona(Persona personaActualizada) {
		facultad.actualizarPersona(personaActualizada);
	}

	// Métodos para crear personas específicos
	public void crearPersona(String ci, String nombre, Sexo sexo, boolean activo, int cantidadGuardias, int cantidadGuardiasFestivo, int grupo) {
    Persona nuevaPersona = new Estudiante(ci, nombre, sexo, activo, cantidadGuardias, cantidadGuardiasFestivo, grupo);
    agregarPersona(nuevaPersona);
	}

	// Sobrecarga para Trabajador
	public void crearPersona(String ci, String nombre, Sexo sexo, boolean activo, java.time.LocalDate fechaDeIncorporacion, int cantidadGuardias, int grupo) {
	    Persona nuevaPersona = new Trabajador(ci, nombre, sexo, activo, fechaDeIncorporacion, cantidadGuardias, grupo);
	    agregarPersona(nuevaPersona);
	}
	
	// Nuevo método para crear guardia
	public void crearGuardia(int id, Horario horario, Persona persona) {
	    Guardia nuevaGuardia = new Guardia(id, horario, persona);
	    guardias.add(nuevaGuardia);
	}
	
	public boolean puedeRealizarGuardia(Persona persona, Horario horario) {
    // Validación para estudiantes
    if (persona instanceof Estudiante) {
        Estudiante est = (Estudiante) persona;
        if (!est.puedeHacerGuardia()) return false;
        if (est.getSexo() != utiles.Sexo.MASCULINO && !esFinDeSemana(horario.getDia())) return false;
        if (est.getSexo() == utiles.Sexo.MASCULINO) {
            // Hombres: todos los días de 20:00 a 08:00
            return horario.getHoraInicio().equals(java.time.LocalTime.of(20,0)) && horario.getHoraFin().equals(java.time.LocalTime.of(8,0));

    private Horario crearHorarioParaDia(int diaSemana, boolean esFinDeSemana) {
        Dia dia = Dia.values()[diaSemana - 1];
        if (esFinDeSemana) {
            return new Horario(dia, "08:00 AM", "02:00 PM", "Diurno");
        } else {
            return new Horario(dia, "06:00 PM", "10:00 PM", "Nocturno");
        }
    }

    private boolean puedeAsignarGuardia(Trabajador trabajador, LocalDate fecha, Horario horario) {
        boolean noTieneGuardia = !tieneGuardiaEnFecha(trabajador, fecha);
        return noTieneGuardia && trabajador.puedeHacerGuardia(fecha, horario);
    }

    private boolean tieneGuardiaEnFecha(Persona persona, LocalDate fecha) {
        List<Guardia> guardias = guardiaFactory.consultarGuardiasPorPersona(persona.getId());
        boolean tieneGuardia = false;
        
        for (Guardia guardia : guardias) {
            if (guardia.getFecha().equals(fecha)) {
                tieneGuardia = true;
            }
        }
        
        return tieneGuardia;
    }

    private void asignarGuardia(Trabajador trabajador, LocalDate fecha, Horario horario) {
        String motivo = "Guardia programada para " + fecha.getDayOfWeek().toString();
        guardiaFactory.crearGuardia(trabajador, horario, fecha, false, motivo);
    }

    // Resto de m�todos existentes (asignarRecuperacion, CRUD d�as festivos, etc.)
    public boolean asignarRecuperacion(Persona persona, String motivo) {
        boolean asignada = false;
        
        if (persona != null && motivo != null && !motivo.trim().isEmpty()) {
            if (persona instanceof Trabajador && persona.isActivo()) {
                Guardia guardia = guardiaFactory.crearGuardiaRecuperacion(persona, motivo);
                asignada = guardia != null;
            }
        }
        
        return asignada;
    }

    public void crearDiasFestivos(LocalDate fecha) {
        if (fecha != null) {
            calendario.agregarFestivo(fecha);
        }
    }

    public Set<LocalDate> consultarDiasFestivos() {
        return calendario.getFestivos();
    }

    public boolean actualizarDiasFestivos(LocalDate fechaAntigua, LocalDate fechaNueva) {
        boolean actualizado = false;
        
        if (fechaAntigua != null && fechaNueva != null) {
            boolean eliminado = calendario.eliminarFestivo(fechaAntigua);
            if (eliminado) {
                calendario.agregarFestivo(fechaNueva);
                actualizado = true;
            }
        }
        
        return actualizado;
    }

	public void agregarGuardia(Guardia guardia) {
		guardias.add(guardia);
	}

	public Guardia buscarGuardiaPorId(int id) {
		for (Guardia g : guardias) {
			if (g.getId() == id) {
				return g;
			}
		}
		return null;
	}

	public void actualizarGuardia(Guardia guardiaActualizada) {
		for (int i = 0; i < guardias.size(); i++) {
			if (guardias.get(i).getId() == guardiaActualizada.getId()) {
				guardias.set(i, guardiaActualizada);
				break;
			}
		}
	}

	public void eliminarGuardia(int id) {
		for (int i = 0; i < guardias.size(); i++) {
			if (guardias.get(i).getId() == id) {
				guardias.remove(i);
				break;
			}
		}
	}

	public Facultad getFacultad() {
		return facultad;
	}

	public void setFacultad(Facultad facultad) {
		this.facultad = facultad;
	}

	public ArrayList<Guardia> planificarGuardiasAutomatico(YearMonth yearMonth) {
		ArrayList<Guardia> resultado = new ArrayList<Guardia>();
		List<Persona> personas = getPersonas();
		int id = 1;

		// Verifica si ya existen guardias para ese mes
		for (Guardia g : guardias) {
			if (g != null && g.getHorario() != null && g.getHorario().getFecha() != null) {
				LocalDate fecha = g.getHorario().getFecha();
				if (fecha.getYear() == yearMonth.getYear() && fecha.getMonthValue() == yearMonth.getMonthValue()) {
					resultado.add(g);
				}
			}
		}
		if (!resultado.isEmpty()) {
			return resultado;
		}

		// Separar listas por tipo y sexo
		ArrayList<Estudiante> estudiantesHombres = new ArrayList<Estudiante>();
		ArrayList<Estudiante> estudiantesMujeres = new ArrayList<Estudiante>();
		ArrayList<Trabajador> trabajadores = new ArrayList<Trabajador>();

		for (Persona p : personas) {
			if (p instanceof Estudiante) {
				Estudiante est = (Estudiante)p;
				if (est.getSexo() == Sexo.MASCULINO) {
					estudiantesHombres.add(est);
				} else {
					estudiantesMujeres.add(est);
				}
			} else if (p instanceof Trabajador) {
				trabajadores.add((Trabajador)p);
			}
		}

		// Copias para conteo temporal de guardias en el mes
		ArrayList<Estudiante> hombresTemp = new ArrayList<Estudiante>(estudiantesHombres);
		ArrayList<Estudiante> mujeresTemp = new ArrayList<Estudiante>(estudiantesMujeres);
		ArrayList<Trabajador> trabajadoresTemp = new ArrayList<Trabajador>(trabajadores);

		java.util.Map<Persona, Integer> guardiasMes = new java.util.HashMap<Persona, Integer>();
		java.util.Map<Persona, Integer> guardiasFestivoMes = new java.util.HashMap<Persona, Integer>();
		for (Persona p : personas) {
			guardiasMes.put(p, p.getCantidadGuardias());
			guardiasFestivoMes.put(p, p.getCantidadGuardiasFestivo());
		}

		boolean finDeSemanaTrabajadores = true;
		LocalDate inicio = yearMonth.atDay(1);
		LocalDate fin = yearMonth.atEndOfMonth();

		for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
			utiles.Dia dia = utiles.Dia.valueOf(fecha.getDayOfWeek().name());
			boolean esFestivo = esDiaFestivo(fecha);

			// Guardias estudiantes hombres: todos los días, 20:00 a 08:00 del siguiente día
			if (!hombresTemp.isEmpty()) {
				// Buscar el estudiante hombre con menor cantidad de guardias
				Estudiante est = hombresTemp.get(0);
				int min = guardiasMes.get(est);
				for (Estudiante e : hombresTemp) {
					if (guardiasMes.get(e) < min) {
						est = e;
						min = guardiasMes.get(e);
					}
				}
				Horario horario = new Horario(dia, fecha, java.time.LocalTime.of(20, 0), java.time.LocalTime.of(8, 0), esFestivo);
				Guardia guardia = new Guardia(id++, horario, est);
				resultado.add(guardia);
				guardias.add(guardia);
				// Aumentar cantidad de guardias temporal
				guardiasMes.put(est, guardiasMes.get(est) + 1);
				if (esFestivo) {
					guardiasFestivoMes.put(est, guardiasFestivoMes.get(est) + 1);
				}
			}

			// Fines de semana: alternancia entre mujeres y trabajadores
			if (dia == utiles.Dia.SATURDAY || dia == utiles.Dia.SUNDAY) {
				if (finDeSemanaTrabajadores) {
					for (int turno = 0; turno < 2; turno++) {
						if (!trabajadoresTemp.isEmpty()) {
							// Buscar el trabajador con menor cantidad de guardias
							Trabajador trab = trabajadoresTemp.get(0);
							int min = guardiasMes.get(trab);
							for (Trabajador t : trabajadoresTemp) {
								if (guardiasMes.get(t) < min) {
									trab = t;
									min = guardiasMes.get(t);
								}
							}
							java.time.LocalTime inicioTurno = (turno == 0) ? java.time.LocalTime.of(9, 0) : java.time.LocalTime.of(14, 0);
							java.time.LocalTime finTurno = (turno == 0) ? java.time.LocalTime.of(14, 0) : java.time.LocalTime.of(19, 0);
							Horario horario = new Horario(dia, fecha, inicioTurno, finTurno, esFestivo);
							Guardia guardia = new Guardia(id++, horario, trab);
							resultado.add(guardia);
							guardias.add(guardia);
							guardiasMes.put(trab, guardiasMes.get(trab) + 1);
							if (esFestivo) {
								guardiasFestivoMes.put(trab, guardiasFestivoMes.get(trab) + 1);
							}
						}
					}
				} else {
					if (!mujeresTemp.isEmpty()) {
						// Buscar la estudiante mujer con menor cantidad de guardias
						Estudiante est = mujeresTemp.get(0);
						int min = guardiasMes.get(est);
						for (Estudiante e : mujeresTemp) {
							if (guardiasMes.get(e) < min) {
								est = e;
								min = guardiasMes.get(e);
							}
						}
						Horario horario = new Horario(dia, fecha, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(20, 0), esFestivo);
						Guardia guardia = new Guardia(id++, horario, est);
						resultado.add(guardia);
						guardias.add(guardia);
						guardiasMes.put(est, guardiasMes.get(est) + 1);
						if (esFestivo) {
							guardiasFestivoMes.put(est, guardiasFestivoMes.get(est) + 1);
						}
					}
				}
				if (dia == utiles.Dia.SUNDAY) {
					finDeSemanaTrabajadores = !finDeSemanaTrabajadores;
				}
			}
		}

		// Al final, actualiza los contadores reales de las personas
		for (Persona p : personas) {
			p.setCantidadGuardias(guardiasMes.get(p));
			p.setCantidadGuardiasFestivo(guardiasFestivoMes.get(p));
		}
		return resultado;
	}
}

