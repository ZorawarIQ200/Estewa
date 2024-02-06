package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyStrokes implements KeyListener {
	public boolean upPressed, downPressed, leftPressed, rightPressed;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			upPressed = true;
			leftPressed = false;
			rightPressed = false;
		}
		else if (code == KeyEvent.VK_A) {
			upPressed = false;
			downPressed = false;
			leftPressed = true;
		}
		else if (code == KeyEvent.VK_R) {
			downPressed = true;
			leftPressed = false;
			rightPressed = false;
		}
		else if (code == KeyEvent.VK_S) {
			upPressed = false;
			downPressed = false;
			leftPressed = false;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			upPressed = false;
		}
		else if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		else if (code == KeyEvent.VK_R) {
			downPressed = false;
		}
		else if (code == KeyEvent.VK_S) {
			rightPressed = false;
		}
	}

}
