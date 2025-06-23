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

	/**
	 * Devuelve una lista de grupos ordenados de mayor a menor según la suma de guardias de recuperación.
	 * Dentro de cada grupo, los estudiantes están ordenados de mayor a menor guardias de recuperación.
	 * @return Lista de objetos GrupoRecuperacionOrdenado con grupo y lista de estudiantes ordenados
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
			// Ordenar estudiantes dentro del grupo por guardias incumplidas (desc)
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
		// Ordenar lista de grupos por suma de guardias de recuperación (desc)
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
	public void agregarProfesoresPrueba() {
			Trabajador a = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador b = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador c = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador d = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador e = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador f = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador g = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador h = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador i = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador j = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador k = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador l = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador m = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador n = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador o = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador p = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador q = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador r = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador s = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			Trabajador t = new Trabajador("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,java.time.LocalDate.of(2020, 1, 1),0,0,true);
			facultad.agregarPersona(a);
			facultad.agregarPersona(b);
			facultad.agregarPersona(c);
			facultad.agregarPersona(d);
			facultad.agregarPersona(e);
			facultad.agregarPersona(f);
			facultad.agregarPersona(g);
			facultad.agregarPersona(h);
			facultad.agregarPersona(i);
			facultad.agregarPersona(j);
			facultad.agregarPersona(k);
			facultad.agregarPersona(l);
			facultad.agregarPersona(m);
			facultad.agregarPersona(n);
			facultad.agregarPersona(o);
			facultad.agregarPersona(p);
			facultad.agregarPersona(q);
			facultad.agregarPersona(r);
			facultad.agregarPersona(s);
			facultad.agregarPersona(t);
	}

	public void agregarEstudiantesVaronesPrueba() {
		Estudiante a = new Estudiante("05090266950","Diego","Canales Calvo",utiles.Sexo.MASCULINO,true,0,0,12, 0, 0);
		Estudiante b = new Estudiante("04032448951","Aleksander","Castañeda Morales",utiles.Sexo.MASCULINO,true,0,0,12, 0, 0);
		Estudiante c = new Estudiante("05090266900","Aniel","Varela  Hernández",utiles.Sexo.MASCULINO,true,0,0,12, 0, 0);
		Estudiante d = new Estudiante("05090266900","Javier Angel","Soto Villanueva",utiles.Sexo.MASCULINO,true,0,0,12, 0, 0);	
		Estudiante e = new Estudiante("05090266900","Dariel","Velazco Falcón",utiles.Sexo.MASCULINO,true,0,0,12, 0, 0);
		Estudiante f = new Estudiante("05090266900","Marcos Alejandro","Carrasco Pérez",utiles.Sexo.MASCULINO,true,0,0,11, 0, 0);
		Estudiante g = new Estudiante("05090266900","Javier Omar","Cepero Carreño",utiles.Sexo.MASCULINO,true,0,0,11, 0, 0);
		Estudiante h = new Estudiante("05090266900","Evian","Diaz Vázquez",utiles.Sexo.MASCULINO,true,0,0,11, 0, 0);
		Estudiante i = new Estudiante("05090266900","Diego","González León",utiles.Sexo.MASCULINO,true,0,0,11, 0, 0);
		Estudiante j = new Estudiante("05090266900","Herbert Luis","Navarro Valdés",utiles.Sexo.MASCULINO,true,0,0,11, 0, 0);	
		Estudiante k = new Estudiante("05032112157","Gabriel","Rodríguez Delgado",utiles.Sexo.MASCULINO,true,0,0,13, 0, 0);
		Estudiante l = new Estudiante("05090266900","Darell","Perdomo Gonzalez",utiles.Sexo.MASCULINO,true,0,0,13, 0, 0);
		Estudiante m = new Estudiante("05090266900","Leonardo David","Guirado Garcia",utiles.Sexo.MASCULINO,true,0,0,13, 0, 0);
		Estudiante n = new Estudiante("05090266900","Jhonathan","Salgado Torres",utiles.Sexo.MASCULINO,true,0,0,13, 0, 0);
		Estudiante o = new Estudiante("05090266900","Haisel","Reino  Bello",utiles.Sexo.MASCULINO,true,0,0,13, 0, 0);
		Estudiante q = new Estudiante("05090266900","Frank","Delgado Durán",utiles.Sexo.MASCULINO,true,0,0,14, 0, 0);
		Estudiante r = new Estudiante("05090266900","Chayanne","Suárez Villches",utiles.Sexo.MASCULINO,true,0,0,14, 0, 0);
		Estudiante s = new Estudiante("05090266900","Ernesto Javier","Diego Rodriguez",utiles.Sexo.MASCULINO,true,0,0,14, 0, 0);
		Estudiante t = new Estudiante("05090266900","David","Mayeta Martinez",utiles.Sexo.MASCULINO,true,0,0,14, 0, 0);
		Estudiante p = new Estudiante("05090266900","Eduardo","Ortega Alfonso",utiles.Sexo.MASCULINO,true,0,0,14, 0, 0);
		facultad.agregarPersona(a);
		facultad.agregarPersona(b);
		facultad.agregarPersona(c);
		facultad.agregarPersona(d);
		facultad.agregarPersona(e);
		facultad.agregarPersona(f);
		facultad.agregarPersona(g);
		facultad.agregarPersona(h);
		facultad.agregarPersona(i);
		facultad.agregarPersona(j);
		facultad.agregarPersona(k);
		facultad.agregarPersona(l);
		facultad.agregarPersona(m);
		facultad.agregarPersona(n);
		facultad.agregarPersona(o);
		facultad.agregarPersona(p);
		facultad.agregarPersona(q);
		facultad.agregarPersona(r);
		facultad.agregarPersona(s);
		facultad.agregarPersona(t);
	}

	public void agregarEstudiantesMujeresPrueba() {
		Estudiante a = new Estudiante("06100948233","Daniela","Rodríguez Molina",utiles.Sexo.FEMENINO,true,0,0,12,0,0);
		Estudiante b = new Estudiante("06030867876","Gloria","Santos Rosado",utiles.Sexo.FEMENINO,true,0,0,12,0,0);
		Estudiante c = new Estudiante("06061368391","Aylin","Vázquez Alvarez",utiles.Sexo.FEMENINO,true,0,0,12,0,0);
		Estudiante d = new Estudiante("06061368091","Naraisa de la Caridad","Pardo Flores",utiles.Sexo.FEMENINO,true,0,0,12,0,0);
		Estudiante e = new Estudiante("06061368091","Ledydiana","Gómez Velázquez",utiles.Sexo.FEMENINO,true,0,0,12,0,0);
		Estudiante f = new Estudiante("05032112157","Nicole","Ríos Rodríguez",utiles.Sexo.FEMENINO,true,0,0,13,0,0);
		Estudiante g = new Estudiante("05032112157","María Karla","Martínez Pozo",utiles.Sexo.FEMENINO,true,0,0,13,0,0);
		Estudiante h = new Estudiante("05032112157","Jennifer","Ramírez Hernández",utiles.Sexo.FEMENINO,true,0,0,13,0,0);
		Estudiante i = new Estudiante("05032112157","Aranza María","Arias Matías",utiles.Sexo.FEMENINO,true,0,0,13,0,0);
		Estudiante j = new Estudiante("06052915155","Sarahi","Perez Mendoza",utiles.Sexo.FEMENINO,true,0,0,13,0,0);
		Estudiante k = new Estudiante("05032112157","Jade Bárbara","Córdova González",utiles.Sexo.FEMENINO,true,0,0,11,0,0);
		Estudiante l = new Estudiante("05032112157","María Fernanda","Llopiz Fabelo",utiles.Sexo.FEMENINO,true,0,0,11,0,0);
		Estudiante m = new Estudiante("05032112157","Jennifer de la Caridad","Rodríguez Barzola",utiles.Sexo.FEMENINO,true,0,0,11,0,0);
		Estudiante n = new Estudiante("05032112157","Claudia","Salazar Martínez",utiles.Sexo.FEMENINO,true,0,0,11,0,0);
		Estudiante o = new Estudiante("05032112157","Claudette","Caro Perez",utiles.Sexo.FEMENINO,true,0,0,11,0,0);
		Estudiante p = new Estudiante("05032112157","Alison","Hidalgo  Guerra",utiles.Sexo.FEMENINO,true,0,0,14,0,0);
		Estudiante q = new Estudiante("05032112157","Gabriela","Castillo Frias",utiles.Sexo.FEMENINO,true,0,0,14,0,0);
		Estudiante r = new Estudiante("05032112157","Yeilin Dignora","De la Cruz Noriega",utiles.Sexo.FEMENINO,true,0,0,14,0,0);
		Estudiante s = new Estudiante("05032112157","Daniela","Quintero Delfino",utiles.Sexo.FEMENINO,true,0,0,14,0,0);
		Estudiante t = new Estudiante("05032112157","Patricia","Tomé Romero",utiles.Sexo.FEMENINO,true,0,0,14,0,0);
		facultad.agregarPersona(a);
		facultad.agregarPersona(b);
		facultad.agregarPersona(c);
		facultad.agregarPersona(d);
		facultad.agregarPersona(e);
		facultad.agregarPersona(f);
		facultad.agregarPersona(g);
		facultad.agregarPersona(h);
		facultad.agregarPersona(i);
		facultad.agregarPersona(j);
		facultad.agregarPersona(k);
		facultad.agregarPersona(l);
		facultad.agregarPersona(m);
		facultad.agregarPersona(n);
		facultad.agregarPersona(o);
		facultad.agregarPersona(p);
		facultad.agregarPersona(q);
		facultad.agregarPersona(r);
		facultad.agregarPersona(s);
		facultad.agregarPersona(t);
	}
	//REPORTES
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

	/**
	 * Reporte de estudiantes inactivos agrupados por grupo (ordenados de mayor a menor cantidad de inactivos)
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

		return new ReporteInactivos(grupos, trabajadoresInactivos);
	}

	public static class ReporteInactivos {
		public List<GrupoInactivos> gruposEstudiantesInactivos;
		public List<Trabajador> trabajadoresInactivos;

		public ReporteInactivos(List<GrupoInactivos> gruposEstudiantesInactivos, List<Trabajador> trabajadoresInactivos) {
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
	public List<Trabajador> buscarTrabajadores(String filtro) {
		List<Trabajador> resultado = new ArrayList<>();
		for (Persona p : facultad.getPersonas()) {
			if (p instanceof Trabajador) {
				Trabajador t = (Trabajador) p;
				boolean coincide = false;
				if (filtro == null) {
					coincide = true;
				} else {
					String ci = t.getCi().toLowerCase();
					String nombre = t.getNombre().toLowerCase();
					String apellidos = t.getApellidos().toLowerCase();
					String fecha = t.getFechaDeIncorporacion() != null ? t.getFechaDeIncorporacion().toString() : "";
					String voluntario = t.getVoluntario() ? "sí" : "no";
					if (ci.contains(filtro) || nombre.contains(filtro) || apellidos.contains(filtro) || fecha.contains(filtro) || voluntario.contains(filtro)) {
						coincide = true;
					}
				}
				if (coincide) {
					resultado.add(t);
				}
			}
		}
		return resultado;
	}
}

