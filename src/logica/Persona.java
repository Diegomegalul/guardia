package logica;
import java.time.LocalDate;
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
        setApellidos(apellidos);
        setSexo(sexo);
        setActivo(activo);
    }

    public abstract boolean puedeHacerGuardia(LocalDate fecha, Horario horario);

    // Getters y Setters
    public int getId() { return id; }
    
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID debe ser positivo");
        }
        this.id = id;
    }

    public String getNombre() { return nombre; }
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vac�o");
        }
        this.nombre = nombre.trim();
    }

    public String getApellidos() { return apellidos; }
    
    public void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellidos no puede estar vac�o");
        }
        this.apellidos = apellidos.trim();
    }

    public Sexo getSexo() { return sexo; }
    
    public void setSexo(Sexo sexo) {
        if (sexo == null) {
            throw new IllegalArgumentException("Sexo no puede ser nulo");
        }
        this.sexo = sexo;
    }

    public boolean isActivo() { return activo; }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format("%s %s (ID: %d)", nombre, apellidos, id);
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

