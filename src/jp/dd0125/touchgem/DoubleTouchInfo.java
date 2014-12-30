
package jp.dd0125.touchgem;

import android.util.Log;

public class DoubleTouchInfo {

    private double currentDistance;
    private double currentDegree;
    private double fluctuationDistance = 0d;
    private double fluctuationDegree = 0d;
    private final FingerData firstFingerData;
    private final FingerData secondFingerData;
    private final TouchGemConfig config;

    public DoubleTouchInfo(FingerData firstFingerData, FingerData secondFingerData,
            TouchGemConfig config) {
        this.firstFingerData = firstFingerData;
        this.secondFingerData = secondFingerData;
        this.config = config;

        currentDistance = Common.calcDistance(firstFingerData.getX(),
                firstFingerData.getY(), secondFingerData.getX(), secondFingerData.getY());
        currentDegree = Common.calcDegree(firstFingerData.getX(),
                firstFingerData.getY(), secondFingerData.getX(), secondFingerData.getY());
        // Log.d("TouchGem", "DoubleTouchInfo.init currentDegree = " +
        // currentDegree + ", first:"
        // + firstFingerData + ", second:" + secondFingerData);
    }

    @Override
    public String toString() {
        return "DoubleTouchInfo currentDistance = " +
                currentDistance
                + ", currentDegree = " + currentDegree + ", first = " +
                firstFingerData
                + ", second = " + secondFingerData;
    }

    public void update() {
        double prevDistance = currentDistance;
        currentDistance = Common.calcDistance(firstFingerData.getX(),
                firstFingerData.getY(), secondFingerData.getX(), secondFingerData.getY());
        fluctuationDistance += (currentDistance - prevDistance);

        double prevDegree = currentDegree;
        currentDegree = Common.calcDegree(firstFingerData.getX(),
                firstFingerData.getY(), secondFingerData.getX(), secondFingerData.getY());

        fluctuationDegree += (currentDegree - prevDegree);
        if (fluctuationDegree > 180d) {
            fluctuationDegree -= 180d;
        }
        if (fluctuationDegree < -180d) {
            fluctuationDegree += 180d;
        }
        Log.d("TouchGem", "DoubleTouchInfo.update currentDegree = " + currentDegree + ", first:"
                + firstFingerData + ", second:" + secondFingerData);
        // Log.d("TouchGem", "DoubleTouchInfo.update prevDistance = " +
        // prevDistance
        // + ", currentDistance = " + currentDistance +
        // ", fluctuationDistance = "
        // + fluctuationDistance);
        // Log.d("TouchGem", "DoubleTouchInfo.update prevDegree = " + prevDegree
        // + ", currentDegree = " + currentDegree + ", fluctuationDegree = "
        // + fluctuationDegree);

    }

    public double getFluctuationDegree() {
        boolean isRotation = false;
        if (config.postponementRotation == 0d) {
            isRotation = true;
        } else if (-config.postponementRotation > fluctuationDegree) {
            isRotation = true;
        } else if (fluctuationDegree > config.postponementRotation) {
            isRotation = true;
        }
        if (isRotation) {
            double returnValue = fluctuationDegree;
            fluctuationDegree = 0d;
            return returnValue;
        } else {
            return 0d;
        }

    }

    public double getFluctuationDistance() {
        boolean isPinchInOut = false;
        if (config.postponementPinchInOut == 0d) {
            isPinchInOut = true;
        } else if (-config.postponementPinchInOut > fluctuationDistance) {
            isPinchInOut = true;
        } else if (fluctuationDistance > config.postponementPinchInOut) {
            isPinchInOut = true;
        }
        if (isPinchInOut) {
            double returnValue = fluctuationDistance;
            fluctuationDistance = 0d;
            return returnValue;
        } else {
            return 0d;
        }
    }

}
