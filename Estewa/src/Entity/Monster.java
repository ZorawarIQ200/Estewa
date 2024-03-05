package Entity;

import processing.core.PImage;

public abstract class Monster {
    public double positionX;
    public double positionY;
    int health;
    int xp;


    public Monster(double positionX, double positionY, int health, int xp) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.health = health;
        this.xp = xp;
    }

    // AttackPattern
    //public abstract void act();

    // Defeat
    public boolean isDefeated() {
        return health <= 0;
    }

    // Damage
    public void receiveDamage(int damage) {
        health -= damage;
        if (isDefeated()) {
            System.out.println("Monster defeated!");
        }
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public int getHealth() {
        return health;
    }
    public boolean goingRight = true;

    public void reduceHealth(int damage) {
        health -= damage;
    }




}