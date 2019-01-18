package com.zerokorez.general;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public interface SceneInterface {
    ArrayList<String> getImagesDirectories();
    void focusOn();
    void focusOff();
    void update();
    void draw(Canvas canvas);
    void receiveTouch(MotionEvent event);
}
