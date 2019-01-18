package com.zerokorez.storageloader;

import com.zerokorez.general.Global;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Group {
    private String version;
    private String packageName;
    private String fullName;
    private ArrayList<Card> cards;
    private boolean isProper;
    private String lastStudiedDate;
    private String lastStudiedTime;
    private boolean isCustomized;
    private boolean isToBeSaved;
    private String authorName;
    private String categories;
    private String infoText;

    public Group(String dataLine, LoadManager loader, String versionLine, String userLine) {
        String[] strings = dataLine.split("::");
        if (strings.length == 3) {
            String[] groupInfo = strings[2].split("~~");
            if (groupInfo.length == 3) {
                authorName = groupInfo[0];
                categories = groupInfo[1];
                infoText = groupInfo[2];
            } else {
                authorName = "N/A";
                categories = "N/A";
                infoText = "N/A";
            }
            String[] userLines = new String[]{};
            if (userLine != null) {
                userLines = userLine.split("::");
                if (userLines.length == 2) {
                    String[] times = userLines[1].split("_");
                    if (times.length == 2) {
                        lastStudiedTime = times[0];
                        lastStudiedDate = times[1];
                    }
                }
                userLines = userLines[0].split("@@");
            }
            if (lastStudiedTime == null || lastStudiedDate == null) {
                lastStudiedTime = "HH:MM";
                lastStudiedDate = "DD.MM.YYYY";
            }
            this.version = versionLine;
            String[] names = strings[0].split("~~");
            if (names.length == 2) {
                packageName = names[0];
                fullName = names[1];
                cards = new ArrayList<>();
                String[] dataLines = strings[1].split("@@");
                for (int index = 0; index < dataLines.length; index++) {
                    String userData = null;
                    if (userLines.length > index) {
                        userData = userLines[index];
                    }
                    new Card(this, dataLines[index], userData);
                }
                isProper = !(cards.size() == 0);
                isCustomized = false;
                isToBeSaved = false;
                if (isProper) {
                    loader.getLoadedGroups().add(this);
                }
            } else {
                isProper = false;
            }
        } else {
            isProper = false;
        }
    }

    public void setLastStudied() {
        Date date = GregorianCalendar.getInstance().getTime();
        String[] strings = date.toString().split(" ");
        String[] times = strings[3].split(":");
        lastStudiedTime = times[0] + ":" + times[1];
        lastStudiedDate = strings[2] + "." + ((date.getMonth() < 9) ? "0" : "") + (date.getMonth()+1) + "." + strings[5];
        isToBeSaved = true;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getVersion() { return version; }

    public boolean isProper() {
        return isProper;
    }

    public String getLastStudiedDate() {
        return lastStudiedDate;
    }

    public String getLastStudiedTime() {
        return lastStudiedTime;
    }

    public boolean isCustomized() {
        return isCustomized;
    }

    public void setIsCustomized(boolean isCustomized) {
        this.isCustomized = isCustomized;
    }

    public boolean isToBeSaved() {
        return isToBeSaved;
    }

    public void setIsToBeSaved(boolean isToBeSaved) {
        this.isToBeSaved = isToBeSaved;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCategories() {
        return categories;
    }

    public String getInfoText() {
        return infoText;
    }
}
