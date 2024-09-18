package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gameP;
    KeyHandler keyH;

    public Player(GamePanel gameP, KeyHandler keyH) {
        this.gameP = gameP;
        this.keyH = keyH;
        setDefaultValues();
    }
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
    }
    public void update() {

        // Player movement with WASD keys
        if (keyH.upPressed == true) {
            y -= speed;

        }
        if (keyH.downPressed == true) {
            y += speed;
        }
        if (keyH.leftPressed == true) {
            x -= speed;
        }
        if (keyH.rightPressed == true) {
            x += speed;
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, gameP.tileSize, gameP.tileSize);
    }
}