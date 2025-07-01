package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.PlanificadorGuardias;
import logica.Trabajador;

public class ProfVoluntarios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static ProfVoluntarios instancia = null;

	public static void mostrarVentana() {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new ProfVoluntarios();
			instancia.setVisible(true);
		} else {
			instancia.toFront();
			instancia.setState(JFrame.NORMAL);
		}
	}

	// Nuevo constructor con modo oscuro
	public ProfVoluntarios(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
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
		String[] columnas = { "Carnet", "Nombre", "Apellidos" };
		DefaultTableModel model = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
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

		// Aplicar modo oscuro según parámetros
		aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
	}

	// Constructor por defecto para compatibilidad (abre en modo claro)
	public ProfVoluntarios() {
		this(false, new Color(255, 215, 0), Color.BLACK, Color.BLACK, new Color(255, 215, 0));
	}

	// --- MODO OSCURO ---
	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		if (getContentPane() instanceof JPanel) {
			JPanel contentPane = (JPanel) getContentPane();
			contentPane.setBackground(fondo);
			setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
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
