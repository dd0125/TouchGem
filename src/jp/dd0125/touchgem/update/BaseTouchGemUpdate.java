
package jp.dd0125.touchgem.update;

import jp.dd0125.touchgem.TouchGem.TouchAction;

public abstract class BaseTouchGemUpdate {

    public abstract void update(int fingerId, TouchAction touchAction, float x, float y);
}
