package entity;

/**
 * Enum representing the different directions an entity can face
 * Replaces string constants with type-safe enum values
 */
public enum Direction {
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right"),
    DEFAULT("default");

    private final String value;

    Direction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Get Direction from string value
     * 
     * @param value The string value
     * @return The corresponding Direction, or DEFAULT as fallback
     */
    public static Direction fromString(String value) {
        for (Direction direction : values()) {
            if (direction.value.equals(value)) {
                return direction;
            }
        }
        return DEFAULT;
    }

    @Override
    public String toString() {
        return value;
    }
}