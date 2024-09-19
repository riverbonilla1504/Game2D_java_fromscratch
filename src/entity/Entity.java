package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public int x, y, speed;
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4, default1, default2, default3, default4;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int spriteCounterDefault = 0;
    public int spriteNumDefault = 1;
    public Rectangle solidArea;
    public Boolean CollisionOn = false;

    // method to set the default values of the entity
    public abstract void setDefaultValues();

    // method to get the images of the entity
    public abstract void getEntityImages();

    // method to draw the player
    public abstract void draw(Graphics2D g2);

    // method to update the player
    public abstract void update();
}
