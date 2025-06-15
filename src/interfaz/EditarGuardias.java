package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.Guardia;
import logica.PlanificadorGuardias;

public class EditarGuardias extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JTextField txtBusqueda;
	@SuppressWarnings("unused")
	private PlanificadorGuardias planificador;
	private List<Guardia> guardiasFiltradas;

	public EditarGuardias() {
		setTitle("Editar Guardias");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 550);

		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Lista de Guardias");
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

		// Tabla de guardias
		String[] columnas = {"ID", "Tipo", "Persona", "CI", "Fecha", "Hora Inicio", "Hora Fin"};
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
		panelBoton.add(btnCerrar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);

		this.planificador = PlanificadorGuardias.getInstancia();

		// Cargar todas las guardias inicialmente
		cargarGuardias(null);

		// Acción buscar
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String texto = txtBusqueda.getText().trim().toLowerCase();
				cargarGuardias(texto.isEmpty() ? null : texto);
			}
		});
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtBusqueda.setText("");
				cargarGuardias(null);
			}
		});

		// Acción editar
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(EditarGuardias.this, "Seleccione una guardia para editar.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int id = Integer.parseInt(model.getValueAt(fila, 0).toString());
				Guardia g = buscarGuardiaPorId(id);
				if (g != null) {
					FormularioGuardia frame = new FormularioGuardia();
					frame.setVisible(true);
					// Aquí puedes pasar la guardia seleccionada al formulario si lo deseas
				}
			}
		});

		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void cargarGuardias(String filtro) {
		model.setRowCount(0);
		List<Guardia> guardias = PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardias();
		guardiasFiltradas = new java.util.ArrayList<Guardia>();
		for (int i = 0; i < guardias.size(); i++) {
			Guardia g = guardias.get(i);
			boolean coincide = false;
			if (filtro == null) {
				coincide = true;
			} else {
				String idStr = String.valueOf(g.getId());
				String persona = g.getPersona().getNombre().toLowerCase() + " " + g.getPersona().getApellidos().toLowerCase();
				String nombre = g.getPersona().getNombre().toLowerCase();
				String apellidos = g.getPersona().getApellidos().toLowerCase();
				String ci = g.getPersona().getCi().toLowerCase();
				String tipo = g.getTipo().toString().toLowerCase();
				String fecha = g.getHorario().getDia().toString();
				String grupo = "";
				if (g.getPersona() instanceof logica.Estudiante) {
					grupo = String.valueOf(((logica.Estudiante)g.getPersona()).getGrupo());
				}
				// Buscar por id, nombre, apellido, nombre completo, grupo, ci, tipo, fecha
				if (
					idStr.contains(filtro) ||
					persona.contains(filtro) ||
					nombre.contains(filtro) ||
					apellidos.contains(filtro) ||
					ci.contains(filtro) ||
					(!grupo.isEmpty() && grupo.contains(filtro)) ||
					tipo.contains(filtro) ||
					fecha.contains(filtro)
				) {
					coincide = true;
				}
			}
			if (coincide) {
				guardiasFiltradas.add(g);
				model.addRow(new Object[] {
					g.getId(),
					g.getTipo(),
					g.getPersona().getNombre() + " " + g.getPersona().getApellidos(),
					g.getPersona().getCi(),
					g.getHorario().getDia(),
					g.getHorario().getHoraInicio(),
					g.getHorario().getHoraFin()
				});
			}
		}
	}

	private Guardia buscarGuardiaPorId(int id) {
		for (Guardia g : PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardias()) {
			if (g.getId() == id) return g;
		}
		return null;
	}
}
