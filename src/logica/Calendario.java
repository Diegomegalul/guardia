package logica;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Calendario {
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


