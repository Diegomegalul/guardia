package logica;

import java.time.DayOfWeek;
import java.time.LocalTime;

import utiles.Sexo;

public class Estudiante extends Persona {
	// Atributos
	private boolean licenciaMatricula;
	private boolean baja;
	private int grupo; // Nuevo atributo grupo
	private int guardiasRecuperacion; // Nuevo atributo guardias de recuperacion
	private int guardiasIncumplidas; // Nuevo atributo guardias incumplidas

	// Constructor
	public Estudiante(String ci, String nombre, String apellidos, Sexo sexo, boolean activo, int guardiasAsignadas,
			int cantidadGuardiasFestivo, int grupo, int guardiasCumplidas, int guardiasRecuperacion) {
		super(ci, nombre, apellidos, sexo, activo, guardiasAsignadas, cantidadGuardiasFestivo, guardiasCumplidas);
		setGrupo(grupo);
		setGuardiasRecuperacion(guardiasRecuperacion);
		this.guardiasIncumplidas = 0;
	}

	// Getters y setters
	public void setGuardiasRecuperacion(int guardiasRecuperacion) {
		this.guardiasRecuperacion = guardiasRecuperacion;
	}

	public int getGuardiasRecuperacion() {
		return guardiasRecuperacion;
	}

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

	public void setGuardiasIncumplidas(int guardiasIncumplidas) {
		this.guardiasIncumplidas = guardiasIncumplidas;
	}

	public int getGuardiasIncumplidas() {
		return guardiasIncumplidas;
	}

	// Metodos
	@Override
	public boolean puedeHacerGuardia(Horario horario) {
		boolean puede = false;
		if (getActivo() && horario != null) {
			int mes = horario.getDia().getMonthValue();
			if (mes != 7 && mes != 8) {
				LocalTime inicio = horario.getHoraInicio();
				LocalTime fin = horario.getHoraFin();
				DayOfWeek dia = horario.getDia().getDayOfWeek();
				switch (getSexo()) {
					case MASCULINO:
						puede = esHorarioNocturnoValido(inicio, fin);
						break;
					case FEMENINO:
						puede = esFinDeSemana(dia) && esHorarioDiurnoValido(inicio, fin);
						break;
					default:
						puede = false;
				}
			}
		}
		return puede;
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
	// No hay métodos con dos returns en caminos alternativos ni uso de break fuera
	// de switch
}
