package interfaz;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
		
		// Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");
        JMenuItem menuAddPersonas = new JMenuItem("A\u00F1adir personas");
        JMenuItem menuOrganizarGuardias = new JMenuItem("Organizar guardias");
        menu.add(menuAddPersonas);
        menu.add(menuOrganizarGuardias);
        menuBar.add(menu);
        setJMenuBar(menuBar);

		JLabel lblNewLabel = new JLabel("Bienvenido al planificador de guardias");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);

		// Panel central vacío para mantener el diseño responsivo
		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new GridBagLayout());
		getContentPane().add(panelCentral, BorderLayout.CENTER);

		// Acción para "Añadir personas"
		menuAddPersonas.addActionListener(new ActionListener() {
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

		// Acción para "Organizar guardias"
		menuOrganizarGuardias.addActionListener(new ActionListener() {
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
