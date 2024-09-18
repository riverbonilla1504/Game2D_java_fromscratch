package main;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
public class GamePanel extends JPanel implements Runnable {
    // Screen settings  
    final int originalTitleSize = 16; // 16x16 pixels
    final int scale = 3; // 3x scale 

    public final int tileSize = originalTitleSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // FPS
    int FPS = 60;
    // instance of KeyHandler
    KeyHandler keyH = new KeyHandler();

    Thread gameThread;

    //instance of player
    Player player = new Player(this, keyH);




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
        //Sleep method to control the frame rate


        double drawInterval = 1000000000/FPS; //FPS 1 second // interval between each frame
        double nexDrawTime = System.nanoTime() + drawInterval; // get current time in nanoseconds
        while (gameThread != null) {

            update(); // Update game state


            repaint(); // Render game state
            try {
                double remainingTime = nexDrawTime - System.nanoTime(); // Calculate remaining time 
                remainingTime = remainingTime / 1000000; // Convert to milliseconds
                System.out.println("FPS: " + 1000/remainingTime); // Print frame rate
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) (remainingTime)); // Sleep for remaining time

                nexDrawTime += drawInterval; // Calculate next draw time
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

            //print frame rate dymamically
           
        }
    }
    public void update() {
        player.update();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        g2.dispose();


    }
}