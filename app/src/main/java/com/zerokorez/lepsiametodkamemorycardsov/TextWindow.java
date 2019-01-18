package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Global;
import com.zerokorez.textparser.Window;

public class TextWindow implements Drawable {
    private Window window;
    private Rect rect;
    private Paint borderPaint;

    private float moveX;
    private Rect textRect;
    private float x;
    private Rect scrollerRect;

    private float columnHeight;
    private float rectHeight;

    private float scrollerSizeRatio;
    private float scrollerMoveRatio;

    public TextWindow(Rect rect, float x, float top) {
        this.rect = rect;
        moveX = 0;

        borderPaint = new Paint();
        borderPaint.setColor(Constants.C2F);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        textRect = new Rect(rect.left, rect.top, rect.right, rect.bottom);

        this.x = x;

        scrollerRect = new Rect((int) (Global.WIDTH - x*4), (int) (top + x*8), (int) (Global.WIDTH - x*7/2f), (int) (Global.HEIGHT - x*18));
    }

    public void setMoveX(float moveX) {
        this.moveX = moveX;
        window.setMoveX(moveX);
    }

    public void setText(String text) {
        window = new Window(text, textRect, x);

        columnHeight = window.getColumnHeight();
        rectHeight = rect.height();

        scrollerSizeRatio = rectHeight/columnHeight;
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        } else if (scrollerSizeRatio > 1) {
            scrollerSizeRatio = 1;
        }
        scrollerMoveRatio = 0;
    }

    public Rect getRect() {
        return rect;
    }

    public float getColumnHeight() {
        return columnHeight;
    }

    public float getRectHeight() {
        return rectHeight;
    }

    public void setScrollerMoveRatio(float moveY) {
        window.setMoveY(moveY);
        scrollerMoveRatio = -(moveY/columnHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        window.draw(canvas);
    }
    public void drawBorders(Canvas canvas) {
        Rect rect = Global.moveRect(this.rect, (int) moveX, 0);
        canvas.drawRect(rect, borderPaint);
        canvas.drawLine(rect.right, rect.top, rect.right + x/2, rect.top, borderPaint);
        canvas.drawLine(rect.right, rect.bottom, rect.right + x/2, rect.bottom, borderPaint);
    }
    public void drawScroller(Canvas canvas) {
        canvas.drawRect(Global.moveRect(scrollerRect, (int) moveX, 0), Constants.SCROLLER_BACKGROUND_PAINT);
        canvas.drawRect(Global.moveRect(new Rect(scrollerRect.left, (int) (scrollerRect.top + scrollerMoveRatio*scrollerRect.height()), scrollerRect.right, (int) ((scrollerRect.bottom-scrollerRect.top)*scrollerSizeRatio + scrollerRect.top + scrollerMoveRatio*scrollerRect.height())), (int) moveX, 0), Constants.C4F_PAINT);
    }

    @Override
    public void update() {
        window.update();
    }

    public void recycle() {
        window.recycle();
    }
}
