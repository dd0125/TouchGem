
package jp.dd0125.touchgem;

import android.util.SparseArray;

public class FingerDataMap {

    private final SparseArray<FingerData> fingerMap = new SparseArray<FingerData>();

    private int firstFingerId;

    public void clear() {
        fingerMap.clear();

    }

    public int size() {
        return fingerMap.size();
    }

    public void put(int fingerId, FingerData fingerData) {
        if (fingerMap.size() == 0) {
            firstFingerId = fingerId;
        }
        fingerMap.put(fingerId, fingerData);
    }

    public FingerData get(int fingerId) {
        return fingerMap.get(fingerId);
    }

    public void remove(int fingerId) {
        fingerMap.remove(fingerId);
    }

}
