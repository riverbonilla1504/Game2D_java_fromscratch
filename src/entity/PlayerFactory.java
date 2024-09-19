package entity;

import main.GamePanel;
import main.KeyHandler;

public class PlayerFactory implements EntityFactory {
    private GamePanel gameP;
    private KeyHandler keyH;

    public PlayerFactory(GamePanel gameP, KeyHandler keyH) {
        this.gameP = gameP;
        this.keyH = keyH;
    }

    @Override
    public Player createEntity() {
        return new Player(gameP, keyH);  // create a new Player instance
    }
}
