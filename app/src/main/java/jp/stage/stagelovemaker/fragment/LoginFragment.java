package jp.stage.stagelovemaker.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.MainActivity;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.ErrorModel;
import jp.stage.stagelovemaker.model.UserTokenModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.LoginActionBar;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 7/28/2017.
 */

public class LoginFragment extends BaseFragment implements LoginActionBar.LoginActionBarDelegate,
        FormInputText.FormInputTextDelegate {
    public static final String TAG = "LoginFragment";

    LoginActionBar actionBar;
    FormInputText tvUsername;
    FormInputText tvPassword;
    TextView tvDescription;
    TextView tvForgotPassword;

    String username = "";
    String password = "";

    boolean bFlagButtonNext = false;
    NetworkManager networkManager;
    Gson gson;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        actionBar = (LoginActionBar) view.findViewById(R.id.action_bar);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        tvUsername = (FormInputText) view.findViewById(R.id.tv_username);
        tvPassword = (FormInputText) view.findViewById(R.id.tv_password);
        tvForgotPassword = (TextView) view.findViewById(R.id.tv_forgot_password);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar.setDelegate(getTag(), this);
        actionBar.setTitle(getString(R.string.log_in));
        actionBar.setTextNextColor(ContextCompat.getColor(getContext(), R.color.color_dim_text));

        tvUsername.renderDara(getString(R.string.username), false);
        tvPassword.renderDara(getString(R.string.password), true);
        tvUsername.setDelegate(this, Constants.TAG_CONTROL_INPUT_USERNAME);
        tvPassword.setDelegate(this, Constants.TAG_CONTROL_INPUT_PASSWORD);
        tvForgotPassword.setText(Utils.fromHtml(getString(R.string.forgot_password)));

        tvForgotPassword.setOnClickListener(mySingleListener);
    }

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_forgot_password:
                    ForgotPasswordFragment fragment = ForgotPasswordFragment.newInstance();
                    replace(fragment, ForgotPasswordFragment.TAG, true, true);
            }
        }
    };

    @Override
    public void didBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void didNext() {
        Utils.hideSoftKeyboard(getActivity());
        bFlagButtonNext = true;
        if (validate()) {
            networkManager.requestApi(networkManager.signIn(username, password), Constants.ID_SIGN_IN);
        }
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
        validate();
    }

    @Override
    public void didReturn(String tag) {
        Utils.hideSoftKeyboard(getActivity());
        bFlagButtonNext = true;
        if (validate()) {
            networkManager.requestApi(networkManager.signIn(username, password), Constants.ID_SIGN_IN);
        }
    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }

    private boolean validate() {
        boolean isValid = true;
        if (bFlagButtonNext) {
            tvUsername.setIssuseText("");
            tvPassword.setIssuseText("");
        }
        if (TextUtils.isEmpty(username)) {
            if (bFlagButtonNext) {
                tvUsername.setIssuseText(getString(R.string.email_blank));
            }
            isValid = false;
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

    private void loginSuccess(UserTokenModel model) {
        UserPreferences.setPrefUserAccessToken(model.getTokenCode());
        UserPreferences.setPrefUserData(model.getUserInfo());
        startNewActivity(MainActivity.class, new Bundle());
        ActivityCompat.finishAffinity(getActivity());
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_SIGN_IN:
                    UserTokenModel userTokenModel = gson.fromJson(response, UserTokenModel.class);
                    if (userTokenModel != null) {
                        loginSuccess(userTokenModel);
                    }
                    break;
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {
            switch (idRequest) {
                case Constants.ID_SIGN_IN:
                    ErrorModel errorModel = gson.fromJson(response, ErrorModel.class);
                    if (!TextUtils.isEmpty(errorModel.getErrorMsg())) {
                        Toast.makeText(getActivity(), errorModel.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
}
