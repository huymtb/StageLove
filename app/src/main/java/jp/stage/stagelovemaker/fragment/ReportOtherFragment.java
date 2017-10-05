package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.os.Bundle;
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
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.FormInputText;

/**
 * Created by congnguyen on 8/29/17.
 */

public class ReportOtherFragment extends BaseFragment implements View.OnClickListener, IHttpResponse,
        FormInputText.FormInputTextDelegate {
    public static final String TAG = "ReportOtherFragment";
    TextView cancelBtn;
    TextView sendBtn;
    FormInputText inappropriate_input;
    NetworkManager networkManager;
    int id;
    String reason = "";
    Boolean bFlagSend = false;
    ReportOtherFragmentDelegate delegate;
    Context context;

    public static ReportOtherFragment newInstance(int userId) {
        ReportOtherFragment fragment = new ReportOtherFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_DATA, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setDelegate(ReportOtherFragmentDelegate fragment) {
        delegate = fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(context, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_other, container, false);
        cancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
        sendBtn = (TextView) view.findViewById(R.id.send_btn);
        inappropriate_input = (FormInputText) view.findViewById(R.id.inappropriate_messages_input);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cancelBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        inappropriate_input.renderDara(getString(R.string.other_inappropriate), false);
        inappropriate_input.setDelegate(this, Constants.TAG_CONTROL_INPUT_INAPPROPRIATE);
        Bundle b = getArguments();
        id = b.getInt(Constants.KEY_DATA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn: {
                getActivity().onBackPressed();
                break;
            }
            case R.id.send_btn: {
                Utils.hideSoftKeyboard(getActivity());
                bFlagSend = true;
                if (validate()) {
                    getActivity().onBackPressed();
                    reportUser();

                }
                break;
            }
        }
    }

    Boolean validate() {
        Boolean isValidate;
        isValidate = true;
        if (bFlagSend) {
            inappropriate_input.setIssuseText("");
        }
        if (TextUtils.isEmpty(reason)) {
            if (bFlagSend) {
                inappropriate_input.setIssuseText(getString(R.string.field_blank));
            }
            isValidate = false;
        }

        if (bFlagSend) {
            bFlagSend = false;
        }
        return isValidate;
    }

    public void reportUser() {
        networkManager.requestApi(networkManager.report(id, reason), Constants.ID_REPORT_USER);
    }

    @Override
    public void onHttpComplete(String response, final int idRequest) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (idRequest == Constants.ID_REPORT_USER) {
                    if (delegate != null) {
                        delegate.onReportOtherFinished();
                    }
                }
            });
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }

    @Override
    public void valuechange(String tag, String text) {
        if (tag.equals(Constants.TAG_CONTROL_INPUT_INAPPROPRIATE)) {
            reason = text;
        }
        validate();
    }

    @Override
    public void didReturn(String tag) {

    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }

    public interface ReportOtherFragmentDelegate {
        void onReportOtherFinished();
    }
}
