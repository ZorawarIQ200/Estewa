package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	final int originalTileSize = 16; // 16x16 pixels
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48x48 pixels
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	
	//WORLD SETTINGS:
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;

	int fps = 120;

	TileManager tileM = new TileManager(this);
	KeyStrokes keyH = new KeyStrokes();
	Thread GameThread;
	public Collision cChecker = new Collision(this);
	public Player player = new Player(this, keyH);


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
		long timer = 0;
		int drawCount = 0;

		while (GameThread != null) {
			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawIntervals;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			if (timer >= 1000000000) {
//				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;

			}
		}

	}

	public void update() {
		player.update();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		tileM.draw(g2);
		player.draw(g2);

		g2.dispose();
	}

}
