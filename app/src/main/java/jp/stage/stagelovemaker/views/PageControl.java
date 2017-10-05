package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 8/4/2017.
 */

public class PageControl extends RelativeLayout {
    View rootView;
    LinearLayout linearLayout;
    android.widget.Button buttons[] = null;
    int numberPage = 0;
    int currentPageColor = Color.parseColor("#FFFFFF");
    int hintPageColor = Color.TRANSPARENT;

    public PageControl(Context context) {
        super(context);
        init(context);
    }

    public PageControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_page_control, this);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.pagecontrol_layout);
    }

    public void setNumberOfPages(int number) {
        int sizeOfButton = (int) getResources().getDimension(R.dimen.page_control_height);
        int margin = (int) getResources().getDimension(R.dimen.page_control_margin);
        numberPage = number;
        buttons = new android.widget.Button[number];
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        for (int i = 0; i < number; i++) {
            buttons[i] = new android.widget.Button(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeOfButton, sizeOfButton);
            if (i != numberPage - 1) {
                params.setMargins(0, 0, margin, 0);
            }
            buttons[i].setLayoutParams(params);
            buttons[i].setEnabled(false);
            buttons[i].setBackgroundResource(R.drawable.circle_button);
            linearLayout.addView(buttons[i]);
        }
    }

    public void setCurrentPageColor(int color) {
        currentPageColor = color;
    }

    public void setHintPageColor(int color) {
        hintPageColor = color;
    }

    public void setCurrentPage(int currentPage) {
        for (int i = 0; i < numberPage; i++) {
            ((GradientDrawable) buttons[i].getBackground()).setStroke((int) Utils.dip2px(getContext(), 0.8f), Color.WHITE);
            if (currentPage == i) {
                ((GradientDrawable) buttons[i].getBackground()).setColor(currentPageColor);
            } else {
                ((GradientDrawable) buttons[i].getBackground()).setColor(hintPageColor);
            }
        }
    }

    public void setCurrentPageProfile(int currentPage) {
        for (int i = 0; i < numberPage; i++) {
            ((GradientDrawable) buttons[i].getBackground()).setStroke((int) Utils.dip2px(getContext(), 1f), hintPageColor);
            if (currentPage == i) {
                ((GradientDrawable) buttons[i].getBackground()).setColor(Color.WHITE);
            } else {
                ((GradientDrawable) buttons[i].getBackground()).setColor(hintPageColor);
            }
        }
    }
}
