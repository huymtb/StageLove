package jp.stage.stagelovemaker.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;

/**
 * Created by congn on 8/4/2017.
 */

public class FormInputText extends RelativeLayout implements View.OnFocusChangeListener {
    View rootView;
    View lineView;
    ExtendedEditText editText;
    FormInputTextDelegate delegate;
    ImageView removeImageView;
    String tag;
    TextView issuseTextView;
    RelativeLayout inputTextLayout;
    TextView titleInputText;
    int colorFocus;
    int colorLine;
    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View view) {
            switch (view.getId()) {
                case R.id.icon_remove_img: {
                    editText.setText("");
                    break;
                }
            }
        }
    };

    public FormInputText(Context context) {
        super(context);
        init(context);
    }

    public FormInputText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_form_input_text, this);
        lineView = rootView.findViewById(R.id.line_maintabbar_view);
        lineView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lineView));
        editText = (ExtendedEditText) rootView.findViewById(R.id.edit_text);
        removeImageView = (ImageView) rootView.findViewById(R.id.icon_remove_img);
        issuseTextView = (TextView) rootView.findViewById(R.id.issuse_txt);
        inputTextLayout = (RelativeLayout) rootView.findViewById(R.id.input_text_layout);
        titleInputText = (TextView) rootView.findViewById(R.id.title_input_text);
        removeImageView.setVisibility(GONE);
        editText.setOnFocusChangeListener(this);
        removeImageView.setOnClickListener(mySingleListener);

        colorFocus = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        colorLine = ContextCompat.getColor(getContext(), R.color.lineView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (delegate != null) {
                    if (TextUtils.isEmpty(charSequence)) {
                        delegate.valuechange(tag, "");
                    } else {
                        delegate.valuechange(tag, charSequence.toString());
                    }
                }

                if (TextUtils.isEmpty(charSequence)) {
                    removeImageView.setVisibility(GONE);
                } else if (removeImageView.getVisibility() != VISIBLE) {
                    removeImageView.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (delegate != null) {
                        delegate.didReturn(tag);
                    }
                }
                return false;
            }
        });
    }

    public void setTitleInputText(String text) {
        titleInputText.setVisibility(VISIBLE);
        titleInputText.setText(text);
        //editText.setTextSize(11);
    }

    public void setIssuseText(String issuseText) {
        if (TextUtils.isEmpty(issuseText)) {
            issuseTextView.setVisibility(GONE);
            lineView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lineView));
        } else {
            issuseTextView.setVisibility(VISIBLE);
            issuseTextView.setText(issuseText);
            lineView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bright_red));
        }
    }

    public void setVisibleIconRemvove(int type) {
        removeImageView.setVisibility(type);
    }

    public void renderDara(String hintText, Boolean bSecure) {
        editText.setHint(hintText);
        if (bSecure == true) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTransformationMethod(null);
        }
    }

    public void setFocus() {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void setDelegate(FormInputTextDelegate fragment, String tag) {
        delegate = fragment;
        this.tag = tag;
    }

    public void setColorTitle(int color) {
        editText.setTextColor(color);
    }

    public void setDelegate(Dialog dialog) {
        delegate = (FormInputTextDelegate) dialog;
    }

    public void setDelegate(Dialog dialog, String tag) {
        delegate = (FormInputTextDelegate) dialog;
        this.tag = tag;
    }

    public Boolean isFocusText() {
        return editText.hasFocus();
    }

    public String getTitle() {
        return editText.getText().toString();
    }

    public void setTitle(String title) {
        editText.setText(title);
    }

    public void setMaxLength(int maxLength) {
        if (maxLength > 1) {
            removeImageView.setVisibility(GONE);
            editText.setMaxLines(100);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        } else {
            editText.setMaxLines(1);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.edit_text) {
            if (b == true) {
                if (editText.getText().length() > 1) {
                    removeImageView.setVisibility(VISIBLE);
                }
                lineView.setBackgroundColor(colorFocus);
                titleInputText.setTextColor(colorFocus);
            } else {
                removeImageView.setVisibility(GONE);
                lineView.setBackgroundColor(colorLine);
                titleInputText.setTextColor(ContextCompat.getColor(getContext(), R.color.medium_dark_gray));
            }
            if (delegate != null) {
                delegate.inputTextFocus(b, tag);
            }
        }
    }

    public interface FormInputTextDelegate {
        void valuechange(String tag, String text);

        void didReturn(String tag);

        void inputTextFocus(Boolean b, String tag);
    }

}
