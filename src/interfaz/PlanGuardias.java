package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

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

	public PlanGuardias() {
		setTitle("Planificar Guardias Automáticamente");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);
		setLocationRelativeTo(null);

		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
		panelSuperior.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel lblMes = new JLabel("Mes:");
		lblMes.setFont(new Font("Arial", Font.BOLD, 16));
		panelSuperior.add(lblMes);

		monthChooser = new JMonthChooser();
		panelSuperior.add(monthChooser);

		JLabel lblAnio = new JLabel("   Año:");
		lblAnio.setFont(new Font("Arial", Font.BOLD, 16));
		panelSuperior.add(lblAnio);

		yearChooser = new JYearChooser();
		panelSuperior.add(yearChooser);

		btnPlanificar = new JButton("Planificar");
		btnPlanificar.setFont(new Font("Arial", Font.BOLD, 16));
		btnPlanificar.setBackground(new Color(60, 63, 80));
		btnPlanificar.setForeground(new Color(255, 215, 0));
		btnPlanificar.setFocusPainted(false);
		btnPlanificar.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 80), 2, true));
		panelSuperior.add(Box.createHorizontalStrut(20));
		panelSuperior.add(btnPlanificar);

		contentPane.add(panelSuperior, BorderLayout.NORTH);

		txtResultado = new JTextArea();
		txtResultado.setEditable(false);
		txtResultado.setFont(new Font("Consolas", Font.PLAIN, 14));
		JScrollPane scroll = new JScrollPane(txtResultado);
		contentPane.add(scroll, BorderLayout.CENTER);

		btnPlanificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtResultado.setText("");
				int mes = monthChooser.getMonth() + 1; // JMonthChooser es 0-based
				int anio = yearChooser.getYear();
				PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();
				List<String> resultado = planificador.getGuardiaFactory()
					.planificarGuardiasMes(planificador.getFacultad(), anio, mes);
				for (String linea : resultado) {
					txtResultado.append(linea + "\n");
				}
			}
		});
	}

}
