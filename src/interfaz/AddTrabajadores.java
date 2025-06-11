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

	public AddTrabajadores(final PlanificadorGuardias planificador) {
		setMinimumSize(new Dimension(500, 530));
		this.planificador = planificador;
		// Colores institucionales
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		final Color blanco = Color.WHITE;

		setTitle("Añadir Trabajador");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cierra la app
		setBounds(100, 100, 500, 530);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Nuevo Trabajador");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelForm = new JPanel();
		panelForm.setBackground(amarillo);
		panelForm.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:200px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:220px:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				}));

		// CI
		txtCi = new JTextField(15);
		txtCi.setFont(new Font("Arial", Font.PLAIN, 15));
		txtCi.setBackground(blanco);
		txtCi.setForeground(negro);
		panelForm.add(new JLabel("CI:"), "2, 2, center, center");
		panelForm.add(txtCi, "4, 2, center, center");

		// Nombre
		txtNombre = new JTextField(15);
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
		txtNombre.setBackground(blanco);
		txtNombre.setForeground(negro);
		panelForm.add(new JLabel("Nombre:"), "2, 4, center, center");
		panelForm.add(txtNombre, "4, 4, center, center");

		// Apellidos
		txtApellidos = new JTextField(15);
		txtApellidos.setFont(new Font("Arial", Font.PLAIN, 15));
		txtApellidos.setBackground(blanco);
		txtApellidos.setForeground(negro);
		panelForm.add(new JLabel("Apellidos:"), "2, 6, center, center");
		panelForm.add(txtApellidos, "4, 6, center, center");

		// Sexo
		comboSexo = new JComboBox<String>();
		comboSexo.addItem("MASCULINO");
		comboSexo.addItem("FEMENINO");
		comboSexo.setFont(new Font("Arial", Font.PLAIN, 15));
		comboSexo.setBackground(blanco);
		comboSexo.setForeground(negro);
		panelForm.add(new JLabel("Sexo:"), "2, 8, center, center");
		panelForm.add(comboSexo, "4, 8, center, center");

		// Activo
		chkActivo = new JCheckBox();
		chkActivo.setBackground(amarillo);
		chkActivo.setSelected(true);
		panelForm.add(new JLabel("Activo:"), "2, 10, center, center");
		panelForm.add(chkActivo, "4, 10, center, center");

		// Fecha de Incorporación
		dateChooserFechaIncorporacion = new JDateChooser();
		dateChooserFechaIncorporacion.setDateFormatString("yyyy-MM-dd");
		dateChooserFechaIncorporacion.setFont(new Font("Arial", Font.PLAIN, 15));
		dateChooserFechaIncorporacion.setBackground(blanco);
		dateChooserFechaIncorporacion.setForeground(negro);
		panelForm.add(new JLabel("Fecha Incorporación:"), "2, 12, center, center");
		panelForm.add(dateChooserFechaIncorporacion, "4, 12, center, center");

		// Guardias Asignadas
		txtGuardiasAsignadas = new JTextField("0", 15);
		txtGuardiasAsignadas.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasAsignadas.setBackground(blanco);
		txtGuardiasAsignadas.setForeground(negro);
		panelForm.add(new JLabel("Guardias Asignadas:"), "2, 14, center, center");
		panelForm.add(txtGuardiasAsignadas, "4, 14, center, center");

		// Guardias Festivo
		txtGuardiasFestivo = new JTextField("0", 15);
		txtGuardiasFestivo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasFestivo.setBackground(blanco);
		txtGuardiasFestivo.setForeground(negro);
		panelForm.add(new JLabel("Guardias Festivo:"), "2, 16, center, center");
		panelForm.add(txtGuardiasFestivo, "4, 16, center, center");

		// Voluntario
		chkVoluntario = new JCheckBox();
		chkVoluntario.setBackground(amarillo);
		chkVoluntario.setSelected(false);
		panelForm.add(new JLabel("Voluntario:"), "2, 18, center, center");
		panelForm.add(chkVoluntario, "4, 18, center, center");

		contentPane.add(panelForm, BorderLayout.CENTER);

		// Botón guardar
		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);
		JButton btnGuardar = new JButton("Guardar") {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				if (isContentAreaFilled()) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(getBackground());
					g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
					g2.dispose();
				}
				super.paintComponent(g);
			}
		};
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
				int guardiasAsignadas = 0;
				int guardiasFestivo = 0;
				LocalDate fechaIncorporacion = null;
				boolean datosValidos = true;
				String mensajeError = "";

				try {
					guardiasAsignadas = Integer.parseInt(txtGuardiasAsignadas.getText().trim());
					guardiasFestivo = Integer.parseInt(txtGuardiasFestivo.getText().trim());
					java.util.Date utilDate = dateChooserFechaIncorporacion.getDate();
					if (utilDate == null) throw new Exception("Fecha no seleccionada");
					fechaIncorporacion = new java.sql.Date(utilDate.getTime()).toLocalDate();
				} catch (Exception ex) {
					datosValidos = false;
					mensajeError = "Verifique los campos numéricos y la fecha.";
				}
				if (datosValidos && (ci.isEmpty() || nombre.isEmpty() || apellidos.isEmpty())) {
					datosValidos = false;
					mensajeError = "Complete todos los campos obligatorios.";
				}
				if (datosValidos) {
					Trabajador trabajador = new Trabajador(ci, nombre, apellidos, Sexo.valueOf(sexoStr), activo, fechaIncorporacion, guardiasAsignadas, guardiasFestivo, voluntario);
					planificador.getFacultad().agregarPersona(trabajador);
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
							JOptionPane.INFORMATION_MESSAGE
							);
					dispose();
				} else {
					JOptionPane.showMessageDialog(AddTrabajadores.this, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelBoton.add(btnGuardar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);
	}
}
