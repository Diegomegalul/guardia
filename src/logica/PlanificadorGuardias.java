package logica;

public class PlanificadorGuardias {
	//Atributos
	private final Facultad facultad;
	private final Calendario calendario;
	private final GuardiaFactory guardiaFactory;
	private static PlanificadorGuardias instancia;//para el Singleton :v
	//Constructor
	private PlanificadorGuardias() {
        this.facultad = new Facultad("Informatica"); 
        this.calendario = new Calendario();  
        this.guardiaFactory = new GuardiaFactory();  
    }
	//Setters y Getters
	public Facultad getFacultad() {
        return facultad;
    }
    
    public Calendario getCalendario() {
        return calendario;
    }
    
    public GuardiaFactory getGuardiaFactory() {
        return guardiaFactory;
    }
	//Metodos
    //Método para obtener la instancia Singleton (OJO)
    public static synchronized PlanificadorGuardias getInstancia() {
        if (instancia == null) {
            instancia = new PlanificadorGuardias();
        }
        return instancia;
    }
}
