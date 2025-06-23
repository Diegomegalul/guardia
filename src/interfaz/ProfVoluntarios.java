package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import logica.PlanificadorGuardias;
import logica.Trabajador;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfVoluntarios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProfVoluntarios frame = new ProfVoluntarios();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ProfVoluntarios() {
		setTitle("Profesores Voluntarios en Vacaciones");
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

		JLabel lblTitulo = new JLabel("Profesores Voluntarios en Vacaciones");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		// Tabla de profesores voluntarios
		String[] columnas = {"Carnet", "Nombre", "Apellidos"};
		DefaultTableModel model = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};
		final JTable tabla = new JTable(model);
		tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
		tabla.setFont(new Font("Arial", Font.PLAIN, 14));
		tabla.setRowHeight(28);

		// Obtener datos del reporte
		List<Trabajador> voluntarios = PlanificadorGuardias.getInstancia().reporteProfesoresVoluntariosEnVacaciones();
		for (int i = 0; i < voluntarios.size(); i++) {
			Trabajador t = voluntarios.get(i);
			model.addRow(new Object[] { t.getCi(), t.getNombre(), t.getApellidos() });
		}

		// Doble clic para abrir VerGuardiasPersona
		final List<Trabajador> voluntariosRef = voluntarios;
		tabla.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
					int fila = tabla.getSelectedRow();
					Trabajador trabajador = voluntariosRef.get(fila);
					VerGuardiasPersona frame = new VerGuardiasPersona(trabajador, null);
					frame.setVisible(true);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(BorderFactory.createLineBorder(amarillo, 2));
		contentPane.add(scrollPane, BorderLayout.CENTER);

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
		contentPane.add(panelBoton, BorderLayout.SOUTH);
	}
}
