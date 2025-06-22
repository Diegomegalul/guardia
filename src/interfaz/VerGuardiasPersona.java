package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.Guardia;
import logica.Persona;

public class VerGuardiasPersona extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public VerGuardiasPersona(Persona persona, List<Guardia> guardias) {
		setTitle("Guardias de " + (persona != null ? persona.getNombre() + " " + persona.getApellidos() : ""));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		// Validación para evitar NullPointerException
		String ci = persona != null ? persona.getCi() : "";
		String nombre = persona != null ? persona.getNombre() : "";
		String apellidos = persona != null ? persona.getApellidos() : "";
		String sexo = persona != null && persona.getSexo() != null ? persona.getSexo().toString() : "";
		String activo = persona != null ? (persona.getActivo() ? "Sí" : "No") : "";

		JLabel lblDatos = new JLabel(
			"<html><b>CI:</b> " + ci +
			" &nbsp; <b>Nombre:</b> " + nombre +
			" &nbsp; <b>Apellidos:</b> " + apellidos +
			" &nbsp; <b>Sexo:</b> " + sexo +
			" &nbsp; <b>Activo:</b> " + activo +
			"</html>"
		);
		lblDatos.setFont(new Font("Arial", Font.PLAIN, 15));
		lblDatos.setForeground(negro);
		lblDatos.setBorder(new EmptyBorder(10, 0, 10, 0));
		contentPane.add(lblDatos, BorderLayout.NORTH);

		// Tabla de guardias asignadas (únicas)
		String[] columnas = {"Fecha", "Hora Inicio", "Hora Fin", "Tipo"};
		DefaultTableModel modelAsignadas = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};
		java.util.HashSet<String> guardiasUnicas = new java.util.HashSet<String>();
		if (guardias != null) {
			for (Guardia g : guardias) {
				if (g != null && g.getHorario() != null) {
					String key = g.getHorario().getDia() + "|" + g.getHorario().getHoraInicio() + "|" + g.getHorario().getHoraFin() + "|" + g.getTipo();
					if (!guardiasUnicas.contains(key)) {
						modelAsignadas.addRow(new Object[] {
							g.getHorario().getDia(),
							g.getHorario().getHoraInicio(),
							g.getHorario().getHoraFin(),
							g.getTipo()
						});
						guardiasUnicas.add(key);
					}
				}
			}
		}
		JTable tablaAsignadas = new JTable(modelAsignadas);
		tablaAsignadas.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaAsignadas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		JScrollPane scrollAsignadas = new JScrollPane(tablaAsignadas);
		scrollAsignadas.setBorder(BorderFactory.createTitledBorder("Guardias asignadas"));

		// Guardias cumplidas
		String[] columnasCumplidas = {"Fecha", "Hora Inicio", "Hora Fin", "Tipo"};
		DefaultTableModel modelCumplidas = new DefaultTableModel(columnasCumplidas, 0) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};
		// Obtener guardias cumplidas de GuardiaFactory
		List<Guardia> guardiasCumplidas = logica.PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardiasCumplidas();
		if (guardiasCumplidas != null && persona != null) {
			java.util.HashSet<String> guardiasCumplidasUnicas = new java.util.HashSet<String>();
			for (Guardia g : guardiasCumplidas) {
				if (g != null && g.getPersona() != null && g.getPersona().equals(persona) && g.getHorario() != null) {
					String key = g.getHorario().getDia() + "|" + g.getHorario().getHoraInicio() + "|" + g.getHorario().getHoraFin() + "|" + g.getTipo();
					if (!guardiasCumplidasUnicas.contains(key)) {
						modelCumplidas.addRow(new Object[] {
							g.getHorario().getDia(),
							g.getHorario().getHoraInicio(),
							g.getHorario().getHoraFin(),
							g.getTipo()
						});
						guardiasCumplidasUnicas.add(key);
					}
				}
			}
		}
		JTable tablaCumplidas = new JTable(modelCumplidas);
		tablaCumplidas.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaCumplidas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		JScrollPane scrollCumplidas = new JScrollPane(tablaCumplidas);
		scrollCumplidas.setBorder(BorderFactory.createTitledBorder("Guardias cumplidas"));

		// Guardias incumplidas (asignadas - cumplidas)
		String[] columnasIncumplidas = {"Fecha", "Hora Inicio", "Hora Fin", "Tipo"};
		DefaultTableModel modelIncumplidas = new DefaultTableModel(columnasIncumplidas, 0) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};
		if (guardias != null && persona != null) {
			java.util.HashSet<String> clavesCumplidas = new java.util.HashSet<String>();
			for (Guardia g : guardiasCumplidas) {
				if (g != null && g.getPersona() != null && g.getPersona().equals(persona) && g.getHorario() != null) {
					String key = g.getHorario().getDia() + "|" + g.getHorario().getHoraInicio() + "|" + g.getHorario().getHoraFin() + "|" + g.getTipo();
					clavesCumplidas.add(key);
				}
			}
			for (Guardia g : guardias) {
				if (g != null && g.getHorario() != null) {
					String key = g.getHorario().getDia() + "|" + g.getHorario().getHoraInicio() + "|" + g.getHorario().getHoraFin() + "|" + g.getTipo();
					if (!clavesCumplidas.contains(key)) {
						modelIncumplidas.addRow(new Object[] {
							g.getHorario().getDia(),
							g.getHorario().getHoraInicio(),
							g.getHorario().getHoraFin(),
							g.getTipo()
						});
					}
				}
			}
		}
		JTable tablaIncumplidas = new JTable(modelIncumplidas);
		tablaIncumplidas.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaIncumplidas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		JScrollPane scrollIncumplidas = new JScrollPane(tablaIncumplidas);
		scrollIncumplidas.setBorder(BorderFactory.createTitledBorder("Guardias incumplidas"));

		// Panel central con las tres tablas
		JPanel panelTablas = new JPanel();
		panelTablas.setLayout(new BoxLayout(panelTablas, BoxLayout.Y_AXIS));
		panelTablas.setBackground(amarillo);
		panelTablas.add(scrollAsignadas);
		panelTablas.add(scrollCumplidas);
		panelTablas.add(scrollIncumplidas);

		contentPane.add(panelTablas, BorderLayout.CENTER);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 15));
		btnCerrar.setBackground(negro);
		btnCerrar.setForeground(amarillo);
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnCerrar.setContentAreaFilled(false);
		btnCerrar.setOpaque(true);
		btnCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);
		panelBoton.add(btnCerrar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);
	}
}
