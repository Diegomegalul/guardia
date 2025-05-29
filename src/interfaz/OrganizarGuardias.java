package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    private JLabel lblHorarioSugerido;

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
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));

        personasFiltradas = new ArrayList<Persona>();
        cbPersonas = new JComboBox<String>();

        panelForm.add(new JLabel("Persona:"));
        panelForm.add(cbPersonas);

        dateChooser = new com.toedter.calendar.JDateChooser();
        dateChooser.getCalendarButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });

        panelForm.add(new JLabel("Fecha:"));
        panelForm.add(dateChooser);

        // Label para mostrar el horario sugerido
        lblHorarioSugerido = new JLabel("Horario sugerido: ");
        panelForm.add(new JLabel(""));
        panelForm.add(lblHorarioSugerido);

        JButton btnAsignar = new JButton("Asignar Guardia");
        panelForm.add(btnAsignar);

        contentPane.add(panelForm, BorderLayout.CENTER);

        // Inicializa la lista de personas filtradas
        actualizarPersonasFiltradas();

        // Actualiza el horario sugerido cuando cambia la persona o la fecha
        ActionListener actualizarHorarioListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarHorarioSugerido();
            }
        };
        cbPersonas.addActionListener(actualizarHorarioListener);

        dateChooser.getDateEditor().addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    mostrarHorarioSugerido();
                }
            }
        });

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
                    java.time.DayOfWeek dayOfWeek = fecha.getDayOfWeek();
                    utiles.Dia dia = utiles.Dia.valueOf(dayOfWeek.name());

                    // Determinar automáticamente el horario
                    java.time.LocalTime horaInicio = null;
                    java.time.LocalTime horaFin = null;
                    boolean esFestivo = false; // Puedes adaptar esto si tienes un campo para festivo

                    if (persona instanceof logica.Trabajador) {
                        // Solo fines de semana
                        if (dia == utiles.Dia.SATURDAY || dia == utiles.Dia.SUNDAY) {
                            // Alternar turno según cantidad de guardias asignadas (par/impar)
                            int cant = persona.getCantidadGuardias();
                            if (cant % 2 == 0) {
                                horaInicio = java.time.LocalTime.of(9, 0);
                                horaFin = java.time.LocalTime.of(14, 0);
                            } else {
                                horaInicio = java.time.LocalTime.of(14, 0);
                                horaFin = java.time.LocalTime.of(19, 0);
                            }
                        }
                    } else if (persona instanceof logica.Estudiante) {
                        if (((logica.Estudiante) persona).getSexo() == utiles.Sexo.MASCULINO) {
                            // Todos los días, turno de noche
                            horaInicio = java.time.LocalTime.of(20, 0);
                            horaFin = java.time.LocalTime.of(8, 0);
                        } else {
                            // Solo fines de semana, turno de día
                            if (dia == utiles.Dia.SATURDAY || dia == utiles.Dia.SUNDAY) {
                                horaInicio = java.time.LocalTime.of(8, 0);
                                horaFin = java.time.LocalTime.of(20, 0);
                            }
                        }
                    }

                    if (horaInicio == null || horaFin == null) {
                        JOptionPane.showMessageDialog(null, "No hay horario válido para la persona y fecha seleccionada.");
                        return;
                    }

                    Horario horario = new Horario(dia, fecha, horaInicio, horaFin, esFestivo);

                    // Validar si la persona puede realizar la guardia
                    if (!planificador.puedeRealizarGuardia(persona, horario)) {
                        JOptionPane.showMessageDialog(null, "La persona seleccionada no puede realizar la guardia en ese horario.");
                        return;
                    }

                    planificador.crearGuardia(idx, horario, persona);
                    persona.setCantidadGuardias(persona.getCantidadGuardias() + 1);
                    JOptionPane.showMessageDialog(null, "Guardia asignada correctamente.");
                    actualizarPersonasFiltradas();
                    mostrarHorarioSugerido();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al asignar guardia: " + ex.getMessage());
                }
            }
        });
    }

    private void mostrarHorarioSugerido() {
        int idx = cbPersonas.getSelectedIndex();
        java.util.Date fechaDate = dateChooser.getDate();
        if (idx == -1 || fechaDate == null) {
            lblHorarioSugerido.setText("Horario sugerido: ");
            return;
        }
        Persona persona = personasFiltradas.get(idx);
        java.time.LocalDate fecha = fechaDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        java.time.DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        utiles.Dia dia = utiles.Dia.valueOf(dayOfWeek.name());

        String texto = "No disponible";
        if (persona instanceof logica.Trabajador) {
            if (dia == utiles.Dia.SATURDAY || dia == utiles.Dia.SUNDAY) {
                int cant = persona.getCantidadGuardias();
                if (cant % 2 == 0) {
                    texto = "9:00 - 14:00";
                } else {
                    texto = "14:00 - 19:00";
                }
            }
        } else if (persona instanceof logica.Estudiante) {
            if (((logica.Estudiante) persona).getSexo() == utiles.Sexo.MASCULINO) {
                texto = "20:00 - 08:00 (todos los días)";
            } else {
                if (dia == utiles.Dia.SATURDAY || dia == utiles.Dia.SUNDAY) {
                    texto = "8:00 - 20:00";
                }
            }
        }
        lblHorarioSugerido.setText("Horario sugerido: " + texto);
    }
}
