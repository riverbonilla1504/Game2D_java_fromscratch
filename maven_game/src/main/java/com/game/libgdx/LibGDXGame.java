package com.game.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.main.GameState;
import com.game.main.GameConfig;
import com.game.main.LibGDXKeyHandler;
import com.game.main.LibGDXResourceManager;
import com.game.main.LibGDXGameMenu;
import com.game.entity.LibGDXPlayer;
import com.game.tile.LibGDXTileManager;
import com.game.object.LibGDXSuperObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Main LibGDX game class that replaces the Swing-based GamePanel
 * Implements the LibGDX ApplicationAdapter pattern for better performance
 */
public class LibGDXGame extends ApplicationAdapter {

    // Graphics
    private static final int VIRTUAL_WIDTH = GameConfig.TILE_SIZE * 12;
    private static final int VIRTUAL_HEIGHT = GameConfig.TILE_SIZE * 8;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    // Game state
    private GameState currentState = GameState.MENU;

    // Game components
    private LibGDXKeyHandler keyHandler;
    private LibGDXResourceManager resourceManager;
    private LibGDXPlayer player;
    private LibGDXTileManager tileManager;
    private LibGDXGameMenu gameMenu;

    // Game objects
    private final List<LibGDXSuperObject> gameObjects = new ArrayList<>();

    // Performance tracking
    private int fps = 0;
    private int frameCount = 0;
    private float fpsTimer = 0f;
    private float gameTime = 0f;

    @Override
    public void create() {
        // Initialize graphics
        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Initialize font for text rendering
        font = new BitmapFont();
        font.getData().setScale(2.0f);

        // Initialize game components (LibGDX context is now ready)
        keyHandler = new LibGDXKeyHandler();
        resourceManager = LibGDXResourceManager.getInstance();
        player = new LibGDXPlayer(this, keyHandler);
        tileManager = new LibGDXTileManager(this);
        gameMenu = new LibGDXGameMenu(this, keyHandler);

        // Setup game
        setupGame();

        System.out.println("LibGDX Game initialized successfully!");
        System.out.println("Controls: WASD to move, R to regenerate map, ESC to exit");
    }

    private void setupGame() {
        if (currentState == GameState.MENU) {
            // Setup initial game objects
            setupGameObjects();
        }
    }

    private void setupGameObjects() {
        // Add some stars to collect
        gameObjects.add(new LibGDXSuperObject("star1", 100, 100));
        gameObjects.add(new LibGDXSuperObject("star1", 300, 200));
        gameObjects.add(new LibGDXSuperObject("star1", 500, 150));
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        gameTime += deltaTime;
        handleInput();
        update(deltaTime);
        updateFPS(deltaTime);
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        if (currentState == GameState.PLAYING && player != null) {
            camera.position.set(player.getX() + GameConfig.TILE_SIZE / 2f, player.getY() + GameConfig.TILE_SIZE / 2f,
                    0);
        } else {
            camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        }
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        switch (currentState) {
            case MENU -> gameMenu.render(spriteBatch, font);
            case PLAYING -> renderGame();
            case PAUSED -> {
                renderGame();
                renderPauseOverlay();
            }
            case GAME_OVER -> renderGameOver();
        }
        spriteBatch.end();
        // HUD/UI fijo
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();
        renderDebugInfo();
        spriteBatch.end();
    }

    private void handleInput() {
        // Update key handler state
        keyHandler.update();

        // Handle state transitions
        if (currentState == GameState.MENU && keyHandler.isEnterPressed()) {
            currentState = GameState.PLAYING;
            keyHandler.setEnterPressed(false);
        }

        if (currentState == GameState.PLAYING) {
            // Handle map regeneration
            if (keyHandler.isRegenerateMapPressed()) {
                tileManager.regenerateMap(System.currentTimeMillis());
                keyHandler.setRegenerateMapPressed(false);
                System.out.println("Map regenerated with new BSP seed");
            }
        }
    }

    private void update(float deltaTime) {
        if (currentState == GameState.PLAYING) {
            // Update player
            player.update(deltaTime);

            // Update tile manager (camera follows player)
            tileManager.update(deltaTime);

            // Update game objects
            for (LibGDXSuperObject obj : gameObjects) {
                obj.update(deltaTime);
            }
        }
    }

    private void renderGame() {
        // Draw tiles with camera system
        tileManager.render(spriteBatch);

        // Draw game objects
        for (LibGDXSuperObject obj : gameObjects) {
            if (obj != null) {
                obj.render(spriteBatch);
            }
        }

        // Draw player
        player.render(spriteBatch);
    }

    private void renderPauseOverlay() {
        font.setColor(1, 1, 1, 0.8f);
        font.draw(spriteBatch, "PAUSED",
                VIRTUAL_WIDTH / 2 - 100,
                VIRTUAL_HEIGHT / 2);
    }

    private void renderGameOver() {
        font.setColor(1, 0, 0, 1);
        font.draw(spriteBatch, "GAME OVER",
                VIRTUAL_WIDTH / 2 - 120,
                VIRTUAL_HEIGHT / 2);
        font.setColor(1, 1, 1, 1);
        font.draw(spriteBatch, "Stars collected: " + player.getStars(),
                VIRTUAL_WIDTH / 2 - 100,
                VIRTUAL_HEIGHT / 2 - 50);
    }

    private void renderDebugInfo() {
        font.setColor(1, 1, 1, 1);
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        font.draw(spriteBatch, "FPS: " + fps, 10, h - 20);
        font.draw(spriteBatch, "Time: " + String.format("%.1f", gameTime), 10, h - 50);
        font.draw(spriteBatch, "State: " + currentState, 10, h - 80);
    }

    private void updateFPS(float deltaTime) {
        frameCount++;
        fpsTimer += deltaTime;

        if (fpsTimer >= 1.0f) {
            fps = frameCount;
            frameCount = 0;
            fpsTimer = 0f;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (currentState == GameState.PLAYING && player != null) {
            camera.position.set(player.getX() + GameConfig.TILE_SIZE / 2f, player.getY() + GameConfig.TILE_SIZE / 2f,
                    0);
        } else {
            camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        }
        camera.update();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        if (resourceManager != null)
            resourceManager.dispose();
    }

    // Getters for game components
    public LibGDXPlayer getPlayer() {
        return player;
    }

    public LibGDXTileManager getTileManager() {
        return tileManager;
    }

    public List<LibGDXSuperObject> getGameObjects() {
        return gameObjects;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState state) {
        this.currentState = state;
    }

    public LibGDXResourceManager getResourceManager() {
        return resourceManager;
    }
}