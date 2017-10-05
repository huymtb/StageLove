package jp.stage.stagelovemaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.MessageModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.OnSingleClickListener;
import retrofit2.http.Query;

import static jp.stage.stagelovemaker.utils.Constants.ARG_CHAT_ROOMS;

/**
 * Created by congnguyen on 8/27/17.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<UserInfoModel> userInfoModels = new ArrayList<>();
    private OnChatAdapterListener listener;
    private DatabaseReference mChatRef;
    private int userId;
    private UserInfoModel currentUser;

    public ChatAdapter(Context context, ArrayList<UserInfoModel> models) {
        this.mContext = context;
        this.userInfoModels = models;
        userId = UserPreferences.getCurrentUserId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        vh = new ChatHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserInfoModel userInfoModel = userInfoModels.get(position);
        int otherId = userInfoModel.getId();
        DatabaseReference userRef;
        if (otherId > userId) {
            userRef = FirebaseDatabase.getInstance().getReference().child(ARG_CHAT_ROOMS)
                    .child(userId + "_" + otherId);
        } else {
            userRef = FirebaseDatabase.getInstance().getReference().child(ARG_CHAT_ROOMS)
                    .child(otherId + "_" + userId);
        }
        com.google.firebase.database.Query lastQuery = userRef.orderByKey().limitToLast(1);

        if (holder instanceof ChatHolder) {
            ChatHolder chatHolder = (ChatHolder) holder;
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        int unread = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            boolean read = (boolean) child.child("read").getValue();
                            long senderId = (long) child.child("sender_id").getValue();
                            if (userInfoModel.getId() == (senderId) && !read) {
                                unread++;
                            }
                        }
                        if (unread > 0) {
                            chatHolder.tvNumberChat.setVisibility(View.VISIBLE);
                            chatHolder.tvNumberChat.setText(String.valueOf(unread));
                        } else {
                            chatHolder.tvNumberChat.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            lastQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String lastMessage = (String) child.child("content").getValue();
                        if(!TextUtils.isEmpty(lastMessage)){
                            ((ChatHolder) holder).tvLastChat.setText(lastMessage);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if (userInfoModel.getAvatars() != null && !userInfoModel.getAvatars().isEmpty()) {
                for (int i = 0; i < userInfoModel.getAvatars().size(); i++) {
                    if (!TextUtils.isEmpty(userInfoModel.getAvatars().get(i).getUrl())) {
                        String url = userInfoModel.getAvatars().get(i).getUrl();
                        Glide.with(mContext)
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .fitCenter()
                                .dontAnimate()
                                .into(((ChatHolder) holder).ivAvatar);
                        break;
                    }
                }
            }
            chatHolder.tvName.setText(userInfoModel.getFirstName() + " " + userInfoModel.getLastName());

            if (position % 2 == 0) {
                chatHolder.userLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.colorLightYellow));
            } else {
                chatHolder.userLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.colorLightOrange));
            }
            chatHolder.userLayout.setTag(userInfoModel);
            chatHolder.userLayout.setOnClickListener(mySingleListener);
        }

    }

    @Override
    public int getItemCount() {
        return userInfoModels.size();
    }

    public void setList(ArrayList<UserInfoModel> userModels) {
        this.userInfoModels.clear();
        this.userInfoModels.addAll(userModels);
        notifyDataSetChanged();
    }

    static class ChatHolder extends RecyclerView.ViewHolder {
        //final TextView tvId;
        final CircleImageView ivAvatar;
        final TextView tvName;
        final TextView tvNumberChat;
        final TextView tvTime;
        final TextView tvLastChat;
        final RelativeLayout userLayout;

        ChatHolder(View view) {
            super(view);
            ivAvatar = (CircleImageView) view.findViewById(R.id.avatar_img);
            //tvId = (TextView) view.findViewById(R.id.tv_user_id);
            tvName = (TextView) view.findViewById(R.id.name_txt);
            tvLastChat = (TextView) view.findViewById(R.id.chat_txt);
            tvNumberChat = (TextView) view.findViewById(R.id.number_chat_txt);
            tvTime = (TextView) view.findViewById(R.id.time_txt);
            userLayout = (RelativeLayout) view.findViewById(R.id.cell_chat_layout);
        }
    }

    public interface OnChatAdapterListener {
        void onShowMessage(UserInfoModel receiver, String chatRoomId, String name, int unreadCount);
    }

    public void setListener(OnChatAdapterListener listener) {
        this.listener = listener;
    }

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            UserInfoModel userModel = (UserInfoModel) v.getTag();
            if (listener != null && userModel != null) {
                int refreshCountChat = 0;
                String roomId;
                if (userId < userModel.getId()) {
                    roomId = userId + "_" + userModel.getId();
                } else {
                    roomId = userModel.getId() + "_" + userId;
                }
                listener.onShowMessage(userModel,
                        roomId,
                        userModel.getFirstName() + " " + userModel.getLastName(),
                        refreshCountChat);
            }
        }
    };
}
