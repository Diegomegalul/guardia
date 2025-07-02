package interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font; // Importar Font
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel; // Importar JLabel
import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; // Importar SwingConstants

public class AcercaDe extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		JLabel lblInfo = new JLabel(
				"<html><div style='text-align: center;'><h2>Sistema de Gestión de Guardias</h2><p>Desarrolladores: Diego Canales Calvo  y Daniela Rguez Molina\r\n</p><p>Este software ayuda a la planificación y seguimiento de guardias de estudiantes y trabajadores.</p><p>Versión: 2.2</p></div></html>");
		lblInfo.setBackground(Color.ORANGE);
		lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setVerticalAlignment(SwingConstants.CENTER);
		contentPane.add(lblInfo, BorderLayout.CENTER);

		JLabel lblProyecto = new JLabel(
				"<html><div style='text-align: center; width: 100%;'>"
						+ "Este es el proyecto de DPOO<br>más sexy que ojos humanos hayan visto"
						+ "</div></html>",
				SwingConstants.CENTER);
		lblProyecto.setFont(new Font("Arial", Font.ITALIC, 22));
		lblProyecto.setForeground(new Color(60, 63, 80));
		lblProyecto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblProyecto.setBorder(new EmptyBorder(30, 0, 0, 0));
		contentPane.add(lblProyecto, BorderLayout.NORTH);

		JButton noTocar = new JButton("");
		noTocar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Cambiar el fondo del Inicio por la imagen "Chico sexy"
				for (Frame frame : JFrame.getFrames()) {
					if (frame instanceof Inicio) {
						Inicio inicio = (Inicio) frame;
						// Cambia el fondo del panel central por la imagen
						JPanel panelCentral = inicio.getContentPane() instanceof JPanel
								? (JPanel) inicio.getContentPane()
								: null;
						if (panelCentral != null) {
							JLabel lblFondo = new JLabel();
							lblFondo.setHorizontalAlignment(SwingConstants.CENTER);
							lblFondo.setVerticalAlignment(SwingConstants.CENTER);
							lblFondo.setOpaque(true);
							ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/Chico sexy.jpg"));
							lblFondo.setIcon(icon);
							lblFondo.setBackground(Color.BLACK);
							panelCentral.removeAll();
							panelCentral.setLayout(new BorderLayout());
							panelCentral.add(lblFondo, BorderLayout.CENTER);
							panelCentral.revalidate();
							panelCentral.repaint();
						}
					}
				}
			}
		});
		noTocar.setBorderPainted(false);
		noTocar.setFocusable(false);
		noTocar.setForeground(Color.ORANGE);
		noTocar.setBackground(Color.ORANGE);
		contentPane.add(noTocar, BorderLayout.SOUTH);
	}

}
