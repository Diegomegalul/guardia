// :)
package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.Guardia;
import logica.Persona;

public class VerGuardiasPersona extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public VerGuardiasPersona(Persona persona, List<Guardia> guardias) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
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
						"</html>");
		lblDatos.setFont(new Font("Arial", Font.PLAIN, 15));
		lblDatos.setForeground(negro);
		lblDatos.setBorder(new EmptyBorder(10, 0, 10, 0));
		contentPane.add(lblDatos, BorderLayout.NORTH);

		logica.GuardiaFactory.GuardiasPorPersona guardiasPorPersona = logica.PlanificadorGuardias.getInstancia()
				.getGuardiaFactory().obtenerGuardiasPorPersona(persona);

		List<Guardia> planificadas = guardiasPorPersona.asignadas;
		List<Guardia> cumplidas = guardiasPorPersona.cumplidas;
		List<Guardia> incumplidas = guardiasPorPersona.incumplidas;

		String[] columnas = { "Fecha", "Hora Inicio", "Hora Fin", "Tipo" };
		DefaultTableModel modelPlanificadas = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		for (Guardia g : planificadas) {
			if (g != null && g.getHorario() != null
					&& (g.getTipo() == utiles.TipoGuardia.NORMAL || g.getTipo() == utiles.TipoGuardia.FESTIVO)) {
				modelPlanificadas.addRow(new Object[] {
						g.getHorario().getDia(),
						g.getHorario().getHoraInicio(),
						g.getHorario().getHoraFin(),
						g.getTipo()
				});
			}
		}
		JTable tablaPlanificadas = new JTable(modelPlanificadas);
		tablaPlanificadas.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaPlanificadas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		JScrollPane scrollPlanificadas = new JScrollPane(tablaPlanificadas);
		scrollPlanificadas
				.setBorder(BorderFactory.createTitledBorder("Guardias planificadas (" + planificadas.size() + ")"));

		DefaultTableModel modelCumplidas = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		for (Guardia g : cumplidas) {
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

		DefaultTableModel modelIncumplidas = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		for (Guardia g : incumplidas) {
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
		scrollIncumplidas
				.setBorder(BorderFactory.createTitledBorder("Guardias incumplidas (" + incumplidas.size() + ")"));

		JPanel panelTablas = new JPanel();
		panelTablas.setLayout(new BoxLayout(panelTablas, BoxLayout.Y_AXIS));
		panelTablas.setBackground(amarillo);

		panelTablas.add(scrollPlanificadas);
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
