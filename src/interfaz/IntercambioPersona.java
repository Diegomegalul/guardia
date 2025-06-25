package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import logica.Guardia;
import logica.Persona;
import logica.Estudiante;
import logica.Trabajador;
import utiles.TipoGuardia;

public class IntercambioPersona extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<Persona> comboPersonas;
	private JLabel lblActual;
	private Guardia guardia;
	private List<Persona> personasIntercambio;

	public IntercambioPersona(Guardia g) {
		this.guardia = g;
		setTitle("Intercambio de Guardia");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagenes/logo.jpg")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 250);
		final Color amarillo = new Color(255, 215, 0);
		final Color negro = Color.BLACK;
		contentPane = new JPanel();
		contentPane.setBackground(amarillo);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		Persona personaActual = g.getPersona();
		final TipoGuardia tipoGuardia = g.getTipo();

		lblActual = new JLabel("Guardia asignada a: " + personaActual.getNombre() + " " + personaActual.getApellidos());
		lblActual.setFont(new Font("Arial", Font.BOLD, 18));
		lblActual.setForeground(negro);
		lblActual.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblActual, BorderLayout.NORTH);

		// Panel central para el combo
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(amarillo);
		panelCentral.setLayout(new BorderLayout(10, 10));
		JLabel lblSeleccion = new JLabel("Selecciona la persona para intercambiar:");
		lblSeleccion.setFont(new Font("Arial", Font.PLAIN, 15));
		lblSeleccion.setForeground(negro);
		lblSeleccion.setHorizontalAlignment(SwingConstants.CENTER);
		panelCentral.add(lblSeleccion, BorderLayout.NORTH);

		personasIntercambio = obtenerPersonasMismoTipoYGuardia(personaActual, tipoGuardia);
		comboPersonas = new JComboBox<>();
		for (Persona p : personasIntercambio) {
			if (!p.getCi().equals(personaActual.getCi())) {
				comboPersonas.addItem(p);
			}
		}
		comboPersonas.setFont(new Font("Arial", Font.PLAIN, 15));
		comboPersonas.setBackground(Color.WHITE);
		comboPersonas.setForeground(negro);
		comboPersonas.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Persona) {
					Persona p = (Persona) value;
					setText(p.getNombre() + " " + p.getApellidos() + " (" + p.getCi() + ")");
				}
				return c;
			}
		});
		panelCentral.add(comboPersonas, BorderLayout.CENTER);
		contentPane.add(panelCentral, BorderLayout.CENTER);

		JButton btnIntercambiar = new JButton("Intercambiar guardias");
		btnIntercambiar.setFont(new Font("Arial", Font.BOLD, 15));
		btnIntercambiar.setBackground(negro);
		btnIntercambiar.setForeground(amarillo);
		btnIntercambiar.setFocusPainted(false);
		btnIntercambiar.setBorder(BorderFactory.createLineBorder(negro, 2, true));
		btnIntercambiar.setContentAreaFilled(false);
		btnIntercambiar.setOpaque(true);
		btnIntercambiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Persona seleccionada = (Persona) comboPersonas.getSelectedItem();
				if (seleccionada == null) {
					JOptionPane.showMessageDialog(IntercambioPersona.this, "Seleccione una persona para intercambiar.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Guardia guardiaSeleccionada = buscarGuardiaDePersona(seleccionada, tipoGuardia);
				if (guardiaSeleccionada == null) {
					JOptionPane.showMessageDialog(IntercambioPersona.this,
							"No se encontró guardia para la persona seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Persona temp = guardia.getPersona();
				guardia.setPersona(seleccionada);
				guardiaSeleccionada.setPersona(temp);
				JLabel label = new JLabel("Guardias intercambiadas correctamente.");
				label.setFont(new Font("Arial", Font.BOLD, 16));
				label.setForeground(negro);
				JPanel panel = new JPanel();
				panel.setBackground(amarillo);
				panel.add(label);
				JOptionPane.showMessageDialog(
						IntercambioPersona.this,
						panel,
						"Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		contentPane.add(btnIntercambiar, BorderLayout.SOUTH);
	}

	private List<Persona> obtenerPersonasMismoTipoYGuardia(Persona persona, TipoGuardia tipo) {
		List<Persona> lista = new ArrayList<>();
		List<Guardia> guardias = logica.PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardias();
		for (Guardia g : guardias) {
			if (g.getTipo() == tipo && g.getPersona() != null) {
				if (persona instanceof Estudiante && g.getPersona() instanceof Estudiante) {
					lista.add(g.getPersona());
				} else if (persona instanceof Trabajador && g.getPersona() instanceof Trabajador) {
					lista.add(g.getPersona());
				}
			}
		}
		return lista;
	}

	private Guardia buscarGuardiaDePersona(Persona persona, TipoGuardia tipo) {
		List<Guardia> guardias = logica.PlanificadorGuardias.getInstancia().getGuardiaFactory().getGuardias();
		for (Guardia g : guardias) {
			if (g.getPersona() != null && g.getPersona().getCi().equals(persona.getCi()) && g.getTipo() == tipo) {
				return g;
			}
		}
		return null;
	}
}
