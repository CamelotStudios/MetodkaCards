package com.zerokorez.storageloader;

import com.zerokorez.general.Global;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadManager {
    public LoadManager() { }

    public void setUpConstants() { }

    public void setActiveGroup(int index) {
        Global.ACTIVE_GROUP = Math.min(Global.GROUPS.size()-1, index);
    }

    public ArrayList<Group> getLoadedGroups() {
        return Global.GROUPS;
    }

    public Group getActiveGroup() {
        return Global.GROUPS.get(Global.ACTIVE_GROUP);
    }

    public void loadGroups() {
        Global.GROUPS.clear();
        Global.ACTIVE_GROUP = 0;
        ArrayList<String> packages = readFromFile(Global.openFile(Global.BASE));
        if (packages != null) {
            for (String name : packages) {
                String[] strings = name.split("<v>");
                if (strings.length == 2) {
                    File file = Global.openFile(Global.DATA + strings[1]);
                    if (file.isFile()) {
                        ArrayList<String> data = readFromFile(file);
                        file = Global.openFile(Global.DATA + strings[1] + "~userdata~");
                        ArrayList<String> user = readFromFile(file);
                        String userLine;
                        if (user != null) {
                            if (user.size() == 1) {
                                userLine = user.get(0);
                            } else {
                                userLine = null;
                            }
                        } else {
                            userLine = null;
                        }
                        if (data != null) {
                            new Group(data.get(0), this, strings[0], userLine);
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, String> readPackages() {
        ArrayList<String> lines = readFromFile(Global.openFile(Global.BASE));
        HashMap<String, String> hashMap = new HashMap<>();
        if (lines != null) {
            for (String line : lines) {
                String[] strings = line.split("<v>");
                if (strings.length == 2) {
                    hashMap.put(strings[1], strings[0]);
                }
            }
        }
        return hashMap;
    }

    public void writePackage(String versionPackageName, ArrayList<String> data) {
        String[] strings = versionPackageName.split("<v>");
        if (strings.length == 2) {
            String string = strings[1];
            File file = Global.createFile(Global.DATA + string);
            if (file != null) {
                writeToFile(file, data);
                file = Global.openFile(Global.BASE);
                ArrayList<String> lines = readFromFile(file);
                if (lines == null) {
                    lines = new ArrayList<>();
                }
                ArrayList<String> newLines = new ArrayList<>();
                boolean wasInserted = false;
                for (String line : lines) {
                    strings = line.split("<v>");
                    if (strings.length == 2) {
                        if (Global.compareStrings(string, strings[1])) {
                            newLines.add(versionPackageName);
                            wasInserted = true;
                        } else {
                            newLines.add(line);
                        }
                    }
                }
                if (!wasInserted) {
                    newLines.add(versionPackageName);
                }
                writeToFile(file, newLines);
            }
        }
    }

    public void removePackage(String packageName) {
        Global.deleteFile(Global.DATA + packageName);
        Global.deleteFile(Global.DATA + packageName + "~userdata~");
        File file = Global.openFile(Global.BASE);
        ArrayList<String> lines = readFromFile(file);
        if (lines == null) {
            lines = new ArrayList<>();
        }
        ArrayList<String> newLines = new ArrayList<>();
        for (String line : lines) {
            String[] strings = line.split("<v>");
            if (strings.length == 2) {
                if (!Global.compareStrings(packageName, strings[1])) {
                    newLines.add(line);
                }
            }
        }
        writeToFile(file, newLines);
    }

    private void writeToFile(File file, ArrayList<String> lines) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String line = "";
            for (String string : lines) {
                if (line.length() > 0) {
                    line += "\n" + string;
                } else {
                    line += string;
                }
            }
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public ArrayList<String> readFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ( (line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public void saveGroupCustomization(Group group) {
        String packageLine = group.getPackageName() + "~~" + group.getFullName() + "::";
        int index = 0;
        for (Card card : group.getCards()) {
            packageLine += ((index > 0) ? "@@" : "") + card.getIndex() + "~~" + card.getValue() + "~~" + card.getText() + "~~" + card.getNote() + ((card.getFunFact() != null) ? "~~" + card.getFunFact() : "");
            index++;
        }
        ArrayList<String> data = new ArrayList<>();
        packageLine += "::" + group.getAuthorName() + "~~" + group.getCategories() + "~~" + group.getInfoText();
        data.add(packageLine);
        File file = Global.openFile(Global.DATA + group.getPackageName());
        writeToFile(file, data);
        group.setIsCustomized(false);
        file = Global.openFile(Global.BASE);
        data = readFromFile(file);
        ArrayList<String> newLines = new ArrayList<>();
        for (String line : data) {
            if (Global.compareStrings(group.getPackageName(), line.split("<v>")[1])) {
                newLines.add(group.getVersion().split(":")[0] + ":private<v>" + group.getPackageName());
            } else {
                newLines.add(line);
            }
        }
        writeToFile(file, newLines);
    }

    public void saveUserData(Group group) {
        String userdata = "";
        int index = 0;
        for (Card card : group.getCards()) {
            userdata += ((index > 0) ? "@@" : "") + ((card.isKnown()) ? "1" : "0") + "~~" + card.getRightCount() + "~~" + card.getWrongCount();
            index++;
        }
        ArrayList<String> data = new ArrayList<>();
        userdata += "::" + group.getLastStudiedTime() + "_" + group.getLastStudiedDate();
        data.add(userdata);
        File file = Global.openFile(Global.DATA + group.getPackageName() + "~userdata~");
        writeToFile(file, data);
        group.setIsToBeSaved(false);
    }

    public void loadLanguagePack(String name) {
        Global.LANGUAGE = new HashMap<>();
        ArrayList<String> data = Global.readAssets(name);
        if (data != null) {
            if (data.size() == 1) {
                for (String phrase : data.get(0).split("::")){
                    String[] strings = phrase.split("~~");
                    if (strings.length == 2) {
                        Global.LANGUAGE.put(strings[0], strings[1]);
                    }
                }
            }
        }
    }
}
