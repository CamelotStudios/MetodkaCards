package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.zerokorez.storageloader.Card;
import com.zerokorez.general.Global;
import com.zerokorez.general.Drawable;
import com.zerokorez.storageloader.Group;
import com.zerokorez.textparser.Window;

public class CardButton implements Drawable {
    private Group group;
    private int number;

    private Rect rect;
    private Paint rectPaint;
    private Paint borderPaint;

    private boolean onTouch;
    private boolean onClick;

    private boolean state;

    private String indicatorText;
    private Rect indicatorTextRect;
    private Paint indicatorTextPaint;

    private Window indexValueWindow;
    private Rect textRect;

    private String noteText;
    private Rect noteTextRect;
    private Paint noteTextPaint;

    private float permanentX;
    private float movementX;
    private float maxMovementX;
    private float touchWidth;
    private float moveWidth;

    private boolean goingBackX;
    private boolean goingBackY;
    private float speedX;
    private boolean changingCard;
    private float fps;

    private boolean isTurned;
    private boolean turningCard;
    private boolean turningState;
    private float turningSpeed;

    private static int LEFT;
    private static int RIGHT;
    private static int CENTER;

    private TextWindow textWindow;
    private ImageButton starButton;

    private Rect upperBackgroundRect;
    private Rect downerBackgroundRect;
    private Rect upperCardRect;
    private Rect downerCardRect;

    private float movementY;
    private float permanentY;
    private float maxMovementY;
    private float touchHeight;
    private float moveHeight;

    private float downSpeed;
    private float upSpeed;
    private long downTime;
    private long upTime;
    private float momentum;
    private float decreaseMomentum;

    public CardButton(Group group, ImageButton starButton) {
        this.group = group;
        number = 0;

        float x = Global.WIDTH/33f;
        float top = Global.HEIGHT/18f;

        rect = new Rect((int) (x*2), (int) (top + x*3), (int) (Global.WIDTH - x*2), (int) (Global.HEIGHT - x*13));
        upperBackgroundRect = new Rect(0, 0, Global.WIDTH, (int) (top + x*3));
        downerBackgroundRect = new Rect(0, (int) (Global.HEIGHT - x*13), Global.WIDTH, Global.HEIGHT);

        rectPaint = new Paint();
        rectPaint.setColor(Constants.WHITE);
        borderPaint = new Paint();
        borderPaint.setColor(Constants.C2F);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        LEFT = (int) (x*2);
        RIGHT = (int) (Global.WIDTH - x*2);
        CENTER = (int) ((RIGHT-LEFT)/2f + LEFT);

        state = true;
        onTouch = false;
        onClick = false;

        indicatorTextRect = new Rect(rect.left, rect.top, rect.right, (int) (rect.top + x*3));
        indicatorTextPaint = new Paint();
        indicatorTextPaint.setColor(Constants.C4F);

        textRect = new Rect((int) (x*4), (int) (top + x*8), (int) (Global.WIDTH - x*4), (int) (Global.HEIGHT - x*18));

        noteTextRect = new Rect(rect.left, (int) (rect.bottom - x*3), rect.right, rect.bottom);
        noteTextPaint = new Paint();
        noteTextPaint.setColor(Constants.C5F);

        resetIndicatorTextPaint();
        resetIndexValueWindow();
        resetNoteTextPaint();

        permanentX = 0;
        movementX = 0;
        maxMovementX = 0;
        touchWidth = 0;
        moveWidth = 0;

        goingBackX = false;
        goingBackY = false;
        changingCard = false;
        speedX = 0;
        fps = 10f;

        isTurned = false;
        turningCard = false;
        turningSpeed = (rect.right-rect.left)/15f;
        turningState = false;

        textWindow = new TextWindow(textRect, x, top);
        upperCardRect = new Rect((int) (x*4), (int) (top + x*3), rect.right, (int) (top + x*8));
        downerCardRect = new Rect((int) (x*4), (int) (Global.HEIGHT - x*18), rect.right, (int) (Global.HEIGHT - x*13));

        textWindow.setText(group.getCards().get(number).getText());
        this.starButton = starButton;
        updateStarButton();

        movementY = 0;
        permanentY = 0;
        maxMovementY = 0;
        touchHeight = 0;
        moveHeight = 0;

        downSpeed = 0;
        upSpeed = 0;

        downTime = 0;
        upTime = 0;
        momentum = 0;
        decreaseMomentum = 0;
    }

    public void setNumber(int number) {
        this.number = number;
        resetIndicatorTextPaint();
        resetIndexValueWindow();
        resetNoteTextPaint();
        textWindow.setText(group.getCards().get(number).getText());
        updateStarButton();
    }

    public Card getCard() {
        return group.getCards().get(number);
    }

    public void nextNumber(boolean right) {
        if (right) {
            if (number < group.getCards().size()) {
                number++;
                if (number == group.getCards().size()) {
                    number = 0;
                }
            }
        } else {
            if (number > -1) {
                number--;
                if (number == -1) {
                    number = group.getCards().size()-1;
                }
            }
        }
        permanentY = 0;
        isTurned = false;
        resetIndicatorTextPaint();
        resetIndexValueWindow();
        resetNoteTextPaint();
        textWindow.setText(group.getCards().get(number).getText());
        updateStarButton();
        textWindow.recycle();
    }

    public void resetIndicatorTextPaint() {
        indicatorText = Integer.toString(number + 1) + " / " + Integer.toString(group.getCards().size());
        indicatorTextPaint.setTextSize(Constants.getTextSize(indicatorTextRect, indicatorTextPaint, indicatorText));
    }

    public void resetIndexValueWindow() {
        indexValueWindow = new Window(group.getCards().get(number).getIndex() + "<#index;V~35>" + group.getCards().get(number).getValue() + "<#value;V~40>´D~1;C", textRect, Global.HEIGHT/36f);
        indexValueWindow.setMoveY(textRect.height()*13f/30f - indexValueWindow.getColumnHeight()/2);
        indexValueWindow.update();
    }

    public void resetNoteTextPaint() {
        noteText = "* " + group.getCards().get(number).getNote();
        noteTextPaint.setTextSize(Constants.getTextSize(noteTextRect, noteTextPaint, noteText));
    }

    public void receiveTouch(MotionEvent event) {
        if (state && !Global.SELECTED) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();

            if (!goingBackX && !changingCard && !turningCard) {
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        if (rect.contains((int) point[0], (int) point[1]) && onTouch) {
                            onClick = true;
                            onTouch = false;

                            if ((maxMovementX < Global.WIDTH / 64f) && (maxMovementY < Global.HEIGHT / 144f)) {
                                onClick = false;
                                turningCard = true;
                                turningState = true;
                                maxMovementX = 0;
                                maxMovementY = 0;
                            } else {
                                onClick = false;
                                maxMovementX = 0;
                                maxMovementY = 0;
                            }
                        } else {
                            onClick = false;
                            onTouch = false;
                            maxMovementX = 0;
                            maxMovementY = 0;
                        }
                        if (touchHeight != 0 && isTurned) {
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

                            float height = textWindow.getColumnHeight() - textWindow.getRectHeight();
                            if (permanentY > 0) {
                                downSpeed = -(permanentY/fps);
                                goingBackY = true;
                            } else if (height > 0) {
                                if (permanentY < -(height)) {
                                    upSpeed = -((permanentY+(height))/fps);
                                    goingBackY = true;
                                }
                            } else if (height <= 0) {
                                if (permanentY < 0) {
                                    upSpeed = -(permanentY/fps);
                                    goingBackY = true;
                                }
                            }
                        }
                        if (!goingBackX && touchWidth != 0) {
                            goingBackX = true;
                            permanentX = moveWidth - touchWidth;
                            touchWidth = 0;
                            moveWidth = 0;
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (rect.contains((int) point[0], (int) point[1]) && !goingBackX) {
                            onTouch = true;

                            touchWidth = point[0];
                            moveWidth = touchWidth;

                            if (isTurned) {
                                touchHeight = point[1];
                                moveHeight = touchHeight;

                                downTime = System.currentTimeMillis();
                                momentum = 0;
                                decreaseMomentum = momentum;
                                upTime = 0;

                                upSpeed = 0;
                                downSpeed = 0;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (rect.contains((int) point[0], (int) point[1]) && onTouch) {
                            if (!goingBackX && touchWidth != 0 && !onClick && !changingCard) {
                                if (isTurned && touchHeight != 0) {
                                    if (textWindow.getRect().contains((int) touchWidth, (int) touchHeight)) {
                                        if (maxMovementX < Global.WIDTH / 64f) {
                                            moveHeight = point[1];
                                            maxMovementY = Math.max(maxMovementY, Math.abs(touchHeight - moveHeight));

                                            upSpeed = 0;
                                            downSpeed = 0;
                                        }
                                    }
                                    if (maxMovementY < Global.HEIGHT / 144f && Math.abs(touchWidth - point[0]) > Global.WIDTH / 64f) {
                                        moveWidth = point[0];
                                        maxMovementX = Math.max(maxMovementX, Math.abs(touchWidth - moveWidth));
                                    }
                                } else if (!isTurned){
                                    moveWidth = point[0];
                                    maxMovementX = Math.max(maxMovementX, Math.abs(touchWidth - moveWidth));
                                }
                            }
                        } else if (!rect.contains((int) point[0], (int) point[1]) && onTouch) {
                            onTouch = false;
                        }
                        break;
                }
                movementX = moveWidth - touchWidth;
                if (isTurned) {
                    if (textWindow.getRect().contains((int) touchWidth, (int) touchHeight)) {
                        movementY = moveHeight - touchHeight;
                    } else {
                        movementY = 0;
                    }
                }
            }
            Global.SELECTED = onTouch || onClick || changingCard;

            if (permanentX != 0 && goingBackX) {
                speedX = -(permanentX/fps);
                if (Math.abs(permanentX) > Global.WIDTH/3f) {
                    changingCard = true;
                    goingBackX = false;
                    speedX = (Global.WIDTH/fps)*(permanentX/Math.abs(permanentX));
                } else {
                    speedX *= 3;
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        update();
        Rect newRect =  Global.moveRect(rect, (int) (movementX + permanentX), 0);
        canvas.drawRect(newRect, rectPaint);

        if (!turningCard && !isTurned) {
            indexValueWindow.setMoveX(movementX + permanentX);
            indexValueWindow.draw(canvas);
        } else if (!turningCard){
            textWindow.setMoveX((int) (movementX + permanentX));
            textWindow.draw(canvas);
            textWindow.drawScroller(canvas);

            canvas.drawRect(Global.moveRect(upperBackgroundRect, (int) (movementX + permanentX), 0), Constants.C5F_PAINT);
            canvas.drawRect(Global.moveRect(downerBackgroundRect, (int) (movementX + permanentX), 0), Constants.C5F_PAINT);

            canvas.drawRect(Global.moveRect(upperCardRect, (int) (movementX + permanentX), 0), rectPaint);
            canvas.drawRect(Global.moveRect(downerCardRect, (int) (movementX + permanentX), 0), rectPaint);

            textWindow.drawBorders(canvas);
        }

        if (!turningCard) {
            Constants.drawTextCenter(canvas, Global.moveRect(indicatorTextRect, (int) (movementX + permanentX), 0), indicatorTextPaint, indicatorText);
        }
        if (!turningCard) {
            Constants.drawTextCenter(canvas, Global.moveRect(noteTextRect, (int) (movementX + permanentX), 0), noteTextPaint, noteText);
        }

        canvas.drawLine(rect.left + (movementX + permanentX), indicatorTextRect.bottom, rect.right + (movementX + permanentX), indicatorTextRect.bottom, borderPaint);
        canvas.drawLine(rect.left + (movementX + permanentX), noteTextRect.top, rect.right + (movementX + permanentX), noteTextRect.top, borderPaint);
        canvas.drawRect(newRect, borderPaint);
    }

    @Override
    public void update() {
        if (goingBackX) {
            if (speedX != 0) {
                if (permanentX < 0) {
                    permanentX = Math.min(0, permanentX + speedX);
                    if (permanentX == 0) {
                        goingBackX = false;
                        speedX = 0;
                    }
                } else if (permanentX > 0) {
                    permanentX = Math.max(0, permanentX + speedX);
                    if (permanentX == 0) {
                        goingBackX = false;
                        speedX = 0;
                    }
                }
            } else {
                goingBackX = false;
            }
        } else if (changingCard) {
            if (speedX != 0) {
                if (speedX < 0) {
                    if (permanentX > -Global.WIDTH) {
                        permanentX = Math.max(-Global.WIDTH, permanentX + speedX);
                    } else if (permanentX == -Global.WIDTH) {
                        nextNumber(true);
                        permanentX = Global.WIDTH;
                        changingCard = false;
                        goingBackX = true;
                        speedX = -(permanentX / fps);
                        isTurned = false;
                        textWindow.recycle();
                    }
                } else if (speedX > 0) {
                    if (permanentX < Global.WIDTH) {
                        permanentX = Math.min(Global.WIDTH, permanentX + speedX);
                    } else if (permanentX == Global.WIDTH) {
                        nextNumber(false);
                        permanentX = -Global.WIDTH;
                        changingCard = false;
                        goingBackX = true;
                        speedX = -(permanentX / fps);
                        isTurned = false;
                        textWindow.recycle();
                    }
                }
            } else {
                changingCard = false;
            }
        }
        if (turningCard) {
            if (turningState) {
                rect = new Rect((int) Math.min(CENTER, rect.left + turningSpeed), rect.top, (int) Math.max(CENTER, rect.right - turningSpeed), rect.bottom);
                if (rect.left == CENTER && rect.right == CENTER) {
                    turningState = false;
                    isTurned = !isTurned;
                    if (isTurned) {
                        textWindow.update();
                    } else {
                        textWindow.recycle();
                    }
                }
            } else {
                rect = new Rect((int) Math.max(LEFT, rect.left - turningSpeed), rect.top, (int) Math.min(RIGHT, rect.right + turningSpeed), rect.bottom);
                if (rect.left == LEFT && rect.right == RIGHT) {
                    turningCard = false;
                }
            }
        }
        textWindow.setScrollerMoveRatio(movementY + permanentY);

        if (isTurned) {
            float height = textWindow.getColumnHeight() - textWindow.getRectHeight();
            if (goingBackY) {
                if (permanentY > 0) {
                    if (downSpeed < 0) {
                        permanentY = Math.max(0, permanentY + downSpeed);
                        if (permanentY == 0) {
                            goingBackY = false;
                        }
                    }
                } else if (height > 0) {
                    if (permanentY < -(height) && upSpeed > 0) {
                        permanentY = Math.min(-(height), permanentY + upSpeed);
                        if (permanentY == -(height)) {
                            goingBackY = false;
                        }
                    }
                } else if (height < 0) {
                    if (permanentY < 0 && upSpeed > 0) {
                        permanentY = Math.min(0, permanentY + upSpeed);
                        if (permanentY == 0) {
                            goingBackY = false;
                        }
                    }
                }
            } else if (permanentY <= 0 && permanentY >= -(height)) {
                downSpeed = 0;
                upSpeed = 0;
            }
            if (momentum != 0 && !goingBackY) {
                if (momentum < 0) {
                    if (decreaseMomentum > 0) {
                        if (height > 0) {
                            permanentY = Math.max(-(height), permanentY + momentum);
                        } else if (height <= 0) {
                            permanentY = Math.max(0, permanentY + momentum);
                        }
                        momentum = Math.min(0, momentum + decreaseMomentum);
                        if (momentum == 0) {
                            decreaseMomentum = 0;
                        }
                    } else {
                        decreaseMomentum = -(momentum / 30f);
                    }
                } else if (momentum > 0) {
                    if (decreaseMomentum < 0) {
                        permanentY = Math.min(0, permanentY + momentum);
                        momentum = Math.max(0, momentum + decreaseMomentum);
                        if (momentum == 0) {
                            decreaseMomentum = 0;
                        }
                    } else {
                        decreaseMomentum = -(momentum / 30f);
                    }
                } else {
                    momentum = 0;
                    decreaseMomentum = 0;
                    downTime = 0;
                    upTime = 0;
                }
            } else if (momentum != 0) {
                momentum = 0;
                decreaseMomentum = 0;
                downTime = 0;
                upTime = 0;
            }
            textWindow.setScrollerMoveRatio(permanentY + movementY);
        }
    }

    public void updateStarButton() {
        if (getCard().isKnown()) {
            starButton.changeImage("yellow_star.png");
        } else {
            starButton.changeImage("white_star.png");
        }
    }
}
