package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.toedter.calendar.JDateChooser;

import logica.*;
import utiles.TipoGuardia;

public class FormularioGuardia extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> comboTipo;
	private JComboBox<Persona> comboPersona;
	private JDateChooser dateChooserFecha;
	private JTextField txtHoraInicio, txtHoraFin;


	public FormularioGuardia() {
		this(null);
	}

	public FormularioGuardia(final Guardia guardia) {
		setTitle("Editar/Crear Guardia");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 340);
		setLocationRelativeTo(null);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelForm = new JPanel();
		panelForm.setBackground(amarillo);
		panelForm.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:100px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("220px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu")}
		));

		// Tipo
		JLabel lblTipo = new JLabel("Tipo:");
		panelForm.add(lblTipo, "2, 2, right, center");
		comboTipo = new JComboBox<>();
		for (TipoGuardia tipo : TipoGuardia.values()) {
			comboTipo.addItem(tipo.toString());
		}
		panelForm.add(comboTipo, "4, 2, fill, center");

		// Persona
		JLabel lblPersona = new JLabel("Persona:");
		panelForm.add(lblPersona, "2, 4, right, center");
		comboPersona = new JComboBox<>();
		panelForm.add(comboPersona, "4, 4, fill, center");

		// Fecha
		JLabel lblFecha = new JLabel("Fecha:");
		panelForm.add(lblFecha, "2, 6, right, center");
		dateChooserFecha = new JDateChooser();
		dateChooserFecha.setDateFormatString("yyyy-MM-dd");
		panelForm.add(dateChooserFecha, "4, 6, fill, center");

		// Hora Inicio
		JLabel lblHoraInicio = new JLabel("Hora Inicio:");
		panelForm.add(lblHoraInicio, "2, 8, right, center");
		txtHoraInicio = new JTextField();
		txtHoraInicio.setEditable(false);
		panelForm.add(txtHoraInicio, "4, 8, fill, center");

		// Hora Fin
		JLabel lblHoraFin = new JLabel("Hora Fin:");
		panelForm.add(lblHoraFin, "2, 10, right, center");
		txtHoraFin = new JTextField();
		txtHoraFin.setEditable(false);
		panelForm.add(txtHoraFin, "4, 10, fill, center");

		contentPane.add(panelForm, BorderLayout.CENTER);

		// Botón guardar
		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);
		JButton btnGuardar = new JButton("Guardar Cambios");
		btnGuardar.setFont(new Font("Arial", Font.BOLD, 16));
		btnGuardar.setBackground(negro);
		btnGuardar.setForeground(amarillo);
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnGuardar.setContentAreaFilled(false);
		btnGuardar.setOpaque(true);

		panelBoton.add(btnGuardar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);

		// --- Lógica de actualización de combos y horas ---
		comboTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarComboPersona();
				actualizarHoras();
			}
		});
		comboPersona.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarHoras();
			}
		});
		dateChooserFecha.addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				actualizarHoras();
			}
		});

		// Si es edición, llenar campos
		if (guardia != null) {
			comboTipo.setSelectedItem(guardia.getTipo().toString());
			actualizarComboPersona();
			comboPersona.setSelectedItem(guardia.getPersona());
			dateChooserFecha.setDate(java.sql.Date.valueOf(guardia.getHorario().getDia()));
			txtHoraInicio.setText(guardia.getHorario().getHoraInicio().toString());
			txtHoraFin.setText(guardia.getHorario().getHoraFin().toString());
		} else {
			actualizarComboPersona();
		}

		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TipoGuardia tipo = TipoGuardia.valueOf(comboTipo.getSelectedItem().toString());
					Persona persona = (Persona) comboPersona.getSelectedItem();
					java.util.Date utilDate = dateChooserFecha.getDate();
					if (persona == null || utilDate == null) {
						JOptionPane.showMessageDialog(FormularioGuardia.this, "Seleccione persona y fecha.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					LocalDate fecha = new java.sql.Date(utilDate.getTime()).toLocalDate();
					LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText().trim());
					LocalTime horaFin = LocalTime.parse(txtHoraFin.getText().trim());
					Horario nuevoHorario = new Horario(fecha, horaInicio, horaFin);

					if (guardia != null) {
						guardia.setTipo(tipo);
						guardia.setPersona(persona);
						guardia.setHorario(nuevoHorario);
						JOptionPane.showMessageDialog(FormularioGuardia.this, "Guardia actualizada correctamente.", "Guardia", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// Aquí podrías crear una nueva guardia si lo deseas
						JOptionPane.showMessageDialog(FormularioGuardia.this, "Guardia creada (simulado).", "Guardia", JOptionPane.INFORMATION_MESSAGE);
					}
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(FormularioGuardia.this, "Error en los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private void actualizarComboPersona() {
		comboPersona.removeAllItems();
		TipoGuardia tipo = TipoGuardia.valueOf(comboTipo.getSelectedItem().toString());
		List<Persona> personas = logica.PlanificadorGuardias.getInstancia().getFacultad().getPersonas();
		if (tipo == TipoGuardia.FESTIVO) {
			// Personas con menos guardias festivas
			int minFestivos = Integer.MAX_VALUE;
			for (Persona p : personas) {
				if (p.getActivo() && p.getCantidadGuardiasFestivo() < minFestivos) {
					minFestivos = p.getCantidadGuardiasFestivo();
				}
			}
			for (Persona p : personas) {
				if (p.getCantidadGuardiasFestivo() == minFestivos && p.getActivo()) {
					comboPersona.addItem(p);
				}
			}
		} else if (tipo == TipoGuardia.RECUPERACION) {
			for (Persona p : personas) {
				if (p instanceof Estudiante && ((Estudiante)p).tieneGuardiasPendientes() && p.getActivo()) {
					comboPersona.addItem(p);
				}
			}
		} else if (tipo == TipoGuardia.VOLUNTARIA) {
			for (Persona p : personas) {
				if (p instanceof Trabajador && ((Trabajador)p).getVoluntario() && p.getActivo()) {
					comboPersona.addItem(p);
				}
			}
		} else {
			for (Persona p : personas) {
				if (p.getActivo()) {
					comboPersona.addItem(p);
				}
			}
		}
	}

	private void actualizarHoras() {
		Persona persona = (Persona) comboPersona.getSelectedItem();
		java.util.Date utilDate = dateChooserFecha.getDate();
		if (persona == null || utilDate == null) {
			txtHoraInicio.setText("");
			txtHoraFin.setText("");
			return;
		}
		LocalDate fecha = new java.sql.Date(utilDate.getTime()).toLocalDate();
		LocalTime horaInicio = null, horaFin = null;
		if (persona instanceof Estudiante) {
			Estudiante est = (Estudiante) persona;
			if (est.getSexo() == utiles.Sexo.MASCULINO) {
				horaInicio = LocalTime.of(20, 0);
				horaFin = LocalTime.of(8, 0);
			} else {
				// Solo fines de semana
				if (fecha.getDayOfWeek() == java.time.DayOfWeek.SATURDAY || fecha.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
					horaInicio = LocalTime.of(8, 0);
					horaFin = LocalTime.of(20, 0);
				}
			}
		} else if (persona instanceof Trabajador) {
			// Ejemplo: horario diurno para trabajadores
			horaInicio = LocalTime.of(9, 0);
			horaFin = LocalTime.of(14, 0);
		}
		if (horaInicio != null && horaFin != null) {
			txtHoraInicio.setText(horaInicio.toString());
			txtHoraFin.setText(horaFin.toString());
		} else {
			txtHoraInicio.setText("");
			txtHoraFin.setText("");
		}
	}
}
