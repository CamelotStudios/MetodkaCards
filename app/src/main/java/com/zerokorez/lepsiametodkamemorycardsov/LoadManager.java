package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import com.zerokorez.general.Global;
import com.zerokorez.general.SceneInterface;

import java.util.*;

public class LoadManager {
    private String imagesDirectory = "drawable/";
    private HashMap<String,Bitmap> loadedImages;

    public LoadManager() {
        if (Global.WIDTH > 720 || Global.HEIGHT > 1280) {
            imagesDirectory += "r1080/";
        } else {
            imagesDirectory += "r720/";
        }
        loadedImages = new HashMap<>();
    }

    //****GETTERS*SETTERS*****
    public Bitmap getImage(String imageDirectory) {
        if (loadedImages.containsKey(imageDirectory)) {
            return loadedImages.get(imageDirectory);
        }
        return null;
    }

    public void setUpConstants() {
        Constants.RANDOM = new Random(System.currentTimeMillis());
        Constants.NOTIFICATOR = new NotificationManager();

        Constants.TAB = new Tab();

        Constants.FULL_SCREEN_RECT = new Rect(0, 0, Global.WIDTH, Global.HEIGHT);
        Constants.TITLE_RECT = new Rect(0, Global.HEIGHT/18, Global.WIDTH, Global.HEIGHT/6);
        Constants.LIST_ITEM_RECT = new Rect(Global.WIDTH/40, 0, Global.WIDTH*38/40, Global.HEIGHT/6);
        Constants.PARAGRAPH_BOUNDS = new Rect();
        Constants.WAITING_RECT = new Rect(0, 0, Global.WIDTH/4, Global.WIDTH/4);

        Constants.TRANSPARENT_PAINT = new Paint();
        Constants.TRANSPARENT_PAINT.setAlpha(0);

        Constants.FADE_PAINT = new Paint();
        Constants.FADE_PAINT.setColor(Constants.BLACK);
        Constants.FADE_PAINT.setAlpha(208);

        Constants.C2F_PAINT = new Paint();
        Constants.C2F_PAINT.setColor(Constants.C2F);

        Constants.C3F_PAINT = new Paint();
        Constants.C3F_PAINT.setColor(Constants.C3F);

        Constants.C4F_PAINT = new Paint();
        Constants.C4F_PAINT.setColor(Constants.C4F);

        Constants.C5F_PAINT = new Paint();
        Constants.C5F_PAINT.setColor(Constants.C5F);

        Constants.CORRECT_PAINT = new Paint();
        Constants.CORRECT_PAINT.setColor(Constants.GREEN);
        Constants.CORRECT_PAINT.setAlpha(128);

        Constants.WRONG_PAINT = new Paint();
        Constants.WRONG_PAINT.setColor(Constants.RED);
        Constants.WRONG_PAINT.setAlpha(128);

        Constants.WHITE_BORDER_PAINT = new Paint();
        Constants.WHITE_BORDER_PAINT.setColor(Constants.WHITE);
        Constants.WHITE_BORDER_PAINT.setStyle(Paint.Style.STROKE);
        Constants.WHITE_BORDER_PAINT.setStrokeWidth(2);

        Constants.BLACK_BORDER_PAINT = new Paint();
        Constants.BLACK_BORDER_PAINT.setColor(Constants.BLACK);
        Constants.BLACK_BORDER_PAINT.setStyle(Paint.Style.STROKE);
        Constants.BLACK_BORDER_PAINT.setStrokeWidth(2);

        Constants.SHADE_PAINT = new Paint();
        Constants.SHADE_PAINT.setColor(Constants.WHITE);
        Constants.SHADE_PAINT.setAlpha(64);

        Constants.SCROLLER_BACKGROUND_PAINT = new Paint();
        Constants.SCROLLER_BACKGROUND_PAINT.setColor(Constants.C7F);

        Constants.LOADING_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        Constants.LOADING_PAINT.setStyle(Paint.Style.STROKE);
        Constants.LOADING_PAINT.setColor(Constants.WHITE);
        Constants.LOADING_PAINT.setStrokeWidth(Global.WIDTH/40f);

        Constants.LOADING_LOWER_LIMIT = Global.WIDTH/120f;
        Constants.LOADING_UPPER_LIMIT = Global.WIDTH/12f;
        Constants.LOADING_RADIUS = Constants.LOADING_LOWER_LIMIT;
        Constants.LOADING_SPEED = (Constants.LOADING_UPPER_LIMIT-Constants.LOADING_LOWER_LIMIT)/30f;
        Constants.IS_ADDING = true;
    }

    public boolean addImage(String imageDirectory) {
        if (loadedImages.containsKey(imageDirectory)) {
            if (loadedImages.get(imageDirectory) != null) {
                if (!loadedImages.get(imageDirectory).isRecycled()) {
                    return true;
                } else {
                    return loadImage(imageDirectory);
                }
            } else {
                return loadImage(imageDirectory);
            }
        } else {
            return loadImage(imageDirectory);
        }
    }

    public void manageImages() {
        Collection<String> collection = new ArrayList<>();
        loadedImages.keySet().addAll(collection);

        for (String imageDirectory : collection) {
            if (!Constants.SCENE_MANAGER.getActive().getImagesDirectories().contains(imageDirectory)) {
                loadedImages.get(imageDirectory).recycle();
            }
        }
    }

    public boolean loadImage(String imageDirectory) {
        try {
            loadedImages.put(imageDirectory, BitmapFactory.decodeStream(Global.ASSET_MANAGER.open(imagesDirectory + imageDirectory)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class AsyncLoader extends AsyncTask<SceneInterface, Void, Void> {
        @Override
        protected Void doInBackground(SceneInterface... scenes) {
            if (Constants.SCENE_MANAGER != null) {
                for (String directory : Constants.SCENE_MANAGER.getActive().getImagesDirectories()) {
                    Global.METODKA_LOADER.addImage(directory);
                }
            }
            return null;
        }
    }
}