package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import logica.PlanificadorGuardias;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.Dimension;

public class PlanGuardias extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMonthChooser monthChooser;
	private JYearChooser yearChooser;
	private JButton btnPlanificar;
	private JTable tablaGuardias;
	private DefaultTableModel tablaModel;
	@SuppressWarnings("unused")
	private PlanificadorGuardias planificador;
	private JScrollPane scroll; //
	// Referencias para modo oscuro no lo he terminado
	private Color amarillo = new Color(255, 215, 0);
	private Color negro = Color.BLACK;

	private static PlanGuardias instancia = null;

	public static void mostrarVentana(PlanificadorGuardias planificador) {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new PlanGuardias(planificador);
			instancia.setVisible(true);
		} else {
			instancia.toFront();
			instancia.setState(JFrame.NORMAL);
		}
	}

	private Inicio ventanaInicio; // Referencia al frame de Inicio

	/**
	 * @wbp.parser.constructor
	 */
	public PlanGuardias(final PlanificadorGuardias planificador) {
		this(planificador, null);
	}

	public PlanGuardias(final PlanificadorGuardias planificador, Inicio inicio) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		this.planificador = planificador;
		this.ventanaInicio = inicio;
		setTitle("Planificar Guardias Automáticamente");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		contentPane.setBackground(amarillo);
		setContentPane(contentPane);

		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
		panelSuperior.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelSuperior.setBackground(amarillo);

		JLabel lblMes = new JLabel("Mes:");
		lblMes.setFont(new Font("Arial", Font.BOLD, 16));
		lblMes.setForeground(negro);
		panelSuperior.add(lblMes);

		monthChooser = new JMonthChooser();
		monthChooser.setFont(new Font("Arial", Font.PLAIN, 15));
		monthChooser.setBackground(Color.WHITE);
		monthChooser.setForeground(negro);
		panelSuperior.add(monthChooser);

		JLabel lblAnio = new JLabel("   Año:");
		lblAnio.setFont(new Font("Arial", Font.BOLD, 16));
		lblAnio.setForeground(negro);
		panelSuperior.add(lblAnio);

		yearChooser = new JYearChooser();
		yearChooser.setFont(new Font("Arial", Font.PLAIN, 15));
		yearChooser.setBackground(Color.WHITE);
		yearChooser.setForeground(negro);
		panelSuperior.add(yearChooser);

		btnPlanificar = new JButton("Planificar") {
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
		btnPlanificar.setSize(new Dimension(33, 9));
		btnPlanificar.setFont(new Font("Arial", Font.BOLD, 16));
		btnPlanificar.setBackground(negro);
		btnPlanificar.setForeground(amarillo);
		btnPlanificar.setFocusPainted(false);
		btnPlanificar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnPlanificar.setContentAreaFilled(false);
		btnPlanificar.setOpaque(true);

		panelSuperior.add(Box.createHorizontalStrut(20));
		panelSuperior.add(btnPlanificar);

		contentPane.add(panelSuperior, BorderLayout.NORTH);

		// Tabla de guardias planificadas
		String[] columnas = { "ID", "Tipo", "Persona", "Tipo Persona", "Fecha", "Hora Inicio", "Hora Fin" };
		tablaModel = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaGuardias = new JTable(tablaModel);
		tablaGuardias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tablaGuardias.setFont(new Font("Consolas", Font.PLAIN, 13));
		tablaGuardias.setRowHeight(24);
		tablaGuardias.setBackground(Color.WHITE);
		tablaGuardias.setForeground(negro);

		scroll = new JScrollPane(tablaGuardias);
		scroll.setBorder(BorderFactory.createLineBorder(amarillo, 2));
		contentPane.add(scroll, BorderLayout.CENTER);

		// Panel de botones de acción
		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);

		JButton btnEditar = new JButton("Intercambiar");
		btnEditar.setFont(new Font("Arial", Font.BOLD, 15));
		btnEditar.setBackground(negro);
		btnEditar.setForeground(amarillo);
		btnEditar.setFocusPainted(false);
		btnEditar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnEditar.setContentAreaFilled(false);
		btnEditar.setOpaque(true);

		JButton btnEditarGuardia = new JButton("Editar");
		btnEditarGuardia.setFont(new Font("Arial", Font.BOLD, 15));
		btnEditarGuardia.setBackground(negro);
		btnEditarGuardia.setForeground(amarillo);
		btnEditarGuardia.setFocusPainted(false);
		btnEditarGuardia.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnEditarGuardia.setContentAreaFilled(false);
		btnEditarGuardia.setOpaque(true);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.setFont(new Font("Arial", Font.BOLD, 15));
		btnActualizar.setBackground(negro);
		btnActualizar.setForeground(amarillo);
		btnActualizar.setFocusPainted(false);
		btnActualizar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnActualizar.setContentAreaFilled(false);
		btnActualizar.setOpaque(true);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 15));
		btnEliminar.setBackground(negro);
		btnEliminar.setForeground(amarillo);
		btnEliminar.setFocusPainted(false);
		btnEliminar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnEliminar.setContentAreaFilled(false);
		btnEliminar.setOpaque(true);

		panelBoton.add(btnEditar);
		panelBoton.add(btnEditarGuardia);
		panelBoton.add(btnActualizar);
		panelBoton.add(btnEliminar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);

		// Acción intercambiar
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablaGuardias.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(PlanGuardias.this, "Seleccione una guardia para intercambiar.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int id = Integer.parseInt(tablaModel.getValueAt(fila, 0).toString());
				logica.Guardia g = buscarGuardiaPorId(id);
				if (g != null) {
					// Mostrar el panel de intercambioPersona embebido en Inicio
					if (ventanaInicio != null) {
						boolean oscuro = ventanaInicio.modoOscuro;
						Color fondo = oscuro ? ventanaInicio.darkBg : ventanaInicio.amarillo;
						Color texto = oscuro ? ventanaInicio.darkFg : ventanaInicio.negro;
						Color boton = oscuro ? new Color(60, 63, 80) : ventanaInicio.negro;
						Color amarilloSec = ventanaInicio.amarillo;

						IntercambioPersona intercambio = new IntercambioPersona(g);
						intercambio.aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
						JPanel panelIntercambio = intercambio.getPanelPrincipal();
						// Usa mostrarPanelCentral para que la pila funcione
						ventanaInicio.mostrarPanelCentral(panelIntercambio);
					} else {
						// Fallback: mostrar el panel en una ventana flotante solo si no hay referencia
						// a Inicio
						JFrame frame = new JFrame("Intercambio de Persona");
						IntercambioPersona intercambio = new IntercambioPersona(g);
						// Por defecto modo claro
						intercambio.aplicarModoOscuro(false, new Color(255, 215, 0), Color.BLACK, Color.BLACK,
								new Color(255, 215, 0));
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						frame.setContentPane(intercambio.getPanelPrincipal());
						frame.pack();
						frame.setLocationRelativeTo(null);
						frame.setVisible(true);
					}
				}
			}
		});

		// Acción editar guardia
		btnEditarGuardia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablaGuardias.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(PlanGuardias.this, "Seleccione una guardia para editar.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int id = Integer.parseInt(tablaModel.getValueAt(fila, 0).toString());
				logica.Guardia g = buscarGuardiaPorId(id);
				if (g != null) {
					// Si la ventana PlanGuardias está embebida en Inicio, usar el método de Inicio
					if (ventanaInicio != null) {
						ventanaInicio.mostrarPanelEditGuardia(g);
					} else {
						// Fallback: ventana flotante
						EditGuardia frame = new EditGuardia(g);
						frame.setVisible(true);
					}
				}
			}
		});

		// Acción actualizar
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarGuardiasEnTabla();
			}
		});

		// Acción eliminar
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablaGuardias.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(PlanGuardias.this, "Seleccione una guardia para eliminar.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int id = Integer.parseInt(tablaModel.getValueAt(fila, 0).toString());
				int confirm = JOptionPane.showConfirmDialog(
						PlanGuardias.this,
						"¿Está seguro de eliminar la guardia seleccionada?",
						"Confirmar eliminación",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					boolean eliminado = PlanificadorGuardias.getInstancia().getGuardiaFactory().eliminarGuardia(id);
					if (eliminado) {
						JOptionPane.showMessageDialog(PlanGuardias.this, "Guardia eliminada correctamente.",
								"Eliminado", JOptionPane.INFORMATION_MESSAGE);
						cargarGuardiasEnTabla();
					} else {
						JOptionPane.showMessageDialog(PlanGuardias.this, "No se pudo eliminar la guardia.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		// --- Cargar guardias existentes al abrir la ventana ---
		cargarGuardiasEnTabla();

		// Seleccionar mes y año por defecto: mes siguiente al último mes planificado
		java.time.LocalDate ultimoPlan = PlanificadorGuardias.getInstancia().getGuardiaFactory()
				.getUltimoMesPlanificado();
		if (ultimoPlan != null) {
			java.time.LocalDate siguiente = ultimoPlan.plusMonths(1);
			monthChooser.setMonth(siguiente.getMonthValue() - 1);
			yearChooser.setYear(siguiente.getYear());
		}

		// Actualizar tabla al cambiar mes o año
		monthChooser.addPropertyChangeListener("month", new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				cargarGuardiasEnTabla();
			}
		});
		yearChooser.addPropertyChangeListener("year", new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				cargarGuardiasEnTabla();
			}
		});

		btnPlanificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int mes = monthChooser.getMonth() + 1; // JMonthChooser es 0-based
				int anio = yearChooser.getYear();
				PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();

				// Validación: no permitir planificar meses anteriores al mes anterior al último
				// mes planificado
				java.time.LocalDate ultimoPlan = planificador.getGuardiaFactory().getUltimoMesPlanificado();
				if (ultimoPlan != null) {
					java.time.LocalDate mesActual = java.time.LocalDate.of(anio, mes, 1);
					java.time.LocalDate mesAnteriorUltimo = ultimoPlan.minusMonths(1);
					if (mesActual.isBefore(mesAnteriorUltimo)) {
						JOptionPane.showMessageDialog(
								PlanGuardias.this,
								"No se puede planificar un mes anterior a " + mesAnteriorUltimo.getMonth() + " "
										+ mesAnteriorUltimo.getYear() + ".",
								"Mes inválido",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				// Verificar si ya hay guardias planificadas para este mes y año
				java.util.List<logica.Guardia> guardiasExistentes = planificador.getGuardiaFactory().getGuardias();
				boolean yaPlanificado = false;
				for (int i = 0; i < guardiasExistentes.size(); i++) {
					java.time.LocalDate fecha = guardiasExistentes.get(i).getHorario().getDia();
					if (fecha.getMonthValue() == mes && fecha.getYear() == anio) {
						yaPlanificado = true;
						break;
					}
				}

				if (yaPlanificado) {
					// Si ya está planificado, intentar planificar huecos disponibles
					int nuevas = planificador.getGuardiaFactory()
							.planificarGuardiasDisponiblesMes(planificador.getFacultad(), anio, mes);
					if (nuevas > 0) {
						JOptionPane.showMessageDialog(
								PlanGuardias.this,
								"Se agregaron " + nuevas + " nuevas guardias en huecos disponibles.",
								"Guardias adicionales",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(
								PlanGuardias.this,
								"Todas las guardias para este mes ya se han planificado y no hay huecos disponibles.",
								"Información",
								JOptionPane.INFORMATION_MESSAGE);
					}
					cargarGuardiasEnTabla();
					return;
				}

				// Planificar y guardar en GuardiaFactory
				java.util.List<logica.Guardia> guardiasPlanificadas = planificador.getGuardiaFactory()
						.planificarGuardiasMes(planificador.getFacultad(), anio, mes);

				if (guardiasPlanificadas == null || guardiasPlanificadas.isEmpty()) {
					JOptionPane.showMessageDialog(
							PlanGuardias.this,
							"No hay guardias para planificar en este mes.",
							"Información",
							JOptionPane.INFORMATION_MESSAGE);
					cargarGuardiasEnTabla();
					return;
				}

				// NO agregar de nuevo las guardias a la lista global, ya lo hace
				// planificarGuardiasMes
				// Solo recargar la tabla
				cargarGuardiasEnTabla();
			}
		});
	}

	// Método para cargar guardias existentes en la tabla según mes y año
	// seleccionados
	private void cargarGuardiasEnTabla() {
		tablaModel.setRowCount(0);
		int mes = monthChooser.getMonth() + 1;
		int anio = yearChooser.getYear();
		java.util.List<logica.Guardia> guardias = PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardias();
		// Ordenar por ID ascendente (menor a mayor)
		java.util.Collections.sort(guardias, new java.util.Comparator<logica.Guardia>() {
			@Override
			public int compare(logica.Guardia g1, logica.Guardia g2) {
				return Integer.compare(g1.getId(), g2.getId());
			}
		});
		for (int i = 0; i < guardias.size(); i++) {
			logica.Guardia g = guardias.get(i);
			java.time.LocalDate fecha = g.getHorario().getDia();
			if (fecha.getMonthValue() == mes && fecha.getYear() == anio) {
				String tipoPersona = "";
				if (g.getPersona() instanceof logica.Estudiante) {
					logica.Estudiante est = (logica.Estudiante) g.getPersona();
					tipoPersona = "Estudiante " + (est.getSexo() == utiles.Sexo.MASCULINO ? "M" : "F");
				} else if (g.getPersona() instanceof logica.Trabajador) {
					logica.Trabajador trab = (logica.Trabajador) g.getPersona();
					tipoPersona = "Trabajador " + (trab.getSexo() == utiles.Sexo.MASCULINO ? "M" : "F");
				}
				tablaModel.addRow(new Object[] {
						g.getId(),
						g.getTipo(),
						g.getPersona().getNombre() + " " + g.getPersona().getApellidos(),
						tipoPersona,
						g.getHorario().getDia(),
						g.getHorario().getHoraInicio(),
						g.getHorario().getHoraFin()
				});
			}
		}
		// Permitir ordenar la tabla por columnas
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tablaModel);
		sorter.setComparator(0, new java.util.Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Integer i1 = Integer.valueOf(o1.toString());
				Integer i2 = Integer.valueOf(o2.toString());
				return i1.compareTo(i2);
			}
		});
		tablaGuardias.setRowSorter(sorter);
	}

	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
		if (tablaGuardias != null) {
			tablaGuardias.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			tablaGuardias.setForeground(oscuro ? Color.WHITE : texto);
		}
		if (scroll != null) {
			scroll.getViewport().setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			scroll.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
		}
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
		} else if (comp instanceof JComboBox) {
			comp.setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
			comp.setForeground(oscuro ? Color.WHITE : texto);
		} else if (comp instanceof JButton) {
			comp.setBackground(boton);
			comp.setForeground(amarilloSec);
		} else if (comp instanceof JTable) {
			comp.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			((JTable) comp).setForeground(oscuro ? Color.WHITE : texto);
		}
	}

	// Método para buscar guardia por ID
	private logica.Guardia buscarGuardiaPorId(int id) {
		java.util.List<logica.Guardia> guardias = PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardias();
		for (logica.Guardia g : guardias) {
			if (g.getId() == id)
				return g;
		}
		return null;
	}

	// Método para integración con Inicio
	public JPanel getPanelPrincipal() {
		return contentPane;
	}
}
