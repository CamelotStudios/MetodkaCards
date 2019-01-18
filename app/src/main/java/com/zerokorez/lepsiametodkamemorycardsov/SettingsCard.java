package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Interactive;

public class SettingsCard implements Drawable, Interactive {
    private SettingsCardList manager;
    private String text;
    private Paint textPaint;
    private float moveY;

    private boolean isLocked;
    private String chainedDirectory;

    private boolean onTouch;
    private boolean onClick;
    private boolean onHover;

    public SettingsCard(SettingsCardList settingsCardList, String text) {
        manager = settingsCardList;
        this.text = text;
        moveY = 0;

        textPaint = new Paint();
        textPaint.setColor(Constants.WHITE);
        textPaint.setTextSize(Constants.getTextSize(manager.getTextRect(), textPaint, Global.getLanguage(this.text)));

        isLocked = false;
        onClick = false;
        onTouch = false;
        onHover = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked, String directory) {
        isLocked = locked;
        chainedDirectory = directory;
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect cardRect = Global.moveRect(manager.getCardRect(), 0, (int) moveY);
        Rect textRect = Global.moveRect(manager.getTextRect(), 0, (int) moveY);
        float x = Global.WIDTH/40f;
        if (!Global.compareStrings(manager.getOrder().get(0), text)) {
            canvas.drawLine(x * 5, cardRect.top, x * 34, cardRect.top, Constants.WHITE_BORDER_PAINT);
        }
        if (!Global.compareStrings(manager.getOrder().get(manager.getOrder().size()-1), text)) {
            canvas.drawLine(x * 5, cardRect.bottom, x * 34, cardRect.bottom, Constants.WHITE_BORDER_PAINT);
        }
        Constants.drawTextCenter(canvas, textRect, textPaint, Global.getLanguage(this.text));
        if (onHover) {
            canvas.drawRect(new Rect((int) (cardRect.left + x*3), cardRect.top, (int) (cardRect.right - x*3), cardRect.bottom), Constants.SHADE_PAINT);
        }
        if (isLocked) {
            Constants.drawImage(canvas, chainedDirectory, cardRect);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (manager.getState() && !Global.SELECTED) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();
            Rect rect = Global.moveRect(manager.getCardRect(), 0, (int) moveY);

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (rect.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onTouch = false;
                        onHover = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (rect.contains((int) point[0], (int) point[1])) {
                        onTouch = true;
                        onHover = true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (!rect.contains((int) point[0], (int) point[1]) || !onTouch || manager.getMaxMovementY() > Global.HEIGHT/72f) {
                        onTouch = false;
                        onHover = false;
                    }
                default:
                    onClick = false;
                    break;
            }
            Global.SELECTED = onClick || onTouch;
            if (Math.abs(manager.getMaxMovementY()) < Global.HEIGHT/72f) {
                return onClick;
            }
        }
        return false;
    }

    public float getMoveY() {
        return moveY;
    }

    public void setMoveY(float moveY) {
        this.moveY = moveY;
    }
}
