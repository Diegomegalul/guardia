package logica;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import logica.DiaFestivo;
import utiles.Sexo;

public class PlanificadorGuardias {
	private Facultad facultad = new Facultad();
	private ArrayList<Guardia> guardias;
	private ArrayList<DiaFestivo> diasFestivos = new ArrayList<>();

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

	public ArrayList<Guardia> getGuardias() {
		return guardias;
	}

	public void setGuardias(ArrayList<Guardia> guardias) {
		this.guardias = guardias;
	}

	public ArrayList<DiaFestivo> getDiasFestivos() {
		return diasFestivos;
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
        } else {
            // Mujeres: solo fines de semana, 8:00 a 20:00
            return esFinDeSemana(horario.getDia()) &&
                horario.getHoraInicio().equals(java.time.LocalTime.of(8,0)) &&
                horario.getHoraFin().equals(java.time.LocalTime.of(20,0));
        }
    }
    // Validación para trabajadores
    if (persona instanceof Trabajador) {
        Trabajador trab = (Trabajador) persona;
        if (!trab.puedeHacerGuardia(horario.getFecha())) return false;
        if (esVacaciones(horario.getFecha())) {
            // Solo voluntarios en vacaciones
            if (!trab.esVoluntarioVacaciones()) return false;
        }
        // Fines de semana, turnos de 9-14 y 14-19
        if (esFinDeSemana(horario.getDia())) {
            return (horario.getHoraInicio().equals(java.time.LocalTime.of(9,0)) && horario.getHoraFin().equals(java.time.LocalTime.of(14,0)))
                || (horario.getHoraInicio().equals(java.time.LocalTime.of(14,0)) && horario.getHoraFin().equals(java.time.LocalTime.of(19,0)));
        }
        return false;
    }
    return false;
}

    private boolean esFinDeSemana(utiles.Dia dia) {
        return dia == utiles.Dia.SATURDAY || dia == utiles.Dia.SUNDAY;
    }

    private boolean esVacaciones(java.time.LocalDate fecha) {
        // Implementa tu lógica para determinar si la fecha es de vacaciones
        return false;
    }

    public void agregarDiaFestivo(DiaFestivo diaFestivo) {
        // Evita duplicados por fecha
        for (DiaFestivo df : diasFestivos) {
            if (df.getFecha().equals(diaFestivo.getFecha())) {
                return;
            }
        }
        diasFestivos.add(diaFestivo);
    }

    public void eliminarDiaFestivo(LocalDate fecha) {
        for (int i = 0; i < diasFestivos.size(); i++) {
            DiaFestivo df = diasFestivos.get(i);
            if (df.getFecha().equals(fecha)) {
                diasFestivos.remove(i);
                i--; // Para manejar múltiples días con la misma fecha, si existieran
            }
        }
    }

    public boolean esDiaFestivo(LocalDate fecha) {
        for (DiaFestivo df : diasFestivos) {
            if (df.getFecha().equals(fecha)) {
                return true;
            }
        }
        return false;
    }

	// CRUD de guardias

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
