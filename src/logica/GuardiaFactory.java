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
        // Para trabajadores podrías añadir lógica similar si es necesario
        return true;
    }

    // BUSCAR POR ID (compatible)
    public Guardia buscarGuardiaPorId(int id) {
        for (Guardia guardia : guardias) {
            if (guardia.getId() == id) {
                return guardia;
            }
        }
        return null;
    }

    // ACTUALIZAR GUARDIA
    public boolean actualizarGuardia(int id, TipoGuardia nuevoTipo, Persona nuevaPersona, Horario nuevoHorario) {
        Guardia guardia = buscarGuardiaPorId(id);
        if (guardia == null) return false;
        
        // Validación básica
        if (!nuevaPersona.puedeHacerGuardia(nuevoHorario)) {
            return false;
        }
        
        // Actualizar
        guardia.setTipo(nuevoTipo);
        guardia.setPersona(nuevaPersona);
        guardia.setHorario(nuevoHorario);
        
        return true;
    }

    // ELIMINAR GUARDIA MEJORADO
    public boolean eliminarGuardia(int id) {
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
                
                // Contador específico para estudiantes
                if (p instanceof Estudiante && g.getTipo() == TipoGuardia.NORMAL) {
                    ((Estudiante)p).setGuardiasAsignadas(((Estudiante)p).getGuardiasAsignadas() - 1);
                }
                
                it.remove();
                return true;
            }
        }
        return false;
    }
}


























/* La basura de Daniela
	//Atributos
	private final Calendario calendario;
	private final List<Guardia> guardias = new ArrayList<>();
	private static int contadorId = 1;

	//Constructor
	public GuardiaFactory(Calendario calendario) {
		if (calendario == null) {
			throw new IllegalArgumentException("El calendario no puede ser nulo");
		}
		this.calendario = calendario;
		this.guardias = new ArrayList<>();
	}

	//Metodos
	public Guardia crearGuardia(Persona persona, Horario horario, LocalDate fecha, 
			boolean esRecuperacion, String motivo) {
		Guardia guardia = null;
		boolean parametrosValidos = persona != null && horario != null && 
				fecha != null && motivo != null;

		if (parametrosValidos) {
			boolean noEsFestivo = !calendario.esFestivo(fecha);
			boolean puedeHacerGuardia = persona.puedeHacerGuardia(fecha, horario);

			if (noEsFestivo && puedeHacerGuardia) {
				guardia = new Guardia(contadorId++, fecha, persona, horario, esRecuperacion, motivo);
				guardias.add(guardia);
			}
		}
		return guardia;
	}

	public Guardia crearGuardiaRecuperacion(Persona persona, String motivo) {
		LocalDate fecha = LocalDate.now();
		Horario horario = new Horario(Dia.MONDAY, "08:00 AM", "04:00 PM", "Diurno");
		return crearGuardia(persona, horario, fecha, true, motivo);
	}

	public Guardia consultarGuardia(int id) {
		Guardia resultado = null;
		for (Guardia guardia : guardias) {
			if (guardia.getId() == id) {
				resultado = guardia;
			}
		}
		return resultado;
	}
	      wtf contigo q te pasa?
    public List<Guardia> consultarGuardiasPorPersona(int idPersona) {
        List<Guardia> resultado = new ArrayList<>();
        for (Guardia guardia : guardias) {
            if (guardia.getPersona().getId() == idPersona) {
                resultado.add(guardia);
            }
        }
        return resultado;
    }

	public boolean actualizarGuardia(int id, Horario nuevoHorario, LocalDate nuevaFecha) {
		boolean actualizado = false;
		Guardia guardia = consultarGuardia(id);

		if (guardia != null) {
			guardia.setHorario(nuevoHorario);
			guardia.setFecha(nuevaFecha);
			actualizado = true;
		}

		return actualizado;
	}

	public boolean eliminarGuardia(int id) {
		boolean eliminado = false;
		Guardia guardia = consultarGuardia(id);

		if (guardia != null) {
			eliminado = guardias.remove(guardia);
		}

		return eliminado;
	}

}
 */