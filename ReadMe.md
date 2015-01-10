TouchGem
==================
Android用タッチ検出ライブラリです。

Examples
---------------------------------

SingleTouch設定
```java:SingleTouch
// class Instance
private TouchGem touchGem;

// onCreate時
TouchGemConfig config = new TouchGemConfig();
config.swipeSpeed = 0.1f;
config.doubleTapCheckTime = 250;
config.swippingLimitTime = 500;
config.longTappingTime = 500;
config.moveToSwipeCheckTime = 250;
config.postponement = 20f;


// Displayのサイズ設定
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
touchGem = new TouchGem(config);
```

【必須】使えるようにする設定
```java:使えるようにする設定
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
            int actionIndex = MotionEventCompat.getActionIndex(event);
            float x = event.getX(actionIndex);
            float y = event.getY(actionIndex);
            touchGem.update(actionIndex, touchAction, x, y);

        }

        return true;
    }
});
```

【オプション】2点タッチの検出
```java:DoubleTouch
config.postponementRotation = 1d;
config.postponementPinchInOut = 1d;
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
```

【オプション】部分タッチの検出
```java:PartialTouch
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
```


Feature
---------------------------------
* 角度自動計算
* 解像度対応
* 画面分割タッチ検出対応
** 左半分と右半分でCallbackを別にする
* 各種シングルタッチ検出
** タップ
** ロングタップ
** ダブルタップ
** ドラッグ
** スワイプ
*** 4方向、8方向検出
* 各種マルチタッチ検出
** ピンチイン・ピンチアウト
** ローテーション

License
---------------------------------
MIT license (© 2015 dd0125)
