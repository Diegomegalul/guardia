package interfaz;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; 
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import logica.PlanificadorGuardias;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PlanificadorGuardias planificador = new PlanificadorGuardias();

	public Inicio() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		JLabel lblNewLabel = new JLabel("Bienvenido al planificador de guardias");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new GridBagLayout());
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.insets = new java.awt.Insets(20, 20, 20, 20);
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		gbc1.weightx = 1.0;

		JButton btnAddPersonas = new JButton("A\u00F1adir personas");
		btnAddPersonas.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
		panelCentral.add(btnAddPersonas, gbc1);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new java.awt.Insets(20, 20, 20, 20);
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.weightx = 1.0;

		JButton btnOrganizarGuardias = new JButton("Organizar guardias");
		btnOrganizarGuardias.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
		panelCentral.add(btnOrganizarGuardias, gbc2);

		getContentPane().add(panelCentral, BorderLayout.CENTER);

		btnAddPersonas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							AddPersonas frame = new AddPersonas(planificador);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});		
			}
		});
		btnOrganizarGuardias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							OrganizarGuardias frame = new OrganizarGuardias();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
}
