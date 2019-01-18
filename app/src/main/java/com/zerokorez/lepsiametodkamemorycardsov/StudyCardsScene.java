package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.SceneInterface;

import java.util.ArrayList;

public class StudyCardsScene implements SceneInterface {
    private ArrayList<String> imagesDirectories;

    private CardButton cardButton;

    private ButtonManager buttonManager;
    private ImageButton backButton;
    private ImageButton starButton;
    private ImageButton nextButton;

    private Rect bottomRect;

    public StudyCardsScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("left_arrow.png");
        imagesDirectories.add("white_star.png");
        imagesDirectories.add("yellow_star.png");
        imagesDirectories.add("right_arrow.png");
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");

        float x = Global.WIDTH/33f;
        float top = Global.HEIGHT - x*11;
        float right = Global.WIDTH - x*2;

        buttonManager = new ButtonManager(Constants.C5F_PAINT);
        backButton = new ImageButton(buttonManager, new Rect((int) (x*2), (int) top, (int) (right - x*20), (int) (top + x*9)), imagesDirectories.get(0));
        starButton = new ImageButton(buttonManager, new Rect((int) (x*12), (int) top, (int) (right - x*10), (int) (top + x*9)), imagesDirectories.get(1));
        nextButton = new ImageButton(buttonManager, new Rect((int) (x*22), (int) top, (int) right, (int) (top + x*9)), imagesDirectories.get(3));

        cardButton = new CardButton(Global.STORAGE_LOADER.getActiveGroup(), starButton);

        bottomRect = new Rect((int) (x*2), (int) top, (int) right, (int) (top + x*9));
    }

    @Override
    public ArrayList<String> getImagesDirectories() {
        return imagesDirectories;
    }

    public void setNumber(int number) {
        cardButton.setNumber(number);
    }

    @Override
    public void focusOn() {
        cardButton.getCard().getGroup().setLastStudied();
    }

    @Override
    public void focusOff() {
    }

    @Override
    public void update() {
        if (cardButton.getCard().getGroup().isToBeSaved()) {
            Global.STORAGE_LOADER.saveUserData(cardButton.getCard().getGroup());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.SCENE_MANAGER.amActive(this)) {
            canvas.drawColor(Constants.C5F);

            cardButton.draw(canvas);

            Constants.TAB.draw(canvas);
            canvas.drawRect(bottomRect, Constants.C4F_PAINT);
            canvas.drawRect(bottomRect, Constants.WHITE_BORDER_PAINT);
            buttonManager.draw(canvas);
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

        cardButton.receiveTouch(event);

        if (backButton.receiveTouch(event)) {
            cardButton.nextNumber(false);
        }
        if (starButton.receiveTouch(event)) {
            cardButton.getCard().toggleIsKnown();
            updateStarButton();
        }
        if (nextButton.receiveTouch(event)) {
            cardButton.nextNumber(true);
        }
    }

    public void updateStarButton() {
        if (cardButton.getCard().isKnown()) {
            starButton.changeImage(imagesDirectories.get(2));
        } else {
            starButton.changeImage(imagesDirectories.get(1));
        }
    }
}
