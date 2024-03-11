import Entity.*;

public class LostGrace {

    double positionX;
    double positionY;
    public LostGrace(double coordinateX, double coordinateY) {
        this.positionX = coordinateX;
        this.positionY = coordinateY;
    }
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    // update the revive position of the player
    public void positionUpdate(Player player) {
        player.playerX = positionX;
        player.playerY = positionY;
        player.life = 100;
    }

    // all resourced the player have when they get into the checkpoint
    public void revive(Player player){
        while(player.isDead()) {
            positionUpdate(player);
        }
    }

    public boolean isAtPosition(double x, double y) {
        return this.positionX == x && this.positionY == y;
    }

}