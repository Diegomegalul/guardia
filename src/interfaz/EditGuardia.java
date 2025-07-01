package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import logica.Guardia;
import logica.Persona;
import logica.Estudiante;
import logica.Trabajador;
import utiles.TipoGuardia;

public class EditGuardia extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<TipoGuardia> comboTipo;
	private JComboBox<Persona> comboPersona;
	private JDateChooser dateChooser;
	private JComboBox<String> comboTurno;

	public EditGuardia(final Guardia guardia) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setTitle("Editar Guardia");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		setLocationRelativeTo(null);
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new GridLayout(6, 2, 10, 10));
		setContentPane(contentPane);

		if (guardia == null) {
			JLabel lbl = new JLabel("Seleccione una guardia para editar", SwingConstants.CENTER);
			lbl.setFont(new Font("Arial", Font.BOLD, 18));
			lbl.setForeground(Color.BLACK);
			contentPane.add(lbl, BorderLayout.CENTER);
		} else {
			// Tipo de guardia
			contentPane.add(new JLabel("Tipo de guardia:"));
			comboTipo = new JComboBox<>();
			for (TipoGuardia tipo : TipoGuardia.values()) {
				comboTipo.addItem(tipo);
			}
			comboTipo.setSelectedItem(guardia.getTipo());
			contentPane.add(comboTipo);

			// Persona
			contentPane.add(new JLabel("Persona:"));
			comboPersona = new JComboBox<>();
			cargarPersonasParaTipo((TipoGuardia) comboTipo.getSelectedItem(), guardia);
			comboPersona.setSelectedItem(guardia.getPersona());
			comboPersona.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected,
						boolean cellHasFocus) {
					Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof Persona) {
						Persona p = (Persona) value;
						setText(p.getNombre() + " " + p.getApellidos() + " (" + p.getCi() + ")");
					}
					return c;
				}
			});
			contentPane.add(comboPersona);

			// Fecha
			contentPane.add(new JLabel("Fecha:"));
			dateChooser = new JDateChooser();
			dateChooser.setDate(java.sql.Date.valueOf(guardia.getHorario().getDia()));
			dateChooser.setDateFormatString("yyyy-MM-dd");
			contentPane.add(dateChooser);

			// Turno (solo para trabajadores)
			contentPane.add(new JLabel("Turno:"));
			comboTurno = new JComboBox<>();
			comboTurno.addItem("9:00-14:00");
			comboTurno.addItem("14:00-19:00");
			if (guardia.getPersona() instanceof Trabajador) {
				LocalTime inicio = guardia.getHorario().getHoraInicio();
				if (inicio.equals(LocalTime.of(14, 0))) {
					comboTurno.setSelectedIndex(1);
				} else {
					comboTurno.setSelectedIndex(0);
				}
				comboTurno.setEnabled(true);
			} else {
				comboTurno.setEnabled(false);
			}
			contentPane.add(comboTurno);

			// Cambiar personas al cambiar tipo
			comboTipo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPersonasParaTipo((TipoGuardia) comboTipo.getSelectedItem(), guardia);
				}
			});

			// Cambiar habilitación de turno según persona
			comboPersona.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Persona p = (Persona) comboPersona.getSelectedItem();
					if (p instanceof Trabajador) {
						comboTurno.setEnabled(true);
					} else {
						comboTurno.setEnabled(false);
					}
				}
			});

			JButton btnGuardar = new JButton("Guardar cambios");
			btnGuardar.setFont(new Font("Arial", Font.BOLD, 15));
			btnGuardar.setBackground(negro);
			btnGuardar.setForeground(amarillo);
			btnGuardar.setFocusPainted(false);
			btnGuardar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
			btnGuardar.setContentAreaFilled(false);
			btnGuardar.setOpaque(true);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TipoGuardia tipo = (TipoGuardia) comboTipo.getSelectedItem();
					Persona persona = (Persona) comboPersona.getSelectedItem();
					java.util.Date utilDate = dateChooser.getDate();
					if (utilDate == null) {
						JOptionPane.showMessageDialog(EditGuardia.this, "Seleccione una fecha.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					LocalDate fecha = new java.sql.Date(utilDate.getTime()).toLocalDate();
					LocalTime inicio;
					LocalTime fin;
					if (persona instanceof Trabajador) {
						if (comboTurno.getSelectedIndex() == 1) {
							inicio = LocalTime.of(14, 0);
							fin = LocalTime.of(19, 0);
						} else {
							inicio = LocalTime.of(9, 0);
							fin = LocalTime.of(14, 0);
						}
					} else if (persona instanceof Estudiante) {
						Estudiante est = (Estudiante) persona;
						if (est.getSexo() == utiles.Sexo.MASCULINO) {
							inicio = LocalTime.of(20, 0);
							fin = LocalTime.of(8, 0);
						} else {
							// Femenino: solo fines de semana
							java.time.DayOfWeek diaSemana = fecha.getDayOfWeek();
							if (diaSemana == java.time.DayOfWeek.SATURDAY || diaSemana == java.time.DayOfWeek.SUNDAY) {
								inicio = LocalTime.of(8, 0);
								fin = LocalTime.of(20, 0);
							} else {
								JOptionPane.showMessageDialog(EditGuardia.this,
										"Las estudiantes solo pueden hacer guardia los fines de semana (sábado o domingo) de 8:00 a 20:00.",
										"Error", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					} else {
						// Por defecto, horario de trabajador
						inicio = LocalTime.of(9, 0);
						fin = LocalTime.of(14, 0);
					}
					// Validar que la persona puede hacer la guardia
					logica.Horario nuevoHorario = new logica.Horario(fecha, inicio, fin);
					if (!persona.puedeHacerGuardia(nuevoHorario)) {
						JOptionPane.showMessageDialog(EditGuardia.this,
								"La persona seleccionada no puede hacer guardia en ese horario.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					// Validar que no exista otra guardia para esa persona en ese día y horario
					boolean existeSolapamiento = logica.PlanificadorGuardias.getInstancia().getGuardiaFactory()
							.existeGuardiaParaPersonaEnHorario(persona, fecha, inicio, fin, guardia);
					if (existeSolapamiento) {
						JOptionPane.showMessageDialog(EditGuardia.this,
								"Ya existe una guardia para esa persona en ese horario.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					// Guardar cambios
					Persona personaAnterior = guardia.getPersona();
					if (!personaAnterior.getCi().equals(persona.getCi())) {
						// Actualizar contadores solo si cambia la persona
						personaAnterior.setGuardiasPlanificadas(personaAnterior.getGuardiasPlanificadas() - 1);
						persona.setGuardiasPlanificadas(persona.getGuardiasPlanificadas() + 1);
					}
					guardia.setTipo(tipo);
					guardia.setPersona(persona);
					guardia.getHorario().setDia(fecha);
					guardia.getHorario().setHoraInicio(inicio);
					guardia.getHorario().setHoraFin(fin);
					JOptionPane.showMessageDialog(EditGuardia.this, "Guardia actualizada correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			});
			contentPane.add(new JLabel());
			contentPane.add(btnGuardar);
		}
	}

	private void cargarPersonasParaTipo(TipoGuardia tipo, Guardia guardia) {
		comboPersona.removeAllItems();
		List<Persona> personas = logica.PlanificadorGuardias.getInstancia().getFacultad().getPersonas();
		for (Persona p : personas) {
			if (tipo == TipoGuardia.NORMAL || tipo == TipoGuardia.FESTIVO) {
				if (p.getActivo())
					comboPersona.addItem(p);
			} else if (tipo == TipoGuardia.VOLUNTARIA) {
				if (p instanceof Trabajador && ((Trabajador) p).getVoluntario())
					comboPersona.addItem(p);
			} else if (tipo == TipoGuardia.RECUPERACION) {
				if (p instanceof Estudiante && ((Estudiante) p).tieneGuardiasPendientes())
					comboPersona.addItem(p);
			}
		}
		if (guardia != null)
			comboPersona.setSelectedItem(guardia.getPersona());
	}

	// Método para integración con Inicio
	public JPanel getPanelPrincipal() {
		return contentPane;
	}

	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		if (getContentPane() instanceof JPanel) {
			JPanel contentPane = (JPanel) getContentPane();
			contentPane.setBackground(fondo);
			setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
		}
	}

	private void setComponentColors(Component comp, boolean oscuro, Color fondo, Color texto, Color boton,
			Color amarilloSec) {
		if (comp instanceof JPanel) {
			comp.setBackground(fondo);
			for (Component child : ((JPanel) comp).getComponents()) {
				setComponentColors(child, oscuro, fondo, texto, boton, amarilloSec);
			}
		} else if (comp instanceof JLabel) {
			((JLabel) comp).setForeground(oscuro ? Color.WHITE : Color.BLACK);
		} else if (comp instanceof JTextField) {
			comp.setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
			((JTextField) comp).setForeground(oscuro ? Color.WHITE : texto);
		} else if (comp instanceof JComboBox) {
			comp.setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
			comp.setForeground(oscuro ? Color.WHITE : texto);
		} else if (comp instanceof JButton) {
			comp.setBackground(boton);
			comp.setForeground(amarilloSec);
		} else if (comp instanceof JTable) {
			comp.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			((JTable) comp).setForeground(oscuro ? Color.WHITE : texto);
		}
	}
}
