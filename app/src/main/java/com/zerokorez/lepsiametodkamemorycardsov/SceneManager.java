package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.zerokorez.general.SceneInterface;

import java.util.ArrayList;

public class SceneManager {
    private ArrayList<SceneInterface> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE = -1;

    public SceneManager() {
        add(new MenuScene());
    }

    public void add(SceneInterface scene) {
        if (scenes.size() > 0) {
            getActive().focusOff();
        }
        scenes.add(scene);
        ACTIVE_SCENE = scenes.size() - 1;
        (new LoadManager.AsyncLoader()).doInBackground(getActive());
        getActive().focusOn();
        Constants.TAB.getManager().setState(true);
    }

    public void removeActive() {
        if (scenes.size() > 0) {
            getActive().focusOff();
        }
        scenes.remove(ACTIVE_SCENE);
        ACTIVE_SCENE = scenes.size() - 1;
        (new LoadManager.AsyncLoader()).doInBackground();
        if (scenes.size() > 0) {
            getActive().focusOn();
        }
        Constants.TAB.getManager().setState(true);
    }

    public void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

    public  boolean amActive(SceneInterface scene) {
        return scenes.get(ACTIVE_SCENE) == scene;
    }

    public SceneInterface getActive() {
        if (ACTIVE_SCENE >= 0) {
            return scenes.get(ACTIVE_SCENE);
        } else {
            return null;
        }
    }

    public  int getActiveIndex() {
        return ACTIVE_SCENE;
    }
}
