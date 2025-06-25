package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.Guardia;
import logica.PlanificadorGuardias;

public class GuardiasIncumplidas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable tablaGuardias;
	private DefaultTableModel tablaModel;
	private JComboBox<String> comboMes;
	private JComboBox<String> comboAnio;
	private PlanificadorGuardias planificador;

	public GuardiasIncumplidas() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		this.planificador = PlanificadorGuardias.getInstancia();
		setTitle("Guardias Incumplidas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
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
		List<?> incumplidas = planificador.getGuardiaFactory().getGuardiasIncumplidas();
		for (int i = 0; i < incumplidas.size(); i++) {
			Guardia g = (Guardia) incumplidas.get(i);
			java.time.LocalDate fecha = g.getHorario().getDia();
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
