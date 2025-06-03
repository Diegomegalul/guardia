package logica;

import java.time.LocalDate;
import utiles.Sexo;

public class Estudiante extends Persona {
    private int grupo;

    public Estudiante(int id, String nombre, String apellidos, Sexo sexo, boolean activo, int grupo) {
        super(id, nombre, apellidos, sexo, activo);
        setGrupo(grupo);
    }

    @Override
    public boolean puedeHacerGuardia(LocalDate fecha, Horario horario) {
        boolean puedeHacerla = false;
        
        if (isActivo()) {
            int diaSemana = fecha.getDayOfWeek().getValue();
            boolean esFinDeSemana = diaSemana >= 6;
            boolean esHorarioDiurno = horario.getTipo().equals("Diurno");
            
            puedeHacerla = esFinDeSemana && esHorarioDiurno;
        }
        
        return puedeHacerla;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        if (grupo <= 0) {
            throw new IllegalArgumentException("El grupo debe ser positivo");
        }
        this.grupo = grupo;
    }
}
   