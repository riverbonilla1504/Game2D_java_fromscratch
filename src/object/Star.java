package object;

import main.ResourceManager;

/**
 * Star object that can be collected by the player
 */
public class Star extends SuperObject {

    public Star() {
        setName("Star");
        try {
            image = ResourceManager.getInstance().loadObject("star1");
        } catch (Exception e) {
            System.err.println("Error loading star image: " + e.getMessage());
            e.printStackTrace();
        }
        setCollision(true);
    }
}
