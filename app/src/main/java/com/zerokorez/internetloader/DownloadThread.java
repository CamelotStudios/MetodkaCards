package com.zerokorez.internetloader;

import com.zerokorez.general.Global;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DownloadThread extends Thread {
    private ArrayList<String> data;
    private String address;
    private boolean isFinished;

    public DownloadThread(String address) {
        this.data = null;
        this.address = address;
        isFinished = false;
    }

    @Override
    public void run() {
        data = readInternetFile(address);
        isFinished = true;
    }

    public static Scanner getNetworkData(String address) {
        try {
            URL url = new URL(address);
            return new Scanner(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> readInternetFile(String url) {
        if (Global.isNetworkAvailable()) {
            Scanner scanner = getNetworkData(url);
            ArrayList<String> lines = new ArrayList<>();
            if (scanner != null) {
                while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine());
                }
                return lines;
            }
        }
        return null;
    }

    public ArrayList<String> getData() {
        return data;
    }
    public boolean isFinished() {
        return isFinished;
    }
}
