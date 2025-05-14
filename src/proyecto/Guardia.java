package proyecto;


public class Guardia {
    //Atributos
    private Horario horario;
    private Persona persona;
    //Constructor
    public Guardia(Horario horario, Persona personas){
        setHorario(horario);
        setPersona(persona);
    }
    //Getters y setters
    public void setHorario(Horario horario){
        this.horario = horario;
    }

    public Horario getHorario(){
        return horario;
    }

    public void setPersona(Persona persona){
        this.persona = persona;
    }

    public Persona getPersona(){
        return persona;
    }
    //Metodos
}
