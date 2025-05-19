package interfaz;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class PlanificarGuardias extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public PlanificarGuardias() {
		setBounds(100, 100, 730, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPlanificandoGuardias = new JLabel("Planificando guardias");
		lblPlanificandoGuardias.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlanificandoGuardias.setFont(new Font("Arial", Font.PLAIN, 30));
		lblPlanificandoGuardias.setBounds(12, 13, 688, 71);
		contentPane.add(lblPlanificandoGuardias);
	}

}
