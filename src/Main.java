import processing.core.*;
import java.io.*;
import Entity.*;

public class Main extends PApplet {
    long lastMoveTime = 0; // Variable to store the last time movement occurred
    int moveDelay = 100; // Delay in milliseconds
    public int tileSize = 32;
    int maxScreenSizeCol = 32;
    int maxScreenSizeRow = 32;
    double initX = 256;
    double initY = 256;
    int speed = 32;
    boolean up = false, down = false, left = false, right = false;
    boolean goingRight = true;
    boolean playerGoingRight = true;
    int badGuyX = 192; // Initial x-coordinate of badGuy
    int[][] mapArray = new int[maxScreenSizeCol][maxScreenSizeRow]; //innit the aarray
    PImage grass, grass2, path, tree, wall, badGuyRight, badGuyLeft, playerLeft, playerRight; // Declare all the images
    Player player;
    SmallBoss smallBoss;

    public static void main(String [] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(maxScreenSizeCol * tileSize, tileSize * maxScreenSizeRow);
    }

    public void setup() {
        playerRight = loadImage("Player/playerRight.png");
        playerLeft = loadImage("Player/playerLeft.png");
//        wizard = loadImage();
        grass = loadImage("Maps/grass1.png");
        grass2 = loadImage("Maps/grass2.png");
        path = loadImage("Maps/pathVertical.png");
        tree = loadImage("Maps/angle.png");
        wall = loadImage("Maps/wall.png");
        badGuyRight = loadImage("Player/badGuyRight.png");
        badGuyLeft = loadImage("Player/badGuyLeft.png");
        player = new Player(initX, initY, 100);
        smallBoss = new SmallBoss(192, 192, 10, 50);

        // Initialize mapArray from the text file
        mapArray = loadMapFromFile("Map.txt");
        background(0,250,256);

    }

    // Method to load map from a text file
    private int[][] loadMapFromFile(String filename) {
        int[][] map = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int rowCount = 0;
            while ((line = reader.readLine()) != null) {
                // Print the contents of each line for debugging
                System.out.println("Line: " + line);

                String[] tokens = line.split(",\\s*"); // Split by comma followed by zero or more spaces
                if (map == null) {
                    map = new int[tokens.length][];
                }
                map[rowCount] = new int[tokens.length];
                for (int col = 0; col < tokens.length; col++) {
                    // Print each token for debugging
                    System.out.println("Token: " + tokens[col]);

                    map[rowCount][col] = Integer.parseInt(tokens[col]);
                }
                rowCount++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }




    public void draw() {
        long currentTime = millis();
        if (currentTime - lastMoveTime > moveDelay) {
            background(0);
            checkMovement();
            drawMap();
            updateEntity();
            drawHealthBar(player.life, 100);
            drawXPBar(100, 100);
//            image(playerRight, maxScreenSizeCol*tileSize/2, maxScreenSizeRow*tileSize/2);
            if (playerGoingRight) {
                image(playerRight, (float) (maxScreenSizeCol * tileSize) /2, (float) (maxScreenSizeRow * tileSize) /2);
            } else {
                image(playerLeft, (float) (maxScreenSizeCol * tileSize) /2, (float) (maxScreenSizeRow * tileSize) /2);
            }
            lastMoveTime = currentTime;
        }
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



    public void checkMovement() {
        if (up) {
            initY -= speed;
            player.move(initX, initY);
        } else if (left) {
            initX -= speed;
            playerGoingRight = false;
            player.move(initX, initY);
        } else if (down) {
            initY += speed;
            player.move(initX, initY);
        } else if (right) {
            initX += speed;
            player.move(initX, initY);
            playerGoingRight = true;
        }


        initX = constrain((float) initX, (float) tileSize, (float) ((maxScreenSizeCol-2) * tileSize));
        initY = constrain((float) initY, tileSize, (float) (((maxScreenSizeRow-2) * tileSize)));
    }

    public void drawMap() {
        float offsetX = (float) (width / 2 - initX);
        float offsetY = (float) (height / 2 - initY);
        translate(offsetX, offsetY);

        for (int i = 0; i < maxScreenSizeRow; i++) {
            for (int j = 0; j < maxScreenSizeCol; j++) {
                int tile = mapArray[i][j]; // Accessing the map array with swapped indices
                PImage tileImage = null; // Variable to hold the image for the current tile
                switch (tile) {
                    case 1:
                        tileImage = grass;
                        break;
                    case 2:
                        tileImage = grass2;
                        break;
                    case 3:
                        tileImage = tree;
                        break;
                    case 4:
                        tileImage = wall;
                        break;
                    case 5:
                        tileImage = path;
                    default:
                        // Handle any other cases
                        break;
                }
                if (tileImage != null) {
                    image(tileImage, j * tileSize, i * tileSize, tileSize, tileSize); // Swapping indices when drawing
                }
            }
        }
        translate(-offsetX, -offsetY);
    }


    int frameCount = 0;

    public void updateEntity() {
        float offsetX = (float) (width / 2 - initX);
        float offsetY = (float) (height / 2 - initY);
        translate(offsetX, offsetY);

        // Move the smallBoss
        if (goingRight) {
            smallBoss.positionX += 8;
            if (smallBoss.positionX >= maxScreenSizeCol * tileSize - 2 * tileSize) {
                goingRight = false;
                smallBoss.positionX -= 8;
            }
        } else {
            smallBoss.positionX -= 8;
            if (smallBoss.positionX <= 192) {
                goingRight = true;
                smallBoss.positionX = 192;
            }
        }

        // Check for collision with player
        if (player.checkCollision(smallBoss, tileSize)) {
            // Reduce player's health
            player.reduceHealth(5); // Assuming 5 health points deduction
        }

        // Draw the smallBoss image
        if (goingRight) {
            image(badGuyRight, (float) smallBoss.getPositionX(), (float) smallBoss.getPositionY(), tileSize, tileSize);
        } else {
            image(badGuyLeft, (float) smallBoss.getPositionX(), (float) smallBoss.getPositionY(), tileSize, tileSize);
        }
        translate(-offsetX, -offsetY);
    }

    public void drawHealthBar(int currentHealth, int maxHealth) {
        int barHeight = 20;
        float remainingWidth = (float) 2 * currentHealth;
        float posX = 50;
        float posY = 50;

        // Draw background bar
        fill(0, 0, 0, 200); // Gray color with some transparency
        rect(50, 50, 2 * maxHealth, barHeight);

        //Health Bar
        fill(255, 0, 0);
        rect(posX, posY, remainingWidth, barHeight-5);
    }



    public void drawXPBar(int currentXP, int maxXp) {
        int barHeight = 10;
        float remainingWidth = (float) 3 /2 * currentXP;
        float posX = 50;
        float posY = 75;


        // background bar
        fill(0, 0, 0, 200);
        rect(50, 75, (float) 3/2*maxXp, barHeight+5);

        // XP bar
        fill(255, 215, 0);
        rect(posX, posY, remainingWidth, barHeight);
    }




}
