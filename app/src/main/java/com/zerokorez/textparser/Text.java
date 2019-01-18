package com.zerokorez.textparser;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.zerokorez.general.Global;

import java.util.ArrayList;

public class Text {
    private ArrayList<Paragraph> paragraphs;
    private Paint paint;
    private Integer align;
    private Float[] maxSizes;
    private Float spaceHeight;
    private Float currentHeight;
    private Float blankLine;
    private Float upperIndent;

    public Text(String string, Float width, Float height) {
        string.replaceAll("--", "→");
        String[] strings = string.split("´");
        paragraphs = new ArrayList<>();
        currentHeight = 0f;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (strings.length > 1) {
            if (strings[1].contains("L")) {
                align = 0;
            } else if (strings[1].contains("C")) {
                align = 1;
            } else if (strings[1].contains("R")) {
                align = 2;
            } else {
                align = 0;
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
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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
                    paint.setColor(Color.BLACK);
                }
            } else {
                paint.setColor(Color.BLACK);
            }

            if (strings[1].contains("V")) {
                if (strings[1].contains("V~")) {
                    paint.setTextSize(Constants.getFloat(strings[1], new Character[]{'V', ';',})*Global.CONTEXT.getResources().getDisplayMetrics().density);
                } else {
                    paint.setTextSize(Constants.getFloat(strings[1], new Character[]{'V', ';',}));
                }
            } else {
                paint.setTextSize(12* Global.CONTEXT.getResources().getDisplayMetrics().density);
            }
            if (strings[1].contains("M")) {
                if (strings[1].contains("M~")) {
                    spaceHeight = Constants.getFloat(strings[1], new Character[]{'M', ';',})*Global.CONTEXT.getResources().getDisplayMetrics().density;
                } else {
                    spaceHeight = Constants.getFloat(strings[1], new Character[]{'M', ';',});
                }
            } else {
                spaceHeight = 0f;
            }
            if (strings[1].contains("D")) {
                if (strings[1].contains("D~")) {
                    upperIndent = Constants.getFloat(strings[1], new Character[]{'D', ';',})*Global.CONTEXT.getResources().getDisplayMetrics().density;
                } else {
                    upperIndent = Constants.getFloat(strings[1], new Character[]{'D', ';',});
                }
            } else {
                upperIndent = 0f;
            }
            if (strings[1].contains("N")) {
                if (strings[1].contains("N~")) {
                    blankLine = Constants.getFloat(strings[1], new Character[]{'N', ';',})*Global.CONTEXT.getResources().getDisplayMetrics().density;
                } else {
                    blankLine = Constants.getFloat(strings[1], new Character[]{'N', ';',});
                }
            } else {
                blankLine = 15*Global.CONTEXT.getResources().getDisplayMetrics().density;
            }
        } else {
            align = 0;
            spaceHeight = 0f;
            upperIndent = 0f;
            blankLine = 15*Global.CONTEXT.getResources().getDisplayMetrics().density;
            paint.setTextSize(12*Global.CONTEXT.getResources().getDisplayMetrics().density);
            paint.setColor(Color.BLACK);
        }

        maxSizes = new Float[]{width, height,};

        for (String paragraph : strings[0].split(">")) {
            addParagraph(new Paragraph(this, paragraph));
        }
    }

    public void addParagraph(Paragraph paragraph) {
        paragraphs.add(paragraph);
        if (currentHeight > 0f) {
            currentHeight += spaceHeight;
            paragraph.setUpperIndent(upperIndent);
        }
        currentHeight += paragraph.getCurrentHeight();
    }

    public Float getMaxWidth() {
        return maxSizes[0];
    }
    public Paint getPaint() {
        return paint;
    }
    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }
    public Integer getAlign() {
        return align;
    }
    public Float getCurrentHeight() {
        return currentHeight;
    }
    public Float getBlankLine() {
        return blankLine;
    }
    public Float getSpaceHeight() {
        return spaceHeight;
    }
    public Float getUpperIndent() {
        return upperIndent;
    }
}
