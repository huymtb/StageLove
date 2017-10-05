package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;

/**
 * Created by congn on 8/21/2017.
 */

public class TitleTextView extends RelativeLayout {

    View rootView;
    TextView titleTextView;
    TextView contentTextView;
    TextView issueTextView;

    public TitleTextView(Context context) {
        super(context);
        init(context);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        rootView = inflate(context, R.layout.view_title_textview, this);
        titleTextView = (TextView) findViewById(R.id.title_txt);
        contentTextView = (TextView) findViewById(R.id.language_txt);
        issueTextView = (TextView) findViewById(R.id.issuse_txt);
    }

    public void setFontTitleRegular() {
        titleTextView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setLanguage(String language) {
        if (!TextUtils.isEmpty(language) && !TextUtils.isEmpty(contentTextView.getText().toString())) {
            language = contentTextView.getText() + ", " + language;
        }
        contentTextView.setText(language);
        contentTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
    }

    public void setIssuseText(String issuseText) {
        if (TextUtils.isEmpty(issuseText)) {
            issueTextView.setVisibility(GONE);
        } else {
            issueTextView.setText(issuseText);
            issueTextView.setVisibility(VISIBLE);
        }
    }

    public String getContent() {
        return contentTextView.getText().toString();
    }

    public void setContent(String nationality) {
        contentTextView.setText(nationality);
        contentTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
    }

}
