import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.Duration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CariOperator extends JFrame {

	private JPanel contentPane;
	private JTextField txtNamaOperator;
	private final ButtonGroup goupJnsKelamin = new ButtonGroup();
	private JTable table;
	private JDateChooser dcTglLahir1;
	private JDateChooser dcTglLahir2;
	private JRadioButton rbPrempuan, rbLaki;
	private JButton btnCari;
	
	String[] header = {"ID", "Nama", "Alamat", "Tgl Lahir", "Jenis Kelamin"};
	private DefaultTableModel tbModel = new DefaultTableModel(null, header);
	
	Koneksi koneksi = new Koneksi();
	Connection con = koneksi.getConnection();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CariOperator frame = new CariOperator();
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
	public CariOperator() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				try{ 
					getData(con.prepareStatement("SELECT * FROM operator"));
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("CARI OPERATOR ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 495, 30);
		contentPane.add(lblNewLabel);
		
		JLabel lblNamaOperator = new JLabel("Nama Operator ");
		lblNamaOperator.setBounds(10, 30, 100, 20);
		contentPane.add(lblNamaOperator);
		
		txtNamaOperator = new JTextField();
		txtNamaOperator.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				PreparedStatement ps;
				String sql = "SELECT * FROM operator WHERE nama LIKE ? ";
				try{
					ps = con.prepareStatement(sql);
					ps.setString(1, "%"+txtNamaOperator.getText()+"%");
					getData(ps);
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		});
		txtNamaOperator.setBounds(120, 30, 150, 20);
		contentPane.add(txtNamaOperator);
		txtNamaOperator.setColumns(10);
		
		JLabel lblTanggalLahir = new JLabel("Tanggal Lahir ");
		lblTanggalLahir.setBounds(10, 55, 100, 20);
		contentPane.add(lblTanggalLahir);
		
		dcTglLahir1 = new JDateChooser();
		dcTglLahir1.setBounds(120, 55, 100, 20);
		contentPane.add(dcTglLahir1);
		
		dcTglLahir2 = new JDateChooser();
		dcTglLahir2.setBounds(240, 55, 100, 20);
		contentPane.add(dcTglLahir2);
		
		JLabel label = new JLabel("Tanggal Lahir ");
		label.setBounds(10, 80, 100, 20);
		contentPane.add(label);
		
		rbLaki = new JRadioButton("Laki-laki");
		rbLaki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PreparedStatement ps;
				String sql = "SELECT * FROM operator WHERE jenis_kelamin LIKE ? ";
				try{
					ps = con.prepareStatement(sql);
					ps.setString(1, "Laki-laki");
					getData(ps);
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		});
		goupJnsKelamin.add(rbLaki);
		rbLaki.setBounds(120, 80, 100, 20);
		contentPane.add(rbLaki);
		
		rbPrempuan = new JRadioButton("Perempuan");
		rbPrempuan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PreparedStatement ps;
				String sql = "SELECT * FROM operator WHERE jenis_kelamin LIKE ? ";
				try{
					ps = con.prepareStatement(sql);
					ps.setString(1, "%Perempuan%");
					getData(ps);
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		});
		goupJnsKelamin.add(rbPrempuan);
		rbPrempuan.setBounds(230, 80, 100, 20);
		contentPane.add(rbPrempuan);
		
		btnCari = new JButton("Cari");
		btnCari.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dcTglLahir1.getDate() ==null || dcTglLahir2.getDate() == null){
					JOptionPane.showMessageDialog(null,"Salah satu atau kedua field tanggal tidak boleh kosong");
				}else if(Duration.between(dcTglLahir1.getDate().toInstant(), dcTglLahir2.getDate().toInstant()).isNegative()){
					JOptionPane.showMessageDialog(null, "Rentang tanggal salah");
				}else{
					SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
					PreparedStatement ps;
					String sql = "SELECT * FROM operator WHERE tgl_lahir BETWEEN ? AND ?";
					
					try{
						ps = con.prepareStatement(sql);
						ps.setString(1, formater.format(dcTglLahir1.getDate()));
						ps.setString(2, formater.format(dcTglLahir2.getDate()));
						getData(ps);
					}catch(SQLException e){
						e.printStackTrace();
					}
				}
			}
		});
		btnCari.setBounds(350, 55, 80, 20);
		contentPane.add(btnCari);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 130, 464, 120);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				int i = table.getSelectedRow();
				String o = table.getValueAt(i, 0).toString();
				
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(tbModel);
		table.setAutoResizeMode(5);
		
		JLabel lblTo = new JLabel("to");
		lblTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTo.setBounds(220, 55, 20, 20);
		contentPane.add(lblTo);
		table.setRowSelectionAllowed(true);
	}

	private void getData(PreparedStatement ps){
		SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy");
		ResultSet rs = null;
		try{
			rs = ps.executeQuery();
			tbModel.getDataVector().removeAllElements();
			
			rs.beforeFirst();
			while(rs.next()){
				Object obj[] = new Object[6];
				obj[0] = rs.getString(1);
				obj[1] = rs.getString(3);
				obj[2] = rs.getString(4);
				obj[3] = formater.format(rs.getDate(5));
				obj[4] = rs.getString(6);
				tbModel.addRow(obj);
			}
			table.setModel(tbModel);
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
