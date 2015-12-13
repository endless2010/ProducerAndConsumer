import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MyTimer implements ActionListener {
	private int seconds = 0;

	private Timer timer;

	public MyTimer() {
		timer = new Timer(1000, this);
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	public void restart() {
		timer.restart();
	}

	public void actionPerformed(ActionEvent e) {
		seconds++;
	}

	public int getMinutes() {
		return seconds / 60;
	}

	public int getSeconds() {
		return seconds % 60;
	}
}