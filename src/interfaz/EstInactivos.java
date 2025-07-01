package interfaz;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.PlanificadorGuardias;
import logica.PlanificadorGuardias.ReporteInactivos;
import logica.PlanificadorGuardias.GrupoInactivos;
import logica.Estudiante;

public class EstInactivos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static EstInactivos instancia = null;

	public static void mostrarVentana() {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new EstInactivos();
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

	public EstInactivos(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setTitle("Estudiantes Inactivos por Grupo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		setLocationRelativeTo(null);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Estudiantes Inactivos por Grupo");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setForeground(negro);
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitulo.setBorder(new EmptyBorder(10, 0, 20, 0));
		contentPane.add(lblTitulo);

		ReporteInactivos reporte = PlanificadorGuardias.getInstancia().reportePersonasInactivas();
		List<GrupoInactivos> grupos = reporte.gruposEstudiantesInactivos;

		if (grupos.isEmpty()) {
			JLabel lblSinDatos = new JLabel("No hay estudiantes inactivos.");
			lblSinDatos.setFont(new Font("Arial", Font.ITALIC, 16));
			lblSinDatos.setForeground(Color.DARK_GRAY);
			lblSinDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPane.add(lblSinDatos);
		} else {
			boolean hayEstudiantes = false;
			for (GrupoInactivos grupo : grupos) {
				if (grupo.estudiantesInactivos != null && !grupo.estudiantesInactivos.isEmpty()) {
					hayEstudiantes = true;
					JLabel lblGrupo = new JLabel("Grupo " + grupo.grupo + " (Total: " + grupo.cantidadInactivos + ")");
					lblGrupo.setFont(new Font("Arial", Font.BOLD, 18));
					lblGrupo.setForeground(negro);
					lblGrupo.setAlignmentX(Component.LEFT_ALIGNMENT);
					lblGrupo.setBorder(new EmptyBorder(10, 0, 5, 0));
					contentPane.add(lblGrupo);

					String[] columnas = { "CI", "Nombre", "Apellidos", "Sexo" };
					DefaultTableModel model = new DefaultTableModel(columnas, 0) {
						private static final long serialVersionUID = 1L;

						public boolean isCellEditable(int row, int col) {
							return false;
						}
					};
					for (Estudiante e : grupo.estudiantesInactivos) {
						model.addRow(new Object[] {
								e.getCi(),
								e.getNombre(),
								e.getApellidos(),
								e.getSexo()
						});
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
			if (!hayEstudiantes) {
				JLabel lblSinDatos = new JLabel("No hay estudiantes inactivos.");
				lblSinDatos.setFont(new Font("Arial", Font.ITALIC, 16));
				lblSinDatos.setForeground(Color.DARK_GRAY);
				lblSinDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
				contentPane.add(lblSinDatos);
			}
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

		// Aplicar modo oscuro según parámetros
		aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
	}

	// Constructor por defecto para compatibilidad (abre en modo claro)
	public EstInactivos() {
		this(false, new Color(255, 215, 0), Color.BLACK, Color.BLACK, new Color(255, 215, 0));
	}

	// --- MODO OSCURO ---
	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
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
