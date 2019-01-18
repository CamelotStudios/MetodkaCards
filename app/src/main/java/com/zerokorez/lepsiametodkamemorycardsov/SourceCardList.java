package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Manageable;
import com.zerokorez.general.Interactive;
import com.zerokorez.internetloader.Source;

import java.util.ArrayList;
import java.util.HashMap;

public class SourceCardList implements Drawable, Manageable, Interactive {
    private ArrayList<Source> sources;
    private ArrayList<SourceCard> cards;
    private Boolean state;

    private com.zerokorez.internetloader.LoadManager internetLoader;
    private com.zerokorez.storageloader.LoadManager storageLoader;

    private Rect scrollerRect;
    private SourceCardListBrowser browser;

    private float movementY;
    private float permanentY;
    private float maxMovementY;

    private float touchHeight;
    private float moveHeight;

    private float columnHeight;
    private float rectHeight;

    private float scrollerSizeRatio;
    private float scrollerMoveRatio;

    private float downSpeed;
    private float upSpeed;

    private float fps;
    private boolean goingBack;

    private long downTime;
    private long upTime;
    private float momentum;
    private float decreaseMomentum;

    public SourceCardList(ArrayList<Source> sources) {
        this.sources = sources;
        HashMap<String, String> packages = Global.STORAGE_LOADER.readPackages();

        this.cards = new ArrayList<>();
        browser = new SourceCardListBrowser();
        state = true;

        internetLoader = Global.INTERNET_LOADER;
        storageLoader = Global.STORAGE_LOADER;

        float y = Global.HEIGHT/18f;
        float x = Global.WIDTH/40f;

        scrollerRect = new Rect(Global.WIDTH*39/40, (int) (y*5), Global.WIDTH, Global.HEIGHT);

        movementY = 0;
        permanentY = 0;
        maxMovementY = 0;

        touchHeight = 0;
        moveHeight = 0;

        columnHeight = x*2;
        for (Source source : sources) {
            String state = "Not downloaded yet";
            if (packages != null) {
                if (packages.containsKey(source.getPackageName())) {
                    String[] strings = packages.get(source.getPackageName()).split(":");
                    if (strings.length == 2) {
                        if (Global.compareStrings(strings[1], "official")) {
                            if (Global.compareStrings(strings[0], source.getVersion().split(":")[0])) {
                                state = "Up-to-date";
                            } else {
                                state = "Outdated";
                            }
                        } else {
                            state = "Customized";
                        }
                    }
                }
            }
            SourceCard card = new SourceCard(source, state, columnHeight);
            columnHeight += card.getHeight() + x*2;
            cards.add(card);
        }

        rectHeight = Global.HEIGHT*13/18f;

        scrollerSizeRatio = (columnHeight-rectHeight)/columnHeight;
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        }

        scrollerMoveRatio = -((permanentY+movementY)/columnHeight);
        downSpeed = 0;
        upSpeed = 0;

        fps = 10;
        goingBack = false;

        downTime = 0;
        upTime = 0;
        momentum = 0;
        decreaseMomentum = 0;
    }

    @Override
    public void setState(boolean state) {
        this.state = state;
    }
    @Override
    public boolean getState() {
        return state;
    }

    @Override
    public void draw(Canvas canvas) {
        update();
        canvas.drawRect(scrollerRect, Constants.SCROLLER_BACKGROUND_PAINT);
        canvas.drawRect(scrollerRect.left, scrollerRect.top + scrollerMoveRatio*Global.HEIGHT*13/18, scrollerRect.right, (scrollerRect.bottom-scrollerRect.top)*scrollerSizeRatio + scrollerRect.top + scrollerMoveRatio*Global.HEIGHT*13/18, Constants.C2F_PAINT);

        for (SourceCard card: cards) {
            card.setMoveY(permanentY + movementY);
            card.draw(canvas);
        }

        browser.draw(canvas);
    }

    @Override
    public void update() {
        if(goingBack) {
            if (permanentY > 0) {
                if (downSpeed < 0) {
                    permanentY = Math.max(0, permanentY + downSpeed);
                    if (permanentY == 0) {
                        goingBack = false;
                    }
                }
            } else if (columnHeight - rectHeight > 0) {
                if (permanentY < -(columnHeight - rectHeight) && upSpeed > 0) {
                    permanentY = Math.min(-(columnHeight - rectHeight), permanentY + upSpeed);
                    if (permanentY == -(columnHeight - rectHeight)) {
                        goingBack = false;
                    }
                }
            } else if (columnHeight - rectHeight < 0) {
                if (permanentY < 0 && upSpeed > 0) {
                    permanentY = Math.min(0, permanentY + upSpeed);
                    if (permanentY == 0) {
                        goingBack = false;
                    }
                }
            }
        } else if (permanentY <= 0 && permanentY >= -(columnHeight - rectHeight)) {
            goingBack = false;
            downSpeed = 0;
            upSpeed = 0;
        }
        if (momentum != 0 && !goingBack) {
            if (momentum < 0) {
                if (decreaseMomentum > 0) {
                    if (columnHeight-rectHeight > 0) {
                        permanentY = Math.max(-(columnHeight - rectHeight), permanentY + momentum);
                    } else if (columnHeight-rectHeight <= 0) {
                        permanentY = Math.max(0, permanentY + momentum);
                    }
                    momentum = Math.min(0, momentum + decreaseMomentum);
                    if (momentum == 0) {
                        decreaseMomentum = 0;
                    }
                } else {
                    decreaseMomentum = -(momentum/30f);
                }
            } else if (momentum > 0) {
                if (decreaseMomentum < 0) {
                    permanentY = Math.min(0, permanentY + momentum);
                    momentum = Math.max(0, momentum + decreaseMomentum);
                    if (momentum == 0) {
                        decreaseMomentum = 0;
                    }
                } else {
                    decreaseMomentum = -(momentum/30f);
                }
            } else {
                momentum = 0;
                decreaseMomentum = 0;
                downTime = 0;
                upTime = 0;
            }
        } else if (momentum != 0 && goingBack) {
            momentum = 0;
            decreaseMomentum = 0;
            downTime = 0;
            upTime = 0;
        }
        scrollerSizeRatio = rectHeight/columnHeight;
        if(scrollerSizeRatio > 1) {
            scrollerSizeRatio = 1;
        }
        scrollerMoveRatio = -((permanentY+movementY)/columnHeight);
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (state && !Global.SELECTED) {
            int action = event.getAction();
            float y = event.getY();

            if (!goingBack) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (y > Global.HEIGHT*5/18f) {
                            touchHeight = y;
                            moveHeight = touchHeight;
                            maxMovementY = 0;

                            downTime = System.currentTimeMillis();
                            momentum = 0;
                            decreaseMomentum = momentum;
                            upTime = 0;

                            upSpeed = 0;
                            downSpeed = 0;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (touchHeight > Global.HEIGHT*5/18f) {
                            moveHeight = y;
                            maxMovementY = Math.max(maxMovementY, Math.abs(moveHeight - touchHeight));

                            upSpeed = 0;
                            downSpeed = 0;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touchHeight > Global.HEIGHT*5/18f) {
                            upTime = System.currentTimeMillis();
                            if (upTime-downTime < 250 && maxMovementY > Global.HEIGHT/48f) {
                                momentum = (moveHeight - touchHeight) / (upTime - downTime) * 50;
                                decreaseMomentum = -(momentum / 30f * (upTime - downTime) / 100f);
                            }
                            downTime = 0;
                            upTime = downTime;

                            touchHeight = 0;
                            moveHeight = touchHeight;
                            permanentY += movementY;

                            if (permanentY > 0) {
                                downSpeed = -(permanentY/fps);
                                goingBack = true;
                            } else if (columnHeight - rectHeight > 0) {
                                if (permanentY < -(columnHeight - rectHeight)) {
                                    upSpeed = -((permanentY+(columnHeight-rectHeight))/fps);
                                    goingBack = true;
                                }
                            } else if (columnHeight - rectHeight <= 0) {
                                if (permanentY < 0) {
                                    upSpeed = -(permanentY/fps);
                                    goingBack = true;
                                }
                            }
                        }
                        break;
                }
            }
            movementY = moveHeight - touchHeight;

            if (y > Global.HEIGHT*5/18f) {
                boolean needReset = false;
                for (SourceCard card : cards) {
                    card.setMaxMoveY(maxMovementY);
                    String note = card.getNote();
                    if (card.receiveMainTouch(event)) {
                        if (note == "Not downloaded yet" || note == "Customized" || note == "Outdated") {
                            Constants.NOTIFICATOR.putNotification("Downloading", " " + card.getSource().getPackageName() + ".", 2000);
                            ArrayList<String> data = internetLoader.readInternetFile(card.getSource().getUrl());
                            if (data != null) {
                                storageLoader.writePackage(card.getSource().getVersion() + "<v>" + card.getSource().getPackageName(), data);
                                needReset = true;
                            }
                        }
                    }
                    if (card.receiveRemoveTouch(event)) {
                        if (note == "Up-to-date" || note == "Outdated" || note == "Customized" || note == "Unavailable") {
                            Constants.NOTIFICATOR.putNotification("Removed", " " + card.getSource().getPackageName() + ".", 2000);
                            storageLoader.removePackage(card.getSource().getPackageName());
                            needReset = true;
                        }
                    }
                }
                if (needReset) {
                    resetCards();
                }
            }
            browser.receiveTouch(event);

        }
        return false;
    }

    public void setSameFontSize() {
        ArrayList<Float> indexSizes = new ArrayList<>();
        ArrayList<Float> valueSizes = new ArrayList<>();
        for (SourceCard card : cards) {
            indexSizes.add(card.getNamePaint().getTextSize());
            valueSizes.add(card.getNamePaint().getTextSize());
        }
        float minIndex = Constants.getMinFloat(indexSizes);
        float minValue = Constants.getMinFloat(valueSizes);
        for (SourceCard card : cards) {
            card.getNamePaint().setTextSize(minIndex);
            card.getNamePaint().setTextSize(minValue);
        }
    }

    private void resetCards() {
        HashMap<String, String> packages = storageLoader.readPackages();
        cards.clear();
        float x = Global.WIDTH/40f;
        columnHeight = x*2;
        for (Source source : sources) {
            String state = "Not downloaded yet";
            if (packages != null) {
                if (packages.containsKey(source.getPackageName())) {
                    String[] strings = packages.get(source.getPackageName()).split(":");
                    if (strings.length == 2) {
                        if (Global.compareStrings(strings[1], "official")) {
                            if (Global.compareStrings(strings[0], source.getVersion().split(":")[0])) {
                                state = "Up-to-date";
                            } else {
                                state = "Outdated";
                            }
                        } else {
                            state = "Customized";
                        }
                    }
                }
            }
            SourceCard card = new SourceCard(source, state, columnHeight);
            columnHeight += card.getHeight() + x*2;
            cards.add(card);
        }
        scrollerSizeRatio = (columnHeight-rectHeight)/columnHeight;
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        }
        setSameFontSize();
    }
}