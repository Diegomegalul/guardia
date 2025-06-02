package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Facultad {
    private final List<Persona> personas = new ArrayList<>();

    public List<Persona> getPersonas() {
        return Collections.unmodifiableList(personas);
    }

    public void agregarPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }
        personas.add(persona);
    }

    public void eliminarPersona(int id) {
        personas.removeIf(p -> p.getId() == id);
    }

    public Persona buscarPersonaPorId(int id) {
        return personas.stream()
                     .filter(p -> p.getId() == id)
                     .findFirst()
                     .orElse(null);
    }

    public void actualizarPersona(Persona personaActualizada) {
        if (personaActualizada == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }
        
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getId() == personaActualizada.getId()) {
                personas.set(i, personaActualizada);
                return;
            }
        }
        throw new IllegalArgumentException("Persona no encontrada");
    }
}