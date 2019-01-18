package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Global;
import com.zerokorez.general.Interactive;

public class SourceCardListBrowser implements Drawable, Interactive {
    private Rect fullRect;
    private Rect browserRect;

    private Paint browserRectPaint;
    private Paint bordersPaint;

    private ButtonManager manager;
    private Button browserButton;

    private String text;
    private Paint textPaint;

    public SourceCardListBrowser() {
        float y = Global.HEIGHT/18f;
        float x = Global.WIDTH/40f;

        browserRectPaint = new Paint();
        browserRectPaint.setColor(Constants.WHITE);
        bordersPaint = new Paint();
        bordersPaint.setColor(Constants.C3F);
        bordersPaint.setStyle(Paint.Style.STROKE);
        bordersPaint.setStrokeWidth(2);

        fullRect = new Rect(0, (int) (y*3), Global.WIDTH, (int) (y*5));
        browserRect = new Rect((int) (fullRect.left + x*2), (int) (fullRect.top + x*2), (int) (fullRect.right - x*2), (int) (fullRect.bottom - x*2));

        manager = new ButtonManager(Constants.TRANSPARENT_PAINT);
        browserButton = new Button(manager, browserRect, "");

        text = "Search groups...";
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(Constants.getTextSize(browserRect, textPaint, text));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(fullRect, Constants.C5F_PAINT);
        canvas.drawRect(browserRect, browserRectPaint);
        canvas.drawRect(browserRect, bordersPaint);
        canvas.drawLine(fullRect.left, fullRect.bottom, fullRect.right, fullRect.bottom, Constants.WHITE_BORDER_PAINT);
        Constants.drawTextCenter(canvas, browserRect, textPaint, text);
        canvas.drawRect(browserRect, Constants.WHITE_BORDER_PAINT);
        manager.draw(canvas);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (browserButton.receiveTouch(event)) {
            Global.showSoftKeyboard();
        }
        return false;
    }
}
