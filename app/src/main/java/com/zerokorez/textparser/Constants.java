package com.zerokorez.textparser;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.zerokorez.general.Global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Constants {
    public static ArrayList<Character> NUMBERS;
    public static HashMap<String, Integer> COLORS;

    public static ArrayList<Character> CONSONANTS;
    public static ArrayList<Character> VOWELS;
    public static ArrayList<Character> LR;

    public static ArrayList<String> DOUBLE_CONSONANTS;
    public static ArrayList<String> DOUBLE_VOWELS;

    public static ArrayList<Character> LOWER;

    public static Float getTextWidth(String string, Paint paint) {
        return paint.measureText(string);
    }

    public static Float getTextHeight(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return metrics.descent - metrics.ascent;
    }

    public static Float getFloat(String string, Character[] separators) {
        String numberString = "";
        boolean isReading = false;
        for (Character character : string.toCharArray()) {
            if (character == separators[0]) {
                isReading = true;
            }
            if (isReading) {
                if (Constants.NUMBERS.contains(character)) {
                    numberString += character;
                }
                if (character == separators[1]) {
                    return Float.valueOf(numberString);
                }
            }
        }
        return Float.valueOf(numberString);
    }

    public static String getString(String string, Character[] separators) {
        String newString = "";
        boolean isReading = false;
        for (Character character : string.toCharArray()) {
            if (character == separators[0] && !isReading) {
                isReading = true;
            } else if (isReading) {
                if (character == separators[1]) {
                    return newString;
                } else if (character != '~') {
                    newString += character;
                }
            }
        }
        return newString;
    }

    public static Integer[] range(Integer limit) {
        Integer[] array = new Integer[limit];
        for(Integer index = 0; index < limit; index = index + 1) {
            array[index] = index;
        }
        return array;
    }

    public static Rect moveRect(Rect rect, int x, int y) {
        return new Rect(rect.left + x, rect.top + y, rect.right + x, rect.bottom + y);
    }

    public static void drawTextCenter(Canvas canvas, Rect rect, Word word, Line line) {
        word.getPaint().setTextAlign(Paint.Align.LEFT);

        float x = rect.left + rect.width()/2f - word.getWidth()/2f;
        float y = rect.bottom - line.getCurrentDescent();

        canvas.drawText(word.getString(), x, y, word.getPaint());
    }
    public static void drawTextRight(Canvas canvas, Rect rect, Word word, Line line, Float odd) {
        word.getPaint().setTextAlign(Paint.Align.LEFT);

        float x = rect.right - odd - word.getWidth();
        float y = rect.bottom - line.getCurrentDescent();

        canvas.drawText(word.getString(), x, y, word.getPaint());
    }
    public static void drawTextLeft(Canvas canvas, Rect rect, Word word, Line line, Float odd) {
        word.getPaint().setTextAlign(Paint.Align.LEFT);

        float x = rect.left + odd;
        float y = rect.bottom - line.getCurrentDescent();

        canvas.drawText(word.getString(), x, y, word.getPaint());
    }

    public static ArrayList<String> divideWord(String word) {
        ArrayList<String> syllables = new ArrayList<>();
        String syllable = "";
        int index = 0;
        while(true) {
            while(index <= word.length() - 1) {
                if (Constants.CONSONANTS.contains(word.charAt(index))) {
                    syllable = syllable + word.charAt(index++);
                } else {
                    ArrayList result;
                    if (Constants.VOWELS.contains(word.charAt(index))) {
                        result = divideVowel(syllables, syllable, word, index);
                        syllable = (String)result.get(0);
                        word = (String)result.get(1);
                        index = (Integer)result.get(2);
                    } else if (Constants.LR.contains(word.charAt(index))) {
                        syllable = syllable + word.charAt(index++);
                        if (word.length() - 1 < index) {
                            if (word.length() - 1 == index) {
                                syllables.add(syllable + word.charAt(index++));
                                syllable = "";
                            }
                        } else if (Constants.VOWELS.contains(word.charAt(index))) {
                            result = divideVowel(syllables, syllable, word, index);
                            syllable = (String)result.get(0);
                            word = (String)result.get(1);
                            index = (Integer)result.get(2);
                        } else if (word.length() - 1 < index) {
                            syllables.add(syllable);
                            syllable = "";
                        } else if (word.length() - 1 > index + 1) {
                            if (Constants.VOWELS.contains(word.charAt(index + 1))) {
                                syllables.add(syllable);
                                syllable = "";
                            } else {
                                if (Constants.DOUBLE_CONSONANTS.contains(""+word.charAt(index)+word.charAt(index+1))) {
                                    if (Constants.VOWELS.contains(word.charAt(index+2))) {
                                        syllables.add(syllable);
                                        syllable = "";
                                    } else {
                                        syllable = syllable + word.charAt(index++);
                                        syllable = syllable + word.charAt(index++);
                                        syllables.add(syllable);
                                        syllable = "";
                                    }
                                } else {
                                    syllable = syllable + word.charAt(index++);
                                    syllables.add(syllable);
                                    syllable = "";
                                }
                            }
                        } else if (word.length() - 1 != index + 1) {
                            if (word.length() - 1 == index) {
                                syllable = syllable + word.charAt(index++);
                                syllables.add(syllable);
                                syllable = "";
                            }
                        } else if (Constants.VOWELS.contains(word.charAt(index + 1))) {
                            syllables.add(syllable);
                            syllable = "";
                        } else {
                            Integer[] var5 = range(2);
                            int var6 = var5.length;
                            for(int var7 = 0; var7 < var6; ++var7) {
                                int number = var5[var7];
                                syllable = syllable + word.charAt(index++);
                            }
                            syllables.add(syllable);
                            syllable = "";
                        }
                    } else {
                        syllable += word.charAt(index++);
                        if (word.length()-1 < index) {
                            syllables.add(syllable);
                            syllable = "";
                        }
                    }
                }
            }
            return syllables;
        }
    }
    public static ArrayList<Object> divideVowel(ArrayList<String> syllables, String syllable, String word, Integer index) {
        StringBuilder var10000 = (new StringBuilder()).append(syllable);
        Integer var4 = index;
        index = index + 1;
        syllable = var10000.append(word.charAt(var4)).toString();
        Integer var6;
        String lastString;
        if (word.length() - 1 > index + 1) {
            if (Constants.VOWELS.contains(word.charAt(index))) {
                lastString = "" + word.charAt(index - 1) + word.charAt(index);
                if (Constants.DOUBLE_VOWELS.contains(lastString)) {
                    var10000 = (new StringBuilder()).append(syllable);
                    var6 = index;
                    index = index + 1;
                    syllable = var10000.append(word.charAt(var6)).toString();
                }
            }
            if (!Constants.CONSONANTS.contains(word.charAt(index)) && !Constants.LR.contains(word.charAt(index)) || !Constants.CONSONANTS.contains(word.charAt(index + 1)) && !Constants.LR.contains(word.charAt(index + 1))) {
                syllables.add(syllable);
                syllable = "";
            } else {
                if (Constants.DOUBLE_CONSONANTS.contains(""+word.charAt(index)+word.charAt(index+1))) {
                    if (word.length() > index+2) {
                        if (Constants.VOWELS.contains(word.charAt(index + 2))) {
                            syllables.add(syllable);
                            syllable = "";
                        } else {
                            var10000 = (new StringBuilder()).append(syllable);
                            var6 = index;
                            index = index + 1;
                            syllable = var10000.append(word.charAt(var6)).toString();
                            var10000 = (new StringBuilder()).append(syllable);
                            var6 = index;
                            index = index + 1;
                            syllable = var10000.append(word.charAt(var6)).toString();
                            syllables.add(syllable);
                            syllable = "";
                        }
                    } else {
                        var10000 = (new StringBuilder()).append(syllable);
                        var6 = index;
                        index = index + 1;
                        syllable = var10000.append(word.charAt(var6)).toString();
                        syllables.add(syllable);
                        syllable = "";
                    }
                } else {
                    var10000 = (new StringBuilder()).append(syllable);
                    var6 = index;
                    index = index + 1;
                    syllable = var10000.append(word.charAt(var6)).toString();
                    syllables.add(syllable);
                    syllable = "";
                }
            }
        } else if (word.length() - 1 == index + 1) {
            int var7;
            int var8;
            int number;
            Integer var10;
            Integer[] var13;
            if (Constants.VOWELS.contains(word.charAt(index))) {
                lastString = "" + word.charAt(index - 1) + word.charAt(index);
                String nextString = "" + word.charAt(index) + word.charAt(index + 1);
                if (Constants.DOUBLE_VOWELS.contains(lastString) && !Global.compareStrings(nextString, "um") && !Global.compareStrings(nextString, "us")) {
                    var13 = range(2);
                    var7 = var13.length;
                    for(var8 = 0; var8 < var7; ++var8) {
                        number = var13[var8];
                        var10000 = (new StringBuilder()).append(syllable);
                        var10 = index;
                        index = index + 1;
                        syllable = var10000.append(word.charAt(var10)).toString();
                    }
                    syllables.add(syllable);
                    syllable = "";
                } else if (Constants.DOUBLE_VOWELS.contains(lastString) && (Global.compareStrings(nextString, "um") || Global.compareStrings(nextString, "us"))) {
                    syllables.add(syllable);
                    syllable = "";
                }
            } else if ((Constants.CONSONANTS.contains(word.charAt(index)) || Constants.LR.contains(word.charAt(index))) && (Constants.CONSONANTS.contains(word.charAt(index + 1)) || Constants.LR.contains(word.charAt(index + 1)))) {
                var13 = range(2);
                var7 = var13.length;
                for(var8 = 0; var8 < var7; ++var8) {
                    number = var13[var8];
                    var10000 = (new StringBuilder()).append(syllable);
                    var10 = index;
                    index = index + 1;
                    syllable = var10000.append(word.charAt(var10)).toString();
                }
                syllables.add(syllable);
                syllable = "";
            } else {
                syllables.add(syllable);
                syllable = "";
            }
        } else if (word.length() - 1 == index) {
            var10000 = (new StringBuilder()).append(syllable);
            var6 = index;
            index = index + 1;
            syllable = var10000.append(word.charAt(var6)).toString();
            syllables.add(syllable);
            syllable = "";
        } else {
            syllables.add(syllable);
            syllable = "";
        }
        ArrayList<Object> result = new ArrayList<>();
        Collections.addAll(result, syllable, word, index);
        return result;
    }
}