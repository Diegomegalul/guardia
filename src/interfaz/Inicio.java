package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
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

	/**
	 * @wbp.parser.constructor
	 */
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

        // Opciones como botones directos en la barra
        JMenuItem menuAddPersonas = new JMenuItem("Añadir personas");
        JMenuItem menuOrganizarGuardias = new JMenuItem("Organizar guardias");
        JMenuItem menuEditarDiasFestivos = new JMenuItem("Dias Festivos");
        JMenuItem menuCrearPrueba = new JMenuItem("Crear personas de prueba");

        menuBar.add(menuAddPersonas);
        menuBar.add(menuOrganizarGuardias);
        menuBar.add(menuEditarDiasFestivos);
        menuBar.add(menuCrearPrueba);

        setJMenuBar(menuBar);

        // Listeners igual que antes
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

        menuCrearPrueba.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // Crear 16 estudiantes con grupos 11,12,13,14,21,22,23,24,31,32,33,34,41,42,43,44
                int[] grupos = {11,12,13,14,21,22,23,24,31,32,33,34,41,42,43,44};
                for (int i = 0; i < grupos.length; i++) {
                    String ci = String.format("%011d", i+1);
                    String nombre = "Estudiante" + (i+1);
                    utiles.Sexo sexo = (i % 2 == 0) ? utiles.Sexo.MASCULINO : utiles.Sexo.FEMENINO;
                    boolean activo = ((i+1) % 2 == 1); // alternar activo/inactivo
                    int cantidadGuardias = 0;
                    int cantidadGuardiasFestivo = 0;
                    int grupo = grupos[i];
                    planificador.crearPersona(
                        ci,
                        nombre,
                        sexo,
                        activo,
                        cantidadGuardias,
                        cantidadGuardiasFestivo,
                        grupo
                    );
                }
                // 4 trabajadores hombres, 4 trabajadoras mujeres
                for (int i = 17; i <= 24; i++) {
                    String ci = String.format("%011d", i);
                    String nombre;
                    utiles.Sexo sexo;
                    if (i <= 20) {
                        nombre = "ProfesorH" + (i - 16);
                        sexo = utiles.Sexo.MASCULINO;
                    } else {
                        nombre = "ProfesoraM" + (i - 20);
                        sexo = utiles.Sexo.FEMENINO;
                    }
                    boolean activo = (i % 2 == 0); // alternar activo/inactivo
                    int cantidadGuardias = 0;
                    int grupo = 0; // Grupo no relevante para trabajadores
                    if (activo) {
                        // Si está activo, no tiene fecha de incorporación
                        planificador.crearPersona(
                            ci,
                            nombre,
                            sexo,
                            true,
                            null, cantidadGuardias, grupo
                        );
                    } else {
                        // Si está inactivo, sí tiene fecha de incorporación
                        java.time.LocalDate fechaIncorporacion = java.time.LocalDate.now().minusYears(i - 16).minusMonths(i);
                        planificador.crearPersona( ci, nombre, sexo, false, fechaIncorporacion, cantidadGuardias, grupo);
                    }
                }
                javax.swing.JOptionPane.showMessageDialog(null, "Se han creado 16 estudiantes y 8 trabajadores de prueba con grupos 11-44.");
            }
        });
        
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

	public Inicio(final PlanificadorGuardias planificador, java.awt.Window parent) {
		this.setPlanificador(planificador);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		setLocationRelativeTo(parent);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		// Menú
        JMenuBar menuBar = new JMenuBar();

        // Opciones como botones directos en la barra
        JMenuItem menuAddPersonas = new JMenuItem("Añadir personas");
        JMenuItem menuOrganizarGuardias = new JMenuItem("Organizar guardias");
        JMenuItem menuEditarDiasFestivos = new JMenuItem("Dias Festivos");
        JMenuItem menuCrearPrueba = new JMenuItem("Crear personas de prueba");

        menuBar.add(menuAddPersonas);
        menuBar.add(menuOrganizarGuardias);
        menuBar.add(menuEditarDiasFestivos);
        menuBar.add(menuCrearPrueba);

        setJMenuBar(menuBar);

        // Listeners igual que antes
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

        menuCrearPrueba.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // Crear 16 estudiantes con grupos 11,12,13,14,21,22,23,24,31,32,33,34,41,42,43,44
                int[] grupos = {11,12,13,14,21,22,23,24,31,32,33,34,41,42,43,44};
                for (int i = 0; i < grupos.length; i++) {
                    String ci = String.format("%011d", i+1);
                    String nombre = "Estudiante" + (i+1);
                    utiles.Sexo sexo = (i % 2 == 0) ? utiles.Sexo.MASCULINO : utiles.Sexo.FEMENINO;
                    boolean activo = ((i+1) % 2 == 1); // alternar activo/inactivo
                    int cantidadGuardias = 0;
                    int cantidadGuardiasFestivo = 0;
                    int grupo = grupos[i];
                    planificador.crearPersona(
                        ci,
                        nombre,
                        sexo,
                        activo,
                        cantidadGuardias,
                        cantidadGuardiasFestivo,
                        grupo
                    );
                }
                // 4 trabajadores hombres, 4 trabajadoras mujeres
                for (int i = 17; i <= 24; i++) {
                    String ci = String.format("%011d", i);
                    String nombre;
                    utiles.Sexo sexo;
                    if (i <= 20) {
                        nombre = "ProfesorH" + (i - 16);
                        sexo = utiles.Sexo.MASCULINO;
                    } else {
                        nombre = "ProfesoraM" + (i - 20);
                        sexo = utiles.Sexo.FEMENINO;
                    }
                    boolean activo = (i % 2 == 0); // alternar activo/inactivo
                    int cantidadGuardias = 0;
                    int grupo = 0; // Grupo no relevante para trabajadores
                    if (activo) {
                        // Si está activo, no tiene fecha de incorporación
                        planificador.crearPersona(
                            ci,
                            nombre,
                            sexo,
                            true,
                            null, cantidadGuardias, grupo
                        );
                    } else {
                        // Si está inactivo, sí tiene fecha de incorporación
                        java.time.LocalDate fechaIncorporacion = java.time.LocalDate.now().minusYears(i - 16).minusMonths(i);
                        planificador.crearPersona( ci, nombre, sexo, false, fechaIncorporacion, cantidadGuardias, grupo);
                    }
                }
                javax.swing.JOptionPane.showMessageDialog(null, "Se han creado 16 estudiantes y 8 trabajadores de prueba con grupos 11-44.");
            }
        });
        
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
