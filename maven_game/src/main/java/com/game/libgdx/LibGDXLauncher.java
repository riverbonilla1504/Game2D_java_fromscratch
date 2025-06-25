package com.game.libgdx;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.main.GameConfig;

/**
 * LibGDX Launcher that replaces the Swing-based App class
 * This is the entry point for the LibGDX version of the game
 */
public class LibGDXLauncher {

    public static void main(String[] args) {
        // Configure the LibGDX application
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        // Window settings
        config.setTitle(GameConfig.GAME_TITLE + " - LibGDX Version");
        config.setWindowedMode(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        config.setResizable(true);
        config.useVsync(true);

        // Performance settings
        config.setForegroundFPS(GameConfig.TARGET_FPS);
        config.setIdleFPS(30);

        // Start the application
        new Lwjgl3Application(new LibGDXGame(), config);
    }
}