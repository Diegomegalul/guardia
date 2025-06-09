package logica;

import java.util.ArrayList;
import java.util.List;

public class GuardiaFactory {
	//Atributos
	private Horario horario;
	private Calendario calendario;
	private List<Guardia> guardias = new ArrayList<>();

	//Constructor
	public GuardiaFactory(){
		
	}
	
	//Setters y Getters
	public List<Guardia> getGuardias() {
		return guardias;
	}

	public void setGuardias(List<Guardia> guardias) {
		this.guardias = guardias;
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
	//Metodos







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