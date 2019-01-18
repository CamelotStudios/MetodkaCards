package com.zerokorez.storageloader;

import com.zerokorez.general.Global;

public class Card {
    private Group group;

    private String index;
    private String value;
    private String text;
    private String note;
    private String funFact;

    private boolean isKnown;
    private int wrongCount;
    private int rightCount;

    public Card(Group group, String lineData, String userData) {
        this.group = group;
        String[] strings = lineData.split("~~");
        if (strings.length >= 4) {
            index = strings[0];
            value = strings[1];
            text = strings[2];
            note = strings[3];
            if (strings.length == 5) {
                funFact = strings[4];
            } else {
                funFact = null;
            }
            if (userData != null) {
                strings = userData.split("~~");
                if (strings.length == 3) {
                    isKnown = Global.compareStrings(strings[0], "1");
                    wrongCount = Integer.valueOf(strings[1]);
                    rightCount = Integer.valueOf(strings[2]);
                } else {
                    isKnown = false;
                    wrongCount = 0;
                    rightCount = 0;
                }
            } else {
                isKnown = false;
                wrongCount = 0;
                rightCount = 0;
            }
            group.getCards().add(this);
        }
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void toggleIsKnown() {
        isKnown = !isKnown;
        group.setIsToBeSaved(true);
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public Group getGroup() {
        return group;
    }

    public String getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public String getNote() {
        return note;
    }

    public boolean isKnown() {
        return isKnown;
    }

    public String getFunFact() {
        return funFact;
    }

    public int getRightCount() {
        return rightCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }
}
