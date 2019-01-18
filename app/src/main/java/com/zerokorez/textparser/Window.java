package com.zerokorez.textparser;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Global;

public class Window implements Drawable {
    private Canvas canvas;
    private Bitmap bitmap;
    private Text text;
    private Rect canvasRect;
    private Rect textRect;
    private Float[] position;
    private Float[] move;
    private Float columnHeight;
    private Paint paint;
    private Float odd;

    public Window(String text, Rect rect, Float odd) {
        textRect = new Rect((int) (rect.left + odd), rect.top, (int) (rect.right - odd), rect.bottom);
        setPosition((float) rect.left, (float) rect.top);
        setMove(0f, 0f);
        this.text = new Text(text, (float) textRect.width(), 0f);
        columnHeight = this.text.getCurrentHeight();
        this.odd = odd;
        canvasRect = new Rect(position[0].intValue(), position[1].intValue(), (int) (position[0] + rect.width()), (int) (position[1] + columnHeight));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(com.zerokorez.lepsiametodkamemorycardsov.Constants.WHITE);
        paint.setStyle(Paint.Style.FILL);

        bitmap = Bitmap.createBitmap(canvasRect.width(), (int) (columnHeight + 1), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawPaint(paint);
    }
    public Window(String text, int width) {
        int screenWidth = Global.WIDTH;
        odd =  screenWidth/48f;
        setMove(0f, 0f);

        this.text = new Text(text, width - odd*2, 0f);
        columnHeight = this.text.getCurrentHeight();
        setPosition( (screenWidth-width)/2f, screenWidth/4f - columnHeight/2f);

        textRect = new Rect((int) (position[0] + odd), position[1].intValue(), (int) (position[0] + width - odd), (int) (position[1] + columnHeight));
        canvasRect = new Rect(position[0].intValue(), position[1].intValue(), (int) (position[0] + width), (int) (position[1] + columnHeight));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(com.zerokorez.lepsiametodkamemorycardsov.Constants.C11F);
        paint.setStyle(Paint.Style.FILL);

        bitmap = Bitmap.createBitmap(canvasRect.width(), (int) (columnHeight + 1), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawPaint(paint);
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                canvas.drawBitmap(bitmap, null, Constants.moveRect(canvasRect, move[0].intValue(), move[1].intValue()), null);
            }
        }
    }

    @Override
    public void update() {
        instantiate();
        Float currentHeight = 0f;
        for (Paragraph paragraph : text.getParagraphs()) {
            currentHeight += paragraph.getUpperIndent();
            for (Line line : paragraph.getLines()) {
                if (line.getWords().size() > 1 ) {
                    for (Word word : line.getWords()) {
                        if (paragraph.getAlign() == 1) {
                            Constants.drawTextCenter(canvas, Constants.moveRect(word.getRect(), odd.intValue(), currentHeight.intValue()), word, line);
                        } else if (paragraph.getAlign() == 2) {
                            Constants.drawTextRight(canvas, Constants.moveRect(word.getRect(), odd.intValue(), currentHeight.intValue()), word, line, 0f);
                        } else {
                            Constants.drawTextLeft(canvas, Constants.moveRect(word.getRect(), odd.intValue(), currentHeight.intValue()), word, line, 0f);
                        }
                    }
                } else if (line.getWords().size() == 1) {
                    Word word = line.getWords().get(0);
                    if (Global.compareStrings(word.getString(), "***")) {
                        Rect rect = Constants.moveRect(word.getRect(), odd.intValue(), currentHeight.intValue());
                        canvas.drawLine(0, rect.top + rect.height()/2f, canvas.getWidth(), rect.top + rect.height()/2f, com.zerokorez.lepsiametodkamemorycardsov.Constants.BLACK_BORDER_PAINT);
                    } else {
                        if (paragraph.getAlign() == 1) {
                            Constants.drawTextCenter(canvas, Constants.moveRect(word.getRect(), odd.intValue(), currentHeight.intValue()), word, line);
                        } else if (paragraph.getAlign() == 2) {
                            Constants.drawTextRight(canvas, Constants.moveRect(word.getRect(), odd.intValue(), currentHeight.intValue()), word, line, 0f);
                        } else {
                            Constants.drawTextLeft(canvas, Constants.moveRect(word.getRect(), odd.intValue(), currentHeight.intValue()), word, line, 0f);
                        }
                    }
                }
            }
            currentHeight += paragraph.getCurrentHeight();
        }
    }

    public void recycle() {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
    public void instantiate() {
        recycle();
        bitmap = Bitmap.createBitmap(canvasRect.width(), (text.getCurrentHeight() < canvasRect.height()) ? canvasRect.height() : (int) (text.getCurrentHeight() + 1), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        canvas.drawPaint(paint);
    }

    public Float getColumnHeight() {
        return columnHeight;
    }

    public void setPosition(Float[] position) {
        this.position = position;
    }
    public void setPosition(Float x, Float y) {
        position = new Float[]{x, y,};
    }
    public void setMove(Float[] move) {
        this.move = move;
    }
    public void setMove(Float x, Float y) {
        move = new Float[]{x, y,};
    }

    public void setMoveX(Float x) {
        move[0] = x;
    }
    public void setMoveY(Float y) {
        move[1] = y;
    }

    public Float[] getPosition() {
        return position;
    }
    public Rect getCanvasRect() {
        return canvasRect;
    }
}
