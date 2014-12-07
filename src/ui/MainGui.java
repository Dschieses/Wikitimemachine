package ui;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.UIManager;

import java.awt.GridBagLayout;

import javax.swing.JRadioButton;

import com.google.gson.Gson;

import entity.Person;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainGui {

	private JFrame frmWikitimemachineCrawlerV;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						  UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
						} catch( Exception e ) { e.printStackTrace(); }
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
	 * @throws IOException 
	 */
	public MainGui() throws IOException {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ButtonGroup bg = new ButtonGroup();
	
		frmWikitimemachineCrawlerV = new JFrame();
		frmWikitimemachineCrawlerV.setTitle("WikiTimeMachine Crawler v 0.5");
		frmWikitimemachineCrawlerV.setBounds(100, 100, 450, 300);
		frmWikitimemachineCrawlerV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmWikitimemachineCrawlerV.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		frmWikitimemachineCrawlerV.getContentPane().setLayout(gridBagLayout);
		
		JRadioButton rdbtnCrawl = new JRadioButton("Crawl");
		GridBagConstraints gbc_rdbtnCrawl = new GridBagConstraints();
		gbc_rdbtnCrawl.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnCrawl.gridx = 0;
		gbc_rdbtnCrawl.gridy = 0;
		frmWikitimemachineCrawlerV.getContentPane().add(rdbtnCrawl, gbc_rdbtnCrawl);
		bg.add(rdbtnCrawl);
		
		JRadioButton rdbtnProcessData = new JRadioButton("Process Data");
		GridBagConstraints gbc_rdbtnProcessData = new GridBagConstraints();
		gbc_rdbtnProcessData.gridx = 1;
		gbc_rdbtnProcessData.gridy = 0;
		frmWikitimemachineCrawlerV.getContentPane().add(rdbtnProcessData, gbc_rdbtnProcessData);
		
		bg.add(rdbtnProcessData);
	}

}
