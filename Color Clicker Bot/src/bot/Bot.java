package bot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Bot {
	Robot robot = new Robot(); // Does the clicking.
	static JFrame frame = new JFrame("Color clicker bot");
	static JPanel panel = new JPanel(new GridBagLayout());
	static GridBagConstraints c = new GridBagConstraints();
	static JButton button = new JButton("Click to start the bot(Starts in 3 seconds)"); // Press
																						// to
																						// initiate
																						// the
																						// bot.
	static JLabel label = new JLabel("Place mouse in center of first circle once clicked.");
	static String[] speedChoices = { "Very Fast", "Fast", "Medium", "Slow" }; // Options
																				// for
																				// the
																				// combo
																				// box.
	static JComboBox<String> options = new JComboBox<String>(speedChoices); // Allows
																			// change
																			// in
																			// speed.

	public static void main(String[] args) throws AWTException {
		new Bot();
		c.gridy = 0;
		panel.add(button, c);
		c.gridy = 1;
		panel.add(label, c);
		c.gridy = 2;
		panel.add(options, c); // Stuff added to content pane
		frame.add(panel);
		frame.setSize(400, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); // Everything display-wise set up.
		button.addActionListener(new ActionListener() { // Starts up new thread
														// on click. (See
														// botThread)
			@Override
			public void actionPerformed(ActionEvent arg0) {
				button.setEnabled(false);
				Thread t = new botThread();
				t.start();
			}
		});
	}

	public Bot() throws AWTException {
		robot.setAutoWaitForIdle(true); // Avoid conflicting actions.
	}
}

class botThread extends Thread {
	Bot bot;
	boolean resume = true;
	int delay = 0;

	public void run() { // Sets everything up to start playing.

		try {
			bot = new Bot();

			if (Bot.options.getSelectedIndex() == 0) { // Adjusts based on
														// selection.
				bot.robot.setAutoDelay(0);
				delay = 0;
			} else if (Bot.options.getSelectedIndex() == 1) {
				bot.robot.setAutoDelay(10);
				delay = 10;
			} else if (Bot.options.getSelectedIndex() == 2) {
				bot.robot.setAutoDelay(25);
				delay = 100;
			} else {
				bot.robot.setAutoDelay(50);
				delay = 200;
			}

			Thread.sleep(3000); // 3 second wait to set up.
			int mouseY = MouseInfo.getPointerInfo().getLocation().y;
			int mouseX = MouseInfo.getPointerInfo().getLocation().x; // Coordinates
																		// of
																		// first
																		// circle
																		// stored.
			// System.out.println("Start");
			while (resume) { // Runs until game is over.
				bot.robot.mouseMove(mouseX, mouseY); // Moves mouse back to
														// first circle.
				startEvent(mouseX, mouseY, bot); // Starts the scanning for
													// improper color.
			}
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startEvent(int mouseX, int mouseY, Bot bot) {
		// System.out.println("Looking");
		Color color1 = bot.robot.getPixelColor(mouseX, mouseY);
		Color color2 = bot.robot.getPixelColor(mouseX + 200, mouseY);
		Color color3 = bot.robot.getPixelColor(mouseX + 400, mouseY); // Finds
																		// colors
																		// of
													 					// the
																		// first
																		// 3
																		// circles.
		Color correctColor;
		if (color1.equals(color2)) { // 1 and 2 are the same; resume as normal.
			correctColor = color1;
			// System.out.println("Found color");
		} else if (color1.equals(color3)) { // 1 and 3 are the same; 2 is the
											// odd one out.
			click(mouseX + 200, mouseY, bot);
			return;
		} else { // 1 is different from 2 AND 3; it is the odd one out.
			click(mouseX, mouseY, bot);
			return;
		}
		int newX = mouseX;
		int newY = mouseY; // Temp values for mouse.
		for (int i = 0; i < 4; i++) { // Each row.
			for (int j = 0; j < 4; j++) { // Each circle in row.
				// bot.robot.mouseMove(newX, newY); //**(Uncomment this for
				// additional look and feel)**
				if (!bot.robot.getPixelColor(newX, newY).equals(correctColor)) { // If
																					// the
																					// color
																					// is
																					// wrong...
					click(newX, newY, bot); // Click on the incorrect color.
					return;
				}
				newX += 200; // Moves coordinates to next circle.
			}
			newX = mouseX; // Back to first circle in row.
			newY += 200; // Next row.
		}
		// System.out.println("failed");
		// If the code reaches this point, the game is over or something went
		// wrong.
		resume = false; // Stop scanning.
		Bot.button.setEnabled(true); // Button reactivated.
	}

	public void click(int posX, int posY, Bot bot) { // Moves mouse and clicks
														// it.
		bot.robot.mouseMove(posX, posY);
		// System.out.println("Click!");
		bot.robot.mousePress(InputEvent.BUTTON1_MASK);
		bot.robot.delay(delay);
		bot.robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
}