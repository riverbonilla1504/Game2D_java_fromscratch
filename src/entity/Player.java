package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    GamePanel gameP;
    KeyHandler keyH;

    public Player(GamePanel gameP, KeyHandler keyH) {
        this.gameP = gameP;
        this.keyH = keyH;
        setDefaultValues();
        getEntityImages();
        solidArea = new Rectangle(8, 16, 32, 32);
    }

    @Override
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "default";
    }

    @Override
    public void getEntityImages() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_up3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_up4.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_down3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_down4.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_left3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_left4.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_right3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_right4.png"));
            default1 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_default1.png"));
            default2 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_default2.png"));
            default3 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_default3.png"));
            default4 = ImageIO.read(getClass().getResourceAsStream("/main/assets/sprite_player_default4.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// player movement
    @Override
    public void update() {

        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){
        if (keyH.upPressed == true) {

            direction = "up";

        }
        if (keyH.downPressed == true) {
            
            direction = "down";
        }
        if (keyH.leftPressed == true) {
            
            direction = "left";
        }
        if (keyH.rightPressed == true) {
            
            direction = "right";
        }
                // Player sprite animation
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 3;
            }
            else if (spriteNum == 3) {
                spriteNum = 4;
            }
            else if (spriteNum == 4) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        CollisionOn = false;
        gameP.collisionChecker.checkTile(this);
        if (CollisionOn == false) {
            switch (direction) {
                case "up": y -= speed; break;
                case "down" : y += speed; break;
                case "left" : x -= speed; break;
                case "right" : x += speed; break;
            }
        }
        System.err.println("CollisionOn: " + CollisionOn);
        } else{
            spriteCounterDefault++;
            if (spriteCounterDefault > 8) {
                if (spriteNumDefault == 1) {
                    spriteNumDefault = 2;
                }
                else if (spriteNumDefault == 2) {
                    spriteNumDefault = 3;
                }
                else if (spriteNumDefault == 3) {
                    spriteNumDefault = 4;
                }
                else if (spriteNumDefault == 4) {
                    spriteNumDefault = 1;
                }
                spriteCounterDefault = 0;
            }
            direction = "default";
        }


    }


    // draw player in the screen
    @Override
    public void draw(Graphics2D g2) {
        BufferedImage img = null;

        switch (direction) {
            case "up" -> {
                if (spriteNum == 1) {
                    img = up1;
                }
                if (spriteNum == 2) {
                    img = up2;
                }
                if (spriteNum == 3) {
                    img = up3;
                }
                if (spriteNum == 4) {
                    img = up4;
                }
            }


            case "down" -> {
                if(spriteNum == 1) {
                    img = down1;
                }
                if (spriteNum == 2) {
                    img = down2;
                }
                if (spriteNum == 3) {
                    img = down3;
                }
                if (spriteNum == 4) {
                    img = down4;
                }
            }


            case "left" -> {
                if(spriteNum == 1) {
                    img = left1;
                }
                if (spriteNum == 2) {
                    img = left2;
                }
                if (spriteNum == 3) {
                    img = left3;
                }
                if (spriteNum == 4) {
                    img = left4;
                }
            }


            case "right" -> {
                if(spriteNum == 1) {
                    img = right1;
                }
                if (spriteNum == 2) {
                    img = right2;
                }
                if (spriteNum == 3) {
                    img = right3;
                }
                if (spriteNum == 4) {
                    img = right4;
                }
            }

            case "default" -> {
                if(spriteNumDefault == 1) {
                    img = default1;
                }
                if (spriteNumDefault == 2) {
                    img = default2;
                }
                if (spriteNumDefault == 3) {
                    img = default3;
                }
                if (spriteNumDefault == 4) {
                    img = default4;
                }
            }
        }

        g2.drawImage(img, x, y, gameP.tileSize, gameP.tileSize, null);
    }
}