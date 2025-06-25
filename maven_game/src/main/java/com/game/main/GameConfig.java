package com.game.main;

/**
 * Centralized configuration class for all game constants
 * Eliminates magic numbers and provides easy configuration management
 */
public final class GameConfig {
    // Screen settings
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int MAX_SCREEN_COL = 16;
    public static final int MAX_SCREEN_ROW = 12;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL; // 768 pixels
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW; // 576 pixels

    // Map settings (much larger than screen for BSP dungeons)
    public static final int MAP_WIDTH = 64; // Larger map width
    public static final int MAP_HEIGHT = 48; // Larger map height

    // Game performance
    public static final int TARGET_FPS = 60;
    public static final long NANOS_PER_SECOND = 1_000_000_000L;
    public static final long DRAW_INTERVAL = NANOS_PER_SECOND / TARGET_FPS;

    // Animation settings
    public static final int SPRITE_ANIMATION_SPEED = 12;
    public static final int DEFAULT_SPRITE_ANIMATION_SPEED = 8;
    public static final int MAX_SPRITE_FRAMES = 4;

    // Player settings
    public static final int PLAYER_SPEED = 4;
    public static final int PLAYER_SOLID_AREA_X = 8;
    public static final int PLAYER_SOLID_AREA_Y = 16;
    public static final int PLAYER_SOLID_AREA_WIDTH = 32;
    public static final int PLAYER_SOLID_AREA_HEIGHT = 32;

    // Object settings
    public static final int MAX_OBJECTS = 10;
    public static final int OBJECT_SOLID_AREA_SIZE = 48;

    // Tile settings
    public static final int MAX_TILES = 12;
    public static final double FLOOR_VARIATION_CHANCE = 0.15; // 15% chance

    // BSP Dungeon generation settings
    public static final int BSP_MIN_ROOM_SIZE = 6;
    public static final int BSP_MAX_ROOM_SIZE = 12;
    public static final int BSP_MIN_REGION_SIZE = 10;
    public static final int BSP_ROOM_PADDING = 2;
    public static final int BSP_MAX_RECURSION_DEPTH = 6;

    // UI settings
    public static final String GAME_TITLE = "Dungeon Escape";
    public static final String MENU_TITLE = "Dungeon Scape";
    public static final String START_PROMPT = "Press Enter to Start";
    public static final String EXIT_PROMPT = "Press 'ESC' to Exit";
    public static final String REGENERATE_PROMPT = "Press 'R' to Regenerate Map";

    // Font settings
    public static final float TITLE_FONT_SIZE = 96.0f;
    public static final float MENU_FONT_SIZE = 48.0f;

    // Colors
    public static final java.awt.Color BACKGROUND_COLOR = java.awt.Color.BLACK;
    public static final java.awt.Color TEXT_COLOR = java.awt.Color.WHITE;

    // Private constructor to prevent instantiation
    private GameConfig() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}