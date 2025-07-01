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
	private JPanel panelCentral, panelInferior, panelLuna, panelSalir, panelCentroInferior;
	private JMenuBar menuBar;
	private JMenuItem itemEstudiante, itemTrabajador;
	private JMenuItem valoresEstudiantes, valoresTrabajadores;
	private JMenuItem planificarAuto;
	private JMenuItem mostrarEstudiantes, mostrarTrabajadores, mostrarGuardiasPlanificadas, mostrarGuardiasCumplidas,
			mostrarGuardiasIncumplidas;
	private JMenuItem reporteRecuperacion, reporteVoluntarios, reporteEstInactivos, reporteFestivas, itemDiasFestivos;
	private JButton btnSalir, btnLuna;
	private Icon iconoLuna, iconoSol;
	private ImageIcon fondoClaroIcon;
	private ImageIcon fondoOscuroIcon;

	// Nuevos paneles para agregar estudiantes y trabajadores
	private JPanel panelAddEstudiantes;
	private JPanel panelAddTrabajadores;
	// Nuevo panel para EditCalendario
	private JPanel panelEditCalendario;
	// Nuevo panel para EditGuardia
	private JPanel panelEditGuardia;
	// Nuevo panel para PlanGuardias
	private JPanel panelPlanGuardias;
	// Nuevo panel para IntercambioPersona
	private JPanel panelIntercambioPersona;

	// Pila para navegación de paneles
	private java.util.Stack<JPanel> pilaPaneles = new java.util.Stack<>();
	private JButton btnVolver;

	public Inicio() {
		setMinimumSize(new Dimension(900, 600));
		// Instancia singleton del planificador
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
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

		// Cargar imágenes de fondo de forma segura
		java.net.URL urlClaro = getClass().getResource("/imagenes/Fondo claro.png");
		java.net.URL urlOscuro = getClass().getResource("/imagenes/Fondo oscuro.png");
		if (urlClaro != null) {
			fondoClaroIcon = new ImageIcon(urlClaro);
		} else {
			System.err.println("No se encontró la imagen: /imagenes/Fondo claro.png");
			fondoClaroIcon = null;
		}
		if (urlOscuro != null) {
			fondoOscuroIcon = new ImageIcon(urlOscuro);
		} else {
			System.err.println("No se encontró la imagen: /imagenes/Fondo oscuro.png");
			fondoOscuroIcon = null;
		}

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
				mostrarPanelCentral(panelAddEstudiantes);
			}
		});

		itemTrabajador = new JMenuItem("Añadir Trabajador");
		itemTrabajador.setFont(new Font("Arial", Font.PLAIN, 15));
		itemTrabajador.setBackground(darkBg);
		itemTrabajador.setForeground(amarillo);
		itemTrabajador.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemTrabajador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelCentral(panelAddTrabajadores);
			}
		});

		menuAdd.add(itemEstudiante);
		menuAdd.add(itemTrabajador);
		menuBar.add(menuAdd); // SIEMPRE al principio

		// Menú Planificar Guardias (después de Añadir Personas)
		JMenu menuPlanificar = new JMenu("Guardias");
		menuPlanificar.setOpaque(true);
		menuPlanificar.setFont(new Font("Arial", Font.BOLD, 16));
		menuPlanificar.setBackground(amarillo);
		menuPlanificar.setForeground(negro);
		menuPlanificar.setBorder(BorderFactory.createLineBorder(negro, 1));

		planificarAuto = new JMenuItem("Planificar ");
		planificarAuto.setFont(new Font("Arial", Font.PLAIN, 15));
		planificarAuto.setBackground(darkBg);
		planificarAuto.setForeground(amarillo);
		planificarAuto.setBorder(BorderFactory.createLineBorder(negro, 1));
		planificarAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelCentral(panelPlanGuardias);
			}
		});

		menuPlanificar.add(planificarAuto);
		menuBar.add(menuPlanificar);

		mostrarGuardiasPlanificadas = new JMenuItem("G.Planificadas");
		menuPlanificar.add(mostrarGuardiasPlanificadas);
		mostrarGuardiasPlanificadas.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarGuardiasPlanificadas.setBackground(darkBg);
		mostrarGuardiasPlanificadas.setForeground(amarillo);
		mostrarGuardiasPlanificadas.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarGuardiasPlanificadas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelGuardiasPlanificadas();
			}
		});

		mostrarGuardiasCumplidas = new JMenuItem("G.Cumplidas");
		menuPlanificar.add(mostrarGuardiasCumplidas);
		mostrarGuardiasCumplidas.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarGuardiasCumplidas.setBackground(darkBg);
		mostrarGuardiasCumplidas.setForeground(amarillo);
		mostrarGuardiasCumplidas.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarGuardiasCumplidas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelGuardiasCumplidas();
			}
		});

		// Nuevo submenu G.Incumplidas
		mostrarGuardiasIncumplidas = new JMenuItem("G.Incumplidas");
		menuPlanificar.add(mostrarGuardiasIncumplidas);
		mostrarGuardiasIncumplidas.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarGuardiasIncumplidas.setBackground(darkBg);
		mostrarGuardiasIncumplidas.setForeground(amarillo);
		mostrarGuardiasIncumplidas.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarGuardiasIncumplidas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelGuardiasIncumplidas();
			}
		});

		// Menú Editar Calendario (después de Planificar)
		JMenuItem menuCalendario = new JMenuItem("Editar Calendario");
		menuCalendario.setOpaque(true);
		menuCalendario.setFont(new Font("Arial", Font.BOLD, 16));
		menuCalendario.setBackground(amarillo);
		menuCalendario.setForeground(negro);
		menuCalendario.setBorder(BorderFactory.createLineBorder(negro, 1));
		menuCalendario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelCentral(panelEditCalendario);
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
				mostrarPanelVerEstudiantes();
			}
		});

		mostrarTrabajadores = new JMenuItem("Trabajadores");
		mostrarTrabajadores.setFont(new Font("Arial", Font.PLAIN, 15));
		mostrarTrabajadores.setBackground(darkBg);
		mostrarTrabajadores.setForeground(amarillo);
		mostrarTrabajadores.setBorder(BorderFactory.createLineBorder(negro, 1));
		mostrarTrabajadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelVerTrabajadores();
			}
		});

		menuMostrar.add(mostrarEstudiantes);
		menuMostrar.add(mostrarTrabajadores);
		menuBar.add(menuMostrar);

		// Menú Reportes (después de Mostrar)
		JMenu menuReportes = new JMenu("Reportes");
		menuReportes.setOpaque(true);
		menuReportes.setFont(new Font("Arial", Font.BOLD, 16));
		menuReportes.setBackground(amarillo);
		menuReportes.setForeground(negro);
		menuReportes.setBorder(BorderFactory.createLineBorder(negro, 1));

		reporteRecuperacion = new JMenuItem("Guardias Recuperacion");
		reporteRecuperacion.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteRecuperacion.setBackground(darkBg);
		reporteRecuperacion.setForeground(amarillo);
		reporteRecuperacion.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteRecuperacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelGuardiasRecuperacion();
			}
		});

		reporteVoluntarios = new JMenuItem("Profesores Voluntarios");
		reporteVoluntarios.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteVoluntarios.setBackground(darkBg);
		reporteVoluntarios.setForeground(amarillo);
		reporteVoluntarios.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteVoluntarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelProfVoluntarios();
			}
		});

		reporteEstInactivos = new JMenuItem("Estudiantes Inactivos");
		reporteEstInactivos.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteEstInactivos.setBackground(darkBg);
		reporteEstInactivos.setForeground(amarillo);
		reporteEstInactivos.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteEstInactivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelEstInactivos();
			}
		});

		reporteFestivas = new JMenuItem("Guardias Festivas");
		reporteFestivas.setFont(new Font("Arial", Font.PLAIN, 15));
		reporteFestivas.setBackground(darkBg);
		reporteFestivas.setForeground(amarillo);
		reporteFestivas.setBorder(BorderFactory.createLineBorder(negro, 1));
		reporteFestivas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelGuardiasFestivas();
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

		valoresEstudiantes = new JMenuItem("Estudiantes");
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
					// Solo agregar si no se han agregado antes
					try {
						java.lang.reflect.Field f1 = planificador.getClass()
								.getDeclaredField("estudiantesMujeresPruebaAgregados");
						java.lang.reflect.Field f2 = planificador.getClass()
								.getDeclaredField("estudiantesVaronesPruebaAgregados");
						f1.setAccessible(true);
						f2.setAccessible(true);
						boolean mujeresAgregadas = f1.getBoolean(planificador);
						boolean varonesAgregados = f2.getBoolean(planificador);
						if (!mujeresAgregadas && !varonesAgregados) {
							planificador.agregarEstudiantesMujeresPrueba();
							planificador.agregarEstudiantesVaronesPrueba();
							JOptionPane.showMessageDialog(Inicio.this, "Estudiantes de prueba cargados.");
						} else {
							JOptionPane.showMessageDialog(Inicio.this, "Los estudiantes de prueba ya fueron cargados.");
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(Inicio.this,
								"Error al verificar si los estudiantes ya fueron cargados.");
					}
				}
			}
		});

		valoresTrabajadores = new JMenuItem("Trabajadores");
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
					// Solo agregar si no se han agregado antes
					try {
						java.lang.reflect.Field f = planificador.getClass()
								.getDeclaredField("profesoresPruebaAgregados");
						f.setAccessible(true);
						boolean yaAgregado = f.getBoolean(planificador);
						if (!yaAgregado) {
							planificador.agregarProfesoresPrueba();
							JOptionPane.showMessageDialog(Inicio.this, "Trabajadores de prueba cargados.");
						} else {
							JOptionPane.showMessageDialog(Inicio.this,
									"Los trabajadores de prueba ya fueron cargados.");
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(Inicio.this,
								"Error al verificar si los trabajadores ya fueron cargados.");
					}
				}
			}
		});

		itemDiasFestivos = new JMenuItem("Días Festivos");
		itemDiasFestivos.setFont(new Font("Arial", Font.PLAIN, 15));
		itemDiasFestivos.setBackground(darkBg);
		itemDiasFestivos.setForeground(amarillo);
		itemDiasFestivos.setBorder(BorderFactory.createLineBorder(negro, 1));
		itemDiasFestivos.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				PlanificadorGuardias.getInstancia().getCalendario().agregarDiasFestivosPrueba();
				JOptionPane.showMessageDialog(
						Inicio.this,
						"Días festivos de prueba cargados correctamente.",
						"Información",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		menuValores.add(valoresEstudiantes);
		menuValores.add(valoresTrabajadores);
		menuValores.add(itemDiasFestivos);
		menuBar.add(menuValores);

		setJMenuBar(menuBar);

		// Panel central con mensaje de bienvenida o imagen
		panelCentral = new JPanel();
		panelCentral.setBackground(amarillo);
		panelCentral.setLayout(new BorderLayout());

		lblBienvenida = new JLabel();
		lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
		lblBienvenida.setForeground(negro);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setBorder(new EmptyBorder(80, 10, 10, 10));
		// Hacer que la imagen ocupe todo el panel central
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setVerticalAlignment(SwingConstants.CENTER);
		lblBienvenida.setOpaque(false);

		// Ajustar el layout para que el label ocupe todo el espacio
		panelCentral.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				actualizarImagenBienvenida();
			}
		});

		panelCentral.add(lblBienvenida, BorderLayout.CENTER);

		// Crear paneles de agregar estudiantes, trabajadores, calendario, editar
		// guardia, planificar guardias e intercambio persona
		panelAddEstudiantes = new AddEstudiantes(planificador, null, null).getPanelPrincipal();
		panelAddTrabajadores = new AddTrabajadores(planificador, null).getPanelPrincipal();
		panelEditCalendario = new EditCalendario(planificador).getPanelPrincipal();
		panelEditGuardia = new EditGuardia(null).getPanelPrincipal();
		// Cambia aquí: pasa 'this' al constructor de PlanGuardias
		panelPlanGuardias = new PlanGuardias(planificador, this).getPanelPrincipal();
		panelIntercambioPersona = new IntercambioPersona(null).getPanelPrincipal();

		// Inicialmente solo el panel central visible
		contentPane.add(panelCentral, BorderLayout.CENTER);

		// Aplicar modo oscuro al inicio
		aplicarModoOscuro();

		// Panel inferior para el botón salir, volver y el botón de modo oscuro
		panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(amarillo);

		// Panel Salir (derecha) y Luna (izquierda) del mismo tamaño
		panelSalir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelSalir.setBackground(amarillo);
		panelSalir.setPreferredSize(new Dimension(90, 55)); // Tamaño fijo

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
			public int getIconWidth() {
				return 20;
			}

			public int getIconHeight() {
				return 20;
			}

			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.RED);
				g2.fillOval(x, y, 18, 18);
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(x + 5, y + 5, x + 13, y + 13);
				g2.drawLine(x + 13, y + 5, x + 5, y + 13);
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

		panelLuna = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelLuna.setBackground(amarillo);
		panelLuna.setPreferredSize(new Dimension(90, 55)); // Tamaño fijo

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
			public int getIconWidth() {
				return 32;
			}

			public int getIconHeight() {
				return 32;
			}

			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(240, 230, 140));
				g2.fillArc(x + 4, y + 4, 24, 24, 45, 270);
				g2.setColor(new Color(30, 32, 40));
				g2.fillArc(x + 12, y + 4, 24, 24, 45, 270);
				g2.dispose();
			}
		};
		iconoSol = new Icon() {
			public int getIconWidth() {
				return 32;
			}

			public int getIconHeight() {
				return 32;
			}

			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 215, 0));
				g2.fillOval(x + 6, y + 6, 20, 20);
				g2.setStroke(new BasicStroke(3));
				for (int i = 0; i < 8; i++) {
					double angle = Math.PI / 4 * i;
					int x1 = (int) (x + 16 + Math.cos(angle) * 14);
					int y1 = (int) (x + 16 + Math.sin(angle) * 14);
					int x2 = (int) (x + 16 + Math.cos(angle) * 24);
					int y2 = (int) (x + 16 + Math.sin(angle) * 24);
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

		// Botón Volver (centrado)
		btnVolver = new JButton("Inicio");
		btnVolver.setFont(new Font("Arial", Font.BOLD, 16));
		btnVolver.setBackground(negro);
		btnVolver.setForeground(amarillo);
		btnVolver.setFocusPainted(false);
		btnVolver.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnVolver.setContentAreaFilled(false);
		btnVolver.setOpaque(true);
		btnVolver.setVisible(true);

		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPanelBienvenida();
			}
		});

		btnVolver.setVisible(false);
		panelCentroInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelInferior.add(panelCentroInferior, BorderLayout.CENTER);
		panelCentroInferior.setBackground(amarillo);
		panelCentroInferior.add(btnVolver);
	}

	private void actualizarImagenBienvenida() {
		if (lblBienvenida == null)
			return;
		ImageIcon iconoBase = modoOscuro ? fondoOscuroIcon : fondoClaroIcon;
		if (iconoBase != null && iconoBase.getIconWidth() > 0 && iconoBase.getIconHeight() > 0) {
			int ancho = panelCentral.getWidth();
			int alto = panelCentral.getHeight();
			if (ancho > 0 && alto > 0) {
				Image imgEscalada = iconoBase.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
				lblBienvenida.setIcon(new ImageIcon(imgEscalada));
			} else {
				lblBienvenida.setIcon(iconoBase);
			}
		}
	}

	private void aplicarModoOscuro() {
		Color fondo = modoOscuro ? darkBg : amarillo;
		Color texto = modoOscuro ? darkFg : negro;
		Color boton = modoOscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;

		// Solo aplicar colores recursivamente a todos los componentes
		setComponentColors(contentPane, modoOscuro, fondo, texto, boton, amarilloSec);

		// Actualizar la imagen de bienvenida si es necesario
		if (lblBienvenida != null) {
			lblBienvenida.setForeground(texto);
			actualizarImagenBienvenida();
		}

		// Seguir aplicando modo oscuro a frames hijos
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
			// Agrega estas líneas:
			if (frame instanceof GuardiasFestivas) {
				((GuardiasFestivas) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof EstInactivos) {
				((EstInactivos) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof GuardiasIncumplidas) {
				((GuardiasIncumplidas) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof GuardiasPlanificadas) {
				((GuardiasPlanificadas) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof GuardiasCumplidas) {
				((GuardiasCumplidas) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof VerTrabajadores) {
				((VerTrabajadores) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof VerEstudiantes) {
				((VerEstudiantes) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof GuardiasRecuperacion) {
				((GuardiasRecuperacion) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof ProfVoluntarios) {
				((ProfVoluntarios) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof VerGuardiasPersona) {
				((VerGuardiasPersona) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof EditGuardia) {
				((EditGuardia) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}
			if (frame instanceof IntercambioPersona) {
				((IntercambioPersona) frame).aplicarModoOscuro(modoOscuro, fondo, texto, boton, amarilloSec);
			}

		}
	}

	// Método recursivo para aplicar colores a todos los componentes
	private void setComponentColors(Component comp, boolean oscuro, Color fondo, Color texto, Color boton,
			Color amarilloSec) {
		if (comp instanceof JPanel) {
			comp.setBackground(fondo);
			for (Component child : ((JPanel) comp).getComponents()) {
				setComponentColors(child, oscuro, fondo, texto, boton, amarilloSec);
			}
		} else if (comp instanceof JLabel) {
			((JLabel) comp).setForeground(oscuro ? darkFg : negro);
		} else if (comp instanceof JTextField) {
			comp.setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
			((JTextField) comp).setForeground(oscuro ? darkFg : texto);
			((JTextField) comp).setCaretColor(oscuro ? Color.WHITE : Color.BLACK);
		} else if (comp instanceof JComboBox) {
			comp.setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
			comp.setForeground(oscuro ? darkFg : texto);
		} else if (comp instanceof JCheckBox) {
			comp.setBackground(fondo);
			((JCheckBox) comp).setForeground(oscuro ? darkFg : texto);
		} else if (comp instanceof JButton) {
			comp.setBackground(boton);
			comp.setForeground(amarilloSec);
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
				JOptionPane.QUESTION_MESSAGE);
		if (opcion == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	// Método para intercambiar el panel central por el panel deseado
	void mostrarPanelCentral(JPanel nuevoPanel) {
		JPanel actual = obtenerPanelActual();
		if (actual != null && actual != nuevoPanel) {
			pilaPaneles.push(actual);
		}
		mostrarPanelCentralInterno(nuevoPanel, true);
	}

	// Método interno para mostrar panel y controlar visibilidad del botón inicio
	private void mostrarPanelCentralInterno(JPanel nuevoPanel, boolean desdeMenu) {
		// Elimina solo si el panel no es null y está actualmente en el contentPane
		if (panelCentral != null && panelCentral.getParent() == contentPane)
			contentPane.remove(panelCentral);
		if (panelAddEstudiantes != null && panelAddEstudiantes.getParent() == contentPane)
			contentPane.remove(panelAddEstudiantes);
		if (panelAddTrabajadores != null && panelAddTrabajadores.getParent() == contentPane)
			contentPane.remove(panelAddTrabajadores);
		if (panelEditCalendario != null && panelEditCalendario.getParent() == contentPane)
			contentPane.remove(panelEditCalendario);
		if (panelEditGuardia != null && panelEditGuardia.getParent() == contentPane)
			contentPane.remove(panelEditGuardia);
		if (panelPlanGuardias != null && panelPlanGuardias.getParent() == contentPane)
			contentPane.remove(panelPlanGuardias);
		if (panelIntercambioPersona != null && panelIntercambioPersona.getParent() == contentPane)
			contentPane.remove(panelIntercambioPersona);

		contentPane.add(nuevoPanel, BorderLayout.CENTER);
		contentPane.revalidate();
		contentPane.repaint();

		if (panelCentral != null)
			panelCentral.setVisible(false);
		if (panelAddEstudiantes != null)
			panelAddEstudiantes.setVisible(false);
		if (panelAddTrabajadores != null)
			panelAddTrabajadores.setVisible(false);
		if (panelEditCalendario != null)
			panelEditCalendario.setVisible(false);
		if (panelEditGuardia != null)
			panelEditGuardia.setVisible(false);
		if (panelPlanGuardias != null)
			panelPlanGuardias.setVisible(false);
		if (panelIntercambioPersona != null)
			panelIntercambioPersona.setVisible(false);

		nuevoPanel.setVisible(true);

		// El botón "Inicio" siempre visible
		btnVolver.setVisible(nuevoPanel != panelCentral);
	}

	// Obtener el panel actualmente visible en el centro
	private JPanel obtenerPanelActual() {
		if (panelCentral.isVisible())
			return panelCentral;
		if (panelAddEstudiantes.isVisible())
			return panelAddEstudiantes;
		if (panelAddTrabajadores.isVisible())
			return panelAddTrabajadores;
		if (panelEditCalendario != null && panelEditCalendario.isVisible())
			return panelEditCalendario;
		if (panelEditGuardia != null && panelEditGuardia.isVisible())
			return panelEditGuardia;
		if (panelPlanGuardias != null && panelPlanGuardias.isVisible())
			return panelPlanGuardias;
		if (panelIntercambioPersona != null && panelIntercambioPersona.isVisible())
			return panelIntercambioPersona;
		return panelCentral; // fallback
	}

	// Métodos para mostrar los paneles principales de cada JFrame en el panel
	// central (todos como JPanel embebidos)
	public void mostrarPanelVerEstudiantes() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		VerEstudiantes frame = new VerEstudiantes(planificador);
		frame.aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
		JPanel panel = frame.getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelVerTrabajadores() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		JPanel panel = new VerTrabajadores(oscuro, fondo, texto, boton, amarilloSec).getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelProfVoluntarios() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		JPanel panel = new ProfVoluntarios(oscuro, fondo, texto, boton, amarilloSec).getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelGuardiasRecuperacion() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		JPanel panel = new GuardiasRecuperacion(oscuro, fondo, texto, boton, amarilloSec).getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelEstInactivos() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		JPanel panel = new EstInactivos(oscuro, fondo, texto, boton, amarilloSec).getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelGuardiasFestivas() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		JPanel panel = new GuardiasFestivas(oscuro, fondo, texto, boton, amarilloSec).getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelGuardiasPlanificadas() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		GuardiasPlanificadas frame = new GuardiasPlanificadas();
		frame.aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
		JPanel panel = frame.getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelGuardiasIncumplidas() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		GuardiasIncumplidas frame = new GuardiasIncumplidas();
		frame.aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
		JPanel panel = frame.getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelGuardiasCumplidas() {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		GuardiasCumplidas frame = new GuardiasCumplidas();
		frame.aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
		JPanel panel = frame.getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	public void mostrarPanelEditGuardia(logica.Guardia guardia) {
		boolean oscuro = modoOscuro;
		Color fondo = oscuro ? darkBg : amarillo;
		Color texto = oscuro ? darkFg : negro;
		Color boton = oscuro ? new Color(60, 63, 80) : negro;
		Color amarilloSec = amarillo;
		EditGuardia frame = new EditGuardia(guardia);
		frame.aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
		JPanel panel = frame.getPanelPrincipal();
		mostrarPanelCentral(panel);
	}

	// Nuevo método para mostrar el label de bienvenida (panelCentral)
	public void mostrarPanelBienvenida() {
		// Oculta todos los paneles secundarios si están visibles
		if (panelAddEstudiantes != null)
			panelAddEstudiantes.setVisible(false);
		if (panelAddTrabajadores != null)
			panelAddTrabajadores.setVisible(false);
		if (panelEditCalendario != null)
			panelEditCalendario.setVisible(false);
		if (panelEditGuardia != null)
			panelEditGuardia.setVisible(false);
		if (panelPlanGuardias != null)
			panelPlanGuardias.setVisible(false);
		if (panelIntercambioPersona != null)
			panelIntercambioPersona.setVisible(false);

		// Elimina todos los paneles secundarios del contentPane si están añadidos
		if (panelAddEstudiantes != null && panelAddEstudiantes.getParent() == contentPane)
			contentPane.remove(panelAddEstudiantes);
		if (panelAddTrabajadores != null && panelAddTrabajadores.getParent() == contentPane)
			contentPane.remove(panelAddTrabajadores);
		if (panelEditCalendario != null && panelEditCalendario.getParent() == contentPane)
			contentPane.remove(panelEditCalendario);
		if (panelEditGuardia != null && panelEditGuardia.getParent() == contentPane)
			contentPane.remove(panelEditGuardia);
		if (panelPlanGuardias != null && panelPlanGuardias.getParent() == contentPane)
			contentPane.remove(panelPlanGuardias);
		if (panelIntercambioPersona != null && panelIntercambioPersona.getParent() == contentPane)
			contentPane.remove(panelIntercambioPersona);

		// Elimina todos los paneles de reportes/listados embebidos si están añadidos
		Component[] components = contentPane.getComponents();
		for (Component comp : components) {
			if (comp != panelCentral && comp != panelInferior) {
				contentPane.remove(comp);
			}
		}

		// Mostrar el panelCentral (bienvenida)
		if (panelCentral.getParent() != contentPane) {
			contentPane.add(panelCentral, BorderLayout.CENTER);
		}
		panelCentral.setVisible(true);

		// Revalida y repinta para asegurar que el label de bienvenida se muestre
		contentPane.revalidate();
		contentPane.repaint();

		// Limpia la pila de navegación
		pilaPaneles.clear();

		btnVolver.setVisible(false);
	}
}