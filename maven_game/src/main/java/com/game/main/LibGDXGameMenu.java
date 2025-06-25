package com.game.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.game.libgdx.LibGDXGame;
import com.game.main.LibGDXKeyHandler;
import com.badlogic.gdx.Gdx;

/**
 * LibGDX version of GameMenu that handles main menu rendering and interaction
 */
public class LibGDXGameMenu {

    private final LibGDXGame game;
    private final LibGDXKeyHandler keyHandler;
    private final GlyphLayout layout = new GlyphLayout();

    public LibGDXGameMenu(LibGDXGame game, LibGDXKeyHandler keyHandler) {
        this.game = game;
        this.keyHandler = keyHandler;
    }

    public void render(SpriteBatch spriteBatch, BitmapFont font) {
        font.setColor(1, 1, 1, 1);
        GlyphLayout layout = new GlyphLayout();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        // Título centrado
        String title = GameConfig.MENU_TITLE;
        layout.setText(font, title);
        float titleX = (screenWidth - layout.width) / 2f;
        float titleY = screenHeight * 0.7f;
        font.draw(spriteBatch, layout, titleX, titleY);
        // Preview del jugador centrado debajo del título
        float playerX = (screenWidth - GameConfig.TILE_SIZE) / 2f;
        float playerY = titleY - GameConfig.TILE_SIZE * 2;
        game.getPlayer().renderAt(spriteBatch, playerX, playerY);
        // Start prompt centrado
        String startPrompt = GameConfig.START_PROMPT;
        layout.setText(font, startPrompt);
        float startX = (screenWidth - layout.width) / 2f;
        float startY = playerY - GameConfig.TILE_SIZE * 2;
        font.draw(spriteBatch, layout, startX, startY);
        // Exit prompt centrado
        String exitPrompt = GameConfig.EXIT_PROMPT;
        layout.setText(font, exitPrompt);
        float exitX = (screenWidth - layout.width) / 2f;
        float exitY = startY - GameConfig.TILE_SIZE * 1.5f;
        font.draw(spriteBatch, layout, exitX, exitY);
        // Regenerate prompt centrado
        String regeneratePrompt = GameConfig.REGENERATE_PROMPT;
        layout.setText(font, regeneratePrompt);
        float regenX = (screenWidth - layout.width) / 2f;
        float regenY = exitY - GameConfig.TILE_SIZE * 1.5f;
        font.draw(spriteBatch, layout, regenX, regenY);
    }
}