package com.game.tile;

import com.badlogic.gdx.graphics.Texture;

/**
 * LibGDX version of Tile class
 * Uses LibGDX Texture instead of BufferedImage for better GPU performance
 */
public class LibGDXTile {
    public Texture texture;
    public boolean collision = false;
}