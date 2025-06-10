package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import utiles.GestorUsuarios;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtContrasena;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login(Window parent) {
		setTitle("Iniciar Sesión");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 340);
		setLocationRelativeTo(parent);

		// Colores
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		Color blanco = Color.WHITE;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Panel superior con título
		JLabel lblTitulo = new JLabel("Sistema de Guardias");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		// Panel central para formulario
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(amarillo);
		panelCentral.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setFont(new Font("Arial", Font.BOLD, 16));
		lblUsuario.setForeground(negro);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.EAST;
		panelCentral.add(lblUsuario, gbc);

		txtUsuario = new JTextField(15);
		txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
		txtUsuario.setBackground(blanco);
		txtUsuario.setForeground(negro);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		panelCentral.add(txtUsuario, gbc);

		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setFont(new Font("Arial", Font.BOLD, 16));
		lblContrasena.setForeground(negro);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		panelCentral.add(lblContrasena, gbc);

		txtContrasena = new JPasswordField(15);
		txtContrasena.setFont(new Font("Arial", Font.PLAIN, 16));
		txtContrasena.setBackground(blanco);
		txtContrasena.setForeground(negro);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		panelCentral.add(txtContrasena, gbc);

		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.setFont(new Font("Arial", Font.BOLD, 16));
		btnIngresar.setBackground(negro);
		btnIngresar.setForeground(amarillo);
		btnIngresar.setFocusPainted(false);
		btnIngresar.setBorder(BorderFactory.createLineBorder(negro, 2));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panelCentral.add(btnIngresar, gbc);

		contentPane.add(panelCentral, BorderLayout.CENTER);

		// Pie de página
		JLabel lblPie = new JLabel("© Facultad de Informática");
		lblPie.setFont(new Font("Arial", Font.PLAIN, 12));
		lblPie.setForeground(negro);
		lblPie.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblPie, BorderLayout.SOUTH);

		// Acción de login
		btnIngresar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String usuario = txtUsuario.getText();
				String contrasena = new String(txtContrasena.getPassword());
				if (GestorUsuarios.validarUsuario(usuario, contrasena)) {
					// Mensaje personalizado con estética
					JLabel label = new JLabel("¡Bienvenido, " + usuario + "!");
					label.setFont(new Font("Arial", Font.BOLD, 18));
					label.setForeground(negro);
					JPanel panel = new JPanel();
					panel.setBackground(amarillo);
					panel.add(label);
					JOptionPane.showMessageDialog(
						Login.this,
						panel,
						"Acceso concedido",
						JOptionPane.INFORMATION_MESSAGE
					);
					Inicio inicioFrame = new Inicio();
					inicioFrame.setVisible(true);
					setVisible(false);
					dispose();
				} else {
					// Mensaje de error con estética
					JLabel label = new JLabel("Usuario o contraseña incorrectos");
					label.setFont(new Font("Arial", Font.BOLD, 16));
					label.setForeground(Color.RED);
					JPanel panel = new JPanel();
					panel.setBackground(amarillo);
					panel.add(label);
					JOptionPane.showMessageDialog(
						Login.this,
						panel,
						"Error de acceso",
						JOptionPane.ERROR_MESSAGE
					);
				}
			}
		});
	}
}
