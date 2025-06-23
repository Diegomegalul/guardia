package logica;
import utiles.TipoGuardia;

public class Guardia {
	//Atributos
	private int id;
	private TipoGuardia tipo;
	private Persona persona;
	private Horario horario;
	private boolean cumplida; // Nuevo atributo

	//Constructor
	public Guardia(int id, TipoGuardia tipo, Persona persona, Horario horario) {
		setId(id);
		setTipo(tipo);
		setPersona(persona);
		setHorario(horario);
		this.cumplida = false;
	}

	// Getters y Setters
	public int getId() { 
		return id; 
	}

	public void setId(int id){
		this.id = id;
	}
	public TipoGuardia getTipo() {
		return tipo;
	}

	public void setTipo(TipoGuardia tipo) {
		this.tipo = tipo;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public boolean getCumplida() {
		return cumplida;
	}

	public void setCumplida(boolean cumplida) {
		this.cumplida = cumplida;
	}

	//Metodos
	// No hay dobles returns aquí
}