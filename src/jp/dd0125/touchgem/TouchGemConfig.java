
package jp.dd0125.touchgem;

import java.util.ArrayList;

import jp.dd0125.touchgem.listener.DoubleTouchGemListener;
import jp.dd0125.touchgem.listener.TouchGemListener;

public class TouchGemConfig {
    protected boolean isScalable = false;
    protected float screenWidth;
    protected float screenHeight;
    private float scaleWidth;
    private float scaleHeight;
    protected float scaleWidthCoefficient;
    protected float scaleHeightCoefficient;

    public float swipeSpeed = 1f;

    public int moveToSwipeCheckTime = 250;
    public int longTappingTime = 500;
    public int swippingLimitTime = 500;
    public int doubleTapCheckTime = 250;

    //
    public float postponement = 20f;
    public double postponementRotation = 1d;
    public double postponementPinchInOut = 1d;
    // Listener
    protected TouchGemListener standardListener;

    public void setListener(TouchGemListener listener) {
        standardListener = listener;
    }

    private final ArrayList<TouchGemListenerPartial> listenerPartialList = new ArrayList<TouchGemListenerPartial>();

    protected DoubleTouchGemListener doubleTouchGemListener;

    public void setDoubleTouchListener(DoubleTouchGemListener doubleTouchGemListener) {
        this.doubleTouchGemListener = doubleTouchGemListener;
    }

    public void addListenerPartial(float top, float bottom, float left, float right,
            TouchGemListener listener) {
        TouchGemListenerPartial listenerPartial = new TouchGemListenerPartial(top, bottom, left,
                right, listener);
        listenerPartialList.add(listenerPartial);
    }

    public TouchGemListenerPartial getTouchGemListenerPartial(float x, float y) {
        for (TouchGemListenerPartial lp : listenerPartialList) {
            boolean isHit = lp.isHit(x, y);
            if (isHit) {
                return lp;
            }
        }
        return null;
    }

    public void setupScale(float scaleWidth, float scaleHeight, float screenWidth,
            float screenHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        scaleWidthCoefficient = scaleWidth / screenWidth;
        scaleHeightCoefficient = scaleHeight / screenHeight;
        isScalable = true;
    }

}
