package logica;

import java.time.LocalDate;
import utiles.Sexo;

public class Trabajador extends Persona {
    private LocalDate fechaDeIncorporacion;
    private boolean voluntario;
    //Constructor
    public Trabajador(String ci,String nombre,String apellidos,Sexo sexo,boolean activo,LocalDate fechaDeIncorporacion,int cantidadGuardias,int cantidadGuardiasFestivo,boolean voluntario){
        super(ci, nombre,apellidos, sexo, activo, cantidadGuardias,cantidadGuardiasFestivo); 
        setFechaDeIncorporacion(fechaDeIncorporacion);
        setVoluntario(voluntario);
    }
    //Getters y setters
    
    public void setFechaDeIncorporacion(LocalDate fechaDeIncorporacion) {
		this.fechaDeIncorporacion = fechaDeIncorporacion;		
	}
    
    public LocalDate getFechaDeIncorporacion(){
    	return fechaDeIncorporacion;
    }

	public boolean getVoluntario() {
		return voluntario;
	}

	public void setVoluntario(boolean voluntario) {
		this.voluntario = voluntario;
	}

	//Metodos
	@Override
	public boolean puedeHacerGuardia(Horario horario) {
		return false;
	}
   
}