
package jp.dd0125.touchgem;

import jp.dd0125.touchgem.listener.TouchGemListener;

public class FingerData {
    private TouchGemListener listener;

    private float x;
    private float y;
    private float speed;

    private float firstX;
    private float firstY;
    private long firstTouchTime;
    private TouchGemConfig config;

    private double degree;
    private double distance;

    public enum Way4Type {
        NONE, LEFT, UP, RIGHT, DOWN
    }

    public enum Way8Type {
        NONE, LEFT, UP, RIGHT, DOWN, LEFTUP, RIGHTUP, LEFTDOWN, RIGHTDOWN
    }

    public void init(float firstX, float firstY, TouchGemConfig config) {
        // Log.d("TouchGem", "FingerData init");

        this.firstX = firstX;
        this.firstY = firstY;
        this.x = firstX;
        this.y = firstY;
        this.config = config;

        firstTouchTime = System.currentTimeMillis();

        // 部分的な Listener が存在する場合、設定する
        TouchGemListenerPartial touchGemListenerPartial = config.getTouchGemListenerPartial(
                getFirstX(),
                getFirstY());
        if (touchGemListenerPartial != null) {
            // Log.d("TouchGem",
            // "FingerData init listener = touchGemListenerPartial.listener");
            listener = touchGemListenerPartial.listener;
        }
    }

    public boolean isMoving(float x, float y) {
        float differenceX = firstX - x;
        if (differenceX < 0) {
            differenceX *= -1;
        }
        float differenceY = firstY - y;
        if (differenceY < 0) {
            differenceY *= -1;
        }
        float difference = differenceX + differenceY;
        // Log.d("TouchGem", "FingerData isMoving difference = " + difference);
        if (difference > config.postponement) {
            return true;
        }
        return false;
    }

    public void update(float newX, float newY) {
        float differenceX = x - newX;
        if (differenceX < 0) {
            differenceX *= -1;
        }
        float differenceY = y - newY;
        if (differenceY < 0) {
            differenceY *= -1;
        }
        // speed の計算
        speed = differenceX + differenceY;
        // from, current の角度を計算
        degree = Common.calcDegree(firstX, firstY, newX, newY);

        // from, current の距離を計算
        distance = Common.calcDistance(firstX, firstY, newX, newY);

        x = newX;
        y = newY;
    }

    public Way8Type getWat8Type() {
        // double degree = calcDegree(firstX, firstY, x, y);
        if (degree > 157.5d) {
            return Way8Type.UP;
        } else if (degree > 112.5d) {
            return Way8Type.RIGHTUP;
        } else if (degree > 67.5d) {
            return Way8Type.RIGHT;
        } else if (degree > 22.5d) {
            return Way8Type.RIGHTDOWN;
        }

        if (degree < -157.5d) {
            return Way8Type.UP;
        } else if (degree < -112.5d) {
            return Way8Type.LEFTUP;
        } else if (degree < -67.5d) {
            return Way8Type.LEFT;
        } else if (degree < -22.5d) {
            return Way8Type.LEFTDOWN;
        }

        if (degree < 22.5d && degree > -22.5d) {
            return Way8Type.DOWN;
        }
        return Way8Type.NONE;
    }

    public Way4Type getWat4Type() {
        // double degree = calcDegree(firstX, firstY, x, y);
        if (degree > 157.5d) {
            return Way4Type.UP;
        } else if (degree > 112.5d) {
            return Way4Type.NONE;
        } else if (degree > 67.5d) {
            return Way4Type.RIGHT;
        } else if (degree > 22.5d) {
            return Way4Type.NONE;
        }

        if (degree < -157.5d) {
            return Way4Type.UP;
        } else if (degree < -112.5d) {
            return Way4Type.NONE;
        } else if (degree < -67.5d) {
            return Way4Type.LEFT;
        } else if (degree < -22.5d) {
            return Way4Type.NONE;
        }

        if (degree < 22.5d && degree > -22.5d) {
            return Way4Type.DOWN;
        }
        return Way4Type.NONE;
    }

    public double getDistance() {
        return distance;
    }

    public double getDegree() {
        return degree;
    }

    public float getSpeed() {
        return speed;
    }

    public float getX() {
        if (config.isScalable) {
            return x * config.scaleWidthCoefficient;
        } else {
            return x;
        }
    }

    public float getY() {
        if (config.isScalable) {
            return y * config.scaleHeightCoefficient;
        } else {
            return y;
        }
    }

    public float getFirstX() {
        if (config.isScalable) {
            return firstX * config.scaleWidthCoefficient;
        } else {
            return firstX;
        }
    }

    public float getFirstY() {
        if (config.isScalable) {
            return firstY * config.scaleHeightCoefficient;
        } else {
            return firstY;
        }
    }

    public long getFirstTouchTime() {
        return firstTouchTime;
    }

    public TouchGemListener getListener() {
        if (listener == null) {
            listener = config.standardListener;
        }
        return listener;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("finger");
        sb.append(";x,y = ").append(getX()).append(", ").append(getY());
        sb.append(";firstX,Y = ").append(getFirstX()).append(", ").append(getFirstY());
        sb.append(";speed = ").append(getSpeed());
        return sb.toString();
    }
}
