package entity;

import main.GamePanel;
import main.KeyHandler;

public class PlayerFactory implements EntityFactory {
    private GamePanel gameP;
    private KeyHandler keyH;

    // PlayerFactory constructor
    public PlayerFactory(GamePanel gameP, KeyHandler keyH) {
        this.gameP = gameP;
        this.keyH = keyH;
    }

    // createEntity() method is implemented from the EntityFactory interface
    @Override
    public Player createEntity() {
        return new Player(gameP, keyH);  // create a new Player instance
    }
}
