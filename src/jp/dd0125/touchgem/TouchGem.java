
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

        Log.d("TouchGem", "TouchGem : screen = " + config.screenHeight + ", " + config.screenWidth);
    }

    public enum TouchAction {
        DOWN, UP, MOVE
    }

    public enum SingleStatus {
        None, FirstTouch, FirstMove, Dragging, Swipping, FirstTouchUp, FirstDoubleTouch
    }

    private SingleStatus status = SingleStatus.None;
    private final Handler handler = new Handler();
    private final FingerDataMap fingerMap = new FingerDataMap();

    public void update(int fingerId, TouchAction touchAction, float x, float y) {
        // Log.d("TouchGem", "update : " + status.toString() + ", x,y = " + x +
        // ", " + y);
        FingerData fingerData = fingerMap.get(fingerId);
        switch (touchAction) {
            case DOWN:
            case MOVE:

                if (fingerData == null) {
                    fingerData = new FingerData(fingerId);
                    fingerData.init(x, y, config);

                    fingerMap.put(fingerId, fingerData);
                } else {
                    fingerData.update(x, y);
                }
            case UP:
                break;
            default:
                break;

        }

        int fingerMapSize = fingerMap.size();
        if (fingerMapSize == 1) {
            switch (status) {
                case None:
                    updateForNone(fingerData, touchAction, x, y);

                    break;
                case FirstTouch:
                    updateForFirstTouch(fingerData, touchAction, x, y);
                    break;
                case FirstMove:
                    updateForFirstMove(fingerData, touchAction, x, y);
                    break;
                case Dragging:
                    updateForDragging(fingerData, touchAction, x, y);
                    break;
                case FirstDoubleTouch:
                    updateForFirstDoubleTouch(fingerData, touchAction, x, y);
                    break;
                case FirstTouchUp:
                    updateForFirstTouchUp(fingerData, touchAction, x, y);
                    break;
                case Swipping:
                    updateForSwipping(fingerData, touchAction, x, y);
                    break;
                default:
                    break;

            }

        }
    }

    private void updateStatus(SingleStatus newStatus) {
        if (status == newStatus) {
            return;
        }
        switch (status) {
            case None:

                break;
            case Dragging:
                break;
            case FirstDoubleTouch:
                break;
            case FirstMove:
                break;
            case FirstTouch:

                break;
            case FirstTouchUp:
                break;
            case Swipping:
                break;
            default:
                break;
        }
        switch (newStatus) {
            case None:
                fingerMap.clear();
                break;
            case Dragging:
                break;
            case FirstDoubleTouch:
                break;
            case FirstMove:
                break;
            case FirstTouch:
                break;
            case FirstTouchUp:
                break;
            case Swipping:
                break;
            default:
                break;
        }
        this.status = newStatus;
    }

    private void updateForNone(final FingerData fingerData, TouchAction touchAction, float x,
            float y) {
        switch (touchAction) {
            case DOWN:

                onLongTapRunnable = new Runnable() {

                    @Override
                    public void run() {
                        fingerData.getListener().onLongTap(fingerData);
                        updateStatus(SingleStatus.None);
                    }
                };
                handler.postDelayed(onLongTapRunnable, config.longTappingTime);

                updateStatus(SingleStatus.FirstTouch);

                break;
            case MOVE:
                // 起こりえない
                break;
            case UP:
                // 起こりえない
                break;
        }
    }

    private void updateForFirstTouch(final FingerData fingerData, TouchAction touchAction,
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
                updateStatus(SingleStatus.FirstMove);

                break;
            case UP:
                handler.removeCallbacks(onLongTapRunnable);
                updateStatus(SingleStatus.FirstTouchUp);

                // onTap handler で登録
                // onTap が行われた場合、 status を None に戻す
                onTapRunnable = new Runnable() {

                    @Override
                    public void run() {
                        fingerData.getListener().onTap(fingerData);
                        updateStatus(SingleStatus.None);

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

    private void updateForFirstTouchUp(FingerData fingerData, TouchAction touchAction, float x,
            float y) {
        switch (touchAction) {
            case DOWN:
                // OnTap の handler を除去
                // Double Tap に備える
                handler.removeCallbacks(onTapRunnable);
                updateStatus(SingleStatus.FirstDoubleTouch);

                break;
            case MOVE:
                break;
            case UP:
                break;
            default:
                break;
        }
    }

    private void updateForFirstDoubleTouch(FingerData fingerData, TouchAction touchAction, float x,
            float y) {

        switch (touchAction) {
            case MOVE:
                boolean isMoving = fingerData.isMoving(x, y);
                if (!isMoving) {
                    return;
                }

                updateStatus(SingleStatus.FirstMove);

            case UP:
                // onDoubleTap
                fingerData.getListener().onDoubleTap(fingerData);
                updateStatus(SingleStatus.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    private void updateForFirstMove(final FingerData fingerData, TouchAction touchAction, float x,
            float y) {
        switch (touchAction) {
            case MOVE:
                fingerData.getListener().onMoving(fingerData);
                // ある程度時間が経過した場合、 Dragging に移行する
                long currentTimeMillis = System.currentTimeMillis();
                if (fingerData.getFirstTouchTime() + config.moveToSwipeCheckTime < currentTimeMillis) {
                    updateStatus(SingleStatus.Dragging);
                    return;
                }

                float speed = fingerData.getSpeed();
                Log.d("TouchGem", "TouchGem updateForFirstMove speed = " + speed);

                // Speed が swipeSpeed に達していない場合、Dragging に移行する
                if (config.swipeSpeed > speed) {
                    updateStatus(SingleStatus.Dragging);
                    return;
                }

                // ある程度距離が稼げた場合、swipping に移行する
                onDraggingRunnable = new Runnable() {

                    @Override
                    public void run() {
                        updateStatus(SingleStatus.Dragging);
                        fingerData.getListener().onDragging(fingerData);

                    }
                };
                handler.postDelayed(onDraggingRunnable, config.swippingLimitTime);
                updateStatus(SingleStatus.Swipping);
                break;
            case UP:
                // OnDragged
                fingerData.getListener().onDragged(fingerData);
                updateStatus(SingleStatus.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    private void updateForDragging(FingerData fingerData, TouchAction touchAction, float x, float y) {
        switch (touchAction) {
            case MOVE:
                fingerData.getListener().onMoving(fingerData);
                fingerData.getListener().onDragging(fingerData);
                break;
            case UP:
                // OnDragged
                fingerData.getListener().onDragged(fingerData);
                updateStatus(SingleStatus.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    private void updateForSwipping(FingerData fingerData, TouchAction touchAction, float x, float y) {
        switch (touchAction) {
            case MOVE:
                fingerData.getListener().onMoving(fingerData);
                break;
            case UP:
                handler.removeCallbacks(onDraggingRunnable);

                // OnSwipped
                fingerData.getListener().onSwipped(fingerData);
                updateStatus(SingleStatus.None);
                break;
            case DOWN:
                break;
            default:
                break;
        }
    }

    public SingleStatus getStatus() {
        return status;
    }

}
