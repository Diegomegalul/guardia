// :)
package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.Persona;
import logica.PlanificadorGuardias;
import logica.Trabajador;

public class VerTrabajadores extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JTextField txtBusqueda;
	private PlanificadorGuardias planificador;
	private List<Persona> trabajadoresFiltrados;
	private static VerTrabajadores instancia = null;

	// Nuevo constructor con modo oscuro
	public VerTrabajadores(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setTitle("Listado de Trabajadores");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 550);
		setLocationRelativeTo(null);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Trabajadores de la Facultad");
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

		// Tabla de trabajadores
		String[] columnas = { "CI", "Nombre", "Apellidos", "Sexo", "Activo", "Fecha Incorp.", "G.Planificadas",
				"G.Festivo", "Voluntario" };
		model = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
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

		// Doble clic para abrir VerGuardiasPersona
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int fila = table.getSelectedRow();
					Trabajador trabajador = (Trabajador) trabajadoresFiltrados.get(table.convertRowIndexToModel(fila));
					VerGuardiasPersona frame = new VerGuardiasPersona(trabajador, null);
					frame.setVisible(true);
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

		// Cargar todos los trabajadores inicialmente
		cargarTrabajadores(null);

		// Acción buscar
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String texto = txtBusqueda.getText().trim().toLowerCase();
				cargarTrabajadores(texto.isEmpty() ? null : texto);
			}
		});
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtBusqueda.setText("");
				cargarTrabajadores(null);
			}
		});

		// Acción eliminar
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(VerTrabajadores.this, "Seleccione un trabajador para eliminar.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Trabajador trabajador = (Trabajador) trabajadoresFiltrados.get(table.convertRowIndexToModel(fila));
				int confirm = JOptionPane.showConfirmDialog(VerTrabajadores.this,
						"¿Está seguro de eliminar al trabajador?", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					planificador.getFacultad().eliminarPersona(trabajador);
					cargarTrabajadores(
							txtBusqueda.getText().trim().isEmpty() ? null : txtBusqueda.getText().trim().toLowerCase());
				}
			}
		});

		// Acción editar
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					Trabajador trabajador = (Trabajador) trabajadoresFiltrados
							.get(table.convertRowIndexToModel(selectedRow));
					// Buscar el contenedor padre del panel principal
					Container parent = getPanelPrincipal().getParent();
					while (parent != null && !(parent instanceof Inicio)) {
						parent = parent.getParent();
					}
					if (parent instanceof Inicio) {
						Inicio inicio = (Inicio) parent;
						JPanel panelEditTrabajador = new AddTrabajadores(planificador, trabajador, VerTrabajadores.this)
								.getPanelPrincipal();
						inicio.mostrarPanelCentral(panelEditTrabajador);
					} else {
						// Fallback: ventana flotante si no está embebido en Inicio
						AddTrabajadores frame = new AddTrabajadores(planificador, trabajador);
						frame.setVisible(true);
					}
				} else {
					JOptionPane.showMessageDialog(VerTrabajadores.this, "Seleccione un trabajador para editar.",
							"Aviso", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// Aplicar modo oscuro según parámetros
		aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
	}

	// Constructor por defecto para compatibilidad (abre en modo claro)
	public VerTrabajadores() {
		this(false, new Color(255, 215, 0), Color.BLACK, Color.BLACK, new Color(255, 215, 0));
	}

	// --- MODO OSCURO ---
	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
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

	private void cargarTrabajadores(String filtro) {
		model.setRowCount(0);
		trabajadoresFiltrados = new java.util.ArrayList<Persona>();
		List<Trabajador> trabajadores = planificador.buscarTrabajadores(filtro);
		for (Trabajador t : trabajadores) {
			trabajadoresFiltrados.add(t);
			model.addRow(new Object[] {
					t.getCi(),
					t.getNombre(),
					t.getApellidos(),
					t.getSexo(),
					t.getActivo() ? "Sí" : "No",
					t.getFechaDeIncorporacion() != null ? t.getFechaDeIncorporacion().toString() : "",
					t.getGuardiasPlanificadas(),
					t.getCantidadGuardiasFestivo(),
					t.getVoluntario() ? "Sí" : "No"
			});
		}
	}

	// Método para refrescar la tabla desde AddTrabajadores tras editar
	public void refrescarTabla() {
		cargarTrabajadores(txtBusqueda.getText().trim().isEmpty() ? null : txtBusqueda.getText().trim().toLowerCase());
	}

	public static void mostrarVentana() {
		if (instancia == null || !instancia.isDisplayable()) {
			instancia = new VerTrabajadores();
			instancia.setVisible(true);
		} else {
			instancia.toFront();
			instancia.setState(JFrame.NORMAL);
		}
	}

	// Método para integración con Inicio
	public JPanel getPanelPrincipal() {
		return contentPane;
	}
}
