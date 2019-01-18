package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Interactive;

public class Button implements Drawable, Interactive {
    private ButtonManager buttonManager;
    private String chainedDirectory;
    private Rect rectangle;

    private String text;
    private Paint paint;

    private Float moveY;
    private Float maxMoveY;
    private Float moveYLimit;

    private boolean onTouch;
    private boolean onHover;
    private boolean onClick;

    private boolean isLocked;
    private boolean isCorrect;
    private boolean isWrong;

    public Button(ButtonManager buttonManager, Rect rectangle, String text) {
        this.buttonManager = buttonManager;
        buttonManager.add(this);

        this.rectangle = rectangle;
        this.text = text;

        onTouch = false;
        onHover = false;
        onClick = false;

        moveY = 0f;
        maxMoveY = 0f;
        moveYLimit = Global.HEIGHT/72f;

        isLocked = false;
        isCorrect = false;
        isWrong = false;

        paint = new Paint();
        paint.setColor(buttonManager.getTextColor());
        paint.setTextSize(Constants.getTextSize(this.rectangle, paint, Global.getLanguage(this.text)));
    }

    public void setMoveY(Float moveY) {
        this.moveY = moveY;
    }

    public void setMaxMoveY(Float maxMoveY) {
        this.maxMoveY = maxMoveY;
    }

    public Paint getPaint() {
        return paint;
    }

    public String getText() {
        return text;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked, String chainedDirectory) {
        this.isLocked = isLocked;
        this.chainedDirectory = chainedDirectory;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setIsWrong(boolean isWrong) {
        this.isWrong = isWrong;
    }

    public void resetText(String text) {
        this.text = text;
        paint.setTextSize(Constants.getTextSize(rectangle, paint, Global.getLanguage(this.text)));
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (buttonManager.getState() && !Global.SELECTED && !isLocked) {
            Rect rectangle = Global.moveRect(this.rectangle, 0, moveY.intValue());
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (rectangle.contains((int) point[0], (int) point[1]) && onTouch && (maxMoveY < moveYLimit)) {
                        onClick = true;
                        onHover = false;
                        onTouch = false;
                    } else {
                        onClick = false;
                        onHover = false;
                        onTouch = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (rectangle.contains((int) point[0], (int) point[1]) && (maxMoveY < moveYLimit)) {
                        onTouch = true;
                        onHover = true;
                    } else {
                        onHover = false;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (rectangle.contains((int) point[0], (int) point[1]) && onTouch && (maxMoveY < moveYLimit)) {
                        onHover = true;
                    } else {
                        onHover = false;
                        onTouch = false;
                    }
                default:
                    onClick = false;
                    break;
            }
            Global.SELECTED = onTouch || onHover || onClick;
            return onClick;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rectangle = Global.moveRect(this.rectangle, 0, moveY.intValue());
        canvas.drawRect(rectangle.left, rectangle.top, rectangle.right, rectangle.bottom, buttonManager.getBasePaint());
        if(onHover) {
            canvas.drawRect(rectangle, Constants.SHADE_PAINT);
        }
        canvas.drawRect(rectangle, Constants.WHITE_BORDER_PAINT);

        Constants.drawTextCenter(canvas, rectangle, paint, Global.getLanguage(this.text));

        if (isLocked) {
            Constants.drawImage(canvas, chainedDirectory, rectangle);
        }
        if (isCorrect) {
            canvas.drawRect(rectangle, Constants.CORRECT_PAINT);
        } else if (isWrong) {
            canvas.drawRect(rectangle, Constants.WRONG_PAINT);
        }
    }

    @Override
    public void update() {
    }
}
