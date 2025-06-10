package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica.Estudiante;
import logica.PlanificadorGuardias;
import utiles.Sexo;

public class AddEstudiantes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCi, txtNombre, txtApellidos, txtGrupo;
	private JComboBox<String> comboSexo;
	private JCheckBox chkActivo;
	private JTextField txtGuardiasAsignadas, txtGuardiasFestivo;
	private PlanificadorGuardias planificador; // <- debe ser de instancia, no static

	/**
	 * Create the frame.
	 */
	public AddEstudiantes(final PlanificadorGuardias planificador) {
		this.planificador = planificador;
		// Colores institucionales
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		final Color blanco = Color.WHITE;

		setTitle("Añadir Estudiante");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cierra la app
		setBounds(100, 100, 500, 500);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Nuevo Estudiante");
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

		// CI
		gbc.gridx = 0; gbc.gridy = 0;
		JLabel lblCi = new JLabel("CI:");
		lblCi.setFont(new Font("Arial", Font.BOLD, 15));
		lblCi.setForeground(negro);
		panelForm.add(lblCi, gbc);
		gbc.gridx = 1;
		txtCi = new JTextField(15);
		txtCi.setFont(new Font("Arial", Font.PLAIN, 15));
		txtCi.setBackground(blanco);
		txtCi.setForeground(negro);
		panelForm.add(txtCi, gbc);

		// Nombre
		gbc.gridx = 0; gbc.gridy++;
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Arial", Font.BOLD, 15));
		lblNombre.setForeground(negro);
		panelForm.add(lblNombre, gbc);
		gbc.gridx = 1;
		txtNombre = new JTextField(15);
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
		txtNombre.setBackground(blanco);
		txtNombre.setForeground(negro);
		panelForm.add(txtNombre, gbc);

		// Apellidos
		gbc.gridx = 0; gbc.gridy++;
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setFont(new Font("Arial", Font.BOLD, 15));
		lblApellidos.setForeground(negro);
		panelForm.add(lblApellidos, gbc);
		gbc.gridx = 1;
		txtApellidos = new JTextField(15);
		txtApellidos.setFont(new Font("Arial", Font.PLAIN, 15));
		txtApellidos.setBackground(blanco);
		txtApellidos.setForeground(negro);
		panelForm.add(txtApellidos, gbc);

		// Sexo
		gbc.gridx = 0; gbc.gridy++;
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setFont(new Font("Arial", Font.BOLD, 15));
		lblSexo.setForeground(negro);
		panelForm.add(lblSexo, gbc);
		gbc.gridx = 1;
		comboSexo = new JComboBox<String>(new String[]{"MASCULINO", "FEMENINO"});
		comboSexo.setFont(new Font("Arial", Font.PLAIN, 15));
		comboSexo.setBackground(blanco);
		comboSexo.setForeground(negro);
		panelForm.add(comboSexo, gbc);

		// Activo
		gbc.gridx = 0; gbc.gridy++;
		JLabel lblActivo = new JLabel("Activo:");
		lblActivo.setFont(new Font("Arial", Font.BOLD, 15));
		lblActivo.setForeground(negro);
		panelForm.add(lblActivo, gbc);
		gbc.gridx = 1;
		chkActivo = new JCheckBox();
		chkActivo.setBackground(amarillo);
		chkActivo.setSelected(true);
		panelForm.add(chkActivo, gbc);

		// Guardias Asignadas
		gbc.gridx = 0; gbc.gridy++;
		JLabel lblGuardias = new JLabel("Guardias Asignadas:");
		lblGuardias.setFont(new Font("Arial", Font.BOLD, 15));
		lblGuardias.setForeground(negro);
		panelForm.add(lblGuardias, gbc);
		gbc.gridx = 1;
		txtGuardiasAsignadas = new JTextField("0", 15);
		txtGuardiasAsignadas.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasAsignadas.setBackground(blanco);
		txtGuardiasAsignadas.setForeground(negro);
		panelForm.add(txtGuardiasAsignadas, gbc);

		// Guardias Festivo
		gbc.gridx = 0; gbc.gridy++;
		JLabel lblFestivo = new JLabel("Guardias Festivo:");
		lblFestivo.setFont(new Font("Arial", Font.BOLD, 15));
		lblFestivo.setForeground(negro);
		panelForm.add(lblFestivo, gbc);
		gbc.gridx = 1;
		txtGuardiasFestivo = new JTextField("0", 15);
		txtGuardiasFestivo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasFestivo.setBackground(blanco);
		txtGuardiasFestivo.setForeground(negro);
		panelForm.add(txtGuardiasFestivo, gbc);

		// Grupo
		gbc.gridx = 0; gbc.gridy++;
		JLabel lblGrupo = new JLabel("Grupo:");
		lblGrupo.setFont(new Font("Arial", Font.BOLD, 15));
		lblGrupo.setForeground(negro);
		panelForm.add(lblGrupo, gbc);
		gbc.gridx = 1;
		txtGrupo = new JTextField(15);
		txtGrupo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGrupo.setBackground(blanco);
		txtGrupo.setForeground(negro);
		panelForm.add(txtGrupo, gbc);

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
				// Usa la variable de instancia 'planificador'
				String ci = txtCi.getText().trim();
				String nombre = txtNombre.getText().trim();
				String apellidos = txtApellidos.getText().trim();
				String sexoStr = (String) comboSexo.getSelectedItem();
				boolean activo = chkActivo.isSelected();
				int guardiasAsignadas = 0;
				int guardiasFestivo = 0;
				int grupo = 0;
				try {
					guardiasAsignadas = Integer.parseInt(txtGuardiasAsignadas.getText().trim());
					guardiasFestivo = Integer.parseInt(txtGuardiasFestivo.getText().trim());
					grupo = Integer.parseInt(txtGrupo.getText().trim());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(AddEstudiantes.this, "Verifique los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ci.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
					JOptionPane.showMessageDialog(AddEstudiantes.this, "Complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Estudiante estudiante = new Estudiante(ci, nombre, apellidos, Sexo.valueOf(sexoStr), activo, guardiasAsignadas, guardiasFestivo, grupo);
				planificador.getFacultad().agregarPersona(estudiante);
				JLabel label = new JLabel("Estudiante guardado correctamente");
				label.setFont(new Font("Arial", Font.BOLD, 16));
				label.setForeground(negro);
				JPanel panel = new JPanel();
				panel.setBackground(amarillo);
				panel.add(label);
				JOptionPane.showMessageDialog(
					AddEstudiantes.this,
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
