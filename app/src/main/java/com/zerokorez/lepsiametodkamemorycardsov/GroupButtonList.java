package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.general.Global;
import com.zerokorez.general.Manageable;

import java.util.ArrayList;

public class GroupButtonList implements Manageable {
    private ArrayList<GroupButton> buttons;
    private Boolean state;

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

    public GroupButtonList() {
        buttons = new ArrayList<>();
        state = true;

        scrollerRect = new Rect(Global.WIDTH*39/40, Global.HEIGHT/6, Global.WIDTH, Global.HEIGHT);

        movementY = 0;
        permanentY = 0;
        maxMovementY = 0;

        touchHeight = 0;
        moveHeight = 0;

        columnHeight = Global.HEIGHT/48f*(buttons.size()+1) + Global.HEIGHT/6f*buttons.size();
        rectHeight = Global.HEIGHT*5/6f;

        scrollerSizeRatio = rectHeight/columnHeight;
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        }

        scrollerMoveRatio = -((permanentY+movementY)/columnHeight);
        downSpeed = 0;
        upSpeed = 0;

        fps = 10f;
        goingBack = false;

        downTime = 0;
        upTime = 0;
        momentum = 0;
        decreaseMomentum = 0;
    }

    public float getMovementY() {
        return maxMovementY;
    }

    public ArrayList<GroupButton> getButtons() {
        return buttons;
    }

    public void setState(Boolean newState) {
        state = newState;
    }

    @Override
    public void setState(boolean state) {
        this.state = state;
    }
    @Override
    public boolean getState() {
        return state;
    }

    public void add(GroupButton groupButton) {
        buttons.add(groupButton);
        columnHeight = Global.HEIGHT/48f*(buttons.size()+1) + Global.HEIGHT/6f*buttons.size();
    }

    public void draw(Canvas canvas) {
        update();
        canvas.drawRect(scrollerRect, Constants.SCROLLER_BACKGROUND_PAINT);
        canvas.drawRect(scrollerRect.left, scrollerRect.top + scrollerMoveRatio*Global.HEIGHT*5/6, scrollerRect.right, (scrollerRect.bottom-scrollerRect.top)*scrollerSizeRatio + scrollerRect.top + scrollerMoveRatio*Global.HEIGHT*5/6, Constants.C2F_PAINT);

        int index = 0;
        for (GroupButton button: buttons) {
            float newY = Global.HEIGHT / 48f * (index + 1) + Global.HEIGHT / 6f * (index + 1) + movementY + permanentY;
            if(button.getTop() + newY < Global.HEIGHT*7/6f || button.getBottom() + newY > 0) {
                button.draw(canvas, newY);
                index++;
            }
        }
    }

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

    public boolean receiveTouch(MotionEvent event) {
        if (state && !Global.SELECTED) {
            int action = event.getAction();
            float y = event.getY();

            if (!goingBack) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (y > Global.HEIGHT / 6f) {
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
                        if (touchHeight > Global.HEIGHT / 6f) {
                            moveHeight = y;
                            maxMovementY = Math.max(maxMovementY, Math.abs(moveHeight - touchHeight));

                            upSpeed = 0;
                            downSpeed = 0;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touchHeight > Global.HEIGHT / 6f) {
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

            for (GroupButton button: buttons) {
                if (button.receiveTouch(event)) {
                    Global.STORAGE_LOADER.setActiveGroup(buttons.indexOf(button));
                    return true;
                }
            }
        }
        return false;
    }
}
