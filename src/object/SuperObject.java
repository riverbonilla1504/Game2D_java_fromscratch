package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class SuperObject {
    public BufferedImage image;
    public String Name;
    public boolean collision=false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public void draw(Graphics2D g2, GamePanel gameP){
        g2.drawImage(image, worldX, worldY, gameP.tileSize, gameP.tileSize, null);
    }
}
