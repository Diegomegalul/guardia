package interfaz;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import logica.Trabajador;
import logica.PlanificadorGuardias;
import utiles.Sexo;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class AddTrabajadores extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCi, txtNombre, txtApellidos, txtGuardiasAsignadas, txtGuardiasFestivo;
	private JComboBox<String> comboSexo;
	private JCheckBox chkActivo, chkVoluntario;
	@SuppressWarnings("unused")
	private PlanificadorGuardias planificador;
	private JDateChooser dateChooserFechaIncorporacion;
	private JDateChooser dateChooserFechaVoluntaria; // Nuevo campo para fecha voluntaria

	/**
	 * @wbp.parser.constructor
	 */
	public AddTrabajadores(final PlanificadorGuardias planificador, final Trabajador trabajador,
			final VerTrabajadores verTrabajadores) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setMinimumSize(new Dimension(900, 600));
		this.planificador = planificador;
		// Colores institucionales
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		final Color blanco = Color.WHITE;

		setTitle(trabajador == null ? "Añadir Trabajador" : "Editar Trabajador");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cierra la app
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel(trabajador == null ? "Nuevo Trabajador" : "Editar Trabajador");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelForm = new JPanel();
		panelForm.setBackground(amarillo);
		panelForm.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:200px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:220px:grow"), },
				new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:pref:grow"), }));

		// CI
		txtCi = new JTextField(15);
		txtCi.setFont(new Font("Arial", Font.PLAIN, 15));
		txtCi.setBackground(blanco);
		txtCi.setForeground(negro);
		JLabel Ci = new JLabel("CI:");
		panelForm.add(Ci, "2, 2, center, center");
		panelForm.add(txtCi, "4, 2, center, center");

		// Voluntario
		chkVoluntario = new JCheckBox();
		chkVoluntario.setBackground(amarillo);
		chkVoluntario.setSelected(false);
		JLabel Voluntario = new JLabel("Voluntario:");
		panelForm.add(Voluntario, "2, 18, center, center");
		panelForm.add(chkVoluntario, "4, 18, center, center");

		// Nombre
		txtNombre = new JTextField(15);
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
		txtNombre.setBackground(blanco);
		txtNombre.setForeground(negro);
		JLabel Nombre = new JLabel("Nombre:");
		panelForm.add(Nombre, "2, 4, center, center");
		panelForm.add(txtNombre, "4, 4, center, center");

		// Apellidos
		txtApellidos = new JTextField(15);
		txtApellidos.setFont(new Font("Arial", Font.PLAIN, 15));
		txtApellidos.setBackground(blanco);
		txtApellidos.setForeground(negro);
		JLabel Apellidos = new JLabel("Apellidos:");
		panelForm.add(Apellidos, "2, 6, center, center");
		panelForm.add(txtApellidos, "4, 6, center, center");

		// Sexo
		comboSexo = new JComboBox<String>();
		comboSexo.addItem("MASCULINO");
		comboSexo.addItem("FEMENINO");
		comboSexo.setFont(new Font("Arial", Font.PLAIN, 15));
		comboSexo.setBackground(blanco);
		comboSexo.setForeground(negro);
		JLabel LabelSexo = new JLabel("Sexo:");
		panelForm.add(LabelSexo, "2, 8, center, center");
		panelForm.add(comboSexo, "4, 8, center, center");

		// Activo
		chkActivo = new JCheckBox();
		chkActivo.setBackground(amarillo);
		chkActivo.setSelected(true);
		JLabel Activo = new JLabel("Activo:");
		panelForm.add(Activo, "2, 10, center, center");
		panelForm.add(chkActivo, "4, 10, center, center");

		// Fecha de Incorporación
		dateChooserFechaIncorporacion = new JDateChooser();
		dateChooserFechaIncorporacion.setDateFormatString("yyyy-MM-dd");
		dateChooserFechaIncorporacion.setFont(new Font("Arial", Font.PLAIN, 15));
		dateChooserFechaIncorporacion.setBackground(blanco);
		dateChooserFechaIncorporacion.setForeground(negro);
		dateChooserFechaIncorporacion.setEnabled(!chkActivo.isSelected()); // Solo editable si NO está activo
		JLabel Fecha = new JLabel("Fecha Incorporación:");
		panelForm.add(Fecha, "2, 12, center, center");
		panelForm.add(dateChooserFechaIncorporacion, "4, 12, center, center");

		// Fecha Guardia Voluntaria (solo julio/agosto)
		dateChooserFechaVoluntaria = new JDateChooser();
		dateChooserFechaVoluntaria.setDateFormatString("yyyy-MM-dd");
		dateChooserFechaVoluntaria.setFont(new Font("Arial", Font.PLAIN, 15));
		dateChooserFechaVoluntaria.setBackground(blanco);
		dateChooserFechaVoluntaria.setForeground(negro);
		dateChooserFechaVoluntaria.setEnabled(chkVoluntario.isSelected()); // Solo editable si voluntario está activo
		JLabel lblFechaVoluntaria = new JLabel("Fecha Guardia Voluntaria");
		panelForm.add(lblFechaVoluntaria, "2, 20, center, center");
		panelForm.add(dateChooserFechaVoluntaria, "4, 20, center, center");

		// Guardias Asignadas
		txtGuardiasAsignadas = new JTextField("0", 15);
		txtGuardiasAsignadas.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasAsignadas.setBackground(blanco);
		txtGuardiasAsignadas.setForeground(negro);
		JLabel GAsignadas = new JLabel("Guardias Realizadas:");
		panelForm.add(GAsignadas, "2, 14, center, center");
		panelForm.add(txtGuardiasAsignadas, "4, 14, center, center");

		// Guardias Festivo
		txtGuardiasFestivo = new JTextField("0", 15);
		txtGuardiasFestivo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasFestivo.setBackground(blanco);
		txtGuardiasFestivo.setForeground(negro);
		JLabel GFestivo = new JLabel("Guardias Realizadas en dias Festivos:");
		panelForm.add(GFestivo, "2, 16, center, center");
		panelForm.add(txtGuardiasFestivo, "4, 16, center, center");

		// --- Habilitar/Deshabilitar campos dependientes ---
		// Listener para habilitar/deshabilitar fecha de incorporación
		chkActivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateChooserFechaIncorporacion.setEnabled(!chkActivo.isSelected());
			}
		});
		// Listener para habilitar/deshabilitar fecha voluntaria
		chkVoluntario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateChooserFechaVoluntaria.setEnabled(chkVoluntario.isSelected());
			}
		});

		contentPane.add(panelForm, BorderLayout.CENTER);

		// --- LLENAR CAMPOS SI ES EDICIÓN ---
		if (trabajador != null) {
			if (txtCi != null)
				txtCi.setText(trabajador.getCi());
			if (txtNombre != null)
				txtNombre.setText(trabajador.getNombre());
			if (txtApellidos != null)
				txtApellidos.setText(trabajador.getApellidos());
			if (comboSexo != null)
				comboSexo.setSelectedItem(trabajador.getSexo().toString());
			if (chkActivo != null)
				chkActivo.setSelected(trabajador.getActivo());
			if (txtGuardiasAsignadas != null)
				txtGuardiasAsignadas.setText(String.valueOf(trabajador.getGuardiasCumplidas()));
			if (txtGuardiasFestivo != null)
				txtGuardiasFestivo.setText(String.valueOf(trabajador.getCantidadGuardiasFestivo()));
			if (chkVoluntario != null)
				chkVoluntario.setSelected(trabajador.getVoluntario());
			if (dateChooserFechaIncorporacion != null) {
				if (trabajador.getFechaDeIncorporacion() != null) {
					dateChooserFechaIncorporacion.setDate(java.sql.Date.valueOf(trabajador.getFechaDeIncorporacion()));
				} else {
					dateChooserFechaIncorporacion.setDate(null);
				}
			}
			if (dateChooserFechaVoluntaria != null) {
				if (trabajador.getFechaGuardiaVoluntaria() != null) {
					dateChooserFechaVoluntaria.setDate(java.sql.Date.valueOf(trabajador.getFechaGuardiaVoluntaria()));
				} else {
					dateChooserFechaVoluntaria.setDate(null);
				}
			}
			if (txtCi != null)
				txtCi.setEditable(false);
		}

		// Botón guardar
		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);
		panelBoton.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:5dlu:grow"),
				ColumnSpec.decode("left:60dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:60dlu:grow"),
				ColumnSpec.decode("right:5dlu:grow"), },
				new RowSpec[] {
						RowSpec.decode("1dlu"),
						RowSpec.decode("20px"), }));

		JButton btnGuardar = new JButton(trabajador == null ? "Guardar" : "Actualizar");
		btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnGuardar.setFont(new Font("Arial", Font.BOLD, 16));
		btnGuardar.setBackground(negro);
		btnGuardar.setForeground(amarillo);
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnGuardar.setContentAreaFilled(false);
		btnGuardar.setOpaque(true);

		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ci = txtCi.getText().trim();
				String nombre = txtNombre.getText().trim();
				String apellidos = txtApellidos.getText().trim();
				String sexoStr = (String) comboSexo.getSelectedItem();
				boolean activo = chkActivo.isSelected();
				boolean voluntario = chkVoluntario.isSelected();
				int guardiasCumplidas = 0;
				int guardiasFestivo = 0;
				LocalDate fechaIncorporacion = null;
				LocalDate fechaVoluntaria = null;
				boolean datosValidos = true;
				String mensajeError = "";

				try {
					guardiasCumplidas = Integer.parseInt(txtGuardiasAsignadas.getText().trim());
					guardiasFestivo = Integer.parseInt(txtGuardiasFestivo.getText().trim());
					java.util.Date utilDate = dateChooserFechaIncorporacion.getDate();
					java.util.Date utilDateVoluntaria = dateChooserFechaVoluntaria.getDate();

					// Validaciones de lógica de negocio
					if (activo) {
						// Si está activo, ignorar el contenido del dateChooser y pasar null
						fechaIncorporacion = null;
					} else {
						if (utilDate == null) {
							datosValidos = false;
							mensajeError = "Debe seleccionar la fecha de incorporación para un trabajador inactivo.";
						} else {
							fechaIncorporacion = new java.sql.Date(utilDate.getTime()).toLocalDate();
						}
					}

					if (voluntario) {
						if (utilDateVoluntaria == null) {
							datosValidos = false;
							mensajeError = "Debe seleccionar la fecha de guardia voluntaria para un voluntario.";
						} else {
							fechaVoluntaria = new java.sql.Date(utilDateVoluntaria.getTime()).toLocalDate();
							int mes = fechaVoluntaria.getMonthValue();
							if (mes != 7 && mes != 8) {
								datosValidos = false;
								mensajeError = "La fecha de guardia voluntaria debe ser en julio o agosto.";
							}
						}
					} else {
						// Si no es voluntario, ignorar el contenido del dateChooser y pasar null
						fechaVoluntaria = null;
					}

					if (datosValidos) {
						if (ci == null || ci.length() != 11 || !ci.matches("\\d{11}")) {
							throw new IllegalArgumentException("El CI debe tener exactamente 11 dígitos numéricos.");
						}
					}
					// Validación de nombre y apellidos solo letras (opcional, ya está en
					// setNombre/setApellidos)
				} catch (IllegalArgumentException ex) {
					datosValidos = false;
					mensajeError = ex.getMessage();
				} catch (Exception ex) {
					datosValidos = false;
					mensajeError = "Verifique los campos numéricos y la fecha.\n" + ex.getMessage();
				}
				if (datosValidos && (ci.isEmpty() || nombre.isEmpty() || apellidos.isEmpty())) {
					datosValidos = false;
					mensajeError = "Complete todos los campos obligatorios.";
				}
				if (datosValidos) {
					try {
						Trabajador nuevoTrabajador = new Trabajador(ci, nombre, apellidos, Sexo.valueOf(sexoStr),
								activo, fechaIncorporacion, 0, guardiasFestivo, guardiasCumplidas, voluntario,
								fechaVoluntaria);
						if (trabajador == null) {
							planificador.getFacultad().agregarPersona(nuevoTrabajador);
							JLabel label = new JLabel("Trabajador guardado correctamente");
							label.setFont(new Font("Arial", Font.BOLD, 16));
							label.setForeground(negro);
							JPanel panel = new JPanel();
							panel.setBackground(amarillo);
							panel.add(label);
							JOptionPane.showMessageDialog(
									AddTrabajadores.this,
									panel,
									"Guardado",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							planificador.getFacultad().eliminarPersona(trabajador);
							planificador.getFacultad().agregarPersona(nuevoTrabajador);
							JLabel label = new JLabel("Trabajador actualizado correctamente");
							label.setFont(new Font("Arial", Font.BOLD, 16));
							label.setForeground(negro);
							JPanel panel = new JPanel();
							panel.setBackground(amarillo);
							panel.add(label);
							JOptionPane.showMessageDialog(
									AddTrabajadores.this,
									panel,
									"Actualizado",
									JOptionPane.INFORMATION_MESSAGE);
						}
						if (verTrabajadores != null) {
							verTrabajadores.refrescarTabla();
						}
						dispose();
						// Si está embebido en Inicio, llamar a volverPanelAnterior
						Container parent = getPanelPrincipal().getParent();
						while (parent != null) {
							if (parent instanceof interfaz.Inicio) {
								((interfaz.Inicio) parent).volverPanelAnterior();
								break;
							}
							parent = parent.getParent();
						}
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(AddTrabajadores.this, ex.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(AddTrabajadores.this, mensajeError, "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelBoton.add(btnGuardar, "4, 2, center, center");
		contentPane.add(panelBoton, BorderLayout.SOUTH);
	}

	public AddTrabajadores(PlanificadorGuardias planificador, Trabajador trabajador) {
		this(planificador, trabajador, null); // Llama al constructor principal
		// No rellenar campos aquí, ya lo hace el constructor principal si trabajador !=
		// null
	}

	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);

		// Recorrer todos los componentes recursivamente
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
			((JTextField) comp).setCaretColor(oscuro ? Color.WHITE : Color.BLACK);
		} else if (comp instanceof JComboBox) {
			comp.setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
			comp.setForeground(oscuro ? Color.WHITE : texto);
		} else if (comp instanceof JCheckBox) {
			comp.setBackground(fondo);
			((JCheckBox) comp).setForeground(oscuro ? Color.WHITE : texto);
		} else if (comp instanceof JButton) {
			comp.setBackground(boton);
			comp.setForeground(amarilloSec);
		} else if (comp instanceof com.toedter.calendar.JDateChooser) {
			Component editor = ((com.toedter.calendar.JDateChooser) comp).getComponent(1);
			editor.setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
			editor.setForeground(oscuro ? Color.WHITE : texto);
			if (editor instanceof JTextField) {
				((JTextField) editor).setCaretColor(oscuro ? Color.WHITE : Color.BLACK);
			}
		}
	}

	// Método para obtener el panel principal para uso en Inicio
	public JPanel getPanelPrincipal() {
		return contentPane;
	}
}
