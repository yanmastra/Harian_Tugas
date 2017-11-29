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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Barang extends JFrame {

	private JPanel contentPane;
	private JTextField txtKodeBarang;
	private JTextField txtNamaBarang;
	private JTextField txtHarga;
	private JTextField txtStock;
	private JComboBox cbJenisBarang;
	private JButton btnSimpan;
	
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
				}catch(SQLException e){
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("INPUT JENIS BARANG");
		lblTitle.setFont(new Font("Georgia", Font.BOLD, 15));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 11, 450, 20);
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
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setColumns(10);
		txtNamaBarang.setBounds(130, 60, 137, 20);
		contentPane.add(txtNamaBarang);
		
		txtHarga = new JTextField();
		txtHarga.setColumns(10);
		txtHarga.setBounds(130, 87, 100, 20);
		contentPane.add(txtHarga);
		
		txtStock = new JTextField();
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
					String sql = "INSERT INTO barang VALUES ("
							+ "'"+txtKodeBarang.getText()
							+ "','"+txtNamaBarang.getText()
							+ "','"+txtHarga.getText()
							+ "','"+txtStock.getText()
							+ "','"+id_jenis[cbJenisBarang.getSelectedIndex()]
							+ "')";
					Statement st = con.createStatement();
					int result = st.executeUpdate(sql);
					if(result>0){
						JOptionPane.showMessageDialog(null, "Data berhasil disimpan.");
						resetField();
						getData(sqlData);
					}
					st.close();
				}catch(SQLException e){
					e.printStackTrace();
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
		scrollPane.setBounds(10, 200, 400, 150);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tbModel);
		
		txtCari = new JTextField();
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				getData(sqlData+" WHERE nama_barang LIKE '%"+txtCari.getText()+"%' ");
			}
		});
		txtCari.setBounds(280, 171, 130, 20);
		contentPane.add(txtCari);
		txtCari.setColumns(10);
		
		JLabel lblCari = new JLabel("Cari :");
		lblCari.setBounds(233, 175, 50, 14);
		contentPane.add(lblCari);
	}
	
	private void resetField(){
		txtKodeBarang.setText("");
		txtNamaBarang.setText("");
		txtHarga.setText("");
		txtStock.setText("");
		cbJenisBarang.setSelectedIndex(0);
	}
	

	private void getData(String sql){
		ResultSet rs;
		try{
			Statement st = con.createStatement();
			rs = st.executeQuery(sql);
			tbModel.getDataVector().removeAllElements();
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
