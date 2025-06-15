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

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuardiasCumplidas frame = new GuardiasCumplidas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GuardiasCumplidas() {
		this.planificador = PlanificadorGuardias.getInstancia();
		setTitle("Guardias Cumplidas");
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
		String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
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

		String[] columnas = {"ID", "Tipo", "Persona", "CI", "Fecha", "Hora Inicio", "Hora Fin"};
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
}
