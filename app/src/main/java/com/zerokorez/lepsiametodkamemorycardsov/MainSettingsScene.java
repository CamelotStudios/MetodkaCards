package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.SceneInterface;

import java.util.ArrayList;

public class MainSettingsScene implements SceneInterface {
    private ArrayList<String> imagesDirectories;

    private String titleText;
    private Paint titleTextPaint;

    private SettingsCardList cardList;

    private String comingSoonText;
    private Paint comingSoonTextPaint;
    private Rect comingSoonTextRect;
    private Rect comingSoonRect;

    private ButtonManager comingSoonButtonManager;
    private Button comingSoonOkButton;
    private boolean comingSoonFadeOn;

    private boolean onTouch;

    public MainSettingsScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("locked_settings_card.png");

        titleText = "Settings";
        titleTextPaint = new Paint();
        titleTextPaint.setColor(Constants.WHITE);
        titleTextPaint.setTextSize(Constants.getTextSize(Constants.TITLE_RECT, titleTextPaint, Global.getLanguage(this.titleText)));

        cardList = new SettingsCardList();
        cardList.addCard("Quiz&Study");
        cardList.getSettingsCard("Quiz&Study").setLocked(true, imagesDirectories.get(0));
        cardList.addCard("Appearance");
        cardList.getSettingsCard("Appearance").setLocked(true, imagesDirectories.get(0));
        cardList.addCard("Sounds");
        cardList.getSettingsCard("Sounds").setLocked(true, imagesDirectories.get(0));
        cardList.addCard("Locals");
        cardList.getSettingsCard("Locals").setLocked(true, imagesDirectories.get(0));
        cardList.addCard("Internet");
        cardList.getSettingsCard("Internet").setLocked(true, imagesDirectories.get(0));
        cardList.addCard("Credits");
        cardList.getSettingsCard("Credits").setLocked(true, imagesDirectories.get(0));
        cardList.addCard("Info");
        cardList.getSettingsCard("Info").setLocked(true, imagesDirectories.get(0));
        cardList.setSameFontSize();

        float left = Global.WIDTH/5f;
        float top = Global.HEIGHT/3f + (Global.HEIGHT / 55f)*11/20f;
        float x = Global.WIDTH*3/45f;
        float y = Global.HEIGHT/48f;

        comingSoonRect = new Rect((int) left, (int) top, (int) (left + x*9), (int) (top + y*8));
        comingSoonTextRect = new Rect((int)left, (int) (top + y),(int) (left + x*9),(int) (top + y*3));
        comingSoonText = "Coming soon!";
        comingSoonTextPaint = new Paint();
        comingSoonTextPaint.setColor(Constants.WHITE);
        comingSoonTextPaint.setTextSize(Constants.getTextSize(comingSoonTextRect, comingSoonTextPaint, Global.getLanguage(this.comingSoonText)));

        comingSoonButtonManager = new ButtonManager(Constants.C4F_PAINT);
        comingSoonOkButton = new Button(comingSoonButtonManager, new Rect((int) (left + x*3), (int) (top + y*45/10f), (int) (left + x*6), (int) (top + y*65/10f)), "Ok");
        comingSoonFadeOn = false;

        onTouch = false;
    }

    @Override
    public ArrayList<String> getImagesDirectories() {
        return imagesDirectories;
    }

    @Override
    public void focusOn() {
    }

    @Override
    public void focusOff() {
    }

    @Override
    public void update() {
        cardList.update();
    }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.SCENE_MANAGER.amActive(this)) {
            canvas.drawColor(Constants.C4F);
            cardList.draw(canvas);

            Rect titleRect = Global.moveRect(Constants.TITLE_RECT, 0, -Global.HEIGHT / 18);
            canvas.drawRect(titleRect, Constants.C3F_PAINT);
            canvas.drawLine(titleRect.left, titleRect.bottom, titleRect.right, titleRect.bottom, Constants.WHITE_BORDER_PAINT);
            Constants.drawTextCenter(canvas, titleRect, titleTextPaint, Global.getLanguage(this.titleText));

            if (comingSoonFadeOn) {
                comingSoonButton(canvas);
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        cardList.receiveTouch(event);
        if (cardList.getSettingsCard("Quiz&Study").receiveTouch(event) ||
            cardList.getSettingsCard("Appearance").receiveTouch(event) ||
            cardList.getSettingsCard("Sounds").receiveTouch(event) ||
            cardList.getSettingsCard("Locals").receiveTouch(event) ||
            cardList.getSettingsCard("Internet").receiveTouch(event) ||
            cardList.getSettingsCard("Credits").receiveTouch(event) ||
            cardList.getSettingsCard("Info").receiveTouch(event)) {
            comingSoonFadeOn = true;
            cardList.setState(false);
        }
        if(comingSoonFadeOn) {
            if (comingSoonOkButton.receiveTouch(event) || cancelFade(event)) {
                comingSoonFadeOn = false;
                cardList.setState(true);
            }
        }
    }

    public void comingSoonButton(Canvas canvas) {
        canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
        canvas.drawRect(comingSoonRect, Constants.C5F_PAINT);
        canvas.drawRect(comingSoonRect, Constants.WHITE_BORDER_PAINT);

        Constants.drawTextCenter(canvas, comingSoonTextRect, comingSoonTextPaint, Global.getLanguage(this.comingSoonText));
        comingSoonButtonManager.draw(canvas);
    }

    public boolean cancelFade(MotionEvent event) {
        if (!Global.SELECTED && !Global.WAITING) {
            float[] point = new float[]{event.getX(), event.getY(),};
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (!comingSoonRect.contains((int) point[0], (int) point[1]) && onTouch) {
                        onTouch = false;
                        Global.SELECTED = true;
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (!comingSoonRect.contains((int) point[0], (int) point[1])) {
                        onTouch = true;
                        Global.SELECTED = true;
                    }
                    break;
            }
        }
        return false;
    }
}
