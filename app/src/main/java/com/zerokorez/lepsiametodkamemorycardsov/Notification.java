package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Interactive;
import com.zerokorez.textparser.Window;

public class Notification implements Drawable, Interactive {
    private boolean state;
    private NotificationManager manager;
    private long putTime;
    private int timeLimit;

    private Window window;
    private Rect windowRect;
    private Rect fadeRect;

    private Rect notificationRect;
    private float touchY;
    private boolean onTouch;
    private boolean onClick;

    public Notification(NotificationManager manager, String phrase, String variable, int time) {
        state = true;
        this.manager = manager;
        putTime = System.currentTimeMillis();
        timeLimit = time;

        onTouch = false;
        onClick = false;
        touchY = 0;

        window = new Window(Global.getLanguage(phrase) + variable + "<>´V~18;C;#black;B;I", Global.WIDTH*6/8);
        window.update();
        windowRect = window.getCanvasRect();
        fadeRect = new Rect(0, windowRect.top - Global.WIDTH/12, Global.WIDTH, windowRect.bottom + Global.WIDTH/12);
        notificationRect = new Rect(Global.WIDTH - Global.WIDTH/16, fadeRect.top, Global.WIDTH, fadeRect.bottom);
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (state && !Global.SELECTED) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (onTouch) {
                        onTouch = false;
                        onClick = true;
                    }
                    manager.setPermY(manager.getMoveY() + manager.getPermY());
                    manager.setMoveY(0);
                    touchY = 0;
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (Global.moveRect(notificationRect, 0, (int) (manager.getMoveY() + manager.getPermY())).contains((int) x, (int) y)) {
                        touchY = y;
                        onTouch = true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (onTouch) {
                        manager.setMoveY(y - touchY);
                    }
                default:
                    onClick = false;
            }
            Global.SELECTED = onTouch || onClick;
        }
        return onTouch;
    }

    @Override
    public void draw(Canvas canvas) {
        update();
        int movement = (int) (manager.getPermY() + manager.getMoveY());
        Rect fadeRect = Global.moveRect(this.fadeRect, 0, movement);
        Rect notificationRect = Global.moveRect(this.notificationRect, 0, movement);
        canvas.drawRect(fadeRect, Constants.FADE_PAINT);
        canvas.drawRect(Global.moveRect(this.windowRect, 0, movement), Constants.WHITE_BORDER_PAINT);
        Constants.drawImage(canvas, "notification_scroller.png", notificationRect);
        if (!state) {
            canvas.drawRect(notificationRect, Constants.FADE_PAINT);
        }
        canvas.drawRect(fadeRect, Constants.WHITE_BORDER_PAINT);
        window.setMoveY((float) movement);
        window.draw(canvas);
    }

    @Override
    public void update() {
        if (Global.WAITING) {
            state = false;
        }
        if (state) {
            float permY = manager.getPermY();
            float moveY = manager.getMoveY();

            if (permY + moveY < -getFadeRect().top) {
                if (permY < -getFadeRect().top) {
                    manager.setPermY(-getFadeRect().top);
                    manager.setMoveY(0);
                } else {
                    manager.setMoveY(moveY - ((permY + moveY) - (-getFadeRect().top)));
                }
            } else if (permY + moveY > Global.HEIGHT - getFadeRect().bottom) {
                if (permY > Global.HEIGHT - getFadeRect().bottom) {
                    manager.setPermY(Global.HEIGHT - getFadeRect().bottom);
                    manager.setMoveY(0);
                } else {
                    manager.setMoveY(moveY - ((permY + moveY) - (Global.HEIGHT - getFadeRect().bottom)));
                }
            }
        }
    }

    public long getPutTime() {
        return putTime;
    }
    public int getTimeLimit() {
        return timeLimit;
    }
    public Rect getFadeRect() {
        return fadeRect;
    }
}
