import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class CariBarang extends JFrame {

	private JPanel contentPane;
	private JTextField txtNamaBarang;
	private JLabel lblHarga;
	private JTextField txtHarga1;
	private JTextField txtHarga2;
	private JLabel lblTo;
	private JLabel lblKategori;
	private JTable table;
	private JComboBox cbJenis;
	private int[] Id_jenis;
	private JLabel lblResult;
	
	Koneksi koneksi = new Koneksi();
	Connection con = koneksi.getConnection();
	
	DefaultComboBoxModel cbMode = new DefaultComboBoxModel();
	String[] header = {"Kode Barang", "Nama Barang", "Harga", "Stock", "Jenis Barang"};
	DefaultTableModel tbModel = new DefaultTableModel(null, header);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CariBarang frame = new CariBarang();
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
	public CariBarang() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				try{
					Statement ps = con.createStatement();
					ResultSet rs = ps.executeQuery("SELECT * FROM jenis_barang");
					rs.last();
					Id_jenis = new int[rs.getRow()+1];
					rs.beforeFirst();
					int i = 0;
					while(rs.next()){
						cbMode.addElement(rs.getString(2));
						Id_jenis[i] = rs.getInt(1);
						i++;
					}
					cbMode.addElement("Semua Kategori");
					Id_jenis[i] = 999;
					cbJenis.setModel(cbMode);
				}catch(SQLException e){
					e.printStackTrace();
				}
				getData("1");
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		setTitle("Form Cari Barang");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				//if() System.out.println("true");
				//else System.out.println("false");
				//System.out.println(arg0.getKeyCode()+"");
				getData("nama_barang LIKE '%"+txtNamaBarang.getText()+"%'");
				//System.out.println("if bernilai true");
			}
		});
		txtNamaBarang.setBounds(130, 40, 195, 20);
		contentPane.add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 43, 110, 14);
		contentPane.add(lblNamaBarang);
		
		lblHarga = new JLabel("Harga");
		lblHarga.setBounds(10, 71, 110, 14);
		contentPane.add(lblHarga);
		
		txtHarga1 = new JTextField();
		txtHarga1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!txtHarga2.getText().equals("") && !txtHarga1.getText().equals("")){
					getData("harga_barang BETWEEN "+txtHarga1.getText()+" AND "+txtHarga2.getText());
				}else{
					getData("1");
				}
			}
		});
		txtHarga1.setColumns(10);
		txtHarga1.setBounds(130, 68, 86, 20);
		contentPane.add(txtHarga1);
		
		txtHarga2 = new JTextField();
		txtHarga2.setColumns(10);
		txtHarga2.setBounds(239, 68, 86, 20);
		contentPane.add(txtHarga2);
		txtHarga2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!txtHarga2.getText().equals("") && !txtHarga1.getText().equals("")){
					getData("harga_barang BETWEEN "+txtHarga1.getText()+" AND "+txtHarga2.getText());
				}else{
					getData("1");
				}
			}
		});
		
		lblTo = new JLabel("to");
		lblTo.setBounds(219, 71, 17, 14);
		contentPane.add(lblTo);
		
		lblKategori = new JLabel("Kategori");
		lblKategori.setBounds(10, 96, 110, 14);
		contentPane.add(lblKategori);
		
		cbJenis = new JComboBox();
		cbJenis.setBounds(130, 93, 106, 20);
		contentPane.add(cbJenis);
		cbJenis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Id_jenis[cbJenis.getSelectedIndex()] == 999)
					getData("1");
				else
					getData("barang.jenis_barang = '"+Id_jenis[cbJenis.getSelectedIndex()]+"'");
			}
		});;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 124, 414, 176);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tbModel);
		
		lblResult = new JLabel("");
		lblResult.setForeground(Color.RED);
		lblResult.setBounds(274, 96, 150, 14);
		contentPane.add(lblResult);
	}

	private void getData(String whereClause){
		ResultSet rs = null;
		try{
			String sql = "SELECT"
					+ " kode_barang,"
					+ " nama_barang,"
					+ " harga_barang,"
					+ " stock_barang,"
					+ " jenis_barang.jenis_barang"
					+ " FROM jenis_barang INNER JOIN barang "
					+ "ON barang.jenis_barang = jenis_barang.kode_jenis"
					+ " WHERE "+whereClause;
			PreparedStatement ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
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
			rs.last();
			if(rs.getRow()<=0){
				getData("1");
				lblResult.setText("Data tidak ditemukan!");
			}else{
				table.setModel(tbModel);
				lblResult.setText("");
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
