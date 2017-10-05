package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import jp.stage.stagelovemaker.R;

/**
 * Created by congn on 7/28/2017.
 */

public class BackgroundGradientTopBottom extends RelativeLayout {
    public BackgroundGradientTopBottom(Context context) {
        super(context);
        init(context);
    }

    public BackgroundGradientTopBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        View rootView = inflate(context, R.layout.view_background_gradient, this);
        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scrollView_background);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return false;
            }
        });

    }
}
