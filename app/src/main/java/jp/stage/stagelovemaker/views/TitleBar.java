package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 7/12/2017.
 */

public class TitleBar extends RelativeLayout implements View.OnClickListener {
    View rootView;
    TextView titleBarTextView;
    TextView doneTextView;
    ImageView iconLeftImageView;
    ImageView iconRightImageView;
    ImageView iconMiddleImageView;
    TitleBarCallback callback;
    ImageView backImageView;
    View backgroundTitlebar;
    RelativeLayout titleLayout;
    RelativeLayout backgroundTitleLayout;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_title_bar, this);
        titleBarTextView = (TextView) rootView.findViewById(R.id.title_bar_txt);
        doneTextView = (TextView) rootView.findViewById(R.id.done_txt);
        iconLeftImageView = (ImageView) rootView.findViewById(R.id.icon_left_img);
        iconRightImageView = (ImageView) rootView.findViewById(R.id.icon_right_img);
        iconMiddleImageView = (ImageView) rootView.findViewById(R.id.icon_middle_img);
        backImageView = (ImageView) rootView.findViewById(R.id.back_img);
        titleLayout = (RelativeLayout) rootView.findViewById(R.id.title_layout);
        backgroundTitlebar = rootView.findViewById(R.id.background_bar);
        backgroundTitleLayout = (RelativeLayout) rootView.findViewById(R.id.background_title_layout);
        backImageView.setVisibility(GONE);

        Typeface semiBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
        titleBarTextView.setTypeface(semiBold);
    }

    public void disableClickTitle() {
        titleBarTextView.setEnabled(false);
        titleBarTextView.setOnClickListener(null);
    }

    public void setCallback(TitleBarCallback callback) {
        this.callback = callback;
    }

    public void disableClickDone() {
        doneTextView.setEnabled(false);
        doneTextView.setOnClickListener(null);
        iconRightImageView.setEnabled(false);
        iconRightImageView.setOnClickListener(null);
    }

    public void setDoneTextColor(int color) {
        doneTextView.setTextColor(color);
    }

    public String getTitle() {
        return titleBarTextView.getText().toString();
    }

    public void setTitle(String title) {
        titleBarTextView.setText(Utils.capitalize(title));
        titleLayout.setOnClickListener(this);
    }

    public void setColorTitle(int color) {
        titleBarTextView.setTextColor(color);
    }

    public void setIconLeft(int icon) {
        iconLeftImageView.setImageResource(icon);
        titleLayout.setOnClickListener(this);
    }

    public void setIconRight(int icon) {
        iconRightImageView.setImageResource(icon);
        iconRightImageView.setOnClickListener(this);
    }

    public void removeIconRight() {
        iconRightImageView.setVisibility(GONE);
        iconRightImageView.setOnClickListener(null);
    }

    public void setIconMiddle(int icon) {
        iconMiddleImageView.setImageResource(icon);
        iconMiddleImageView.setOnClickListener(this);
    }

    public void changeSizeIconMiddle(int width, int height) {
        RelativeLayout.LayoutParams params = (LayoutParams) iconMiddleImageView.getLayoutParams();
        params.width = width;
        params.height = height;
        iconMiddleImageView.requestLayout();
    }

    public void setColorBackground(int colorBackground) {
        backgroundTitlebar.setVisibility(VISIBLE);
        backgroundTitlebar.setBackgroundColor(colorBackground);
    }

    public void enableBackButton() {
        backImageView.setVisibility(VISIBLE);
        backImageView.setOnClickListener(this);
    }

    public void setBackIcon(int icon) {
        backImageView.setImageResource(icon);
    }

    public void setTitleRight(String titleRight) {
        doneTextView.setText(titleRight);
        doneTextView.setOnClickListener(this);
    }

    public void setBackgroundColor(int color) {
        backgroundTitleLayout.setBackgroundColor(color);
    }

    public void setTextColor(int color) {
        titleBarTextView.setTextColor(color);
        doneTextView.setTextColor(color);
    }

    public void setIconBack(int icon) {
        backImageView.setImageResource(icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_right_img:
                if (callback != null) {
                    callback.onRightButtonClicked();
                }
                break;
            case R.id.back_img:
                if (callback != null) {
                    callback.onBackButtonClicked();
                }
                break;
            case R.id.icon_middle_img:
                if (callback != null) {
                    callback.onMiddleButtonClicked();
                }
                break;
            case R.id.done_txt:
                if (callback != null) {
                    callback.onRightButtonClicked();
                }
                break;
            case R.id.title_layout:
                if (callback != null) {
                    callback.onTitleBarClicked();
                }
                break;
        }
    }

    public interface TitleBarCallback {
        void onTitleBarClicked();

        void onRightButtonClicked();

        void onBackButtonClicked();

        void onMiddleButtonClicked();
    }
}
