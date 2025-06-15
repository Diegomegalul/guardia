package interfaz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logica.PlanificadorGuardias;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PlanificadorGuardias planificador;

	private boolean modoOscuro = false;
	private Color amarillo = new Color(255, 215, 0);
	private Color negro = Color.BLACK;
	private Color darkBg = new Color(30, 32, 40); // color principal modo oscuro
	private Color darkFg = new Color(220, 220, 220); // texto modo oscuro

	// Referencias para modo oscuro
	private JLabel lblBienvenida;
	private JPanel panelCentral, panelInferior;
	private JMenuBar menuBar;
	private JMenuItem itemEstudiante, itemTrabajador;
	private JMenuItem valoresEstudiantes, valoresTrabajadores;
	private JMenuItem planificarAuto, planificarRecuperacion, editarManual;
	private JMenuItem mostrarEstudiantes, mostrarTrabajadores, mostrarGuardiasPlanificadas, mostrarGuardiasCumplidas;
	private JMenuItem reporteRecuperacion, reporteVoluntarios, reporteEstInactivos, reporteFestivas;
	private JButton btnSalir, btnLuna;
	private Icon iconoLuna, iconoSol;

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
				Inicio.this.solicitarConfirmacionSalida();
			}
		});

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// Menú superior
		menuBar = new JMenuBar();
		menuBar.setBackground(amarillo);
		menuBar.setBorder(BorderFactory.createLineBorder(negro, 2)); // Borde negro para el menú

		// "Añadir Personas" (no seleccionable, solo submenu)
		JMenu menuAdd = new JMenu("Añadir Personas");
		menuAdd.setFont(new Font("Arial", Font.BOLD, 16));
		menuAdd.setForeground(negro);
		menuAdd.setOpaque(true);
		menuAdd.setBackground(amarillo);
		menuAdd.setBorder(BorderFactory.createLineBorder(negro, 1)); // Borde negro

		itemEstudiante = new JMenuItem("Añadir Estudiante");
		itemEstudiante.setFont(new Font("Arial", Font.PLAIN, 15));
		itemEstudiante.setBackground(darkBg);
		itemEstudiante.setForeground(amarillo);
		itemEstudiante.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemEstudiante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof AddEstudiantes && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					AddEstudiantes frame = new AddEstudiantes(planificador);
					if (modoOscuro) frame.aplicarModoOscuro(modoOscuro, darkBg, darkFg, new Color(60, 63, 80), amarillo);
					frame.setVisible(true);
				}
			}
		});

		itemTrabajador = new JMenuItem("Añadir Trabajador");
		itemTrabajador.setFont(new Font("Arial", Font.PLAIN, 15));
		itemTrabajador.setBackground(darkBg);
		itemTrabajador.setForeground(amarillo);
		itemTrabajador.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemTrabajador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof AddTrabajadores && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					AddTrabajadores frame = new AddTrabajadores(planificador, null, null);
					if (modoOscuro) frame.aplicarModoOscuro(modoOscuro, darkBg, darkFg, new Color(60, 63, 80), amarillo);
					frame.setVisible(true);
				}
			}
		});

		menuAdd.add(itemEstudiante);
		menuAdd.add(itemTrabajador);
		menuBar.add(menuAdd); // SIEMPRE al principio

		// Menú Planificar Guardias (después de Añadir Personas)
		JMenu menuPlanificar = new JMenu("Planificar Guardias");
		menuPlanificar.setOpaque(true);
		menuPlanificar.setFont(new Font("Arial", Font.BOLD, 16));
		menuPlanificar.setBackground(amarillo);
		menuPlanificar.setForeground(negro);
		menuPlanificar.setBorder(BorderFactory.createLineBorder(negro, 1));

		planificarAuto = new JMenuItem("Planificar Auto");
		planificarAuto.setFont(new Font("Arial", Font.PLAIN, 15));
		planificarAuto.setBackground(darkBg);
		planificarAuto.setForeground(amarillo);
		planificarAuto.setBorder(BorderFactory.createLineBorder(negro, 1));
		planificarAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof PlanGuardias && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					PlanGuardias frame = new PlanGuardias(planificador);
					frame.setVisible(true);
					if (modoOscuro) {
						frame.aplicarModoOscuro(
								modoOscuro,
								darkBg,
								darkFg,
								new Color(60, 63, 80),
								amarillo
						);
					}
				}
			}
		});

		planificarRecuperacion = new JMenuItem("Planificar Recuperación");
		planificarRecuperacion.setFont(new Font("Arial", Font.PLAIN, 15));
		planificarRecuperacion.setBackground(darkBg);
		planificarRecuperacion.setForeground(amarillo);
		planificarRecuperacion.setBorder(BorderFactory.createLineBorder(negro, 1));
		planificarRecuperacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Planificar recuperación (próximamente).");
			}
		});

		editarManual = new JMenuItem("Editar Manualmente");
		editarManual.setFont(new Font("Arial", Font.PLAIN, 15));
		editarManual.setBackground(darkBg);
		editarManual.setForeground(amarillo);
		editarManual.setBorder(BorderFactory.createLineBorder(negro, 1));
		editarManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Edición manual de guardias (próximamente).");
			}
		});

		menuPlanificar.add(planificarAuto);
		menuPlanificar.add(planificarRecuperacion);
		menuPlanificar.add(editarManual);
		menuBar.add(menuPlanificar);

		// Menú Editar Calendario (después de Planificar)
		JMenuItem menuCalendario = new JMenuItem("Editar Calendario");
		menuCalendario.setOpaque(true);
		menuCalendario.setFont(new Font("Arial", Font.BOLD, 16));
		menuCalendario.setBackground(amarillo);
		menuCalendario.setForeground(negro);
		menuCalendario.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuCalendario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof EditCalendario && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					EditCalendario frame = new EditCalendario(planificador);
					if (modoOscuro) frame.aplicarModoOscuro(modoOscuro, darkBg, darkFg, new Color(60, 63, 80), amarillo);
					frame.setVisible(true);
				}
			}
		});
		menuBar.add(menuCalendario);

		// Menú Mostrar (después de Editar Calendario)
		JMenu menuMostrar = new JMenu("Mostrar");
		menuMostrar.setOpaque(true);
		menuMostrar.setFont(new Font("Arial", Font.BOLD, 16));
		menuMostrar.setBackground(amarillo);
		menuMostrar.setForeground(negro);
		menuMostrar.setBorder(BorderFactory.createLineBorder(negro, 1));

		mostrarEstudiantes = new JMenuItem("Estudiantes");
		mostrarEstudiantes.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarEstudiantes.setBackground(darkBg);
		mostrarEstudiantes.setForeground(amarillo);
		mostrarEstudiantes.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarEstudiantes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof VerEstudiantes && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					VerEstudiantes frame = new VerEstudiantes(planificador);
					frame.setVisible(true);
				}
			}
		});

		mostrarTrabajadores = new JMenuItem("Trabajadores");
		mostrarTrabajadores.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarTrabajadores.setBackground(darkBg);
		mostrarTrabajadores.setForeground(amarillo);
		mostrarTrabajadores.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarTrabajadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof VerTrabajadores && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					VerTrabajadores frame = new VerTrabajadores();
					frame.setVisible(true);
				}
			}
		});

		mostrarGuardiasPlanificadas = new JMenuItem("Guardias planificadas");
		mostrarGuardiasPlanificadas.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarGuardiasPlanificadas.setBackground(darkBg);
		mostrarGuardiasPlanificadas.setForeground(amarillo);
		mostrarGuardiasPlanificadas.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarGuardiasPlanificadas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof GuardiasPlanificadas && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					GuardiasPlanificadas frame = new GuardiasPlanificadas();
					frame.setVisible(true);
				}
			}
		});

		mostrarGuardiasCumplidas = new JMenuItem("Guardias cumplidas");
		mostrarGuardiasCumplidas.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarGuardiasCumplidas.setBackground(darkBg);
		mostrarGuardiasCumplidas.setForeground(amarillo);
		mostrarGuardiasCumplidas.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarGuardiasCumplidas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof GuardiasCumplidas && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					GuardiasCumplidas frame = new GuardiasCumplidas();
					frame.setVisible(true);
				}
			}
		});

		menuMostrar.add(mostrarEstudiantes);
		menuMostrar.add(mostrarTrabajadores);
		menuMostrar.add(mostrarGuardiasPlanificadas);
		menuMostrar.add(mostrarGuardiasCumplidas);
		menuBar.add(menuMostrar);

		// Menú Reportes (después de Mostrar)
		JMenu menuReportes = new JMenu("Reportes");
		menuReportes.setOpaque(true);
		menuReportes.setFont(new Font("Arial", Font.BOLD, 16));
		menuReportes.setBackground(amarillo);
		menuReportes.setForeground(negro);
		menuReportes.setBorder(BorderFactory.createLineBorder(negro, 1));

		JMenuItem reporteRecuperacion = new JMenuItem("Guardias Recuperacion");
		reporteRecuperacion.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteRecuperacion.setBackground(darkBg);
		reporteRecuperacion.setForeground(amarillo);
		reporteRecuperacion.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteRecuperacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Reporte de guardias de recuperación (próximamente).");
			}
		});

		JMenuItem reporteVoluntarios = new JMenuItem("Profesores Voluntarios");
		reporteVoluntarios.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteVoluntarios.setBackground(darkBg);
		reporteVoluntarios.setForeground(amarillo);
		reporteVoluntarios.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteVoluntarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Reporte de profesores voluntarios (próximamente).");
			}
		});

		JMenuItem reporteEstInactivos = new JMenuItem("Estudiantes Inactivos");
		reporteEstInactivos.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteEstInactivos.setBackground(darkBg);
		reporteEstInactivos.setForeground(amarillo);
		reporteEstInactivos.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteEstInactivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Reporte de estudiantes inactivos (próximamente).");
			}
		});

		JMenuItem reporteFestivas = new JMenuItem("Guardias Festivas");
		reporteFestivas.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteFestivas.setBackground(darkBg);
		reporteFestivas.setForeground(amarillo);
		reporteFestivas.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteFestivas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Inicio.this, "Reporte de guardias festivas (próximamente).");
			}
		});

		menuReportes.add(reporteRecuperacion);
		menuReportes.add(reporteVoluntarios);
		menuReportes.add(reporteEstInactivos);
		menuReportes.add(reporteFestivas);
		menuBar.add(menuReportes);

		// Menú Valores de Prueba (al final)
		JMenu menuValores = new JMenu("Valores de prueba");
		menuValores.setOpaque(true);
		menuValores.setFont(new Font("Arial", Font.BOLD, 16));
		menuValores.setBackground(amarillo);
		menuValores.setForeground(negro);
		menuValores.setBorder(BorderFactory.createLineBorder(negro, 1));

		JMenuItem valoresEstudiantes = new JMenuItem("Estudiantes");
		valoresEstudiantes.setFont(new Font("Arial", Font.PLAIN, 15));
		valoresEstudiantes.setBackground(darkBg);
		valoresEstudiantes.setForeground(amarillo);
		valoresEstudiantes.setBorder(BorderFactory.createLineBorder(negro, 1));
		valoresEstudiantes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame.getClass().getSimpleName().equals("VerEstudiantes") && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					planificador.agregarEstudiantesMujeresPrueba();
					planificador.agregarEstudiantesVaronesPrueba();
					JOptionPane.showMessageDialog(Inicio.this, "Estudiantes de prueba cargados.");
				}
			}
		});

		JMenuItem valoresTrabajadores = new JMenuItem("Trabajadores");
		valoresTrabajadores.setFont(new Font("Arial", Font.PLAIN, 15));
		valoresTrabajadores.setBackground(darkBg);
		valoresTrabajadores.setForeground(amarillo);
		valoresTrabajadores.setBorder(BorderFactory.createLineBorder(negro, 1));
		valoresTrabajadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				for (Frame frame : JFrame.getFrames()) {
					if (frame.getClass().getSimpleName().equals("VerTrabajadores") && frame.isVisible()) {
						frame.toFront();
						frame.requestFocus();
						found = true;
						break;
					}
				}
				if (!found) {
					planificador.agregarProfesoresPrueba();
					JOptionPane.showMessageDialog(Inicio.this, "Trabajadores de prueba cargados.");
				}
			}
		});

		menuValores.add(valoresEstudiantes);
		menuValores.add(valoresTrabajadores);
		menuBar.add(menuValores);

		setJMenuBar(menuBar);

		// Panel central con mensaje de bienvenida o imagen
		panelCentral = new JPanel();
		panelCentral.setBackground(amarillo);
		panelCentral.setLayout(new BorderLayout());

		lblBienvenida = new JLabel("Bienvenido al Sistema de Guardias");
		lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
		lblBienvenida.setForeground(negro);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setBorder(new EmptyBorder(80, 10, 10, 10));
		panelCentral.add(lblBienvenida, BorderLayout.CENTER);

		// Panel inferior para el botón salir y el botón de modo oscuro
		panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(amarillo);

		JPanel panelSalir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelSalir.setBackground(amarillo);

		btnSalir = new JButton("Salir") {
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

		JPanel panelLuna = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelLuna.setBackground(amarillo);

		btnLuna = new JButton() {
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				if (modoOscuro) {
					setBackground(new Color(40, 40, 50));
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

		iconoLuna = new Icon() {
			public int getIconWidth() { return 32; }
			public int getIconHeight() { return 32; }
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(240, 230, 140));
				g2.fillArc(x+4, y+4, 24, 24, 45, 270);
				g2.setColor(new Color(30, 32, 40));
				g2.fillArc(x+12, y+4, 24, 24, 45, 270);
				g2.dispose();
			}
		};
		iconoSol = new Icon() {
			public int getIconWidth() { return 32; }
			public int getIconHeight() { return 32; }
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 215, 0));
				g2.fillOval(x+6, y+6, 20, 20);
				g2.setStroke(new BasicStroke(3));
				for (int i = 0; i < 8; i++) {
					double angle = Math.PI/4 * i;
					int x1 = (int)(x+16+Math.cos(angle)*14);
					int y1 = (int)(y+16+Math.sin(angle)*14);
					int x2 = (int)(x+16+Math.cos(angle)*24);
					int y2 = (int)(x+16+Math.sin(angle)*24);
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
	}

	private void aplicarModoOscuro() {
		Color fondo = modoOscuro ? darkBg : amarillo;
		Color texto = modoOscuro ? darkFg : negro;
		Color boton = modoOscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;

		if (menuBar != null) {
			menuBar.setBackground(amarillo);
			menuBar.setOpaque(true);
		}
		if (contentPane != null) contentPane.setBackground(fondo);
		if (panelCentral != null) panelCentral.setBackground(fondo);
		if (panelInferior != null) panelInferior.setBackground(fondo);
		if (lblBienvenida != null) lblBienvenida.setForeground(texto);

		if (itemEstudiante != null) {
			itemEstudiante.setBackground(modoOscuro ? amarillo : darkBg);
			itemEstudiante.setForeground(modoOscuro ? negro : amarillo);
		}
		if (reporteFestivas != null) {
			reporteFestivas.setBackground(modoOscuro ? amarillo : darkBg);
			reporteFestivas.setForeground(modoOscuro ? negro : amarillo);
		}
		if (reporteEstInactivos != null) {
			reporteEstInactivos.setBackground(modoOscuro ? amarillo : darkBg);
			reporteEstInactivos.setForeground(modoOscuro ? negro : amarillo);
		}
		if (reporteRecuperacion != null) {
			reporteRecuperacion.setBackground(modoOscuro ? amarillo : darkBg);
			reporteRecuperacion.setForeground(modoOscuro ? negro : amarillo);
		}
		if (reporteVoluntarios != null) {
			reporteVoluntarios.setBackground(modoOscuro ? amarillo : darkBg);
			reporteVoluntarios.setForeground(modoOscuro ? negro : amarillo);
		}
		if (itemTrabajador != null) {
			itemTrabajador.setBackground(modoOscuro ? amarillo : darkBg);
			itemTrabajador.setForeground(modoOscuro ? negro : amarillo);
		}
		if (mostrarGuardiasCumplidas != null) {
			mostrarGuardiasCumplidas.setBackground(modoOscuro ? amarillo : darkBg);
			mostrarGuardiasCumplidas.setForeground(modoOscuro ? negro : amarillo);
		}
		if (mostrarGuardiasPlanificadas != null) {
			mostrarGuardiasPlanificadas.setBackground(modoOscuro ? amarillo : darkBg);
			mostrarGuardiasPlanificadas.setForeground(modoOscuro ? negro : amarillo);
		}
		if (mostrarTrabajadores != null) {
			mostrarTrabajadores.setBackground(modoOscuro ? amarillo : darkBg);
			mostrarTrabajadores.setForeground(modoOscuro ? negro : amarillo);
		}
		if (mostrarEstudiantes != null) {
			mostrarEstudiantes.setBackground(modoOscuro ? amarillo : darkBg);
			mostrarEstudiantes.setForeground(modoOscuro ? negro : amarillo);
		}
		if (editarManual != null) {
			editarManual.setBackground(modoOscuro ? amarillo : darkBg);
			editarManual.setForeground(modoOscuro ? negro : amarillo);
		}
		if (planificarAuto != null) {
			planificarAuto.setBackground(modoOscuro ? amarillo : darkBg);
			planificarAuto.setForeground(modoOscuro ? negro : amarillo);
		}
		if (planificarRecuperacion != null) {
			planificarRecuperacion.setBackground(modoOscuro ? amarillo : darkBg);
			planificarRecuperacion.setForeground(modoOscuro ? negro : amarillo);
		}
		if (valoresEstudiantes != null) {
			valoresEstudiantes.setBackground(modoOscuro ? amarillo : darkBg);
			valoresEstudiantes.setForeground(modoOscuro ? negro : amarillo);
		}
		if (valoresTrabajadores != null) {
			valoresTrabajadores.setBackground(modoOscuro ? amarillo : darkBg);
			valoresTrabajadores.setForeground(modoOscuro ? negro : amarillo);
		}
		if (btnSalir != null) {
			btnSalir.setBackground(boton);
			btnSalir.setForeground(amarilloSec);
		}
		if (btnLuna != null) {
			btnLuna.setBackground(fondo);
			btnLuna.setIcon(modoOscuro ? iconoSol : iconoLuna);
		}

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
			if (frame instanceof PlanGuardias) {
				((PlanGuardias) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
		}
	}

	protected void solicitarConfirmacionSalida() {
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

