package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font; // Importar Font
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel; // Importar JLabel
import javax.swing.SwingConstants;
import java.awt.Color; // Importar SwingConstants

public class AcercaDe extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AcercaDe frame = new AcercaDe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AcercaDe() {
		setTitle("Acerca de - Sistema de Guardias"); // Título de la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Importante: solo cierra esta ventana
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null); // Centrar la ventana en la pantalla

		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15)); // Aumentar el padding
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Añadir contenido a la ventana AcercaDe
		JLabel lblInfo = new JLabel("<html><div style='text-align: center;'><h2>Sistema de Gestión de Guardias</h2><p>Desarrolladores: Diego Canales Calvo  y Daniela Rguez Molina\r\n</p><p>Este software ayuda a la planificación y seguimiento de guardias de estudiantes y trabajadores.</p><p>Versión: 1.0</p></div></html>");
		lblInfo.setBackground(Color.ORANGE);
		lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setVerticalAlignment(SwingConstants.CENTER);
		contentPane.add(lblInfo, BorderLayout.CENTER);
	}

}
