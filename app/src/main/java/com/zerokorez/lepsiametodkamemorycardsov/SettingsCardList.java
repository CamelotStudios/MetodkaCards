package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.general.Manageable;
import com.zerokorez.general.Interactive;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsCardList implements Drawable, Interactive, Manageable {
    private boolean state;
    private HashMap<String, SettingsCard> cards;
    private ArrayList<String> order;
    private Rect scrollerRect;

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

    private Rect cardRect;
    private Rect textRect;
    private Rect backgroundRect;

    private Rect upperRect;
    private Rect downerRect;

    public void addCard(String text) {
        order.add(text);
        cards.put(text, new SettingsCard(this, text));
        columnHeight += Global.HEIGHT*2f/18f;

        scrollerMoveRatio = -((permanentY+movementY)/columnHeight);
        scrollerSizeRatio = (columnHeight-rectHeight)/columnHeight;
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        }
    }

    public SettingsCardList() {
        order = new ArrayList<>();
        cards = new HashMap<>();
        state = true;

        float y = Global.HEIGHT/18f;
        float x = Global.WIDTH/40f;

        scrollerRect = new Rect(Global.WIDTH*39/40, (int) (y*2), Global.WIDTH, Global.HEIGHT);
        cardRect = new Rect((int) (x*2), (int) (y*3), (int) (Global.WIDTH - x*3), (int) (y*5)+1);
        textRect = new Rect((int) (x*5), (int) (cardRect.top + x), (int) (Global.WIDTH - x*6), (int) (cardRect.bottom - x));
        backgroundRect = new Rect((int) (x*2), cardRect.top, (int) (Global.WIDTH - x*3), (int) (Global.HEIGHT - y));

        upperRect = new Rect(0, (int) (y*2), backgroundRect.right, (int) (y*3));
        downerRect = new Rect(0, (int) (Global.HEIGHT - y), backgroundRect.right, Global.HEIGHT);

        movementY = 0;
        permanentY = 0;
        maxMovementY = 0;

        touchHeight = 0;
        moveHeight = 0;

        columnHeight = x*2;

        rectHeight = Global.HEIGHT*16/18f;

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

    public float getMaxMovementY() {
        return maxMovementY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(backgroundRect, Constants.C5F_PAINT);
        canvas.drawRect(scrollerRect, Constants.SCROLLER_BACKGROUND_PAINT);
        canvas.drawRect(scrollerRect.left, scrollerRect.top + scrollerMoveRatio*Global.HEIGHT*16/18, scrollerRect.right, (scrollerRect.bottom-scrollerRect.top)*scrollerSizeRatio + scrollerRect.top + scrollerMoveRatio*Global.HEIGHT*16/18, Constants.C2F_PAINT);
        int index = 0;
        float movement = permanentY + movementY;
        for (String name : order) {
            cards.get(name).setMoveY(movement + Global.HEIGHT*index*2/18f);
            cards.get(name).draw(canvas);
            index++;
        }
        canvas.drawRect(upperRect, Constants.C4F_PAINT);
        canvas.drawRect(downerRect, Constants.C4F_PAINT);
        canvas.drawRect(backgroundRect, Constants.WHITE_BORDER_PAINT);
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
    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public boolean getState() {
        return state;
    }

    @Override
    public boolean receiveTouch(MotionEvent event) {
        if (state && !Global.SELECTED) {
            int action = event.getAction();
            float y = event.getY();

            if (!goingBack) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (y > Global.HEIGHT * 2 / 18f) {
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
                        if (touchHeight > Global.HEIGHT * 2 / 18f) {
                            moveHeight = y;
                            maxMovementY = Math.max(maxMovementY, Math.abs(moveHeight - touchHeight));

                            upSpeed = 0;
                            downSpeed = 0;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touchHeight > Global.HEIGHT * 2 / 18f) {
                            upTime = System.currentTimeMillis();
                            if (upTime - downTime < 250 && maxMovementY > Global.HEIGHT / 48f) {
                                momentum = (moveHeight - touchHeight) / (upTime - downTime) * 50;
                                decreaseMomentum = -(momentum / 30f * (upTime - downTime) / 100f);
                            }
                            downTime = 0;
                            upTime = downTime;

                            touchHeight = 0;
                            moveHeight = touchHeight;
                            permanentY += movementY;

                            if (permanentY > 0) {
                                downSpeed = -(permanentY / fps);
                                goingBack = true;
                            } else if (columnHeight - rectHeight > 0) {
                                if (permanentY < -(columnHeight - rectHeight)) {
                                    upSpeed = -((permanentY + (columnHeight - rectHeight)) / fps);
                                    goingBack = true;
                                }
                            } else if (columnHeight - rectHeight <= 0) {
                                if (permanentY < 0) {
                                    upSpeed = -(permanentY / fps);
                                    goingBack = true;
                                }
                            }
                        }
                        break;
                }
            }
            movementY = moveHeight - touchHeight;
        }
        return false;
    }

    public void setSameFontSize() {
        ArrayList<Float> sizes = new ArrayList<>();
        for (SettingsCard card : cards.values()) {
            sizes.add(card.getTextPaint().getTextSize());
        }
        float min = Constants.getMinFloat(sizes);
        for (SettingsCard card : cards.values()) {
            card.getTextPaint().setTextSize(min);
        }
    }

    public Rect getCardRect() {
        return cardRect;
    }
    public Rect getTextRect() {
        return textRect;
    }
    public ArrayList<String> getOrder() {
        return order;
    }
    public SettingsCard getSettingsCard(String name) {
        return cards.get(name);
    }
}
