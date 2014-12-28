
package jp.dd0125.touchgem;

import jp.dd0125.touchgem.listener.TouchGemListener;
import android.util.Log;

public class TouchGemListenerPartial {

    protected float top;
    protected float left;
    protected float bottom;
    protected float right;

    protected TouchGemListener listener;

    public TouchGemListenerPartial(float top, float bottom, float left, float right,
            TouchGemListener listener) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.listener = listener;
    }

    public boolean isHit(float x, float y) {
        Log.d("TouchGem", "TouchGemListenerPartial isHit : x,y = " + x + ", " + y);
        Log.d("TouchGem", "TouchGemListenerPartial isHit : t,b = " + top + ", " + bottom);
        Log.d("TouchGem", "TouchGemListenerPartial isHit : l,r = " + left + ", " + right);
        if (top > y) {
            return false;
        }
        if (bottom < y) {
            return false;
        }
        if (left > x) {
            return false;
        }
        if (right < x) {
            return false;
        }
        return true;
    }

}
