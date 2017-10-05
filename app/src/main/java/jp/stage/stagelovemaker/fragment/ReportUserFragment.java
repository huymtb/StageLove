package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 8/29/2017.
 */

public class ReportUserFragment extends BaseFragment implements IHttpResponse {
    public static final String TAG = "ReportUserFragment";
    ImageView inappropriateMessagesRadio;
    ImageView inappropriatePhotosRadio;
    ImageView badOfflineRadio;
    ImageView feelspamRadio;
    ImageView otherRadio;
    TextView cancelBtn;
    TextView sendBtn;
    ReportUserFragmentDelegate delegate;
    NetworkManager networkManager;
    int id;
    String reason = "";

    int check = 1;
    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View view) {
            switch (view.getId()) {
                case R.id.inappropriate_messages_radio: {
                    check = 0;
                    checkInfoReport(inappropriateMessagesRadio, inappropriatePhotosRadio, badOfflineRadio, feelspamRadio, otherRadio);
                    break;
                }
                case R.id.inappropriate_photos_radio: {
                    check = 1;
                    checkInfoReport(inappropriatePhotosRadio, inappropriateMessagesRadio, badOfflineRadio, feelspamRadio, otherRadio);
                    break;
                }
                case R.id.bad_offline_radio: {
                    check = 2;
                    checkInfoReport(badOfflineRadio, inappropriatePhotosRadio, inappropriateMessagesRadio, feelspamRadio, otherRadio);
                    break;
                }
                case R.id.feels_like_spam_radio: {
                    check = 3;
                    checkInfoReport(feelspamRadio, inappropriatePhotosRadio, badOfflineRadio, inappropriateMessagesRadio, otherRadio);
                    break;
                }
                case R.id.other_radio: {
                    check = 4;
                    checkInfoReport(otherRadio, inappropriatePhotosRadio, badOfflineRadio, feelspamRadio, inappropriateMessagesRadio);
                    if (delegate != null) {
                        delegate.clickOther();
                    }
                    break;
                }
                case R.id.cancel_btn: {
                    getActivity().onBackPressed();
                    break;
                }
                case R.id.send_btn: {
                    reportUser();
                    break;
                }
            }
        }
    };

    public static ReportUserFragment newInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_DATA, userId);
        ReportUserFragment fragment = new ReportUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHttpComplete(String response, final int idRequest) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (idRequest == Constants.ID_REPORT_USER) {
                    if (delegate != null) {
                        delegate.onReportFinished();
                    }
                }
            });
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }

    public void setDelegate(ReportUserFragmentDelegate fragment) {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_user, container, false);
        inappropriateMessagesRadio = (ImageView) view.findViewById(R.id.inappropriate_messages_radio);
        inappropriatePhotosRadio = (ImageView) view.findViewById(R.id.inappropriate_photos_radio);
        badOfflineRadio = (ImageView) view.findViewById(R.id.bad_offline_radio);
        feelspamRadio = (ImageView) view.findViewById(R.id.feels_like_spam_radio);
        otherRadio = (ImageView) view.findViewById(R.id.other_radio);
        cancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
        sendBtn = (TextView) view.findViewById(R.id.send_btn);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inappropriateMessagesRadio.setOnClickListener(mySingleListener);
        inappropriatePhotosRadio.setOnClickListener(mySingleListener);
        badOfflineRadio.setOnClickListener(mySingleListener);
        feelspamRadio.setOnClickListener(mySingleListener);
        otherRadio.setOnClickListener(mySingleListener);
        cancelBtn.setOnClickListener(mySingleListener);
        sendBtn.setOnClickListener(mySingleListener);

        inappropriatePhotosRadio.performClick();
        Bundle b = getArguments();
        id = b.getInt(Constants.KEY_DATA);
    }

    void checkInfoReport(ImageView check, ImageView uncheck1, ImageView uncheck2, ImageView uncheck3, ImageView uncheck4) {
        check.setImageResource(R.mipmap.icon_radio_check);
        uncheck1.setImageResource(R.mipmap.icon_radio_uncheck);
        uncheck2.setImageResource(R.mipmap.icon_radio_uncheck);
        uncheck3.setImageResource(R.mipmap.icon_radio_uncheck);
        uncheck4.setImageResource(R.mipmap.icon_radio_uncheck);
    }

    public void reportUser() {
        switch (check) {
            case 0: {
                reason = "inappropriate_message";
                break;
            }
            case 1: {
                reason = "inappropriate_photo";
                break;
            }
            case 2: {
                reason = "bad_offline_behavior";
                break;
            }
            case 3: {
                reason = "feel_like_spam";
                break;
            }
            case 4: {
                reason = "other";
                break;
            }
        }
        networkManager.requestApi(networkManager.report(id, reason), Constants.ID_REPORT_USER);
    }

    public interface ReportUserFragmentDelegate {
        void clickOther();

        void onReportFinished();
    }
}
