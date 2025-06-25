package com.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * LibGDX version of KeyHandler that uses LibGDX's input system
 * Replaces the Swing-based KeyHandler for better performance
 */
public class LibGDXKeyHandler {
    private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, regenerateMapPressed;

    /**
     * Update input state - should be called every frame
     */
    public void update() {
        // Check for key presses using LibGDX input system
        upPressed = Gdx.input.isKeyPressed(Input.Keys.W);
        downPressed = Gdx.input.isKeyPressed(Input.Keys.S);
        leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
        rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);

        // Check for just pressed keys (for single press events)
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            enterPressed = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            regenerateMapPressed = true;
        }

        // Handle exit
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    // Getters
    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public boolean isRegenerateMapPressed() {
        return regenerateMapPressed;
    }

    // Setters for resetting single-press events
    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public void setRegenerateMapPressed(boolean regenerateMapPressed) {
        this.regenerateMapPressed = regenerateMapPressed;
    }
}