package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

import jp.stage.stagelovemaker.R;

/**
 * Created by congn on 7/28/2017.
 */

public class LoginActionBar extends RelativeLayout {
    View rootView;
    ImageView backBtn;
    android.widget.Button nextBtn;
    TextView titleTextView;
    LoginActionBarDelegate delegate;
    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View view) {
            switch (view.getId()) {
                case R.id.back_btn: {
                    if (delegate != null) {
                        delegate.didBack();
                    }
                    break;
                }
                case R.id.next_btn: {
                    if (delegate != null) {
                        delegate.didNext();
                    }
                    break;
                }
            }
        }
    };

    public LoginActionBar(Context context) {
        super(context);
        init(context);
    }

    public LoginActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_login_actionbar, this);
        backBtn = (ImageView) findViewById(R.id.back_btn);
        nextBtn = (android.widget.Button) findViewById(R.id.next_btn);
        titleTextView = (TextView) findViewById(R.id.title_txt);
        titleTextView.setText(context.getString(R.string.register));
        backBtn.setOnClickListener(mySingleListener);
        nextBtn.setOnClickListener(mySingleListener);
        nextBtn.setText(context.getString(R.string.next).toUpperCase());
    }

    public void setTitle(String title) {
        this.titleTextView.setText(title);
    }

    public void setTitleNextBtn(String title) {
        this.nextBtn.setText(title);
    }

    public void setTextNextColor(int textColor) {
        nextBtn.setTextColor(textColor);
    }

    public void invisibleNextButton() {
        nextBtn.setVisibility(GONE);
    }

    public void setDelegate(String tag, LoginActionBarDelegate fragment) {
        delegate = fragment;
    }

    public interface LoginActionBarDelegate {
        void didBack();

        void didNext();
    }
}
