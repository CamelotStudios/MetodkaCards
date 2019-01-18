package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.storageloader.Card;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Interactive;

public class ResultButton implements Drawable, Interactive {
    private ResultButtonList list;
    private Card card;

    private String indexText;
    private Rect indexTextRect;
    private Paint indexTextPaint;

    private String valueText;
    private Rect valueTextRect;
    private Paint valueTextPaint;

    private boolean onTouch;
    private boolean onClick;

    private String imageDirectory;
    private Rect imageRect;

    private float additionalY;

    public ResultButton(ResultButtonList resultButtonList, Card card, boolean result) {
        list = resultButtonList;
        list.add(this);
        this.card = card;

        onTouch = false;
        onClick = false;

        float left = Global.WIDTH/40f;
        float right = Global.WIDTH*38/40f;
        float y = Global.HEIGHT/18f;

        indexText = card.getIndex();
        indexTextRect = new Rect((int) (left + y*3), 0, (int) right, (int) y);
        indexTextPaint = new Paint();
        indexTextPaint.setColor(Constants.WHITE);
        indexTextPaint.setTextSize(Constants.getTextSize(indexTextRect, indexTextPaint, indexText));

        valueText = card.getValue();
        valueTextRect = new Rect((int) (left + y*3), (int) y, (int) right, (int) (y*3));
        valueTextPaint = new Paint();
        if (result) {
            valueTextPaint.setColor(Constants.GREEN);
        } else {
            valueTextPaint.setColor(Constants.RED);
        }
        valueTextPaint.setTextSize(Constants.getTextSize(valueTextRect, valueTextPaint, valueText));

        imageRect = new Rect((int) left, 0, (int) (left + y*3), (int) (y*3));
        if (result) {
            imageDirectory = "tick_result_button.png";
        } else {
            imageDirectory = "cross_result_button.png";
        }
        additionalY = 0;
    }

    public Card getCard() {
        return card;
    }

    public float getTop() {
        return Constants.LIST_ITEM_RECT.top + additionalY;
    }

    public float getBottom() {
        return Constants.LIST_ITEM_RECT.bottom + additionalY;
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (list.getState() && !Global.SELECTED) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();
            Rect rect = Global.moveRect(Constants.LIST_ITEM_RECT, 0, (int) additionalY);

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (rect.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onTouch = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (rect.contains((int) point[0], (int) point[1])) {
                        onTouch = true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (!rect.contains((int) point[0], (int) point[1]) || !onTouch) {
                        onTouch = false;
                    }
                default:
                    onClick = false;
                    break;
            }
            Global.SELECTED = onClick || onTouch;
            if (Math.abs(list.getMovementY()) < Global.HEIGHT/48f) {
                return onClick;
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
    }

    public void draw(Canvas canvas, float newY) {
        additionalY = newY;
        Rect rect = Global.moveRect(Constants.LIST_ITEM_RECT, 0, (int) additionalY);
        canvas.drawRect(rect, Constants.C4F_PAINT);
        canvas.drawRect(rect, Constants.WHITE_BORDER_PAINT);

        Constants.drawTextCenter(canvas, Global.moveRect(new Rect(indexTextRect.left, indexTextRect.top, indexTextRect.right, indexTextRect.bottom + Global.HEIGHT/36), 0, (int) additionalY), indexTextPaint, indexText);
        Constants.drawTextCenter(canvas, Global.moveRect(valueTextRect, 0, (int) additionalY), valueTextPaint, valueText);

        rect = Global.moveRect(imageRect, 0, (int) additionalY);
        canvas.drawRect(rect, Constants.C3F_PAINT);

        float x = Global.HEIGHT/48f;
        Constants.drawImage(canvas, imageDirectory, new Rect((int) (rect.left + x), (int) (rect.top + x), (int) (rect.right - x), (int) (rect.bottom - x)));
        canvas.drawRect(rect, Constants.WHITE_BORDER_PAINT);
    }

    @Override
    public void update() {
    }

    public Paint getIndexTextPaint() {
        return indexTextPaint;
    }

    public Paint getValueTextPaint() {
        return valueTextPaint;
    }
}
