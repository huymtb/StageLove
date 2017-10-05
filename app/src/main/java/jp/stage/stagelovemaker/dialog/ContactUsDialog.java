package jp.stage.stagelovemaker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 8/24/2017.
 */

public class ContactUsDialog extends BaseFragment implements View.OnClickListener, IHttpResponse,
        FormInputText.FormInputTextDelegate {
    public static final String TAG = "ContactUsDialog";

    TextView cancelTextView;
    TextView okTextView;
    FormInputText contactUsInputText;
    NetworkManager networkManager;
    String content;

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.cancel_btn: {
                    getActivity().onBackPressed();
                    break;
                }
                case R.id.ok_btn: {
                    sendContact();
                    break;
                }
            }
        }
    };

    public static ContactUsDialog newInstance() {
        Bundle args = new Bundle();
        ContactUsDialog fragment = new ContactUsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(context, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_contact_us, container, false);
        cancelTextView = (TextView) view.findViewById(R.id.cancel_btn);
        okTextView = (TextView) view.findViewById(R.id.ok_btn);
        contactUsInputText = (FormInputText) view.findViewById(R.id.contact_us_inputtext);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cancelTextView.setOnClickListener(mySingleListener);
        okTextView.setOnClickListener(mySingleListener);

        contactUsInputText.renderDara(getContext().getString(R.string.max_500_letters), false);
        contactUsInputText.setMaxLength(500);
        contactUsInputText.setDelegate(this, "");
    }

    void sendContact() {
        contactUsInputText.setIssuseText("");
        if (TextUtils.isEmpty(content)) {
            contactUsInputText.setIssuseText(getString(R.string.field_blank));
        } else {
            networkManager.requestApi(networkManager.contacts(content), Constants.ID_CONTACT_US);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> getActivity().onBackPressed());
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> getActivity().onBackPressed());
        }
    }

    @Override
    public void valuechange(String tag, String text) {
        content = text;
    }

    @Override
    public void didReturn(String tag) {

    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }
}
