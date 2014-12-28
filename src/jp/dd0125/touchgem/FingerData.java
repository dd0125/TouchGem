
package jp.dd0125.touchgem;

import android.util.Log;

public class FingerData {
    private final int fingerId;

    private float x;
    private float y;
    private float speed;

    private float firstX;
    private float firstY;
    private long firstTouchTime;
    private float postponement;

    private double degree;
    private double distance;

    public enum Way4Type {
        NONE, LEFT, UP, RIGHT, DOWN
    }

    public enum Way8Type {
        NONE, LEFT, UP, RIGHT, DOWN, LEFTUP, RIGHTUP, LEFTDOWN, RIGHTDOWN
    }

    public FingerData(int fingerId) {
        this.fingerId = fingerId;
    }

    public void init(float firstX, float firstY, float postponement) {
        this.firstX = firstX;
        this.firstY = firstY;
        this.postponement = postponement;

        firstTouchTime = System.currentTimeMillis();
        Log.d("TouchGem", "FingerData init");
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
        Log.d("TouchGem", "FingerData isMoving difference = " + difference);
        if (difference > postponement) {
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
        degree = calcDegree(firstX, firstY, newX, newY);

        // from, current の距離を計算
        distance = calcDistance(firstX, firstY, newX, newY);

        x = newX;
        y = newY;
    }

    private double calcDistance(double x, double y, double x2, double y2) {
        double distance = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
        return distance;
    }

    private double calcRadian(double x, double y, double x2, double y2) {
        double radian = Math.atan2(x2 - x, y2 - y);
        return radian;
    }

    private double calcDegree(double x, double y, double x2, double y2) {
        double radian = calcRadian(x, y, x2, y2);
        double degree = radian * 180d / Math.PI;
        return degree;
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
        return x;
    }

    public float getY() {
        return y;
    }

    public float getFirstX() {
        return firstX;
    }

    public float getFirstY() {
        return firstY;
    }

    public long getFirstTouchTime() {
        return firstTouchTime;
    }
}
