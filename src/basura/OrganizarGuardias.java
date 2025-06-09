package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.YearMonth;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.Persona;
import logica.PlanificadorGuardias;
import logica.Guardia;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class OrganizarGuardias extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private static DefaultTableModel modeloTablaGuardias;
    private static JTable tablaGuardias;

    public OrganizarGuardias(final PlanificadorGuardias planificador) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 600);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));

        // Selector de mes y año
        JLabel lblMes = new JLabel("Mes:");
        final JMonthChooser monthChooser = new JMonthChooser();
        JLabel lblAnio = new JLabel("Año:");
        final JYearChooser yearChooser = new JYearChooser();

        panelForm.add(lblMes);
        panelForm.add(monthChooser);
        panelForm.add(lblAnio);
        panelForm.add(yearChooser);

        JButton btnPlanificar = new JButton("Planificar Guardias");
        panelForm.add(new JLabel(""));
        panelForm.add(btnPlanificar);

        contentPane.add(panelForm, BorderLayout.NORTH);

        // Tabla para mostrar guardias asignadas
        String[] columnas = {"CI", "Nombre", "Fecha", "Hora", "Grupo"};
        if (modeloTablaGuardias == null) {
            modeloTablaGuardias = new DefaultTableModel(columnas, 0) {
                private static final long serialVersionUID = 1L;
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }
        if (tablaGuardias == null) {
            tablaGuardias = new JTable(modeloTablaGuardias);
        }
        JScrollPane scrollTabla = new JScrollPane(tablaGuardias);
        contentPane.add(scrollTabla, BorderLayout.CENTER);

        btnPlanificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int mes = monthChooser.getMonth() + 1; // JMonthChooser es 0-based
                int anio = yearChooser.getYear();
                YearMonth yearMonth = YearMonth.of(anio, mes);

                // Limpiar tabla antes de planificar
                modeloTablaGuardias.setRowCount(0);

                ArrayList<Guardia> guardiasPlanificadas = planificador.planificarGuardiasAutomatico(yearMonth);
                if (guardiasPlanificadas == null || guardiasPlanificadas.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay guardias planificadas para " + yearMonth.getMonthValue() + "/" + yearMonth.getYear());
                    return;
                }
                for (Guardia g : guardiasPlanificadas) {
                    if (g == null || g.getPersona() == null || g.getHorario() == null) continue;
                    Persona persona = g.getPersona();
                    String ci = persona.getCi();
                    String nombre = persona.getNombre();
                    String fecha = g.getHorario().getFecha().toString();
                    String hora = g.getHorario().getHoraInicio().toString() + " - " + g.getHorario().getHoraFin().toString();
                    String grupo = "";
                    if (persona instanceof logica.Estudiante) {
                        grupo = String.valueOf(((logica.Estudiante) persona).getGrupo());
                    }
                    modeloTablaGuardias.addRow(new Object[]{
                        ci,
                        nombre,
                        fecha,
                        hora,
                        grupo
                    });
                }
                JOptionPane.showMessageDialog(null, "Guardias planificadas automáticamente para " + yearMonth.getMonthValue() + "/" + yearMonth.getYear());
            }
        });

        // Mostrar solo las guardias del mes seleccionado al cambiar mes/año
        java.beans.PropertyChangeListener actualizarTablaListener = new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                int mes = monthChooser.getMonth() + 1;
                int anio = yearChooser.getYear();
                //YearMonth yearMonth = YearMonth.of(anio, mes);

                modeloTablaGuardias.setRowCount(0);

                ArrayList<Guardia> guardias = planificador.getGuardias();
                for (Guardia g : guardias) {
                    if (g == null || g.getPersona() == null || g.getHorario() == null) continue;
                    if (g.getHorario().getFecha().getYear() == anio && g.getHorario().getFecha().getMonthValue() == mes) {
                        Persona persona = g.getPersona();
                        String ci = persona.getCi();
                        String nombre = persona.getNombre();
                        String fecha = g.getHorario().getFecha().toString();
                        String hora = g.getHorario().getHoraInicio().toString() + " - " + g.getHorario().getHoraFin().toString();
                        String grupo = "";
                        if (persona instanceof logica.Estudiante) {
                            grupo = String.valueOf(((logica.Estudiante) persona).getGrupo());
                        }
                        modeloTablaGuardias.addRow(new Object[]{
                            ci,
                            nombre,
                            fecha,
                            hora,
                            grupo
                        });
                    }
                }
                // Si no hay guardias, la tabla queda vacía
            }
        };
        monthChooser.addPropertyChangeListener("month", actualizarTablaListener);
        yearChooser.addPropertyChangeListener("year", actualizarTablaListener);
    }
}
