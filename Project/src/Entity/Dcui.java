package Entity;

import processing.core.PImage;

public class Dcui extends Monster {
    int attackDamage;
    PImage imgLeft;
    PImage imgRight;


    public Dcui(int coordinateX, int coordinateY, int attack, int health, int amountXP, PImage pL, PImage pR) {
        super(coordinateX, coordinateY, health, amountXP);
        this.attackDamage = attack;
        this.imgLeft = pL;
        this.imgRight = pR;
    }
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        //chaneg l8r?
    }
    public PImage getRight() {
        return imgRight;
    }
    public PImage getLeft() {
        return imgLeft;
    }
    public boolean inRange(Player player) {
        return ((player.playerX - getPositionX() < 480 || getPositionX() - player.playerX < 480)) && (player.playerY - getPositionY() < 480 || getPositionY() - player.playerY < 480);
    }


    public boolean isDead() {
        return this.health <= 0;
    }

    public int getAttackDamage() {
        return this.attackDamage;
    }
}