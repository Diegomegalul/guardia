package logica;

import java.time.LocalDate;
import utiles.Sexo;

public class Estudiante extends Persona{
    //Atributos
    private boolean licenciaMatricula;
    private boolean baja;
    private int grupo; // Nuevo atributo grupo

    //Constructor
    public Estudiante (String ci, String nombre, Sexo sexo, boolean activo, int cantidadGuardias, int cantidadGuardiasFestivo, int grupo){
        super(ci, nombre, sexo, activo, cantidadGuardias, cantidadGuardiasFestivo);
        setGrupo(grupo);
    }
    //Getters y setters
    public boolean isLicenciaMatricula() {
        return licenciaMatricula;
    }

    public void setLicenciaMatricula(boolean licenciaMatricula) {
        this.licenciaMatricula = licenciaMatricula;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public int getGrupo() {
        return grupo;
    }  
    //Metodos
	@Override
	public boolean puedeHacerGuardia(LocalDate fecha, Horario horario) {
		// TODO Auto-generated method stub
		return false;
	}

 
}
   