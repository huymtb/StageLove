package jp.stage.stagelovemaker.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.MainActivity;
import jp.stage.stagelovemaker.model.UserInfoModel;

/**
 * Created by congn on 8/24/2017.
 */

public class MatchesRecycleAdapter extends RecyclerView.Adapter<MatchesRecycleAdapter.Holder> {
    public static final String TAG = "MatchesRecycleAdapter";

    private final WeakReference<MainActivity> mainActivity;
    private final ItemAccess itemAccess;
    private int position = -1;

    public MatchesRecycleAdapter(MainActivity mainActivity,
                                 ItemAccess itemAccess) {
        super();
        this.mainActivity = new WeakReference<>(mainActivity);
        this.itemAccess = itemAccess;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_matches, parent, false);
        Holder holder = new Holder(view);
        holder.ivAvatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
        holder.tvName = (TextView) view.findViewById(R.id.tv_name);
        holder.item = null;
        holder.mainActivityRef = mainActivity;
        holder.position = -1;
        view.setTag(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final UserInfoModel item = itemAccess.getItem(position);
        if (item == null) return;
        holder.item = item;
        holder.position = position;
        holder.tvName.setText(item.getFirstName());
        if (item.getAvatars() != null && !item.getAvatars().isEmpty()) {
            for (int i = 0; i < item.getAvatars().size(); i++) {
                if (!TextUtils.isEmpty(item.getAvatars().get(i).getUrl())) {
                    String url = item.getAvatars().get(i).getUrl();
                    Glide.with(mainActivity.get())
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter()
                            .dontAnimate()
                            .into(holder.ivAvatar);
                    break;
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        UserInfoModel item = itemAccess.getItem(position);
        return item != null ? item.getId() : RecyclerView.NO_POSITION;
    }

    @Override
    public int getItemCount() {
        return itemAccess.getCount();
    }

    public UserInfoModel getItem(int position) {
        return itemAccess.getItem(position);
    }

    public int getPosition() {
        int pos = position;
        position = -1;
        return pos;
    }

    public class Holder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        CircleImageView ivAvatar;
        TextView tvName;
        UserInfoModel item;
        WeakReference<MainActivity> mainActivityRef;
        int position;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserInfoModel receiver = itemAccess.getItem(position);
            itemAccess.onClickItem(receiver);
        }

        public UserInfoModel getUser() {
            return item;
        }
    }

    public interface ItemAccess {
        int getCount();

        UserInfoModel getItem(int position);

        void onClickItem(UserInfoModel receiver);
    }
}
