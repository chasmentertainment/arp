package com.chasmentertainment.arpg;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Tyler on 6/21/2014.
 */
public class Actor {

    Sprite mSprite;
    int mX, mY;

    public Actor(Sprite sprite) {
        mSprite = sprite;
    }

    public void setX(float newX) {
        mSprite.setX(newX);
    }

    public void setY(float newY) {
        mSprite.setY(newY);
    }



    public void draw(Batch batch) {
        this.draw(batch);
    }

}
