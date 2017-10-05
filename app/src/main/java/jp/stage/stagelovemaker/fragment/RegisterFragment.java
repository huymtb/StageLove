package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.model.ErrorModel;
import jp.stage.stagelovemaker.model.SignUpModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.LoginActionBar;
import jp.stage.stagelovemaker.views.LoginActionBar.LoginActionBarDelegate;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 7/28/2017.
 */

public class RegisterFragment extends BaseFragment implements LoginActionBarDelegate,
        FormInputText.FormInputTextDelegate {
    public static final String TAG = "RegisterFragment";
    private LoginActionBar actionBar;
    private boolean bFlagButtonNext = false;

    FormInputText tvUsername;
    FormInputText tvEmail;
    FormInputText tvPassword;
    FormInputText tvConfirmPassword;
    TextView tvDescription;

    SignUpModel signUpModel;
    String username = "";
    String email = "";
    String password = "";
    String confirmPass = "";
    NetworkManager networkManager;
    Gson gson;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        signUpModel = new SignUpModel();
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        actionBar = (LoginActionBar) view.findViewById(R.id.action_bar);

        tvUsername = (FormInputText) view.findViewById(R.id.tv_username);
        tvEmail = (FormInputText) view.findViewById(R.id.tv_email);
        tvPassword = (FormInputText) view.findViewById(R.id.tv_password);
        tvConfirmPassword = (FormInputText) view.findViewById(R.id.tv_confirm_password);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar.setDelegate(getTag(), this);
        actionBar.setTitle(getString(R.string.register));
        actionBar.setTextNextColor(ContextCompat.getColor(getContext(), R.color.color_dim_text));

        tvUsername.renderDara(getString(R.string.username), false);
        tvEmail.renderDara(getString(R.string.email), false);
        tvPassword.renderDara(getString(R.string.password), true);
        tvConfirmPassword.renderDara(getString(R.string.confirm_password), true);
        tvUsername.setDelegate(this, Constants.TAG_CONTROL_INPUT_USERNAME);
        tvEmail.setDelegate(this, Constants.TAG_CONTROL_INPUT_EMAIL);
        tvPassword.setDelegate(this, Constants.TAG_CONTROL_INPUT_PASSWORD);
        tvConfirmPassword.setDelegate(this, Constants.TAG_CONTROL_INPUT_CONFIRM_PASS);

        tvDescription.setOnClickListener(mySingleListener);
        String tos = getString(R.string.term_of_service);
        String policy = getString(R.string.privacy_policy);
        String des = String.format(getString(R.string.policy_authentication), tos, policy);
        SpannableString spannableString = new SpannableString(des);
        ClickableSpan clickableTos = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        ClickableSpan clickablePolicy = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        int start = des.indexOf(tos);
        spannableString.setSpan(clickableTos, start, start + tos.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.darkgrey)), start, start + tos.length(), 0);

        start = des.indexOf(policy);

        spannableString.setSpan(clickablePolicy, start, start + policy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.darkgrey)), start, start + policy.length(), 0);

        tvDescription.setText(spannableString);

        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void didBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void didNext() {
        nextAction();
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
            case Constants.TAG_CONTROL_INPUT_CONFIRM_PASS:
                confirmPass = text;
                break;
            case Constants.TAG_CONTROL_INPUT_EMAIL:
                email = text;
                break;
        }
        validate();
    }

    @Override
    public void didReturn(String tag) {
        nextAction();
    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }

    private boolean validate() {
        boolean isValid = true;
        if (bFlagButtonNext) {
            tvUsername.setIssuseText("");
            tvPassword.setIssuseText("");
            tvConfirmPassword.setIssuseText("");
        }
        if (TextUtils.isEmpty(username)) {
            if (bFlagButtonNext) {
                tvUsername.setIssuseText(getString(R.string.username_blank));
            }
            isValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            if (bFlagButtonNext) {
                tvEmail.setIssuseText(getString(R.string.email_blank));
            }
            isValid = false;
        } else {
            if (!Utils.isValidEmail(email)) {
                if (bFlagButtonNext) {
                    tvEmail.setIssuseText(getString(R.string.email_invalid));
                }
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(password)) {
            if (bFlagButtonNext) {
                tvPassword.setIssuseText(getString(R.string.password_blank));
            }
            isValid = false;
        } else if (password.length() < 6) {
            if (bFlagButtonNext) {
                tvPassword.setIssuseText(getString(R.string.password_invalid));
            }
            isValid = false;
        }
        if (TextUtils.isEmpty(confirmPass)) {
            if (bFlagButtonNext) {
                tvConfirmPassword.setIssuseText(getString(R.string.field_blank));
            }
            isValid = false;
        } else if (!confirmPass.equals(password)) {
            if (bFlagButtonNext) {
                tvConfirmPassword.setIssuseText(getString(R.string.password_not_match));
            }
            isValid = false;
        }

        if (isValid) {
            actionBar.setTextNextColor(Color.WHITE);
        } else {
            actionBar.setTextNextColor(ContextCompat.getColor(getContext(), R.color.color_dim_text));
        }

        if (bFlagButtonNext) {
            bFlagButtonNext = false;
        }
        return isValid;
    }

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (signUpModel != null) {
            tvEmail.setTitle(signUpModel.getEmail());
            tvPassword.setTitle(signUpModel.getPassword());
            tvUsername.setTitle(signUpModel.getUsername());
            tvConfirmPassword.setTitle(signUpModel.getPassword());
        }
    }

    private void nextAction() {
        Utils.hideSoftKeyboard(getActivity());
        bFlagButtonNext = true;
        if (validate()) {
            if (signUpModel != null) {
                signUpModel.setUsername(username);
                signUpModel.setEmail(email);
                signUpModel.setPassword(password);
                networkManager.requestApi(networkManager
                                .validateUsernameAndEmail(signUpModel.getUsername(), signUpModel.getEmail()),
                        Constants.ID_VALIDATE_EMAIL);

            }
        }
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_VALIDATE_EMAIL:
                    RegisterProfileFragment registerProfileFragment = RegisterProfileFragment.newInstance();
                    replace(registerProfileFragment, RegisterProfileFragment.TAG, true, true);
                    break;
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {
            switch (idRequest) {
                case Constants.ID_VALIDATE_EMAIL:
                    ErrorModel errorModel = gson.fromJson(response, ErrorModel.class);
                    if (errorModel != null) {
                        if (!TextUtils.isEmpty(errorModel.getErrorMsg())) {
                            Toast.makeText(getActivity(), errorModel.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
            }
        }
    };

    public SignUpModel getSignUp() {
        return signUpModel;
    }
}
