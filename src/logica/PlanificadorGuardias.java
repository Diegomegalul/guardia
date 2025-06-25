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
	// Atributos
	private final Facultad facultad;
	private final Calendario calendario;
	private final GuardiaFactory guardiaFactory;
	private static PlanificadorGuardias instancia;// para el Singleton :v
	// Constructor

	private PlanificadorGuardias() {
		this.facultad = new Facultad("Informatica");
		this.calendario = Calendario.getInstancia(); // Usar singleton
		this.guardiaFactory = new GuardiaFactory();
		// Enlazar el calendario singleton al guardiaFactory porque no queria pinchar
		this.guardiaFactory.setCalendario(this.calendario);
	}

	// Setters y Getters
	public Facultad getFacultad() {
		return facultad;
	}

	public Calendario getCalendario() {
		return calendario;
	}

	public GuardiaFactory getGuardiaFactory() {
		return guardiaFactory;
	}

	// Metodos
	// Metodo para obtener la instancia Singleton (OJO)
	public static synchronized PlanificadorGuardias getInstancia() {
		if (instancia == null) {
			instancia = new PlanificadorGuardias();
		}
		return instancia;
	}

	/**
	 * Reporte de grupos con más guardias de recuperación.
	 * 
	 * return Lista ordenada de grupos y personas con guardias de recuperación.
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

			// Ordenar personas por cantidad de guardias de recuperación 
			List<PersonaRecuperacion> personasOrdenadas = new ArrayList<PersonaRecuperacion>();
			for (Map.Entry<Persona, Integer> pe : personas.entrySet()) {
				personasOrdenadas.add(new PersonaRecuperacion(pe.getKey(), pe.getValue()));
			}
			Collections.sort(personasOrdenadas, new Comparator<PersonaRecuperacion>() {
				public int compare(PersonaRecuperacion a, PersonaRecuperacion b) {
					return Integer.compare(b.cantidad, a.cantidad);
				}
			});
			int totalGrupo = 0;
			for (Integer val : personas.values()) {
				totalGrupo += val;
			}
			reporte.add(new GrupoRecuperacionReporte(grupo, totalGrupo, personasOrdenadas));
		}

		Collections.sort(reporte, new Comparator<GrupoRecuperacionReporte>() {
			public int compare(GrupoRecuperacionReporte a, GrupoRecuperacionReporte b) {
				return Integer.compare(b.totalGuardias, a.totalGuardias);
			}
		});
		return reporte;
	}

	/**
	 * Devuelve una lista de grupos ordenados de mayor a menor según la suma de
	 * guardias de recuperación.
	 * Dentro de cada grupo, los estudiantes están ordenados de mayor a menor
	 * guardias de recuperación.
	 * 
	 * @return Lista de objetos GrupoRecuperacionOrdenado con grupo y lista de
	 *         estudiantes ordenados
	 */
	public List<GrupoRecuperacionOrdenado> estudiantesPorGrupoRecuperacionDesc() {
		Map<Integer, List<Estudiante>> mapa = new HashMap<>();
		for (Persona p : facultad.getPersonas()) {
			if (p instanceof Estudiante) {
				Estudiante e = (Estudiante) p;
				int grupo = e.getGrupo();
				if (!mapa.containsKey(grupo)) {
					mapa.put(grupo, new ArrayList<Estudiante>());
				}
				mapa.get(grupo).add(e);
			}
		}
		List<GrupoRecuperacionOrdenado> resultado = new ArrayList<GrupoRecuperacionOrdenado>();
		for (Map.Entry<Integer, List<Estudiante>> entry : mapa.entrySet()) {
			List<Estudiante> estudiantes = entry.getValue();
			// Ordenar estudiantes dentro del grupo por guardias incumplidas 
			Collections.sort(estudiantes, new Comparator<Estudiante>() {
				public int compare(Estudiante a, Estudiante b) {
					return Integer.compare(b.getGuardiasIncumplidas(), a.getGuardiasIncumplidas());
				}
			});
			int suma = 0;
			for (Estudiante est : estudiantes) {
				suma += est.getGuardiasIncumplidas();
			}
			resultado.add(new GrupoRecuperacionOrdenado(entry.getKey(), suma, estudiantes));
		}
		// Ordenar lista de grupos por suma de guardias de recuperación 
		Collections.sort(resultado, new Comparator<GrupoRecuperacionOrdenado>() {
			public int compare(GrupoRecuperacionOrdenado a, GrupoRecuperacionOrdenado b) {
				return Integer.compare(b.totalRecuperacion, a.totalRecuperacion);
			}
		});
		return resultado;
	}

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

	public static class GrupoRecuperacionOrdenado {
		public int grupo;
		public int totalRecuperacion;
		public List<Estudiante> estudiantes;

		public GrupoRecuperacionOrdenado(int grupo, int totalRecuperacion, List<Estudiante> estudiantes) {
			this.grupo = grupo;
			this.totalRecuperacion = totalRecuperacion;
			this.estudiantes = estudiantes;
		}
	}

	// Métodos de prueba para añadir personas
	private boolean profesoresPruebaAgregados = false;
	private boolean estudiantesVaronesPruebaAgregados = false;
	private boolean estudiantesMujeresPruebaAgregados = false;

	public void agregarProfesoresPrueba() {
		if (profesoresPruebaAgregados)
			return;
		profesoresPruebaAgregados = true;
		facultad.agregarPersona(new Trabajador("12345678901", "Carlos", "Pérez Gómez", utiles.Sexo.MASCULINO, false,
				java.time.LocalDate.of(2025, 1, 1), 0, 0, 0, true, null));
		facultad.agregarPersona(new Trabajador("23456789012", "María", "López Fernández", utiles.Sexo.FEMENINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 7, 10)));
		facultad.agregarPersona(new Trabajador("34567890123", "Juan", "Martínez Ruiz", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 8, 5)));
		facultad.agregarPersona(new Trabajador("45678901234", "Ana", "García Torres", utiles.Sexo.FEMENINO, true, null,
				0, 0, 0, true, java.time.LocalDate.of(2025, 7, 20)));
		facultad.agregarPersona(new Trabajador("56789012345", "Luis", "Sánchez Díaz", utiles.Sexo.MASCULINO, true, null,
				0, 0, 0, true, java.time.LocalDate.of(2025, 8, 15)));
		facultad.agregarPersona(new Trabajador("67890123456", "Elena", "Romero Castro", utiles.Sexo.FEMENINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 7, 25)));
		facultad.agregarPersona(new Trabajador("78901234567", "Miguel", "Vega Herrera", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 8, 2)));
		facultad.agregarPersona(new Trabajador("89012345678", "Lucía", "Jiménez Morales", utiles.Sexo.FEMENINO, false,
				java.time.LocalDate.of(2025, 4, 12), 0, 0, 0, false, null));
		facultad.agregarPersona(new Trabajador("90123456789", "Pedro", "Navarro Ramos", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 7, 30)));
		facultad.agregarPersona(new Trabajador("11223344556", "Sofía", "Molina Pérez", utiles.Sexo.FEMENINO, true, null,
				0, 0, 0, true, java.time.LocalDate.of(2025, 8, 8)));
		facultad.agregarPersona(new Trabajador("22334455667", "Javier", "Castro López", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 7, 18)));
		facultad.agregarPersona(new Trabajador("33445566778", "Patricia", "Ortega Gil", utiles.Sexo.FEMENINO, true,
				null, 0, 0, 0, false, null));
		facultad.agregarPersona(new Trabajador("44556677889", "Raúl", "Domínguez León", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 8, 12)));
		facultad.agregarPersona(new Trabajador("55667788990", "Isabel", "Serrano Cruz", utiles.Sexo.FEMENINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 7, 26)));
		facultad.agregarPersona(new Trabajador("66778899001", "Alberto", "Morales Peña", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 8, 20)));
		facultad.agregarPersona(new Trabajador("77889900112", "Teresa", "Ramos Soto", utiles.Sexo.FEMENINO, false,
				java.time.LocalDate.of(2025, 9, 23), 0, 0, 0, true, null));
		facultad.agregarPersona(new Trabajador("88990011223", "Francisco", "Cano Vargas", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 7, 14)));
		facultad.agregarPersona(new Trabajador("99001122334", "Marta", "Reyes Flores", utiles.Sexo.FEMENINO, true, null,
				0, 0, 0, true, java.time.LocalDate.of(2025, 8, 3)));
		facultad.agregarPersona(new Trabajador("10111213141", "Andrés", "Suárez Bravo", utiles.Sexo.MASCULINO, true,
				null, 0, 0, 0, true, java.time.LocalDate.of(2025, 7, 22)));
		facultad.agregarPersona(new Trabajador("12131415161", "Beatriz", "Medina Pardo", utiles.Sexo.FEMENINO, true,
				null, 0, 0, 0, false, null));
	}

	public void agregarEstudiantesVaronesPrueba() {
		if (estudiantesVaronesPruebaAgregados)
			return;
		estudiantesVaronesPruebaAgregados = true;
		facultad.agregarPersona(
				new Estudiante("05090266950", "Diego", "Canales Calvo", utiles.Sexo.MASCULINO, true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(new Estudiante("04032448951", "Aleksander", "Castañeda Morales", utiles.Sexo.MASCULINO,
				true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Aniel", "Varela  Hernández", utiles.Sexo.MASCULINO, true,
				0, 0, 12, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Javier Angel", "Soto Villanueva", utiles.Sexo.MASCULINO,
				true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05090266900", "Dariel", "Velazco Falcón", utiles.Sexo.MASCULINO, true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Marcos Alejandro", "Carrasco Pérez",
				utiles.Sexo.MASCULINO, true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Javier Omar", "Cepero Carreño", utiles.Sexo.MASCULINO,
				true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05090266900", "Evian", "Diaz Vázquez", utiles.Sexo.MASCULINO, true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05090266900", "Diego", "González León", utiles.Sexo.MASCULINO, true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Herbert Luis", "Navarro Valdés", utiles.Sexo.MASCULINO,
				true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Gabriel", "Rodríguez Delgado", utiles.Sexo.MASCULINO,
				true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Darell", "Perdomo Gonzalez", utiles.Sexo.MASCULINO, true,
				0, 0, 13, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Leonardo David", "Guirado Garcia", utiles.Sexo.MASCULINO,
				false, 0, 0, 13, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Jhonathan", "Salgado Torres", utiles.Sexo.MASCULINO,
				true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05090266900", "Haisel", "Reino  Bello", utiles.Sexo.MASCULINO, true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05090266900", "Frank", "Delgado Durán", utiles.Sexo.MASCULINO, true, 0, 0, 14, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Chayanne", "Suárez Villches", utiles.Sexo.MASCULINO,
				false, 0, 0, 14, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Ernesto Javier", "Diego Rodriguez",
				utiles.Sexo.MASCULINO, true, 0, 0, 14, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "David", "Mayeta Martinez", utiles.Sexo.MASCULINO, false,
				0, 0, 14, 0, 0));
		facultad.agregarPersona(new Estudiante("05090266900", "Eduardo", "Ortega Alfonso", utiles.Sexo.MASCULINO, true,
				0, 0, 14, 0, 0));
	}

	public void agregarEstudiantesMujeresPrueba() {
		if (estudiantesMujeresPruebaAgregados)
			return;
		estudiantesMujeresPruebaAgregados = true;
		facultad.agregarPersona(new Estudiante("06100948233", "Daniela", "Rodríguez Molina", utiles.Sexo.FEMENINO, true,
				0, 0, 12, 0, 0));
		facultad.agregarPersona(
				new Estudiante("06030867876", "Gloria", "Santos Rosado", utiles.Sexo.FEMENINO, true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(
				new Estudiante("06061368391", "Aylin", "Vázquez Alvarez", utiles.Sexo.FEMENINO, true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(new Estudiante("06061368091", "Naraisa de la Caridad", "Pardo Flores",
				utiles.Sexo.FEMENINO, true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(new Estudiante("06061368091", "Ledydiana", "Gómez Velázquez", utiles.Sexo.FEMENINO,
				true, 0, 0, 12, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05032112157", "Nicole", "Ríos Rodríguez", utiles.Sexo.FEMENINO, true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "María Karla", "Martínez Pozo", utiles.Sexo.FEMENINO,
				true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Jennifer", "Ramírez Hernández", utiles.Sexo.FEMENINO,
				true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Aranza María", "Arias Matías", utiles.Sexo.FEMENINO,
				true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(
				new Estudiante("06052915155", "Sarahi", "Perez Mendoza", utiles.Sexo.FEMENINO, true, 0, 0, 13, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Jade Bárbara", "Córdova González", utiles.Sexo.FEMENINO,
				true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "María Fernanda", "Llopiz Fabelo", utiles.Sexo.FEMENINO,
				true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Jennifer de la Caridad", "Rodríguez Barzola",
				utiles.Sexo.FEMENINO, true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Claudia", "Salazar Martínez", utiles.Sexo.FEMENINO, true,
				0, 0, 11, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05032112157", "Claudette", "Caro Perez", utiles.Sexo.FEMENINO, true, 0, 0, 11, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05032112157", "Alison", "Hidalgo  Guerra", utiles.Sexo.FEMENINO, true, 0, 0, 14, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Gabriela", "Castillo Frias", utiles.Sexo.FEMENINO, true,
				0, 0, 14, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Yeilin Dignora", "De la Cruz Noriega",
				utiles.Sexo.FEMENINO, true, 0, 0, 14, 0, 0));
		facultad.agregarPersona(new Estudiante("05032112157", "Daniela", "Quintero Delfino", utiles.Sexo.FEMENINO, true,
				0, 0, 14, 0, 0));
		facultad.agregarPersona(
				new Estudiante("05032112157", "Patricia", "Tomé Romero", utiles.Sexo.FEMENINO, true, 0, 0, 14, 0, 0));
	}

	// REPORTES
	// Profesores voluntarios en vacaciones
	public List<Trabajador> reporteProfesoresVoluntariosEnVacaciones() {
		List<Trabajador> voluntarios = new ArrayList<Trabajador>();
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
						if (!voluntarios.contains(trabajador)) {
							voluntarios.add(trabajador);
						}
					}
				}
			}
		}

		return voluntarios;
	}

	// Listado de gurdias en dias festivos
	public List<Guardia> listaGuardiasEnDiasFestivos() {
		List<Guardia> resultado = new ArrayList<>();
		// Guardias planificadas
		for (Guardia guardia : guardiaFactory.getGuardias()) {
			utiles.TipoGuardia tipo = guardia.getTipo();
			if ((tipo == utiles.TipoGuardia.FESTIVO || tipo == utiles.TipoGuardia.VOLUNTARIA_FESTIVO || tipo == utiles.TipoGuardia.RECUPERACION_FESTIVO)
				&& calendario.existeDiaFestivo(guardia.getHorario().getDia())) {
				resultado.add(guardia);
			}
		}
		// Guardias cumplidas
		for (Guardia guardia : guardiaFactory.getGuardiasCumplidas()) {
			utiles.TipoGuardia tipo = guardia.getTipo();
			if ((tipo == utiles.TipoGuardia.FESTIVO || tipo == utiles.TipoGuardia.VOLUNTARIA_FESTIVO || tipo == utiles.TipoGuardia.RECUPERACION_FESTIVO)
				&& calendario.existeDiaFestivo(guardia.getHorario().getDia())) {
				resultado.add(guardia);
			}
		}
		// Guardias incumplidas
		for (Guardia guardia : guardiaFactory.getGuardiasIncumplidas()) {
			utiles.TipoGuardia tipo = guardia.getTipo();
			if ((tipo == utiles.TipoGuardia.FESTIVO || tipo == utiles.TipoGuardia.VOLUNTARIA_FESTIVO || tipo == utiles.TipoGuardia.RECUPERACION_FESTIVO)
				&& calendario.existeDiaFestivo(guardia.getHorario().getDia())) {
				resultado.add(guardia);
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

	/**
	 * Reporte de estudiantes inactivos agrupados por grupo (ordenados de mayor a
	 * menor cantidad de inactivos)
	 * y lista de trabajadores inactivos.
	 */
	public ReporteInactivos reportePersonasInactivas() {
		// Estudiantes inactivos por grupo
		Map<Integer, List<Estudiante>> mapaInactivos = new HashMap<>();
		// Trabajadores inactivos
		List<Trabajador> trabajadoresInactivos = new ArrayList<>();

		for (Persona p : facultad.getPersonas()) {
			if (!p.getActivo()) {
				if (p instanceof Estudiante) {
					Estudiante e = (Estudiante) p;
					int grupo = e.getGrupo();
					if (!mapaInactivos.containsKey(grupo)) {
						mapaInactivos.put(grupo, new ArrayList<Estudiante>());
					}
					mapaInactivos.get(grupo).add(e);
				} else if (p instanceof Trabajador) {
					trabajadoresInactivos.add((Trabajador) p);
				}
			}
		}

		// Crear lista de grupos ordenados por cantidad de inactivos (desc)
		List<GrupoInactivos> grupos = new ArrayList<GrupoInactivos>();
		for (Map.Entry<Integer, List<Estudiante>> entry : mapaInactivos.entrySet()) {
			List<Estudiante> estudiantes = entry.getValue();
			grupos.add(new GrupoInactivos(entry.getKey(), estudiantes.size(), estudiantes));
		}
		Collections.sort(grupos, new Comparator<GrupoInactivos>() {
			public int compare(GrupoInactivos a, GrupoInactivos b) {
				return Integer.compare(b.cantidadInactivos, a.cantidadInactivos);
			}
		});

		ReporteInactivos reporte = new ReporteInactivos(grupos, trabajadoresInactivos);
		return reporte;
	}

	public static class ReporteInactivos {
		public List<GrupoInactivos> gruposEstudiantesInactivos;
		public List<Trabajador> trabajadoresInactivos;

		public ReporteInactivos(List<GrupoInactivos> gruposEstudiantesInactivos,
				List<Trabajador> trabajadoresInactivos) {
			this.gruposEstudiantesInactivos = gruposEstudiantesInactivos;
			this.trabajadoresInactivos = trabajadoresInactivos;
		}
	}

	public static class GrupoInactivos {
		public int grupo;
		public int cantidadInactivos;
		public List<Estudiante> estudiantesInactivos;

		public GrupoInactivos(int grupo, int cantidadInactivos, List<Estudiante> estudiantesInactivos) {
			this.grupo = grupo;
			this.cantidadInactivos = cantidadInactivos;
			this.estudiantesInactivos = estudiantesInactivos;
		}
	}

	// :)
	// Buscar estudiantes por filtro (nombre, apellidos, ci, grupo)
	public List<Estudiante> buscarEstudiantes(String filtro) {
		List<Estudiante> resultado = new ArrayList<>();
		List<Persona> personas = getFacultad().getPersonas();
		for (Persona p : personas) {
			if (p instanceof Estudiante) {
				Estudiante e = (Estudiante) p;
				if (filtro == null || filtro.isEmpty()) {
					resultado.add(e);
				} else {
					String ci = e.getCi().toLowerCase();
					String nombre = e.getNombre().toLowerCase();
					String apellidos = e.getApellidos().toLowerCase();
					String grupoStr = String.valueOf(e.getGrupo());
					filtro = filtro.toLowerCase();
					if (ci.contains(filtro) || nombre.contains(filtro) || apellidos.contains(filtro)
							|| grupoStr.contains(filtro)) {
						resultado.add(e);
					}
				}
			}
		}
		return resultado;
	}

	// Buscar trabajadores por filtro (nombre, apellidos, ci)
	public List<Trabajador> buscarTrabajadores(String filtro) {
		List<Trabajador> resultado = new ArrayList<>();
		List<Persona> personas = getFacultad().getPersonas();
		for (Persona p : personas) {
			if (p instanceof Trabajador) {
				Trabajador t = (Trabajador) p;
				if (filtro == null || filtro.isEmpty()) {
					resultado.add(t);
				} else {
					String ci = t.getCi().toLowerCase();
					String nombre = t.getNombre().toLowerCase();
					String apellidos = t.getApellidos().toLowerCase();
					filtro = filtro.toLowerCase();
					if (ci.contains(filtro) || nombre.contains(filtro) || apellidos.contains(filtro)) {
						resultado.add(t);
					}
				}
			}
		}
		return resultado;
	}

}
