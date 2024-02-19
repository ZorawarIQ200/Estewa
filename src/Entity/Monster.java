package Entity;

public abstract class Monster {
    public double positionX;
    double positionY;
    int health;

    public Monster(double positionX, double positionY, int health) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.health = health;
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
}