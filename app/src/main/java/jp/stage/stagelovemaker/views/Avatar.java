package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 8/7/2017.
 */

public class Avatar extends RelativeLayout {
    View rootView;
    CircularImageView iconImageView;
    CircularImageView avatarImageView;

    public Avatar(Context context) {
        super(context);
        init(context);
    }

    public Avatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_avatar, this);
        iconImageView = (CircularImageView) rootView.findViewById(R.id.icon_img);
        avatarImageView = (CircularImageView) rootView.findViewById(R.id.avatar_user_img);
    }

    public void invisibleIconEdit() {
        iconImageView.setVisibility(GONE);
    }

    public void setAvatar(Bitmap avatar) {
        avatarImageView.setImageBitmap(avatar);
    }

    public void setAvatar(String avatar) {
        Glide.with(getContext()).load(avatar).centerCrop().into(avatarImageView);
    }

}
