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

	public VerEstudiantes(PlanificadorGuardias planificadorplanificador) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/IMG_20250617_110529.jpg")));
		setTitle("Listado de Estudiantes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 550);

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
		String[] columnas = {"CI", "Nombre", "Apellidos", "Sexo", "Activo", "Grupo", "Guardias Asignadas", "Guardias Cumplidas"};
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
					JOptionPane.showMessageDialog(VerEstudiantes.this, "Seleccione un estudiante para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String ci = (String) model.getValueAt(fila, 0);
				Persona p = planificador.getFacultad().buscarPersonaPorCI(ci);
				if (p != null && p instanceof Estudiante) {
					int confirm = JOptionPane.showConfirmDialog(VerEstudiantes.this, "¿Está seguro de eliminar al estudiante?", "Confirmar", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						planificador.getFacultad().eliminarPersona(p);
						cargarEstudiantes(txtBusqueda.getText().trim().isEmpty() ? null : txtBusqueda.getText().trim().toLowerCase());
					}
				}
			}
		});

		// Acción editar
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(VerEstudiantes.this, "Seleccione un estudiante para editar.", "Error", JOptionPane.ERROR_MESSAGE);
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
	}

	private void cargarEstudiantes(String filtro) {
		model.setRowCount(0);
		List<Persona> personas = planificador.getFacultad().getPersonas();
		estudiantesFiltrados = new java.util.ArrayList<Persona>();
		for (int i = 0; i < personas.size(); i++) {
			Persona p = personas.get(i);
			if (p instanceof Estudiante) {
				Estudiante e = (Estudiante) p;
				boolean coincide = false;
				if (filtro == null) {
					coincide = true;
				} else {
					String ci = e.getCi().toLowerCase();
					String nombre = e.getNombre().toLowerCase();
					String apellidos = e.getApellidos().toLowerCase();
					String grupoStr = String.valueOf(e.getGrupo());
					if (ci.contains(filtro) || nombre.contains(filtro) || apellidos.contains(filtro) || grupoStr.contains(filtro)) {
						coincide = true;
					}
				}
				if (coincide) {
					estudiantesFiltrados.add(e);
					model.addRow(new Object[] {
						e.getCi(),
						e.getNombre(),
						e.getApellidos(),
						e.getSexo(),
						e.getActivo() ? "Sí" : "No",
						e.getGrupo(),
						e.getGuardiasAsignadas(),
						e.getGuardiasCumplidas()
					});
				}
			}
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
