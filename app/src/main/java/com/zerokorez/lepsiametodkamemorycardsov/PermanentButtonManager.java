package com.zerokorez.lepsiametodkamemorycardsov;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import com.zerokorez.general.Manageable;

import java.util.ArrayList;

public class PermanentButtonManager implements Manageable {
    private ArrayList<PermanentButton> buttons;
    private Boolean state;

    private Paint selectedPaint;
    private Paint deselectedPaint;
    private Paint bordersPaint;
    private Paint backgroundPaint;

    private int textColor;
    private int odd;

    public PermanentButtonManager(Paint selectedPaint, Paint deselectedPaint, Paint bordersPaint, Paint backgroundPaint, int odd) {
        buttons = new ArrayList<>();
        state = true;

        this.selectedPaint = selectedPaint;
        this.deselectedPaint = deselectedPaint;
        this.bordersPaint = bordersPaint;
        this.backgroundPaint = backgroundPaint;

        textColor = Constants.WHITE;
        this.odd = odd;
    }

    public Paint getSelectedPaint() {
        return selectedPaint;
    }

    public Paint getDeselectedPaint() {
        return deselectedPaint;
    }

    public Paint getBordersPaint() {
        return bordersPaint;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public int getTextColor() {
        return textColor;
    }

    public ArrayList<PermanentButton> getButtons() {
        return buttons;
    }

    @Override
    public void setState(boolean state) {
        this.state = state;
    }
    @Override
    public boolean getState() {
        return state;
    }

    public int getOdd() {
        return odd;
    }


    public void setState(Boolean newState) {
        state = newState;
    }

    public void setSameFont() {
        ArrayList<Float> sizes = new ArrayList<>();
        for (PermanentButton button: buttons) {
            sizes.add(button.getPaint().getTextSize());
        }
        float min = Constants.getMinFloat(sizes);
        for (PermanentButton button: buttons) {
            button.getPaint().setTextSize(min);
        }
    }
    public void setDefaultSelected(int index) {
        for (PermanentButton button : buttons) {
            button.setIsSelected(false);
        }
        if (buttons.size() > index) {
            buttons.get(index).setIsSelected(true);
        }
    }

    public void add(PermanentButton button) {
        buttons.add(button);
    }

    public void draw(Canvas canvas) {
        for (PermanentButton button: buttons) {
            button.draw(canvas);
        }
    }

    public PermanentButton receiveTouch(MotionEvent event) {
        for (PermanentButton button: buttons) {
            if (button.receiveTouch(event)) {
                for (PermanentButton object: buttons) {
                    if (object.getIsSelected()) {
                        object.setIsSelected(false);
                    }
                }
                button.setIsSelected(true);
                return button;
            }
        }
        return null;
    }
}
