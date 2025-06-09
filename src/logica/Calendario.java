package logica;

import java.util.ArrayList;
import java.util.List;

public class Calendario {
	//Atributos
	private List<DiaFestivo> diasFestivos;

	//Constructor
	public Calendario(){
		setDiasFestivos(diasFestivos);
	}
	
	//Setters y getters
	public void setDiasFestivos(List<DiaFestivo> diasFestivos) {
		this.diasFestivos = new ArrayList<>();
	}
	
	public List<DiaFestivo> getDiasFestivos() {
		return diasFestivos;
	}

	//Metodos
	
	
	
	
	
	
	
	
}



























	/* mas basura de Daniela
    private Set<LocalDate> festivos;

    public Calendario(Set<LocalDate> festivos) {
        if (festivos == null) {
            throw new IllegalArgumentException("El conjunto de festivos no puede ser nulo");
        }
        this.festivos = new HashSet<>(festivos);
    }

    public boolean esFestivo(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        return festivos.contains(fecha);
    }

    public Set<LocalDate> getFestivos() {
        return Collections.unmodifiableSet(festivos);
    }
}

*/
