/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package ui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import util.DbConnectionStrings;
import util.DbConnector;

/**
 * This class represents a GUI for a database connection.
 * 
 *
 */
public class DbSettings extends JFrame {

	/**
	 * a GUI field
	 */
	private JPanel contentPane;

	/**
	 * a GUI field
	 */
	private JTextField txtServer;

	/**
	 * a GUI field
	 */
	private JTextField txtPort;

	/**
	 * a GUI field
	 */
	private JTextField txtDatabase;

	/**
	 * a GUI field
	 */
	private JTextField txtUser;

	/**
	 * a GUI field
	 */
	private JPasswordField pwdPassword;

	/**
	 * The main launch method
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						e.printStackTrace();
					}
					DbSettings frame = new DbSettings();
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
	public DbSettings() {
		setTitle("Database Settings");

		setBounds(100, 100, 274, 232);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblServer = new JLabel("Server");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblServer, gbc_lblNewLabel);

		txtServer = new JTextField();
		GridBagConstraints gbc_txtServer = new GridBagConstraints();
		gbc_txtServer.insets = new Insets(0, 0, 5, 0);
		gbc_txtServer.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtServer.gridx = 1;
		gbc_txtServer.gridy = 0;
		txtServer.setText(DbConnectionStrings.getInstance().getHost());
		contentPane.add(txtServer, gbc_txtServer);
		txtServer.setColumns(10);

		JLabel lblPort = new JLabel("Port");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		contentPane.add(lblPort, gbc_lblNewLabel_1);

		txtPort = new JTextField();
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.insets = new Insets(0, 0, 5, 0);
		gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPort.gridx = 1;
		gbc_txtPort.gridy = 1;
		txtPort.setText(DbConnectionStrings.getInstance().getPort());
		contentPane.add(txtPort, gbc_txtPort);
		txtPort.setColumns(10);

		JLabel lblDatabase = new JLabel("Database");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		contentPane.add(lblDatabase, gbc_lblNewLabel_2);

		txtDatabase = new JTextField();
		GridBagConstraints gbc_txtDatabase = new GridBagConstraints();
		gbc_txtDatabase.insets = new Insets(0, 0, 5, 0);
		gbc_txtDatabase.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDatabase.gridx = 1;
		gbc_txtDatabase.gridy = 2;
		txtDatabase.setText(DbConnectionStrings.getInstance().getDb());
		contentPane.add(txtDatabase, gbc_txtDatabase);
		txtDatabase.setColumns(10);

		JLabel lblUser = new JLabel("User");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		contentPane.add(lblUser, gbc_lblNewLabel_3);

		txtUser = new JTextField();
		GridBagConstraints gbc_txtUser = new GridBagConstraints();
		gbc_txtUser.insets = new Insets(0, 0, 5, 0);
		gbc_txtUser.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUser.gridx = 1;
		gbc_txtUser.gridy = 3;
		txtUser.setText(DbConnectionStrings.getInstance().getDbUsr());
		contentPane.add(txtUser, gbc_txtUser);
		txtUser.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 4;
		contentPane.add(lblPassword, gbc_lblNewLabel_4);

		pwdPassword = new JPasswordField();
		GridBagConstraints gbc_pwdPassword = new GridBagConstraints();
		gbc_pwdPassword.insets = new Insets(0, 0, 5, 0);
		gbc_pwdPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdPassword.gridx = 1;
		gbc_pwdPassword.gridy = 4;
		pwdPassword.setText(DbConnectionStrings.getInstance().getDbPwd());
		contentPane.add(pwdPassword, gbc_pwdPassword);

		JButton btnTest = new JButton("Test and save");
		btnTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
					DbConnector db = new DbConnector();
					JOptionPane.showOptionDialog(null, "Connected successfully.", "Success!", JOptionPane.OK_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, new String[] { "OK" }, "OK");

				} catch (ClassNotFoundException | SQLException e1) {
					JOptionPane.showMessageDialog(null, "Can't connect to database.");
					e1.printStackTrace();
				}

			}

		});
		GridBagConstraints gbc_btnTest = new GridBagConstraints();
		gbc_btnTest.insets = new Insets(0, 0, 5, 0);
		gbc_btnTest.gridx = 1;
		gbc_btnTest.gridy = 5;
		contentPane.add(btnTest, gbc_btnTest);
		this.pack();
	}
/**
 * the method saves the database settings into the databse settings object
 */
	private void save() {
		DbConnectionStrings.getInstance().setDb(txtDatabase.getText());
		DbConnectionStrings.getInstance().setDbPwd(String.copyValueOf(pwdPassword.getPassword()));
		DbConnectionStrings.getInstance().setDbUsr(txtUser.getText());
		DbConnectionStrings.getInstance().setHost(txtServer.getText());
		DbConnectionStrings.getInstance().setPort(txtPort.getText());
	}

}
