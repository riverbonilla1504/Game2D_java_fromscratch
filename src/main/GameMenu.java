package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GameMenu {

    private GamePanel gamePanel;  // reference to the GamePanel used in the game
    private KeyHandler keyH;      // reference to the KeyHandler used in the game

    // constructor
    public GameMenu(GamePanel gamePanel, KeyHandler keyH) {
        this.gamePanel = gamePanel;
        this.keyH = keyH;
    }

    public void drawMenu(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;  // initialize Graphics2D

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Dungeon Scape";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        g2.setColor(Color.white);
        g2.drawString(text, (gamePanel.screenWidth - textWidth) / 2, gamePanel.screenHeight / 4);

        gamePanel.player.draw(g2); // display the player

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Press Enter to Start";
        textWidth = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (gamePanel.screenWidth - textWidth) / 2, gamePanel.screenHeight / 2 + gamePanel.tileSize + gamePanel.tileSize);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Press 'ESC' to Exit";
        textWidth = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (gamePanel.screenWidth - textWidth) / 2, gamePanel.screenHeight / 2 + gamePanel.tileSize * 4);

        // use the key handler to check if the Enter key is pressed
        if (keyH.enterPressed) {
            gamePanel.player.setDefaultValues();
            gamePanel.ScreenState = 1; // change the screen state to 1
        }
    }
}
