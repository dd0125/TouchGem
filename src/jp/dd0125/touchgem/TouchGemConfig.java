
package jp.dd0125.touchgem;

public class TouchGemConfig {
    protected boolean isScalable = false;
    protected float screenWidth;
    protected float screenHeight;
    private float scaleWidth;
    private float scaleHeight;
    protected float scaleWidthCoefficient;
    protected float scaleHeightCoefficient;

    public float postponement = 20f;

    public float swipeSpeed = 1f;

    public int moveToSwipeCheckTime = 250;
    public int longTappingTime = 500;
    public int swippingLimitTime = 500;
    public int doubleTapCheckTime = 250;

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
