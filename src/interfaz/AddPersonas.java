package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import utiles.Sexo;
import logica.Estudiante;
import logica.Persona;
import logica.PlanificadorGuardias;
import logica.Trabajador;

public class AddPersonas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel tableModel;
	private JTable table;
	private final PlanificadorGuardias planificador;

	public AddPersonas(final PlanificadorGuardias planificador) {
		this.planificador = planificador;
		setBounds(100, 100, 750, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Panel de formulario
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(0, 2, 10, 10));
		final JTextField txtCI = new JTextField();
		final JTextField txtNombre = new JTextField();
		final JComboBox<Sexo> cbSexo = new JComboBox<Sexo>();
		for (Sexo s : Sexo.values()) {
			cbSexo.addItem(s);
		}
		final JCheckBox chkActivo = new JCheckBox("Activo");
		final JTextField txtCantidadGuardias = new JTextField();
		final JComboBox<String> cbTipo = new JComboBox<String>();
		cbTipo.addItem("Estudiante");
		cbTipo.addItem("Trabajador");
		final JTextField txtGuardiasFestivo = new JTextField();
		final JTextField txtFechaIncorporacion = new JTextField();
		txtGuardiasFestivo.setEnabled(true);
		txtFechaIncorporacion.setEnabled(false);

		cbTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbTipo.getSelectedItem().equals("Estudiante")) {
					txtGuardiasFestivo.setEnabled(true);
					txtFechaIncorporacion.setEnabled(false);
				} else {
					txtGuardiasFestivo.setEnabled(false);
					txtFechaIncorporacion.setEnabled(true);
				}
			}
		});

		formPanel.add(new JLabel("CI:"));
		formPanel.add(txtCI);
		formPanel.add(new JLabel("Nombre:"));
		formPanel.add(txtNombre);
		formPanel.add(new JLabel("Sexo:"));
		formPanel.add(cbSexo);
		formPanel.add(new JLabel("Activo:"));
		formPanel.add(chkActivo);
		formPanel.add(new JLabel("Cantidad Guardias:"));
		formPanel.add(txtCantidadGuardias);
		formPanel.add(new JLabel("Tipo:"));
		formPanel.add(cbTipo);
		formPanel.add(new JLabel("Guardias Festivo (solo estudiante):"));
		formPanel.add(txtGuardiasFestivo);
		formPanel.add(new JLabel("Fecha Incorporaci\u00F3n (solo trabajador, yyyy-mm-dd):"));
		formPanel.add(txtFechaIncorporacion);

		JButton btnAgregar = new JButton("Agregar Persona");
		formPanel.add(btnAgregar);

		// Bot√≥n para editar persona seleccionada
		JButton btnEditar = new JButton("Editar Persona");
		formPanel.add(btnEditar);

		contentPane.add(formPanel, BorderLayout.NORTH);

		String[] columnas = {"CI", "Nombre", "Sexo", "Activo", "Tipo", "Guardias", "Extra"};
		tableModel = new DefaultTableModel(columnas, 0) {
			
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ci = txtCI.getText();
				if (ci == null || ci.trim().isEmpty()) return;
				// Validaci√≥n de longitud de CI
				if (ci.length() != 11 || !ci.matches("\\d{11}")) {
				    javax.swing.JOptionPane.showMessageDialog(null, "El CI debe tener exactamente 11 dÌgitos numÈricos.");
				    return;
				}
				// Evitar duplicados por CI
				for (Persona p : planificador.getPersonas()) {
					if (p.getCi().equals(ci)) {
						javax.swing.JOptionPane.showMessageDialog(null, "Ya existe una persona con ese CI.");
						return;
					}
				}
				String nombre = txtNombre.getText();
				Sexo sexo = (Sexo) cbSexo.getSelectedItem();
				boolean activo = chkActivo.isSelected();
				int cantidadGuardias = 0;
				String cantidadGuardiasStr = txtCantidadGuardias.getText();
				if (cantidadGuardiasStr != null && !cantidadGuardiasStr.trim().isEmpty()) {
					try { cantidadGuardias = Integer.parseInt(cantidadGuardiasStr); } catch (Exception ex) {}
				}
				String tipo = (String) cbTipo.getSelectedItem();
				Persona persona = null;
				if ("Estudiante".equals(tipo)) {
					int guardiasFestivo = 0;
					String guardiasFestivoStr = txtGuardiasFestivo.getText();
					if (guardiasFestivoStr != null && !guardiasFestivoStr.trim().isEmpty()) {
						try { guardiasFestivo = Integer.parseInt(guardiasFestivoStr); } catch (Exception ex) {}
					}
					planificador.crearPersona(ci, nombre, sexo, activo, cantidadGuardias, guardiasFestivo);
					tableModel.addRow(new Object[]{ci, nombre, sexo, Boolean.valueOf(activo), "Estudiante", new Integer(cantidadGuardias), new Integer(guardiasFestivo)});
				} else {
					java.time.LocalDate fecha = null;
					String fechaStr = txtFechaIncorporacion.getText();
					if (fechaStr != null && !fechaStr.trim().isEmpty()) {
						try { fecha = java.time.LocalDate.parse(fechaStr); } catch (Exception ex) {}
					}
					planificador.crearPersona(ci, nombre, sexo, activo, fecha, cantidadGuardias);
					tableModel.addRow(new Object[]{ci, nombre, sexo, Boolean.valueOf(activo), "Trabajador", new Integer(cantidadGuardias), fecha});
				}
			}
		});

		// Cargar personas existentes en la tabla
		for (Persona p : planificador.getPersonas()) {
			String tipo = (p instanceof Estudiante) ? "Estudiante" : "Trabajador";
			Object extra = (tipo.equals("Estudiante")) ? ((Estudiante)p).getCantidadGuardiasFestivo() : ((Trabajador)p).getFechaDeIncorporacion();
			tableModel.addRow(new Object[]{
				p.getCi(),
				p.getNombre(),
				p.getSexo(),
				Boolean.valueOf(p.getActivo()),
				tipo,
				new Integer(p.getCantidadGuardias()),
				extra
			});
		}

		// Selecci√≥n de fila para editar
		final int[] selectedRow = { -1 };
		table.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedRow[0] = table.getSelectedRow();
					if (selectedRow[0] != -1) {
						txtCI.setText((String) tableModel.getValueAt(selectedRow[0], 0));
						txtNombre.setText((String) tableModel.getValueAt(selectedRow[0], 1));
						cbSexo.setSelectedItem(tableModel.getValueAt(selectedRow[0], 2));
						chkActivo.setSelected(Boolean.valueOf(tableModel.getValueAt(selectedRow[0], 3).toString()));
						cbTipo.setSelectedItem(tableModel.getValueAt(selectedRow[0], 4));
						txtCantidadGuardias.setText(tableModel.getValueAt(selectedRow[0], 5).toString());
						if ("Estudiante".equals(tableModel.getValueAt(selectedRow[0], 4))) {
							txtGuardiasFestivo.setText(tableModel.getValueAt(selectedRow[0], 6).toString());
							txtFechaIncorporacion.setText("");
						} else {
							txtFechaIncorporacion.setText(tableModel.getValueAt(selectedRow[0], 6).toString());
							txtGuardiasFestivo.setText("");
						}
					}
				}
			}
		});

		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRow[0] == -1) return;
				String ci = txtCI.getText();
				String nombre = txtNombre.getText();
				Sexo sexo = (Sexo) cbSexo.getSelectedItem();
				boolean activo = chkActivo.isSelected();
				int cantidadGuardias = 0;
				String cantidadGuardiasStr = txtCantidadGuardias.getText();
				if (cantidadGuardiasStr != null && !cantidadGuardiasStr.trim().isEmpty()) {
					try { cantidadGuardias = Integer.parseInt(cantidadGuardiasStr); } catch (Exception ex) {}
				}
				String tipo = (String) cbTipo.getSelectedItem();
				Object extra = "Estudiante".equals(tipo) ? txtGuardiasFestivo.getText() : txtFechaIncorporacion.getText();
				tableModel.setValueAt(ci, selectedRow[0], 0);
				tableModel.setValueAt(nombre, selectedRow[0], 1);
				tableModel.setValueAt(sexo, selectedRow[0], 2);
				tableModel.setValueAt(Boolean.valueOf(activo), selectedRow[0], 3);
				tableModel.setValueAt(tipo, selectedRow[0], 4);
				tableModel.setValueAt(new Integer(cantidadGuardias), selectedRow[0], 5);
				tableModel.setValueAt(extra, selectedRow[0], 6);
				// Actualizar en planificador
				Persona persona = null;
				if ("Estudiante".equals(tipo)) {
					int guardiasFestivo = 0;
					String guardiasFestivoStr = txtGuardiasFestivo.getText();
					if (guardiasFestivoStr != null && !guardiasFestivoStr.trim().isEmpty()) {
						try { guardiasFestivo = Integer.parseInt(guardiasFestivoStr); } catch (Exception ex) {}
					}
					persona = new Estudiante(ci, nombre, sexo, activo, cantidadGuardias, guardiasFestivo);
				} else {
					LocalDate fecha = null;
					String fechaStr = txtFechaIncorporacion.getText();
					if (fechaStr != null && !fechaStr.trim().isEmpty()) {
						try { fecha = LocalDate.parse(fechaStr); } catch (Exception ex) {}
					}
					persona = new Trabajador(ci, nombre, sexo, activo, fecha, cantidadGuardias);
				}
				planificador.getPersonas().set(selectedRow[0], persona);
			}
		});
	}

}
