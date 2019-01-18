package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Global;
import com.zerokorez.general.Interactive;

import java.util.ArrayList;

public class NotificationManager implements Drawable, Interactive {
    private Notification active;
    private Notification last;
    private Notification created;
    private ArrayList<Notification> notifications;
    private ArrayList<Notification> elapsed;
    private float moveY;
    private float permY;

    public NotificationManager() {
        active = null;
        last = null;
        created = null;
        notifications = new ArrayList<>();
        elapsed = new ArrayList<>();
        moveY = 0f;
        permY = 0f;
    }

    public void putNotification(String phrase, String variable, int time) {
        notifications.add(0, new Notification(this, phrase, variable, time));
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (active != null) {
            active.receiveTouch(event);
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        long currentTimeMillis = System.currentTimeMillis();
        for (Notification notification : notifications) {
            if (notification.getPutTime() + (long) notification.getTimeLimit() > currentTimeMillis) {
                active = notification;
                created = active;
                notification.update();
                notification.draw(canvas);
                break;
            } else {
                if (!elapsed.contains(notification)) {
                    elapsed.add(notification);
                }
                if (active == notification) {
                    last = active;
                    active = null;
                    permY += moveY;
                    moveY = 0;
                }
            }
        }
        for (Notification notification : elapsed) {
            if (notifications.contains(notification)) {
                notifications.remove(notification);
            }
        }
        elapsed.clear();
    }

    @Override
    public void update() {
        Notification notification;
        if (created != null) {
            notification = created;
            created = null;
        } else if (last != null){
            notification = last;
            last = null;
        } else {
            notification = null;
        }
        if (notification != null) {
            if (permY + moveY < -notification.getFadeRect().top) {
                if (permY < -notification.getFadeRect().top) {
                    setPermY(-notification.getFadeRect().top);
                    setMoveY(0);
                } else {
                    setMoveY(moveY - ((permY + moveY) - (-notification.getFadeRect().top)));
                }
            } else if (permY + moveY > Global.HEIGHT - notification.getFadeRect().bottom) {
                if (permY > Global.HEIGHT - notification.getFadeRect().bottom) {
                    setPermY(Global.HEIGHT - notification.getFadeRect().bottom);
                    setMoveY(0);
                } else {
                    setMoveY(moveY - ((permY + moveY) - (Global.HEIGHT - notification.getFadeRect().bottom)));
                }
            }
        }
    }
    public void setMoveY(float moveY) {
        this.moveY = moveY;
    }
    public float getMoveY() {
        return moveY;
    }
    public void setActive(Notification active) {
        this.active = active;
    }
    public float getPermY() {
        return permY;
    }
    public void setPermY(float permY) {
        this.permY = permY;
    }
}
