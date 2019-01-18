package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.SceneInterface;
import com.zerokorez.storageloader.Group;

import java.util.ArrayList;

public class NewSessionScene implements SceneInterface {
    private ArrayList<String> imagesDirectories;

    private String titleText;
    private Paint titleTextPaint;

    private GroupButtonList groupButtonList;

    private boolean fadeOn;
    private boolean onTouch;

    private Rect menuRect;

    private ButtonManager menuButtonManager;
    private Button menuStudyCardsButton;
    private Button menuCardQuizButton;

    public NewSessionScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("default.png");
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");
        imagesDirectories.add("notification_scroller.png");

        titleText = "Card Groups";
        titleTextPaint = new Paint();
        titleTextPaint.setColor(Constants.WHITE);
        titleTextPaint.setTextSize(Constants.getTextSize(Constants.TITLE_RECT, titleTextPaint, Global.getLanguage(this.titleText)));

        fadeOn = false;
        onTouch = false;

        float x = Global.WIDTH*3/50f;
        float y = Global.HEIGHT/63f;
        float left = Global.WIDTH/5f;
        float top = Global.HEIGHT*5/14f + y;

        menuRect = new Rect((int) left, (int) top, (int) (left + x*10), (int) (top + y*18 - y*2));

        menuButtonManager = new ButtonManager(Constants.C4F_PAINT);
        menuStudyCardsButton = new Button(menuButtonManager, new Rect((int) (left + x), (int) (top + y*3/2f), (int) (left + x*9), (int) (top + y*14/2f)), "Study Cards");
        menuCardQuizButton = new Button(menuButtonManager, new Rect((int) (left + x), (int) (top + y*17/2f), (int) (left + x*9), (int) (top + y*29/2f)), "Card Quiz");
        menuButtonManager.setSameFont();
    }

    @Override
    public ArrayList<String> getImagesDirectories() {
        return imagesDirectories;
    }

    @Override
    public void focusOn() {
        Global.STORAGE_LOADER.loadGroups();

        groupButtonList = new GroupButtonList();
        for(Group group: Global.GROUPS) {
            new GroupButton(groupButtonList, group);
        }
        if (groupButtonList.getButtons().size() == 0) {
            Constants.NOTIFICATOR.putNotification("No cards group was found.", "", 3000);
        }
    }

    @Override
    public void focusOff() {
    }

    @Override
    public void update() { }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.SCENE_MANAGER.amActive(this)) {
            canvas.drawColor(Constants.C5F);
            groupButtonList.draw(canvas);

            canvas.drawRect(Constants.TITLE_RECT, Constants.C3F_PAINT);
            canvas.drawLine(Constants.TITLE_RECT.left, Constants.TITLE_RECT.bottom, Constants.TITLE_RECT.right, Constants.TITLE_RECT.bottom, Constants.WHITE_BORDER_PAINT);
            Constants.drawTextCenter(canvas, Constants.TITLE_RECT, titleTextPaint, Global.getLanguage(this.titleText));

            Constants.TAB.draw(canvas);

            if (fadeOn) {
                canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
                canvas.drawRect(menuRect, Constants.C5F_PAINT);
                canvas.drawRect(menuRect, Constants.WHITE_BORDER_PAINT);

                menuButtonManager.draw(canvas);
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (Constants.TAB.getExit().receiveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
        } else if (Constants.TAB.getSettings().receiveTouch(event)) {
            Constants.SCENE_MANAGER.add(new MainSettingsScene());
        } else if (Constants.TAB.getInfo().receiveTouch(event)) {
            Constants.NOTIFICATOR.putNotification("Coming soon!", "", 2000);
        }

        if(groupButtonList.receiveTouch(event)) {
            Constants.SCENE_MANAGER.add(new StudyMenuScene());
        }
    }

    public boolean cancelFade(MotionEvent event) {
        float[] point = new float[]{event.getX(), event.getY(),};
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                if (!menuRect.contains((int) point[0], (int) point[1]) && onTouch) {
                    onTouch = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (!menuRect.contains((int) point[0], (int) point[1])) {
                    onTouch = true;
                }
                break;
        }
        return false;
    }
}
