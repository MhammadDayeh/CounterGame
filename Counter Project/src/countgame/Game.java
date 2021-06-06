package countgame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Game implements ActionListener {
	private JFrame gameFrame;
	private JPanel gamePanel, statusPanel;
	private ArrayList<JButton> buttons;
	private ArrayList<Integer> randomNumbers;
	private JLabel timerLabel, result, chancesLabel, diffecultyLabelStatus;
	private Timer timer;
	private volatile boolean stopTimer = true;
	private static Random generator;
	private static int chances = 3;
	private Player player; 
	private static File file;
	private static FileOutputStream fout;
	private static ObjectOutputStream oOut;

	Game() {
		// ====== file ======
		file = new File("playerScore.txt");
		
		// ====== initiating streams ======
		InitiateStreams();

		// ====== player ======
		player = new Player();

		// ====== timer =====
		timer = new Timer();

		// ====== random ======
		generator = new Random();

		// ====== randomNumbers ======
		randomNumbers = returnRandomNumberArray();

		// ====== buttons ======
		buttons = new ArrayList<JButton>(16);
		for (int i = 0; i < 16; i++) {
			buttons.add(new JButton(Integer.toString(randomNumbers.get(i))));
		}

		for (JButton button : buttons) { // adding actionListeners and font to buttons
			button.addActionListener(this); 
			button.setFont(GUI.font);
			button.setBackground(Color.WHITE);
		}

		// ====== gameFrame ======
		gameFrame = new JFrame("Game");
		gameFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		gameFrame.setLayout(null);
		gameFrame.getContentPane().setBackground(Color.GRAY);
		gameFrame.setSize(500, 555);
		gameFrame.setResizable(false);

		// ====== gamePanel ======
		gamePanel = new JPanel();
		gamePanel.setBounds(10, 10, 465, 340);
		gamePanel.setLayout(new GridLayout(4, 4));
		gamePanel.setBackground(Color.GRAY);
		gameFrame.add(gamePanel);

		// ====== statusPanel ======
		statusPanel = new JPanel();
		statusPanel.setBounds(10, 355, 465, 150);
		statusPanel.setBackground(Color.CYAN);
		statusPanel.setLayout(new BorderLayout());
		gameFrame.add(statusPanel);

		// ====== timerLabel ======
		timerLabel = new JLabel("Timer: 0:0:0");
		timerLabel.setFont(new Font("Lucida Console", 3, 25));
		statusPanel.add(timerLabel, BorderLayout.NORTH);

		// ====== result label ======
		result = new JLabel();
		result.setFont(new Font("Lucida Console", 3, 25));
		statusPanel.add(result, BorderLayout.SOUTH);
			
		// ====== chancesLabel ======
		chancesLabel = new JLabel("Chances: " + Integer.toString(chances));
		chancesLabel.setFont(new Font("Lucida Console", 3, 25));
		statusPanel.add(chancesLabel, BorderLayout.CENTER);
		
		determineDiffeculty();
		diffecultyLabelStatus.setFont(GUI.font);
		statusPanel.add(diffecultyLabelStatus, BorderLayout.EAST);

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					timer.startTimer();
					while (!stopTimer) {
						timerLabel.setText("Timer: "
							+ String.valueOf(timer.getTime()[0]) + ":"
							+ String.valueOf(timer.getTime()[1]) + ":"
							+ String.valueOf(timer.getTime()[2])
						);

						if (diffecultyLabelStatus.getText().equals("Meduim")) {
							if (timer.getTime()[1] == 1) {
								result.setText("Time Expired LOST");
								stopTimer = true;
								GUI.score.setText("Time Expired LOST");
								player.setScore(GUI.score.getText());
								writePlayerObject();
							}
						} else if (diffecultyLabelStatus.getText().equals("Hard")) {
							if (timer.getTime()[2] == 30) {
								result.setText("Time Expired LOST");
								stopTimer = true;
								GUI.score.setText("Time Expired LOST");
								player.setScore(GUI.score.getText());
								writePlayerObject();
							}
						} else {
							continue;
						}
					}
					timer.stopTimer();
				}
			}
		}).start(); // make new thread for timer and start() it		

		for (int i = 0; i < 16; i++) { // adding buttons to gamePanel
			gamePanel.add(buttons.get(i));
		}
		
		gameFrame.setVisible(true); // setting gameFrame visible
	}

	private static ArrayList<Integer> returnRandomNumberArray() {
		ArrayList<Integer> result = new ArrayList<Integer>(16);
		result.add(generator.nextInt(16) + 1);
		
		ArrayList<Integer> used = new ArrayList<Integer>(16);
		used.add(result.get(0));
		
		int checkedTimes = 0;
		while (true) {
			boolean passed = true;
			
			int rand = generator.nextInt(16) + 1;
			for (int i = 0; i < used.size(); i++) {
				if (rand == used.get(i)) passed = false;
				System.out.println("\u001b[32m" + "CHECKED" + i + "\u001b[0m");
				if (passed) 
					System.out.println("\u001b[32m" + "passed" + "\u001b[0m");
				else
					System.out.println("\u001b[31m" + "not passed" + "\u001b[0m");
			}

			if (passed) {
				result.add(rand);
				used.add(rand);
			}

			if (result.size() == 16) {
				System.out.println("Broke at used:" + used.size());
				System.out.println("Broke at result:" + result.size());
				System.out.println("Checked " + checkedTimes + " times.");
				break; 
			} else {
				checkedTimes++;
				continue;
			}
		}
		
		return result;
	}
	
	ArrayList<Integer> userInput = new ArrayList<Integer>(16);
	
	public void actionPerformed(ActionEvent ae) {
		stopTimer = false;	
		JButton pressedButton = null;
		
		for (int i = 0; i < buttons.size(); i++) {
			if (ae.getActionCommand() == buttons.get(i).getText()) {
				pressedButton = buttons.get(i);
				break;
			}
		}	
		
		if (Integer.parseInt(pressedButton.getText()) == 1) {
				pressedButton.setBackground(Color.GREEN);
			userInput.add(Integer.parseInt(pressedButton.getText()));
		} else if (userInput.contains(Integer.parseInt(pressedButton.getText()) - 1)) {
			pressedButton.setBackground(Color.GREEN);
			userInput.add(Integer.parseInt(pressedButton.getText()));
		} else {
			pressedButton.setBackground(Color.RED);
			chances--;
			chancesLabel.setText("Chances: " + String.valueOf(chances));
		}

		if (chances == 0) { 
			result.setText("YOU LOST");
			stopTimer = true;
			GUI.score.setText("LOST");

			player.setScore(GUI.score.getText());
			if (GUI.username.getText().isEmpty()) {
				player.setName("No Name");
			} else {
				player.setName(GUI.username.getText());
			}
			System.out.println(player);
			
			writePlayerObject();
		}

		if (userInput.size() == 16) {
			result.setText("YOU WON");
			stopTimer = true;
			GUI.score.setText("WON " + timerLabel.getText());
			
			player.setScore(GUI.score.getText());
			if (GUI.username.getText().isEmpty()) {
				player.setName("No Name");
			} else {
				player.setName(GUI.username.getText());
			}
			System.out.println(player);
						
			writePlayerObject();
		}
	}
	
	public void InitiateStreams() {
		try {	
			fout = new FileOutputStream(file);
			oOut = new ObjectOutputStream(fout);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void determineDiffeculty() {
		switch ((String) GUI.diffeculty.getSelectedItem()) {
			case "Easy": diffecultyLabelStatus = new JLabel("Easy"); break;
			case "Meduim": diffecultyLabelStatus = new JLabel("Meduim"); break;
			case "Hard": diffecultyLabelStatus = new JLabel("Hard"); break;
		}
	}

	public void writePlayerObject() {
		try {
			oOut.writeObject(player);
			oOut.close();
			fout.close();
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

}
