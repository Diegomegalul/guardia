package logica;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Calendario {
	//Atributos
	private List<DiaFestivo> diasFestivos;
	private static Calendario instancia; // Singleton

	//Constructor privado para Singleton
	private Calendario(){
		this.diasFestivos = new ArrayList<>();
	}

	// Singleton: obtener instancia única
	public static synchronized Calendario getInstancia() {
		if (instancia == null) {
			instancia = new Calendario();
		}
		return instancia;
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
		if (fecha == null || diasFestivos == null || diasFestivos.isEmpty()) {
			return false;
		}
		for (DiaFestivo d : diasFestivos) {
			if (d != null && fecha.equals(d.getFecha())) {
				return true;
			}
		}
		return false;
	}

	public void agregarDiasFestivosPrueba() {
		// Validar que no se agreguen dos veces
		boolean yaAgregados =
			existeDiaFestivo(java.time.LocalDate.of(2025, 1, 1)) &&
			existeDiaFestivo(java.time.LocalDate.of(2025, 5, 1)) &&
			existeDiaFestivo(java.time.LocalDate.of(2025, 7, 26)) &&
			existeDiaFestivo(java.time.LocalDate.of(2025, 12, 25)) &&
			existeDiaFestivo(java.time.LocalDate.of(2025, 10, 10));
		if (yaAgregados) return;

		agregarDiaFestivo(new DiaFestivo(java.time.LocalDate.of(2025, 1, 1), "Año Nuevo"));
		agregarDiaFestivo(new DiaFestivo(java.time.LocalDate.of(2025, 5, 1), "Día del Trabajador"));
		agregarDiaFestivo(new DiaFestivo(java.time.LocalDate.of(2025, 7, 26), "Día de la Rebeldía Nacional"));
		agregarDiaFestivo(new DiaFestivo(java.time.LocalDate.of(2025, 12, 25), "Navidad"));
		agregarDiaFestivo(new DiaFestivo(java.time.LocalDate.of(2025, 10, 10), "Inicio de las Guerras de Independencia"));
	}

}

