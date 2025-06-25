package com.game.main;

import com.game.entity.Entity;
import com.game.entity.EntityFactory;
import com.game.entity.PlayerFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import com.game.object.SuperObject;
import com.game.tile.TileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game panel that handles rendering, game loop, and state management
 * Now supports larger BSP-generated maps with camera system
 */
public class GamePanel extends JPanel implements Runnable {
    // Game state
    private GameState currentState = GameState.MENU;

    // Game loop
    private Thread gameThread;
    private volatile boolean running = false;

    // Game components
    private final KeyHandler keyHandler = new KeyHandler();
    private final CollisionChecker collisionChecker = new CollisionChecker(this);
    private final AssetSetter assetSetter = new AssetSetter(this);
    private final EntityFactory playerFactory = new PlayerFactory(this, keyHandler);
    private final Entity player = playerFactory.createEntity();
    private final TileManager tileManager = new TileManager(this);
    private final GameMenu gameMenu = new GameMenu(this, keyHandler);

    // Game objects - using ArrayList instead of fixed array
    private final List<SuperObject> gameObjects = new ArrayList<>();

    // Singleton instance
    private static GamePanel instance;

    public GamePanel() {
        setupPanel();
    }

    private void setupPanel() {
        this.setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setBackground(GameConfig.BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.requestFocus();
    }

    public void setupGame() {
        if (currentState == GameState.MENU) {
            assetSetter.setObject();
        }
    }

    // Singleton pattern
    public static GamePanel getInstance() {
        if (instance == null) {
            instance = new GamePanel();
        }
        return instance;
    }

    public void startGameThread() {
        if (gameThread == null || !running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stopGameThread() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = 0;
        int frames = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long deltaTime = currentTime - lastTime;

            // Update game logic
            update();

            // Render
            repaint();

            // FPS calculation
            timer += deltaTime;
            frames++;

            if (timer >= GameConfig.NANOS_PER_SECOND) {
                // Optional: Log FPS for debugging
                // System.out.println("FPS: " + frames);
                frames = 0;
                timer = 0;
            }

            // Cap the frame rate
            long sleepTime = GameConfig.DRAW_INTERVAL - deltaTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            lastTime = currentTime;
        }
    }

    public void update() {
        if (currentState == GameState.PLAYING) {
            // Update player
            player.update();

            // Update tile manager (camera follows player)
            tileManager.update();

            // Check for map regeneration
            if (keyHandler.regenerateMapPressed) {
                tileManager.regenerateMap(System.currentTimeMillis());
                keyHandler.regenerateMapPressed = false;
                System.out.println("Map regenerated with new BSP seed");
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch (currentState) {
            case MENU -> gameMenu.drawMenu(g2);
            case PLAYING -> renderGame(g2);
            case PAUSED -> {
                renderGame(g2);
                renderPauseOverlay(g2);
            }
            case GAME_OVER -> renderGameOver(g2);
        }
    }

    private void renderGame(Graphics2D g2) {
        // Draw tiles with camera system
        tileManager.draw(g2);

        // Draw objects (need to implement camera transform for objects)
        for (SuperObject obj : gameObjects) {
            if (obj != null) {
                drawObjectWithCamera(g2, obj);
            }
        }

        // Draw player with camera transform
        drawPlayerWithCamera(g2);
    }

    /**
     * Draw object with camera transformation
     */
    private void drawObjectWithCamera(Graphics2D g2, SuperObject obj) {
        // Get camera for coordinate transformation
        Camera camera = tileManager.getCamera();

        // Convert world position to screen position
        int screenX = camera.worldToScreenX(obj.getWorldX());
        int screenY = camera.worldToScreenY(obj.getWorldY());

        // Only draw if visible on screen
        if (camera.isVisible(obj.getWorldX(), obj.getWorldY(), GameConfig.TILE_SIZE, GameConfig.TILE_SIZE)) {
            // Save original position
            int originalX = obj.getWorldX();
            int originalY = obj.getWorldY();

            // Temporarily set screen position for drawing
            obj.setWorldX(screenX);
            obj.setWorldY(screenY);

            // Draw object
            obj.draw(g2, this);

            // Restore original position
            obj.setWorldX(originalX);
            obj.setWorldY(originalY);
        }
    }

    /**
     * Draw player with camera transformation
     */
    private void drawPlayerWithCamera(Graphics2D g2) {
        Camera camera = tileManager.getCamera();

        // Convert player world position to screen position
        int screenX = camera.worldToScreenX(player.getX());
        int screenY = camera.worldToScreenY(player.getY());

        // Save original position
        int originalX = player.getX();
        int originalY = player.getY();

        // Temporarily set screen position for drawing
        player.setX(screenX);
        player.setY(screenY);

        // Draw player
        player.draw(g2);

        // Restore original position
        player.setX(originalX);
        player.setY(originalY);
    }

    private void renderPauseOverlay(Graphics2D g2) {
        // Draw semi-transparent overlay
        g2.setColor(new Color(0, 0, 0, 128));
        g2.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Draw pause text
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(48f));
        String pauseText = "PAUSED";
        int textX = GameConfig.SCREEN_WIDTH / 2 - g2.getFontMetrics().stringWidth(pauseText) / 2;
        int textY = GameConfig.SCREEN_HEIGHT / 2;
        g2.drawString(pauseText, textX, textY);
    }

    private void renderGameOver(Graphics2D g2) {
        // Draw game over screen
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(48f));
        String gameOverText = "GAME OVER";
        int textX = GameConfig.SCREEN_WIDTH / 2 - g2.getFontMetrics().stringWidth(gameOverText) / 2;
        int textY = GameConfig.SCREEN_HEIGHT / 2;
        g2.drawString(gameOverText, textX, textY);
    }

    // Getters and setters
    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState state) {
        this.currentState = state;
    }

    public Entity getPlayer() {
        return player;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public List<SuperObject> getGameObjects() {
        return gameObjects;
    }

    // Legacy method for compatibility
    public SuperObject[] getObjArray() {
        return gameObjects.toArray(new SuperObject[0]);
    }

    public void setObjArray(SuperObject[] objArray) {
        gameObjects.clear();
        for (SuperObject obj : objArray) {
            if (obj != null) {
                gameObjects.add(obj);
            }
        }
    }
}