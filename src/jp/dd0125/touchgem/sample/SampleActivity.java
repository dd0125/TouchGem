
package jp.dd0125.touchgem.sample;

import jp.dd0125.touchgem.FingerData;
import jp.dd0125.touchgem.R;
import jp.dd0125.touchgem.TouchGem;
import jp.dd0125.touchgem.TouchGem.TouchAction;
import jp.dd0125.touchgem.TouchGemConfig;
import jp.dd0125.touchgem.listener.DoubleTouchGemListener;
import jp.dd0125.touchgem.listener.TouchGemListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SampleActivity extends Activity {
    private TouchGem touchGem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TouchGemConfig config = new TouchGemConfig();
        config.swipeSpeed = 0.1f;

        // Displayのインスタンス取得
        Display disp = getWindowManager().getDefaultDisplay();
        float scaleHeight = 400f;
        float scaleWidth = 300f;
        config.setupScale(scaleWidth, scaleHeight, disp.getWidth(), disp.getHeight());

        TouchGemListener listener = new TouchGemListener() {

            @Override
            public void onTap(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem Event onTap : " + fingerData.toString());

            }

            @Override
            public void onFirstTouch(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem Event onFirstTouch");

            }

            @Override
            public void onDragging(FingerData fingerData) {
                Log.d("TouchGem",
                        "SampleActivity TouchGem Event onDragging : " + fingerData.toString());

            }

            @Override
            public void onDragged(FingerData fingerData) {
                Log.d("TouchGem",
                        "SampleActivity TouchGem Event onDragged : " + fingerData.toString());

            }

            @Override
            public void onDoubleTap(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem Event onDoubleTap");

            }

            @Override
            public void onSwipped(FingerData fingerData) {
                Log.d("TouchGem",
                        "SampleActivity TouchGem Event onSwipped : " + fingerData.getDegree()
                                + ", way8Type = " + fingerData.getWat8Type() + ", way4Type = "
                                + fingerData.getWat4Type());

            }

            @Override
            public void onMoving(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem Event onMoving");

            }

            @Override
            public void onLongTap(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem Event onLongTap");

            }
        };
        config.setListener(listener);
        config.setDoubleTouchListener(new DoubleTouchGemListener() {

            @Override
            public void onRotation(double fluctuationDegree) {
                Log.d("TouchGem", "SampleActivity TouchGem onRotation fluctuationDegree = "
                        + fluctuationDegree);
            }

            @Override
            public void onPinchInOut(double fluctuationDistance) {
                Log.d("TouchGem", "SampleActivity TouchGem onPinchInOut fluctuationDistance = "
                        + fluctuationDistance);

            }
        });
        // 部分的なListener設定
        TouchGemListener partialListener = new TouchGemListener() {

            @Override
            public void onTap(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem partialListener Event onTap : "
                        + fingerData.toString());

            }

            @Override
            public void onFirstTouch(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem partialListener Event onFirstTouch");

            }

            @Override
            public void onDragging(FingerData fingerData) {
                Log.d("TouchGem",
                        "SampleActivity TouchGem partialListener Event onDragging : "
                                + fingerData.toString());

            }

            @Override
            public void onDragged(FingerData fingerData) {
                Log.d("TouchGem",
                        "SampleActivity TouchGem partialListener Event onDragged : "
                                + fingerData.toString());

            }

            @Override
            public void onDoubleTap(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem partialListener Event onDoubleTap");

            }

            @Override
            public void onSwipped(FingerData fingerData) {
                Log.d("TouchGem",
                        "SampleActivity TouchGem partialListener Event onSwipped : "
                                + fingerData.getDegree()
                                + ", way8Type = " + fingerData.getWat8Type() + ", way4Type = "
                                + fingerData.getWat4Type());

            }

            @Override
            public void onMoving(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem partialListener Event onMoving");

            }

            @Override
            public void onLongTap(FingerData fingerData) {
                Log.d("TouchGem", "SampleActivity TouchGem partialListener Event onLongTap");

            }
        };
        config.addListenerPartial(0, 200f, 0, 200f, partialListener);
        touchGem = new TouchGem(config);

        View view = findViewById(android.R.id.content);
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction() & MotionEvent.ACTION_MASK;
                TouchAction touchAction = null;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchAction = TouchAction.DOWN;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchAction = TouchAction.MOVE;

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        touchAction = TouchAction.UP;

                        break;
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    // fingerId は不明であるため、指の数だけ Update を送る
                    int pointerCount = MotionEventCompat.getPointerCount(event);
                    for (int actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                        float x = event.getX(actionIndex);
                        float y = event.getY(actionIndex);
                        touchGem.update(actionIndex, touchAction, x, y);
                    }

                } else {
                    int actionIndex = MotionEventCompat.getActionIndex(event);// event.getPointerId(pointerIndex);
                    float x = event.getX(actionIndex);
                    float y = event.getY(actionIndex);
                    Log.d("TouchGem", "SampleActivity TouchGem onTouch action = " +
                            action + ", actionIndex = " + actionIndex);
                    touchGem.update(actionIndex, touchAction, x, y);

                }

                return true;
            }
        });
    }
}
