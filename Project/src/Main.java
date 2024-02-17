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
    public boolean goingRight = true;
    int badGuyX = 192; // Initial x-coordinate of badGuy
    PImage mapImage; // Declare a variable to hold the map image
    PImage mapImage2;
    PImage BadGuy1;
    PImage badGuy2;


    PImage playerImage; // Declare a variable to hold the player image

//    public Player player = new Player((int)initX, (int)initY, 100);

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

    }

    public void draw() {
        background(0);
        checkMovement();
        // Draw the map with respect to the player's position
        drawMap();
        updateEntity();
        // Draw the player image at the center of the screen
        image(playerImage,262, 262);
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
                initY -= 16;

            } else if (left) {
                initX -= 16;

            } else if (down) {
                initY += 16;

            } else if (right) {
                initX += 16;
            }
            lastMoveTime = currentTime;
        }

        // Adjust boundaries to prevent the map from scrolling off-screen
        initX = constrain((float) initX,-6, (float) ((maxScreenSizeCol + 1) * tileSize - radius));
        initY = constrain((float) initY,-6, (float) (((maxScreenSizeRow + 1) * tileSize - radius))*2);
    }


    // Function to draw the map based on the player's position
    public void drawMap() {
        float offsetX = (float) (width / 2 - initX);
        float offsetY = (float) (height / 2 - initY);
        translate(offsetX, offsetY);

        // Load the map image
        mapImage = loadImage("Maps/grass1.png");
        mapImage2 = loadImage("maps/grass2.png");


        // Draw the map tiles
        for (int i = 0; i <= tileSize * maxScreenSizeCol; i += tileSize) {
            for (int j = 0; j <= tileSize * maxScreenSizeRow*2; j += tileSize) {
                if (i % 64 == 0 && j % 64 ==0) {
                    image(mapImage, i, j, tileSize, tileSize);
                } else {
                    image(mapImage2, i, j, tileSize, tileSize);
                }
            }
        }


        // End of map drawing
        translate(-offsetX, -offsetY);
    }

    int frameCount = 0;


    public void updateEntity() {
        float offsetX = (float) (width / 2 - initX);
        float offsetY = (float) (height / 2 - initY);
        translate(offsetX, offsetY);

        badGuy2 = loadImage("Player/badGuyRight.png");
        BadGuy1 = loadImage("Player/badGuyLeft.png");

        // Increment frame count
        frameCount++;


        // Move badGuy every 12 frames
        if (frameCount % 5 == 0) {
            // Move the badGuy
            if (goingRight) {
                // Move the badGuy to the right
                badGuyX += tileSize;
                // Check if badGuy exceeds the screen boundary
                if (badGuyX >= maxScreenSizeCol * tileSize - 2*tileSize) {
                    // Set badGuy to move left and adjust position to stay within the boundary
                    goingRight = false;
                    badGuyX -= tileSize;
                }
            }
            if (!goingRight) {
                // Move the badGuy to the left
                badGuyX -= tileSize;
                // Check if badGuy reaches the leftmost position
                if (badGuyX <= 192) {
                    // Set badGuy to move right
                    goingRight = true;
                    badGuyX = 192; // Reset to initial position
                }
            }
        }

        // Draw the badGuy image
        if (goingRight) {
            image(badGuy2, badGuyX, 192, tileSize, tileSize);
        } else {
            image(BadGuy1, badGuyX, 192, tileSize, tileSize);
        }
        translate(-offsetX, -offsetY);
    }

}
