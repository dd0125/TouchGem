package jp.dd0125.touchgem;

public class Common {

    /**
     * Degree<br />
     * ・UP(-180 or 180)<br />
     * ・DOWN(0)<br />
     * ・LEFT(-90)<br />
     * ・RIGHT(90)<br />
     * 
     * @return
     */
    public static double calcDegree(double x, double y, double x2, double y2) {
        double radian = Common.calcRadian(x, y, x2, y2);
        double degree = radian * 180d / Math.PI;
        return degree;
    }

    public static double calcRadian(double x, double y, double x2, double y2) {
        double radian = Math.atan2(x2 - x, y2 - y);
        return radian;
    }

    public static double calcDistance(double x, double y, double x2, double y2) {
        double distance = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
        return distance;
    }

}
