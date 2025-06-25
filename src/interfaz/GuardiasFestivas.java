package interfaz;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.PlanificadorGuardias;
import logica.Guardia;

public class GuardiasFestivas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static GuardiasFestivas instancia = null;

	public static void mostrarVentana() {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new GuardiasFestivas();
			instancia.setVisible(true);
		} else {
			instancia.toFront();
			instancia.setState(JFrame.NORMAL);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				mostrarVentana();
			}
		});
	}

	public GuardiasFestivas() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setTitle("Guardias en Días Festivos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(null);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Guardias en Días Festivos");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		String[] columnas = { "CI", "Nombre", "Apellidos", "Fecha", "Hora Inicio", "Hora Fin", "Tipo" };
		DefaultTableModel model = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		List<Guardia> guardiasFestivas = PlanificadorGuardias.getInstancia().listaGuardiasEnDiasFestivos();
		for (Guardia g : guardiasFestivas) {
			if (g.getPersona() != null && g.getHorario() != null) {
				utiles.TipoGuardia tipo = g.getTipo();
				if (tipo == utiles.TipoGuardia.FESTIVO || tipo == utiles.TipoGuardia.VOLUNTARIA_FESTIVO
						|| tipo == utiles.TipoGuardia.RECUPERACION_FESTIVO) {
					model.addRow(new Object[] {
							g.getPersona().getCi(),
							g.getPersona().getNombre(),
							g.getPersona().getApellidos(),
							g.getHorario().getDia(),
							g.getHorario().getHoraInicio(),
							g.getHorario().getHoraFin(),
							g.getTipo()
					});
				}
			}
		}

		JTable tabla = new JTable(model);
		tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
		tabla.setFont(new Font("Arial", Font.PLAIN, 14));
		tabla.setRowHeight(28);

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
