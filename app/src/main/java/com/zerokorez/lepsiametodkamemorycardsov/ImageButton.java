package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Interactive;

public class ImageButton implements Drawable, Interactive {
    private ButtonManager buttonManager;
    private Rect rectangle;
    private String directory;

    private boolean onTouch = false;
    private boolean onHover = false;
    private boolean onClick = false;

    public ImageButton(ButtonManager buttonManager, Rect rectangle, String imageDirectory) {
        this.buttonManager = buttonManager;
        buttonManager.add(this);
        this.rectangle = rectangle;
        directory = imageDirectory;
    }

    public void changeImage(String imageDirectory) {
        this.directory = imageDirectory;
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (buttonManager.getState() && !Global.SELECTED) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (rectangle.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onHover = false;
                        onTouch = false;
                    } else {
                        onTouch = false;
                        onHover = false;
                        onClick = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (rectangle.contains((int) point[0], (int) point[1])) {
                        onTouch = true;
                        onHover = true;
                    } else {
                        onTouch = false;
                        onHover = false;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (rectangle.contains((int) point[0], (int) point[1]) && onTouch) {
                        onHover = true;
                    } else {
                        onHover = false;
                        onTouch = false;
                    }
                default:
                    onClick = false;
                    break;
            }
            Global.SELECTED = onClick || onTouch || onHover;
            return onClick;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Constants.drawImage(canvas, directory, rectangle);
        if(onHover) {
            canvas.drawRect(rectangle, Constants.SHADE_PAINT);
        }
    }

    @Override
    public void update() {
    }
}
