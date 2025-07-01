package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import logica.Guardia;
import logica.PlanificadorGuardias;

public class GuardiasPlanificadas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaGuardias;
	private DefaultTableModel tablaModel;
	private JComboBox<String> comboMes;
	private JComboBox<String> comboAnio;
	private PlanificadorGuardias planificador;

	private static GuardiasPlanificadas instancia = null;

	public static void mostrarVentana() {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new GuardiasPlanificadas();
			instancia.setVisible(true);
		} else {
			instancia.toFront();
			instancia.setState(JFrame.NORMAL);
		}
	}

	public GuardiasPlanificadas() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		this.planificador = PlanificadorGuardias.getInstancia();
		setTitle("Guardias Planificadas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
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

		JLabel lblGuardiasPlanificadas = new JLabel("Guardias Planificadas");
		lblGuardiasPlanificadas.setFont(new Font("Arial", Font.BOLD, 16));
		lblGuardiasPlanificadas.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelSuperior.add(lblGuardiasPlanificadas);

		String[] columnas = { "ID", "Tipo", "Persona", "CI", "Fecha", "Hora Inicio", "Hora Fin", "Cumplida" };
		tablaModel = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col) {
				return col == 7;
			}

			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 7)
					return Boolean.class;
				return String.class;
			}
		};
		tablaGuardias = new JTable(tablaModel);
		tablaGuardias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tablaGuardias.setFont(new Font("Consolas", Font.PLAIN, 13));
		tablaGuardias.setRowHeight(24);

		// Listener para marcar cumplidas
		tablaGuardias.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 7) {
					int row = e.getFirstRow();
					Object val = tablaModel.getValueAt(row, 7);
					Boolean checked = Boolean.FALSE;
					if (val instanceof Boolean) {
						checked = (Boolean) val;
					}
					if (checked != null && checked.booleanValue()) {
						int id = Integer.parseInt(tablaModel.getValueAt(row, 0).toString());
						PlanificadorGuardias.getInstancia().getGuardiaFactory().registrarCumplimientoGuardia(id);
						tablaModel.removeRow(row);
					}
				}
			}
		});

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

		// Seleccionar mes y año por defecto: mes n-1 del último mes planificado
		java.time.LocalDate ultimoPlan = PlanificadorGuardias.getInstancia().getGuardiaFactory()
				.getUltimoMesPlanificado();
		if (ultimoPlan != null) {
			java.time.LocalDate mesAnterior = ultimoPlan.minusMonths(1);
			comboMes.setSelectedIndex(mesAnterior.getMonthValue() - 1);
			comboAnio.setSelectedItem(String.valueOf(mesAnterior.getYear()));
		}
	}

	private void cargarGuardiasEnTabla() {
		tablaModel.setRowCount(0);
		int mes = comboMes.getSelectedIndex() + 1;
		int anio = Integer.parseInt((String) comboAnio.getSelectedItem());
		List<?> guardias = planificador.getGuardiaFactory().getGuardias();
		List<?> cumplidas = planificador.getGuardiaFactory().getGuardiasCumplidas();
		for (int i = 0; i < guardias.size(); i++) {
			Guardia g = (Guardia) guardias.get(i);
			LocalDate fecha = g.getHorario().getDia();
			if (fecha.getMonthValue() == mes && fecha.getYear() == anio) {
				// No mostrar si ya está en cumplidas
				boolean yaCumplida = false;
				for (int j = 0; j < cumplidas.size(); j++) {
					Guardia c = (Guardia) cumplidas.get(j);
					if (c.getId() == g.getId()) {
						yaCumplida = true;
						break;
					}
				}
				if (!yaCumplida) {
					tablaModel.addRow(new Object[] {
							g.getId(),
							g.getTipo(),
							g.getPersona().getNombre() + " " + g.getPersona().getApellidos(),
							g.getPersona().getCi(),
							g.getHorario().getDia(),
							g.getHorario().getHoraInicio(),
							g.getHorario().getHoraFin(),
							Boolean.FALSE
					});
				}
			}
		}
	}

	public JPanel getPanelPrincipal() {
		return contentPane;
	}

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
}
