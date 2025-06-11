package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import logica.PlanificadorGuardias;
import logica.DiaFestivo;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class EditCalendario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PlanificadorGuardias planificador;
	private JDateChooser dateChooser;
	private JTextField txtDescripcion;
	private DefaultListModel<String> modeloLista;
	private JList<String> listaFestivos;

	public EditCalendario(final PlanificadorGuardias planificador) {
		this.planificador = planificador;
		setMinimumSize(new Dimension(600, 500));
		setTitle("Editar Calendario - Días Festivos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		setLocationRelativeTo(null);

		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		final Color blanco = Color.WHITE;

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Gestión de Días Festivos");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelForm = new JPanel();
		panelForm.setBackground(amarillo);
		panelForm.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:200px:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:220px:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),}));

		// Fecha
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setFont(new Font("Arial", Font.PLAIN, 15));
		panelForm.add(new JLabel("Fecha:"), "2, 2, center, center");
		panelForm.add(dateChooser, "4, 2, center, center");

		// Descripción
		txtDescripcion = new JTextField(20);
		txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 15));
		panelForm.add(new JLabel("Descripción:"), "2, 4, center, center");
		panelForm.add(txtDescripcion, "4, 4, center, center");

		// Botones CRUD
		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(amarillo);
		panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.setBackground(negro);
		btnAgregar.setForeground(amarillo);
		btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
		panelBotones.add(btnAgregar);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.setBackground(negro);
		btnActualizar.setForeground(amarillo);
		btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
		panelBotones.add(btnActualizar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBackground(negro);
		btnEliminar.setForeground(amarillo);
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));
		panelBotones.add(btnEliminar);

		panelForm.add(panelBotones, "2, 6, 3, 1, center, center");

		contentPane.add(panelForm, BorderLayout.CENTER);

		// Lista de días festivos (debajo del formulario, no al lado)
		modeloLista = new DefaultListModel<>();
		listaFestivos = new JList<>(modeloLista);
		listaFestivos.setFont(new Font("Arial", Font.PLAIN, 15));
		listaFestivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(listaFestivos);
		scroll.setPreferredSize(new Dimension(0, 120)); // ancho flexible, altura fija

		// Panel para ubicar la lista debajo del formulario
		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBackground(amarillo);
		panelInferior.add(scroll, BorderLayout.CENTER);

		contentPane.add(panelInferior, BorderLayout.SOUTH);

		// Acciones CRUD
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.util.Date utilDate = dateChooser.getDate();
				String descripcion = txtDescripcion.getText().trim();
				if (utilDate == null || descripcion.isEmpty()) {
					JOptionPane.showMessageDialog(EditCalendario.this, "Ingrese fecha y descripción.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				LocalDate fecha = new java.sql.Date(utilDate.getTime()).toLocalDate();
				DiaFestivo dia = new DiaFestivo(fecha, descripcion);
				planificador.getCalendario().agregarDiaFestivo(dia);
				actualizarLista();
				txtDescripcion.setText("");
				dateChooser.setDate(null);
			}
		});

		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = listaFestivos.getSelectedIndex();
				java.util.Date utilDate = dateChooser.getDate();
				String descripcion = txtDescripcion.getText().trim();
				if (idx == -1 || utilDate == null || descripcion.isEmpty()) {
					JOptionPane.showMessageDialog(EditCalendario.this, "Seleccione un día y complete los campos.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				LocalDate fecha = new java.sql.Date(utilDate.getTime()).toLocalDate();
				boolean actualizado = planificador.getCalendario().actualizarDiaFestivo(fecha, descripcion);
				if (actualizado) {
					actualizarLista();
					JOptionPane.showMessageDialog(EditCalendario.this, "Día festivo actualizado.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(EditCalendario.this, "No se pudo actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = listaFestivos.getSelectedIndex();
				if (idx == -1) {
					JOptionPane.showMessageDialog(EditCalendario.this, "Seleccione un día para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String selected = modeloLista.getElementAt(idx);
				String fechaStr = selected.split(" - ")[0];
				LocalDate fecha = LocalDate.parse(fechaStr);
				boolean eliminado = planificador.getCalendario().eliminarDiaFestivo(fecha);
				if (eliminado) {
					actualizarLista();
					JOptionPane.showMessageDialog(EditCalendario.this, "Día festivo eliminado.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(EditCalendario.this, "No se pudo eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Al seleccionar un elemento, mostrar en los campos (sin lambda)
		listaFestivos.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int idx = listaFestivos.getSelectedIndex();
				if (idx != -1) {
					String selected = listaFestivos.getModel().getElementAt(idx);
					String[] partes = selected.split(" - ", 2);
					if (partes.length == 2) {
						LocalDate fecha = LocalDate.parse(partes[0]);
						dateChooser.setDate(java.sql.Date.valueOf(fecha));
						txtDescripcion.setText(partes[1]);
					}
				}
			}
		});

		actualizarLista();
	}

	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		for (Component c : contentPane.getComponents()) {
			if (c instanceof JLabel) ((JLabel)c).setForeground(oscuro ? Color.WHITE : texto);
			if (c instanceof JPanel) ((JPanel)c).setBackground(fondo);
		}
		JPanel panelForm = null;
		for (Component c : contentPane.getComponents()) {
			if (c instanceof JPanel && c != contentPane) panelForm = (JPanel)c;
		}
		if (panelForm != null) {
			panelForm.setBackground(fondo);
			for (Component c : panelForm.getComponents()) {
				if (c instanceof JLabel) ((JLabel)c).setForeground(oscuro ? Color.WHITE : texto);
				if (c instanceof JTextField) {
					((JTextField)c).setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
					((JTextField)c).setForeground(oscuro ? Color.WHITE : texto);
				}
				if (c instanceof com.toedter.calendar.JDateChooser) {
					((com.toedter.calendar.JDateChooser)c).getComponent(1).setBackground(oscuro ? new Color(50, 50, 60) : Color.WHITE);
					((com.toedter.calendar.JDateChooser)c).getComponent(1).setForeground(oscuro ? Color.WHITE : texto);
				}
			}
		}
		for (Component c : contentPane.getComponents()) {
			if (c instanceof JPanel) {
				for (Component b : ((JPanel)c).getComponents()) {
					if (b instanceof JButton) {
						b.setBackground(boton);
						b.setForeground(amarilloSec);
					}
				}
			}
		}
		listaFestivos.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
		listaFestivos.setForeground(oscuro ? Color.WHITE : texto);
	}

	private void actualizarLista() {
		modeloLista.clear();
		for (DiaFestivo d : planificador.getCalendario().getDiasFestivos()) {
			modeloLista.addElement(d.getFechaString() + " - " + d.getDescripcion());
		}
	}
}
