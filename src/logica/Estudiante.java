package logica;

import java.time.LocalDate;
import utiles.Sexo;

public class Estudiante extends Persona{
    //Atributos
    private boolean licenciaMatricula;
    private boolean baja;
    private int grupo; // Nuevo atributo grupo

    //Constructor
    public Estudiante (String ci, String nombre, Sexo sexo, boolean activo, int cantidadGuardias, int cantidadGuardiasFestivo, int grupo){
        super(ci, nombre, sexo, activo, cantidadGuardias, cantidadGuardiasFestivo);
        setGrupo(grupo);
    }

    // Constructor compatible antiguo (por si hay código legacy)
    public Estudiante (String ci, String nombre, Sexo sexo, boolean activo, int cantidadGuardias, int cantidadGuardiasFestivo){
        this(ci, nombre, sexo, activo, cantidadGuardias, cantidadGuardiasFestivo, 0);
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
    public boolean puedeHacerGuardia() {
        return activo && !licenciaMatricula && !baja;


    public Estudiante(int id, String nombre, String apellidos, Sexo sexo, boolean activo, int grupo) {
        super(id, nombre, apellidos, sexo, activo);
        setGrupo(grupo);
    }

    @Override
    public boolean puedeHacerGuardia(LocalDate fecha, Horario horario) {
        boolean puedeHacerla = false;
        
        if (isActivo()) {
            int diaSemana = fecha.getDayOfWeek().getValue();
            boolean esFinDeSemana = diaSemana >= 6;
            boolean esHorarioDiurno = horario.getTipo().equals("Diurno");
            
            puedeHacerla = esFinDeSemana && esHorarioDiurno;
        }
        
        return puedeHacerla;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        if (grupo <= 0) {
            throw new IllegalArgumentException("El grupo debe ser positivo");
        }
        this.grupo = grupo;
    }
}
   