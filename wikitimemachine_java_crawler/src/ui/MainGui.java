/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package ui;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.ApiCaller;
import util.IO;
import util.SqlUtil;
import util.StoreMethods;
import entity.Person;

/**
 * The main GUI class which has all crawler windows other than data base specific, which are in the DBSettings class.
 * 
 *
 */
public class MainGui {

	private static final String LAST_USED_FOLDER = "WIKITIMEMACHINE_LAST_USED_FOLDER";
	private JFrame frmWikitimemachineCrawlerV;
	private String path;
	private List<String> category;
	protected String category2;
	private JButton btnSaveAs;
	private JFormattedTextField formattedTextField;
	private JTextArea formattedTextField_1;
	private JButton btnNewButton;
	private JLabel lblCategory;
	private JRadioButton rdbtnCrawl;
	private JPanel panel;
	private ButtonGroup bg;
	private JRadioButton rdbtnLoad;
	private JMenuBar menuBar;
	private JMenu mnHelp;
	private JMenu mnFile;
	private JPanel panel_1;
	private JRadioButton rdbtnStorePersons;
	private JComboBox<?> langComboBox;
	private JRadioButton rdbtnDetermineDates;
	private List<Person> pList;
	private SqlUtil sq;
	private JRadioButton rdbtnStoreCategories;
	private JRadioButton rdbtnStoreConnections;
	private JRadioButton rdbtnComputePagerank;
	private JMenuItem mntmDatabase;

	/**
	 * Launch the application.
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
		addActionListeners();
		sq = new SqlUtil();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWikitimemachineCrawlerV = new JFrame();
		frmWikitimemachineCrawlerV.setTitle("WikiTimeMachine Crawler v 0.5");
		frmWikitimemachineCrawlerV.setBounds(100, 100, 519, 300);
		frmWikitimemachineCrawlerV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel_1 = new JPanel();
		panel_1.setBounds(8, 5, 264, 33);
		frmWikitimemachineCrawlerV.getContentPane().add(panel_1);
		btnSaveAs = new JButton("Save as...");
		panel_1.add(btnSaveAs);
		formattedTextField = new JFormattedTextField();
		formattedTextField.setColumns(20);
		panel_1.add(formattedTextField);

		bg = new ButtonGroup();

		panel = new JPanel();
		panel.setBounds(277, 5, 216, 159);
		frmWikitimemachineCrawlerV.getContentPane().add(panel);

		rdbtnCrawl = new JRadioButton("Crawl");

		rdbtnCrawl.setSelected(true);
		panel.add(rdbtnCrawl);
		bg.add(rdbtnCrawl);

		rdbtnLoad = new JRadioButton("Load File to Memory");
		panel.add(rdbtnLoad);
		bg.add(rdbtnLoad);

		rdbtnStorePersons = new JRadioButton("Store Persons");
		panel.add(rdbtnStorePersons);
		bg.add(rdbtnStorePersons);

		rdbtnStoreCategories = new JRadioButton("Store Categories");
		panel.add(rdbtnStoreCategories);
		bg.add(rdbtnStoreCategories);
		rdbtnStoreConnections = new JRadioButton("Store Connections");
		panel.add(rdbtnStoreConnections);
		bg.add(rdbtnStoreConnections);
		rdbtnComputePagerank = new JRadioButton("Compute Pagerank");
		panel.add(rdbtnComputePagerank);
		bg.add(rdbtnComputePagerank);
		rdbtnDetermineDates = new JRadioButton("Determine Dates");
		panel.add(rdbtnDetermineDates);
		bg.add(rdbtnDetermineDates);
		btnNewButton = new JButton("Run");

		btnNewButton.setBounds(8, 206, 418, 23);
		frmWikitimemachineCrawlerV.getContentPane().add(btnNewButton);

		formattedTextField_1 = new JTextArea();
		formattedTextField_1.setRows(5);
		formattedTextField_1.setBounds(101, 42, 171, 94);
		frmWikitimemachineCrawlerV.getContentPane().add(formattedTextField_1);

		lblCategory = new JLabel("Categories");
		lblCategory.setBounds(18, 45, 79, 14);
		frmWikitimemachineCrawlerV.getContentPane().add(lblCategory);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addMenu() {
		menuBar = new JMenuBar();
		frmWikitimemachineCrawlerV.setJMenuBar(menuBar);
		mnFile = new JMenu("Settings");
		menuBar.add(mnFile);

		mntmDatabase = new JMenuItem("Database");
		mntmDatabase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DbSettings dbS = new DbSettings();
				dbS.setVisible(true);
			}
		});

		mnFile.add(mntmDatabase);

		mnHelp = new JMenu("Help");
		mnHelp.addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent arg0) {
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
			}

			@Override
			public void menuSelected(MenuEvent arg0) {
				// for copying style
				JLabel label = new JLabel();
				Font font = label.getFont();

				// create some css from the label's font
				StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
				style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
				style.append("font-size:" + font.getSize() + "pt;");

				// html content
				final JEditorPane ep = new JEditorPane(
						"text/html",
						"<html><body style=\""
								+ style
								+ "\">" //
								+ "This interface is part of the COINS-Course 2014. For further information write an e-mail to<br/>"
								+ "<center><a href='mailto:praederp@smail.uni-koeln.de'>Peter Praeder</a><br/>"
								+ "<a href='mailto:mkoettin@smail.uni-koeln.de'>Michael Kötting</a><br/>"
								+ "<a href='mailto:a103555@smail.uni-koeln.de'>Vladimir Trajt</a>" //
								+ "</center></body></html>");

				// handle link events
				ep.addHyperlinkListener(new HyperlinkListener() {
					@Override
					public void hyperlinkUpdate(HyperlinkEvent e) {
						if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
							// ProcessHandler.launchUrl(e.getURL().toString());
							try {

								Desktop.getDesktop().mail(new URI(e.getURL().toString()));
								Window win = SwingUtilities.getWindowAncestor(ep);
								win.setVisible(false);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (URISyntaxException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
				ep.setEditable(false);
				ep.setBackground(label.getBackground());

				// show
				JOptionPane.showMessageDialog(null, ep);
				// JOptionPane
				// .showOptionDialog(
				// null,
				// "For further information write a mail to praederp@smail.uni-koeln.de, mkoettin@smail.uni-koeln.de or a103555@smail.uni-koeln.de",
				// "About.", JOptionPane.OK_OPTION,
				// JOptionPane.INFORMATION_MESSAGE, null,
				// new String[] { "OK" }, "OK");
			}
		});
		mnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		menuBar.add(mnHelp);
		frmWikitimemachineCrawlerV.getContentPane().setLayout(null);

		langComboBox = new JComboBox();
		langComboBox.setModel(new DefaultComboBoxModel(new String[] { "DE" }));
		langComboBox.setBounds(277, 175, 171, 20);
		frmWikitimemachineCrawlerV.getContentPane().add(langComboBox);
	}

	public void addActionListeners() {
		rdbtnCrawl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					btnSaveAs.setText("Save as...");
					lblCategory.setVisible(true);
					visibility = true;
					btnSaveAs.setVisible(true);
					formattedTextField.setVisible(true);

				} else {

				}
				formattedTextField_1.setVisible(visibility);
				lblCategory.setVisible(visibility);

			}
		});
		rdbtnLoad.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					btnSaveAs.setText("Open...");
					visibility = false;
					btnSaveAs.setVisible(true);
					formattedTextField.setVisible(true);
				}
				formattedTextField_1.setVisible(visibility);
				lblCategory.setVisible(visibility);

			}
		});
		rdbtnStorePersons.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setInvisible();
				}

			}

		});
		rdbtnStoreCategories.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setInvisible();
				}

			}

		});
		rdbtnStoreConnections.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setInvisible();
				}

			}

		});
		rdbtnComputePagerank.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setInvisible();
				}

			}

		});
		rdbtnDetermineDates.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setInvisible();
				}

			}

		});

		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					runPerformed();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnCrawl.isSelected()) {
					openOrSave(true);
				} else {
					openOrSave(false);
				}

			}
		});
	}

	private void setInvisible() {
		btnSaveAs.setVisible(false);
		formattedTextField.setVisible(false);
		formattedTextField_1.setVisible(false);
		lblCategory.setVisible(false);
	}

	boolean openOrSave(boolean save) {

		Preferences prefs = Preferences.userRoot().node(getClass().getName());
		JFileChooser chooser;

		if (path == null) {
			path = System.getProperty("user.home");
		}
		File file = new File(path.trim());

		chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER, new File(".").getAbsolutePath()));
		chooser.setDialogType(save ? JFileChooser.SAVE_DIALOG : JFileChooser.OPEN_DIALOG);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON FILES", "json");

		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

		chooser.setFileFilter(filter);
		chooser.setDialogTitle(save ? "Save as..." : "Open...");
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
		chooser = null;
		return false;
	}

	public void runPerformed() throws ClassNotFoundException, SQLException {
		if (rdbtnCrawl.isSelected()) {
			if (path == null) {
				return;
			}
			if (formattedTextField_1.getText() == null) {
				return;
			}
			category = Arrays.asList(formattedTextField_1.getText().split("\n"));
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
		} else if (rdbtnLoad.isSelected()) {
			if (path == null) {
				return;
			}
			frmWikitimemachineCrawlerV.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			IO io = new IO();
			pList = null;
			try {
				pList = io.readFromJsonFile(path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frmWikitimemachineCrawlerV.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			JOptionPane.showOptionDialog(null, "All persons loaded.", "Done!", JOptionPane.OK_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, new String[] { "OK" }, "OK");

		} else if (rdbtnStorePersons.isSelected()) {
			sq.store(pList, StoreMethods.Pages, langComboBox.getSelectedItem().toString());
		} else if (rdbtnStoreCategories.isSelected()) {
			sq.store(pList, StoreMethods.Categories, langComboBox.getSelectedItem().toString());
		} else if (rdbtnStoreConnections.isSelected()) {
			sq.store(pList, StoreMethods.Connections, langComboBox.getSelectedItem().toString());
		} else if (rdbtnComputePagerank.isSelected()) {
			sq.computePagerank(langComboBox.getSelectedItem().toString());
		} else if (rdbtnDetermineDates.isSelected()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					SqlUtil sq = new SqlUtil();
					try {
						sq.determineDates(langComboBox.getSelectedItem().toString());
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

		} else {
			System.out.println("Nothing defined");
		}

	}
}
