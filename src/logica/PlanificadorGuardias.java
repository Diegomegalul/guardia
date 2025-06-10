package logica;

import java.util.*;
import utiles.TipoGuardia;

public class PlanificadorGuardias {
	//Atributos
	private final Facultad facultad;
	private final Calendario calendario;
	private final GuardiaFactory guardiaFactory;
	private static PlanificadorGuardias instancia;//para el Singleton :v
	//Constructor
	private PlanificadorGuardias() {
        this.facultad = new Facultad("Informatica"); 
        this.calendario = new Calendario();  
        this.guardiaFactory = new GuardiaFactory();  
    }
	//Setters y Getters
	public Facultad getFacultad() {
        return facultad;
    }
    
    public Calendario getCalendario() {
        return calendario;
    }
    
    public GuardiaFactory getGuardiaFactory() {
        return guardiaFactory;
    }
	//Metodos
    //M�todo para obtener la instancia Singleton (OJO)
    public static synchronized PlanificadorGuardias getInstancia() {
        if (instancia == null) {
            instancia = new PlanificadorGuardias();
        }
        return instancia;
    }

	/**
	 * Reporte de grupos con más guardias de recuperación.
	 * @return Lista ordenada de grupos y personas con guardias de recuperación.
	 */
	public List<GrupoRecuperacionReporte> reporteGuardiasRecuperacionPorGrupo() {
		Map<Integer, Map<Persona, Integer>> grupoPersonasRec = new HashMap<Integer, Map<Persona, Integer>>();

		for (Guardia g : guardiaFactory.getGuardias()) {
			if (g.getTipo() == TipoGuardia.RECUPERACION && g.getPersona() instanceof Estudiante) {
				Estudiante est = (Estudiante) g.getPersona();
				int grupo = est.getGrupo();
				if (!grupoPersonasRec.containsKey(grupo)) {
					grupoPersonasRec.put(grupo, new HashMap<Persona, Integer>());
				}
				Map<Persona, Integer> personas = grupoPersonasRec.get(grupo);
				Integer cantidad = personas.get(est);
				if (cantidad == null) {
					cantidad = 0;
				}
				personas.put(est, cantidad + 1);
			}
		}

		// Crear lista de reportes por grupo
		List<GrupoRecuperacionReporte> reporte = new ArrayList<GrupoRecuperacionReporte>();
		for (Map.Entry<Integer, Map<Persona, Integer>> entry : grupoPersonasRec.entrySet()) {
			int grupo = entry.getKey();
			Map<Persona, Integer> personas = entry.getValue();

			// Ordenar personas por cantidad de guardias de recuperación (desc)
			List<PersonaRecuperacion> personasOrdenadas = new ArrayList<PersonaRecuperacion>();
			for (Map.Entry<Persona, Integer> pe : personas.entrySet()) {
				personasOrdenadas.add(new PersonaRecuperacion(pe.getKey(), pe.getValue()));
			}
			Collections.sort(personasOrdenadas, new Comparator<PersonaRecuperacion>() {
				public int compare(PersonaRecuperacion a, PersonaRecuperacion b) {
					return Integer.compare(b.cantidad, a.cantidad);
				}
			});

			// int totalGrupo = personas.values().stream().mapToInt(Integer::intValue).sum();
			int totalGrupo = 0;
			for (Integer val : personas.values()) {
				totalGrupo += val;
			}
			reporte.add(new GrupoRecuperacionReporte(grupo, totalGrupo, personasOrdenadas));
		}

		// Ordenar grupos por total de guardias de recuperación (desc)
		Collections.sort(reporte, new Comparator<GrupoRecuperacionReporte>() {
			public int compare(GrupoRecuperacionReporte a, GrupoRecuperacionReporte b) {
				return Integer.compare(b.totalGuardias, a.totalGuardias);
			}
		});
		return reporte;
	}

	// Clases internas para el reporte
	public static class GrupoRecuperacionReporte {
		public int grupo;
		public int totalGuardias;
		public List<PersonaRecuperacion> personas;

		public GrupoRecuperacionReporte(int grupo, int totalGuardias, List<PersonaRecuperacion> personas) {
			this.grupo = grupo;
			this.totalGuardias = totalGuardias;
			this.personas = personas;
		}
	}

	public static class PersonaRecuperacion {
		public Persona persona;
		public int cantidad;

		public PersonaRecuperacion(Persona persona, int cantidad) {
			this.persona = persona;
			this.cantidad = cantidad;
		}
	}
}
