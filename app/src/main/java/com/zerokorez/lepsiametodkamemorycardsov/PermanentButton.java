package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Interactive;

public class PermanentButton implements Drawable, Interactive {
    private PermanentButtonManager buttonManager;
    private String chainedDirectory;
    private Rect fullRect;
    private Rect realRect;

    private String text;
    private Paint paint;

    private boolean onTouch;
    private boolean onHover;
    private boolean onClick;

    private boolean isSelected;
    private boolean notAvailable;

    public PermanentButton(PermanentButtonManager manager, Rect rectangle, String string) {
        buttonManager = manager;
        buttonManager.add(this);

        fullRect = rectangle;
        realRect = new Rect(rectangle.left + manager.getOdd(), rectangle.top + manager.getOdd(), rectangle.right - manager.getOdd(), rectangle.bottom);
        text = string;

        onTouch = false;
        onHover = false;
        onClick = false;

        isSelected = false;
        notAvailable = false;

        paint = new Paint();
        paint.setColor(buttonManager.getTextColor());
        paint.setTextSize(Constants.getTextSize(realRect, paint, Global.getLanguage(this.text)));
    }

    public Paint getPaint() {
        return paint;
    }

    public String getText() {
        return text;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public boolean getNotAvailable() {
        return notAvailable;
    }

    public void setNotAvailable(boolean notAvailable, String chainedDirectory) {
        this.notAvailable = notAvailable;
        this.chainedDirectory = chainedDirectory;
    }

    public void resetText(String text) {
        this.text = text;
        paint.setTextSize(Constants.getTextSize(realRect, paint, Global.getLanguage(this.text)));
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (buttonManager.getState() && !Global.SELECTED) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (fullRect.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onHover = false;
                        onTouch = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (fullRect.contains((int) point[0], (int) point[1]) && !isSelected) {
                        onTouch = true;
                        onHover = true;
                    } else {
                        onHover = false;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (fullRect.contains((int) point[0], (int) point[1]) && onTouch) {
                        onHover = true;
                    } else {
                        onHover = false;
                        onTouch = false;
                    }
                default:
                    onClick = false;
                    break;
            }
            Global.SELECTED = onClick || onHover || onTouch;
            return onClick;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(fullRect, buttonManager.getBackgroundPaint());
        if (isSelected) {
            canvas.drawRect(realRect, buttonManager.getSelectedPaint());
        } else {
            canvas.drawRect(realRect, buttonManager.getDeselectedPaint());
        }
        if(onHover && !isSelected) {
            canvas.drawRect(realRect, Constants.SHADE_PAINT);
        }
        drawBorders(canvas);

        Constants.drawTextCenter(canvas, realRect, paint, Global.getLanguage(this.text));

        if (notAvailable) {
            Constants.drawImage(canvas, chainedDirectory, realRect);
        }
    }
    private void drawBorders(Canvas canvas) {
        Paint paint = buttonManager.getBordersPaint();
        canvas.drawLine(realRect.left, realRect.top, realRect.right, realRect.top, paint);
        canvas.drawLine(realRect.left, realRect.top, realRect.left, realRect.bottom, paint);
        canvas.drawLine(realRect.right, realRect.top, realRect.right, realRect.bottom, paint);

        if (isSelected) {
            canvas.drawLine(fullRect.left, fullRect.bottom, realRect.left, fullRect.bottom, paint);
            canvas.drawLine(realRect.right, realRect.bottom, fullRect.right, fullRect.bottom, paint);
        } else {
            canvas.drawLine(fullRect.left, fullRect.bottom, fullRect.right, fullRect.bottom, paint);
        }
    }

    @Override
    public void update() {
    }
}
