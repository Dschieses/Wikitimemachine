package ui;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.UIManager;

import java.awt.Button;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.JButton;

import util.ApiCaller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFormattedTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.FlowLayout;

import javax.swing.JLabel;

public class MainGui {

	private static final String LAST_USED_FOLDER = "WIKITIMEMACHINE_LAST_USED_FOLDER";
	private JFrame frmWikitimemachineCrawlerV;
	private String path;
	private String category;
	private JFormattedTextField formattedTextField;
	private JButton btnSaveAs;
	private JFormattedTextField formattedTextField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						e.printStackTrace();
					}
					MainGui window = new MainGui();
					window.frmWikitimemachineCrawlerV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public MainGui() throws IOException {
		initialize();
		addMenu();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWikitimemachineCrawlerV = new JFrame();
		frmWikitimemachineCrawlerV.setTitle("WikiTimeMachine Crawler v 0.5");
		frmWikitimemachineCrawlerV.setBounds(100, 100, 450, 300);
		frmWikitimemachineCrawlerV
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		frmWikitimemachineCrawlerV.setJMenuBar(menuBar);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		frmWikitimemachineCrawlerV.getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(8, 5, 264, 33);
		frmWikitimemachineCrawlerV.getContentPane().add(panel_1);
		btnSaveAs = new JButton("Save as...");
		panel_1.add(btnSaveAs);
		formattedTextField = new JFormattedTextField();
		formattedTextField.setColumns(20);
		panel_1.add(formattedTextField);
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAs();
			}
		});

		ButtonGroup bg = new ButtonGroup();

		JPanel panel = new JPanel();
		panel.setBounds(277, 5, 149, 33);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		frmWikitimemachineCrawlerV.getContentPane().add(panel);

		final JRadioButton rdbtnCrawl = new JRadioButton("Crawl");
		rdbtnCrawl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					btnSaveAs.setText("Open...");
				} else {
					btnSaveAs.setText("Save as...");
				}

			}
		});
		rdbtnCrawl.setSelected(true);
		panel.add(rdbtnCrawl);
		bg.add(rdbtnCrawl);

		JRadioButton rdbtnReadDates = new JRadioButton("Read Dates");
		panel.add(rdbtnReadDates);
		bg.add(rdbtnReadDates);

		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (path == null) {
					return;
				}
				if (formattedTextField_1.getText() == null) {
					return;
				}
				category = formattedTextField_1.getText();
				runPerformed();

			}
		});
		btnNewButton.setBounds(8, 206, 418, 23);
		frmWikitimemachineCrawlerV.getContentPane().add(btnNewButton);

		formattedTextField_1 = new JFormattedTextField();
		formattedTextField_1.setBounds(101, 42, 171, 20);
		frmWikitimemachineCrawlerV.getContentPane().add(formattedTextField_1);

		JLabel lblCategory = new JLabel("Category");
		lblCategory.setBounds(18, 45, 46, 14);
		frmWikitimemachineCrawlerV.getContentPane().add(lblCategory);

	}

	boolean saveAs() {

		Preferences prefs = Preferences.userRoot().node(getClass().getName());
		JFileChooser chooser;

		if (path == null)
			path = System.getProperty("user.home");
		File file = new File(path.trim());

		chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER,
				new File(".").getAbsolutePath()));
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JSON FILES", "json");

		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Save as...");
		chooser.setVisible(true);

		int result = chooser.showSaveDialog(frmWikitimemachineCrawlerV);

		if (result == JFileChooser.APPROVE_OPTION) {

			path = chooser.getSelectedFile().toString();
			file = new File(path);
			if (!filter.accept(file)) {
				path += ".json";
			}
			prefs.put(LAST_USED_FOLDER, file.getParent());
			chooser.setVisible(false);
			formattedTextField.setText(path);
			return true;
		}
		chooser.setVisible(false);
		return false;
	}

	public void runPerformed() {
		ApiCaller api;
		try {
			api = new ApiCaller(path, category);
			api.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
