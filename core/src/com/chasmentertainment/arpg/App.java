package com.chasmentertainment.arpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chasmentertainment.arpg.com.chasmentertainment.arpg.util.SpriteDef;

public class App extends ApplicationAdapter
{

    SpriteBatch blackDevilSpriteBatch;

    Animation samusStand;
    Animation samusTurnLeft;
    Animation samusTurnRight;
    Animation currentAnimation;


    float stateTime;

    private int spriteX = 50;
    private int spriteY = 50;

    public void animateRight() {
        currentAnimation = samusTurnRight;
    }

    public void animateStand() {
        currentAnimation = samusStand;
    }

    public void animateLeft() {
        currentAnimation = samusTurnLeft;
    }

    public int spriteXSpeed = 0;
    public int spriteYSpeed = 0;

//  ===============================================================
//  ===============================================================
//  ================================== ApplicationAdapter Overrides

    @Override
	public void create () {

        SpriteDef blackDevilDef = new SpriteDef("characters/blackdevil/spritedef.xml");
        SpriteDef samus = new SpriteDef("characters/samus/spritedef.xml");

        samusStand = samus.getAnimationNamed("standFace");
        samusTurnLeft = samus.getAnimationNamed("turnLeft");
        samusTurnRight = samus.getAnimationNamed("turnRight");

        currentAnimation = samusStand;

        blackDevilSpriteBatch = new SpriteBatch();
        stateTime = 0f;

		MainMenuInputProcessor inputProcessor = new MainMenuInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);
//
//		GL20 gl = Gdx.graphics.getGL20();
//		gl.glClearColor(0.1f, 0.0f, 0.0f, 1);
//		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//

	}
//  ===============================================================
	@Override
	public void render () {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stateTime += Gdx.graphics.getDeltaTime();

//        blackDevilCurrentFrame = blackDevilWalkAnimation.getKeyFrame(stateTime, true);
//        blackDevilCurrentFrame = blackDevilCastAnimation.getKeyFrame(stateTime, true);
        blackDevilSpriteBatch.begin();

//        if (currentAnimation != samusStand && currentAnimation.isAnimationFinished(stateTime)) {
//            currentAnimation = samusStand;
//        }


        blackDevilSpriteBatch.draw(currentAnimation.getKeyFrame(stateTime, true), spriteX+=spriteXSpeed, spriteY+=spriteYSpeed);
//        blackDevilSpriteBatch.draw(samusStandB.getKeyFrame(stateTime, true), spriteX+100, spriteY+100);
        blackDevilSpriteBatch.end();

	}
//  ===============================================================
	@Override
	public void resize(int width, int height) {
        blackDevilSpriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
//  ===============================================================
	@Override
	public void pause() {

	}
//  ===============================================================
	public void resume() {

	}
//  ===============================================================
	public void dispose() {

	}
//  ===============================================================
//  ===============================================================
//  ==================================================== Game Logic
}
