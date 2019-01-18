package com.zerokorez.general;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.zerokorez.storageloader.Group;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Global {
    public static Context CONTEXT;
    public static boolean WAITING;
    public static boolean SELECTED;

    public static String CREDENTIALS;
    public static String DATA;
    public static String BASE;

    public static int WIDTH;
    public static int HEIGHT;
    public static View VIEW;

    public static ArrayList<Group> GROUPS;
    public static int ACTIVE_GROUP;
    public static AssetManager ASSET_MANAGER;

    public static HashMap<String, String> LANGUAGE;

    public static com.zerokorez.lepsiametodkamemorycardsov.LoadManager METODKA_LOADER;
    public static com.zerokorez.internetloader.LoadManager INTERNET_LOADER;
    public static com.zerokorez.storageloader.LoadManager STORAGE_LOADER;
    public static com.zerokorez.textparser.LoadManager PARSER_LOADER;

    public static void setUpConstants(Context context, View view) {
        CONTEXT = context;
        VIEW = view;
        WAITING = false;
        SELECTED = false;

        GROUPS = new ArrayList<>();
        ASSET_MANAGER = context.getAssets();

        METODKA_LOADER = new com.zerokorez.lepsiametodkamemorycardsov.LoadManager();
        INTERNET_LOADER = new com.zerokorez.internetloader.LoadManager();
        STORAGE_LOADER = new com.zerokorez.storageloader.LoadManager();
        PARSER_LOADER = new com.zerokorez.textparser.LoadManager();

        METODKA_LOADER.setUpConstants();
        INTERNET_LOADER.setUpConstants();
        STORAGE_LOADER.setUpConstants();
        PARSER_LOADER.setUpConstants();

        STORAGE_LOADER.loadLanguagePack("Slovak");
    }

    public static void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) CONTEXT.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (VIEW.requestFocus()) {
                if (!inputMethodManager.showSoftInput(VIEW, InputMethodManager.SHOW_FORCED)) {
                    ((Activity) CONTEXT).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
            }
        }
    }

    public static String getLanguage(String word) {
        if (LANGUAGE != null) {
            if (LANGUAGE.keySet().contains(word)) {
                String newString = LANGUAGE.get(word);
                if (newString != null) {
                    return newString;
                }
            }
        }
        return word;
    }

    public static ArrayList<String> readAssets(String path) {
        ArrayList<String> lines = new ArrayList<>();
        if (ASSET_MANAGER == null) {
            ASSET_MANAGER = CONTEXT.getAssets();
        }
        try {
            InputStream inputStream = ASSET_MANAGER.open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return lines;
    }

    public static boolean compareStrings(String string1, String string2) {
        if (string1.length() != string2.length()) {
            return false;
        } else {
            for(int index = 0; index < string1.length(); ++index) {
                if (string1.charAt(index) != string2.charAt(index)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static Rect moveRect(Rect rect, int x, int y) {
        return new Rect(rect.left + x, rect.top + y, rect.right + x, rect.bottom + y);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static File createFile(String path) {
        File file = openFile(path);
        while (!file.isFile()) {
            if (file.isDirectory()) {
                file.delete();
            }
            if (file.getParentFile().isFile()) {
                file.getParentFile().delete();
            }
            if (!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return file;
    }
    public static File createDirectory(String path) {
        File file = openFile(path);
        while (!file.isDirectory()) {
            if (file.isFile()) {
                file.delete();
            }
            if (file.getParentFile().isFile()) {
                file.getParentFile().delete();
            }
            if (!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.mkdir();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return file;
    }

    public static File openFile(String path) {
        return new File(CONTEXT.getFilesDir(), path);
    }

    public static File deleteFile(String path) {
        File file = openFile(path);
        file.delete();
        return file;
    }
}
