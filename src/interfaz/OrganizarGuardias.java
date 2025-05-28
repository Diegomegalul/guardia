package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.time.LocalTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logica.Horario;
import logica.Persona;
import logica.PlanificadorGuardias;

import com.toedter.calendar.JDateChooser;

public class OrganizarGuardias extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> cbPersonas;
    private ArrayList<Persona> personasFiltradas;
    private PlanificadorGuardias planificador; // Debe estar declarado aquí

    // Elimina: private JTextField txtDia;
    private JDateChooser dateChooser;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;

    private void actualizarPersonasFiltradas() {
        // Encontrar el mínimo de cantidad de guardias entre todas las personas
        int minGuardias = Integer.MAX_VALUE;
        for (Persona p : planificador.getPersonas()) {
            if (p.getCantidadGuardias() < minGuardias) {
                minGuardias = p.getCantidadGuardias();
            }
        }
        // Filtrar solo las personas con la cantidad mínima de guardias
        personasFiltradas.clear();
        cbPersonas.removeAllItems();
        for (Persona p : planificador.getPersonas()) {
            if (p.getCantidadGuardias() == minGuardias) {
                personasFiltradas.add(p);
                cbPersonas.addItem(p.getNombre());
            }
        }
    }

    public OrganizarGuardias(final PlanificadorGuardias planificador) {
        this.planificador = planificador;
        setBounds(100, 100, 750, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));

        personasFiltradas = new ArrayList<Persona>();
        cbPersonas = new JComboBox<String>();

        panelForm.add(new JLabel("Persona:"));
        panelForm.add(cbPersonas);

        // Elimina la inicialización y el agregado de txtDia

        dateChooser = new com.toedter.calendar.JDateChooser();
        dateChooser.getCalendarButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        txtHoraInicio = new JTextField();
        txtHoraFin = new JTextField();

        // Elimina la línea:
        // panelForm.add(new JLabel("Día:"));
        // panelForm.add(txtDia);

        panelForm.add(new JLabel("Fecha:"));
        panelForm.add(dateChooser);
        panelForm.add(new JLabel("Hora inicio (HH:mm):"));
        panelForm.add(txtHoraInicio);
        panelForm.add(new JLabel("Hora fin (HH:mm):"));
        panelForm.add(txtHoraFin);

        JButton btnAsignar = new JButton("Asignar Guardia");
        panelForm.add(btnAsignar);

        contentPane.add(panelForm, BorderLayout.CENTER);

        // Inicializa la lista de personas filtradas
        actualizarPersonasFiltradas();

        btnAsignar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = cbPersonas.getSelectedIndex();
                if (idx == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione una persona.");
                    return;
                }
                Persona persona = personasFiltradas.get(idx);

                try {
                    java.util.Date fechaDate = dateChooser.getDate();
                    if (fechaDate == null) {
                        JOptionPane.showMessageDialog(null, "Seleccione una fecha válida.");
                        return;
                    }
                    java.time.LocalDate fecha = fechaDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                    // Obtener el día de la semana automáticamente
                    java.time.DayOfWeek dayOfWeek = fecha.getDayOfWeek();
                    // Asume que tu enum utiles.Dia tiene los mismos nombres que DayOfWeek (MONDAY, TUESDAY, ...)
                    utiles.Dia dia = utiles.Dia.valueOf(dayOfWeek.name());

                    LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText().trim());
                    LocalTime horaFin = LocalTime.parse(txtHoraFin.getText().trim());
                    boolean esFestivo = false; // Puedes adaptar esto si tienes un campo para festivo

                    Horario horario = new Horario(dia, fecha, horaInicio, horaFin, esFestivo);
                    planificador.crearGuardia(horario, persona);
                    persona.setCantidadGuardias(persona.getCantidadGuardias() + 1);
                    JOptionPane.showMessageDialog(null, "Guardia asignada correctamente.");
                    actualizarPersonasFiltradas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al asignar guardia: " + ex.getMessage());
                }
            }
        });
    }
}
