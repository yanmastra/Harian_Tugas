import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Home extends JFrame {

	private JPanel contentPane;
	
	//operator;
	private String idOperator = "";
	private String namaOperator = "";
	
	private JenisBarang frJenis;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Home();
					frame.setVisible(true);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 430);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDataMaster = new JMenu("Data Master");
		menuBar.add(mnDataMaster);
		
		JMenuItem mntmKategori = new JMenuItem("Kategori");
		mntmKategori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frJenis = new JenisBarang();
				frJenis.setVisible(true);
			}
		});
		mnDataMaster.add(mntmKategori);
		
		JMenu mnDataBarang = new JMenu("Data Barang");
		mnDataMaster.add(mnDataBarang);
		
		JMenuItem mntmTambahBarang = new JMenuItem("Tambah Barang");
		mnDataBarang.add(mntmTambahBarang);
		
		JMenu mnOperator = new JMenu("Operator");
		mnDataMaster.add(mnOperator);
		
		JMenu mnTransaksi = new JMenu("Transaksi");
		menuBar.add(mnTransaksi);
		
		JMenuItem mntmTransasksi = new JMenuItem("Transaski");
		mnTransaksi.add(mntmTransasksi);
		mntmTransasksi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Transaksi transaksi = new Transaksi();
				transaksi.setOperator(idOperator, namaOperator);
				transaksi.setVisible(true);
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	public void setIdOperator(String id) {
		this.idOperator = id;
	}
	
	public void setNamaOperator(String nama) {
		this.namaOperator = nama;
	}
}
