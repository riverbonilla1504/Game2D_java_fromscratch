package com.game.main;

/**
 * Enum representing the different states of the game
 * Replaces magic numbers with semantic constants
 */
public enum GameState {
    MENU(0, "Menu"),
    PLAYING(1, "Playing"),
    PAUSED(2, "Paused"),
    GAME_OVER(3, "Game Over");

    private final int value;
    private final String description;

    GameState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get GameState from integer value
     * 
     * @param value The integer value
     * @return The corresponding GameState, or MENU as default
     */
    public static GameState fromValue(int value) {
        for (GameState state : values()) {
            if (state.value == value) {
                return state;
            }
        }
        return MENU; // Default fallback
    }

    @Override
    public String toString() {
        return description;
    }
}