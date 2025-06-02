package logica;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlanificadorGuardias {
    private final Calendario calendario;
    private final GuardiaFactory guardiaFactory;
    private final Facultad facultad;

    public PlanificadorGuardias(Calendario calendario, Facultad facultad) {
        if (calendario == null || facultad == null) {
            throw new IllegalArgumentException("Dependencias no pueden ser nulas");
        }
        this.calendario = calendario;
        this.facultad = facultad;
        this.guardiaFactory = new GuardiaFactory(calendario);
    }

    /**
     * Planifica guardias para todos los días del mes actual
     * que no sean festivos, asignando turnos según el día de la semana
     */
    public void planificarMes() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
        
        List<Trabajador> trabajadoresDisponibles = obtenerTrabajadoresDisponibles();
        LocalDate fechaActual = inicioMes;
        
        while (!fechaActual.isAfter(finMes)) {
            if (!calendario.esFestivo(fechaActual)) {
                planificarDia(fechaActual, trabajadoresDisponibles);
            }
            fechaActual = fechaActual.plusDays(1);
        }
    }

    private List<Trabajador> obtenerTrabajadoresDisponibles() {
        List<Trabajador> disponibles = new ArrayList<>();
        for (Persona persona : facultad.getPersonas()) {
            if (persona instanceof Trabajador && persona.isActivo()) {
                disponibles.add((Trabajador) persona);
            }
        }
        return disponibles;
    }

    private void planificarDia(LocalDate fecha, List<Trabajador> trabajadores) {
        int diaSemana = fecha.getDayOfWeek().getValue();
        boolean esFinDeSemana = diaSemana >= 6;
        Horario horario = crearHorarioParaDia(diaSemana, esFinDeSemana);
        
        for (Trabajador trabajador : trabajadores) {
            if (puedeAsignarGuardia(trabajador, fecha, horario)) {
                asignarGuardia(trabajador, fecha, horario);
            }
        }
    }

    private Horario crearHorarioParaDia(int diaSemana, boolean esFinDeSemana) {
        Dia dia = Dia.values()[diaSemana - 1];
        if (esFinDeSemana) {
            return new Horario(dia, "08:00 AM", "02:00 PM", "Diurno");
        } else {
            return new Horario(dia, "06:00 PM", "10:00 PM", "Nocturno");
        }
    }

    private boolean puedeAsignarGuardia(Trabajador trabajador, LocalDate fecha, Horario horario) {
        boolean noTieneGuardia = !tieneGuardiaEnFecha(trabajador, fecha);
        return noTieneGuardia && trabajador.puedeHacerGuardia(fecha, horario);
    }

    private boolean tieneGuardiaEnFecha(Persona persona, LocalDate fecha) {
        List<Guardia> guardias = guardiaFactory.consultarGuardiasPorPersona(persona.getId());
        boolean tieneGuardia = false;
        
        for (Guardia guardia : guardias) {
            if (guardia.getFecha().equals(fecha)) {
                tieneGuardia = true;
            }
        }
        
        return tieneGuardia;
    }

    private void asignarGuardia(Trabajador trabajador, LocalDate fecha, Horario horario) {
        String motivo = "Guardia programada para " + fecha.getDayOfWeek().toString();
        guardiaFactory.crearGuardia(trabajador, horario, fecha, false, motivo);
    }

    // Resto de métodos existentes (asignarRecuperacion, CRUD días festivos, etc.)
    public boolean asignarRecuperacion(Persona persona, String motivo) {
        boolean asignada = false;
        
        if (persona != null && motivo != null && !motivo.trim().isEmpty()) {
            if (persona instanceof Trabajador && persona.isActivo()) {
                Guardia guardia = guardiaFactory.crearGuardiaRecuperacion(persona, motivo);
                asignada = guardia != null;
            }
        }
        
        return asignada;
    }

    public void crearDiasFestivos(LocalDate fecha) {
        if (fecha != null) {
            calendario.agregarFestivo(fecha);
        }
    }

    public Set<LocalDate> consultarDiasFestivos() {
        return calendario.getFestivos();
    }

    public boolean actualizarDiasFestivos(LocalDate fechaAntigua, LocalDate fechaNueva) {
        boolean actualizado = false;
        
        if (fechaAntigua != null && fechaNueva != null) {
            boolean eliminado = calendario.eliminarFestivo(fechaAntigua);
            if (eliminado) {
                calendario.agregarFestivo(fechaNueva);
                actualizado = true;
            }
        }
        
        return actualizado;
    }

    public boolean eliminarFestivos(LocalDate fecha) {
        boolean eliminado = false;
        if (fecha != null) {
            eliminado = calendario.eliminarFestivo(fecha);
        }
        return eliminado;
    }
}