package logica;

import java.time.LocalDate;
import utiles.Sexo;

public class Trabajador extends Persona {
    private LocalDate fechaDeIncorporacion;
   // private boolean voluntarioVacaciones; Para q es esta wea?
    //Constructor
    public Trabajador(String ci,String nombre,Sexo sexo,boolean activo,LocalDate fechaDeIncorporacion,int cantidadGuardias,int cantidadGuardiasFestivo){
        super(ci, nombre, sexo, activo, cantidadGuardias,cantidadGuardiasFestivo); 
        setFechaDeIncorporacion(fechaDeIncorporacion);
    }
    //Getters y setters
    
    public void setFechaDeIncorporacion(LocalDate fechaDeIncorporacion) {
		this.fechaDeIncorporacion = fechaDeIncorporacion;		
	}
    
    public LocalDate getFechaDeIncorporacion(){
    	return fechaDeIncorporacion;
    }

	//Metodos
	@Override
	public boolean puedeHacerGuardia(LocalDate fecha, Horario horario) {
		// TODO Auto-generated method stub
		return false;
	}
   
}