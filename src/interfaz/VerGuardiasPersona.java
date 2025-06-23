// :)
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
import logica.Trabajador;

public class VerGuardiasPersona extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public VerGuardiasPersona(Persona persona, List<Guardia> guardias) {
		setTitle("Guardias de " + (persona != null ? persona.getNombre() + " " + persona.getApellidos() : ""));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(null); // Centrar en pantalla

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

		logica.GuardiaFactory.GuardiasPorPersona guardiasPorPersona =
			logica.PlanificadorGuardias.getInstancia().getGuardiaFactory().obtenerGuardiasPorPersona(persona);

		List<Guardia> asignadas = guardiasPorPersona.asignadas;
		List<Guardia> cumplidas = guardiasPorPersona.cumplidas;
		List<Guardia> incumplidas = guardiasPorPersona.incumplidas;

		String[] columnas = {"Fecha", "Hora Inicio", "Hora Fin", "Tipo"};
		DefaultTableModel modelAsignadas = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};
		for (int i = 0; i < asignadas.size(); i++) {
			Guardia g = asignadas.get(i);
			if (g != null && g.getHorario() != null) {
				modelAsignadas.addRow(new Object[] {
					g.getHorario().getDia(),
					g.getHorario().getHoraInicio(),
					g.getHorario().getHoraFin(),
					g.getTipo()
				});
			}
		}
		JTable tablaAsignadas = new JTable(modelAsignadas);
		tablaAsignadas.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaAsignadas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		JScrollPane scrollAsignadas = new JScrollPane(tablaAsignadas);
		scrollAsignadas.setBorder(BorderFactory.createTitledBorder("Guardias asignadas (" + asignadas.size() + ")"));

		// Solo mostrar tabla de asignadas si es Trabajador, si no mostrar las tres
		JPanel panelTablas = new JPanel();
		panelTablas.setLayout(new BoxLayout(panelTablas, BoxLayout.Y_AXIS));
		panelTablas.setBackground(amarillo);

		if (persona instanceof Trabajador) {
			panelTablas.add(scrollAsignadas);
		} else {
			// Tabla de guardias cumplidas
			DefaultTableModel modelCumplidas = new DefaultTableModel(columnas, 0) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) { return false; }
			};
			for (int i = 0; i < cumplidas.size(); i++) {
				Guardia g = cumplidas.get(i);
				if (g != null && g.getHorario() != null) {
					modelCumplidas.addRow(new Object[] {
						g.getHorario().getDia(),
						g.getHorario().getHoraInicio(),
						g.getHorario().getHoraFin(),
						g.getTipo()
					});
				}
			}
			JTable tablaCumplidas = new JTable(modelCumplidas);
			tablaCumplidas.setFont(new Font("Arial", Font.PLAIN, 14));
			tablaCumplidas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
			JScrollPane scrollCumplidas = new JScrollPane(tablaCumplidas);
			scrollCumplidas.setBorder(BorderFactory.createTitledBorder("Guardias cumplidas (" + cumplidas.size() + ")"));

			// Tabla de guardias incumplidas
			DefaultTableModel modelIncumplidas = new DefaultTableModel(columnas, 0) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) { return false; }
			};
			for (int i = 0; i < incumplidas.size(); i++) {
				Guardia g = incumplidas.get(i);
				if (g != null && g.getHorario() != null) {
					modelIncumplidas.addRow(new Object[] {
						g.getHorario().getDia(),
						g.getHorario().getHoraInicio(),
						g.getHorario().getHoraFin(),
						g.getTipo()
					});
				}
			}
			JTable tablaIncumplidas = new JTable(modelIncumplidas);
			tablaIncumplidas.setFont(new Font("Arial", Font.PLAIN, 14));
			tablaIncumplidas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
			JScrollPane scrollIncumplidas = new JScrollPane(tablaIncumplidas);
			scrollIncumplidas.setBorder(BorderFactory.createTitledBorder("Guardias incumplidas (" + incumplidas.size() + ")"));

			panelTablas.add(scrollAsignadas);
			panelTablas.add(scrollCumplidas);
			panelTablas.add(scrollIncumplidas);
		}

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
