package logica;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	// Métodos de prueba para añadir personas
	public void agregarProfesoresPrueba() {
		for (int i = 1; i <= 10; i++) {
			Trabajador t = new Trabajador(
					"CI-PROF-" + i,
					"Profesor" + i,
					"Apellido" + i,
					utiles.Sexo.MASCULINO,
					true,
					java.time.LocalDate.of(2020, 1, 1),
					0,
					0,
					i % 2 == 0 // alterna voluntario
					);
			facultad.agregarPersona(t);
		}
	}

	public void agregarEstudiantesVaronesPrueba() {
		for (int i = 1; i <= 10; i++) {
			Estudiante e = new Estudiante(
					"CI-EST-M-" + i,
					"EstudianteM" + i,
					"Apellido" + i,
					utiles.Sexo.MASCULINO,
					true,
					0,
					0,
					1 // grupo
					);
			facultad.agregarPersona(e);
		}
	}

	public void agregarEstudiantesMujeresPrueba() {
		for (int i = 1; i <= 10; i++) {
			Estudiante e = new Estudiante(
					"CI-EST-F-" + i,
					"EstudianteF" + i,
					"Apellido" + i,
					utiles.Sexo.FEMENINO,
					true,
					0,
					0,
					1 // grupo
					);
			facultad.agregarPersona(e);
		}
	}
	//Profesores voluntarios en vacaciones
	public List<String> reporteProfesoresVoluntariosEnVacaciones() {
		List<String> voluntarios = new ArrayList<>();
		boolean tieneGuardiaEnVacaciones;

		for (Persona persona : facultad.getPersonas()) {
			if (persona instanceof Trabajador) {
				Trabajador trabajador = (Trabajador) persona;
				if (trabajador.getActivo() && trabajador.getVoluntario()) {

					tieneGuardiaEnVacaciones = false;
					Iterator<Guardia> it = guardiaFactory.getGuardias().iterator();

					while (it.hasNext() && !tieneGuardiaEnVacaciones) {
						Guardia guardia = it.next();
						if (guardia.getPersona().equals(trabajador)) {
							int mes = guardia.getHorario().getDia().getMonthValue();
							tieneGuardiaEnVacaciones = (mes == 7 || mes == 8);
						}
					}

					if (tieneGuardiaEnVacaciones) {
						String nombreCompleto = trabajador.getNombre() + " " + trabajador.getApellidos();
						if (!voluntarios.contains(nombreCompleto)) {
							voluntarios.add(nombreCompleto);
						}
					}
				}
			}
		}

		return voluntarios;
	}
	//Listado de gurdias en dias festivos
	public List<Guardia> listaGuardiasEnDiasFestivos() {
		List<Guardia> resultado = new ArrayList<>();

		for (Guardia guardia : guardiaFactory.getGuardias()) {
			if(guardia.getTipo() == TipoGuardia.FESTIVO) {
				if (calendario.existeDiaFestivo(guardia.getHorario().getDia())) {
					resultado.add(guardia);
				}
			}
		}

		return resultado;
	}

	/**
	 * Planifica guardias de recuperación para estudiantes con pendientes,
	 * en el mes y año indicados.
	 */
	public void planificarGuardiasRecuperacion(int anio, int mes) {
		guardiaFactory.planificarGuardiasRecuperacion(anio, mes);
	}
}

