package com.zerokorez.textparser;

import android.graphics.Paint;

public class Syllable {
    private String string;
    private Paint paint;
    private Float[] size;

    public Syllable(Word word, String string) {
        this.string = string;
        paint = word.getPaint();
        size = new Float[]{Constants.getTextWidth(string, paint), Constants.getTextHeight(paint),};
    }

    public Word toWord() {
        return new Word(this);
    }

    public Float getWidth() {
        return size[0];
    }
    public Float getHeight() {
        return size[1];
    }
    public String getString() {
        return string;
    }
    public Paint getPaint() {
        return paint;
    }
}
