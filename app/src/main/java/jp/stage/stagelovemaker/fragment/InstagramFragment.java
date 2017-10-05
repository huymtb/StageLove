package jp.stage.stagelovemaker.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.model.InstagramUserModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.InstagramNetworkManager;
import jp.stage.stagelovemaker.utils.API;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.TitleBar;

/**
 * Created by congn on 8/7/2017.
 */

public class InstagramFragment extends BaseFragment implements TitleBar.TitleBarCallback, IHttpResponse {
    public static final String TAG = "InstagramFragment";
    TitleBar titleBar;
    String mAuthUrl;
    WebView webView;
    ProgressDialog progressBar;
    InstagramNetworkManager instagramNetworkManager;
    String accessToken;
    InstagramFragmentDelegate delegate;

    public static InstagramFragment createInstance() {
        InstagramFragment fragment = new InstagramFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        progressBar = new ProgressDialog(context);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instagramNetworkManager = new InstagramNetworkManager(getContext(), this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthUrl = Constants.INSTAGRAM_AUTH_URL
                + "?client_id="
                + Constants.INSTAGRAM_CLIENT_ID
                + "&redirect_uri="
                + Constants.INSTAGRAM_CALLBACK_URL
                + "&response_type=token&display=touch&scope=likes+comments+relationships";

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instagram, container, false);
        titleBar = (TitleBar) view.findViewById(R.id.titleBar);
        webView = (WebView) view.findViewById(R.id.instagram_webview);
        return view;
    }

    public void setDelegate(BaseFragment fragment) {
        delegate = (InstagramFragmentDelegate) fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.enableBackButton();
        titleBar.setTitle(getString(R.string.instagram));
        titleBar.setCallback(this);

        signinInstagram();
    }

    void signinInstagram() {
        Utils.clearCookies(getContext());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new OAuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(mAuthUrl);
    }

    void getInfoUser(String code) {
        progressBar.show();
        progressBar.setContentView(R.layout.dialog_progress_bar);

        accessToken = code;

        instagramNetworkManager.requestInstagramApi(instagramNetworkManager.getUserInfo(accessToken),
                API.INSTAGRAM_USER_INFO);

    }

    void handleResponeApi(final InstagramUserModel model) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.setAccessToken(accessToken);
                    if (delegate != null) {
                        //delegate.instagramUserInfo(model);
                        delegate.instagramUserInfo("congnguyen3887");
                    }
                }
            });
        }
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        if ((progressBar != null) && progressBar.isShowing()) {
            progressBar.dismiss();
        }
        Gson gson = new Gson();
        switch (idRequest) {
            case API.INSTAGRAM_USER_INFO: {
//                InstagramUserModel model = gson.fromJson(response, InstagramUserModel.class);
//                if (model != null) {
//                    handleResponeApi(model);
//                    if (delegate != null) {
//                        delegate.instagramUserInfo(model);
//                        onBackButtonClicked();
//                    }
//                }
                if(delegate != null){
                    delegate.instagramUserInfo("congnguyen3887");
                    getActivity().onBackPressed();
                }
                break;
            }
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {
        if ((progressBar != null) && progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    @Override
    public void onTitleBarClicked() {

    }

    @Override
    public void onRightButtonClicked() {

    }

    @Override
    public void onBackButtonClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onMiddleButtonClicked() {

    }

    public interface InstagramFragmentDelegate {
        //void instagramUserInfo(InstagramUserModel model);
        void instagramUserInfo(String username);
    }

    private class OAuthWebViewClient extends WebViewClient {


        private boolean handleUrl(final String url) {
            if (url.startsWith(Constants.INSTAGRAM_CALLBACK_URL)) {
                String urls[] = url.split("=");
                getInfoUser(urls[1]);
                return true;
            }
            return false;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return handleUrl(url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return handleUrl(request.getUrl().toString());
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.show();
            progressBar.setContentView(R.layout.dialog_progress_bar);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if ((progressBar != null) && progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if ((progressBar != null) && progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }
}
