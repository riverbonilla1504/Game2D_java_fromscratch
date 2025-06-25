package com.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.game.main.GameConfig;
import com.game.main.LibGDXKeyHandler;
import com.game.main.LibGDXResourceManager;
import com.game.libgdx.LibGDXGame;

/**
 * LibGDX version of Player entity class
 * Handles player movement, animation, and interaction using LibGDX
 */
public class LibGDXPlayer {
    private final LibGDXGame game;
    private final LibGDXKeyHandler keyHandler;
    private final LibGDXResourceManager resourceManager;

    // Position and movement
    private float x, y;
    private float speed;
    private Direction direction = Direction.DEFAULT;
    private boolean collisionOn = false;

    // Animation
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private final int animationSpeed = 12;
    private int animationCounter = 0;

    // Collision
    private Rectangle solidArea;
    private int solidAreaDefaultX, solidAreaDefaultY;

    // Game state
    private int stars = 0;

    // Textures for different directions and frames
    private Texture up1, up2, up3, up4;
    private Texture down1, down2, down3, down4;
    private Texture left1, left2, left3, left4;
    private Texture right1, right2, right3, right4;
    private Texture default1, default2, default3, default4;

    public LibGDXPlayer(LibGDXGame game, LibGDXKeyHandler keyHandler) {
        this.game = game;
        this.keyHandler = keyHandler;
        this.resourceManager = LibGDXResourceManager.getInstance();

        setDefaultValues();
        getEntityImages();
        setupCollisionArea();
    }

    private void setDefaultValues() {
        x = GameConfig.SCREEN_WIDTH / 2f - 32;
        y = GameConfig.SCREEN_HEIGHT / 2f - GameConfig.TILE_SIZE;
        speed = GameConfig.PLAYER_SPEED;
        direction = Direction.DEFAULT;
    }

    private void getEntityImages() {
        // Load player sprites using LibGDX ResourceManager
        up1 = resourceManager.loadPlayerSprite("up", 1);
        up2 = resourceManager.loadPlayerSprite("up", 2);
        up3 = resourceManager.loadPlayerSprite("up", 3);
        up4 = resourceManager.loadPlayerSprite("up", 4);

        down1 = resourceManager.loadPlayerSprite("down", 1);
        down2 = resourceManager.loadPlayerSprite("down", 2);
        down3 = resourceManager.loadPlayerSprite("down", 3);
        down4 = resourceManager.loadPlayerSprite("down", 4);

        left1 = resourceManager.loadPlayerSprite("left", 1);
        left2 = resourceManager.loadPlayerSprite("left", 2);
        left3 = resourceManager.loadPlayerSprite("left", 3);
        left4 = resourceManager.loadPlayerSprite("left", 4);

        right1 = resourceManager.loadPlayerSprite("right", 1);
        right2 = resourceManager.loadPlayerSprite("right", 2);
        right3 = resourceManager.loadPlayerSprite("right", 3);
        right4 = resourceManager.loadPlayerSprite("right", 4);

        default1 = resourceManager.loadPlayerSprite("default", 1);
        default2 = resourceManager.loadPlayerSprite("default", 2);
        default3 = resourceManager.loadPlayerSprite("default", 3);
        default4 = resourceManager.loadPlayerSprite("default", 4);
    }

    private void setupCollisionArea() {
        solidArea = new Rectangle(
                GameConfig.PLAYER_SOLID_AREA_X,
                GameConfig.PLAYER_SOLID_AREA_Y,
                GameConfig.PLAYER_SOLID_AREA_WIDTH,
                GameConfig.PLAYER_SOLID_AREA_HEIGHT);
        solidAreaDefaultX = (int) solidArea.x;
        solidAreaDefaultY = (int) solidArea.y;
    }

    public void update(float deltaTime) {
        handleInput();
        handleMovement();
        handleAnimation();
    }

    private void handleInput() {
        boolean isMoving = keyHandler.isUpPressed() || keyHandler.isDownPressed() ||
                keyHandler.isLeftPressed() || keyHandler.isRightPressed();

        if (isMoving) {
            if (keyHandler.isUpPressed()) {
                direction = Direction.UP;
            } else if (keyHandler.isDownPressed()) {
                direction = Direction.DOWN;
            } else if (keyHandler.isLeftPressed()) {
                direction = Direction.LEFT;
            } else if (keyHandler.isRightPressed()) {
                direction = Direction.RIGHT;
            }
        } else {
            direction = Direction.DEFAULT;
        }
    }

    private void handleMovement() {
        collisionOn = false;
        // Checa colisión con tiles sólidos antes de mover
        float nextX = x;
        float nextY = y;
        switch (direction) {
            case UP -> nextY += speed;
            case DOWN -> nextY -= speed;
            case LEFT -> nextX -= speed;
            case RIGHT -> nextX += speed;
        }
        // Ajusta el punto de colisión al centro del sprite
        float checkX = nextX + GameConfig.TILE_SIZE / 2f;
        float checkY = nextY + GameConfig.TILE_SIZE / 2f;
        if (!game.getTileManager().isTileSolid(checkX, checkY)) {
            x = nextX;
            y = nextY;
        } else {
            collisionOn = true;
        }
    }

    private void handleAnimation() {
        boolean isMoving = direction != Direction.DEFAULT;
        updateAnimation(isMoving);
    }

    private void updateAnimation(boolean isMoving) {
        if (isMoving) {
            animationCounter++;
            if (animationCounter > animationSpeed) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 4;
                } else if (spriteNum == 4) {
                    spriteNum = 1;
                }
                animationCounter = 0;
            }
        } else {
            spriteNum = 1;
        }
    }

    public void pickUpObject(int index) {
        if (index != 999) {
            stars++;
            game.getGameObjects().remove(index);
        }
    }

    public void render(SpriteBatch spriteBatch) {
        Texture currentSprite = getCurrentSprite();
        if (currentSprite != null) {
            spriteBatch.draw(currentSprite, x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
        }
    }

    public void renderAt(SpriteBatch spriteBatch, float x, float y) {
        Texture currentSprite = getCurrentSprite();
        if (currentSprite != null) {
            spriteBatch.draw(currentSprite, x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
        }
    }

    private Texture getCurrentSprite() {
        return switch (direction) {
            case UP -> switch (spriteNum) {
                case 1 -> up1;
                case 2 -> up2;
                case 3 -> up3;
                case 4 -> up4;
                default -> up1;
            };
            case DOWN -> switch (spriteNum) {
                case 1 -> down1;
                case 2 -> down2;
                case 3 -> down3;
                case 4 -> down4;
                default -> down1;
            };
            case LEFT -> switch (spriteNum) {
                case 1 -> left1;
                case 2 -> left2;
                case 3 -> left3;
                case 4 -> left4;
                default -> left1;
            };
            case RIGHT -> switch (spriteNum) {
                case 1 -> right1;
                case 2 -> right2;
                case 3 -> right3;
                case 4 -> right4;
                default -> right1;
            };
            case DEFAULT -> switch (spriteNum) {
                case 1 -> default1;
                case 2 -> default2;
                case 3 -> default3;
                case 4 -> default4;
                default -> default1;
            };
        };
    }

    // Getters
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public Direction getDirection() {
        return direction;
    }
}