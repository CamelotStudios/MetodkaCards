package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Global;

public class Tab implements Drawable {
    private Rect rectangle;

    private ButtonManager manager;
    private ImageButton exit;
    private ImageButton settings;
    private ImageButton info;

    public Tab() {
        rectangle = new Rect(0, 0, Global.WIDTH, Global.HEIGHT/18);

        float y = Global.HEIGHT/18f;

        manager = new ButtonManager(Constants.C2F_PAINT);
        exit = new ImageButton(manager, new Rect((int) (Global.WIDTH - y), 0, Global.WIDTH, Global.HEIGHT/18), "exit.png");
        settings = new ImageButton(manager, new Rect((int) (Global.WIDTH - y*2), 0, (int) (Global.WIDTH - y), Global.HEIGHT/18), "settings.png");
        info = new ImageButton(manager, new Rect((int) (Global.WIDTH - y*3), 0, (int) (Global.WIDTH - y*2), Global.HEIGHT/18), "info.png");
    }

    public ImageButton getExit() {
        return exit;
    }

    public ImageButton getSettings() {
        return settings;
    }

    public ImageButton getInfo() {
        return info;
    }

    public ButtonManager getManager() {
        return manager;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rectangle, Constants.C2F_PAINT);
        canvas.drawLine(rectangle.left, rectangle.bottom, rectangle.right, rectangle.bottom, Constants.WHITE_BORDER_PAINT);
        manager.draw(canvas);
    }

    @Override
    public void update() {
    }
}
