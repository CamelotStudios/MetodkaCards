package com.zerokorez.textparser;

import java.util.ArrayList;

public class Line {
    private ArrayList<Word> words;
    private Float currentWidth;
    private Float currentHeight;
    private Paragraph paragraph;
    private Float y;
    private Float currentDescent;

    public Line(Paragraph paragraph) {
        words = new ArrayList<>();
        currentWidth = 0f;
        currentHeight = 0f;
        currentDescent = 0f;
        this.paragraph = paragraph;
    }

    public void addWord(Word word) {
        words.add(word);
        if (currentWidth > 0f) {
            currentWidth += paragraph.getSpaceWidth();
        }
        word.setX(currentWidth);
        currentWidth += word.getWidth();
        currentHeight = Math.max(currentHeight, word.getHeight());
        currentDescent = Math.max(currentDescent, word.getDescent());
    }

    public void addSpace(float space) {
        currentWidth += space - paragraph.getSpaceWidth();
        currentHeight = Math.max(currentHeight, 0);
        currentDescent = Math.max(currentDescent, 0);
    }

    public Float getCurrentWidth() {
        return currentWidth;
    }
    public Float getCurrentHeight() {
        return currentHeight;
    }
    public ArrayList<Word> getWords() {
        return words;
    }
    public Float getY() {
        return y;
    }
    public Float getCurrentDescent() {
        return currentDescent;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
