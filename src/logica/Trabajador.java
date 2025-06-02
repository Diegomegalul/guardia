package logica;

import java.time.LocalDate;

import utiles.Sexo;

public class Trabajador extends Persona{
    //Atributos
    private LocalDate fechaDeIncorporacion;
    private boolean voluntarioVacaciones;
    //Constructor
    public Trabajador(String ci,String nombre,Sexo sexo,boolean activo,LocalDate fechaDeIncorporacion,int cantidadGuardias,int grupo){
        super(ci, nombre, sexo, activo, cantidadGuardias, 0); // cantidadGuardiasFestivo no aplica
        setFechaDeIncorporacion(fechaDeIncorporacion);
        // No llamar a setGrupo(grupo) aquí, grupo solo aplica a Estudiante
    }
    //Getters y setters
    public void setFechaDeIncorporacion(LocalDate fechaDeIncorporacion){
        this.fechaDeIncorporacion = fechaDeIncorporacion;
    }

    public LocalDate getFechaDeIncorporacion(){
        return fechaDeIncorporacion;
    }

    public boolean esVoluntarioVacaciones() {
        return voluntarioVacaciones;
    }

    public void setVoluntarioVacaciones(boolean voluntarioVacaciones) {
        this.voluntarioVacaciones = voluntarioVacaciones;
    }

    //Metodos
    public boolean puedeHacerGuardia(LocalDate fecha) {
        return activo && (fechaDeIncorporacion == null || !fecha.isBefore(fechaDeIncorporacion));
    }
}
