package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GameConfig;

/**
 * Abstract base class for all game entities
 * Provides common functionality for movement, animation, and collision
 * detection
 */
public abstract class Entity {
    // Position and movement
    protected int x, y;
    protected int speed;
    protected Direction direction = Direction.DEFAULT;

    // Animation properties
    protected BufferedImage up1, up2, up3, up4;
    protected BufferedImage down1, down2, down3, down4;
    protected BufferedImage left1, left2, left3, left4;
    protected BufferedImage right1, right2, right3, right4;
    protected BufferedImage default1, default2, default3, default4;

    // Animation counters
    protected int spriteCounter = 0;
    protected int spriteNum = 1;
    protected int spriteCounterDefault = 0;
    protected int spriteNumDefault = 1;

    // Collision detection
    protected Rectangle solidArea;
    protected int solidAreaDefaultX, solidAreaDefaultY;
    protected boolean collisionOn = false; // Fixed naming convention

    /**
     * Set default values for the entity
     */
    public abstract void setDefaultValues();

    /**
     * Load entity images from resources
     */
    public abstract void getEntityImages();

    /**
     * Draw the entity on the screen
     * 
     * @param g2 Graphics2D context
     */
    public abstract void draw(Graphics2D g2);

    /**
     * Update entity state (movement, animation, etc.)
     */
    public abstract void update();

    /**
     * Get the current sprite image based on direction and animation frame
     * 
     * @return The current sprite image to display
     */
    protected BufferedImage getCurrentSprite() {
        BufferedImage[][] sprites = {
                { up1, up2, up3, up4 },
                { down1, down2, down3, down4 },
                { left1, left2, left3, left4 },
                { right1, right2, right3, right4 },
                { default1, default2, default3, default4 }
        };

        int directionIndex = direction.ordinal();
        int frameIndex = (direction == Direction.DEFAULT) ? spriteNumDefault - 1 : spriteNum - 1;

        if (directionIndex < sprites.length && frameIndex < sprites[directionIndex].length) {
            return sprites[directionIndex][frameIndex];
        }

        return default1; // Fallback
    }

    /**
     * Update animation counters
     * 
     * @param isMoving Whether the entity is currently moving
     */
    protected void updateAnimation(boolean isMoving) {
        if (isMoving) {
            spriteCounter++;
            if (spriteCounter > GameConfig.SPRITE_ANIMATION_SPEED) {
                spriteNum = (spriteNum % GameConfig.MAX_SPRITE_FRAMES) + 1;
                spriteCounter = 0;
            }
        } else {
            spriteCounterDefault++;
            if (spriteCounterDefault > GameConfig.DEFAULT_SPRITE_ANIMATION_SPEED) {
                spriteNumDefault = (spriteNumDefault % GameConfig.MAX_SPRITE_FRAMES) + 1;
                spriteCounterDefault = 0;
            }
        }
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }

    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }
}
