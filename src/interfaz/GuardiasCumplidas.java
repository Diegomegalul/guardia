package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import logica.Guardia;
import logica.PlanificadorGuardias;

public class GuardiasCumplidas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaGuardias;
	private DefaultTableModel tablaModel;
	private JComboBox<String> comboMes;
	private JComboBox<String> comboAnio;
	private PlanificadorGuardias planificador;

	private static GuardiasCumplidas instancia = null;

	public static void mostrarVentana() {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new GuardiasCumplidas();
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

	public GuardiasCumplidas() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		this.planificador = PlanificadorGuardias.getInstancia();
		setTitle("Guardias Cumplidas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		contentPane.setBackground(new Color(255, 215, 0)); // amarillo por defecto
		setContentPane(contentPane);

		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelSuperior.setBackground(new Color(255, 215, 0));

		JLabel lblMes = new JLabel("Mes:");
		panelSuperior.add(lblMes);

		comboMes = new JComboBox<String>();
		String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
				"Octubre", "Noviembre", "Diciembre" };
		for (int i = 0; i < meses.length; i++) {
			comboMes.addItem(meses[i]);
		}
		panelSuperior.add(comboMes);

		JLabel lblAnio = new JLabel("Año:");
		panelSuperior.add(lblAnio);

		comboAnio = new JComboBox<String>();
		int anioActual = java.time.Year.now().getValue();
		for (int i = anioActual - 5; i <= anioActual + 5; i++) {
			comboAnio.addItem(String.valueOf(i));
		}
		comboAnio.setSelectedItem(String.valueOf(anioActual));
		panelSuperior.add(comboAnio);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarGuardiasEnTabla();
			}
		});
		panelSuperior.add(btnActualizar);

		contentPane.add(panelSuperior, BorderLayout.NORTH);

		panelSuperior.add(Box.createHorizontalStrut(180));

		JLabel GuardiasCumplidas = new JLabel("Guardias Cumplidas");
		GuardiasCumplidas.setFont(new Font("Arial", Font.BOLD, 16));
		panelSuperior.add(GuardiasCumplidas);

		String[] columnas = { "ID", "Tipo", "Persona", "CI", "Fecha", "Hora Inicio", "Hora Fin" };
		tablaModel = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public Class<?> getColumnClass(int columnIndex) {
				return String.class;
			}
		};
		tablaGuardias = new JTable(tablaModel);
		tablaGuardias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tablaGuardias.setFont(new Font("Consolas", Font.PLAIN, 13));
		tablaGuardias.setRowHeight(24);

		JScrollPane scroll = new JScrollPane(tablaGuardias);
		contentPane.add(scroll, BorderLayout.CENTER);

		// Cargar guardias al abrir
		cargarGuardiasEnTabla();

		// Cambiar mes/año recarga la tabla
		comboMes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarGuardiasEnTabla();
			}
		});
		comboAnio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarGuardiasEnTabla();
			}
		});

		// Seleccionar mes y año por defecto: mes n-2 del último mes planificado
		java.time.LocalDate ultimoPlan = PlanificadorGuardias.getInstancia().getGuardiaFactory()
				.getUltimoMesPlanificado();
		if (ultimoPlan != null) {
			java.time.LocalDate mesDosAntes = ultimoPlan.minusMonths(2);
			comboMes.setSelectedIndex(mesDosAntes.getMonthValue() - 1);
			comboAnio.setSelectedItem(String.valueOf(mesDosAntes.getYear()));
		}
	}

	private void cargarGuardiasEnTabla() {
		tablaModel.setRowCount(0);
		int mes = comboMes.getSelectedIndex() + 1;
		int anio = Integer.parseInt((String) comboAnio.getSelectedItem());
		List<?> cumplidas = planificador.getGuardiaFactory().getGuardiasCumplidas();
		for (int i = 0; i < cumplidas.size(); i++) {
			Guardia g = (Guardia) cumplidas.get(i);
			LocalDate fecha = g.getHorario().getDia();
			if (fecha.getMonthValue() == mes && fecha.getYear() == anio) {
				tablaModel.addRow(new Object[] {
						g.getId(),
						g.getTipo(),
						g.getPersona().getNombre() + " " + g.getPersona().getApellidos(),
						g.getPersona().getCi(),
						g.getHorario().getDia(),
						g.getHorario().getHoraInicio(),
						g.getHorario().getHoraFin()
				});
			}
		}
	}

	// --- MODO OSCURO ---
	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
		if (tablaGuardias != null) {
			tablaGuardias.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			tablaGuardias.setForeground(oscuro ? Color.WHITE : texto);
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
