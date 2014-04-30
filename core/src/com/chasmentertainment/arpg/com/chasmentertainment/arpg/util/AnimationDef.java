package com.chasmentertainment.arpg.com.chasmentertainment.arpg.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 4/26/2014.
 */
public class AnimationDef {

    ArrayList<StillFrame> imageFrames = new ArrayList<StillFrame>();

    public AnimationDef() {

    }

    public AnimationDef(List<StillFrame> frameList) {
        for (StillFrame r : frameList) {
            imageFrames.add(r);
        }
    }

    public void addFrame(StillFrame r) {
        imageFrames.add(r);
    }
    public void addFrames(List<StillFrame> r) {
        imageFrames.addAll(r);
    }

    public TextureRegion[] getTextureRegions(Texture t) {
        TextureRegion[] regions = new TextureRegion[imageFrames.size()];

        for (int i = 0; i < imageFrames.size(); i++) {
            StillFrame frame = imageFrames.get(i);
            regions[i] = new TextureRegion(t, frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
        }

        return regions;
    }

    public int countFrames() {
        return imageFrames.size();
    }

    public static Animation makeAnimation(Texture t, AnimationDef def) {
        Animation anim = new Animation(0.125f, def.getTextureRegions(t));
        return anim;
    }
}
