package logica;

import java.time.LocalDate;
import java.util.ArrayList;

public class GuardiaFactory {
	private ArrayList<Guardia>guardias;
	private Calendario calendario;
	
	public GuardiaFactory(Calendario calendario) {
		this.calendario = calendario;
	    this.guardias= new ArrayList<>();
	}
	
	//Operaciones CRUD para las guardias
	public Guardia crearGuardias(Persona persona, Horario horario, LocalDate fecha){
	
	

}
