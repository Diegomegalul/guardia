package logica;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
				if(getSexo() == Sexo.MASCULINO){
					if(horario.getHoraInicio().equals(LocalTime.of(20, 00))){
						if(horario.getHoraFin().equals(LocalTime.of(8, 00))){
							puede = true;
						}
					}	
				}else{
					if(getSexo() == Sexo.FEMENINO){
						if(horario.getDia().getDayOfWeek() == DayOfWeek.SATURDAY||horario.getDia().getDayOfWeek() == DayOfWeek.SUNDAY){
							if(horario.getHoraInicio().equals(LocalTime.of(8, 00))){
								if(horario.getHoraFin().equals(LocalTime.of(20, 00))){
									puede = true;
								}
							}
						}
					}	
				}
			}
		}
		return puede;
	}


}
