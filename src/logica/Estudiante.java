package logica;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import utiles.Sexo;

public class Estudiante extends Persona{
	//Atributos
	private boolean licenciaMatricula;
	private boolean baja;
	private int grupo; // Nuevo atributo grupo
	private int guardiasCumplidas;  //Nuevo atributo guardiasCumplidas

	//Constructor
	public Estudiante (String ci, String nombre,String apellidos, Sexo sexo, boolean activo, int guardiasAsignadas , int cantidadGuardiasFestivo, int grupo){
		super(ci, nombre, apellidos, sexo, activo, guardiasAsignadas, cantidadGuardiasFestivo);
		setGrupo(grupo);
		this.guardiasAsignadas = 0;
        this.guardiasCumplidas = 0;
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
	    // Validaciones básicas
	    if (!getActivo() || horario == null) return false;
	    
	    // Meses vacacionales
	    int mes = horario.getDia().getMonthValue();
	    if (mes == 7 || mes == 8) return false;
	    
	    // Variables para validación
	    LocalTime inicio = horario.getHoraInicio();
	    LocalTime fin = horario.getHoraFin();
	    DayOfWeek dia = horario.getDia().getDayOfWeek();
	    
	    // Validación por sexo
	    switch (getSexo()) {
	        case MASCULINO:
	            return esHorarioNocturnoValido(inicio, fin);
	            
	        case FEMENINO:
	            return esFinDeSemana(dia) && 
	                   esHorarioDiurnoValido(inicio, fin);
	            
	        default:
	            return false;
	    }
	}

	private boolean esHorarioNocturnoValido(LocalTime inicio, LocalTime fin) {
	    return inicio.equals(LocalTime.of(20, 0)) && 
	           fin.equals(LocalTime.of(8, 0));
	}

	private boolean esHorarioDiurnoValido(LocalTime inicio, LocalTime fin) {
	    return inicio.equals(LocalTime.of(8, 0)) && 
	           fin.equals(LocalTime.of(20, 0));
	}

	private boolean esFinDeSemana(DayOfWeek dia) {
	    return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
	}

    public void incrementarGuardiasAsignadas() {
        this.guardiasAsignadas++;
    }
    
    public void registrarGuardiaCumplida() {
        this.guardiasCumplidas++;
    }
    
    public int calcularGuardiasPendientes() {
        return guardiasAsignadas - guardiasCumplidas;
    }
    
    public boolean tieneGuardiasPendientes() {
        return calcularGuardiasPendientes() > 0;
    }
    

}
