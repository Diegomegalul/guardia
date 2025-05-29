package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logica.PlanificadorGuardias;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PlanificadorGuardias planificador;

	public Inicio(final PlanificadorGuardias planificador) {
		this.setPlanificador(planificador);
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
        
        JMenuItem menuEditarDiasFestivos = new JMenuItem("Dias Festivos");
        menuEditarDiasFestivos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            EditDiasFestivos frame = new EditDiasFestivos(planificador);
                            frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        	}
        });
        menu.add(menuEditarDiasFestivos);
        
        JMenuItem menuCrearPrueba = new JMenuItem("Crear personas de prueba");
        menuCrearPrueba.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // Crear 10 personas de prueba variados
                // 1-3: Estudiantes hombres, 4-5: Estudiantes mujeres
                for (int i = 1; i <= 5; i++) {
                    String ci = String.format("%011d", i);
                    String nombre = (i <= 3) ? "EstudianteH" + i : "EstudianteM" + (i - 3);
                    utiles.Sexo sexo = (i <= 3) ? utiles.Sexo.MASCULINO : utiles.Sexo.FEMENINO;
                    boolean activo = (i % 2 == 1); // alternar activo/inactivo
                    int cantidadGuardiasFestivo = i;
                    planificador.crearPersona(
                        ci,
                        nombre,
                        sexo,
                        activo,
                        i, // año de la carrera
                        cantidadGuardiasFestivo
                    );
                }
                // 6-8: Profesores hombres, 9-10: Profesoras mujeres
                for (int i = 6; i <= 10; i++) {
                    String ci = String.format("%011d", i);
                    String nombre;
                    utiles.Sexo sexo;
                    if (i <= 8) {
                        nombre = "ProfesorH" + (i - 5);
                        sexo = utiles.Sexo.MASCULINO;
                    } else {
                        nombre = "ProfesoraM" + (i - 8);
                        sexo = utiles.Sexo.FEMENINO;
                    }
                    boolean activo = (i % 2 == 0); // alternar activo/inactivo
                    java.time.LocalDate fechaIncorporacion = java.time.LocalDate.now().minusYears(i - 5).minusMonths(i);
                    int cantidadGuardiasFestivo = i;
                    planificador.crearPersona(
                        ci,
                        nombre,
                        sexo,
                        activo,
                        fechaIncorporacion,
                        cantidadGuardiasFestivo
                    );
                }
                javax.swing.JOptionPane.showMessageDialog(null, "Se han creado 10 personas de prueba variadas.");
            }
        });
        menu.add(menuCrearPrueba);
        
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
							OrganizarGuardias frame = new OrganizarGuardias(planificador);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	public PlanificadorGuardias getPlanificador() {
		return planificador;
	}

	public void setPlanificador(PlanificadorGuardias planificador) {
		this.planificador = planificador;
	}
}
