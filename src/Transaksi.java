import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Transaksi extends JFrame {

	private JPanel contentPane;
	private JTextField txtNoPegawai, txtNamaPegawai, txtKode, txtNamaBarang, txtHarga, txtJumlah;
	private JLabel lblKodeBarang, lblNamaBarang, lblHarga, lblJumlah, lblTotal;
	private JButton btnSave;
	
	private Koneksi konek = new Koneksi();
	private Connection con = konek.getConnection();
	
	private PreparedStatement ps;
	private int stocks = 0;
	private int subtotal = 0;
	
	private JTable table;
	private JTextField txtBayar;
	
	private DefaultTableModel tbModel = new DefaultTableModel(null, new String[] {"Kode Barang", "Nama Barang", "Harga", "Jumlah", "Subtotal"});
	private int total = 0;
	private JLabel lblBayar;
	private JTextField txtKembali;
	private JLabel lblKembali;
	
	private String bla;

	public String getBla() {
		return bla;
	}

	public void setBla(String bla) {
		this.bla = bla;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Transaksi frame = new Transaksi();
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
	public Transaksi() {
		setTitle("Frame Transaksi");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 455, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNoPegawai = new JLabel("No. Pegawai");
		lblNoPegawai.setBounds(10, 40, 80, 14);
		contentPane.add(lblNoPegawai);
		
		txtNoPegawai = new JTextField();
		txtNoPegawai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ps = con.prepareStatement("SELECT * FROM operator WHERE id_operator = ?");
					ps.setString(1, txtNoPegawai.getText());
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						txtNamaPegawai.setText(rs.getString(3));
						txtKode.setEditable(true);
						txtKode.requestFocus();
					}else {
						JOptionPane.showMessageDialog(null, "Id Operator salah!");
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
		txtNoPegawai.setEditable(true);
		txtNoPegawai.setBounds(90, 37, 100, 20);
		contentPane.add(txtNoPegawai);
		txtNoPegawai.setColumns(10);
		
		JLabel lblNamaPegawai = new JLabel("Nama Pegawai");
		lblNamaPegawai.setBounds(200, 40, 94, 14);
		contentPane.add(lblNamaPegawai);
		
		txtNamaPegawai = new JTextField();
		txtNamaPegawai.setEditable(false);
		txtNamaPegawai.setColumns(10);
		txtNamaPegawai.setBounds(294, 37, 130, 20);
		contentPane.add(txtNamaPegawai);
		
		txtKode = new JTextField();
		txtKode.setBounds(10, 100, 97, 20);
		contentPane.add(txtKode);
		txtKode.setColumns(10);
		txtKode.setEditable(false);
		
		txtKode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ps = con.prepareStatement("SELECT * FROM barang WHERE kode_barang = ?");
					ps.setString(1, txtKode.getText());
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						if(rs.getInt(4)>0) {
							txtNamaBarang.setText(rs.getString(2));
							txtHarga.setText(rs.getString(3));
							stocks = rs.getInt(4);
							txtJumlah.setEditable(true);
							txtJumlah.requestFocus();
						}else {
							JOptionPane.showMessageDialog(null, "Stock Barang habis!");
						}
					}else {
						JOptionPane.showMessageDialog(null, "Kode barang tidak ditemukan");
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(117, 100, 147, 20);
		contentPane.add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtHarga = new JTextField("0");
		txtHarga.setEditable(false);
		txtHarga.setBounds(274, 100, 80, 20);
		contentPane.add(txtHarga);
		txtHarga.setColumns(10);
		
		txtJumlah = new JTextField("0");
		txtJumlah.setBounds(364, 100, 60, 20);
		contentPane.add(txtJumlah);
		txtJumlah.setColumns(10);
		txtJumlah.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				int jumlah = 0;
				jumlah = Integer.valueOf(txtJumlah.getText());
				if(stocks < jumlah || jumlah == 0) {
					JOptionPane.showMessageDialog(null, "Jumlah tidak bisa melebihi stok yang ada dan tidak boleh 0\nStok saat ini"+stocks);
					txtJumlah.setText(stocks+"");
				}else {
					subtotal = jumlah * Integer.valueOf(txtHarga.getText());
					Object col[] = new Object[5];
					col[0] = txtKode.getText();
					col[1] = txtNamaBarang.getText();
					col[2] = txtHarga.getText();
					col[3] = txtJumlah.getText();
					col[4] = subtotal;
					tbModel.addRow(col);
					
					total += subtotal;
					lblTotal.setText("Total : "+total);
					
					txtKode.setText("");
					txtNamaBarang.setText("");
					txtHarga.setText("");
					txtJumlah.setText("");
					txtKode.requestFocus();
					txtBayar.setEditable(true);
				}
			}
		});
		txtJumlah.setEditable(false);
		
		lblKodeBarang = new JLabel("Kode Barang");
		lblKodeBarang.setBounds(10, 80, 97, 14);
		contentPane.add(lblKodeBarang);
		
		lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(117, 80, 147, 14);
		contentPane.add(lblNamaBarang);
		
		lblHarga = new JLabel("Harga");
		lblHarga.setBounds(274, 80, 80, 14);
		contentPane.add(lblHarga);
		
		
		lblJumlah = new JLabel("Jumlah");
		lblJumlah.setBounds(364, 80, 60, 14);
		contentPane.add(lblJumlah);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 150, 424, 110);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tbModel);
		
		txtBayar = new JTextField();
		txtBayar.setColumns(10);
		txtBayar.setBounds(100, 271, 86, 20);
		contentPane.add(txtBayar);
		txtBayar.setEditable(false);
		
		txtBayar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int bayar = Integer.valueOf(txtBayar.getText());
				if(total>bayar) {
					JOptionPane.showMessageDialog(null, "Jumlah bayar tidak boleh kurang!");
				}else {
					int kembali = bayar - total;
					txtKembali.setText(kembali+"");
					btnSave.setEnabled(true);
				}
			}
		});
		
		lblTotal = new JLabel("Total : 0");
		lblTotal.setHorizontalAlignment(SwingConstants.LEFT);
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTotal.setBounds(10, 125, 145, 14);
		contentPane.add(lblTotal);
		
		lblBayar = new JLabel("Bayar");
		lblBayar.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBayar.setBounds(10, 274, 80, 14);
		contentPane.add(lblBayar);
		
		txtKembali = new JTextField();
		txtKembali.setEditable(false);
		txtKembali.setColumns(10);
		txtKembali.setBounds(275, 271, 86, 20);
		contentPane.add(txtKembali);
		
		lblKembali = new JLabel("Kembali");
		lblKembali.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKembali.setBounds(191, 274, 80, 14);
		contentPane.add(lblKembali);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(367, 270, 67, 23);
		btnSave.setEnabled(false);
		contentPane.add(btnSave);
		
		JLabel lblTransaksi = new JLabel("TRANSAKSI");
		lblTransaksi.setFont(new Font("Georgia", Font.BOLD, 18));
		lblTransaksi.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransaksi.setBounds(0, 5, 439, 20);
		contentPane.add(lblTransaksi);
		
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					ps = null;
					ps = con.prepareStatement("INSERT INTO transaksi(id_operator) VALUE (?)", ps.RETURN_GENERATED_KEYS);
					ps.setString(1, txtNoPegawai.getText());
					int res = ps.executeUpdate();
					if(res>0) {
						System.out.println("Insert transaksi berhasil");
						ResultSet rs = ps.getGeneratedKeys();
						rs.first();
						System.out.println("generate key : "+rs.getString(1));
						ps = con.prepareStatement("INSERT INTO detail_transaksi VALUES (?, ?, ?)");
						for(int i=0; i<tbModel.getRowCount();i++) {
							ps.setInt(1, rs.getInt(1));
							ps.setString(2, tbModel.getValueAt(i, 0).toString());
							ps.setString(3, tbModel.getValueAt(i, 3).toString());
							res = ps.executeUpdate();
							if(res>0) {
								System.out.println("Insert detail transaksi berhasil "+i);
//								Statement st = con.createStatement();
//								int hasil = st.executeUpdate("UPDATE barang SET stock_barang = stock_barang -"+tbModel.getValueAt(i, 3)+" WHERE kode_barang = '"+tbModel.getValueAt(i, 0));
//								if(hasil>0) { 
//									System.out.println("Update stok barang berhasil");
//								}else System.out.println("Update stok barang GAGAL");
							}else {
								System.out.println("Insert ddetail transaksi GAGAL");
							}
						}
						JOptionPane.showMessageDialog(null, "Data transaksi disimpan!");
						tbModel.getDataVector().removeAllElements();
						tbModel.fireTableDataChanged();
						txtKode.requestFocus();
						txtBayar.setEditable(false);
						txtBayar.setText("");
						btnSave.setEnabled(false);
						total = 0;
					}else {
						JOptionPane.showMessageDialog(null, "Ada kesalahan dalam menyimpan data");
						System.out.println("Insert detail transaksi berhasil");
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setOperator(String id, String nama) {
		txtNoPegawai.setText(id);
		txtNamaPegawai.setText(nama);
		txtNoPegawai.setEnabled(false);
		txtKode.requestFocus();
	}
}
