package main;

import entity.Entity;
import entity.EntityFactory;
import entity.PlayerFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game panel that handles rendering, game loop, and state management
 * Implements singleton pattern for global access
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
            player.update();
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
        // Draw tiles
        tileManager.draw(g2);

        // Draw objects
        for (SuperObject obj : gameObjects) {
            if (obj != null) {
                obj.draw(g2, this);
            }
        }

        // Draw player
        player.draw(g2);
    }

    private void renderPauseOverlay(Graphics2D g2) {
        // TODO: Implement pause overlay
    }

    private void renderGameOver(Graphics2D g2) {
        // TODO: Implement game over screen
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

    // Legacy compatibility - convert to use new ArrayList
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