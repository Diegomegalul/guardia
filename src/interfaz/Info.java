package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Info extends JPanel {

	private static final long serialVersionUID = 1L;

	public Info() {
		// Colores institucionales
		Color amarillo = new Color(255, 215, 0);
		Color negro = Color.BLACK;
		setBackground(amarillo);
		setBorder(new EmptyBorder(40, 40, 40, 40));
		setLayout(new BorderLayout());

		// Título
		JLabel lblTitulo = new JLabel("Información del Proyecto");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
		lblTitulo.setForeground(negro);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBorder(new EmptyBorder(0, 0, 30, 0));
		add(lblTitulo, BorderLayout.NORTH);

		// Panel central con los datos
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(amarillo);
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

		JLabel lblDesarrolladores = new JLabel("Desarrolladores:");
		lblDesarrolladores.setFont(new Font("Arial", Font.BOLD, 22));
		lblDesarrolladores.setForeground(negro);
		lblDesarrolladores.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblDiego = new JLabel("Diego Canales Calvo");
		lblDiego.setFont(new Font("Arial", Font.PLAIN, 20));
		lblDiego.setForeground(negro);
		lblDiego.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblDaniela = new JLabel("Daniela Rodriguez Molina");
		lblDaniela.setFont(new Font("Arial", Font.PLAIN, 20));
		lblDaniela.setForeground(negro);
		lblDaniela.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblProyecto = new JLabel(
			"<html><div style='text-align: center; width: 100%;'>"
			+ "Este es el proyecto de DPOO<br>más sexy que ojos humanos hayan visto"
			+ "</div></html>", SwingConstants.CENTER);
		lblProyecto.setFont(new Font("Arial", Font.ITALIC, 22));
		lblProyecto.setForeground(new Color(60, 63, 80));
		lblProyecto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblProyecto.setBorder(new EmptyBorder(30, 0, 0, 0));

		panelCentral.add(lblDesarrolladores);
		panelCentral.add(Box.createVerticalStrut(10));
		panelCentral.add(lblDiego);
		panelCentral.add(lblDaniela);
		panelCentral.add(lblProyecto);

		add(panelCentral, BorderLayout.CENTER);
	}
}
