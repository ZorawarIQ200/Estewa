import processing.core.*;
import java.io.*;
import java.util.ArrayList;

import Entity.*;


public class Main extends PApplet {

    long lastCreatureSpawnTime = 0; // Variable to store the last time a creature was spawned
    int creatureSpawnInterval = 330;
    boolean gameStarted = false, playerDead = false;
    boolean playerWin = false;
    boolean space = false;
    long lastMoveTime = 0; // Variable to store the last time movement occurred
    int moveDelay = 100; // Delay in milliseconds
    long startTime; // Variable to store the start time of the game
    int tileSize = 32;
    int maxScreenSizeCol = 64;
    int maxScreenSizeRow = 64;
    double initX = 250;
    double initY = 250;
    int speed = 32;
    boolean up = false, down = false, left = false, right = false, attacking = false;
    boolean goingRight = true;
    boolean playerGoingRight = true;
    int badGuyX = 192; // Initial x-coordinate of badGuy
    int[][] mapArray = new int[maxScreenSizeCol][maxScreenSizeRow]; // Init the array
    PImage grass, grass2, path, tree, wall, badGuyRight, badGuyLeft, playerLeft, playerRight, wizard, brianLeft, brianRight, swordLeft, swordRight; // Declare all the images
    Player player;
    SmallBoss smallBoss;
    int playerScore = 0; // Variable to track the player's score
    long lastScoreUpdateTime = 0; // Variable to store the last time the score was updated
    int scoreUpdateInterval = 1000; // Update score every 1000 milliseconds (1 second)
    SmallBoss brian;
    ArrayList<SmallBoss> smallBosses= new ArrayList<>();

    public static void main(String [] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(maxScreenSizeCol/2 * tileSize, tileSize/2 * maxScreenSizeRow);
    }

    public void setup() {
        startTime = millis(); // Initialize the start time
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
        brian = new SmallBoss(300, 500, 5, 25, 5, brianLeft, brianRight);

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
            // Display start message and wait for space bar press
            textSize(32);
            fill(255);
            textAlign(CENTER, CENTER);
            text("Press SPACE to start the game", width/2, height/2);

            if (keyPressed && space) {
                gameStarted = true; // Start the game when space bar is pressed
            }
        } else if (gameStarted){
            if (!playerDead) {
                long currentTime = millis();
                if (currentTime - lastMoveTime > moveDelay) {
                    background(0);

                    drawMap();

                    // Draw the player first
                    if (playerGoingRight) {
                        image(playerRight, (float) (width / 2), (float) (height / 2)); // Draw player facing right
                    } else {
                        image(playerLeft, (float) (width / 2), (float) (height / 2)); // Draw player facing left
                    }
                    checkMovement();

                    // Draw the rest of the game elements
                    updateEntity();
                    drawHealthBar(player.life, 100);
                    drawXPBar(player.xpCount);

                    // Check if the player has survived for more than 5 minutes
                    if (millis() - startTime > 5 * 60 * 1000) {
                        playerWin = true;
                        // Player wins
                        displayGameOverMessage();
                    }
                    lastMoveTime = currentTime;
                }
                // Update player's score every second
                if (currentTime - lastScoreUpdateTime >= scoreUpdateInterval) {
                    player.gainXP(1); // Increment player's score
                    lastScoreUpdateTime = currentTime; // Update last score update time
                }

                if (currentTime - lastCreatureSpawnTime >= creatureSpawnInterval) {
                    spawnCreature();
                    lastCreatureSpawnTime = currentTime; // Update last creature spawn time
                }

            } else {
                // Player is dead, display game over screen
                displayGameOverMessage();
            }
        }
        // Display "Level Up!" message if player.killAll is true
        if (player.killAll) {
            textSize(32);
            fill(0, 255, 0); // Green color
            textAlign(CENTER, CENTER);
            text("Level Up!", width / 2, height / 2 + 50); // Display the message at a specific position on the screen
        }
    }

    public void spawnCreature() {
        // Generate random position for the creature
        double randX = random(0, maxScreenSizeCol * tileSize);
        double randY = random(0, maxScreenSizeRow * tileSize);
        float rando = random(0, 2);

        // Create a new creature
        SmallBoss newCreature;
        if (rando < 1) {
            newCreature = new SmallBoss((int) randX, (int) randY, 10, 50, 10, badGuyLeft, badGuyRight);
        } else {
            newCreature = new SmallBoss((int) randX, (int) randY, 10, 50, 10, brianRight, brianLeft);
        }

        // Add the new creature to the smallBosses ArrayList
        smallBosses.add(newCreature);
        if (player.killAll) {
            smallBosses.clear();
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
            player.equiped = true;
        } else if (key == ' ') {
            space = true;
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
        } else if (key == ' ') {
            space = true;
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

    public void updateEntity() {
        for (SmallBoss b : smallBosses) {
            float offsetX = (float) (width / 2 - initX);
            float offsetY = (float) (height / 2 - initY);
            translate(offsetX, offsetY);

            // Move the smallBoss
            if (b.positionX < player.getPositionX()) {
                b.goingRight = true;
                b.positionX += player.enemySpeed;
                if (b.positionX >= maxScreenSizeCol * tileSize - 2 * tileSize) {
                    b.goingRight = false;
                    b.positionX -= player.enemySpeed;
                }
            } else if (b.positionX > player.getPositionX()) {
                b.positionX -= player.enemySpeed;
                b.goingRight = false;
            /*if (b.positionX <= 192) {
                b.goingRight = true;
                b.positionX = 192;
            }*/
            }
            // Adjust vertical movement based on player movement
            if (player.getPositionY() < b.getPositionY()) {
                b.positionY -= player.enemySpeed;
            } else if (player.getPositionY() > b.getPositionY()) {
                b.positionY += player.enemySpeed;
            }

            // Check for collision with player
            if (player.checkCollision(b, tileSize)) {
                // Reduce player's health
                player.reduceHealth(player.reduceHealth); // Assuming 5 health points deduction
            }

            // Draw the smallBoss image
            if (b.goingRight) {
                image(b.getRight(), (float) b.getPositionX(), (float) b.getPositionY(), tileSize, tileSize);
            } else {
                image(b.getLeft(), (float) b.getPositionX(), (float) b.getPositionY(), tileSize, tileSize);
            }
            translate(-offsetX, -offsetY);
        }

        // Check if the player's life reaches zero
        if (player.life <= 0) {
            // Player dies
            displayGameOverMessage();
            playerDead = true;
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

    public void drawXPBar(int currentXP) {
        int barHeight = 10;
        float remainingWidth = (float) (3.0 / 2.0 * currentXP); // Adjust multiplier as needed
        float posX = 50; // Start the XP bar from the left side of the screen
        float posY = 75;

        fill(255, 215, 0);
        rect(posX, posY, remainingWidth, barHeight);
    }


    public void displayGameOverMessage() {

        background(0);
        textSize(32);
        fill(255, 0, 0);
        textAlign(CENTER, CENTER);
        if (!playerWin) {
            text("Game Over!", width / 2, height / 2);
            text("you got " + (player.totalXp * 1000) + " total points", width / 2, height / 2 + 200);
        } else {
            text("You Win!", width/2, height/2);
            text("you got all the points. Congrats nerdge", width / 2, height / 2 + 200);

        }
        }
    public void displayWinMessage() {
        textSize(32);
        fill(0, 255, 0);
        textAlign(CENTER, CENTER);

    }
}

