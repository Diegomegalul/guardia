package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logica.Persona;

public class Facultad {
	//Atributos
	private final List<Persona>personas = new ArrayList<>();

	//Constructor
	public Facultad(){

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
}