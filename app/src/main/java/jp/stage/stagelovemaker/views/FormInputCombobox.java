package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;

/**
 * Created by congn on 8/7/2017.
 */

public class FormInputCombobox extends RelativeLayout {
    View rootView;
    TextView titleTextView;
    ImageView arrowImageView;
    TextView valueTextView;
    TextView issueTextView;

    public FormInputCombobox(Context context) {
        super(context);
        init(context);
    }

    public FormInputCombobox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_form_input_combobox, this);
        titleTextView = (TextView) rootView.findViewById(R.id.title_txt);
        arrowImageView = (ImageView) rootView.findViewById(R.id.arrow_img);
        arrowImageView.setImageResource(R.mipmap.ic_drop_down);
        valueTextView = (TextView) rootView.findViewById(R.id.value_txt);
        issueTextView = (TextView) rootView.findViewById(R.id.issue_txt);
    }


    public void setFontTitleRegular() {
        titleTextView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setValue(String value) {
        valueTextView.setText(value);
    }

    public void setIssuseText(String text) {
        if (TextUtils.isEmpty(text)) {
            issueTextView.setVisibility(GONE);
        } else {
            issueTextView.setText(text);
            issueTextView.setVisibility(VISIBLE);
        }
    }
}
