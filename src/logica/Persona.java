package logica;

import utiles.Sexo;

public abstract class Persona {
	// Atributos
	protected int guardiasPlanificadas;
	protected String ci;
	protected String nombre;
	protected String apellidos;
	protected Sexo sexo;
	protected boolean activo;
	protected int cantidadGuardiasFestivo;
	protected int guardiasCumplidas;

	// Contructor
	protected Persona(String ci, String nombre, String apellidos, Sexo sexo, boolean activo, int guardiasPlanificadas,
			int cantidadGuardiasFestivo, int guardiasCumplidas) {
		setCi(ci);
		setNombre(nombre);
		setApellidos(apellidos);
		setSexo(sexo);
		setActivo(activo);
		setGuardiasPlanificadas(guardiasPlanificadas);
		setCantidadGuardiasFestivo(cantidadGuardiasFestivo);
		setGuardiasCumplidas(guardiasCumplidas);
	}

	// Getters y Setters
	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		// Validar que el CI tenga exactamente 11 dígitos y sean todos números
		if (ci == null || ci.length() != 11 || !ci.matches("\\d{11}")) {
			throw new IllegalArgumentException("El CI debe tener exactamente 11 dígitos numéricos.");
		}
		this.ci = ci;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		// Validar que el nombre solo contenga letras y espacios
		if (nombre == null || !nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
			throw new IllegalArgumentException("El nombre solo debe contener letras y espacios.");
		}
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		// Validar que los apellidos solo contengan letras y espacios
		if (apellidos == null || !apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
			throw new IllegalArgumentException("Los apellidos solo deben contener letras y espacios.");
		}
		this.apellidos = apellidos;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public void setGuardiasPlanificadas(int guardiasPlanificadas) {
		this.guardiasPlanificadas = guardiasPlanificadas;
	}

	public int getGuardiasPlanificadas() {
		return guardiasPlanificadas;
	}

	public boolean getActivo() {
		return activo;
	}

	public void setCantidadGuardiasFestivo(int cantidadGuardiasFestivo) {
		this.cantidadGuardiasFestivo = cantidadGuardiasFestivo;
	}

	public int getCantidadGuardiasFestivo() {
		return cantidadGuardiasFestivo;
	}

	public void setGuardiasCumplidas(int guardiasCumplidas) {
		this.guardiasCumplidas = guardiasCumplidas;
	}

	public int getGuardiasCumplidas() {
		return guardiasCumplidas;
	}

	// Metodos
	public abstract boolean puedeHacerGuardia(Horario horario);
	// No hay métodos con dos returns en caminos alternativos ni uso de break fuera
	// de switch.
}
