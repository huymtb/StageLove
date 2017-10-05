package jp.stage.stagelovemaker.dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.Button;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 8/24/2017.
 */

public class DeleteAccountDialog extends BaseFragment implements FormInputText.FormInputTextDelegate {
    public static final String TAG = "DeleteAccountDialog";
    TextView tvTitle;
    FormInputText tvUsername;
    FormInputText tvPassword;
    TextView tvCancel;
    TextView tvOK;

    ProgressDialog progressDialog;
    String username;
    String password;
    Callback delegate;

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.deny_txt: {
                    getActivity().onBackPressed();
                    break;
                }
                case R.id.allow_txt: {
                    actionDelete();
                    break;
                }
            }
        }
    };

    public static DeleteAccountDialog newInstance() {
        Bundle args = new Bundle();
        DeleteAccountDialog fragment = new DeleteAccountDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_account, container, false);
        tvTitle = (TextView) view.findViewById(R.id.title_txt);
        tvUsername = (FormInputText) view.findViewById(R.id.username_inputtext);
        tvPassword = (FormInputText) view.findViewById(R.id.password_inputtext);
        tvCancel = (TextView) view.findViewById(R.id.deny_txt);
        tvOK = (TextView) view.findViewById(R.id.allow_txt);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTitle.setText(Utils.capitalize(getString(R.string.delete_account).toUpperCase()));
        tvCancel.setOnClickListener(mySingleListener);
        tvOK.setOnClickListener(mySingleListener);
        tvUsername.renderDara(getString(R.string.enter_username), false);
        tvPassword.renderDara(getString(R.string.enter_password), true);
        tvUsername.setTitleInputText(getString(R.string.username));
        tvPassword.setTitleInputText(getString(R.string.password));
        tvUsername.setDelegate(this, Constants.TAG_CONTROL_INPUT_USERNAME);
        tvPassword.setDelegate(this, Constants.TAG_CONTROL_INPUT_PASSWORD);
    }

    @Override
    public void valuechange(String tag, String text) {
        switch (tag) {
            case Constants.TAG_CONTROL_INPUT_USERNAME:
                username = text;
                break;
            case Constants.TAG_CONTROL_INPUT_PASSWORD:
                password = text;
                break;
        }
    }

    @Override
    public void didReturn(String tag) {
        actionDelete();
    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }

    void actionDelete() {
        if (validate() && delegate != null) {
            delegate.onAccountDeleted(username, password);
            getActivity().onBackPressed();
        }
    }

    boolean validate() {
        Boolean isValidate = true;
        tvUsername.setIssuseText("");
        tvPassword.setIssuseText("");
        if (TextUtils.isEmpty(username)) {
            tvUsername.setIssuseText(getContext().getString(R.string.username_blank));
            isValidate = false;
        }
        if (TextUtils.isEmpty(password)) {
            tvPassword.setIssuseText(getContext().getString(R.string.password_blank));
            isValidate = false;
        } else if (password.length() < 6) {
            tvPassword.setIssuseText(getContext().getString(R.string.password_invalid));
            isValidate = false;
        }
        return isValidate;
    }

    public void setCallback(Callback delegate) {
        this.delegate = delegate;
    }

    public interface Callback {
        void onAccountDeleted(String username, String password);
    }
}
