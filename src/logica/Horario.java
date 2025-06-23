package logica;

import java.time.LocalDate;
import java.time.LocalTime;

public class Horario {
	//Atributos
	private LocalDate dia;
	private LocalTime horaInicio;
	private LocalTime horaFin;

	//Constructor
	public Horario(LocalDate dia,LocalTime horaInicio,LocalTime horaFin){
		setDia(dia);
		setHoraInicio(horaInicio);
		setHoraFin(horaFin);
	}

	//Setters y Getters
	public void setDia(LocalDate dia){
		this.dia = dia;
	}

	public void setHoraInicio(LocalTime horaInicio){
		this.horaInicio = horaInicio;
	}

	public void setHoraFin(LocalTime horaFin){
		this.horaFin = horaFin;
	}

	public LocalDate getDia(){
		return dia;
	}

	public LocalTime getHoraInicio(){
		return horaInicio;
	}

	public LocalTime getHoraFin(){
		return horaFin;
	}

	// No hay métodos con dos returns en caminos alternativos ni uso de break fuera de switch.
}