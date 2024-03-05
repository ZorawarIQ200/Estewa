package Zowawa;
import Entity.*;

public class LostGrace {

    double positionX;
    double positionY;
    public LostGrace(double coordinateX, double coordinateY) {
        this.positionX = coordinateX;
        this.positionY = coordinateY;
    }

    // update the revive position of the player
    public void positionUpdate(Player player) {
        player.playerX = positionX;
        player.playerY = positionY;
        player.life = 100;
    }

    // all resourced the player have when they get into the checkpoint
    public void revive(Player player){
//        resourceStore();
        while(player.isDead()) {
            positionUpdate(player);
        }
    }

}
