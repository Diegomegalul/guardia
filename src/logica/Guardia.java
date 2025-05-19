package logica;


public class Guardia {
    //Atributos
    private int id;
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

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
    //Metodos
}
