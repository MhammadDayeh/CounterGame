// Fix the txtfield looking weird.
package countgame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.File;

public class GUI {
	public static Font font;
	private JFrame mainFrame;
	private JPanel titlePanel, instructionsPanel, informationPanel;
	private JLabel title, name, Lastscore, diffecultyLabel; 
	public static JLabel score;
	private JButton playButton, quitButton;
	private JTextArea txtarea;
	public static JTextField username;
	private GridBagConstraints gbc;
	private static ObjectInputStream oin;
	private static FileInputStream fin; 
	private static File file;
	public static JComboBox<String> diffeculty; 

	GUI() {
		// ====== file ======
		file = new File("playerScore.txt");

		//====== initiateStreams() ======
		initiateStreams();
		
		// ====== font settings ======
		font = new Font("Lucida Console", Font.BOLD, 40);		
		
		// ====== mainFrame ======
		mainFrame = new JFrame("Count Game");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(500, 300);
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);
		mainFrame.getContentPane().setBackground(Color.GRAY);

		// ====== gbc =======
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 1, 0, 0); 
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.weightx = 0.1;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// ====== informationPanel ======
		informationPanel = new JPanel();
		informationPanel.setBackground(Color.GRAY);
		informationPanel.setLayout(new GridBagLayout());
		informationPanel.setBounds(270, 69, 200, 145);
		
		// ====== diffeculty jlabel ======
		diffecultyLabel = new JLabel("Diffecutly: ");
		gbc.gridx = 0;
		gbc.gridy = 2;
		informationPanel.add(diffecultyLabel, gbc);

		// ====== diffeculty ======
		final String[] options = {"Easy", "Meduim", "Hard"};
		diffeculty = new JComboBox<>(options); 
		diffeculty.setPreferredSize(new Dimension(2, 10));
		gbc.gridx = 2;
		gbc.gridy = 2; 
		gbc.weightx = 1.0;
		gbc.weightx = 1.0; 
		informationPanel.add(diffeculty, gbc);

		// ====== name ======
		name = new JLabel("Name: ", JLabel.LEFT);
		gbc.gridx = 0;
		gbc.gridy = 0;
		informationPanel.add(name, gbc);
		
		// ====== username txtfield ======
		username = new JTextField(30);
 		username.setFont(new Font("Consolas", Font.BOLD, 12));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 5;
		informationPanel.add(username, gbc);
		
		// ====== score ======
		score = new JLabel();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 100;
		informationPanel.add(score, gbc);
		
		try {
			Player player = (Player) oin.readObject();
			username.setText(player.getName());
			score.setText(player.getScore());
		} catch (EOFException ex) {
			System.out.println(ex);
		} catch (ClassNotFoundException | IOException ex) {
			System.out.println(ex);
		}

		// ====== Lastscore ======
		Lastscore = new JLabel("Score: ", JLabel.LEFT);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 0;
		informationPanel.add(Lastscore, gbc);
		
		mainFrame.add(informationPanel); // add informationPanel to mainFrame
		
		// ====== JLable title =======	
		title = new JLabel("Counting Game");
		title.setFont(font);
		
		// ====== JPanel title =======
		titlePanel = new JPanel();
		titlePanel.setBackground(Color.RED);
		titlePanel.setBounds(10, 10, 464, 50);
		titlePanel.add(title);
		mainFrame.add(titlePanel);
		
		// ====== playButton ======
		playButton = new JButton("PLAY");
		playButton.setForeground(Color.GREEN);
		playButton.setBackground(Color.WHITE);
		playButton.setBounds(140, 215, 200, 40);
		mainFrame.add(playButton);

		// ====== quitButton ======
		quitButton = new JButton("Quit");
		quitButton.setForeground(Color.RED);
		quitButton.setBounds(370, 215, 100, 40);
		quitButton.setBackground(Color.WHITE);
		mainFrame.add(quitButton);
		
		// ====== ActionListener for quitButton ======
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});

		// ====== ActionListener for playButton ======
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (username.getText().isEmpty()) { //replace it with JOPTIONPANE ERROR PAGE
					System.out.println("ERROR");
					return;
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new Game();
					}
				});
			}
		});
		
		// ====== String instrustions ======
		final String playingInstructions = "You will have a grid of 4 x 4 containing buttons with numbers from 1 to 16. You have to press the buttons in order. Easy option will give you unlimited time to complete the task, normal will give you a mintue, hard will give you 30 seconds.";
	
		// ====== JTextArea ======
		txtarea = new JTextArea();
		txtarea.setSize(240, 100);
		txtarea.setBackground(Color.GRAY);
		txtarea.setLineWrap(true);
		txtarea.setWrapStyleWord(true);
		txtarea.setText(playingInstructions);
		
		// ====== JPanel with title for how to play ======
		instructionsPanel = new JPanel();
		instructionsPanel.setBackground(Color.GRAY);
		instructionsPanel.setBorder(BorderFactory.createTitledBorder("How to play"));
		instructionsPanel.setBounds(10, 60, 250, 155);
		instructionsPanel.setLayout(new FlowLayout());
		instructionsPanel.add(txtarea);
		mainFrame.add(instructionsPanel);
		
		mainFrame.setVisible(true);
	}

	public void initiateStreams() {
		try {	
			fin = new FileInputStream(file);	
			oin = new ObjectInputStream(fin);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
}
