package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.KeyStrokes;

public class Player extends Entity {

	GamePanel gp;
	KeyStrokes keyH;
	
	public final int screenX;
	public final int screenY;

	public Player(GamePanel gp, KeyStrokes keyH) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle(8, 16, 32, 32);

		setDefaultValues();
		getPlayerImage();
	}

	public void setDefaultValues() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";

	}

	public void getPlayerImage() {

		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/ez.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/ez.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/ez.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/ez.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		BufferedImage image = null;

		switch (direction) {
		case ("up"):
			image = up1;
			break;
		case ("down"):
			image = up1;
			break;
		case ("left"):
			image = up1;
			break;
		case ("right"):
			image = up1;
			break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}

	public void update() {
		if (keyH.downPressed == true) {
			direction = "down";
			worldY += speed;
		}
		if (keyH.upPressed == true) {
			direction = "up";
			worldY -= speed;
		}
		if (keyH.leftPressed == true) {
			direction = "left";
			worldX -= speed;
		}
		if (keyH.rightPressed == true) {
			direction = "right";
			worldX += speed;
		}
		
		collisionOn = true;
		gp.cChecker.checkTile(this);
		
		
		
		
	}
}
