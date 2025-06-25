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
		setTitle("Editar Guardia");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new GridLayout(6, 2, 10, 10));
		setContentPane(contentPane);

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
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
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
					JOptionPane.showMessageDialog(EditGuardia.this, "Seleccione una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				LocalDate fecha = new java.sql.Date(utilDate.getTime()).toLocalDate();
				LocalTime inicio = LocalTime.of(9, 0);
				LocalTime fin = LocalTime.of(14, 0);
				if (persona instanceof Trabajador && comboTurno.getSelectedIndex() == 1) {
					inicio = LocalTime.of(14, 0);
					fin = LocalTime.of(19, 0);
				}
				// Validar que la persona puede hacer la guardia
				logica.Horario nuevoHorario = new logica.Horario(fecha, inicio, fin);
				if (!persona.puedeHacerGuardia(nuevoHorario)) {
					JOptionPane.showMessageDialog(EditGuardia.this, "La persona seleccionada no puede hacer guardia en ese horario.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Validar que no exista otra guardia para esa persona en ese día y horario
				boolean existeSolapamiento = logica.PlanificadorGuardias.getInstancia().getGuardiaFactory()
					.existeGuardiaParaPersonaEnHorario(persona, fecha, inicio, fin, guardia);
				if (existeSolapamiento) {
					JOptionPane.showMessageDialog(EditGuardia.this, "Ya existe una guardia para esa persona en ese horario.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Guardar cambios
				guardia.setTipo(tipo);
				guardia.setPersona(persona);
				guardia.getHorario().setDia(fecha);
				guardia.getHorario().setHoraInicio(inicio);
				guardia.getHorario().setHoraFin(fin);
				JOptionPane.showMessageDialog(EditGuardia.this, "Guardia actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		contentPane.add(new JLabel());
		contentPane.add(btnGuardar);
	}

	private void cargarPersonasParaTipo(TipoGuardia tipo, Guardia guardia) {
		comboPersona.removeAllItems();
		List<Persona> personas = logica.PlanificadorGuardias.getInstancia().getFacultad().getPersonas();
		for (Persona p : personas) {
			if (tipo == TipoGuardia.NORMAL || tipo == TipoGuardia.FESTIVO) {
				if (p.getActivo()) comboPersona.addItem(p);
			} else if (tipo == TipoGuardia.VOLUNTARIA) {
				if (p instanceof Trabajador && ((Trabajador)p).getVoluntario()) comboPersona.addItem(p);
			} else if (tipo == TipoGuardia.RECUPERACION) {
				if (p instanceof Estudiante && ((Estudiante)p).tieneGuardiasPendientes()) comboPersona.addItem(p);
			}
		}
		if (guardia != null) comboPersona.setSelectedItem(guardia.getPersona());
	}
}
