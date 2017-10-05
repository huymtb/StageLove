package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.LoginActionBar;

/**
 * Created by congn on 8/4/2017.
 */

public class ForgotPasswordFragment extends BaseFragment implements FormInputText.FormInputTextDelegate,
        LoginActionBar.LoginActionBarDelegate, View.OnClickListener {
    public static final String TAG = "ForgotPasswordFragment";
    LoginActionBar actionBar;
    FormInputText emailInputText;
    TextView forgotTextView;
    TextView desTextView;
    Button sendMailButton;
    String email;
    Boolean bFlagButtonReset = false;
    NetworkManager networkManager;

    public static ForgotPasswordFragment newInstance() {
        Bundle args = new Bundle();
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_RESET_PASSWORD:
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getString(R.string.reset_password_to_email),
                                        Toast.LENGTH_LONG).show();
                                //getActivity().onBackPressed();
                                ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance();
                                replace(changePasswordFragment, ChangePasswordFragment.TAG, false, true);
                            }
                        });
                    }
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        actionBar = (LoginActionBar) view.findViewById(R.id.action_bar);
        emailInputText = (FormInputText) view.findViewById(R.id.email_inputtext);
        forgotTextView = (TextView) view.findViewById(R.id.forgot_txt);
        desTextView = (TextView) view.findViewById(R.id.des_forgot_password);
        sendMailButton = (Button) view.findViewById(R.id.sendmail_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar.invisibleNextButton();
        emailInputText.renderDara(getString(R.string.email), false);
        emailInputText.setDelegate(this, Constants.TAG_CONTROL_INPUT_EMAIL);
        forgotTextView.setText(Utils.fromHtml(getString(R.string.forgot_password)).toString());
        desTextView.setText(Utils.fromHtml(getString(R.string.des_forgot_password)));
        ((GradientDrawable) sendMailButton.getBackground()).setColor(Color.GRAY);
        ((GradientDrawable) sendMailButton.getBackground()).setStroke(0, Color.TRANSPARENT);
        sendMailButton.setTextColor(ContextCompat.getColor(getContext(), R.color.light_grayish_blue));
        sendMailButton.setOnClickListener(this);
        actionBar.setTitle(getString(R.string.reset_password));
        actionBar.setDelegate(getTag(), this);
    }

    Boolean validate() {
        Boolean isValidate;
        isValidate = true;
        if (bFlagButtonReset) {
            emailInputText.setIssuseText("");
        }
        if (TextUtils.isEmpty(email)) {
            if (bFlagButtonReset) {
                emailInputText.setIssuseText(getString(R.string.email_blank));
            }
            isValidate = false;
        } else {
            if (!Utils.isValidEmail(email)) {
                if (bFlagButtonReset) {
                    emailInputText.setIssuseText(getString(R.string.email_invalid));
                }
                isValidate = false;
            } else {
                emailInputText.setIssuseText("");
            }
        }

        if (isValidate) {
            sendMailButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            sendMailButton.setTextColor(Color.WHITE);
        } else {
            sendMailButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
            sendMailButton.setTextColor(ContextCompat.getColor(getContext(), R.color.light_grayish_blue));
        }

        if (bFlagButtonReset) {
            bFlagButtonReset = false;
        }
        return isValidate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendmail_btn: {
                Utils.hideSoftKeyboard(getActivity());
                bFlagButtonReset = true;
                if (validate()) {
                    resetPassword();
                }
            }
        }
    }

    private void resetPassword() {
        networkManager.requestApiNoProgress(networkManager.forgotPassword(email), Constants.ID_RESET_PASSWORD);
    }

    @Override
    public void didBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void didNext() {

    }

    @Override
    public void valuechange(String tag, String text) {
        if (tag.equals(Constants.TAG_CONTROL_INPUT_EMAIL)) {
            email = text;
        }
        validate();
    }

    @Override
    public void didReturn(String tag) {

    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }
}
