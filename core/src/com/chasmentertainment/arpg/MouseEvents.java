package com.chasmentertainment.arpg;

import java.util.ArrayList;
import org.lwjgl.input.Mouse;

public class MouseEvents {

    public interface Interface {
        public void onMouseChangeClickedState(Event e);
        public void onMouseMove(Event e);
    }

    public enum ButtonState {
        Unclicked,
        Clicked;
    }

    public static class Event {
        public int button;
        public ButtonState buttonState;
        public int x;
        public int y;
    }

    private static ArrayList<ButtonState> mLastButtonState;
    private static int lastX;
    private static int lastY;
    private static int mButtonCount;

    public static void init() {
        mLastButtonState = new ArrayList<ButtonState>();
        mButtonCount = Mouse.getButtonCount();
    }

    public static Event makeEvent(int buttonId, ButtonState state, int mouseX, int mouseY) {
        Event e = new Event();
        e.button = buttonId;
        e.buttonState = state;
        e.x = mouseX;
        e.y = mouseY;

        return e;
    }

    public static void pollInput(Interface target) {
        boolean isButtonStateChanged;

        for (int buttonId=0; buttonId<mButtonCount; buttonId++) {
            
            isButtonStateChanged = false;
            if (Mouse.isButtonDown(buttonId)) {
                if (mLastButtonState.get(buttonId) == ButtonState.Unclicked) {
                    mLastButtonState.set(buttonId, ButtonState.Clicked);
                    isButtonStateChanged = true;
                }
            } else {
                if (mLastButtonState.get(buttonId) == ButtonState.Clicked) {
                    mLastButtonState.set(buttonId, ButtonState.Unclicked);
                    isButtonStateChanged = true;
                }
            }

            Event e = makeEvent(buttonId, mLastButtonState.get(buttonId), Mouse.getX(), Mouse.getY());

            if (isButtonStateChanged) {
                target.onMouseChangeClickedState(e);
            }

            if (lastX != e.x || lastY != e.y) {
                target.onMouseMove(e);
            }

        }
    }

}
