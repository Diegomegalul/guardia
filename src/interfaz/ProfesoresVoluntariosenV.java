package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import logica.PlanificadorGuardias;
import java.util.List;

public class ProfesoresVoluntariosenV extends JFrame {

    private JTable tabla;
    private JPanel contentPane;

    public ProfesoresVoluntariosenV() {
        setTitle("Reporte - Profesores Voluntarios en Vacaciones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        
        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(240, 240, 240));
        JLabel lblTitulo = new JLabel("PROFESORES VOLUNTARIOS EN VACACIONES (JULIO-AGOSTO)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelTitulo.add(lblTitulo);
        contentPane.add(panelTitulo, BorderLayout.NORTH);
        
        // Tabla principal
        String[] columnas = {"N°", "CÉDULA", "NOMBRE COMPLETO", "GUARDIAS EN VACACIONES"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.setRowHeight(25);
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setShowGrid(true);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelInferior.add(btnCerrar);
        contentPane.add(panelInferior, BorderLayout.SOUTH);
        
        cargarDatos();
    }
    
    private void cargarDatos() {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar tabla
        
        PlanificadorGuardias planificador = PlanificadorGuardias.getInstancia();
        List<String> voluntarios = planificador.reporteProfesoresVoluntariosEnVacaciones();
        
        if (voluntarios.isEmpty()) {
            modelo.addRow(new Object[]{"", "No hay datos disponibles", "", ""});
        } else {
            for (int i = 0; i < voluntarios.size(); i++) {
                // Adapta esto según la estructura real de tus datos
                String[] partes = voluntarios.get(i).split(" ");
                String nombreCompleto = partes[0] + " " + partes[1];
                
                modelo.addRow(new Object[]{
                    i + 1,
                    "CI-PROF-" + (i + 1), // Ejemplo, usa la cédula real
                    nombreCompleto,
                    "2" // Ejemplo, usa el dato real de guardias
                });
            }
        }
    }
}