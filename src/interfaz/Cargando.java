package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import java.awt.Font;

public class Cargando extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JProgressBar progressBar;
	private Timer timer;
	private int progreso = 0;

	public Cargando() {
		setTitle("Cargando...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 150);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(30, 144, 255)); // Azul
		progressBar.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(progressBar, BorderLayout.CENTER);

		// Timer para simular la carga (2 segundos en total)
		timer = new Timer(10, new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				progreso++;
				progressBar.setValue(progreso);
				if (progreso >= 100) {
					timer.stop();
					// Aquí puedes cerrar la ventana o lanzar la siguiente pantalla
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Login loginFrame = new Login(null);
								loginFrame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					dispose();
					// Mostrar la ventana de login
				}
			}
		});
		timer.start();


	}   
}
