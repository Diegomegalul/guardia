package logica;

import utiles.Dia;

public class Horario {
    private final String horaInicio;
    private final String horaFin;
    private final String tipo;
    private final Dia dia;

    public Horario(Dia dia, String horaInicio, String horaFin, String tipo) {
        if (dia == null || horaInicio == null || horaFin == null || tipo == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo");
        }
        if (!validarFormatoHora(horaInicio) || !validarFormatoHora(horaFin)) {
            throw new IllegalArgumentException("Formato de hora inválido. Use 'hh:mm AM/PM'");
        }
        if (!tipo.equals("Diurno") && !tipo.equals("Nocturno")) {
            throw new IllegalArgumentException("Tipo de horario debe ser 'Diurno' o 'Nocturno'");
        }

        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tipo = tipo;
    }

    private boolean validarFormatoHora(String hora) {
        return hora.matches("^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$");
    }

    public boolean solapaCon(Horario otro) {
        int inicioThis = convertirAMinutos(this.horaInicio);
        int finThis = convertirAMinutos(this.horaFin);
        int inicioOtro = convertirAMinutos(otro.horaInicio);
        int finOtro = convertirAMinutos(otro.horaFin);

        return !(finThis <= inicioOtro || finOtro <= inicioThis);
    }

    private int convertirAMinutos(String horaAmPm) {
        String[] partes = horaAmPm.split(" ");
        String[] tiempo = partes[0].split(":");
        int horas = Integer.parseInt(tiempo[0]);
        int minutos = Integer.parseInt(tiempo[1]);

        if (partes[1].equals("PM") && horas != 12) {
            horas += 12;
        } else if (partes[1].equals("AM") && horas == 12) {
            horas = 0;
        }

        return horas * 60 + minutos;
    }

    // Getters
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getTipo() { return tipo; }
    public Dia getDia() { return dia; }

    @Override
    public String toString() {
        return String.format("%s: %s a %s (%s)", dia, horaInicio, horaFin, tipo);
    }
}