package jp.stage.stagelovemaker.views.utility;

import android.view.View;

/**
 * Created by congn on 8/4/2017.
 */

public interface SwipeCallback {
    void cardSwipedTop(View card);
    void cardSwipedLeft(View card);
    void cardSwipedRight(View card);
    void cardOffScreen(View card);
    void cardActionDown();
    void cardActionUp();

    /**
     * Check whether we can start dragging current view.
     * @return true if we can start dragging view, false otherwise
     */
    boolean isDragEnabled();
}
