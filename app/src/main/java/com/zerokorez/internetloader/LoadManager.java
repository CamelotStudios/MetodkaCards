package com.zerokorez.internetloader;

import com.zerokorez.general.Global;

import java.util.ArrayList;

public class LoadManager {
    public void setUpConstants() {
        ArrayList<String> list = Global.readAssets("credentials");
        Global.CREDENTIALS = list.get(0);
        Global.BASE = list.get(1);
        Global.DATA = list.get(2);
        Global.createFile(Global.BASE);
        Global.createDirectory(Global.DATA);
    }

    public ArrayList<String> readInternetFile(String url) {
        Global.SELECTED = true;
        Global.WAITING = true;
        DownloadThread downloader = new DownloadThread(url);
        downloader.start();
        while (!downloader.isFinished()) { }
        try {
            downloader.join();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        Global.WAITING = false;
        return downloader.getData();
    }

    public ArrayList<Source> readInternetDatabase() {
        ArrayList<String> lines = readInternetFile(Global.CREDENTIALS);
        if (lines != null) {
            ArrayList<Source> sources = new ArrayList<>();
            for (String line : lines) {
                Source source = new Source(line);
                if (source.isProper()) {
                    sources.add(source);
                }
            }
            if (sources.size() > 0) {
                return sources;
            }
        }
        return null;
    }
}
