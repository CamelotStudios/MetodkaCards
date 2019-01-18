package com.zerokorez.textparser;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.zerokorez.general.Global;

import java.util.ArrayList;

public class Word {
    private ArrayList<Syllable> syllables;
    private String word;
    private Paint paint;
    private Float[] size;
    private Float x;
    private Rect rect;

    public Word(Paragraph paragraph, String string) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        String[] strings = string.split("ˇ");
        word = strings[0];

        if (strings.length > 1) {
            if (strings[1].contains("S")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            } else if (strings[1].contains("B") && !strings[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (!strings[1].contains("B") && strings[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            } else if (strings[1].contains("B") && strings[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            } else {
                paint.setTypeface(paragraph.getPaint().getTypeface());
            }

            if (strings[1].contains("#")) {
                boolean isSet = false;
                for (String color : Constants.COLORS.keySet()) {
                    if (strings[1].contains(color)) {
                        try {
                            paint.setColor(Constants.COLORS.get(color));
                            isSet = true;
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                        break;
                    }
                }
                if (!isSet) {
                    paint.setColor(paragraph.getPaint().getColor());
                }
            } else {
                paint.setColor(paragraph.getPaint().getColor());
            }

            if (strings[1].contains("V")) {
                if (strings[1].contains("V~")) {
                    paint.setTextSize(Constants.getFloat(strings[1], new Character[]{'V', ';',})* Global.CONTEXT.getResources().getDisplayMetrics().density);
                } else {
                    paint.setTextSize(Constants.getFloat(strings[1], new Character[]{'V', ';',}));
                }
            } else {
                paint.setTextSize(paragraph.getPaint().getTextSize());
            }
        } else {
            paint = paragraph.getPaint();
        }

        size = new Float[]{Constants.getTextWidth(strings[0], paint), Constants.getTextHeight(paint),};
    }
    public Word(Syllable syllable) {
        word = syllable.getString();
        paint = syllable.getPaint();
        size = new Float[]{syllable.getWidth(), syllable.getHeight(),};
    }
    public Word(String string, Paint paint, Float[] size) {
        this.word = string;
        this.paint = paint;
        this.size = size;
    }
    public Word(String string, Float number) {
        this.word = string;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(number);
        size = new Float[]{Constants.getTextWidth(word, paint), Constants.getTextHeight(paint),};
    }

    public ArrayList<Syllable> getSyllables() {
        syllables = new ArrayList<>();
        for (String string : Constants.divideWord(word)) {
            syllables.add(new Syllable(this, string));
        }
        return syllables;
    }
    public void concatenate(Word word) {
        this.word += word.word;
        size = new Float[]{Constants.getTextWidth(this.word, paint), Constants.getTextHeight(paint),};
    }
    public void concatenate(String string) {
        word += string;
        size = new Float[]{Constants.getTextWidth(word, paint), Constants.getTextHeight(paint),};
    }

    public Float getWidth() {
        return size[0];
    }
    public Float getHeight() {
        return size[1];
    }
    public String getString() {
        return word;
    }
    public Paint getPaint() {
        return paint;
    }
    public Integer getLength() {
        return word.length();
    }
    public Rect getRect() {
        return rect;
    }
    public Float getX() {
        return x;
    }
    public Float getDescent() {
        return paint.getFontMetrics().descent;
    }

    public void setX(Float x) {
        this.x = x;
    }
    public void setRect(Line line) {
        rect = new Rect(x.intValue(), line.getY().intValue(), (int) (x + getWidth()), (int) (line.getY() + line.getCurrentHeight()));
    }
}
