package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	final int originalTileSize = 16; //16x16 pixels
	final int scale = 3;
	
	final int tileSize = originalTileSize * scale; //48x48 pixels
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	
	int fps = 120;
	
	KeyStrokes keyH = new KeyStrokes();
	Thread GameThread;
	
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
			
	public GamePanel() {
	
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.magenta);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		
		
		
	}

	public void startGameThread() {
		GameThread = new Thread(this);
		GameThread.start();
	}
	@Override
	public void run() {
		
		double drawIntervals = 1000000000 / fps;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(GameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawIntervals;
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
			
		}
		
	}
	public void update () {
		if (keyH.downPressed == true) {
			playerY += playerSpeed;
		}
		if (keyH.upPressed == true) {
			playerY -= playerSpeed;
		}
		if (keyH.leftPressed == true) {
			playerX -= playerSpeed;
		}
		if (keyH.rightPressed == true) {
			playerX += playerSpeed;
		}
		
	}
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		
		g2.dispose();
	}
	
}

















