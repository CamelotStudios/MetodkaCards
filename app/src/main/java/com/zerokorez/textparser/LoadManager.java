package com.zerokorez.textparser;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LoadManager {
    public void setUpConstants() {
        Constants.NUMBERS = new ArrayList<>();
        Collections.addAll(Constants.NUMBERS, '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '-');

        Constants.COLORS = new HashMap<>();
        Constants.COLORS.put("black", Color.rgb(0, 0, 0));
        Constants.COLORS.put("white", Color.rgb(255, 255, 255));

        Constants.COLORS.put("red", Color.rgb(255, 0, 0));
        Constants.COLORS.put("green", Color.rgb(0, 255, 0));
        Constants.COLORS.put("blue", Color.rgb(0, 0, 255));

        Constants.COLORS.put("yellow", Color.rgb(255, 255, 0));
        Constants.COLORS.put("cyan", Color.rgb(0, 255, 255));
        Constants.COLORS.put("magenta", Color.rgb(255, 0, 255));

        Constants.COLORS.put("index", com.zerokorez.lepsiametodkamemorycardsov.Constants.C5F);
        Constants.COLORS.put("value", com.zerokorez.lepsiametodkamemorycardsov.Constants.C3F);

        Constants.CONSONANTS = new ArrayList<>();
        Collections.addAll(Constants.CONSONANTS, 'b', 'c', 'č', 'd', 'ď', 'f', 'g', 'h', 'j', 'k', 'ľ', 'm', 'n', 'ň', 'p', 'q', 's', 'š', 't', 'ť', 'v', 'w', 'x', 'z', 'ž', 'B', 'C', 'Č', 'D', 'Ď', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'Ň', 'P', 'Q', 'S', 'Š', 'T', 'Ť', 'V', 'W', 'X', 'Z', 'Ž');
        Constants.VOWELS = new ArrayList<>();
        Collections.addAll(Constants.VOWELS, 'a', 'e', 'i', 'o', 'u', 'y', 'á', 'é', 'í', 'ó' ,'ú' ,'ý', 'ä', 'ô', 'A', 'E', 'I', 'O', 'U', 'Y', 'Á', 'É', 'Í', 'Ó' ,'Ú' ,'Ý', 'Ä', 'Ô');
        Constants.LR = new ArrayList<>();
        Collections.addAll(Constants.LR, 'l', 'ĺ', 'r', 'ŕ', 'L', 'Ĺ', 'R', 'Ŕ');

        Constants.DOUBLE_CONSONANTS = new ArrayList<>();
        Collections.addAll(Constants.DOUBLE_CONSONANTS, "ch", "dz", "dž", "Ch", "Dz", "Dž", "CH", "DZ", "DŽ");
        Constants.DOUBLE_VOWELS = new ArrayList<>();
        Collections.addAll(Constants.DOUBLE_VOWELS, "ia", "ie", "iu", "au", "Ia", "Ie", "Iu", "Au", "IA", "IE", "IU", "AU");

        Constants.LOWER = new ArrayList<>();
        Collections.addAll(Constants.LOWER, 'g', 'j', 'p', 'q', 'y', 'ý');
    }
}
