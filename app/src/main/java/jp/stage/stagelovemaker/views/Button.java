package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 7/14/2017.
 */

public class Button extends RelativeLayout implements View.OnClickListener {
    public static final int ACTION_PLUS = 1, ACTION_BOOST = 2, ACTION_SUPER_LIKES = 3;
    View rootView;
    TextView tvTitle;
    ImageView ivLogo;
    TextView tvDescription;
    RelativeLayout viewLayout;
    TextView tvTitle2;
    Callback mCallback;
    int action;

    public Button(Context context) {
        super(context);
        init(context);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_button, this);
        viewLayout = (RelativeLayout) rootView.findViewById(R.id.view_layout);
        ivLogo = (ImageView) rootView.findViewById(R.id.iv_logo);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle2 = (TextView) rootView.findViewById(R.id.tv_title2);
        tvDescription = (TextView) rootView.findViewById(R.id.tv_description);

        tvTitle.setTypeface(Utils.getProximaBold(context));
        tvDescription.setTypeface(Utils.getProximaSemiBold(context));
        tvTitle2.setTypeface(Utils.getProximaBold(context));
        viewLayout.setOnClickListener(this);
        tvTitle2.setVisibility(GONE);
    }

    public void setContent(int resId, String title, int textColor) {
        ivLogo.setImageResource(resId);
        tvTitle.setText(title);
        tvTitle.setTextColor(textColor);
        viewLayout.setVisibility(VISIBLE);

        ivLogo.setVisibility(VISIBLE);
        tvTitle.setVisibility(VISIBLE);
        tvDescription.setVisibility(VISIBLE);
        tvTitle2.setVisibility(GONE);
    }

    public void setText(String title, int textColor) {
        ivLogo.setVisibility(GONE);
        tvTitle.setVisibility(GONE);
        tvDescription.setVisibility(GONE);
        tvTitle2.setVisibility(VISIBLE);
        tvTitle2.setText(title);
        tvTitle2.setTextColor(textColor);
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            mCallback.onButtonClicked(action);
        }
    }

    public void setCallback(Callback callback, int action) {
        this.mCallback = callback;
        this.action = action;
    }

    public interface Callback {
        void onButtonClicked(int action);
    }
}
