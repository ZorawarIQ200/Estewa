//import processing.core.*;
//
//public class Main extends PApplet {
//    int tileSize = 16;
//    int mapWidth = 800; // Width of the map image
//    int mapHeight = 800; // Height of the map image
//    PImage mapImage; // Declare a variable to hold the map image
//    double initX = 250;
//    double initY = 250;
//    double speed = 10;
//    boolean up = false, down = false, left = false, right = false;
//
//    public static void main(String[] args) {
//        PApplet.main("Main");
//    }
//
//    public void settings() {
//        size(800, 800);
//    }
//
//    public void setup() {
//        // Load the map image from the file path
//        mapImage = loadImage("Maps/map.png");
//    }
//
//    public void draw() {
//        background(0);
//        checkMovement();
//        // Draw the map image centered at the player's position
//        image(mapImage, (float) (width / 2 - initX), (float) (height / 2 - initY));
//    }
//
//
//
//

import processing.core.*;

public class Main extends PApplet {
    int tileSize = 16;
    int maxScreenSizeCol = 32;
    int maxScreenSizeRow = 32;
    double radius = 15;
    double initX = 250;
    double initY = 250;
    double speed = 10;
    boolean up = false, down = false, left = false, right = false;
    PImage mapImage; // Declare a variable to hold the map image

    PImage playerImage; // Declare a variable to hold the player image

    public static void main(String [] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(maxScreenSizeCol * tileSize, tileSize * maxScreenSizeRow);
    }

    public void setup() {
        // Load the player image from the file path
        // Load the map image from the file path

        playerImage = loadImage("Player/ez.png");

        playerImage.resize((int)radius * 2, (int)radius * 2); // Resize the image to match the circle's diameter
    }

    public void draw() {
        background(0);
        checkMovement();
        // Draw the map with respect to the player's position
        drawMap();
        // Draw the player image at the center of the screen
        image(playerImage, width/2 - (float)radius, height/2 - (float)radius);
    }

    public void keyPressed() {
        if (key == 'w') {
            up = true;
        } else if (key == 'a') {
            left = true;
        } else if (key == 'r') {
            down = true;
        } else if (key == 's') {
            right = true;
        }
    }

    public void keyReleased() {
        if (key == 'w') {
            up = false;
        } else if (key == 'a') {
            left = false;
        } else if (key == 'r') {
            down = false;
        } else if (key == 's') {
            right = false;
        }
    }

    long lastMoveTime = 0; // Variable to store the last time movement occurred
    int moveDelay = 100; // Delay in milliseconds

    public void checkMovement() {
        long currentTime = millis();

        if (currentTime - lastMoveTime > moveDelay) {
            if (up) {
                initY -= 32;

            } else if (left) {
                initX -= 32;

            } else if (down) {
                initY += 32;

            } else if (right) {
                initX += 32;

            }
            lastMoveTime = currentTime;
        }

        // Adjust boundaries to prevent the map from scrolling off-screen
        initX = constrain((float) initX, (float) radius, (float) ((maxScreenSizeCol + 1) * tileSize - radius));
        initY = constrain((float) initY, (float) radius, (float) ((maxScreenSizeRow + 1) * tileSize - radius));
    }


    // Function to draw the map based on the player's position
    public void drawMap() {
        float offsetX = (float) (width / 2 - initX);
        float offsetY = (float) (height / 2 - initY);
        translate(offsetX, offsetY);

        // Load the map image
        mapImage = loadImage("Maps/grass.png");

        // Draw the map tiles
        for (int i = 0; i <= tileSize * maxScreenSizeCol; i += tileSize) {
            for (int j = 0; j <= tileSize * maxScreenSizeRow; j += tileSize) {
                image(mapImage, i, j, tileSize, tileSize);
            }
        }

        // End of map drawing
        translate(-offsetX, -offsetY);
    }
}
