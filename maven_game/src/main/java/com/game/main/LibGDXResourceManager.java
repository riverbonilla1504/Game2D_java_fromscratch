package com.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

/**
 * LibGDX version of ResourceManager that uses LibGDX's Texture system
 * Replaces the Swing-based ResourceManager for better GPU performance
 */
public class LibGDXResourceManager {
    private static LibGDXResourceManager instance;
    private final Map<String, Texture> textureCache;
    private final Map<String, TextureRegion> regionCache;

    // Asset paths constants
    public static final String ASSETS_PATH = "assets/";
    public static final String TILES_PATH = "tiles/";
    public static final String OBJECTS_PATH = "objects/";

    private LibGDXResourceManager() {
        textureCache = new HashMap<>();
        regionCache = new HashMap<>();
    }

    public static LibGDXResourceManager getInstance() {
        if (instance == null) {
            instance = new LibGDXResourceManager();
        }
        return instance;
    }

    /**
     * Loads a texture from the specified path and caches it
     * 
     * @param path The path to the texture resource
     * @return The loaded Texture
     */
    public Texture loadTexture(String path) {
        if (textureCache.containsKey(path)) {
            return textureCache.get(path);
        }

        try {
            Texture texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest); // Pixel art filtering
            textureCache.put(path, texture);
            return texture;
        } catch (Exception e) {
            System.err.println("Could not load texture: " + path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a player sprite texture
     * 
     * @param direction The direction (up, down, left, right, default)
     * @param frame     The animation frame (1-4)
     * @return The loaded Texture
     */
    public Texture loadPlayerSprite(String direction, int frame) {
        String path = ASSETS_PATH + "sprite_player_" + direction + frame + ".png";
        return loadTexture(path);
    }

    /**
     * Loads a tile texture
     * 
     * @param tileName The name of the tile file
     * @return The loaded Texture
     */
    public Texture loadTile(String tileName) {
        String path = TILES_PATH + tileName + ".png";
        return loadTexture(path);
    }

    /**
     * Loads an object texture
     * 
     * @param objectName The name of the object file
     * @return The loaded Texture
     */
    public Texture loadObject(String objectName) {
        String path = OBJECTS_PATH + objectName + ".png";
        return loadTexture(path);
    }

    /**
     * Gets a TextureRegion for a texture (useful for sprite sheets)
     * 
     * @param path The path to the texture
     * @return The TextureRegion
     */
    public TextureRegion getRegion(String path) {
        if (regionCache.containsKey(path)) {
            return regionCache.get(path);
        }

        Texture texture = loadTexture(path);
        if (texture != null) {
            TextureRegion region = new TextureRegion(texture);
            regionCache.put(path, region);
            return region;
        }
        return null;
    }

    /**
     * Clears the texture cache to free memory
     */
    public void clearCache() {
        for (Texture texture : textureCache.values()) {
            texture.dispose();
        }
        textureCache.clear();
        regionCache.clear();
    }

    /**
     * Gets the current cache size
     * 
     * @return Number of cached textures
     */
    public int getCacheSize() {
        return textureCache.size();
    }

    /**
     * Dispose all resources - should be called when shutting down
     */
    public void dispose() {
        clearCache();
    }
}