package com.game.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.game.main.GameConfig;
import com.game.main.LibGDXResourceManager;

/**
 * LibGDX version of SuperObject base class
 * Handles game objects using LibGDX rendering system
 */
public class LibGDXSuperObject {
    private Texture texture;
    private String name;
    private boolean collision = false;
    private float worldX, worldY;
    private Rectangle solidArea = new Rectangle(0, 0, GameConfig.OBJECT_SOLID_AREA_SIZE,
            GameConfig.OBJECT_SOLID_AREA_SIZE);
    private int solidAreaDefaultX = 0;
    private int solidAreaDefaultY = 0;

    private final LibGDXResourceManager resourceManager = LibGDXResourceManager.getInstance();

    public LibGDXSuperObject(String objectName, float x, float y) {
        this.name = objectName;
        this.worldX = x;
        this.worldY = y;
        loadTexture();
        setupSolidArea();
    }

    private void loadTexture() {
        this.texture = resourceManager.loadObject(name);
    }

    private void setupSolidArea() {
        solidArea.x = worldX;
        solidArea.y = worldY;
        solidAreaDefaultX = (int) solidArea.x;
        solidAreaDefaultY = (int) solidArea.y;
    }

    public void update(float deltaTime) {
        // Update logic can be overridden by subclasses
    }

    public void render(SpriteBatch spriteBatch) {
        if (texture != null) {
            spriteBatch.draw(texture, worldX, worldY, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public float getWorldX() {
        return worldX;
    }

    public void setWorldX(float worldX) {
        this.worldX = worldX;
        solidArea.x = worldX + solidAreaDefaultX;
    }

    public float getWorldY() {
        return worldY;
    }

    public void setWorldY(float worldY) {
        this.worldY = worldY;
        solidArea.y = worldY + solidAreaDefaultY;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public Texture getTexture() {
        return texture;
    }
}