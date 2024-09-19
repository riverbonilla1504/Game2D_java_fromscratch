package main;

import entity.Entity;
import entity.EntityFactory;
import entity.PlayerFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings  
    final int originalTitleSize = 16; // 16x16 pixels
    final int scale = 3; // 3x scale 

    public final int tileSize = originalTitleSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // FPS
    int FPS = 60;
    // Instance of KeyHandler
    KeyHandler keyH = new KeyHandler();

    Thread gameThread;

    // Instance of collision checker
    public CollisionChecker collisionChecker = new CollisionChecker(this);

    // create a new PlayerFactory instance
    EntityFactory playerFactory = new PlayerFactory(this, keyH);

    // create a new Player instance
    Entity player = playerFactory.createEntity();

    // Tile manager
    TileManager tileManager = new TileManager(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.requestFocus();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nexDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nexDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) remainingTime = 0;

                Thread.sleep((long) remainingTime);
                nexDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update(); // update player
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        player.draw(g2); // draw player
        g2.dispose();
    }
}
