package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.List;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import logica.PlanificadorGuardias;
import logica.DiaFestivo;
import com.toedter.calendar.JDateChooser;

public class EditDiasFestivos extends JFrame {
    private PlanificadorGuardias planificador;
    private DefaultListModel<DiaFestivo> modeloLista;
    private JList<DiaFestivo> listaDias;
    private JTextField txtDescripcion;
    private JDateChooser dateChooser;

    private static final long serialVersionUID = 1L;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Necesita un planificador para funcionar correctamente
                    // EditDiasFestivos frame = new EditDiasFestivos(planificador);
                    // frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public EditDiasFestivos(PlanificadorGuardias planificador) {
        this.planificador = planificador;
        setTitle("Editar D\u00EDas Festivos");
        setSize(400, 350);
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modeloLista = new DefaultListModel<DiaFestivo>();
        listaDias = new JList<DiaFestivo>(modeloLista);
        JScrollPane scroll = new JScrollPane(listaDias);

        cargarDiasFestivos();

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        panelForm.add(new JLabel("Fecha:"));
        dateChooser = new JDateChooser();
        panelForm.add(dateChooser);

        panelForm.add(new JLabel("Descripci\u00F3n:"));
        txtDescripcion = new JTextField();
        panelForm.add(txtDescripcion);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnEditar = new JButton("Editar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(panelForm, BorderLayout.NORTH);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                agregarDiaFestivo();
            }
        });
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                eliminarDiaFestivo();
            }
        });
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                editarDiaFestivo();
            }
        });
    }

    private void cargarDiasFestivos() {
        modeloLista.clear();
        List<DiaFestivo> lista = planificador.getDiasFestivos();
        for (DiaFestivo df : lista) {
            modeloLista.addElement(df);
        }
        // Personaliza la visualización en la lista
        listaDias.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DiaFestivo) {
                    DiaFestivo df = (DiaFestivo) value;
                    setText(df.getFechaString() + " - " + df.getDescripcion());
                }
                return this;
            }
        });
    }

    private void agregarDiaFestivo() {
        Date date = dateChooser.getDate();
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una fecha.");
            return;
        }
        LocalDate fecha = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String descripcion = txtDescripcion.getText().trim();
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción.");
            return;
        }
        DiaFestivo nuevo = new DiaFestivo(fecha, descripcion);
        planificador.agregarDiaFestivo(nuevo);
        cargarDiasFestivos();
    }

    private void eliminarDiaFestivo() {
        DiaFestivo seleccionado = listaDias.getSelectedValue();
        if (seleccionado != null) {
            planificador.eliminarDiaFestivo(seleccionado.getFecha());
            cargarDiasFestivos();
        }
    }

    private void editarDiaFestivo() {
        DiaFestivo seleccionado = listaDias.getSelectedValue();
        if (seleccionado != null) {
            Date date = dateChooser.getDate();
            if (date == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha.");
                return;
            }
            LocalDate fecha = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            String descripcion = txtDescripcion.getText().trim();
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una descripción.");
                return;
            }
            seleccionado.setFecha(fecha);
            seleccionado.setDescripcion(descripcion);
            cargarDiasFestivos();
        }
    }
}
