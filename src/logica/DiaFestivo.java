package logica;

import java.time.LocalDate;

public class DiaFestivo {
	//Atributos
	private LocalDate fecha;
	private String descripcion;

	//Contructor
	public DiaFestivo(LocalDate fecha, String descripcion) {
		setFecha(fecha);
		setDescripcion(descripcion);
	}

	//Setters y Getters
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFechaString() {
		return fecha.toString();
	}

}
