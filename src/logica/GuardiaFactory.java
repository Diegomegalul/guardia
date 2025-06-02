package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GuardiaFactory {
    private final Calendario calendario;
    private final List<Guardia> guardias;
    private static int contadorId = 1;

    public GuardiaFactory(Calendario calendario) {
        if (calendario == null) {
            throw new IllegalArgumentException("El calendario no puede ser nulo");
        }
        this.calendario = calendario;
        this.guardias = new ArrayList<>();
    }

    public Guardia crearGuardia(Persona persona, Horario horario, LocalDate fecha, 
                              boolean esRecuperacion, String motivo) {
        Guardia guardia = null;
        boolean parametrosValidos = persona != null && horario != null && 
                                  fecha != null && motivo != null;
        
        if (parametrosValidos) {
            boolean noEsFestivo = !calendario.esFestivo(fecha);
            boolean puedeHacerGuardia = persona.puedeHacerGuardia(fecha, horario);
            
            if (noEsFestivo && puedeHacerGuardia) {
                guardia = new Guardia(contadorId++, fecha, persona, horario, esRecuperacion, motivo);
                guardias.add(guardia);
            }
        }
        return guardia;
    }

    public Guardia crearGuardiaRecuperacion(Persona persona, String motivo) {
        LocalDate fecha = LocalDate.now();
        Horario horario = new Horario(Dia.LUNES, "08:00 AM", "04:00 PM", "Diurno");
        return crearGuardia(persona, horario, fecha, true, motivo);
    }

    public Guardia consultarGuardia(int id) {
        Guardia resultado = null;
        for (Guardia guardia : guardias) {
            if (guardia.getId() == id) {
                resultado = guardia;
            }
        }
        return resultado;
    }

    public List<Guardia> consultarGuardiasPorPersona(int idPersona) {
        List<Guardia> resultado = new ArrayList<>();
        for (Guardia guardia : guardias) {
            if (guardia.getPersona().getId() == idPersona) {
                resultado.add(guardia);
            }
        }
        return resultado;
    }

    public boolean actualizarGuardia(int id, Horario nuevoHorario, LocalDate nuevaFecha) {
        boolean actualizado = false;
        Guardia guardia = consultarGuardia(id);
        
        if (guardia != null) {
            guardia.setHorario(nuevoHorario);
            guardia.setFecha(nuevaFecha);
            actualizado = true;
        }
        
        return actualizado;
    }

    public boolean eliminarGuardia(int id) {
        boolean eliminado = false;
        Guardia guardia = consultarGuardia(id);
        
        if (guardia != null) {
            eliminado = guardias.remove(guardia);
        }
        
        return eliminado;
    }
    
}