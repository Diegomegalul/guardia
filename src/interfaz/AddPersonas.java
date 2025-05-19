package interfaz;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AddPersonas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	public AddPersonas() {
		setBounds(100, 100, 730, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAadiendoPersonas = new JLabel("A\u00F1adiendo personas");
		lblAadiendoPersonas.setHorizontalAlignment(SwingConstants.CENTER);
		lblAadiendoPersonas.setFont(new Font("Arial", Font.PLAIN, 30));
		lblAadiendoPersonas.setBounds(12, 13, 688, 71);
		contentPane.add(lblAadiendoPersonas);

		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"CI", "Nombre", "Sexo", "Activo", "Tipo"
				}
				) {
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] {
					Object.class, Object.class, Object.class, Boolean.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setBounds(12, 306, 688, 234);
		contentPane.add(table);
	}
}
