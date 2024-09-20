package main;

import object.Star;

public class AssetSetter {
    GamePanel gameP;

    // AssetSetter constructor
    public AssetSetter(GamePanel gameP){
        this.gameP = gameP;
    }
    

    // setObject() method
    public void setObject(){
        gameP.obj[0] = new Star();
        gameP.obj[0].worldX = 8 * gameP.tileSize;
        gameP.obj[0].worldY = 8 * gameP.tileSize;

    }
}
