package interfaz;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import logica.Guardia;
import logica.Persona;
import logica.PlanificadorGuardias;

public class GuardiasIncumplidas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuardiasIncumplidas frame = new GuardiasIncumplidas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GuardiasIncumplidas() {
		setTitle("Guardias Incumplidas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(null);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setBackground(amarillo);
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Guardias Incumplidas");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		String[] columnas = {"CI", "Nombre", "Apellidos", "Fecha", "Hora Inicio", "Hora Fin", "Tipo"};
		DefaultTableModel model = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};

		final List<Guardia> guardiasIncumplidas = PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardiasIncumplidas();
		for (Guardia g : guardiasIncumplidas) {
			Persona p = g.getPersona();
			if (p != null && g.getHorario() != null) {
				model.addRow(new Object[] {
					p.getCi(),
					p.getNombre(),
					p.getApellidos(),
					g.getHorario().getDia(),
					g.getHorario().getHoraInicio(),
					g.getHorario().getHoraFin(),
					g.getTipo()
				});
			}
		}

		final JTable tabla = new JTable(model);
		tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
		tabla.setFont(new Font("Arial", Font.PLAIN, 14));
		tabla.setRowHeight(28);

		// Doble clic para abrir VerGuardiasPersona
		tabla.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
					int fila = tabla.getSelectedRow();
					Guardia g = guardiasIncumplidas.get(tabla.convertRowIndexToModel(fila));
					Persona persona = g.getPersona();
					if (persona != null) {
						VerGuardiasPersona frame = new VerGuardiasPersona(persona, null);
						frame.setVisible(true);
					}
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
