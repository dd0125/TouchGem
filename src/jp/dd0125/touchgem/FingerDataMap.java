
package jp.dd0125.touchgem;

import java.util.ArrayList;

import android.util.Log;

public class FingerDataMap {

    private final ArrayList<FingerData> fingerList = new ArrayList<FingerData>();

    private FingerData firstFingerData;
    private FingerData secondFingerData;
    private DoubleTouchInfo doubleTouchInfo;
    private final TouchGemConfig config;

    public FingerDataMap(TouchGemConfig config) {
        this.config = config;
    }

    // public void clear() {
    // firstFingerData = null;
    // secondFingerData = null;
    // doubleTouchInfo = null;
    // fingerMap.clear();
    // // Log.d("TouchGem", "FingerDataMap.clear : fingerMap.size = " +
    // // fingerMap.size());
    //
    // }

    public int size() {
        return fingerList.size();
    }

    public void put(int actionIndex, FingerData fingerData) {
        if (fingerList.size() != 0) {
            FingerData fb = get(actionIndex);
            if (fb != null) {
                return;
            }
        }
        // 追加
        fingerList.add(fingerData);

        // 追加後の size によって、情報を更新する
        switch (fingerList.size()) {
            case 1:
                firstFingerData = fingerData;
                break;
            case 2:
                secondFingerData = fingerData;
                // DoubleTouch状態になった場合、距離と角度を計算して保持しておく
                doubleTouchInfo = new DoubleTouchInfo(firstFingerData, secondFingerData, config);

                break;
        }
        // Log.d("TouchGem", "put : fingerMap.size() = " + fingerMap.size());
    }

    public FingerData get(int fingerId) {
        if (fingerList.size() <= fingerId) {
            return null;
        }
        return fingerList.get(fingerId);
    }

    public void remove(int fingerId) {
        Log.d("TouchGem", "remove : fingerId = " + fingerId);
        fingerList.remove(fingerId);
    }

    public DoubleTouchInfo getDoubleTouchInfo() {
        return doubleTouchInfo;
    }

    // public FingerData getOtherFingerDataForDoubleTouch(int currentFingerId) {
    // final int fingerMapSize = fingerMap.size();
    // if (fingerMapSize != 2) {
    // return null;
    // }
    // for (int i = 0; i < fingerMapSize; i++) {
    // int fingerId = fingerMap.keyAt(i);
    // if (currentFingerId == fingerId) {
    // continue;
    // }
    //
    // // get the object by the key.
    // FingerData fd = fingerMap.get(fingerId);
    // return fd;
    //
    // }
    // return null;
    // }

}
