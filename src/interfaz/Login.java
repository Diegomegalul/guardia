package interfaz;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import basura.PlanificadorGuardias;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private PlanificadorGuardias planificador;

    public static void main(String[] args) {
		
	}

    public Login(java.awt.Window parent) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(parent);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panelCentral = new JPanel();
        contentPane.add(panelCentral, BorderLayout.CENTER);
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        panelCentral.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentral.add(txtUsuario, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelCentral.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentral.add(txtContrasena, gbc);

        JButton btnIngresar = new JButton("Ingresar");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentral.add(btnIngresar, gbc);

        // Instanciar planificador si es null
        planificador = new PlanificadorGuardias();

        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String contrasena = new String(txtContrasena.getPassword());
                if ("Diego".equals(usuario) && "Hola".equals(contrasena)) {
                    JOptionPane.showMessageDialog(Login.this, "¡Bienvenido, Diego!");
                    // Abrir la ventana de inicio centrada respecto a Login y pasar planificador
                    Inicio inicioFrame = new Inicio(planificador, Login.this);
                    inicioFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
