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
import android.widget.Toast;

import com.google.gson.Gson;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.model.ErrorModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.LoginActionBar;

/**
 * Created by congn on 8/15/2017.
 */

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener,
        LoginActionBar.LoginActionBarDelegate {
    public static final String TAG = "ChangePasswordFragment";

    LoginActionBar actionBar;
    FormInputText tvEmail;
    FormInputText tvPassword;
    FormInputText tvCode;
    Button btAgree;

    String email;
    String password;
    String numberCode;
    Boolean bFlagButtonReset = false;
    NetworkManager networkManager;
    Gson gson;

    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
        gson = new Gson();
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_CHANGE_PASSWORD:
                    Toast.makeText(getActivity(), getString(R.string.password_has_changed), Toast.LENGTH_LONG).show();
                    LoginFragment loginFragment = LoginFragment.newInstance();
                    replace(loginFragment, LoginFragment.TAG, false, true);
                    break;
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {
            switch (idRequest) {
                case Constants.ID_CHANGE_PASSWORD:
                    ErrorModel errorModel = gson.fromJson(response, ErrorModel.class);
                    if (errorModel != null && !TextUtils.isEmpty(errorModel.getErrorMsg())) {
                        Toast.makeText(getActivity(), errorModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public FormInputText.FormInputTextDelegate textChanged = new FormInputText.FormInputTextDelegate() {
        @Override
        public void valuechange(String tag, String text) {
            switch (tag) {
                case Constants.TAG_CONTROL_INPUT_CODE_NUMBER:
                    numberCode = text;
                    break;
                case Constants.TAG_CONTROL_INPUT_PASSWORD:
                    password = text;
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
    };

    private void changePassword() {
        networkManager.requestApi(networkManager.updatePassword(email, numberCode, password), Constants.ID_CHANGE_PASSWORD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        actionBar = (LoginActionBar) view.findViewById(R.id.action_bar);
        tvEmail = (FormInputText) view.findViewById(R.id.tv_email);
        tvCode = (FormInputText) view.findViewById(R.id.tv_code_number);
        tvPassword = (FormInputText) view.findViewById(R.id.tv_password);
        btAgree = (Button) view.findViewById(R.id.bt_change_password);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar.invisibleNextButton();
        tvEmail.renderDara(getString(R.string.email), false);
        tvEmail.setDelegate(textChanged, Constants.TAG_CONTROL_INPUT_EMAIL);
        tvPassword.renderDara(getString(R.string.password), true);
        tvPassword.setDelegate(textChanged, Constants.TAG_CONTROL_INPUT_PASSWORD);
        tvCode.renderDara(getString(R.string.code_number), true);
        tvCode.setDelegate(textChanged, Constants.TAG_CONTROL_INPUT_CODE_NUMBER);

        ((GradientDrawable) btAgree.getBackground()).setColor(Color.GRAY);
        ((GradientDrawable) btAgree.getBackground()).setStroke(0, Color.TRANSPARENT);
        btAgree.setTextColor(ContextCompat.getColor(getContext(), R.color.light_grayish_blue));
        btAgree.setOnClickListener(this);
        actionBar.setTitle(getString(R.string.reset_password));
        actionBar.setDelegate(getTag(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_change_password:
                nextAction();
                break;
        }
    }

    private void nextAction() {
        Utils.hideSoftKeyboard(getActivity());
        bFlagButtonReset = true;
        if (validate()) {
            changePassword();
        }
    }

    @Override
    public void didBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void didNext() {

    }

    private boolean validate() {
        boolean isValid = true;
        if (bFlagButtonReset) {
            tvEmail.setIssuseText("");
            tvPassword.setIssuseText("");
            tvCode.setIssuseText("");
        }
        if (TextUtils.isEmpty(email)) {
            if (bFlagButtonReset) {
                tvEmail.setIssuseText(getString(R.string.email_blank));
            }
            isValid = false;
        } else {
            if (!Utils.isValidEmail(email)) {
                if (bFlagButtonReset) {
                    tvEmail.setIssuseText(getString(R.string.email_invalid));
                }
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(password)) {
            if (bFlagButtonReset) {
                tvPassword.setIssuseText(getString(R.string.password_blank));
            }
            isValid = false;
        } else if (password.length() < 8) {
            if (bFlagButtonReset) {
                tvPassword.setIssuseText(getString(R.string.password_invalid));
            }
            isValid = false;
        }
        if (TextUtils.isEmpty(numberCode)) {
            if (bFlagButtonReset) {
                tvCode.setIssuseText(getString(R.string.field_blank));
            }
            isValid = false;
        }

        if (isValid) {
            btAgree.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            btAgree.setTextColor(Color.WHITE);
        } else {
            btAgree.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
            btAgree.setTextColor(ContextCompat.getColor(getContext(), R.color.light_grayish_blue));
        }

        if (bFlagButtonReset) {
            bFlagButtonReset = false;
        }
        return isValid;
    }
}
