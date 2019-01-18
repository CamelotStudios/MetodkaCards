package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.SceneInterface;
import com.zerokorez.storageloader.Group;
import com.zerokorez.textparser.Window;

import java.util.ArrayList;

public class StudyMenuScene implements SceneInterface {
    private ArrayList<String> imagesDirectories;
    private Group group;

    private ButtonManager menuButtonManager;
    private Button menuStudyCardsButton;
    private Button menuCardQuizButton;

    private Rect scroller;
    private Rect board;
    private Rect upperRect;
    private Rect lowerRect;

    private Paint borderPaint;
    private Paint boardPaint;

    private Window window;

    private boolean goingBackY;
    private float upSpeed;
    private float downSpeed;
    private float fps;

    private float movementY;
    private float permanentY;
    private float maxMovementY;
    private float touchHeight;
    private float moveHeight;

    private boolean onTouch;
    private boolean onClick;
    private boolean state;

    private float scrollerSizeRatio;
    private float scrollerMoveRatio;
    private float rectHeight;

    public StudyMenuScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");
        imagesDirectories.add("locked_new_session_scene.png");

        float x = Global.WIDTH/18f;
        float y = Global.HEIGHT/18f;

        menuButtonManager = new ButtonManager(Constants.C4F_PAINT);
        menuStudyCardsButton = new Button(menuButtonManager, new Rect((int) (x/2f), (int) (y + y/3f), (int) (Global.WIDTH/2f - x/4f), (int) (y + y/2f + y*3/2f)), "Study Cards");
        menuCardQuizButton = new Button(menuButtonManager, new Rect((int) (Global.WIDTH/2f + x/4f), (int) (y + y/3f), (int) (Global.WIDTH - x/2f), (int) (y + y/2f + y*3/2f)), "Card Quiz");
        menuButtonManager.setSameFont();

        group = Global.STORAGE_LOADER.getActiveGroup();
        if (group.getCards().size() < 4) {
            menuCardQuizButton.setLocked(true, "locked_new_session_scene.png");
            if (group.getCards().size() == 0) {
                menuStudyCardsButton.setLocked(true, "locked_new_session_scene.png");
            }
        }

        scroller = new Rect((int) (Global.WIDTH - x), (int) (y + y/3f + y*3/2f + x), (int) (Global.WIDTH - x*2/3f), (int) (Global.HEIGHT - x));
        board = new Rect((int) (x), (int) (y + y/3f + y*3/2f + x), (int) (Global.WIDTH - x), (int) (Global.HEIGHT - x));

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Constants.BLACK);
        borderPaint.setStrokeWidth(2);

        boardPaint = new Paint();
        boardPaint.setColor(Constants.WHITE);

        upperRect = new Rect(0, 0, Global.WIDTH, (int) (y + y/3f + y*3/2f + x));
        lowerRect = new Rect(0, (int) (Global.HEIGHT - x), Global.WIDTH, Global.HEIGHT);

        window = new Window("<N~24>" + group.getFullName() + "<V~24;#value;B;C>" + group.getInfoText(), board, x);
        window.update();

        state = true;
        onTouch = false;
        onClick = false;

        goingBackY = false;
        upSpeed = 0;
        downSpeed = 0;
        fps = 10f;

        movementY = 0;
        permanentY = 0;
        maxMovementY = 0;
        touchHeight = 0;
        moveHeight = 0;

        rectHeight = board.height();

        scrollerSizeRatio = rectHeight/window.getColumnHeight();
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        } else if (scrollerSizeRatio > 1) {
            scrollerSizeRatio = 1;
        }
        scrollerMoveRatio = 0;
    }

    @Override
    public ArrayList<String> getImagesDirectories() {
        return imagesDirectories;
    }

    @Override
    public void focusOn() {
        group.setLastStudied();
    }

    @Override
    public void focusOff() {

    }

    @Override
    public void update() {
        window.setMoveY(movementY + permanentY);
        scrollerMoveRatio = -((movementY + permanentY)/window.getColumnHeight());

        float height = window.getColumnHeight() - board.height();
        if (goingBackY) {
            if (permanentY > 0) {
                if (downSpeed < 0) {
                    permanentY = Math.max(0, permanentY + downSpeed);
                    if (permanentY == 0) {
                        goingBackY = false;
                    }
                }
            } else if (height > 0) {
                if (permanentY < -(height) && upSpeed > 0) {
                    permanentY = Math.min(-(height), permanentY + upSpeed);
                    if (permanentY == -(height)) {
                        goingBackY = false;
                    }
                }
            } else if (height < 0) {
                if (permanentY < 0 && upSpeed > 0) {
                    permanentY = Math.min(0, permanentY + upSpeed);
                    if (permanentY == 0) {
                        goingBackY = false;
                    }
                }
            }
        } else if (permanentY <= 0 && permanentY >= -(height)) {
            downSpeed = 0;
            upSpeed = 0;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.SCENE_MANAGER.amActive(this)) {
            canvas.drawColor(Constants.C5F);

            canvas.drawRect(board, boardPaint);
            //canvas.drawRect(scroller, Constants.SCROLLER_BACKGROUND_PAINT);
            canvas.drawRect(new Rect(scroller.left, (int) (scroller.top + scrollerMoveRatio*scroller.height()), scroller.right, (int) ((scroller.bottom-scroller.top)*scrollerSizeRatio + scroller.top + scrollerMoveRatio*scroller.height())), Constants.C3F_PAINT);
            //canvas.drawRect(scroller, borderPaint);

            window.draw(canvas);

            canvas.drawRect(upperRect, Constants.C5F_PAINT);
            canvas.drawRect(lowerRect, Constants.C5F_PAINT);
            canvas.drawRect(board, borderPaint);

            Constants.TAB.draw(canvas);

            menuButtonManager.draw(canvas);
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

        if (menuStudyCardsButton.receiveTouch(event)) {
            Constants.SCENE_MANAGER.add(new StudyCardsScene());
        } else if (menuCardQuizButton.receiveTouch(event)) {
            Constants.SCENE_MANAGER.add(new CardQuizScene());
        }


        if (state && !Global.SELECTED) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (board.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onTouch = false;

                    } else {
                        onClick = false;
                        onTouch = false;
                        maxMovementY = 0;
                    }
                    if (touchHeight != 0) {
                        touchHeight = 0;
                        moveHeight = touchHeight;
                        permanentY += movementY;

                        float height = window.getColumnHeight() - board.height();
                        if (permanentY > 0) {
                            downSpeed = -(permanentY/fps);
                            goingBackY = true;
                        } else if (height > 0) {
                            if (permanentY < -(height)) {
                                upSpeed = -((permanentY+(height))/fps);
                                goingBackY = true;
                            }
                        } else if (height <= 0) {
                            if (permanentY < 0) {
                                upSpeed = -(permanentY/fps);
                                goingBackY = true;
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (board.contains((int) point[0], (int) point[1])) {
                        onTouch = true;

                        touchHeight = point[1];
                        moveHeight = touchHeight;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (board.contains((int) point[0], (int) point[1]) && onTouch) {
                        moveHeight = point[1];
                    } else if (!board.contains((int) point[0], (int) point[1]) && onTouch) {
                        onTouch = false;
                    }
                    break;
            }
            movementY = moveHeight - touchHeight;

            Global.SELECTED = onTouch || onClick;
        }
    }
}
