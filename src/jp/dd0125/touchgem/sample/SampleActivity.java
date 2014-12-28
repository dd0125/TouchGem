
package jp.dd0125.touchgem.sample;

import jp.dd0125.touchgem.FingerData;
import jp.dd0125.touchgem.R;
import jp.dd0125.touchgem.TouchGem;
import jp.dd0125.touchgem.TouchGem.TouchAction;
import jp.dd0125.touchgem.TouchGemConfig;
import jp.dd0125.touchgem.listener.TouchGemListener;
import android.app.Activity;
import android.os.Bundle;
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

        touchGem = new TouchGem(config, new TouchGemListener() {

            @Override
            public void onTap(FingerData fingerData) {
                Log.d("TouchGem", "MainActivity TouchGem Event onTap : " + fingerData.toString());

            }

            @Override
            public void onFirstTouch(FingerData fingerData) {
                Log.d("TouchGem", "MainActivity TouchGem Event onFirstTouch");

            }

            @Override
            public void onDragging(FingerData fingerData) {
                Log.d("TouchGem",
                        "MainActivity TouchGem Event onDragging : " + fingerData.toString());

            }

            @Override
            public void onDragged(FingerData fingerData) {
                Log.d("TouchGem",
                        "MainActivity TouchGem Event onDragged : " + fingerData.toString());

            }

            @Override
            public void onDoubleTap(FingerData fingerData) {
                Log.d("TouchGem", "MainActivity TouchGem Event onDoubleTap");

            }

            @Override
            public void onSwipped(FingerData fingerData) {
                Log.d("TouchGem",
                        "MainActivity TouchGem Event onSwipped : " + fingerData.getDegree()
                                + ", way8Type = " + fingerData.getWat8Type());

            }

            @Override
            public void onMoving(FingerData fingerData) {
                Log.d("TouchGem", "MainActivity TouchGem Event onMoving");

            }

            @Override
            public void onLongTap(FingerData fingerData) {
                Log.d("TouchGem", "MainActivity TouchGem Event onLongTap");

            }
        });

        View view = findViewById(android.R.id.content);
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int fingerId = event.getPointerId(pointerIndex);

                float x = event.getX();
                float y = event.getY();
                TouchAction touchAction = null;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        touchAction = TouchAction.DOWN;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchAction = TouchAction.MOVE;

                        break;
                    case MotionEvent.ACTION_UP:
                        touchAction = TouchAction.UP;

                        break;
                }

                touchGem.update(fingerId, touchAction, x, y);

                return true;
            }
        });
    }
}
