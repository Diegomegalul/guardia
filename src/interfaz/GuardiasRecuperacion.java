// :)
package interfaz;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logica.PlanificadorGuardias;
import logica.PlanificadorGuardias.GrupoRecuperacionOrdenado;
import logica.Estudiante;
import logica.Guardia;
import utiles.TipoGuardia;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GuardiasRecuperacion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public GuardiasRecuperacion(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setTitle("Guardias de Recuperación por Grupo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null); // Centrar en pantalla

		contentPane = new JPanel();
		contentPane.setBackground(fondo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Guardias de Recuperación por Grupo");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setForeground(texto);
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitulo.setBorder(new EmptyBorder(10, 0, 20, 0));
		contentPane.add(lblTitulo);

		PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();
		List<GrupoRecuperacionOrdenado> grupos = planificador.estudiantesPorGrupoRecuperacionDesc();

		// Obtener todas las guardias de recuperación del sistema
		List<Guardia> guardiasRecuperacion = planificador.getGuardiaFactory().getGuardias();
		final java.util.List<Guardia> guardiasRecuperacionAsignadas = new java.util.ArrayList<>();
		final java.util.List<Guardia> guardiasRecuperacionCumplidas = planificador.getGuardiaFactory()
				.getGuardiasCumplidas();

		for (Guardia g : guardiasRecuperacion) {
			if (g.getTipo() == TipoGuardia.RECUPERACION) {
				guardiasRecuperacionAsignadas.add(g);
			}
		}

		boolean hayGrupos = false;
		for (int i = 0; i < grupos.size(); i++) {
			GrupoRecuperacionOrdenado grupo = grupos.get(i);

			// Ordenar estudiantes por la suma de guardias de recuperación asignadas y cumplidas (desc)
			List<Estudiante> estudiantesOrdenados = new java.util.ArrayList<Estudiante>(grupo.estudiantes);
			java.util.Collections.sort(estudiantesOrdenados, new java.util.Comparator<Estudiante>() {
				public int compare(Estudiante a, Estudiante b) {
					int aAsignadas = contarGuardiasRecuperacionAsignadas(guardiasRecuperacionAsignadas, a);
					int aCumplidas = contarGuardiasRecuperacionCumplidas(guardiasRecuperacionCumplidas, a);
					int bAsignadas = contarGuardiasRecuperacionAsignadas(guardiasRecuperacionAsignadas, b);
					int bCumplidas = contarGuardiasRecuperacionCumplidas(guardiasRecuperacionCumplidas, b);
					int sumaA = aAsignadas + aCumplidas;
					int sumaB = bAsignadas + bCumplidas;
					return Integer.compare(sumaB, sumaA);
				}
			});

			// Mostrar solo si hay al menos un estudiante con alguna guardia de recuperación
			boolean grupoTieneRecuperacion = false;
			for (Estudiante e : estudiantesOrdenados) {
				int asignadas = contarGuardiasRecuperacionAsignadas(guardiasRecuperacionAsignadas, e);
				int cumplidas = contarGuardiasRecuperacionCumplidas(guardiasRecuperacionCumplidas, e);
				if (asignadas > 0 || cumplidas > 0) {
					grupoTieneRecuperacion = true;
					break;
				}
			}
			if (!grupoTieneRecuperacion)
				continue;
			hayGrupos = true;

			JLabel lblGrupo = new JLabel("Grupo " + grupo.grupo + " (Total: " + grupo.totalRecuperacion + ")");
			lblGrupo.setFont(new Font("Arial", Font.BOLD, 18));
			lblGrupo.setForeground(texto);
			lblGrupo.setAlignmentX(Component.LEFT_ALIGNMENT);
			lblGrupo.setBorder(new EmptyBorder(10, 0, 5, 0));
			contentPane.add(lblGrupo);

			String[] columnas = { "CI", "Nombre", "Apellidos", "Recuperación Asignadas", "Recuperación Cumplidas" };
			DefaultTableModel model = new DefaultTableModel(columnas, 0) {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int col) {
					return false;
				}
			};
			for (Estudiante e : estudiantesOrdenados) {
				int asignadas = contarGuardiasRecuperacionAsignadas(guardiasRecuperacionAsignadas, e);
				int cumplidas = contarGuardiasRecuperacionCumplidas(guardiasRecuperacionCumplidas, e);
				if (asignadas > 0 || cumplidas > 0) {
					model.addRow(new Object[] {
							e.getCi(),
							e.getNombre(),
							e.getApellidos(),
							asignadas,
							cumplidas
					});
				}
			}
			final JTable tabla = new JTable(model);
			tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
			tabla.getTableHeader().setForeground(texto);
			tabla.getTableHeader().setBackground(fondo);
			tabla.setFont(new Font("Arial", Font.PLAIN, 14));
			tabla.setRowHeight(24);

			// Doble click para abrir VerGuardiasPersona
			final List<Estudiante> estudiantesRef = estudiantesOrdenados;
			tabla.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
						int fila = tabla.getSelectedRow();
						Estudiante est = estudiantesRef.get(fila);
						VerGuardiasPersona frame = new VerGuardiasPersona(est, null);
						frame.setVisible(true);
					}
				}
			});

			JScrollPane scroll = new JScrollPane(tabla);
			scroll.setBorder(BorderFactory.createLineBorder(amarilloSec, 2));
			scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
			scroll.getViewport().setBackground(fondo);
			contentPane.add(scroll);
		}

		if (!hayGrupos) {
			JLabel lblSinDatos = new JLabel("No hay grupos con guardias de recuperación.");
			lblSinDatos.setFont(new Font("Arial", Font.ITALIC, 16));
			lblSinDatos.setForeground(texto);
			lblSinDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPane.add(lblSinDatos);
		}

		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(fondo);
		contentPane.add(Box.createVerticalStrut(10));
		contentPane.add(panelBoton);

		// Aplicar modo oscuro al final del constructor
		aplicarModoOscuro(oscuro, fondo, texto, boton, amarilloSec);
	}

	private int contarGuardiasRecuperacionAsignadas(List<Guardia> guardias, Estudiante estudiante) {
		int count = 0;
		for (Guardia g : guardias) {
			if (g.getPersona() instanceof Estudiante &&
					g.getPersona().equals(estudiante) &&
					g.getTipo() == TipoGuardia.RECUPERACION) {
				count++;
			}
		}
		return count;
	}

	private int contarGuardiasRecuperacionCumplidas(List<Guardia> guardiasCumplidas, Estudiante estudiante) {
		int count = 0;
		for (Guardia g : guardiasCumplidas) {
			if (g.getPersona() instanceof Estudiante &&
					g.getPersona().equals(estudiante) &&
					g.getTipo() == TipoGuardia.RECUPERACION) {
				count++;
			}
		}
		return count;
	}

	public JPanel getPanelPrincipal() {
		return contentPane;
	}

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
}
