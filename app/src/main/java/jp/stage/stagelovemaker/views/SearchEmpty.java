package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import jp.stage.stagelovemaker.R;

/**
 * Created by congn on 8/4/2017.
 */

public class SearchEmpty extends RelativeLayout {
    View rootView;

    public SearchEmpty(Context context) {
        super(context);
        init(context);
    }

    public SearchEmpty(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        rootView = inflate(context, R.layout.view_search_empty, this);
    }
}
