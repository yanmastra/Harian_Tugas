import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JenisBarang extends JFrame {

	private JPanel contentPane;
	private Koneksi koneksi = new Koneksi();
	private Connection con = koneksi.getConnection();
	private JTextField jenisBarang;
	private JButton btnSimpan;
	private JTable tbJenis;
	
	String header[] = {"ID", "Jenis Brang"};
	DefaultTableModel tbModel = new DefaultTableModel(null, header);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JenisBarang frame = new JenisBarang();
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
	public JenisBarang() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				getData("SELECT * FROM jenis_barang");
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("JENIS BARANG");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 10, 434, 15);
		contentPane.add(lblTitle);
		
		JLabel lblJenisBarang_1 = new JLabel("Nama Jenis Barang");
		lblJenisBarang_1.setBounds(10, 53, 130, 15);
		contentPane.add(lblJenisBarang_1);
		
		jenisBarang = new JTextField();
		jenisBarang.setBounds(155, 50, 160, 20);
		contentPane.add(jenisBarang);
		jenisBarang.setColumns(10);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					Statement st = con.createStatement();
					String sql = "SELECT * FROM jenis_barang WHERE jenis_barang = '"+jenisBarang.getText()+"'";
					ResultSet rs = null;
					rs = st.executeQuery(sql);
					rs.last();
					if(rs.getRow()>=1){
						JOptionPane.showMessageDialog(null, "Data jenis barang sudah ada!. ("+rs.getString(1)+". "+rs.getString(2)+"). ");
					}else{
						try{
							sql = "INSERT INTO jenis_barang(jenis_barang) VALUES ('"+jenisBarang.getText()+"')";
							
							int result = st.executeUpdate(sql);
							if(result>0){
								JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan.");
								jenisBarang.setText("");
								getData("SELECT * FROM jenis_barang");
							}
						}catch(SQLException e){
							JOptionPane.showMessageDialog(null , e.getMessage());
						}
					}
				}catch(SQLException e){
					JOptionPane.showMessageDialog(null , e.getMessage());
				}
			}
		});
		btnSimpan.setBounds(155, 81, 89, 23);
		contentPane.add(btnSimpan);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 160, 414, 200);
		contentPane.add(scrollPane);
		
		tbJenis = new JTable();
		scrollPane.setViewportView(tbJenis);
		tbJenis.setModel(tbModel);		
	}
	
	private void getData(String sql){
		ResultSet rs;
		try{
			Statement st = con.createStatement();
			rs = st.executeQuery(sql);
			tbModel.getDataVector().removeAllElements();
			rs.beforeFirst();
			while(rs.next()){
				Object obj[] = new Object[2];
				obj[0] = rs.getString(1);
				obj[1] = rs.getString(2);
				tbModel.addRow(obj);
			}
			tbJenis.setModel(tbModel);
			rs.close();
			st.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
}
