package runner;

import interfaz.Cargando;

import java.awt.EventQueue;

import logica.Calendario;
import logica.Facultad;
import logica.GuardiaFactory;
import logica.Persona;
import logica.PlanificadorGuardias;

public class Main {
	public static void main(String[] args) {
		PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();
		
		Facultad facultad = planificador.getFacultad();
		Calendario calendario = planificador.getCalendario();
		GuardiaFactory factory = planificador.getGuardiaFactory();
		
		Persona persona = Persona();
		
		planificador.getFacultad().agregarPersona(persona);

	}
}









/*
EventQueue.invokeLater(new Runnable() {
public void run() {
    try {
        Cargando frame = new Cargando();
        frame.setVisible(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}); 
 */