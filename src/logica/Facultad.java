package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logica.Persona;

public class Facultad {
	//Atributos
	private final List<Persona>personas = new ArrayList<>();
	private String nombre;
	//Constructor
	public Facultad(String nombre){
		setNombre(nombre);
	}
	//Getters y setters
	public List<Persona> getPersonas(){
		return Collections.unmodifiableList(personas);
	}
	//Metodos
	public void agregarPersona(Persona persona){
		personas.add(persona);
	}

	public void eliminarPersona(Persona persona){
		personas.remove(persona);
	} 

	public Persona buscarPersonaPorCI(String ci) {
		for (Persona p : personas) {
			if (p.getCi().equals(ci)) {
				return p;
			}
		}
		return null;
	}

	public boolean actualizarPersona(String ci, Persona nuevaPersona) {
		for (int i = 0; i < personas.size(); i++) {
			if (personas.get(i).getCi().equals(ci)) {
				personas.set(i, nuevaPersona);
				return true;
			}
		}
		return false;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}