package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logica.Guardia;
import logica.PlanificadorGuardias;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class PlanGuardias extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMonthChooser monthChooser;
	private JYearChooser yearChooser;
	private JButton btnPlanificar;
	private JTextArea txtResultado;
	@SuppressWarnings("unused")
	private PlanificadorGuardias planificador;
	private JScrollPane scroll; // <-- cambia a atributo para poder acceder

	// Referencias para modo oscuro
	private Color amarillo = new Color(255, 215, 0);
	private Color negro = Color.BLACK;

	public PlanGuardias(final PlanificadorGuardias planificador) {
		this.planificador = planificador;
		setTitle("Planificar Guardias Automáticamente");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
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

		txtResultado = new JTextArea();
		txtResultado.setEditable(false);
		txtResultado.setFont(new Font("Consolas", Font.PLAIN, 14));
		txtResultado.setBackground(Color.WHITE);
		txtResultado.setForeground(negro);

		scroll = new JScrollPane(txtResultado);
		scroll.setBorder(BorderFactory.createLineBorder(amarillo, 2));
		contentPane.add(scroll, BorderLayout.CENTER);

		btnPlanificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtResultado.setText("");
				int mes = monthChooser.getMonth() + 1; // JMonthChooser es 0-based
				int anio = yearChooser.getYear();
				PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();
				java.util.List<Guardia> resultado = planificador.getGuardiaFactory().planificarGuardiasMes(planificador.getFacultad(), anio, mes);
				for (Guardia linea : resultado) {
					txtResultado.append(linea + "\n");
				}
			}
		});
	}

	public void aplicarModoOscuro(boolean oscuro, Color fondo, Color texto, Color boton, Color amarilloSec) {
		contentPane.setBackground(fondo);
		setComponentColors(contentPane, oscuro, fondo, texto, boton, amarilloSec);
		if (txtResultado != null) {
			txtResultado.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			txtResultado.setForeground(oscuro ? Color.WHITE : texto);
		}
		if (scroll != null) {
			scroll.getViewport().setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			scroll.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
		}
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
		} else if (comp instanceof JButton) {
			comp.setBackground(boton);
			comp.setForeground(amarilloSec);
		} else if (comp instanceof JTextArea) {
			comp.setBackground(oscuro ? new Color(40, 40, 50) : Color.WHITE);
			((JTextArea) comp).setForeground(oscuro ? Color.WHITE : texto);
		}
	}
}
