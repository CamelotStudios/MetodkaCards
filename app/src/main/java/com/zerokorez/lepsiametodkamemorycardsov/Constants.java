package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.zerokorez.general.Global;
import com.zerokorez.storageloader.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Constants {
    public static SceneManager SCENE_MANAGER;
    public static Random RANDOM;
    public static NotificationManager NOTIFICATOR;

    public static int BLACK = Color.BLACK;
    public static int WHITE = Color.rgb(205, 205, 205);
    public static int RED = Color.rgb(160, 0, 0);
    public static int GREEN = Color.rgb(0, 128, 0);

    public static int C11F = Color.rgb(255, 121, 121);
    public static int C10F;
    public static int C9F;
    public static int C8F;
    public static int C7F = Color.rgb(224, 77, 77);
    public static int C6F;
    public static int C5F = Color.rgb(160, 55, 55);
    public static int C4F = Color.rgb(128, 44, 44);
    public static int C3F = Color.rgb(96, 33, 33);
    public static int C2F = Color.rgb(64, 22, 22);
    public static int C1F;

    public static Tab TAB;

    public static Rect FULL_SCREEN_RECT;
    public static Rect TITLE_RECT;
    public static Rect LIST_ITEM_RECT;
    public static Rect PARAGRAPH_BOUNDS;
    public static Rect WAITING_RECT;

    public static Paint FADE_PAINT;
    public static Paint SHADE_PAINT;
    public static Paint C2F_PAINT;
    public static Paint C3F_PAINT;
    public static Paint C4F_PAINT;
    public static Paint C5F_PAINT;
    public static Paint WHITE_BORDER_PAINT;
    public static Paint CORRECT_PAINT;
    public static Paint WRONG_PAINT;
    public static Paint SCROLLER_BACKGROUND_PAINT;
    public static Paint TRANSPARENT_PAINT;
    public static Paint BLACK_BORDER_PAINT;

    public static Paint LOADING_PAINT;
    public static Float LOADING_RADIUS;
    public static Float LOADING_UPPER_LIMIT;
    public static Float LOADING_LOWER_LIMIT;
    public static Float LOADING_SPEED;
    public static Boolean IS_ADDING;

    public static float getTextSize(Rect rect, Paint paint, String string) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(rect.height());

        float width = paint.measureText(string);
        float height = paint.getFontMetrics().descent-paint.getFontMetrics().ascent;

        if(width < rect.width() && height < rect.height()) {
            return paint.getTextSize();
        } else {
            return 4 * (paint.getTextSize() / Math.max(width / (float) rect.width(), height / (float) rect.height())) / 5f;
        }
    }
    public static void drawTextCenter(Canvas canvas, Rect rect, Paint paint, String string) {
        paint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();

        paint.getTextBounds(string, 0, string.length(), bounds);
        float x = rect.left + rect.width()/2f - bounds.width()/2f;

        paint.getTextBounds("0", 0, 1, bounds);
        float y = rect.top + rect.height()/2f + bounds.height()/2f;

        canvas.drawText(string, x, y, paint);
    }
    public static void drawTextRight(Canvas canvas, Rect rect, Paint paint, String string, float odd) {
        paint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();

        paint.getTextBounds(string, 0, string.length(), bounds);
        float x = rect.right - odd - bounds.width();

        paint.getTextBounds("0", 0, 1, bounds);
        float y = rect.top + rect.height()/2f + bounds.height()/2f;

        canvas.drawText(string, x, y, paint);
    }
    public static Float getMinFloat(ArrayList<Float> numbers) {
        Float min = numbers.get(0);
        for (Float number : numbers) {
            min = Math.min(min, number);
        }
        return min;
    }
    public static ArrayList cloneArrayList(ArrayList arrayList) {
        ArrayList newArrayList = new ArrayList();
        newArrayList.addAll(arrayList);
        return newArrayList;
    }
    public static ArrayList<Card> shuffleCards(ArrayList<Card> arrayList) {
        ArrayList oldList = cloneArrayList(arrayList);
        ArrayList newList = new ArrayList<Card>();
        int size = oldList.size();
        for (int index = 0; index < size; index++) {
            Object item = oldList.get(RANDOM.nextInt(oldList.size()));
            newList.add(item);
            oldList.remove(item);
        }
        return newList;
    }
    public static ArrayList<Button> shuffleButtons(ArrayList<Button> arrayList) {
        ArrayList oldList = cloneArrayList(arrayList);
        ArrayList newList = new ArrayList<Button>();
        int size = oldList.size();
        for (int index = 0; index < size; index++) {
            Object item = oldList.get(RANDOM.nextInt(oldList.size()));
            newList.add(item);
            oldList.remove(item);
        }
        return newList;
    }
    public static ArrayList<Card> getMostVariedCards(ArrayList<Card> arrayList, int number) {
        ArrayList newList = new ArrayList<Card>();
        int index = 1;
        while (newList.size() < number) {
            for (Object object : arrayList) {
                if (newList.size() < number) {
                    if (Collections.frequency(newList, object) < index) {
                        newList.add(object);
                    }
                }
            }
            index++;
        }
        return newList;
    }

    public static void drawImage(Canvas canvas, String imageDirectory, Rect rect) {
        if (Global.METODKA_LOADER.getImage(imageDirectory) != null) {
            if (!Global.METODKA_LOADER.getImage(imageDirectory).isRecycled()) {
                canvas.drawBitmap(Global.METODKA_LOADER.getImage(imageDirectory), null, rect, null);
            } else {
                if (Global.METODKA_LOADER.addImage(imageDirectory)) {
                    canvas.drawBitmap(Global.METODKA_LOADER.getImage(imageDirectory), null, rect, null);
                }
            }
        } else {
            if (Global.METODKA_LOADER.addImage(imageDirectory)) {
                canvas.drawBitmap(Global.METODKA_LOADER.getImage(imageDirectory), null, rect, null);
            }
        }
    }
}
