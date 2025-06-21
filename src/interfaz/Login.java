package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import utiles.GestorUsuarios;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtContrasena;

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

	public Login(Window parent) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/IMG_20250617_110529.jpg")));
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
		panelCentral.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("30px"),
				ColumnSpec.decode("96px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("183px"),
				ColumnSpec.decode("right:max(25dlu;default)"),},
			new RowSpec[] {
				RowSpec.decode("47px"),
				RowSpec.decode("25px"),
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("57px"),}));
		
		// Cargar y escalar el icono de usuario
		ImageIcon iconoUsuario = new ImageIcon(Login.class.getResource("/imagenes/usuario.png"));
		Image img = iconoUsuario.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
		iconoUsuario = new ImageIcon(img);

		JLabel label_1 = new JLabel("Usuario:");
		label_1.setIcon(iconoUsuario);
		label_1.setIconTextGap(10);
		panelCentral.add(label_1, "2, 2, right, center");
		txtUsuario = new JTextField(15);
		txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
		txtUsuario.setBackground(blanco);
		txtUsuario.setForeground(negro);
		panelCentral.add(txtUsuario, "4, 2, right, center");
		
		JLabel label_2 = new JLabel("Contraseña:");
		panelCentral.add(label_2, "2, 4, right, center");
		txtContrasena = new JPasswordField(15);
		txtContrasena.setFont(new Font("Arial", Font.PLAIN, 16));
		txtContrasena.setBackground(blanco);
		txtContrasena.setForeground(negro);
		panelCentral.add(txtContrasena, "4, 4, right, center");

		contentPane.add(panelCentral, BorderLayout.CENTER);
		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.setFont(new Font("Arial", Font.BOLD, 16));
		btnIngresar.setBackground(negro);
		btnIngresar.setForeground(amarillo);
		btnIngresar.setFocusPainted(false);
		btnIngresar.setBorder(BorderFactory.createLineBorder(negro, 2));
		panelCentral.add(btnIngresar, "1, 6, 5, 1, center, center");
		
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

		// Pie de página
		JLabel lblPie = new JLabel("© Facultad de Informática");
		lblPie.setFont(new Font("Arial", Font.PLAIN, 12));
		lblPie.setForeground(negro);
		lblPie.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblPie, BorderLayout.SOUTH);
	}
}
