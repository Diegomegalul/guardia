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
	private JTable tabla; // Añadido para modo oscuro

	public static void mostrarVentana() {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new GuardiasFestivas();
			instancia.setVisible(true);
		} else {
			instancia.toFront();
			instancia.setState(JFrame.NORMAL);
		}
	}

	public GuardiasFestivas(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
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

		tabla = new JTable(model);
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

		// Aplicar modo oscuro según parámetros
		aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
	}

	// Constructor por defecto para compatibilidad (abre en modo claro)
	public GuardiasFestivas() {
		this(false, new Color(255, 215, 0), Color.BLACK, Color.BLACK, new Color(255, 215, 0));
	}

	// --- MODO OSCURO ---
	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
		if (tabla != null) {
			tabla.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			tabla.setForeground(oscuro ? Color.WHITE : texto);
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

	public JPanel getPanelPrincipal() {
		return contentPane;
	}
}
