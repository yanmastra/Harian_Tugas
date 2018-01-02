import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Barang extends JFrame {

	private JPanel contentPane;
	private JTextField txtKodeBarang;
	private JTextField txtNamaBarang;
	private JTextField txtHarga;
	private JTextField txtStock;
	private JComboBox cbJenisBarang;
	private JButton btnSimpan, btnUpdate, btnHapus;
	private String value,value2;
	
	private DefaultComboBoxModel cbModel = new DefaultComboBoxModel();
	private int[] id_jenis;
	
	Koneksi koneksi = new Koneksi();
	Connection con = koneksi.getConnection();
	private JTable table;
	
	String header[] = {"Kode Barang", "Nama Barang", "Harga", "Stok", "Jenis Barang"};
	DefaultTableModel tbModel = new DefaultTableModel(null, header);
	String sqlData = "SELECT barang.kode_barang, "
			+ "barang.nama_barang, "
			+ "barang.harga_barang, "
			+ "barang.stock_barang, "
			+ "jenis_barang.jenis_barang "
			+ "FROM jenis_barang INNER JOIN barang "
			+ "ON barang.jenis_barang = jenis_barang.kode_jenis";
	
	private JTextField txtCari;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Barang frame = new Barang();
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
	public Barang() {
		setFont(new Font("Georgia", Font.BOLD, 15));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				try{
					Statement st = con.createStatement();
					String sql = "SELECT * FROM jenis_barang ORDER BY jenis_barang";
					ResultSet rs = st.executeQuery(sql);
					rs.last();
					id_jenis = new int[rs.getRow()];
					int i = 0;
					rs.beforeFirst();
					while(rs.next()){
						cbModel.addElement(rs.getString(2));
						id_jenis[i] = rs.getInt(1);
						i++;
					}
					cbJenisBarang.setModel(cbModel);
					st.close();
					getData(sqlData);
					btnUpdate.setEnabled(false);
					btnHapus.setEnabled(false);
				}catch(SQLException e){
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 430, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("FORM DATA BARANG");
		lblTitle.setFont(new Font("Georgia", Font.BOLD, 15));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 11, 414, 20);
		contentPane.add(lblTitle);
		
		JLabel lblKodeBarang = new JLabel("Kode Barang");
		lblKodeBarang.setBounds(10, 35, 124, 20);
		contentPane.add(lblKodeBarang);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 60, 124, 20);
		contentPane.add(lblNamaBarang);
		
		txtKodeBarang = new JTextField();
		txtKodeBarang.setBounds(130, 35, 100, 20);
		contentPane.add(txtKodeBarang);
		txtKodeBarang.setColumns(10);
		//txtKodeBarang.setMinimumSize(getMinimumSize());
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setColumns(10);
		txtNamaBarang.setBounds(130, 60, 137, 20);
		contentPane.add(txtNamaBarang);
		
		txtHarga = new JTextField();
		txtHarga.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(KeyFilter.isNumericKey(arg0, true)) {
					value = txtHarga.getText();
				}else {
					JOptionPane.showMessageDialog(null, "Inputkan angka");
					txtHarga.setText(value);
				}
			}
		});
		txtHarga.setColumns(10);
		txtHarga.setBounds(130, 87, 100, 20);
		contentPane.add(txtHarga);
		
		txtStock = new JTextField();
		txtStock.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(KeyFilter.isNumericKey(arg0, true)) {
					value2 = txtStock.getText();
				}else {
					JOptionPane.showMessageDialog(null, "Inputkan angka");
					txtStock.setText(value2);
				}
			}
		});
		txtStock.setColumns(10);
		txtStock.setBounds(130, 114, 50, 20);
		contentPane.add(txtStock);
		
		JLabel lblHarga = new JLabel("Harga");
		lblHarga.setBounds(10, 87, 124, 20);
		contentPane.add(lblHarga);
		
		JLabel lblStok = new JLabel("Stok");
		lblStok.setBounds(10, 114, 124, 20);
		contentPane.add(lblStok);
		
		JLabel lblJenis = new JLabel("Jenis");
		lblJenis.setBounds(10, 140, 124, 20);
		contentPane.add(lblJenis);
		
		cbJenisBarang = new JComboBox();
		cbJenisBarang.setBounds(130, 140, 137, 20);
		contentPane.add(cbJenisBarang);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
					String sql = "INSERT INTO barang VALUES (?, ?, ?, ?, ?);";
					PreparedStatement ps  = con.prepareStatement(sql);
					ps.setString(1, txtKodeBarang.getText());
					ps.setString(2, txtNamaBarang.getText());
					ps.setString(3, txtHarga.getText());
					ps.setString(4, txtStock.getText());
					ps.setInt(5, id_jenis[cbJenisBarang.getSelectedIndex()]);
					int result = ps.executeUpdate();
					if(result>0){
						JOptionPane.showMessageDialog(null, "Data berhasil disimpan.");
						resetField();
						getData(sqlData);
					}
					ps.close();
				}catch(SQLException e){
					JOptionPane.showMessageDialog(null, "Lengkapi form !\n\n"+e.getMessage());
				}
			}
		});
		btnSimpan.setBounds(10, 170, 90, 25);
		contentPane.add(btnSimpan);
		
		JButton btnUlang = new JButton("Ulang");
		btnUlang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetField();
			}
		});
		btnUlang.setBounds(110, 170, 70, 25);
		contentPane.add(btnUlang);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 225, 400, 130);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = table.getSelectedRow();
				if(row == -1) {
					return;
				}else {
					txtKodeBarang.setText(table.getValueAt(row, 0).toString());
					txtNamaBarang.setText(table.getValueAt(row, 1).toString());
					txtHarga.setText(table.getValueAt(row, 2).toString());
					txtStock.setText(table.getValueAt(row, 3).toString());
					cbJenisBarang.setSelectedItem(table.getValueAt(row, 4));
					txtKodeBarang.setEditable(false);
					btnSimpan.setEnabled(false);
					btnUpdate.setEnabled(true);
					btnHapus.setEnabled(true);
				}
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		table.setModel(tbModel);
		
		txtCari = new JTextField();
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String txt = txtCari.getText();
				getData(sqlData+" WHERE "
						+ "nama_barang LIKE '%"+txt+
						"%' OR kode_barang LIKE '%"+txt+
						"%' OR harga_barang LIKE '%"+txt+
						"%' OR stock_barang LIKE '%"+txt+
						"%' OR jenis_barang.jenis_barang LIKE '%"+txt+"%'");
			}
		});
		txtCari.setBounds(280, 197, 130, 20);
		contentPane.add(txtCari);
		txtCari.setColumns(10);
		
		JLabel lblCari = new JLabel("Cari :");
		lblCari.setBounds(233, 201, 50, 14);
		contentPane.add(lblCari);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "UPDATE barang SET nama_barang = ?, harga_barang = ?, stock_barang = ?, jenis_barang = ? WHERE kode_barang = ?";
				try {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setString(1, txtNamaBarang.getText());
					ps.setString(2, txtHarga.getText());
					ps.setString(3, txtStock.getText());
					ps.setInt(4, id_jenis[cbJenisBarang.getSelectedIndex()]);
					ps.setString(5, txtKodeBarang.getText());
					int res = ps.executeUpdate();
					if(res>0) {
						JOptionPane.showMessageDialog(null, "Perubahan telah tersimpan");
						getData(sqlData);
						resetField();
					}else {
						JOptionPane.showMessageDialog(null, "Perubahan GAGAL disimpan!");
					}
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(10, 196, 90, 23);
		contentPane.add(btnUpdate);
		
		btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String sql = "DELETE FROM barang WHERE kode_barang = '"+txtKodeBarang.getText()+"'";
				int confirm = JOptionPane.showConfirmDialog(null, "Yakin ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if(confirm == JOptionPane.YES_OPTION) {
					try {
						Statement st = con.createStatement();
						int res = st.executeUpdate(sql);
						if(res>0) {
							resetField();
							getData(sqlData);
							JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
						}else {
							JOptionPane.showMessageDialog(null, "Data gagal dihapus");
						}
					}catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnHapus.setBounds(110, 196, 70, 23);
		contentPane.add(btnHapus);
	}
	
	private void resetField(){
		getData(sqlData);
		txtKodeBarang.setText("");
		txtNamaBarang.setText("");
		txtHarga.setText("");
		txtStock.setText("");
		cbJenisBarang.setSelectedIndex(0);
		txtKodeBarang.setEditable(true);
		btnSimpan.setEnabled(true);
		btnUpdate.setEnabled(false);
		btnHapus.setEnabled(false);
	}
	

	private void getData(String sql){
		ResultSet rs;
		try{
			Statement st = con.createStatement();
			rs = st.executeQuery(sql);
			tbModel.getDataVector().removeAllElements();
			tbModel.fireTableDataChanged();
			rs.beforeFirst();
			while(rs.next()){
				Object obj[] = new Object[6];
				obj[0] = rs.getString(1);
				obj[1] = rs.getString(2);
				obj[2] = rs.getString(3);
				obj[3] = rs.getString(4);
				obj[4] = rs.getString(5);
				tbModel.addRow(obj);
			}
			table.setModel(tbModel);
			rs.close();
			st.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
