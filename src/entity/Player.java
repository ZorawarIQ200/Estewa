package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.KeyStrokes;

public class Player extends Entity {

	GamePanel gp;
	KeyStrokes keyH;

	public Player(GamePanel gp, KeyStrokes keyH) {

		this.gp = gp;
		this.keyH = keyH;

		setDefaultValues();
		getPlayerImage();
	}

	public void setDefaultValues() {
		x = 100;
		y = 100;
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
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}

	public void update() {
		if (keyH.downPressed == true) {
			direction = "down";
			y += speed;
		}
		if (keyH.upPressed == true) {
			direction = "up";
			y -= speed;
		}
		if (keyH.leftPressed == true) {
			direction = "left";
			x -= speed;
		}
		if (keyH.rightPressed == true) {
			direction = "right";
			x += speed;
		}
	}
}
