package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaz.AddTrabajadores;
import interfaz.AddEstudiantes;
import interfaz.EditCalendario;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public Inicio() {
		// Colores institucionales
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		final Color blanco = Color.WHITE;

		setTitle("Inicio - Sistema de Guardias");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Cambiado para control personalizado
		setBounds(100, 100, 900, 600); // Mejor tamaño para menú
		setLocationRelativeTo(null);

		// Confirmación al cerrar ventana principal
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				solicitarConfirmacionSalida();
			}
		});

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// Menú superior
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(amarillo);
		menuBar.setBorder(BorderFactory.createLineBorder(negro, 2)); // Borde negro para el menú

		// "Añadir Personas" (no seleccionable, solo submenu)
		JMenu menuAdd = new JMenu("Añadir Personas");
		menuAdd.setFont(new Font("Arial", Font.BOLD, 16));
		menuAdd.setForeground(negro);
		menuAdd.setOpaque(true);
		menuAdd.setBackground(amarillo);
		menuAdd.setBorder(BorderFactory.createLineBorder(negro, 1)); // Borde negro

		JMenuItem itemEstudiante = new JMenuItem("Añadir Estudiante");
		itemEstudiante.setFont(new Font("Arial", Font.PLAIN, 15));
		itemEstudiante.setBackground(blanco);
		itemEstudiante.setForeground(negro);
		itemEstudiante.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemEstudiante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddEstudiantes frame = new AddEstudiantes();
				frame.setVisible(true);
			}
		});

		JMenuItem itemTrabajador = new JMenuItem("Añadir Trabajador");
		itemTrabajador.setFont(new Font("Arial", Font.PLAIN, 15));
		itemTrabajador.setBackground(blanco);
		itemTrabajador.setForeground(negro);
		itemTrabajador.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemTrabajador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddTrabajadores frame = new AddTrabajadores();
				frame.setVisible(true);
			}
		});

		menuAdd.add(itemEstudiante);
		menuAdd.add(itemTrabajador);
		menuBar.add(menuAdd);

		// "Planificar Guardias"
		JMenuItem menuPlanificar = new JMenuItem("Planificar Guardias");
		menuPlanificar.setFont(new Font("Arial", Font.BOLD, 16));
		menuPlanificar.setBackground(amarillo);
		menuPlanificar.setForeground(negro);
		menuPlanificar.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuPlanificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Funcionalidad próximamente disponible.");
			}
		});
		menuBar.add(menuPlanificar);

		// "Editar Calendario"
		JMenuItem menuCalendario = new JMenuItem("Editar Calendario");
		menuCalendario.setFont(new Font("Arial", Font.BOLD, 16));
		menuCalendario.setBackground(amarillo);
		menuCalendario.setForeground(negro);
		menuCalendario.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuCalendario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditCalendario frame = new EditCalendario();
				frame.setVisible(true);
			}
		});
		menuBar.add(menuCalendario);

		// "Reportes"
		JMenuItem menuReportes = new JMenuItem("Reportes");
		menuReportes.setFont(new Font("Arial", Font.BOLD, 16));
		menuReportes.setBackground(amarillo);
		menuReportes.setForeground(negro);
		menuReportes.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuReportes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Funcionalidad de reportes próximamente disponible.");
			}
		});
		menuBar.add(menuReportes);

		// "Valores de Prueba"
		JMenuItem menuValores = new JMenuItem("Valores de prueba");
		menuValores.setFont(new Font("Arial", Font.BOLD, 16));
		menuValores.setBackground(amarillo);
		menuValores.setForeground(negro);
		menuValores.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuValores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Valores de prueba cargados (simulado).");
			}
		});
		menuBar.add(menuValores);

		setJMenuBar(menuBar);

		// Panel central con mensaje de bienvenida o imagen
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(amarillo);
		panelCentral.setLayout(new BorderLayout());

		JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Guardias");
		lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
		lblBienvenida.setForeground(negro);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setBorder(new EmptyBorder(80, 10, 10, 10));
		panelCentral.add(lblBienvenida, BorderLayout.CENTER);

		// Panel inferior para el botón salir
		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelInferior.setBackground(amarillo);

		// Botón salir con icono y esquinas redondeadas
		JButton btnSalir = new JButton("Salir") {
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				if (isContentAreaFilled()) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(getBackground());
					g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
					g2.dispose();
				}
				super.paintComponent(g);
			}
		};
		btnSalir.setFont(new Font("Arial", Font.BOLD, 16));
		btnSalir.setBackground(negro);
		btnSalir.setForeground(amarillo);
		btnSalir.setFocusPainted(false);
		btnSalir.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnSalir.setContentAreaFilled(false);
		btnSalir.setOpaque(true);

		// Icono redondeado (círculo rojo con X blanca)
		Icon iconoSalir = new Icon() {
			public int getIconWidth() { return 20; }
			public int getIconHeight() { return 20; }
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.RED);
				g2.fillOval(x, y, 18, 18);
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(x+5, y+5, x+13, y+13);
				g2.drawLine(x+13, y+5, x+5, y+13);
				g2.dispose();
			}
		};
		btnSalir.setIcon(iconoSalir);

		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solicitarConfirmacionSalida();
			}
		});
		panelInferior.add(btnSalir);

		contentPane.add(panelCentral, BorderLayout.CENTER);
		contentPane.add(panelInferior, BorderLayout.SOUTH);
	}

	private void solicitarConfirmacionSalida() {
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;

		JLabel label = new JLabel("¿Estás seguro de que deseas salir de la aplicación?");
		label.setFont(new Font("Arial", Font.BOLD, 16));
		label.setForeground(negro);
		JPanel panel = new JPanel();
		panel.setBackground(amarillo);
		panel.add(label);

		int opcion = JOptionPane.showConfirmDialog(
			this,
			panel,
			"Confirmar salida",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
		);
		if (opcion == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}
