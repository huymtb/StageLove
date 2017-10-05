package jp.stage.stagelovemaker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.MessageModel;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congnguyen on 8/27/17.
 */

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int CHATS_RECEIVER = 1;
    private static final int CHATS_SENDER = 2;
    private static final int IMAGE_RECEIVER = 3;
    private static final int IMAGE_SENDER = 4;

    private Context context;
    private List<MessageModel> messageModels;
    private String userId;
    private int marginAdjust;
    private int marginNormal;

    String avatarUrl;

    public MessageAdapter(Context context, ArrayList<MessageModel> models) {
        this.context = context;
        this.messageModels = models;
        userId = "";
        marginAdjust = (int) Utils.dip2px(context, 1);
        marginNormal = (int) Utils.dip2px(context, 10);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == CHATS_SENDER) {
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.item_message_sender, parent, false);
            vh = new SenderHolder(v);
        } else if (viewType == CHATS_RECEIVER) {
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.item_message_receiver, parent, false);
            vh = new ReceiverHolder(v);
        } else if (viewType == IMAGE_SENDER) {
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.item_image_sender, parent, false);
            vh = new ImageSenderHolder(v);
        } else if (viewType == IMAGE_RECEIVER) {
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.item_image_receiver, parent, false);
            vh = new ImageReceiverHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MessageModel model = messageModels.get(position);
        if (model == null) {
            return;
        }
        if (TextUtils.isEmpty(model.getContent())) {
            model.setContent("");
        }
        int marginTop = marginNormal;
        int marginBottom = marginNormal;

        String showTime = showCurrentTime(position);
        if (viewHolder instanceof SenderHolder) {
            SenderHolder holder = (SenderHolder) viewHolder;
            if (TextUtils.isEmpty(showTime)) {
                holder.tvHeaderTime.setVisibility(View.GONE);
            } else {
                holder.tvHeaderTime.setVisibility(View.VISIBLE);
                holder.tvHeaderTime.setText(showTime);
            }
            holder.tvTimeSender.setText(Utils.getShowTime(context, model.getCreate()));

            if (!TextUtils.isEmpty(model.getContent())) {
                holder.tvMessageSender.setText(model.getContent());
            }

//            if (model.getAvatars() != null && !item.getAvatars().isEmpty()) {
//                for (int i = 0; i < item.getAvatars().size(); i++) {
//                    if (!TextUtils.isEmpty(item.getAvatars().get(i).getUrl())) {
//                        String url = item.getAvatars().get(i).getUrl();
//                        Glide.with(mainActivity.get())
//                                .load(url)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .fitCenter()
//                                .dontAnimate()
//                                .into(holder.ivAvatar);
//                        break;
//                    }
//                }
//            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder
                    .senderLayout.getLayoutParams();
            if (checkPrevPositionChatSender(position)) {
                marginTop = marginAdjust;
            }
            if (checkNextPositionChatSender(position)) {
                marginBottom = marginAdjust;
            }
            params.topMargin = marginTop;
            params.bottomMargin = marginBottom;
        } else if (viewHolder instanceof ReceiverHolder) {
            ReceiverHolder holder = (ReceiverHolder) viewHolder;
            if (!TextUtils.isEmpty(avatarUrl)) {
                Glide.with(context)
                        .load(avatarUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .dontAnimate()
                        .into(holder.ivAvatarReceiver);
            } else {
                holder.ivAvatarReceiver.setImageResource(R.drawable.ic_receiver);
            }

            holder.tvTimeReceiver.setText(Utils.getShowTime(context, model.getCreate()));
            holder.tvMessageReceiver.setText(model.getContent());
            if (TextUtils.isEmpty(showTime)) {
                holder.tvHeaderTime.setVisibility(View.GONE);
            } else {
                holder.tvHeaderTime.setVisibility(View.VISIBLE);
                holder.tvHeaderTime.setText(showTime);
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.receiverLayout.getLayoutParams();
            if (checkPrevPositionChatReceiver(position)) {
                marginTop = marginAdjust;
            }
            if (checkNextPositionChatReceiver(position)) {
                marginBottom = marginAdjust;
            }
            params.topMargin = marginTop;
            params.bottomMargin = marginBottom;
        } else if (viewHolder instanceof ImageSenderHolder) {
            ImageSenderHolder holder = (ImageSenderHolder) viewHolder;
            if (TextUtils.isEmpty(showTime)) {
                holder.tvHeaderTime.setVisibility(View.GONE);
            } else {
                holder.tvHeaderTime.setVisibility(View.VISIBLE);
                holder.tvHeaderTime.setText(showTime);
            }
            holder.tvTimeSender.setText(Utils.getShowTime(context, model.getCreate()));
            Utils.setImageByUrl(context, holder.ivPhoto, holder.progressBar, model.getContent());

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder
                    .senderLayout.getLayoutParams();
            if (checkPrevPositionChatSender(position)) {
                marginTop = marginAdjust;
            }
            if (checkNextPositionChatSender(position)) {
                marginBottom = marginAdjust;
            }
            params.topMargin = marginTop;
            params.bottomMargin = marginBottom;
        } else if (viewHolder instanceof ImageReceiverHolder) {
            ImageReceiverHolder holder = (ImageReceiverHolder) viewHolder;
            if (TextUtils.isEmpty(showTime)) {
                holder.tvHeaderTime.setVisibility(View.GONE);
            } else {
                holder.tvHeaderTime.setVisibility(View.VISIBLE);
                holder.tvHeaderTime.setText(showTime);
            }
            holder.tvTimeReceiver.setText(Utils.getShowTime(context, model.getCreate()));
            Utils.setImageByUrl(context, holder.ivPhoto, holder.progressBar, model.getContent());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder
                    .receiverLayout.getLayoutParams();
            if (checkPrevPositionChatSender(position)) {
                marginTop = marginAdjust;
            }
            if (checkNextPositionChatSender(position)) {
                marginBottom = marginAdjust;
            }
            params.topMargin = marginTop;
            params.bottomMargin = marginBottom;
        }
    }

    public void setAvatarUrl(String avatar) {
        this.avatarUrl = avatar;
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModels.get(position);

        int senderId = messageModel.getSender_id();
        int id = UserPreferences.getCurrentUserId();
        if (!messageModel.getType().equals("image")) {
            if (senderId == id) {
                return CHATS_SENDER;
            } else {
                return CHATS_RECEIVER;
            }
        } else {
            if (senderId == id) {
                return IMAGE_SENDER;
            } else {
                return IMAGE_RECEIVER;
            }
        }
    }

    public void add(MessageModel chat) {
        messageModels.add(chat);
        notifyItemInserted(messageModels.size() - 1);
    }

    public void setData(List<MessageModel> messageModelList) {
        this.validateData(messageModelList);
        this.messageModels = messageModelList;
        this.notifyDataSetChanged();
    }

    public void validateData(List<MessageModel> messageModelList) {
        if (messageModelList == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    public static class ReceiverHolder extends RecyclerView.ViewHolder {
        CircleImageView ivAvatarReceiver;
        EmojiconTextView tvMessageReceiver;
        TextView tvTimeReceiver;
        LinearLayout receiverLayout;
        TextView tvHeaderTime;

        public ReceiverHolder(View view) {
            super(view);
            tvHeaderTime = (TextView) view.findViewById(R.id.tv_item_time);
            ivAvatarReceiver = (CircleImageView) view.findViewById(R.id.iv_receiver_item_avatar);
            tvMessageReceiver = (EmojiconTextView) view.findViewById(R.id.tv_receiver_content_chat);
            tvTimeReceiver = (TextView) view.findViewById(R.id.tv_receiver_time_chat);
            receiverLayout = (LinearLayout) view.findViewById(R.id.item_receiver_layout);
        }
    }

    public static class SenderHolder extends RecyclerView.ViewHolder {
        CircleImageView ivAvatarSender;
        EmojiconTextView tvMessageSender;
        TextView tvTimeSender;
        TextView tvHeaderTime;
        LinearLayout senderLayout;

        public SenderHolder(View view) {
            super(view);
            ivAvatarSender = (CircleImageView) view.findViewById(R.id.iv_sender_item_avatar);
            tvMessageSender = (EmojiconTextView) view.findViewById(R.id.tv_sender_content_chat);
            tvTimeSender = (TextView) view.findViewById(R.id.tv_sender_time_chat);
            tvHeaderTime = (TextView) view.findViewById(R.id.tv_item_time);
            senderLayout = (LinearLayout) view.findViewById(R.id.item_sender_layout);
        }
    }

    public static class ImageReceiverHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderTime;
        CircleImageView ivAvatarReceiver;
        TextView tvTimeReceiver;
        ImageView ivPhoto;
        ImageView ivAvatar;
        LinearLayout receiverLayout;
        ProgressBar progressBar;

        public ImageReceiverHolder(View view) {
            super(view);
            ivAvatarReceiver = (CircleImageView) view.findViewById(R.id.iv_receiver_item_avatar);
            tvHeaderTime = (TextView) view.findViewById(R.id.tv_item_time);
            tvTimeReceiver = (TextView) view.findViewById(R.id.tv_receiver_time_chat);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_receiver_image);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_receiver_item_avatar);
            receiverLayout = (LinearLayout) view.findViewById(R.id.item_receiver_layout);
            progressBar = (ProgressBar) view.findViewById(R.id.pgb_load);
        }
    }

    public static class ImageSenderHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderTime;
        CircleImageView ivAvatarSender;
        TextView tvTimeSender;
        ImageView ivPhoto;
        LinearLayout senderLayout;
        ProgressBar progressBar;

        public ImageSenderHolder(View view) {
            super(view);
            ivAvatarSender = (CircleImageView) view.findViewById(R.id.iv_sender_item_avatar);
            tvHeaderTime = (TextView) view.findViewById(R.id.tv_item_time);
            tvTimeSender = (TextView) view.findViewById(R.id.tv_sender_time_chat);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_sender_image);
            senderLayout = (LinearLayout) view.findViewById(R.id.item_sender_layout);
            progressBar = (ProgressBar) view.findViewById(R.id.pgb_load);
        }
    }

    private Date getZeroTimeDate(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        res = calendar.getTime();
        return res;
    }

    private String showCurrentTime(int position) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            MessageModel model = messageModels.get(position);

            Date value = null;
            if (!TextUtils.isEmpty(model.getCreate())) {
                value = getZeroTimeDate(simpleDateFormat.parse(model.getCreate()));
            }
            if (value != null) {
                if (position > 0) {
                    MessageModel modelPrev = messageModels.get(position - 1);
                    Date valuePrev;
                    if (!TextUtils.isEmpty(modelPrev.getCreate())) {
                        valuePrev = getZeroTimeDate(simpleDateFormat.parse(modelPrev.getCreate()));
                        if (value.compareTo(valuePrev) != 0) {
                            SimpleDateFormat sdf = new SimpleDateFormat("------------- MM月dd日yyyy -------------", Locale.US);
                            //String formatDate = Utils.formatDateLocal(context, value);
                            return sdf.format(value);
                        } else {
                            return null;
                        }
                    }
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("------------- MM月dd日yyyy -------------", Locale.US);
                    //String formatDate = Utils.formatDateLocal(context, value);
                    return sdf.format(value);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean checkPrevPositionChatSender(int position) {
        if (position - 1 >= 0) {
            MessageModel messageModel = messageModels.get(position - 1);
            if (userId.equals(messageModel.getSender_id())) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkNextPositionChatSender(int position) {
        if (position + 1 < messageModels.size()) {
            MessageModel messageModel = messageModels.get(position + 1);
            if (userId.equals(messageModel.getSender_id())) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkPrevPositionChatReceiver(int position) {
        if (position - 1 >= 0) {
            MessageModel messageModel = messageModels.get(position - 1);
            if (!userId.equals(messageModel.getSender_id())) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkNextPositionChatReceiver(int position) {
        if (position + 1 < messageModels.size()) {
            MessageModel messageModel = messageModels.get(position + 1);

            if (!userId.equals(messageModel.getSender_id())) {
                return true;
            }

        }
        return false;
    }
}
