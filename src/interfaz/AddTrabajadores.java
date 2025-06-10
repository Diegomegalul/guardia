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
		panelForm.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL; // <-- Añadido para expandir los campos de texto
		gbc.weightx = 1.0; // <-- Añadido para que los campos ocupen el espacio disponible

		// CI
		gbc.gridx = 0; gbc.gridy = 0;
		panelForm.add(new JLabel("CI:"), gbc);
		gbc.gridx = 1;
		txtCi = new JTextField(15);
		txtCi.setFont(new Font("Arial", Font.PLAIN, 15));
		txtCi.setBackground(blanco);
		txtCi.setForeground(negro);
		panelForm.add(txtCi, gbc);

		// Nombre
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Nombre:"), gbc);
		gbc.gridx = 1;
		txtNombre = new JTextField(15);
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
		txtNombre.setBackground(blanco);
		txtNombre.setForeground(negro);
		panelForm.add(txtNombre, gbc);

		// Apellidos
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Apellidos:"), gbc);
		gbc.gridx = 1;
		txtApellidos = new JTextField(15);
		txtApellidos.setFont(new Font("Arial", Font.PLAIN, 15));
		txtApellidos.setBackground(blanco);
		txtApellidos.setForeground(negro);
		panelForm.add(txtApellidos, gbc);

		// Sexo
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Sexo:"), gbc);
		gbc.gridx = 1;
		comboSexo = new JComboBox<String>(new String[]{"MASCULINO", "FEMENINO"});
		comboSexo.setFont(new Font("Arial", Font.PLAIN, 15));
		comboSexo.setBackground(blanco);
		comboSexo.setForeground(negro);
		panelForm.add(comboSexo, gbc);

		// Activo
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Activo:"), gbc);
		gbc.gridx = 1;
		chkActivo = new JCheckBox();
		chkActivo.setBackground(amarillo);
		chkActivo.setSelected(true);
		panelForm.add(chkActivo, gbc);

		// Fecha de Incorporación
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Fecha Incorporación:"), gbc);
		gbc.gridx = 1;
		dateChooserFechaIncorporacion = new JDateChooser();
		dateChooserFechaIncorporacion.setDateFormatString("yyyy-MM-dd");
		dateChooserFechaIncorporacion.setFont(new Font("Arial", Font.PLAIN, 15));
		dateChooserFechaIncorporacion.setBackground(blanco);
		dateChooserFechaIncorporacion.setForeground(negro);
		panelForm.add(dateChooserFechaIncorporacion, gbc);

		// Guardias Asignadas
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Guardias Asignadas:"), gbc);
		gbc.gridx = 1;
		txtGuardiasAsignadas = new JTextField("0", 15);
		txtGuardiasAsignadas.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasAsignadas.setBackground(blanco);
		txtGuardiasAsignadas.setForeground(negro);
		panelForm.add(txtGuardiasAsignadas, gbc);

		// Guardias Festivo
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Guardias Festivo:"), gbc);
		gbc.gridx = 1;
		txtGuardiasFestivo = new JTextField("0", 15);
		txtGuardiasFestivo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasFestivo.setBackground(blanco);
		txtGuardiasFestivo.setForeground(negro);
		panelForm.add(txtGuardiasFestivo, gbc);

		// Voluntario
		gbc.gridx = 0; gbc.gridy++;
		panelForm.add(new JLabel("Voluntario:"), gbc);
		gbc.gridx = 1;
		chkVoluntario = new JCheckBox();
		chkVoluntario.setBackground(amarillo);
		chkVoluntario.setSelected(false);
		panelForm.add(chkVoluntario, gbc);

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
				try {
					guardiasAsignadas = Integer.parseInt(txtGuardiasAsignadas.getText().trim());
					guardiasFestivo = Integer.parseInt(txtGuardiasFestivo.getText().trim());
					java.util.Date utilDate = dateChooserFechaIncorporacion.getDate();
					if (utilDate == null) throw new Exception("Fecha no seleccionada");
					fechaIncorporacion = new java.sql.Date(utilDate.getTime()).toLocalDate();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(AddTrabajadores.this, "Verifique los campos numéricos y la fecha.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ci.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
					JOptionPane.showMessageDialog(AddTrabajadores.this, "Complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
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
			}
		});
		panelBoton.add(btnGuardar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);
	}
}
