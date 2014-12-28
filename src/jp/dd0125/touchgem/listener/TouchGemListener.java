
package jp.dd0125.touchgem.listener;

import jp.dd0125.touchgem.FingerData;

public interface TouchGemListener {
    public void onFirstTouch(FingerData fingerData);

    public void onTap(FingerData fingerData);

    public void onDoubleTap(FingerData fingerData);

    public void onLongTap(FingerData fingerData);

    public void onSwipped(FingerData fingerData);

    public void onDragging(FingerData fingerData);

    public void onMoving(FingerData fingerData);

    public void onDragged(FingerData fingerData);

}
