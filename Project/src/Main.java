import processing.core.*;
import java.io.*;
import java.util.ArrayList;

import Entity.*;

public class Main extends PApplet {
    boolean gameStarted = false;
    long lastMoveTime = 0; // Variable to store the last time movement occurred
    int moveDelay = 100; // Delay in milliseconds
    public int tileSize = 32;
    int maxScreenSizeCol = 128;
    int maxScreenSizeRow = 128;
    double initX = 256;
    double initY = 256;
    int speed = 32;
    boolean up = false, down = false, left = false, right = false, attacking = false;
    boolean goingRight = true;
    boolean playerGoingRight = true;
    int badGuyX = 192; // Initial x-coordinate of badGuy
    int[][] mapArray = new int[maxScreenSizeCol][maxScreenSizeRow]; //innit the aarray
    PImage grass, grass2, path, tree, wall, badGuyRight, badGuyLeft, playerLeft, playerRight, wizard, brianLeft, brianRight, swordLeft, swordRight; // Declare all the images
    Player player;
    SmallBoss smallBoss;
    SmallBoss brian;
    ArrayList<SmallBoss> smallBosses= new ArrayList<>();

    public static void main(String [] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(32 * tileSize, tileSize * 32);
    }

    public void setup() {
        playerRight = loadImage("Player/playerRight.png");
        playerLeft = loadImage("Player/playerLeft.png");
        wizard = loadImage("Player/ez.png");
        grass = loadImage("Maps/grass1.png");
        grass2 = loadImage("Maps/grass2.png");
        path = loadImage("Maps/pathVertical.png");
        tree = loadImage("Maps/angle.png");
        wall = loadImage("Maps/wall.png");
        badGuyRight = loadImage("Player/badGuyRight.png");
        badGuyLeft = loadImage("Player/badGuyLeft.png");
        brianLeft = loadImage("Player/BrianLeft.png");
        brianRight = loadImage("Player/BrianRight.png");
        swordLeft = loadImage("Player/swordLeft.png");
        swordRight = loadImage("Player/swordRight.png");


        player = new Player(initX, initY, 100);
        player.getInventory("Sword");
        smallBoss = new SmallBoss(192, 192, 10, 50, 10, badGuyLeft, badGuyRight);
        brian = new SmallBoss(200, 256, 5, 25, 5, brianLeft, brianRight);

        smallBosses.add(smallBoss);
        smallBosses.add(brian);
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

        if (!gameStarted) {
            // Display the "Press space to start" message
            background(0);
            fill(255); // Set text color to black
            text("Press SPACE to start", width/2, height/2);

        } else {
            long currentTime = millis();
            if (currentTime - lastMoveTime > moveDelay) {
                background(0);
                System.out.println("1");
                checkMovement();
                System.out.println("2");

                drawMap();
                System.out.println("3");

                System.out.println("4");
                updateEntity();

                drawHealthBar(player.life, 100);
                System.out.println("5");

                System.out.println("6");
                drawXPBar(100, 100);

                //            image(playerRight, maxScreenSizeCol*tileSize/2, maxScreenSizeRow*tileSize/2);
                if (playerGoingRight) {
                    image(playerRight, (float) (32 * tileSize) / 2, (float) (32 * tileSize) / 2);
                    if (player.equiped) {
                        image(swordRight, (float) (32 * tileSize - 16) / 2 + 32, (float) (32 * tileSize - 16) / 2);
                    }

                } else {
                    image(playerLeft, (float) (32 * tileSize) / 2, (float) (32 * tileSize) / 2);
                    if (player.equiped) {
                        image(swordLeft, (float) (32 * tileSize) / 2 - 24, (float) (32 * tileSize - 16) / 2);
                    }
                }
                System.out.println("7");

                lastMoveTime = currentTime;
            }
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
        } else if (key == 'f') {
            player.pressedF(true);
            attacking = player.getFResult();
        } else if (key == 'q') {
            player.setInventory("sword");
//            player.equiped = !player.equiped;
        }
        if (key == ' ' && !gameStarted) {
            // Start the game when SPACE is pressed
            gameStarted = true;
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
        } else if (key == 'f') {
            player.pressedF(false);
            attacking = player.getFResult();
        } else if (key == 'q') {
            player.setInventory("sword");
            player.equiped = true;
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
                int tile = mapArray[i][j];
                PImage tileImage = null;
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
        for (SmallBoss b : smallBosses) {
            float offsetX = (float) (width / 2 - initX);
            float offsetY = (float) (height / 2 - initY);
            translate(offsetX, offsetY);


            if (!b.inRange(player)) {
                if (b.goingRight) {
                    b.positionX += 8;
                    if (b.positionX >= maxScreenSizeCol * tileSize - 2 * tileSize) {
                        b.goingRight = false;
                        b.positionX -= 8;
                    }
                } else {
                    b.positionX -= 8;
                    if (b.positionX <= 192) {
                        b.goingRight = true;
                        b.positionX = 192;
                    }
                }
            } else {
                if (player.equiped) {
                    if (player.getFResult()) {
                        // If the player is attacking
                        b.receiveDamage(player.getAttack());
                    }
                    if (b.isDefeated()) {
                        smallBosses.remove(b);
                    }
                    b.receiveDamage(player.getAttack());
                    if (b.isDefeated()) {
                        // If the small boss is defeated
                        smallBosses.remove(b);
                        player.gainXP(b.getXP());
                    }
                }

                if (b.positionX < player.playerX) {
                    b.positionX += 8;
                } else {
                    b.positionX -= 8;
                }

                if (b.positionY < player.playerY) {
                    b.positionY += 8;
                } else {
                    b.positionY -= 8;
                }
            }

            // Check for collision with player
            if (player.checkCollision(b, tileSize)) {
                long currentTime = millis();
                if (currentTime == 2000) {
                    // Reduce player's health
                    player.reduceHealth(5); // Assuming 5 health points deduction
                }
                if (player.equiped) {
                    if (player.getFResult()) {
                        b.receiveDamage(player.getAttack());
                    }
                    if (b.isDefeated()) {
                        smallBosses.remove(b);
                    }
                }
            }

            // Draw the smallBoss image
            if (b.goingRight) {
                image(b.getRight(), (float) b.getPositionX(), (float) b.getPositionY(), tileSize, tileSize);
            } else {
                image(b.getLeft(), (float) b.getPositionX(), (float) b.getPositionY(), tileSize, tileSize);
            }
            translate(-offsetX, -offsetY);
        }
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
    public void collision () {

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