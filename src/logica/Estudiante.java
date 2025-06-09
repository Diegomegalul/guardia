package logica;

import utiles.Sexo;

public class Estudiante extends Persona{
	//Atributos
	private boolean licenciaMatricula;
	private boolean baja;
	private int grupo; // Nuevo atributo grupo

	//Constructor
	public Estudiante (String ci, String nombre,String apellidos, Sexo sexo, boolean activo, int cantidadGuardias, int cantidadGuardiasFestivo, int grupo){
		super(ci, nombre, apellidos, sexo, activo, cantidadGuardias, cantidadGuardiasFestivo);
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
	public boolean puedeHacerGuardia(Horario horario) {
		boolean puede = false;
		if(getActivo()){
			int mes = horario.getDia().getMonthValue();
			if(mes != 7 || mes != 8){
				puede = true;
			}
		}
		return puede;
	}


}
