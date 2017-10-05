package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.MainActivity;
import jp.stage.stagelovemaker.adapter.ChatAdapter;
import jp.stage.stagelovemaker.adapter.MatchesRecycleAdapter;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.EventDistributor;
import jp.stage.stagelovemaker.model.InfoRoomModel;
import jp.stage.stagelovemaker.model.RoomResponseModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.model.UsersPageModel;
import jp.stage.stagelovemaker.model.UsersPageResponseModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.views.FormInputText;
import jp.stage.stagelovemaker.views.SearchEmpty;
import jp.stage.stagelovemaker.views.TitleBar;

/**
 * Created by congn on 8/4/2017.
 */

public class MatchesFragment extends BaseFragment implements FormInputText.FormInputTextDelegate,
        IHttpResponse {
    public static final String TAG = "MatchesFragment";

    private static final int EVENTS = EventDistributor.MATCH_CHANGE;

    FormInputText searchInput;
    SearchEmpty searchEmpty;
    NetworkManager networkManager;
    Gson gson;

    protected RecyclerView recyclerViewMatches;
    protected MatchesRecycleAdapter listMatchesAdapter;
    private List<UserInfoModel> userInfoModels;
    private UsersPageModel usersPageModel;

    private RecyclerView rcvChat;
    private ChatAdapter chatAdapter;

    private boolean itemsLoaded = false;
    private boolean viewsCreated = false;

    UserInfoModel receiverModel;

    public static MatchesFragment newInstance() {
        Bundle args = new Bundle();
        MatchesFragment fragment = new MatchesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(context, this);
        gson = new Gson();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventDistributor.getInstance().register(contentUpdate);
        if (viewsCreated && itemsLoaded) {
            onFragmentLoaded();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //listMatchesAdapter.notifyDataSetChanged();
        //chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventDistributor.getInstance().unregister(contentUpdate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //resetViewState();
    }

    private void resetViewState() {
        recyclerViewMatches = null;
        rcvChat = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        searchEmpty = (SearchEmpty) view.findViewById(R.id.search_empty_chat_view);
        searchInput = (FormInputText) view.findViewById(R.id.tv_search);
        recyclerViewMatches = (RecyclerView) view.findViewById(R.id.list);
        rcvChat = (RecyclerView) view.findViewById(R.id.list_message);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchInput.renderDara(getString(R.string.search), false);
        searchInput.setDelegate(this, Constants.TAG_CONTROL_INPUT_SEARCH);
        searchEmpty.setVisibility(View.GONE);

        loadMatches();
    }

    private void onFragmentLoaded() {
        if (listMatchesAdapter == null) {
            MainActivity activity = (MainActivity) getActivity();
            listMatchesAdapter = new MatchesRecycleAdapter(activity, itemAccess);
            listMatchesAdapter.setHasStableIds(true);
            recyclerViewMatches.setAdapter(listMatchesAdapter);
            listMatchesAdapter.notifyDataSetChanged();;
        }

        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(getActivity(), (ArrayList<UserInfoModel>) userInfoModels);
            chatAdapter.setHasStableIds(true);
            chatAdapter.setListener(OnChatAdapter);
            rcvChat.setAdapter(chatAdapter);
            chatAdapter.notifyDataSetChanged();
        }

        if (userInfoModels == null || userInfoModels.size() == 0) {
            recyclerViewMatches.setVisibility(View.GONE);
            searchEmpty.setVisibility(View.VISIBLE);
        } else {
            searchEmpty.setVisibility(View.GONE);
            recyclerViewMatches.setVisibility(View.VISIBLE);
        }
    }

    private MatchesRecycleAdapter.ItemAccess itemAccess = new MatchesRecycleAdapter.ItemAccess() {
        @Override
        public int getCount() {
            return userInfoModels != null ? userInfoModels.size() : 0;
        }

        @Override
        public UserInfoModel getItem(int position) {
            if(userInfoModels == null)
                userInfoModels = new ArrayList<>();
            if (0 <= position && position < userInfoModels.size()) {
                return userInfoModels.get(position);
            }
            return null;
        }

        @Override
        public void onClickItem(UserInfoModel receiver) {
            if (receiver != null) {
                receiverModel = receiver;
                networkManager.requestApi(networkManager.getRoom(receiver.getId()), Constants.ID_CHAT_ROOM);
            }
        }
    };

    @Override
    public void valuechange(String tag, String text) {
        if (tag.equals(Constants.TAG_CONTROL_INPUT_SEARCH)) {
            getActivity().runOnUiThread(() -> {
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (text.length() == 0) {
                        loadMatches();
                    } else {
                        loadMatches(text);
                    }
                }, 200);
            });
        }
    }

    @Override
    public void didReturn(String tag) {

    }

    @Override
    public void inputTextFocus(Boolean b, String tag) {

    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        switch (idRequest) {
            case Constants.ID_LIST_MATCHES:
                UsersPageResponseModel resp = gson.fromJson(response, UsersPageResponseModel.class);
                if (resp.getUsersPage() != null) {
                    usersPageModel = resp.getUsersPage();
                    if (usersPageModel != null) {
                        userInfoModels = new ArrayList<>();
                        if (usersPageModel.getRecords() != null) {
                            userInfoModels.addAll(usersPageModel.getRecords());
                            onFragmentLoaded();
                            if (listMatchesAdapter != null) {
                                listMatchesAdapter.notifyDataSetChanged();

                            }
                            if(chatAdapter != null){
                                chatAdapter.setList((ArrayList<UserInfoModel>) userInfoModels);
                                chatAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
            case Constants.ID_CHAT_ROOM:
                RoomResponseModel roomResponseModel = gson.fromJson(response, RoomResponseModel.class);
                if (roomResponseModel != null) {
                    InfoRoomModel infoRoomModel = roomResponseModel.getInfoRoom();
                    MessageFragment messageFragment = MessageFragment.newInstance(receiverModel, infoRoomModel.getChatRoomId());
                    replace(messageFragment, MessageFragment.TAG, true, true);
                }
                break;
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }

    private EventDistributor.EventListener contentUpdate = new EventDistributor.EventListener() {
        @Override
        public void update(EventDistributor eventDistributor, Integer arg) {
            if ((arg & EVENTS) != 0) {
                Log.d(TAG, "arg: " + arg);
                loadMatches();
            }
        }
    };

    protected void loadMatches() {
        if (viewsCreated && !itemsLoaded) {
            recyclerViewMatches.setVisibility(View.GONE);
        }
        networkManager.requestApiNoProgress(networkManager.listMatches(1, ""), Constants.ID_LIST_MATCHES);
    }

    protected void loadMatches(String query) {
        if (viewsCreated && !itemsLoaded) {
            recyclerViewMatches.setVisibility(View.GONE);
        }
        networkManager.requestApiNoProgress(networkManager.listMatches(1, query), Constants.ID_LIST_MATCHES);
    }

    public ChatAdapter.OnChatAdapterListener OnChatAdapter = (receiver, chatRoomId, name, unreadCount) -> {
        MessageFragment messageFragment = MessageFragment.newInstance(receiver, chatRoomId);
        replace(messageFragment, MessageFragment.TAG, true, true);
    };
}
