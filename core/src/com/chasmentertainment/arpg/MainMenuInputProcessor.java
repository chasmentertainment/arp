package com.chasmentertainment.arpg;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.chasmentertainment.arpg.networking.NetworkingTester;

public class MainMenuInputProcessor implements InputProcessor {
    protected App app;

    private boolean[] keysDown = new boolean[256];

    public MainMenuInputProcessor(App context) {
        app = context;
    }

    private void updateAnimations() {
        app.spriteXSpeed = 0;
        app.spriteYSpeed = 0;

        if (keysDown[Input.Keys.W]) {
            app.spriteYSpeed = 2;
        } else if (keysDown[Input.Keys.A]) {
            app.spriteXSpeed = -2;
            app.animateLeft();
        } else if (keysDown[Input.Keys.S]) {
            app.spriteYSpeed = -2;
        } else if (keysDown[Input.Keys.D]) {
            app.spriteXSpeed = 2;
            app.animateRight();
        } else {

            app.animateStand();
        }
    }

    @Override
    public boolean keyDown (int keycode) {
        keysDown[keycode] = true;

        if (keycode == Input.Keys.ENTER) {
            try {
//                NetworkingTester.test();
//                NetworkingTester.testReqRep();
//                NetworkingTester.testPubSub();
//                NetworkingTester.testPipeline();
//                NetworkingTester.testExclusivePair();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateAnimations();

        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        keysDown[keycode] = false;

        updateAnimations();
        return false;
    }

    @Override
    public boolean keyTyped (char character) {
      return false;
   }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
      return false;
   }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
      return false;
   }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
      return false;
   }

//   @Override
//   public boolean touchMoved (int x, int y) {
//      return false;
//   }

    @Override
    public boolean scrolled (int amount) {
      return false;
   }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
      return false;
   }
}
