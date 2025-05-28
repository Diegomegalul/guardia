package logica;

import java.time.LocalDate;
import java.time.LocalTime;

import utiles.Dia;

public class Horario {
    //Atributos
    private Dia dia;
    private LocalDate fecha;
    private LocalTime horaFin;
    private LocalTime horaInicio; 
    private boolean esFestivo; 
    //Constructor
    public Horario(utiles.Dia dia, java.time.LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, boolean esFestivo) {
        this.dia = dia;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.esFestivo = esFestivo;
    }
    //Getters y setters
    public void setDia(Dia dia){
        this.dia = dia;
    }

    public Dia getDia(){
        return dia;
    }

    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }

    public LocalDate getFecha(){
        return fecha;
    }

    public void setEsFestivo(boolean esFestivo){
        this.esFestivo = esFestivo;
    }

    public boolean getEsFestivo(){
        return esFestivo;
    }

    public void setHoraInicio(LocalTime horaInicio){
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraInicio(){
        return horaInicio;
    }

    public void setHoraFin(LocalTime horaFin){
        this.horaFin = horaFin;
    }

    public LocalTime getHoraFin(){
        return horaFin;
    }
    //Metodos
}
