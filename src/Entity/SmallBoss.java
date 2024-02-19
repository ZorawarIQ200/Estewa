package Entity;

public class SmallBoss extends Monster {
    int attackDamage;

    public SmallBoss(int coordinateX, int coordinateY, int attack, int amountXP) {
        super(coordinateX, coordinateY, amountXP);
        this.attackDamage = attack;
    }
    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        //chaneg l8r?
    }

}