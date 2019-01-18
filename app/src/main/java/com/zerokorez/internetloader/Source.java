package com.zerokorez.internetloader;

public class Source {
    private boolean isProper;
    private boolean isAvailable;
    private String version;
    private String packageName;
    private String url;

    public Source(String lineData) {
        isAvailable = !lineData.contains("///");
        String[] strings = lineData.split("<v>");
        if (strings.length == 2) {
            strings[0].replaceAll("///", "");
            version = strings[0];
            strings = strings[1].split("###");
            if (strings.length == 2) {
                url = strings[1];
                strings = strings[0].split("~~");
                if (strings.length == 2) {
                    packageName = strings[0];
                    isProper = true;
                } else {
                    isProper = false;
                }
            } else {
                isProper = false;
            }
        } else {
            isProper = false;
        }
    }

    public boolean isProper() {
        return isProper;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public String getPackageName() {
        return packageName;
    }
    public String getUrl() {
        return url;
    }
    public String getVersion() {
        return version;
    }
}
