package com.chasmentertainment.arpg;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;

import java.util.ArrayList;
import java.util.Collection;

/*
    A Gamespace is composed of Actor objects.
 */
public class Gamespace {
    private final Collection<Actor> mActors;
    private final Collection<PartyMember> mCharacters;
//    private final Collection<MapObject> mMapObjects = new ArrayList<MapObject>();
    private Batch mBatch;

    public Gamespace () {
        mActors = new ArrayList<Actor>();
        mCharacters = new ArrayList<PartyMember>();
    }

    public void addActor(Actor actor) {
        mActors.add(actor);
    }

    public void removeActor(Actor actor) {
        mActors.remove(actor);
    }

    public void loop() {
        Batch drawBatch = new SpriteBatch();
        drawBatch.begin();

        for (Actor g : mActors) {
            g.draw(drawBatch);
        }

        drawBatch.end();
    }

    public void onCreate() {

    }

    public Batch getBatch() {
        return mBatch;
    }

    public void addCharacter(PartyMember character) {
        mCharacters.add(character);
    }

//    public void addMapObject(MapObject object) {
//        mMapObjects.add(object);
//    }
}
