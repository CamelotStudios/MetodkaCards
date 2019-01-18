package com.zerokorez.lepsiametodkamemorycardsov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.zerokorez.general.Global;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    private SideThread sideThread;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        Global.setUpConstants(context, this);
        Constants.SCENE_MANAGER = new SceneManager();

        sideThread = new SideThread();
        mainThread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        sideThread = new SideThread();
        mainThread = new MainThread(getHolder(), this);

        sideThread.setRunning(true);
        sideThread.start();

        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
        retry = true;
        while(retry) {
            try {
                sideThread.setRunning(false);
                sideThread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Global.SELECTED = false;
        Constants.NOTIFICATOR.receiveTouch(event);
        Constants.SCENE_MANAGER.receiveTouch(event);

        return true;
    }

    public void update() {
        Constants.NOTIFICATOR.update();
        Constants.SCENE_MANAGER.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Constants.SCENE_MANAGER.draw(canvas);

        if (Global.WAITING) {
            canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);

            Rect rect = Global.moveRect(Constants.WAITING_RECT, Global.WIDTH/2 - Constants.WAITING_RECT.width()/2, Global.HEIGHT/2 - Constants.WAITING_RECT.height()/2);
            canvas.drawRect(rect, Constants.C4F_PAINT);
            canvas.drawRect(rect, Constants.WHITE_BORDER_PAINT);

            canvas.drawCircle(Global.WIDTH/2f, Global.HEIGHT/2f, Constants.LOADING_RADIUS, Constants.LOADING_PAINT);

            if (Constants.IS_ADDING) {
                Constants.LOADING_RADIUS = Math.min(Constants.LOADING_UPPER_LIMIT + 1, Constants.LOADING_RADIUS + Constants.LOADING_SPEED);
                if (Constants.LOADING_RADIUS >= Constants.LOADING_UPPER_LIMIT) {
                    Constants.IS_ADDING = false;
                }
            } else {
                Constants.LOADING_RADIUS = Math.max(Constants.LOADING_LOWER_LIMIT - 1, Constants.LOADING_RADIUS - Constants.LOADING_SPEED);
                if (Constants.LOADING_RADIUS <= Constants.LOADING_LOWER_LIMIT) {
                    Constants.IS_ADDING = true;
                }
            }
        }

        Constants.NOTIFICATOR.draw(canvas);
    }
}
