package logica;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import logica.DiaFestivo;

public class Calendario {
	//Atributos
	private List<DiaFestivo> diasFestivos;

	//Constructor
	public Calendario(){
		this.diasFestivos = new ArrayList<>();
	}
	
	//Setters y getters
	public void setDiasFestivos(List<DiaFestivo> diasFestivos) {
		this.diasFestivos = new ArrayList<>();
		if (diasFestivos != null) {
			this.diasFestivos.addAll(diasFestivos);
		}
	}
	
	public List<DiaFestivo> getDiasFestivos() {
		return diasFestivos;
	}
    //Metodos

	// CRUD de Dias Festivos

	// Crear
	public void agregarDiaFestivo(DiaFestivo diaFestivo) {
		if (diaFestivo != null && !existeDiaFestivo(diaFestivo.getFecha())) {
			diasFestivos.add(diaFestivo);
		}
	}

	// Leer (buscar por fecha)
	public DiaFestivo obtenerDiaFestivo(LocalDate fecha) {
		DiaFestivo encontrado = null;
		for (DiaFestivo d : diasFestivos) {
			if (d.getFecha().equals(fecha)) {
				encontrado = d;
				break;
			}
		}
		return encontrado;
	}

	public boolean actualizarDiaFestivo(LocalDate fecha, String nuevaDescripcion) {
		boolean actualizado = false;
		DiaFestivo dia = obtenerDiaFestivo(fecha);
		if (dia != null) {
			dia.setDescripcion(nuevaDescripcion);
			actualizado = true;
		}
		return actualizado;
	}

	public boolean eliminarDiaFestivo(LocalDate fecha) {
		boolean eliminado = false;
		DiaFestivo dia = obtenerDiaFestivo(fecha);
		if (dia != null) {
			diasFestivos.remove(dia);
			eliminado = true;
		}
		return eliminado;
	}

	// Verificar si existe un día festivo por fecha
	public boolean existeDiaFestivo(LocalDate fecha) {
		return obtenerDiaFestivo(fecha) != null;
	}

	
	
	
	
	
	
	
	
	
	
	
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
