package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.SceneInterface;
import com.zerokorez.internetloader.Source;

import java.util.ArrayList;

public class CreateButtonScene implements SceneInterface {
    private ArrayList<String> imagesDirectories;
    private com.zerokorez.internetloader.LoadManager internetLoader;
    private com.zerokorez.storageloader.LoadManager storageLoader;
    private boolean isLocalNotInternet;

    private Float y;

    private PermanentButton localButton;
    private PermanentButton internetButton;
    private PermanentButtonManager buttonManager;

    private SourceCardList sourceCardList;
    private Rect comingSoonRect;

    public CreateButtonScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");
        imagesDirectories.add("locked_create_button_scene.png");
        imagesDirectories.add("locked_browser.png");
        imagesDirectories.add("coming_soon.png");
        imagesDirectories.add("notification_scroller.png");

        internetLoader = Global.INTERNET_LOADER;
        storageLoader = Global.STORAGE_LOADER;
        isLocalNotInternet = true;

        y = Global.HEIGHT / 18f;

        buttonManager = new PermanentButtonManager(Constants.C5F_PAINT, Constants.C4F_PAINT, Constants.WHITE_BORDER_PAINT, Constants.C3F_PAINT, Global.WIDTH / 48);
        localButton = new PermanentButton(buttonManager, new Rect(0, y.intValue(), Global.WIDTH / 2 + Global.WIDTH / 96, (int) (y * 3)), "Local");
        internetButton = new PermanentButton(buttonManager, new Rect(Global.WIDTH / 2 - Global.WIDTH / 96, y.intValue(), Global.WIDTH, (int) (y * 3)), "Network");
        buttonManager.setSameFont();
        buttonManager.setDefaultSelected(0);

        sourceCardList = null;
        comingSoonRect = new Rect(Global.WIDTH/8, Global.HEIGHT/2 - 101, Global.WIDTH*7/8, Global.HEIGHT/2 + 101);
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
        if (Global.isNetworkAvailable()) {
            if (internetButton.getNotAvailable()) {
                internetButton.setNotAvailable(false, "");
            }
            if (!buttonManager.getState()) {
                buttonManager.setState(true);
            }
            if (!isLocalNotInternet) {
                if (sourceCardList == null) {
                    ArrayList<Source> sources = internetLoader.readInternetDatabase();
                    if (sources != null) {
                        if (sources.size() > 0) {
                            sourceCardList = new SourceCardList(sources);
                            sourceCardList.setSameFontSize();
                        }
                    }
                }
            }
        } else {
            if (!localButton.getIsSelected()) {
                buttonManager.setDefaultSelected(0);
            }
            if (buttonManager.getState()) {
                buttonManager.setState(false);
            }
            if (!internetButton.getNotAvailable()) {
                internetButton.setNotAvailable(true, "locked_create_button_scene.png");
            }
            if (!isLocalNotInternet) {
                isLocalNotInternet = true;
            }
            if (sourceCardList != null) {
                sourceCardList = null;
            }
        }
        if (isLocalNotInternet) {
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.SCENE_MANAGER.amActive(this)) {
            canvas.drawColor(Constants.C5F);

            if (!isLocalNotInternet) {
                if (sourceCardList != null) {
                    sourceCardList.draw(canvas);
                }
            } else {
                Constants.drawImage(canvas, "coming_soon.png", comingSoonRect);
            }

            Constants.TAB.draw(canvas);
            buttonManager.draw(canvas);
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (!Global.WAITING && !Global.SELECTED) {
            if (Constants.TAB.getExit().receiveTouch(event)) {
                Constants.SCENE_MANAGER.removeActive();
            } else if (Constants.TAB.getSettings().receiveTouch(event)) {
                Constants.SCENE_MANAGER.add(new MainSettingsScene());
            } else if (Constants.TAB.getInfo().receiveTouch(event)) {
                Constants.NOTIFICATOR.putNotification("Coming soon!", "", 2000);
            }

            PermanentButton active = buttonManager.receiveTouch(event);
            if (active != null) {
                isLocalNotInternet = (active == localButton);
            }

            if (!isLocalNotInternet) {
                if (sourceCardList != null) {
                    sourceCardList.receiveTouch(event);
                }
            }
        }
    }
}
