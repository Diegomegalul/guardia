package logica;

import java.util.ArrayList;
import java.util.List;
import logica.Persona;

public class Facultad {
    private List<Persona> personas = new ArrayList<Persona>();

    public List<Persona> getPersonas() {
        return personas;
    }

    public void agregarPersona(Persona persona) {
        personas.add(persona);
    }

    public void eliminarPersona(String ci) {
        for (int i = 0; i < personas.size(); i++) {
            Persona p = personas.get(i);
            if (p.getCi().equals(ci)) {
                personas.remove(i);
                i--; // Por si hay más de una persona con el mismo CI
            }
        }
    }

    public Persona buscarPersonaPorCi(String ci) {
        for (Persona p : personas) {
            if (p.getCi().equals(ci)) {
                return p;
            }
        }
        return null;
    }

    public void actualizarPersona(Persona personaActualizada) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getCi().equals(personaActualizada.getCi())) {
                personas.set(i, personaActualizada);
                break;
            }
        }
    }
}
