package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica.Estudiante;
import logica.PlanificadorGuardias;
import utiles.Sexo;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class AddEstudiantes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCi, txtNombre, txtApellidos, txtGrupo;
	private JComboBox<String> comboSexo;
	private JCheckBox chkActivo;
	private JTextField txtGuardiasAsignadas, txtGuardiasFestivo;
	@SuppressWarnings("unused")
	private PlanificadorGuardias planificador; // <- debe ser de instancia, no static

	public AddEstudiantes(final PlanificadorGuardias planificador) {
		setMinimumSize(new Dimension(500, 500));
		this.planificador = planificador;
		// Colores institucionales
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		final Color blanco = Color.WHITE;

		setTitle("Añadir Estudiante");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cierra la app
		setBounds(100, 100, 500, 500);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Nuevo Estudiante");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelForm = new JPanel();
		panelForm.setBackground(amarillo);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// Inicializar los campos antes de agregarlos al panel
		txtCi = new JTextField(15);
		txtCi.setFont(new Font("Arial", Font.PLAIN, 15));
		txtCi.setBackground(blanco);
		txtCi.setForeground(negro);

		// CI
		gbc.gridx = 0; gbc.gridy = 0;
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
				RowSpec.decode("fill:20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:20dlu:grow"),}));
		panelForm.add(new JLabel("CI:"), "2, 2, center, center");
		gbc.gridx = 1;
		panelForm.add(txtCi, "4, 2, center, center");

		// Nombre
		gbc.gridx = 0; gbc.gridy = 1;
		gbc.gridx = 1;
		JLabel label_1 = new JLabel("Nombre:");
		panelForm.add(label_1, "2, 4, center, center");

		// Apellidos
		gbc.gridx = 0; gbc.gridy = 2;

		txtNombre = new JTextField(15);
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
		txtNombre.setBackground(blanco);
		txtNombre.setForeground(negro);
		panelForm.add(txtNombre, "4, 4, center, center");
		gbc.gridx = 1;
		JLabel label_2 = new JLabel("Apellidos:");
		panelForm.add(label_2, "2, 6, center, center");

		// Sexo
		gbc.gridx = 0; gbc.gridy = 3;

		txtApellidos = new JTextField(15);
		txtApellidos.setFont(new Font("Arial", Font.PLAIN, 15));
		txtApellidos.setBackground(blanco);
		txtApellidos.setForeground(negro);
		panelForm.add(txtApellidos, "4, 6, center, center");
		gbc.gridx = 1;

		// Activo
		gbc.gridx = 0; gbc.gridy = 4;
		gbc.gridx = 1;

		// Guardias Asignadas
		gbc.gridx = 0; gbc.gridy = 5;
		gbc.gridx = 1;

		// Guardias Festivo
		gbc.gridx = 0; gbc.gridy = 6;
		gbc.gridx = 1;

		// Grupo
		gbc.gridx = 0; gbc.gridy = 7;
		gbc.gridx = 1;

		contentPane.add(panelForm, BorderLayout.CENTER);
		JLabel label_3 = new JLabel("Sexo:");
		panelForm.add(label_3, "2, 8, center, center");

		comboSexo = new JComboBox<String>();
		comboSexo.addItem("MASCULINO");
		comboSexo.addItem("FEMENINO");
		comboSexo.setFont(new Font("Arial", Font.PLAIN, 15));
		comboSexo.setBackground(blanco);
		comboSexo.setForeground(negro);
		panelForm.add(comboSexo, "4, 8, center, center");
		JLabel label_4 = new JLabel("Activo:");
		panelForm.add(label_4, "2, 10, center, center");

		chkActivo = new JCheckBox();
		chkActivo.setBackground(amarillo);
		chkActivo.setSelected(true);
		panelForm.add(chkActivo, "4, 10, center, center");
		JLabel label_5 = new JLabel("Guardias Asignadas:");
		panelForm.add(label_5, "2, 12, center, center");

		txtGuardiasAsignadas = new JTextField("0", 15);
		txtGuardiasAsignadas.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasAsignadas.setBackground(blanco);
		txtGuardiasAsignadas.setForeground(negro);
		panelForm.add(txtGuardiasAsignadas, "4, 12, center, center");
		JLabel label_6 = new JLabel("Guardias Festivo:");
		panelForm.add(label_6, "2, 14, center, center");

		txtGuardiasFestivo = new JTextField("0", 15);
		txtGuardiasFestivo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGuardiasFestivo.setBackground(blanco);
		txtGuardiasFestivo.setForeground(negro);
		panelForm.add(txtGuardiasFestivo, "4, 14, center, center");
		JLabel label_7 = new JLabel("Grupo:");
		panelForm.add(label_7, "2, 16, center, center");

		txtGrupo = new JTextField(15);
		txtGrupo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGrupo.setBackground(blanco);
		txtGrupo.setForeground(negro);
		panelForm.add(txtGrupo, "4, 16, center, center");

		// Botón guardar
		JPanel panelBoton = new JPanel();
		panelBoton.setBackground(amarillo);
		JButton btnGuardar = new JButton("Guardar") {
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
		btnGuardar.setFont(new Font("Arial", Font.BOLD, 16));
		btnGuardar.setBackground(negro);
		btnGuardar.setForeground(amarillo);
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnGuardar.setContentAreaFilled(false);
		btnGuardar.setOpaque(true);

		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Usa la variable de instancia 'planificador'
				String ci = txtCi.getText().trim();
				String nombre = txtNombre.getText().trim();
				String apellidos = txtApellidos.getText().trim();
				String sexoStr = (String) comboSexo.getSelectedItem();
				boolean activo = chkActivo.isSelected();
				int guardiasAsignadas = 0;
				int guardiasFestivo = 0;
				int grupo = 0;
				try {
					guardiasAsignadas = Integer.parseInt(txtGuardiasAsignadas.getText().trim());
					guardiasFestivo = Integer.parseInt(txtGuardiasFestivo.getText().trim());
					grupo = Integer.parseInt(txtGrupo.getText().trim());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(AddEstudiantes.this, "Verifique los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ci.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
					JOptionPane.showMessageDialog(AddEstudiantes.this, "Complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Estudiante estudiante = new Estudiante(ci, nombre, apellidos, Sexo.valueOf(sexoStr), activo, guardiasAsignadas, guardiasFestivo, grupo);
				planificador.getFacultad().agregarPersona(estudiante);
				JLabel label = new JLabel("Estudiante guardado correctamente");
				label.setFont(new Font("Arial", Font.BOLD, 16));
				label.setForeground(negro);
				JPanel panel = new JPanel();
				panel.setBackground(amarillo);
				panel.add(label);
				JOptionPane.showMessageDialog(
						AddEstudiantes.this,
						panel,
						"Guardado",
						JOptionPane.INFORMATION_MESSAGE
						);
				dispose();
			}
		});
		panelBoton.add(btnGuardar);
		contentPane.add(panelBoton, BorderLayout.SOUTH);
	}

	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
	}

	private void setComponentColors(Component comp, boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
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
		} else if (comp instanceof JCheckBox) {
			comp.setBackground(fondo);
			((JCheckBox) comp).setForeground(oscuro ? Color.WHITE : texto);
		} else if (comp instanceof JButton) {
			comp.setBackground(boton);
			comp.setForeground(amarilloSec);
		}
	}
}
