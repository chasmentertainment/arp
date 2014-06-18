package com.chasmentertainment.arpg.AnimationParser;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 4/26/2014.
 */
public class AnimationFactory {


    /*  Create an animation by specifying a list of rectangles inside of a texture.
     */
    public static Animation makeAnimation(Texture t, List<Rectangle> frameList) {
        TextureRegion[] regions = new TextureRegion[frameList.size()];

        Rectangle frame;
        for (int i = 0; i < frameList.size(); i++) {
            frame = frameList.get(i);
            regions[i] = new TextureRegion(t, frame.x, frame.y, frame.width, frame.height);
        }

        Animation anim = new Animation(0.125f, regions);
        return anim;
    }
}
