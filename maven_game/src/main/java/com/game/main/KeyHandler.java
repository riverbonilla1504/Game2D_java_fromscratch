package com.game.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, regenerateMapPressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) { // If the key pressed is W
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) { // If the key pressed is A
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) { // If the key pressed is S
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) { // If the key pressed is D
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) { // If the key pressed is ESC
            System.exit(0); // Exit the game
        }
        if (code == KeyEvent.VK_ENTER) { // If the key pressed is SPACE
            enterPressed = true;
        }
        if (code == KeyEvent.VK_R) { // If the key pressed is R
            regenerateMapPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode(); // Get the key code
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_R) {
            regenerateMapPressed = false;
        }
    }

}
