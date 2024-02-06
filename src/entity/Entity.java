package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

	public int worldX, worldY, speed;
	public BufferedImage up1,left1,right1,down1;
	public String direction;
	public Rectangle solidArea;
	public Boolean collisionOn = false;
}
