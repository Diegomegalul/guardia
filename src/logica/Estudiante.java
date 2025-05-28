package logica;

import utiles.Sexo;

public class Estudiante extends Persona{
    //Atributos
    private int cantidadGuardiasFestivo;
    private boolean licenciaMatricula;
    private boolean baja;
    //Constructor
    public Estudiante (String ci,String nombre,Sexo sexo,boolean activo,int cantidadGuardias,int cantidadGuardiasFestivo){
        super(ci, nombre, sexo, activo, cantidadGuardias);
        setCantidadGuardiasFestivo(cantidadGuardiasFestivo);
    }
    //Getters y setters
    public void setCantidadGuardiasFestivo(int cantidadGuardiasFestivo) {
        this.cantidadGuardiasFestivo = cantidadGuardiasFestivo;
    }

    public int getCantidadGuardiasFestivo(){
        return cantidadGuardiasFestivo;
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
    //Metodos
    public boolean puedeHacerGuardia() {
        return activo && !licenciaMatricula && !baja;
    }
}
