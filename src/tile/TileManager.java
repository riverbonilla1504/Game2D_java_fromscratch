package tile;

import java.awt.Graphics2D;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;
public class TileManager {
    public int [][] tileIndexes;
    GamePanel gameP;
    public Tile[] tile;

    // Constructor for TileManager
    public TileManager(GamePanel gameP) {
        this.gameP = gameP;
        tile = new Tile[12];
        tileIndexes = new int[gameP.maxScreenCol][gameP.maxScreenRow];
        initialize();
        generateTiles();
    }

    // Initialization method to avoid calling overridable methods in the constructor
    private void initialize() {
        getTileImage();
    }
    
    // Method to load the tiles
    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/floor1.png"));

            tile[1] = new Tile();
            tile[1].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallUp.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallLeft.png"));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallCornerUpLeft.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallCornerUpRight.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallCornerDownLeft.png"));
            tile[5].collision = true;

            tile[6] = new Tile();
            tile[6].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallCornerDownRight.png"));
            tile[6].collision = true;

            tile[7] = new Tile();
            tile[7].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallDown.png"));
            tile[7].collision = true;

            tile[8] = new Tile();
            tile[8].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/wallRight.png"));
            tile[8].collision = true;

            tile[9] = new Tile();
            tile[9].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/floor2.png"));

            tile[10] = new Tile();
            tile[10].tileImage = ImageIO.read(getClass().getResourceAsStream("/main/tiles/floor3.png"));


            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateTiles() {
        Random random = new Random();
    
        // Iterate over the map
        for (int i = 0; i < tileIndexes.length; i++) {
            for (int j = 0; j < tileIndexes[i].length; j++) {
            
            // Check if the cell is a corner
            if (isCorner(i, j)) {
                tileIndexes[i][j] = getCornerTile(i, j);
            } else if (isVerticalBorder(i, j)) {
                tileIndexes[i][j] = 2; // Tile type for vertical border
            } else if (isHorizontalBorder(i, j)) {
                tileIndexes[i][j] = 1; // Tile type for horizontal border
            } else {

                // Floor Randomization tiles
                if (random.nextInt(100) < 15) {
                tileIndexes[i][j] = 9; 
                } else if (random.nextInt(100) < 15) {
                tileIndexes[i][j] = 10; 
                } else {
                tileIndexes[i][j] = 0; 
                }
            }
            }
        }
        replaceWallsAtBottom();
        replaceVerticalBorders();
        }
    // Method to check if a cell is a corner
    private boolean isCorner(int x, int y) {
        return (x == 0 || x == tileIndexes.length - 1) && (y == 0 || y == tileIndexes[0].length - 1);
    }
    
    // Method to check if a cell is a vertical border (excluding corners)
    private boolean isVerticalBorder(int x, int y) {
        return (x == 0 || x == tileIndexes.length - 1) && !(y == 0 || y == tileIndexes[0].length - 1);
    }
    
    // Method to check if a cell is a horizontal border (excluding corners)
    private boolean isHorizontalBorder(int x, int y) {
        return (y == 0 || y == tileIndexes[0].length - 1) && !(x == 0 || x == tileIndexes.length - 1);
    }
    // Method to get the type of tile for corners
    private int getCornerTile(int x, int y) {
        if (x == 0 && y == 0) {
            return 3; // Upper Left Corner
        } else if (x == tileIndexes.length - 1 && y == 0) {
            return 4; // Upper Right Corner
        } else if (x == 0 && y == tileIndexes[0].length - 1) {
            return 5; // Lower Left Corner
        } else if (x == tileIndexes.length - 1 && y == tileIndexes[0].length - 1) {
            return 6; // Lower Right Corner
        }
        return 1; // Default
        }

        private void replaceWallsAtBottom() {
        for (int i = 0; i < tileIndexes.length; i++) {
            int bottomRowIndex = tileIndexes[0].length - 1; // Last row   

            if (tileIndexes[i][bottomRowIndex] == 1) { // If it's a wall
            tileIndexes[i][bottomRowIndex] = 7; // Change to another type of tile (index 7)
            }
        }
        }
    // Method to check and replace vertical walls on the left and right borders
    private void replaceVerticalBorders() {
        for (int j = 0; j < tileIndexes[0].length; j++) {
            // Left border
            if (tileIndexes[0][j] == 2) {
                tileIndexes[0][j] = 2; // Change to tile type for left vertical border (index 2)
            }

            // Right border
            if (tileIndexes[tileIndexes.length - 1][j] == 2) {
                tileIndexes[tileIndexes.length - 1][j] = 8; // Change to tile type for right vertical border (index 8)
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        for (int i = 0; i < gameP.maxScreenCol; i++) {
            for (int j = 0; j < gameP.maxScreenRow; j++) {
                int tileIndex = tileIndexes[i][j];
                if (tileIndex >= 0 && tileIndex < tile.length && tile[tileIndex] != null) {
                    g2.drawImage(tile[tileIndex].tileImage, i * gameP.tileSize, j * gameP.tileSize, gameP.tileSize, gameP.tileSize, null);
                } else {
                    // Draw a default tile or a graphical error here
                    System.err.println("Error: Tile index " + tileIndex + " is invalid or the tile is not initialized.");
                }

            }
        }
    }
}
