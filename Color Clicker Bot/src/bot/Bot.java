package bot;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Bot {
	Robot robot = new Robot();
	static JFrame frame = new JFrame("Color clicker bot");
	static JPanel panel = new JPanel();
	static JButton button = new JButton("Click to start the bot(Starts in 3 seconds)");
	static JLabel label = new JLabel("Place mouse in center of first circle once clicked.");

	public static void main(String[] args) throws AWTException {
		new Bot();
		panel.add(button);
		panel.add(label);
		frame.add(panel);
		frame.setSize(400, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				button.setEnabled(false);
				Thread t = new botThread();
				t.start();

			}
		});
	}

	public Bot() throws AWTException {
		//robot.setAutoDelay(10);
		robot.setAutoWaitForIdle(true);
	}
}

class botThread extends Thread {
	Bot bot;
	boolean resume = true;

	public void run() {
		try {
			bot = new Bot();
			Thread.sleep(3000);
			int mouseY = MouseInfo.getPointerInfo().getLocation().y;
			int mouseX = MouseInfo.getPointerInfo().getLocation().x;
			//System.out.println("Start");
			while (resume) {
				bot.robot.mouseMove(mouseX, mouseY);
				startEvent(mouseX, mouseY, bot);
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
		//System.out.println("Looking");
		Color color1 = bot.robot.getPixelColor(mouseX, mouseY);
		Color color2 = bot.robot.getPixelColor(mouseX + 200, mouseY);
		Color color3 = bot.robot.getPixelColor(mouseX + 400, mouseY);
		Color correctColor;
		if (color1.equals(color2)) {
			correctColor = color1;
			//System.out.println("Found color");
		} else if (color1.equals(color3)) {
			click(mouseX + 200, mouseY, bot);
			return;
		} else {
			click(mouseX, mouseY, bot);
			return;
		}
		int newX = mouseX;
		int newY = mouseY;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// bot.robot.mouseMove(newX, newY);
				if (!bot.robot.getPixelColor(newX, newY).equals(correctColor)) {
					click(newX, newY, bot);
					return;
				}
				newX += 200;
			}
			newX = mouseX;
			newY += 200;
		}
		//System.out.println("failed");
		resume = false;
		Bot.button.setEnabled(true);
	}

	public void click(int posX, int posY, Bot bot) {
		bot.robot.mouseMove(posX, posY);
		//System.out.println("Click!");
		bot.robot.mousePress(InputEvent.BUTTON1_MASK);
		//bot.robot.delay(10);
		bot.robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
}