package com.game.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Handles the main menu rendering and interaction
 */
public class GameMenu {

    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;

    public GameMenu(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
    }

    public void drawMenu(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Draw title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, GameConfig.TITLE_FONT_SIZE));
        String title = GameConfig.MENU_TITLE;
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.setColor(GameConfig.TEXT_COLOR);
        g2.drawString(title, (GameConfig.SCREEN_WIDTH - titleWidth) / 2, GameConfig.SCREEN_HEIGHT / 4);

        // Draw player preview
        gamePanel.getPlayer().draw(g2);

        // Draw start prompt
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, GameConfig.MENU_FONT_SIZE));
        String startPrompt = GameConfig.START_PROMPT;
        int startWidth = g2.getFontMetrics().stringWidth(startPrompt);
        g2.drawString(startPrompt, (GameConfig.SCREEN_WIDTH - startWidth) / 2,
                GameConfig.SCREEN_HEIGHT / 2 + GameConfig.TILE_SIZE * 2);

        // Draw exit prompt
        String exitPrompt = GameConfig.EXIT_PROMPT;
        int exitWidth = g2.getFontMetrics().stringWidth(exitPrompt);
        g2.drawString(exitPrompt, (GameConfig.SCREEN_WIDTH - exitWidth) / 2,
                GameConfig.SCREEN_HEIGHT / 2 + GameConfig.TILE_SIZE * 4);

        // Handle input
        if (keyHandler.enterPressed) {
            gamePanel.getPlayer().setDefaultValues();
            gamePanel.setCurrentState(GameState.PLAYING);
        }
    }
}
