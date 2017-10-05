package jp.stage.stagelovemaker.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.InfoRoomModel;
import jp.stage.stagelovemaker.model.RoomResponseModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.model.UserTokenModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 8/29/2017.
 */

public class NewMatchFragment extends BaseFragment implements IHttpResponse {
    public static final String TAG = "NewMatchFragment";
    NetworkManager networkManager;

    Button btChat;
    TextView btLater;
    RelativeLayout layoutLater;
    TextView tvMyName;
    TextView tvMatchName;
    CircleImageView ivMyAvatar;
    CircleImageView ivMatchAvatar;
    UserInfoModel myUser;
    UserInfoModel matchUser;
    RelativeLayout layoutMy;
    RelativeLayout layoutMatch;
    String idChatRoom;
    int widthScreen;
    Gson gson;
    NewMatchDelegate delegate;

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.later_btn:
                    getActivity().onBackPressed();
                    break;
                case R.id.chat_btn:
                    getChatRoom();
                    break;
            }
        }
    };

    public static NewMatchFragment newInstance(UserInfoModel currentUser, UserInfoModel matchUser) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_DATA, currentUser);
        args.putParcelable(Constants.KEY_DATA_TWO, matchUser);
        NewMatchFragment fragment = new NewMatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(context, this);
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_match, container, false);
        btChat = (Button) view.findViewById(R.id.chat_btn);
        btLater = (TextView) view.findViewById(R.id.later_btn);
        layoutLater = (RelativeLayout) view.findViewById(R.id.later_layout);
        tvMyName = (TextView) view.findViewById(R.id.my_name_txt);
        tvMatchName = (TextView) view.findViewById(R.id.pal_name_txt);
        ivMatchAvatar = (CircleImageView) view.findViewById(R.id.pals_avatar);
        ivMyAvatar = (CircleImageView) view.findViewById(R.id.my_avatar);
        layoutMatch = (RelativeLayout) view.findViewById(R.id.new_pal_layout);
        layoutMy = (RelativeLayout) view.findViewById(R.id.my_pal_layout);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("myUser", myUser);
        outState.putParcelable("matchUser", matchUser);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((GradientDrawable) layoutLater.getBackground()).setColor(Color.WHITE);
        ((GradientDrawable) layoutLater.getBackground()).setStroke((int) Utils.dip2px(getContext(), 2),
                ContextCompat.getColor(getContext(), R.color.dark_gray));

        widthScreen = Utils.getiWidthScreen(getActivity()) / 2;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutMatch.getLayoutParams();
        params.width = widthScreen;
        params.leftMargin = -widthScreen;
        layoutMatch.requestLayout();

        RelativeLayout.LayoutParams myParams = (RelativeLayout.LayoutParams) layoutMy.getLayoutParams();
        myParams.width = widthScreen;
        myParams.rightMargin = -widthScreen;
        layoutMy.requestLayout();

        if (savedInstanceState != null) {
            matchUser = savedInstanceState.getParcelable("matchUser");
            myUser = savedInstanceState.getParcelable("myUser");
        } else {
            myUser = getArguments().getParcelable(Constants.KEY_DATA);
            matchUser = getArguments().getParcelable(Constants.KEY_DATA_TWO);
        }
        loadData();

        if (myUser != null) {
            loadMyData();
        } else {
            getMyProfile();
        }

        btChat.setOnClickListener(mySingleListener);
        btLater.setOnClickListener(mySingleListener);
    }

    public void getMyProfile() {
        int id = UserPreferences.getCurrentUserId();
        networkManager.requestApi(networkManager.getProfile(id), Constants.ID_SELF_INFO);
    }

    public void loadData() {
        tvMatchName.setText(matchUser.getFirstName() + ", " + matchUser.getAge());
        Utils.setAvatar(getContext(), ivMatchAvatar, matchUser.getAvatars().get(0).getUrl());
    }

    void loadMyData() {
        tvMyName.setText(myUser.getFirstName() + ", " + myUser.getAge());
        Utils.setAvatar(getContext(), ivMyAvatar, myUser.getAvatars().get(0).getUrl());

        ObjectAnimator transMy = ObjectAnimator.ofFloat(layoutMatch, "translationX", widthScreen);
        transMy.setDuration(700);
        transMy.start();

        ObjectAnimator transNew = ObjectAnimator.ofFloat(layoutMy, "translationX", -widthScreen);
        transNew.setDuration(700);
        transNew.start();
    }

    void getChatRoom() {
        networkManager.requestApiNoProgress(networkManager.getRoom(matchUser.getId()), Constants.ID_CHAT_ROOM);
    }

    void handleResponse(final int idRequest) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                switch (idRequest) {
                    case Constants.ID_SELF_INFO:
                        loadMyData();
                        break;
                    case Constants.ID_CHAT_ROOM:
                        if (delegate != null) {
                            delegate.onShowChatWithMatch(matchUser, idChatRoom);
                        }
                        break;
                }
            });
        }
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        switch (idRequest) {
            case Constants.ID_SELF_INFO:
                UserTokenModel userTokenModel = gson.fromJson(response, UserTokenModel.class);
                if (userTokenModel != null) {
                    myUser = userTokenModel.getUserInfo();
                    handleResponse(idRequest);
                }
                break;
            case Constants.ID_CHAT_ROOM:
                RoomResponseModel roomResponseModel = gson.fromJson(response, RoomResponseModel.class);
                if (roomResponseModel != null) {
                    InfoRoomModel roomInfo = roomResponseModel.getInfoRoom();
                    idChatRoom = roomInfo.getChatRoomId();
                    handleResponse(idRequest);
                }
                break;

        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }

    public void setDelegate(NewMatchDelegate delegate){
        this.delegate = delegate;
    }

    public interface NewMatchDelegate {
        void onShowChatWithMatch(UserInfoModel matchUser, String chatRoom);
    }
}
