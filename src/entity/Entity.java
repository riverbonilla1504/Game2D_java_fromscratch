package entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y, speed;

    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4, default1, default2, default3, default4;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public int spriteCounterDefault = 0;
    public int spriteNumDefault = 1;

}
