package interfaz;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import logica.PlanificadorGuardias;
import logica.PlanificadorGuardias.GrupoRecuperacionOrdenado;
import logica.Estudiante;

public class GuardiasRecuperacion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuardiasRecuperacion frame = new GuardiasRecuperacion();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GuardiasRecuperacion() {
		setTitle("Guardias de Recuperación por Grupo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null); // Centrar en pantalla

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Guardias de Recuperación por Grupo");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setForeground(negro);
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitulo.setBorder(new EmptyBorder(10, 0, 20, 0));
		contentPane.add(lblTitulo);

		PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();
		List<GrupoRecuperacionOrdenado> grupos = planificador.estudiantesPorGrupoRecuperacionDesc();

		boolean hayGrupos = false;
		for (int i = 0; i < grupos.size(); i++) {
			GrupoRecuperacionOrdenado grupo = grupos.get(i);
			if (grupo.totalRecuperacion > 0) {
				hayGrupos = true;
				JLabel lblGrupo = new JLabel("Grupo " + grupo.grupo + " (Total: " + grupo.totalRecuperacion + ")");
				lblGrupo.setFont(new Font("Arial", Font.BOLD, 18));
				lblGrupo.setForeground(negro);
				lblGrupo.setAlignmentX(Component.LEFT_ALIGNMENT);
				lblGrupo.setBorder(new EmptyBorder(10, 0, 5, 0));
				contentPane.add(lblGrupo);

				String[] columnas = {"CI", "Nombre", "Apellidos", "Guardias Recuperación"};
				DefaultTableModel model = new DefaultTableModel(columnas, 0) {
					private static final long serialVersionUID = 1L;
					public boolean isCellEditable(int row, int col) { return false; }
				};
				for (int j = 0; j < grupo.estudiantes.size(); j++) {
					Estudiante e = grupo.estudiantes.get(j);
					if (e.getGuardiasRecuperacion() > 0) {
						model.addRow(new Object[] {
							e.getCi(),
							e.getNombre(),
							e.getApellidos(),
							e.getGuardiasRecuperacion()
						});
					}
				}
				JTable tabla = new JTable(model);
				tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
				tabla.setFont(new Font("Arial", Font.PLAIN, 14));
				tabla.setRowHeight(24);
				JScrollPane scroll = new JScrollPane(tabla);
				scroll.setBorder(BorderFactory.createLineBorder(amarillo, 2));
				scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
				contentPane.add(scroll);
			}
		}

		if (!hayGrupos) {
			JLabel lblSinDatos = new JLabel("No hay grupos con guardias de recuperación.");
			lblSinDatos.setFont(new Font("Arial", Font.ITALIC, 16));
			lblSinDatos.setForeground(Color.DARK_GRAY);
			lblSinDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPane.add(lblSinDatos);
		}

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 15));
		btnCerrar.setBackground(negro);
		btnCerrar.setForeground(amarillo);
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnCerrar.setContentAreaFilled(false);
		btnCerrar.setOpaque(true);
		btnCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				dispose();
			}
		});
		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);
		panelBoton.add(btnCerrar);
		contentPane.add(Box.createVerticalStrut(10));
		contentPane.add(panelBoton);
	}
}
