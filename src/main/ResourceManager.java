package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * ResourceManager handles loading and caching of game assets
 * Implements singleton pattern for global access
 */
public class ResourceManager {
    private static ResourceManager instance;
    private final Map<String, BufferedImage> imageCache;

    // Asset paths constants
    public static final String ASSETS_PATH = "/main/assets/";
    public static final String TILES_PATH = "/main/tiles/";
    public static final String OBJECTS_PATH = "/main/objects/";

    private ResourceManager() {
        imageCache = new HashMap<>();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    /**
     * Loads an image from the specified path and caches it
     * 
     * @param path The path to the image resource
     * @return The loaded BufferedImage
     * @throws IOException if the image cannot be loaded
     */
    public BufferedImage loadImage(String path) throws IOException {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
        if (image == null) {
            throw new IOException("Could not load image: " + path);
        }

        imageCache.put(path, image);
        return image;
    }

    /**
     * Loads a player sprite image
     * 
     * @param direction The direction (up, down, left, right, default)
     * @param frame     The animation frame (1-4)
     * @return The loaded BufferedImage
     */
    public BufferedImage loadPlayerSprite(String direction, int frame) {
        try {
            String path = ASSETS_PATH + "sprite_player_" + direction + frame + ".png";
            return loadImage(path);
        } catch (IOException e) {
            System.err.println("Error loading player sprite: " + direction + frame);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a tile image
     * 
     * @param tileName The name of the tile file
     * @return The loaded BufferedImage
     */
    public BufferedImage loadTile(String tileName) {
        try {
            String path = TILES_PATH + tileName + ".png";
            return loadImage(path);
        } catch (IOException e) {
            System.err.println("Error loading tile: " + tileName);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads an object image
     * 
     * @param objectName The name of the object file
     * @return The loaded BufferedImage
     */
    public BufferedImage loadObject(String objectName) {
        try {
            String path = OBJECTS_PATH + objectName + ".png";
            return loadImage(path);
        } catch (IOException e) {
            System.err.println("Error loading object: " + objectName);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Clears the image cache to free memory
     */
    public void clearCache() {
        imageCache.clear();
    }

    /**
     * Gets the current cache size
     * 
     * @return Number of cached images
     */
    public int getCacheSize() {
        return imageCache.size();
    }
}