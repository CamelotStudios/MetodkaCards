package com.zerokorez.lepsiametodkamemorycardsov;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.SceneInterface;

import java.util.ArrayList;

public class MenuScene implements SceneInterface {
    private ArrayList<String> imagesDirectories;

    private ButtonManager buttonManager;
    private Button newSessionButton;
    private Button createGroupButton;
    private Button managerButton;
    private Button settingsButton;
    private Button quitButton;

    private boolean fadeOn;

    private Rect quitRect;

    private String quitText;
    private Paint quitTextPaint;
    private Rect quitTextRect;

    private ButtonManager quitButtonManager;
    private Button quitYesButton;
    private Button quitNoButton;

    private String comingSoonText;
    private Paint comingSoonTextPaint;

    private ButtonManager comingSoonButtonManager;
    private Button comingSoonOkButton;
    private boolean comingSoonFadeOn;

    private boolean onTouch;
    private boolean updateTested;

    public MenuScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("title_screen.png");
        imagesDirectories.add("locked_menu_scene.png");
        imagesDirectories.add("notification_scroller.png");

        float top = Global.HEIGHT * 9f / 15f;
        float x = Global.WIDTH / 5f;
        float y = Global.HEIGHT / 55f;
        float space = 17f/1920f*Global.HEIGHT;
        float plus = ((646f/1920f*Global.HEIGHT - Global.HEIGHT/55f*15f)-(17f/1920f*Global.HEIGHT*4f))/5f;

        buttonManager = new ButtonManager(Constants.C4F_PAINT);
        newSessionButton = new Button(buttonManager, new Rect((int) x, (int) top, (int) (Global.WIDTH - x), (int) (top + y * 3 + plus)), "New Session");
        createGroupButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 3 + space + plus), (int) (Global.WIDTH - x), (int) (top + y * 6 + space + plus*2)), "Create Group");
        managerButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 6 + space*2 + plus*2), (int) (Global.WIDTH - x), (int) (top + y * 9 + space*2 + plus*3)), "Manager");
        managerButton.setLocked(true, "locked_menu_scene.png");
        settingsButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 9 + space*3 + plus*3), (int) (Global.WIDTH - x), (int) (top + y * 12 + space*3 + plus*4)), "Settings");
        quitButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 12 + space*4 + plus*4), (int) (Global.WIDTH - x), (int) (top + y * 15 + space*4 + plus*5)), "Quit");

        fadeOn = false;

        float left = Global.WIDTH/5f;
        top = Global.HEIGHT/3f + y*11/20f;
        x = Global.WIDTH*3/45f;
        y = Global.HEIGHT/48f;

        quitRect = new Rect((int) left, (int) top, (int) (left + x*9), (int) (top + y*8));

        quitText = "Are you sure?";
        quitTextRect = new Rect((int)left, (int) (top + y),(int) (left + x*9),(int) (top + y*3));
        quitTextPaint = new Paint();
        quitTextPaint.setColor(Constants.WHITE);
        quitTextPaint.setTextSize(Constants.getTextSize(quitTextRect, quitTextPaint, Global.getLanguage(this.quitText)));

        quitButtonManager = new ButtonManager(Constants.C4F_PAINT);
        quitYesButton = new Button(quitButtonManager, new Rect((int) (left + x), (int) (top + y*45/10f), (int) (left + x*4), (int) (top + y*65/10f)), "Yes");
        quitNoButton = new Button(quitButtonManager, new Rect((int) (left + x*5), (int) (top + y*45/10f), (int) (left + x*8), (int) (top + y*65/10f)), "No");

        comingSoonText = "Coming soon!";
        comingSoonTextPaint = new Paint();
        comingSoonTextPaint.setColor(Constants.WHITE);
        comingSoonTextPaint.setTextSize(Constants.getTextSize(quitTextRect, comingSoonTextPaint, comingSoonText));

        comingSoonButtonManager = new ButtonManager(Constants.C4F_PAINT);
        comingSoonOkButton = new Button(comingSoonButtonManager, new Rect((int) (left + x*3), (int) (top + y*45/10f), (int) (left + x*6), (int) (top + y*65/10f)), "Ok");
        comingSoonFadeOn = false;

        onTouch = false;
        updateTested = true;
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
        if (!updateTested) {
            //Constants.ACTIVITY.requestPermissions(MainScreenActivity.PERMISSIONS, MainScreenActivity.REQUEST_CODE);
            updateTested = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.SCENE_MANAGER.amActive(this)) {
            canvas.drawColor(Constants.C5F);

            Constants.drawImage(canvas, imagesDirectories.get(0), Constants.FULL_SCREEN_RECT);

            buttonManager.draw(canvas);

            if (fadeOn) {
                quitButton(canvas);
            }
            if (comingSoonFadeOn) {
                comingSoonButton(canvas);
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if(newSessionButton.receiveTouch(event)) {
            Constants.SCENE_MANAGER.add(new NewSessionScene());
        } else if(createGroupButton.receiveTouch(event)) {
            Constants.SCENE_MANAGER.add(new CreateButtonScene());
        } else if (managerButton.receiveTouch(event)) {
            buttonManager.setState(false);
            comingSoonFadeOn = true;
        } else if (settingsButton.receiveTouch(event)) {
            Constants.SCENE_MANAGER.add(new MainSettingsScene());
        } else if(comingSoonFadeOn) {
            if(comingSoonOkButton.receiveTouch(event)) {
                buttonManager.setState(true);
                comingSoonFadeOn = false;
            }
            if (cancelFade(event)) {
                buttonManager.setState(true);
                comingSoonFadeOn = false;
            }
        }
        if(quitButton.receiveTouch(event)) {
            buttonManager.setState(false);
            fadeOn = true;
        } else if(fadeOn) {
            if(quitYesButton.receiveTouch(event)) {
                ((Activity) Global.CONTEXT).finish();
                System.exit(0);
            }
            if(quitNoButton.receiveTouch(event)) {
                buttonManager.setState(true);
                fadeOn = false;
            }
            if (cancelFade(event)) {
                buttonManager.setState(true);
                fadeOn = false;
            }
        }
    }

    public boolean cancelFade(MotionEvent event) {
        float[] point = new float[]{event.getX(), event.getY(),};
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                if (!quitRect.contains((int) point[0], (int) point[1]) && onTouch) {
                    onTouch = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (!quitRect.contains((int) point[0], (int) point[1])) {
                    onTouch = true;
                }
                break;
        }
        return false;
    }

    public void quitButton(Canvas canvas) {
        canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
        canvas.drawRect(quitRect, Constants.C5F_PAINT);
        canvas.drawRect(quitRect, Constants.WHITE_BORDER_PAINT);

        Constants.drawTextCenter(canvas, quitTextRect, quitTextPaint, Global.getLanguage(this.quitText));
        quitButtonManager.draw(canvas);
    }

    public void comingSoonButton(Canvas canvas) {
        canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
        canvas.drawRect(quitRect, Constants.C5F_PAINT);
        canvas.drawRect(quitRect, Constants.WHITE_BORDER_PAINT);

        Constants.drawTextCenter(canvas, quitTextRect, comingSoonTextPaint, comingSoonText);
        comingSoonButtonManager.draw(canvas);
    }
}
