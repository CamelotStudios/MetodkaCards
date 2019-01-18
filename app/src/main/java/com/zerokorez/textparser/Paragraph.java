package com.zerokorez.textparser;

import android.graphics.Paint;
import android.graphics.Typeface;
import com.zerokorez.general.Global;

import java.util.ArrayList;

public class Paragraph {
    private ArrayList<Line> lines;
    private Paint paint;
    private Text text;
    private Integer align;

    private Float dashWidth;
    private Float spaceHeight;
    private Float blankLine;
    private Float pointWidth;
    private boolean isListItem;

    private Float currentHeight;
    private Float upperIndent;

    public Paragraph(Text text, String string) {
        String[] strings = string.split("<");
        lines = new ArrayList<>();
        currentHeight = 0f;
        upperIndent = 0f;
        this.text = text;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        isListItem = false;

        if (strings.length > 1) {
            if (strings[1].contains("L")) {
                align = 0;
            } else if (strings[1].contains("C")) {
                align = 1;
            } else if (strings[1].contains("R")) {
                align = 2;
            } else {
                align = text.getAlign();
            }

            if (strings[1].contains("S")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            } else if (strings[1].contains("B") && !strings[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (!strings[1].contains("B") && strings[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            } else if (strings[1].contains("B") && strings[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            } else {
                paint.setTypeface(text.getPaint().getTypeface());
            }

            if (strings[1].contains("P")) {
                strings[0] = "・" + strings[0];
                isListItem = true;
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
                    paint.setColor(text.getPaint().getColor());
                }
            } else {
                paint.setColor(text.getPaint().getColor());
            }

            if (strings[1].contains("V")) {
                if (strings[1].contains("V~")) {
                    paint.setTextSize(Constants.getFloat(strings[1], new Character[]{'V', ';',})* Global.CONTEXT.getResources().getDisplayMetrics().density);
                } else {
                    paint.setTextSize(Constants.getFloat(strings[1], new Character[]{'V', ';',}));
                }
            } else {
                paint.setTextSize(text.getPaint().getTextSize());
            }
            if (strings[1].contains("M")) {
                if (strings[1].contains("M~")) {
                    spaceHeight = Constants.getFloat(strings[1], new Character[]{'M', ';',})* Global.CONTEXT.getResources().getDisplayMetrics().density;
                } else {
                    spaceHeight = Constants.getFloat(strings[1], new Character[]{'M', ';',});
                }
            } else {
                spaceHeight = text.getSpaceHeight();
            }
        } else {
            paint = text.getPaint();
            align = text.getAlign();
            spaceHeight = text.getSpaceHeight();
        }
        dashWidth = Constants.getTextWidth("-", paint);
        pointWidth = Constants.getTextWidth("・", paint);

        Line line = new Line(this);
        for (String wordText : strings[0].split(" ")) {
            if (wordText.contains(" ")) {
                wordText = wordText.replaceAll(" ", "");
            }
            Word word = new Word(this, wordText);
            if (line.getCurrentWidth() + word.getWidth() < text.getMaxWidth()) {
                line.addWord(word);
            } else if (word.getWidth() < text.getMaxWidth()) {
                boolean isStopped = false;
                ArrayList<Syllable> syllables = word.getSyllables();
                Word lastWord = new Word("", word.getPaint(), new Float[]{0f, 0f,});
                Word nextWord = new Word("", word.getPaint(), new Float[]{0f, 0f,});
                for (Syllable syllable : syllables) {
                    if (line.getCurrentWidth() + lastWord.getWidth() + syllable.getWidth() + dashWidth < text.getMaxWidth() && !isStopped) {
                        lastWord.concatenate(syllable.toWord());
                    } else {
                        if (!isStopped) {
                            isStopped = true;
                            if (lastWord.getLength() > 0) {
                                lastWord.concatenate("-");
                                line.addWord(lastWord);
                            }
                            addLine(line);
                            line = new Line(this);
                            if (isListItem) {
                                line.addSpace(pointWidth);
                            }

                        }
                        nextWord.concatenate(syllable.toWord());
                    }
                }
                if (isStopped && nextWord.getLength() > 0) {
                    line.addWord(nextWord);
                }
            }
        }
        if (line.getCurrentWidth() > 0f) {
            addLine(line);
        }

        if (strings.length > 1) {
            if (strings[1].contains("N")) {
                try {
                    if (strings[1].contains("N~")) {
                        blankLine = Constants.getFloat(strings[1], new Character[]{'N', ';',})* Global.CONTEXT.getResources().getDisplayMetrics().density;
                    } else {
                        blankLine = Constants.getFloat(strings[1], new Character[]{'N', ';',});
                    }
                } catch (NumberFormatException e) {
                    blankLine = text.getBlankLine();
                }
                int number;
                if (strings[1].contains("N+")) {
                    number = Constants.getFloat(strings[1], new Character[]{'N', ';',}).intValue();
                } else {
                    number = 1;
                }
                for (int range = 0; range < number; range++) {
                    line = new Line(this);
                    line.addWord(new Word(" ", blankLine));
                    addLine(line);
                }
            } else if (strings[1].contains("D")) {
                try {
                    if (strings[1].contains("D~")) {
                        blankLine = Constants.getFloat(strings[1], new Character[]{'D', ';',})* Global.CONTEXT.getResources().getDisplayMetrics().density;
                    } else {
                        blankLine = Constants.getFloat(strings[1], new Character[]{'D', ';',});
                    }
                } catch (NumberFormatException e) {
                    blankLine = text.getBlankLine();
                }
                line = new Line(this);
                line.addWord(new Word("***", blankLine));
                addLine(line);
            }
        }
    }

    public void addLine(Line line) {
        lines.add(line);
        if (currentHeight > 0f) {
            currentHeight += spaceHeight;
        }
        line.setY(currentHeight);

        if (align == 1) {
            Float space = (text.getMaxWidth() - line.getCurrentWidth())/2f;
            for (Word word : line.getWords()) {
                word.setX(word.getX() + space);
            }
        } else if (align == 2) {
            Float space = text.getMaxWidth() - line.getCurrentWidth();
            for (Word word : line.getWords()) {
                word.setX(word.getX() + space);
            }
        }

        for (Word word : line.getWords()) {
            word.setRect(line);
        }
        currentHeight += line.getCurrentHeight();
    }

    public Paint getPaint() {
        return paint;
    }
    public Float getSpaceWidth() {
        return dashWidth;
    }
    public ArrayList<Line> getLines() {
        return lines;
    }
    public Integer getAlign() {
        return align;
    }
    public Float getCurrentHeight() {
        return currentHeight;
    }
    public Float getUpperIndent() {
        return upperIndent;
    }
    public void setUpperIndent(Float upperIndent) {
        this.upperIndent = upperIndent;
    }
}
