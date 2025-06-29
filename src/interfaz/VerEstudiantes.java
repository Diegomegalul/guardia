package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.Estudiante;
import logica.Persona;
import logica.PlanificadorGuardias;

public class VerEstudiantes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JTextField txtBusqueda;
	private PlanificadorGuardias planificador;
	private List<Persona> estudiantesFiltrados;
	private static VerEstudiantes instancia = null;

	public static void mostrarVentana(PlanificadorGuardias planificador) {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new VerEstudiantes(planificador);
			instancia.setVisible(true);
		} else {
			instancia.toFront();
			instancia.setState(JFrame.NORMAL);
		}
	}

	public VerEstudiantes(PlanificadorGuardias planificadorplanificador) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setTitle("Listado de Estudiantes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 550);
		setLocationRelativeTo(null);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;
		@SuppressWarnings("unused")
		Color darkBg = new Color(30, 32, 40);

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Estudiantes de la Facultad");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		// Panel superior de búsqueda
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.setBackground(amarillo);
		panelBusqueda.setLayout(new BoxLayout(panelBusqueda, BoxLayout.X_AXIS));
		JLabel lblBuscar = new JLabel("Buscar : ");
		lblBuscar.setFont(new Font("Arial", Font.PLAIN, 15));
		txtBusqueda = new JTextField();
		txtBusqueda.setFont(new Font("Arial", Font.PLAIN, 15));
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
		btnBuscar.setBackground(negro);
		btnBuscar.setForeground(amarillo);
		btnBuscar.setFocusPainted(false);
		btnBuscar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnBuscar.setContentAreaFilled(false);
		btnBuscar.setOpaque(true);
		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
		btnLimpiar.setBackground(negro);
		btnLimpiar.setForeground(amarillo);
		btnLimpiar.setFocusPainted(false);
		btnLimpiar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnLimpiar.setContentAreaFilled(false);
		btnLimpiar.setOpaque(true);

		panelBusqueda.add(lblBuscar);
		panelBusqueda.add(txtBusqueda);
		panelBusqueda.add(Box.createHorizontalStrut(10));
		panelBusqueda.add(btnBuscar);
		panelBusqueda.add(Box.createHorizontalStrut(5));
		panelBusqueda.add(btnLimpiar);
		contentPane.add(panelBusqueda, BorderLayout.BEFORE_FIRST_LINE);

		// Tabla de estudiantes
		String[] columnas = { "CI", "Nombre", "Apellidos", "Sexo", "Activo", "Grupo", "G.Planificadas", "G.Cumplidas",
				"G.Incumplidas" };
		model = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Solo las columnas numéricas deben ser Integer.class
				if (columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8) {
					return Integer.class;
				}
				// Para evitar el error de casteo, las demás columnas serán String
				return String.class;
			}
		};
		table = new JTable(model);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
		table.getTableHeader().setBackground(amarillo);
		table.getTableHeader().setForeground(negro);
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setBackground(Color.WHITE);
		table.setForeground(negro);
		table.setRowHeight(28);

		// Habilitar ordenamiento por columnas numéricas
		final javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(
				model);
		table.setRowSorter(sorter);

		// Ordenar de mayor a menor al hacer click en las columnas de guardias (sin
		// lambda)
		table.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int col = table.columnAtPoint(e.getPoint());
				String colName = table.getColumnName(col);
				if (colName.equals("G.Asignadas") || colName.equals("G.Cumplidas") || colName.equals("G.Incumplidas")) {
					java.util.List<javax.swing.RowSorter.SortKey> keys = new java.util.ArrayList<javax.swing.RowSorter.SortKey>();
					keys.add(new javax.swing.RowSorter.SortKey(col, javax.swing.SortOrder.DESCENDING));
					sorter.setSortKeys(keys);
					sorter.sort();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(BorderFactory.createLineBorder(amarillo, 2));
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// Botones de acción
		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 15));
		btnEliminar.setBackground(negro);
		btnEliminar.setForeground(amarillo);
		btnEliminar.setFocusPainted(false);
		btnEliminar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnEliminar.setContentAreaFilled(false);
		btnEliminar.setOpaque(true);

		JButton btnEditar = new JButton("Editar");
		btnEditar.setFont(new Font("Arial", Font.BOLD, 15));
		btnEditar.setBackground(negro);
		btnEditar.setForeground(amarillo);
		btnEditar.setFocusPainted(false);
		btnEditar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnEditar.setContentAreaFilled(false);
		btnEditar.setOpaque(true);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 15));
		btnCerrar.setBackground(negro);
		btnCerrar.setForeground(amarillo);
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnCerrar.setContentAreaFilled(false);
		btnCerrar.setOpaque(true);

		panelBoton.add(btnEditar);
		panelBoton.add(btnEliminar);
		panelBoton.add(btnCerrar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);

		// Instancia planificador
		this.planificador = PlanificadorGuardias.getInstancia();

		// Cargar todos los estudiantes inicialmente
		cargarEstudiantes(null);

		// Acción buscar
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String texto = txtBusqueda.getText().trim().toLowerCase();
				cargarEstudiantes(texto.isEmpty() ? null : texto);
			}
		});
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtBusqueda.setText("");
				cargarEstudiantes(null);
			}
		});

		// Acción eliminar
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(VerEstudiantes.this, "Seleccione un estudiante para eliminar.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String ci = (String) model.getValueAt(fila, 0);
				Persona p = planificador.getFacultad().buscarPersonaPorCI(ci);
				if (p != null && p instanceof Estudiante) {
					int confirm = JOptionPane.showConfirmDialog(VerEstudiantes.this,
							"¿Está seguro de eliminar al estudiante?", "Confirmar", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						planificador.getFacultad().eliminarPersona(p);
						cargarEstudiantes(txtBusqueda.getText().trim().isEmpty() ? null
								: txtBusqueda.getText().trim().toLowerCase());
					}
				}
			}
		});

		// Acción editar
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(VerEstudiantes.this, "Seleccione un estudiante para editar.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String ci = (String) model.getValueAt(fila, 0);
				Persona p = planificador.getFacultad().buscarPersonaPorCI(ci);
				if (p != null && p instanceof Estudiante) {
					abrirFormularioEdicion((Estudiante) p);
				}
			}
		});

		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// Doble clic en una fila para abrir VerGuardiasPersona (sin lambda)
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					int fila = table.rowAtPoint(e.getPoint());
					if (fila != -1) {
						String ci = (String) model.getValueAt(table.convertRowIndexToModel(fila), 0);
						Persona p = planificador.getFacultad().buscarPersonaPorCI(ci);
						if (p != null) {
							// Obtener todas las guardias de la persona
							List<logica.Guardia> guardiasPersona = new java.util.ArrayList<logica.Guardia>();
							List<logica.Guardia> todas = planificador.getGuardiaFactory().getGuardias();
							if (todas != null) {
								for (int i = 0; i < todas.size(); i++) {
									logica.Guardia g = todas.get(i);
									if (g != null && g.getPersona() != null && g.getPersona().equals(p)) {
										guardiasPersona.add(g);
									}
								}
							}
							// Evitar pasar null como lista
							VerGuardiasPersona frame = new VerGuardiasPersona(p, guardiasPersona);
							frame.setVisible(true);
						}
					}
				}
			}
		});
	}

	public JPanel getPanelPrincipal() {
		return contentPane;
	}

	private void cargarEstudiantes(String filtro) {
		model.setRowCount(0);
		estudiantesFiltrados = new java.util.ArrayList<Persona>();
		List<Estudiante> estudiantes = planificador.buscarEstudiantes(filtro);
		for (Estudiante e : estudiantes) {
			estudiantesFiltrados.add(e);
			model.addRow(new Object[] {
					e.getCi(),
					e.getNombre(),
					e.getApellidos(),
					e.getSexo().toString(),
					e.getActivo() ? "Sí" : "No",
					e.getGrupo(),
					e.getGuardiasPlanificadas() + e.getGuardiasRecuperacionAsignadas(),
					e.getGuardiasCumplidas(),
					e.getGuardiasIncumplidas()
			});
		}
	}

	private void abrirFormularioEdicion(Estudiante estudiante) {
		AddEstudiantes frame = new AddEstudiantes(planificador, estudiante, this);
		frame.setVisible(true);
	}

	// Método para refrescar la tabla desde AddEstudiantes tras editar
	public void refrescarTabla() {
		cargarEstudiantes(txtBusqueda.getText().trim().isEmpty() ? null : txtBusqueda.getText().trim().toLowerCase());
	}
}
