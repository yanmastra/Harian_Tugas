import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Operator extends JFrame {

	private JPanel contentPane;
	private JTextField txtKode;
	private JTextField txtNama;
	private JPasswordField txtPassword;
	private JPasswordField txtUlangiPassword;
	private final ButtonGroup jenisKelaminGroup = new ButtonGroup();
	private JTextArea txtAlamat;
	private JDateChooser dcTglLahir;
	private JButton btnSimpan;
	private JRadioButton rdbtnLakilaki;
	private JRadioButton rdbtnPerempuan;
	private JLabel lblUlangiPassword;
	
	private Koneksi koneksi = new Koneksi();
	private Connection con = koneksi.getConnection();
	private boolean modeSimpan = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Operator frame = new Operator();
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
	public Operator(String args){
		this();
		ResultSet rs;
		try{
			PreparedStatement ps = con.prepareStatement("SELECT * FROM operator WHERE id_operator = ?");
			ps.setString(1, args);
			rs = ps.executeQuery();
			rs.beforeFirst();
			while(rs.next()){
				txtKode.setText(args);
				txtKode.setEditable(false);
				txtPassword.setEditable(false);
				lblUlangiPassword.setText("Password Lama");
				txtNama.setText(rs.getString(3));
				txtAlamat.setText(rs.getString(4));
				dcTglLahir.setDate(rs.getDate(5));
				if(rs.getString(6).equals("Laki-laki")){
					rdbtnLakilaki.setSelected(true);
				}else{
					rdbtnPerempuan.setSelected(true);
				}
			}
			rs.close();
			ps.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public Operator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblKodeOperator = new JLabel("Kode Operator");
		lblKodeOperator.setBounds(10, 53, 100, 14);
		contentPane.add(lblKodeOperator);
		
		txtKode = new JTextField();
		txtKode.setBounds(116, 50, 160, 20);
		contentPane.add(txtKode);
		txtKode.setColumns(10);
		
		JLabel lblNamaOperator = new JLabel("Nama Operator");
		lblNamaOperator.setBounds(10, 81, 100, 14);
		contentPane.add(lblNamaOperator);
		
		txtNama = new JTextField();
		txtNama.setColumns(10);
		txtNama.setBounds(116, 78, 160, 20);
		contentPane.add(txtNama);
		
		JLabel lblAlamat = new JLabel("Alamat");
		lblAlamat.setBounds(10, 109, 100, 14);
		contentPane.add(lblAlamat);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 163, 100, 14);
		contentPane.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(116, 160, 160, 20);
		contentPane.add(txtPassword);
		
		lblUlangiPassword = new JLabel("Ulangi Password");
		lblUlangiPassword.setBounds(10, 185, 100, 14);
		contentPane.add(lblUlangiPassword);
		
		txtUlangiPassword = new JPasswordField();
		txtUlangiPassword.setColumns(10);
		txtUlangiPassword.setBounds(116, 185, 160, 20);
		contentPane.add(txtUlangiPassword);
		
		JLabel lblTanggalLahir = new JLabel("Tanggal Lahir");
		lblTanggalLahir.setBounds(10, 229, 100, 14);
		contentPane.add(lblTanggalLahir);
		
		dcTglLahir = new JDateChooser();
		dcTglLahir.setBounds(116, 229, 160, 20);
		contentPane.add(dcTglLahir);
		
		JLabel lblJenisKelamin = new JLabel("Jenis Kelamin");
		lblJenisKelamin.setBounds(10, 260, 100, 14);
		contentPane.add(lblJenisKelamin);
		
		rdbtnLakilaki = new JRadioButton("Laki-laki");
		jenisKelaminGroup.add(rdbtnLakilaki);
		rdbtnLakilaki.setBounds(116, 260, 76, 23);
		contentPane.add(rdbtnLakilaki);
		
		rdbtnPerempuan = new JRadioButton("Perempuan");
		jenisKelaminGroup.add(rdbtnPerempuan);
		rdbtnPerempuan.setBounds(194, 260, 106, 23);
		contentPane.add(rdbtnPerempuan);
		
		txtAlamat = new JTextArea();
		txtAlamat.setLineWrap(true);
		txtAlamat.setRows(3);
		txtAlamat.setBounds(116, 104, 160, 51);
		contentPane.add(txtAlamat);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "";
				PreparedStatement ps;
				sql = "INSERT INTO operator VALUES (?, MD5(?), ?, ?, ?, ?)";
				if(String.valueOf(txtPassword.getPassword()).equals(String.valueOf(txtUlangiPassword.getPassword()))){
					try{
						ps = con.prepareStatement(sql);
						ps.setString(1, txtKode.getText());
						ps.setString(2, String.valueOf(txtPassword.getPassword()));
						ps.setString(3, txtNama.getText());
						ps.setString(4, txtAlamat.getText());
						SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						ps.setString(5, formater.format(dcTglLahir.getDate()));
						if(rdbtnLakilaki.isSelected()){
							ps.setString(6, "Laki-laki");
						}else{
							ps.setString(6, "Perempuan");
						}
						int hasil = ps.executeUpdate();
						if(hasil>0){
							JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
							txtKode.setText("");
							txtPassword.setText("");
							txtUlangiPassword.setText("");
							txtNama.setText("");
							txtAlamat.setText("");
							dcTglLahir.setDate(null);
							rdbtnLakilaki.setSelected(false);
							rdbtnPerempuan.setSelected(false);
						}else{
							JOptionPane.showMessageDialog(null, "Data GAGAL disimpan!");
						}
					}catch(SQLException e){
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				}else{
					JOptionPane.showMessageDialog(null, "Password tidak sama!");
				}
			}
		});
		btnSimpan.setBounds(116, 286, 89, 23);
		contentPane.add(btnSimpan);
		
		JLabel lblFormOperator = new JLabel("FORM OPERATOR");
		lblFormOperator.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblFormOperator.setHorizontalAlignment(SwingConstants.CENTER);
		lblFormOperator.setBounds(10, 11, 300, 28);
		contentPane.add(lblFormOperator);
		
		JLabel txtPassError = new JLabel("");
		txtPassError.setBounds(116, 204, 160, 14);
		contentPane.add(txtPassError);
		
	}
	
	//public Operator(){
		
	//}
}
