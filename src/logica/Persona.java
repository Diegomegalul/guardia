package logica;

import utiles.Sexo;

public abstract class Persona {
    //Atributos
    protected int cantidadGuardias;
    protected String ci;
    protected String nombre;
    protected String apellidos;
    protected Sexo sexo; 
    protected boolean activo;
    protected int cantidadGuardiasFestivo;
    //Contructor
    protected Persona(String ci,String nombre,Sexo sexo,boolean activo,int cantidadGuardias,int cantidadGuardiasFestivo){
        setCi(ci);
        setNombre(nombre);
        setSexo(sexo);
        setActivo(activo);
        setCantidadGuardias(cantidadGuardias);
    }
    //Getters y setters
    public void setCi(String ci){
        this.ci = ci;
    }

    public String getCi(){
        return ci;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setSexo(Sexo sexo){
        this.sexo = sexo;
    }

    public Sexo getSexo(){
        return sexo;
    }

    public void setActivo(boolean activo){
        this.activo = activo;
    }

    public boolean getActivo(){
        return activo;
    }
    
    public void setCantidadGuardias(int cantidadGuardias){
        this.cantidadGuardias = cantidadGuardias;
    }

    public int getCantidadGuardias(){
        return cantidadGuardias;
    }

    public boolean estaActivo() {
        return activo;
    }
    
    public void setCantidadGuardiasFestivo(int cantidadGuardiasFestivo) {
        this.cantidadGuardiasFestivo = cantidadGuardiasFestivo;
    }

    public int getCantidadGuardiasFestivo(){
        return cantidadGuardiasFestivo;
    }
    //Metodos
}
