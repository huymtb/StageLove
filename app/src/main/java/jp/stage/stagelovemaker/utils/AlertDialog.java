package jp.stage.stagelovemaker.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 8/4/2017.
 */

public class AlertDialog extends Dialog {
    TextView titleTextView;
    ImageView iconImageView;
    TextView denyTextView;
    TextView allowTextView;
    AlertDialogDelegate delegate;
    String type;
    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View view) {
            switch (view.getId()) {
                case R.id.deny_txt: {
                    dismiss();
                    if (delegate != null) {
                        delegate.clickAllow(false, type);
                    }
                    break;
                }
                case R.id.allow_txt: {
                    dismiss();
                    if (delegate != null) {
                        delegate.clickAllow(true, type);
                    }
                }
            }
        }
    };

    public AlertDialog(Context context, CharSequence title, int icon) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alert);
        titleTextView = (TextView) findViewById(R.id.title_txt);
        iconImageView = (ImageView) findViewById(R.id.icon_img);
        denyTextView = (TextView) findViewById(R.id.deny_txt);
        allowTextView = (TextView) findViewById(R.id.allow_txt);
        iconImageView.setVisibility(View.GONE);

        titleTextView.setText(title);
        denyTextView.setOnClickListener(mySingleListener);
        allowTextView.setOnClickListener(mySingleListener);
        if (icon != -1) {
            iconImageView.setVisibility(View.VISIBLE);
            iconImageView.setImageResource(icon);
        } else {
            iconImageView.setVisibility(View.GONE);
        }
    }

    public void disableDenyButton() {
        denyTextView.setVisibility(View.GONE);
        allowTextView.setText(getContext().getString(R.string.ok));
    }

    public void setTextButton(String cancel, String ok) {
        denyTextView.setText(cancel);
        allowTextView.setText(ok);
    }

    public void setDelegate(AlertDialogDelegate fragment, String type) {
        delegate = fragment;
        this.type = type;
    }

    public interface AlertDialogDelegate {
        void clickAllow(Boolean bAllow, String type);
    }
}
