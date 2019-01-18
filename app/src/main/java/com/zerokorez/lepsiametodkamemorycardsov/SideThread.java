package com.zerokorez.lepsiametodkamemorycardsov;

import com.zerokorez.general.Global;

public class SideThread extends Thread {
    private boolean running;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public SideThread() {
        super();
    }

    @Override
    public void run() {
        while (running) {
            Global.METODKA_LOADER.manageImages();
        }
    }
}
