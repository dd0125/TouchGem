
package jp.dd0125.touchgem;

import android.os.Handler;
import android.util.Log;

public class TouchGem {
    private final TouchGemConfig config;

    private Runnable onTapRunnable;
    private Runnable onDraggingRunnable;
    private Runnable onLongTapRunnable;

    public TouchGem(TouchGemConfig config) {
        this.config = config;
        fingerMap = new FingerDataMap(config);
        Log.d("TouchGem", "TouchGem : screen = " + config.screenHeight + ", " + config.screenWidth);
    }

    public enum TouchAction {
        DOWN, UP, MOVE
    }

    public enum TouchCountStatus {
        None, Single, Double
    }

    private TouchCountStatus touchCountStatus = TouchCountStatus.None;

    public enum Status {
        None, SingleTouch, SingleMove, Dragging, Swipping, SingleTouchUp, SingleDoubleTouch
        , DoubleTouch
    }

    private Status status = Status.None;
    private final Handler handler = new Handler();
    private final FingerDataMap fingerMap;

    public void update(int actionIndex, TouchAction touchAction, float x, float y) {
        // Log.d("TouchGem", "update : fingerId = " + fingerId + ", " +
        // status.toString() + ", x,y = "
        // + x + ", " + y + ", touchAction = " + touchAction);

        // Action により、FinderData を update する
        FingerData fingerData = fingerMap.get(actionIndex);
        switch (touchAction) {
            case DOWN:
            case MOVE:
                if (fingerData == null) {
                    fingerData = new FingerData();
                    fingerData.init(x, y, config);

                    fingerMap.put(actionIndex, fingerData);
                } else {
                    fingerData.update(x, y);
                }
                break;
            case UP:
                break;
            default:
                break;

        }

        // 指の数が変化した場合、ステータスを上書きする
        if (config.doubleTouchGemListener != null) {
            int fingerMapSize = fingerMap.size();
            // Log.d("TouchGem", "fingerMapSize = " + fingerMapSize);
            if (fingerMapSize == 1) {
                if (touchCountStatus != TouchCountStatus.Single) {
                    touchCountStatus = TouchCountStatus.Single;
                    updateStatus(Status.SingleTouch);
                }
            } else if (fingerMapSize == 2) {
                if (touchCountStatus != TouchCountStatus.Double) {

                    touchCountStatus = TouchCountStatus.Double;
                    updateStatus(Status.DoubleTouch);
                }
            }
        }

        switch (status) {
            case None:
                updateForNone(actionIndex, fingerData, touchAction, x, y);
                break;
            case SingleTouch:
                updateForSingleTouch(actionIndex, fingerData, touchAction, x, y);
                break;
            case SingleMove:
                updateForSingleMove(actionIndex, fingerData, touchAction, x, y);
                break;
            case Dragging:
                updateForDragging(actionIndex, fingerData, touchAction, x, y);
                break;
            case SingleDoubleTouch:
                updateForSingleDoubleTouch(actionIndex, fingerData, touchAction, x, y);
                break;
            case SingleTouchUp:
                updateForSingleTouchUp(actionIndex, fingerData, touchAction, x, y);
                break;
            case Swipping:
                updateForSwipping(actionIndex, fingerData, touchAction, x, y);
                break;
            case DoubleTouch:
                updateForDoubleTouch(actionIndex, fingerData, touchAction, x, y);
                break;
            default:
                break;

        }

    }

    /**
     * ステータス変更
     * 
     * @param newStatus
     */
    private void updateStatus(Status newStatus) {
        if (status == newStatus) {
            return;
        }
        Log.d("TouchGem", "TouchGem.updateStatus : " + status + " -> " + newStatus);
        switch (status) {
            case None:
                break;
            case Dragging:
                break;
            case SingleDoubleTouch:
                break;
            case SingleMove:
                break;
            case SingleTouch:
                break;
            case SingleTouchUp:
                break;
            case Swipping:
                break;
            default:
                break;
        }
        switch (newStatus) {
            case None:
                touchCountStatus = TouchCountStatus.None;
                break;
            case Dragging:
                break;
            case SingleDoubleTouch:
                break;
            case SingleMove:
                break;
            case SingleTouch:
                break;
            case SingleTouchUp:
                break;
            case Swipping:
                break;
            case DoubleTouch:
                handler.removeCallbacks(onTapRunnable);
                handler.removeCallbacks(onLongTapRunnable);
                handler.removeCallbacks(onDraggingRunnable);

                break;
            default:
                break;
        }
        this.status = newStatus;
    }

    private void updateForNone(final int actionIndex, final FingerData fingerData,
            TouchAction touchAction, float x,
            float y) {
        switch (touchAction) {
            case DOWN:

                onLongTapRunnable = new Runnable() {

                    @Override
                    public void run() {
                        fingerData.getListener().onLongTap(fingerData);
                        fingerMap.remove(actionIndex);
                        updateStatus(Status.None);
                    }
                };
                // handler.postDelayed(onLongTapRunnable,
                // config.longTappingTime);

                updateStatus(Status.SingleTouch);

                break;
            case MOVE:
                // 起こりえない
                break;
            case UP:
                // 起こりえない
                break;
        }
    }

    private void updateForSingleTouch(final int actionIndex, final FingerData fingerData,
            TouchAction touchAction,
            final float x,
            final float y) {
        switch (touchAction) {
            case MOVE:
                boolean isMoving = fingerData.isMoving(x, y);
                if (!isMoving) {
                    // 遊び内の場合、処理しない
                    return;
                }
                handler.removeCallbacks(onLongTapRunnable);
                updateStatus(Status.SingleMove);

                break;
            case UP:
                handler.removeCallbacks(onLongTapRunnable);
                updateStatus(Status.SingleTouchUp);

                // onTap handler で登録
                // onTap が行われた場合、 status を None に戻す
                onTapRunnable = new Runnable() {

                    @Override
                    public void run() {
                        if (fingerData == null) {
                            Log.d("TouchGem", "fingerData is null");
                        }
                        fingerData.getListener().onTap(fingerData);
                        fingerMap.remove(actionIndex);
                        updateStatus(Status.None);

                    }
                };
                handler.postDelayed(onTapRunnable, config.doubleTapCheckTime);
                break;
            case DOWN:
                break;
            default:
                break;

        }

    }

    private void updateForSingleTouchUp(int actionIndex, FingerData fingerData,
            TouchAction touchAction, float x,
            float y) {
        switch (touchAction) {
            case DOWN:
                // OnTap の handler を除去
                // Double Tap に備える
                handler.removeCallbacks(onTapRunnable);
                updateStatus(Status.SingleDoubleTouch);

                break;
            case MOVE:
                break;
            case UP:
                break;
            default:
                break;
        }
    }

    private void updateForSingleDoubleTouch(int actionIndex, FingerData fingerData,
            TouchAction touchAction, float x,
            float y) {

        switch (touchAction) {
            case MOVE:
                boolean isMoving = fingerData.isMoving(x, y);
                if (!isMoving) {
                    return;
                }

                updateStatus(Status.SingleMove);

            case UP:
                // onDoubleTap
                fingerData.getListener().onDoubleTap(fingerData);
                fingerMap.remove(actionIndex);
                updateStatus(Status.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    private void updateForSingleMove(int actionIndex, final FingerData fingerData,
            TouchAction touchAction, float x,
            float y) {
        switch (touchAction) {
            case MOVE:
                fingerData.getListener().onMoving(fingerData);
                // ある程度時間が経過した場合、 Dragging に移行する
                long currentTimeMillis = System.currentTimeMillis();
                if (fingerData.getFirstTouchTime() + config.moveToSwipeCheckTime < currentTimeMillis) {
                    updateStatus(Status.Dragging);
                    return;
                }

                float speed = fingerData.getSpeed();
                // Log.d("TouchGem", "TouchGem updateForFirstMove speed = " +
                // speed);

                // Speed が swipeSpeed に達していない場合、Dragging に移行する
                if (config.swipeSpeed > speed) {
                    updateStatus(Status.Dragging);
                    return;
                }

                // ある程度距離が稼げた場合、swipping に移行する
                onDraggingRunnable = new Runnable() {

                    @Override
                    public void run() {
                        updateStatus(Status.Dragging);
                        fingerData.getListener().onDragging(fingerData);

                    }
                };
                handler.postDelayed(onDraggingRunnable, config.swippingLimitTime);
                updateStatus(Status.Swipping);
                break;
            case UP:
                // OnDragged
                fingerData.getListener().onDragged(fingerData);
                fingerMap.remove(actionIndex);
                updateStatus(Status.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    private void updateForDragging(int actionIndex, FingerData fingerData, TouchAction touchAction,
            float x, float y) {
        switch (touchAction) {
            case MOVE:
                fingerData.getListener().onMoving(fingerData);
                fingerData.getListener().onDragging(fingerData);
                break;
            case UP:
                // OnDragged
                fingerData.getListener().onDragged(fingerData);
                fingerMap.remove(actionIndex);
                updateStatus(Status.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    private void updateForSwipping(int actionIndex, FingerData fingerData, TouchAction touchAction,
            float x, float y) {
        switch (touchAction) {
            case MOVE:
                fingerData.getListener().onMoving(fingerData);
                break;
            case UP:
                handler.removeCallbacks(onDraggingRunnable);

                // OnSwipped
                fingerData.getListener().onSwipped(fingerData);
                fingerMap.remove(actionIndex);
                updateStatus(Status.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    private void updateForDoubleTouch(int actionIndex, FingerData updateFingerData,
            TouchAction touchAction,
            float x,
            float y) {
        switch (touchAction) {
            case DOWN:

                break;
            case MOVE:
                if (config.doubleTouchGemListener != null) {
                    boolean isMoving = updateFingerData.isMoving(x, y);
                    if (isMoving) {
                        DoubleTouchInfo doubleTouchInfo = fingerMap.getDoubleTouchInfo();
                        // 変化量を取得
                        doubleTouchInfo.update();
                        double fluctuationDegree = doubleTouchInfo.getFluctuationDegree();

                        if (fluctuationDegree != 0d) {
                            config.doubleTouchGemListener.onRotation(fluctuationDegree);
                        }

                        // PinchInOut と Rotaion を同時に動かさないために工夫してもいいかも
                        double fluctuationDistance = doubleTouchInfo.getFluctuationDistance();
                        if (fluctuationDistance != 0d) {
                            config.doubleTouchGemListener.onPinchInOut(fluctuationDistance);
                        }
                    }

                }
                break;
            case UP:
                fingerMap.remove(actionIndex);
                updateStatus(Status.SingleTouch);
                break;
            default:
                break;

        }

    }

    public Status getStatus() {
        return status;
    }

}
