import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;
	private JFrame frame;
	
	private Koneksi konek = new Koneksi();
	private Connection con = konek.getConnection();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 40, 75, 20);
		contentPane.add(lblNewLabel);
		
		username = new JTextField();
		username.setBounds(95, 40, 175, 20);
		contentPane.add(username);
		username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPassword.setBounds(10, 70, 75, 20);
		contentPane.add(lblPassword);
		
		password = new JPasswordField();
		password.setBounds(95, 70, 175, 20);
		contentPane.add(password);
		password.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PreparedStatement ps;
				try {
					ps = con.prepareStatement("SELECT * FROM operator WHERE id_operator = ? AND password = md5(?)");
					ps.setString(1, username.getText());
					ps.setString(2, String.valueOf(password.getPassword()));
					ResultSet rs = ps.executeQuery();
					rs.last();
					if(rs.getRow()>0) {
						JOptionPane.showMessageDialog(null, "Login berhasil");
						frame = new Operator();
						frame.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Login gagal");
					}
				}catch(SQLException e) {
					e.printStackTrace();
					
				}
			}
		});
		btnNewButton.setBounds(90, 100, 100, 25);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Login");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 5, this.getWidth()-10, 20);
		contentPane.add(lblNewLabel_1);
	}
}
