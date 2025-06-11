package interfaz;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logica.PlanificadorGuardias;
import javax.swing.border.LineBorder;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PlanificadorGuardias planificador;

	private boolean modoOscuro = false;
	private Color amarillo = new Color(255, 215, 0);
	private Color negro = Color.BLACK;
	private Color darkBg = new Color(30, 32, 40); // color principal modo oscuro
	private Color darkFg = new Color(220, 220, 220); // texto modo oscuro

	public Inicio() {
		// Instancia singleton del planificador
		this.planificador = PlanificadorGuardias.getInstancia();

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
		itemEstudiante.setBackground(darkBg);
		itemEstudiante.setForeground(amarillo);
		itemEstudiante.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemEstudiante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddEstudiantes frame = new AddEstudiantes(planificador);
				if (modoOscuro) frame.aplicarModoOscuro(modoOscuro, darkBg, darkFg, new Color(60, 63, 80), amarillo);
				frame.setVisible(true);
			}
		});

		JMenuItem itemTrabajador = new JMenuItem("Añadir Trabajador");
		itemTrabajador.setFont(new Font("Arial", Font.PLAIN, 15));
		itemTrabajador.setBackground(darkBg);
		itemTrabajador.setForeground(amarillo);
		itemTrabajador.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemTrabajador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddTrabajadores frame = new AddTrabajadores(planificador);
				if (modoOscuro) frame.aplicarModoOscuro(modoOscuro, darkBg, darkFg, new Color(60, 63, 80), amarillo);
				frame.setVisible(true);
			}
		});

		menuAdd.add(itemEstudiante);
		menuAdd.add(itemTrabajador);
		menuBar.add(menuAdd);

		// "Planificar Guardias"
		JMenuItem menuPlanificar = new JMenuItem("Planificar Guardias");
		menuPlanificar.setOpaque(true);
		menuPlanificar.setFont(new Font("Arial", Font.BOLD, 16));
		menuPlanificar.setBackground(amarillo);
		menuPlanificar.setForeground(negro);
		menuPlanificar.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuPlanificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aquí deberías abrir el JFrame correspondiente a planificar guardias
				JOptionPane.showMessageDialog(Inicio.this, "Funcionalidad próximamente disponible.");
			}
		});
		
		// NUEVO: Menú "Mostrar" con submenús
		JMenu menuVer = new JMenu("Mostrar");
		menuVer.setOpaque(true);
		menuVer.setFont(new Font("Arial", Font.BOLD, 16));
		menuVer.setForeground(negro);
		menuVer.setBackground(amarillo);
		menuVer.setBorder(new LineBorder(new Color(0, 0, 0)));

		JMenuItem verEstudiantes = new JMenuItem("Estudiantes");
		verEstudiantes.setFont(new Font("Arial", Font.PLAIN, 15));
		verEstudiantes.setBackground(darkBg);
		verEstudiantes.setForeground(amarillo);
		verEstudiantes.setBorder(new LineBorder(new Color(0, 0, 0)));
		verEstudiantes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Listado de estudiantes (próximamente).");
			}
		});

		JMenuItem verProfesores = new JMenuItem("Profesores");
		verProfesores.setFont(new Font("Arial", Font.PLAIN, 15));
		verProfesores.setBackground(darkBg);
		verProfesores.setForeground(amarillo);
		verProfesores.setBorder(BorderFactory.createLineBorder(negro, 1));
		verProfesores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Listado de profesores (próximamente).");
			}
		});

		JMenuItem verGuardias = new JMenuItem("Guardias");
		verGuardias.setFont(new Font("Arial", Font.PLAIN, 15));
		verGuardias.setBackground(darkBg);
		verGuardias.setForeground(amarillo);
		verGuardias.setBorder(BorderFactory.createLineBorder(negro, 1));
		verGuardias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Listado de guardias (próximamente).");
			}
		});

		menuVer.add(verEstudiantes);
		menuVer.add(verProfesores);
		menuVer.add(verGuardias);
		menuBar.add(menuVer);
		menuBar.add(menuPlanificar);

		// "Editar Calendario"
		JMenuItem menuCalendario = new JMenuItem("Editar Calendario");
		menuCalendario.setOpaque(true);
		menuCalendario.setFont(new Font("Arial", Font.BOLD, 16));
		menuCalendario.setBackground(amarillo);
		menuCalendario.setForeground(negro);
		menuCalendario.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuCalendario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditCalendario frame = new EditCalendario(planificador);
				if (modoOscuro) frame.aplicarModoOscuro(modoOscuro, darkBg, darkFg, new Color(60, 63, 80), amarillo);
				frame.setVisible(true);
			}
		});
		menuBar.add(menuCalendario);

		// "Reportes"
		JMenuItem menuReportes = new JMenuItem("Reportes");
		menuReportes.setOpaque(true);
		menuReportes.setFont(new Font("Arial", Font.BOLD, 16));
		menuReportes.setBackground(amarillo);
		menuReportes.setForeground(negro);
		menuReportes.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuReportes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aquí deberías abrir el JFrame correspondiente a reportes
				JOptionPane.showMessageDialog(Inicio.this, "Funcionalidad de reportes próximamente disponible.");
			}
		});
		menuBar.add(menuReportes);

		// "Valores de Prueba"
		JMenuItem menuValores = new JMenuItem("Valores de prueba");
		menuValores.setOpaque(true);
		menuValores.setFont(new Font("Arial", Font.BOLD, 16));
		menuValores.setBackground(amarillo);
		menuValores.setForeground(negro);
		menuValores.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuValores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aquí deberías abrir el JFrame correspondiente a valores de prueba o ejecutar la lógica
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

		// Panel inferior para el botón salir y el botón de modo oscuro
		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(amarillo);

		// Botón salir (derecha)
		JPanel panelSalir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelSalir.setBackground(amarillo);

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
		panelSalir.add(btnSalir);

		// Botón modo oscuro (izquierda)
		JPanel panelLuna = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelLuna.setBackground(amarillo);

		final JButton btnLuna = new JButton() {
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				if (modoOscuro) {
					setBackground(new Color(40, 40, 50)); // Fondo oscuro para el botón en modo oscuro
				} else {
					setBackground(amarillo);
				}
				super.paintComponent(g);
			}
		};
		btnLuna.setPreferredSize(new Dimension(44, 44));
		btnLuna.setBackground(amarillo);
		btnLuna.setBorderPainted(false);
		btnLuna.setFocusPainted(false);
		btnLuna.setContentAreaFilled(true);
		btnLuna.setOpaque(true);
		btnLuna.setToolTipText("Cambiar modo oscuro");

		final Icon iconoLuna = new Icon() {
			public int getIconWidth() { return 32; }
			public int getIconHeight() { return 32; }
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(240, 230, 140)); // luna amarilla suave
				g2.fillArc(x+4, y+4, 24, 24, 45, 270);
				g2.setColor(new Color(30, 32, 40));
				g2.fillArc(x+12, y+4, 24, 24, 45, 270);
				g2.dispose();
			}
		};
		final Icon iconoSol = new Icon() {
			public int getIconWidth() { return 32; }
			public int getIconHeight() { return 32; }
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				// Fondo del botón ya es oscuro, así que el sol resalta
				g2.setColor(new Color(255, 215, 0));
				g2.fillOval(x+6, y+6, 20, 20);
				g2.setStroke(new BasicStroke(3));
				for (int i = 0; i < 8; i++) {
					double angle = Math.PI/4 * i;
					int x1 = (int)(x+16+Math.cos(angle)*14);
					int y1 = (int)(y+16+Math.sin(angle)*14);
					int x2 = (int)(x+16+Math.cos(angle)*24);
					int y2 = (int)(y+16+Math.sin(angle)*24);
					g2.drawLine(x1, y1, x2, y2);
				}
				g2.dispose();
			}
		};
		btnLuna.setIcon(iconoLuna);

		btnLuna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modoOscuro = !modoOscuro;
				btnLuna.setIcon(modoOscuro ? iconoSol : iconoLuna);
				btnLuna.setBackground(modoOscuro ? new Color(40, 40, 50) : amarillo);
				aplicarModoOscuro();
			}
		});
		panelLuna.add(btnLuna);

		panelInferior.add(panelLuna, BorderLayout.WEST);
		panelInferior.add(panelSalir, BorderLayout.EAST);

		contentPane.add(panelCentral, BorderLayout.CENTER);
		contentPane.add(panelInferior, BorderLayout.SOUTH);

		// Guardar referencias para cambio de modo
		this.lblBienvenida = lblBienvenida;
		this.panelCentral = panelCentral;
		this.panelInferior = panelInferior;
		this.menuBar = menuBar;
		this.menuAdd = menuAdd;
		this.itemEstudiante = itemEstudiante;
		this.itemTrabajador = itemTrabajador;
		this.menuPlanificar = menuPlanificar;
		this.menuCalendario = menuCalendario;
		this.menuReportes = menuReportes;
		this.menuValores = menuValores;
		this.btnSalir = btnSalir;
		this.btnLuna = btnLuna;
		this.iconoLuna = iconoLuna;
		this.iconoSol = iconoSol;

		// Referencias para menú "Mostrar"
		this.menuVer = menuVer;
		this.verEstudiantes = verEstudiantes;
		this.verProfesores = verProfesores;
		this.verGuardias = verGuardias;
	}

	// Referencias para modo oscuro
	private JLabel lblBienvenida;
	private JPanel panelCentral, panelInferior;
	private JMenuBar menuBar;
	private JMenu menuAdd, menuVer;
	private JMenuItem itemEstudiante, itemTrabajador, menuPlanificar, menuCalendario, menuReportes, menuValores;
	private JMenuItem verEstudiantes, verProfesores, verGuardias;
	private JButton btnSalir, btnLuna;
	private Icon iconoLuna, iconoSol;

	private void aplicarModoOscuro() {
		Color fondo = modoOscuro ? darkBg : amarillo;
		Color texto = modoOscuro ? darkFg : negro;
		Color boton = modoOscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;

		contentPane.setBackground(fondo);
		panelCentral.setBackground(fondo);
		panelInferior.setBackground(fondo);
		lblBienvenida.setForeground(texto);

		// Menú principal y "Mostrar" siempre amarillo/negro
		menuBar.setBackground(amarillo);
		menuAdd.setBackground(amarillo);
		menuAdd.setForeground(negro);
		menuVer.setBackground(amarillo);
		menuVer.setForeground(negro);

		if (modoOscuro) {
			verEstudiantes.setBackground(amarillo);
			verEstudiantes.setForeground(negro);
			verProfesores.setBackground(amarillo);
			verProfesores.setForeground(negro);
			verGuardias.setBackground(amarillo);
			verGuardias.setForeground(negro);
			itemEstudiante.setBackground(amarillo);
			itemEstudiante.setForeground(negro);
			itemTrabajador.setBackground(amarillo);
			itemTrabajador.setForeground(negro);
		} else {
			verEstudiantes.setBackground(darkBg);
			verEstudiantes.setForeground(amarillo);
			verProfesores.setBackground(darkBg);
			verProfesores.setForeground(amarillo);
			verGuardias.setBackground(darkBg);
			verGuardias.setForeground(amarillo);
			itemEstudiante.setBackground(darkBg);
			itemEstudiante.setForeground(amarillo);
			itemTrabajador.setBackground(darkBg);
			itemTrabajador.setForeground(amarillo);
		}

		verEstudiantes.setBorder(BorderFactory.createLineBorder(negro, 1));
		verProfesores.setBorder(BorderFactory.createLineBorder(negro, 1));
		verGuardias.setBorder(BorderFactory.createLineBorder(negro, 1));

		menuPlanificar.setBackground(fondo);
		menuPlanificar.setForeground(texto);
		menuCalendario.setBackground(fondo);
		menuCalendario.setForeground(texto);
		menuReportes.setBackground(fondo);
		menuReportes.setForeground(texto);
		menuValores.setBackground(fondo);
		menuValores.setForeground(texto);

		btnSalir.setBackground(boton);
		btnSalir.setForeground(amarilloSec);

		btnLuna.setBackground(fondo);
		btnLuna.setIcon(modoOscuro ? iconoSol : iconoLuna);

		// Cambiar modo en todos los frames abiertos
		for (Frame frame : JFrame.getFrames()) {
			if (frame instanceof AddEstudiantes) {
				((AddEstudiantes) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof AddTrabajadores) {
				((AddTrabajadores) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof EditCalendario) {
				((EditCalendario) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
		}
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
