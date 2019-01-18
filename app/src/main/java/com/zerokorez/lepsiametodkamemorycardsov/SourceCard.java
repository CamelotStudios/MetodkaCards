package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Global;
import com.zerokorez.internetloader.Source;

public class SourceCard implements Drawable {
    private Source source;
    private String note;

    private Rect fullRect;
    private Rect nameRect;
    private Rect noteRect;

    private Paint namePaint;
    private Paint notePaint;

    private Float Y;
    private Float moveY;
    private int height;

    private ButtonManager manager;
    private Button mainButton;
    private Button removeButton;

    public SourceCard(Source source, String state, Float Y) {
        this.source = source;
        note = state;
        this.Y = Y;

        float y = Global.HEIGHT/72f;
        float x = Global.WIDTH/40f;
        moveY = 0f;
        nameRect = new Rect((int) (x*2), (int) (y*20), (int) (Global.WIDTH - x*2), (int) (y*26));

        namePaint = new Paint();
        namePaint.setColor(Constants.WHITE);
        namePaint.setTextSize(Constants.getTextSize(nameRect, namePaint, source.getPackageName()));
        notePaint = new Paint();

        manager = new ButtonManager(Constants.C3F_PAINT);
        Rect mainRect = new Rect((int) (nameRect.left + y*5), (int) (y*29 + Y), (int) (nameRect.right - 5*y), (int) (33*y + Y));
        Rect removeRect = new Rect((int) (nameRect.left + y*5), (int) (y*35 + Y), (int) (nameRect.right - 5*y), (int) (39*y + Y));
        Rect mainNote = new Rect(nameRect.left, (int) (y*36), nameRect.right, (int) (y*39));
        Rect removeNote = new Rect(nameRect.left, (int) (y*42), nameRect.right, (int) (y*45));

        if (state == "Not downloaded yet") {
            mainButton = new Button(manager, mainRect, "Download");
            removeButton = null;
            noteRect = mainNote;
            if (source.isAvailable()) {
                notePaint.setColor(Color.GRAY);
            } else {
                note = "Unavailable";
                notePaint.setColor(Color.RED);
                mainButton.setLocked(true, "locked_create_button_scene.png");
            }
            notePaint.setTextSize(Constants.getTextSize(noteRect, notePaint, Global.getLanguage(this.note)));
        } else if (state == "Customized") {
            mainButton = new Button(manager, mainRect, "Restore");
            removeButton = new Button(manager, removeRect, "Remove");
            noteRect = removeNote;
            if (source.isAvailable()) {
                notePaint.setColor(Color.BLUE);
            } else {
                note = "Unavailable";
                notePaint.setColor(Color.RED);
                mainButton.setLocked(true, "locked_create_button_scene.png");
            }
            notePaint.setTextSize(Constants.getTextSize(noteRect, notePaint, Global.getLanguage(this.note)));
        } else if (state == "Up-to-date") {
            removeButton = new Button(manager, mainRect, "Remove");
            mainButton = null;
            noteRect = mainNote;
            if (source.isAvailable()) {
                notePaint.setColor(Color.GREEN);
            } else {
                note = "Unavailable";
                notePaint.setColor(Color.RED);
            }
            notePaint.setTextSize(Constants.getTextSize(noteRect, notePaint, Global.getLanguage(this.note)));
        } else if (state == "Outdated") {
            mainButton = new Button(manager, mainRect, "Update");
            removeButton = new Button(manager, removeRect, "Remove");
            noteRect = removeNote;
            if (source.isAvailable()) {
            } else {
                note = "Unavailable";
                mainButton.setLocked(true, "locked_create_button_scene.png");
            }
            notePaint.setColor(Color.RED);
            notePaint.setTextSize(Constants.getTextSize(noteRect, notePaint, Global.getLanguage(this.note)));
        }

        fullRect = new Rect(nameRect.left, nameRect.top, noteRect.right, noteRect.bottom);
        height = fullRect.height();
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect fullRect = Global.moveRect(this.fullRect, 0, moveY.intValue() + Y.intValue());
        Rect nameRect = Global.moveRect(this.nameRect, 0, moveY.intValue() + Y.intValue());
        Rect noteRect = Global.moveRect(this.noteRect, 0, moveY.intValue() + Y.intValue());

        canvas.drawRect(fullRect, Constants.C4F_PAINT);

        canvas.drawRect(nameRect, Constants.C3F_PAINT);
        canvas.drawRect(nameRect, Constants.WHITE_BORDER_PAINT);

        //canvas.drawRect(noteRect, Constants.C4F_PAINT);
        canvas.drawRect(noteRect, Constants.WHITE_BORDER_PAINT);

        canvas.drawRect(fullRect, Constants.WHITE_BORDER_PAINT);

        manager.draw(canvas);

        Constants.drawTextCenter(canvas, nameRect, namePaint, source.getPackageName());
        Constants.drawTextCenter(canvas, noteRect, notePaint, Global.getLanguage(this.note));
    }

    @Override
    public void update() {

    }

    public void setMoveY(Float moveY) {
        this.moveY = moveY;
        if (mainButton != null) {
            mainButton.setMoveY(moveY);
        }
        if (removeButton != null) {
            removeButton.setMoveY(moveY);
        }
    }

    public void setMaxMoveY(Float maxMoveY) {
        if (mainButton != null) {
            mainButton.setMaxMoveY(maxMoveY);
        }
        if (removeButton != null) {
            removeButton.setMaxMoveY(maxMoveY);
        }
    }

    public boolean receiveMainTouch(MotionEvent event) {
        if (mainButton != null) {
            return mainButton.receiveTouch(event);
        }
        return false;
    }
    public boolean receiveRemoveTouch(MotionEvent event) {
        if (removeButton != null) {
            return removeButton.receiveTouch(event);
        }
        return false;
    }

    public Paint getNamePaint() {
        return namePaint;
    }

    public String getNote() {
        return note;
    }

    public Source getSource() {
        return source;
    }
}
