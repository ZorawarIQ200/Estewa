package Entity;
import java.util.ArrayList;


public class Player {

    public double playerX;
    public double playerY;
    public int life;
    private int xpCount;
    private ArrayList<String> inventory;
    int attack = 10;
    boolean fPressed;
    public boolean equiped;


    public Player(double coordsX, double coordsY, int totalLife) {
        this.playerX = coordsX;
        this.playerY = coordsY;
        this.life = totalLife;
        this.xpCount = 0;
        this.inventory = new ArrayList<>();
    }

    public void move(double x, double y) {
        // Implement movement logic here
        this.playerX = x;
        this.playerY  = y;
    }

    public void reduceHealth(int damage) {
        life -= damage;
        if (life <= 0) {
            life = 0;
            ifDead();
        }
    }

    // Method to check collision between two entities
    public boolean checkCollision(SmallBoss smallBoss, int tileSize) {

        // Calculate hitbox for player
        float playerLeft = (float) playerX;
        float playerRight = playerLeft + tileSize;
        float playerTop = (float) playerY;
        float playerBottom = playerTop + tileSize;

        // Calculate hitbox for smallBoss
        float bossLeft = (float) smallBoss.getPositionX();
        float bossRight = bossLeft + tileSize;
        float bossTop = (float) smallBoss.getPositionY();
        float bossBottom = bossTop + tileSize;

        // Check for intersection


        return playerRight >= bossLeft && playerLeft <= bossRight && playerBottom >= bossTop && playerTop <= bossBottom;
    }

    public void pressedF(boolean mainFPressed) {
        this.fPressed = mainFPressed;
    }

    public boolean getFResult() {
        return fPressed;
    }

    public int getAttack(){
        return attack;
    }

    public void collectItem(String item) {
        // Implement item collection logic here
        this.inventory.add(item);
    }

    public void gainXP(int xp) {
        // Increase XP count by the given amount
        this.xpCount += xp;
        if (this.xpCount >= 10 && this.xpCount < 25) {
            this.levelUp();
        } else if (this.xpCount >= 25 && this.xpCount < 50) {
            this.levelUp();
        } else if (this.xpCount >= 50 && this.xpCount < 100) {
            this.levelUp();
        } else if (this.xpCount >= 100 && this.xpCount < 200) {
            this.levelUp();
        } else if (this.xpCount >= 200) {
            this.levelUp();
        }
    }

    private void levelUp() {
        // Implement logic l8r
        this.life += 10;
        this.xpCount = 0;
        this.attack += 10;
    }


    public boolean isDead() {
        return this.life <= 0;
    }

    public void ifDead() {
        // Implement logic for what happens when the player die

    }
    public ArrayList<String> setInventory(String s) {
        inventory.add(s);
        return this.inventory;

    }

    public ArrayList<String> getInventory(String sword) {
        return this.inventory;
    }

}