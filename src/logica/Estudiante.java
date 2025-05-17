package logica;

import utiles.Sexo;

public class Estudiante extends Persona{
    //Atributos
    private int cantidadGuardiasFestivo;
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
    //Metodos

}
