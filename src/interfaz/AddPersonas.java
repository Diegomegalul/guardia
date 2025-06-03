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

import com.toedter.calendar.JDateChooser;
import utiles.Sexo;
import logica.Estudiante;
import logica.Facultad;
import logica.Persona;
import logica.PlanificadorGuardias;
import logica.Trabajador;

public class AddPersonas extends JFrame {

	private static final long serialVersionUID = 1L;
	private PlanificadorGuardias planificador;
	private JPanel contentPane;
	private DefaultTableModel tableModel;
	private JTable table;
	private Facultad facultad;

	public AddPersonas(final PlanificadorGuardias planificador) {
		this.setPlanificador(planificador);
		this.facultad = planificador.getFacultad(); // Acceso a la facultad desde el planificador
		setBounds(100, 100, 750, 600);
		setLocationRelativeTo(null); // Centra la ventana en la pantalla
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
		// Reemplaza el JTextField por JDateChooser para la fecha de incorporación
		final JDateChooser dateChooserFechaIncorporacion = new JDateChooser();
		dateChooserFechaIncorporacion.setEnabled(false);
		final JTextField txtGrupo = new JTextField();

		txtGuardiasFestivo.setEnabled(true);

		cbTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbTipo.getSelectedItem().equals("Estudiante")) {
					txtGuardiasFestivo.setEnabled(true); // Siempre habilitado
					dateChooserFechaIncorporacion.setEnabled(false);
					txtGrupo.setEnabled(true);
				} else {
					txtGuardiasFestivo.setEnabled(true); // Siempre habilitado
					dateChooserFechaIncorporacion.setEnabled(!chkActivo.isSelected());
					txtGrupo.setEnabled(false);
					txtGrupo.setText("");
				}
			}
		});

		chkActivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbTipo.getSelectedItem().equals("Trabajador")) {
					dateChooserFechaIncorporacion.setEnabled(!chkActivo.isSelected());
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
		formPanel.add(new JLabel("Guardias Festivo:"));
		formPanel.add(txtGuardiasFestivo);
		formPanel.add(new JLabel("Tipo:"));
		formPanel.add(cbTipo);
		formPanel.add(new JLabel("Fecha Incorporaci\u00F3n (solo trabajador, usar calendario):"));
		formPanel.add(dateChooserFechaIncorporacion);
		formPanel.add(new JLabel("Grupo (solo estudiante):"));
		formPanel.add(txtGrupo);

		JButton btnAgregar = new JButton("Agregar Persona");
		formPanel.add(btnAgregar);

		// Botón para editar persona seleccionada
		JButton btnEditar = new JButton("Editar Persona");
		formPanel.add(btnEditar);

		// Botón para eliminar persona seleccionada
		JButton btnEliminar = new JButton("Eliminar Persona");
		formPanel.add(btnEliminar);

		// Botón para actualizar la tabla
		JButton btnActualizar = new JButton("Actualizar");
		formPanel.add(btnActualizar);

		contentPane.add(formPanel, BorderLayout.NORTH);

		String[] columnas = {"CI", "Nombre", "Sexo", "Activo", "Tipo", "Guardias", "Extra", "Grupo"};
		tableModel = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// Método para actualizar y ordenar la tabla
		final ActionListener actualizarTabla = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				java.util.List<Object[]> varones = new java.util.ArrayList<Object[]>();
				java.util.List<Object[]> hembras = new java.util.ArrayList<Object[]>();
				java.util.List<Object[]> profesores = new java.util.ArrayList<Object[]>();
				for (Persona p : facultad.getPersonas()) {
					String tipo = (p instanceof Estudiante) ? "Estudiante" : "Trabajador";
					Object extra = (tipo.equals("Estudiante")) ? ((Estudiante)p).getCantidadGuardiasFestivo() : ((Trabajador)p).getFechaDeIncorporacion();
					int grupo = (p instanceof Estudiante) ? ((Estudiante)p).getGrupo() : 0;
					Object[] row = new Object[]{
						p.getCi(),
						p.getNombre(),
						p.getSexo(),
						Boolean.valueOf(p.getActivo()),
						tipo,
						new Integer(p.getCantidadGuardias()),
						extra,
						grupo
					};
					if (p instanceof Estudiante) {
						if (((Estudiante)p).getSexo() == utiles.Sexo.MASCULINO) {
							varones.add(row);
						} else {
							hembras.add(row);
						}
					} else {
						profesores.add(row);
					}
				}
				for (Object[] row : varones) tableModel.addRow(row);
				for (Object[] row : hembras) tableModel.addRow(row);
				for (Object[] row : profesores) tableModel.addRow(row);
			}
		};

		// Llenar la tabla al inicio ordenada
		actualizarTabla.actionPerformed(null);

		btnActualizar.addActionListener(actualizarTabla);

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ci = txtCI.getText();
				if (ci == null || ci.trim().isEmpty()) return;
				// Validación de longitud de CI
				if (ci.length() != 11 || !ci.matches("\\d{11}")) {
				    javax.swing.JOptionPane.showMessageDialog(null, "El CI debe tener exactamente 11 digitos numéricos.");
				    return;
				}
				// Evitar duplicados por CI
				if (facultad.buscarPersonaPorCi(ci) != null) {
					javax.swing.JOptionPane.showMessageDialog(null, "Ya existe una persona con ese CI.");
					return;
				}
				String nombre = txtNombre.getText();
				Sexo sexo = (Sexo) cbSexo.getSelectedItem();
				boolean activo = chkActivo.isSelected();
				int cantidadGuardias = 0;
				String cantidadGuardiasStr = txtCantidadGuardias.getText();
				if (cantidadGuardiasStr == null || cantidadGuardiasStr.trim().isEmpty()) {
					cantidadGuardias = 0;
				} else {
					try { 
						cantidadGuardias = Integer.parseInt(cantidadGuardiasStr); 
						if (cantidadGuardias < 0) {
							javax.swing.JOptionPane.showMessageDialog(null, "La cantidad de guardias no puede ser menor que 0.");
							return;
						}
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(null, "La cantidad de guardias debe ser un número válido.");
						return;
					}
				}
				String tipo = (String) cbTipo.getSelectedItem();
				int grupo = 0;
				String grupoStr = txtGrupo.getText();
				if (grupoStr != null && !grupoStr.trim().isEmpty()) {
					try { grupo = Integer.parseInt(grupoStr); } catch (Exception ex) {}
				}
				if ("Estudiante".equals(tipo)) {
					int guardiasFestivo = 0;
					String guardiasFestivoStr = txtGuardiasFestivo.getText();
					if (guardiasFestivoStr != null && !guardiasFestivoStr.trim().isEmpty()) {
						try { guardiasFestivo = Integer.parseInt(guardiasFestivoStr); } catch (Exception ex) {}
					}
					Estudiante estudiante = new Estudiante(ci, nombre, sexo, activo, cantidadGuardias, guardiasFestivo, grupo);
					facultad.agregarPersona(estudiante);
					tableModel.addRow(new Object[]{ci, nombre, sexo, Boolean.valueOf(activo), "Estudiante", new Integer(cantidadGuardias), new Integer(guardiasFestivo), grupo});
				} else {
					java.time.LocalDate fecha = null;
					java.util.Date fechaDate = dateChooserFechaIncorporacion.getDate();
					// Solo tomar la fecha si el trabajador NO está activo
					if (!chkActivo.isSelected() && fechaDate != null) {
						fecha = fechaDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
					}
					Trabajador trabajador = new Trabajador(ci, nombre, sexo, activo, fecha, cantidadGuardias, grupo);
					facultad.agregarPersona(trabajador);
					tableModel.addRow(new Object[]{ci, nombre, sexo, Boolean.valueOf(activo), "Trabajador", new Integer(cantidadGuardias), fecha, grupo});
				}
				// Vaciar campos después de agregar
				txtCI.setText("");
				txtNombre.setText("");
				cbSexo.setSelectedIndex(0);
				chkActivo.setSelected(false);
				txtCantidadGuardias.setText("");
				cbTipo.setSelectedIndex(0);
				txtGuardiasFestivo.setText("");
				dateChooserFechaIncorporacion.setDate(null);
				txtGrupo.setText("");
				actualizarTabla.actionPerformed(null);
			}
		});

		// Cargar personas existentes en la tabla
		for (Persona p : facultad.getPersonas()) {
			String tipo = (p instanceof Estudiante) ? "Estudiante" : "Trabajador";
			Object extra = (tipo.equals("Estudiante")) ? ((Estudiante)p).getCantidadGuardiasFestivo() : ((Trabajador)p).getFechaDeIncorporacion();
			int grupo = (p instanceof Estudiante) ? ((Estudiante)p).getGrupo() : 0;
			tableModel.addRow(new Object[]{
				p.getCi(),
				p.getNombre(),
				p.getSexo(),
				Boolean.valueOf(p.getActivo()),
				tipo,
				new Integer(p.getCantidadGuardias()),
				extra,
				grupo
			});
		}

		// Selección de fila para editar
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
							dateChooserFechaIncorporacion.setDate(null);
						} else {
							dateChooserFechaIncorporacion.setDate(null);
							Object fechaObj = tableModel.getValueAt(selectedRow[0], 6);
							if (fechaObj != null && fechaObj instanceof java.time.LocalDate) {
								java.time.LocalDate fecha = (java.time.LocalDate) fechaObj;
								java.util.Date date = java.util.Date.from(fecha.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
								dateChooserFechaIncorporacion.setDate(date);
							}
							txtGuardiasFestivo.setText("");
						}
						// Habilitar/deshabilitar el calendario según el estado actual
						if ("Trabajador".equals(cbTipo.getSelectedItem())) {
							dateChooserFechaIncorporacion.setEnabled(!chkActivo.isSelected());
						} else {
							dateChooserFechaIncorporacion.setEnabled(false);
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
				if (cantidadGuardiasStr == null || cantidadGuardiasStr.trim().isEmpty()) {
					cantidadGuardias = 0;
				} else {
					try { 
						cantidadGuardias = Integer.parseInt(cantidadGuardiasStr); 
						if (cantidadGuardias < 0) {
							javax.swing.JOptionPane.showMessageDialog(null, "La cantidad de guardias no puede ser menor que 0.");
							return;
						}
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(null, "La cantidad de guardias debe ser un número válido.");
						return;
					}
				}
				String tipo = (String) cbTipo.getSelectedItem();
				Persona persona = null;
				int grupo = 0;
				String grupoStr = txtGrupo.getText();
				if (grupoStr != null && !grupoStr.trim().isEmpty()) {
					try { grupo = Integer.parseInt(grupoStr); } catch (Exception ex) {}
				}
				if ("Estudiante".equals(tipo)) {
					int guardiasFestivo = 0;
					String guardiasFestivoStr = txtGuardiasFestivo.getText();
					if (guardiasFestivoStr != null && !guardiasFestivoStr.trim().isEmpty()) {
						try { guardiasFestivo = Integer.parseInt(guardiasFestivoStr); } catch (Exception ex) {}
					}
					persona = new Estudiante(ci, nombre, sexo, activo, cantidadGuardias, guardiasFestivo, grupo);
				} else {
					LocalDate fecha = null;
					java.util.Date fechaDate = dateChooserFechaIncorporacion.getDate();
					// Solo tomar la fecha si el trabajador NO está activo
					if (!chkActivo.isSelected() && fechaDate != null) {
						fecha = fechaDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
					}
					persona = new Trabajador(ci, nombre, sexo, activo, fecha, cantidadGuardias, grupo);
				}
				// Actualizar en facultad usando el método correspondiente
				facultad.actualizarPersona(persona);

				// Actualizar en la tabla
				Object extra = "Estudiante".equals(tipo) ? txtGuardiasFestivo.getText() : dateChooserFechaIncorporacion.getDate();
				tableModel.setValueAt(ci, selectedRow[0], 0);
				tableModel.setValueAt(nombre, selectedRow[0], 1);
				tableModel.setValueAt(sexo, selectedRow[0], 2);
				tableModel.setValueAt(Boolean.valueOf(activo), selectedRow[0], 3);
				tableModel.setValueAt(tipo, selectedRow[0], 4);
				tableModel.setValueAt(new Integer(cantidadGuardias), selectedRow[0], 5);
				tableModel.setValueAt(extra, selectedRow[0], 6);
				tableModel.setValueAt(grupo, selectedRow[0], 7);
				txtGrupo.setText("");
				actualizarTabla.actionPerformed(null);
			}
		});

		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = table.getSelectedRow();
				if (selected == -1) {
					javax.swing.JOptionPane.showMessageDialog(null, "Seleccione una persona para eliminar.");
					return;
				}
				String ci = (String) tableModel.getValueAt(selected, 0);
				facultad.eliminarPersona(ci);
				tableModel.removeRow(selected);
				javax.swing.JOptionPane.showMessageDialog(null, "Persona eliminada correctamente.");
				// Vaciar campos después de eliminar
				txtCI.setText("");
				txtNombre.setText("");
				cbSexo.setSelectedIndex(0);
				chkActivo.setSelected(false);
				txtCantidadGuardias.setText("");
				cbTipo.setSelectedIndex(0);
				txtGuardiasFestivo.setText("");
				dateChooserFechaIncorporacion.setDate(null);
				actualizarTabla.actionPerformed(null);
			}
		});
	}

	public PlanificadorGuardias getPlanificador() {
		return planificador;
	}

	public void setPlanificador(PlanificadorGuardias planificador) {
		this.planificador = planificador;
	}

}
