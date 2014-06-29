package com.chasmentertainment.arpg;

import com.badlogic.gdx.graphics.g2d.Sprite;

/*
    A Graphic is a game object that can be drawn into a Gamespace
 */
public class ScreenGraphic {

    Sprite mSprite;
    int mX, mY;

    public ScreenGraphic(Sprite sprite) {
        mSprite = sprite;
    }

    public void display(Gamespace g) {
        mSprite.draw(g.getBatch());
    }
}
