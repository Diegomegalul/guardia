package logica;
import java.time.LocalDate;
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
	public void crearPersona(String ci, String nombre, Sexo sexo, boolean activo, int cantidadGuardias, int cantidadGuardiasFestivo) {
    Persona nuevaPersona = new Estudiante(ci, nombre, sexo, activo, cantidadGuardias, cantidadGuardiasFestivo);
    agregarPersona(nuevaPersona);
	}

	// Sobrecarga para Trabajador
	public void crearPersona(String ci, String nombre, Sexo sexo, boolean activo, java.time.LocalDate fechaDeIncorporacion, int cantidadGuardias) {
	    Persona nuevaPersona = new Trabajador(ci, nombre, sexo, activo, fechaDeIncorporacion, cantidadGuardias);
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
}
