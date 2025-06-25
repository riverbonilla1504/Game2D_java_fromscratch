package com.game;

import javax.swing.JFrame;
import com.game.main.GamePanel;
import com.game.main.GameConfig;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        // Create a new JFrame window
        JFrame window = new JFrame();

        // Set the window to close when the user clicks the close button
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Allow the window to be resized
        window.setResizable(true);

        // Create a new GamePanel object
        GamePanel gamePanel = GamePanel.getInstance();

        // Add the GamePanel to the window
        window.add(gamePanel);

        // Pack the window to fit the GamePanel
        window.pack();

        // Set the title of the window
        window.setTitle(GameConfig.GAME_TITLE);

        // Center the window on the screen
        window.setLocationRelativeTo(null);

        // Make the window visible
        window.setVisible(true);

        // Call the setupGame method in the GamePanel
        gamePanel.setupGame();

        // Start the game thread
        gamePanel.startGameThread();
    }
}