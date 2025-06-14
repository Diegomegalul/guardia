package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Cargando extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JProgressBar progressBar;
	private Timer timer;
	private int progreso = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cargando frame = new Cargando();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Cargando() {
		// Colores principales
		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;
		Color blanco = Color.WHITE;

		setTitle("Cargando...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 180);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(10, 10));
		contentPane.setBackground(amarillo);
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Guardias");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
		lblTitulo.setHorizontalAlignment(JLabel.CENTER);
		lblTitulo.setForeground(negro);
		contentPane.add(lblTitulo, BorderLayout.NORTH);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(negro);
		progressBar.setBackground(blanco);
		progressBar.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(progressBar, BorderLayout.CENTER);

		JLabel lblCargando = new JLabel("Cargando, por favor espere...");
		lblCargando.setFont(new Font("Arial", Font.PLAIN, 14));
		lblCargando.setHorizontalAlignment(JLabel.CENTER);
		lblCargando.setForeground(negro);
		contentPane.add(lblCargando, BorderLayout.SOUTH);

		timer = new Timer(15, new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				progreso++;
				progressBar.setValue(progreso);
				if (progreso >= 100) {
					timer.stop();
					// Aquí puedes abrir la ventana de Login o la siguiente pantalla
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Login loginFrame = new Login(Cargando.this);
								loginFrame.setVisible(true);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					});
					setVisible(false);
					dispose();
				}
			}
		});
		timer.start();
	}
}
