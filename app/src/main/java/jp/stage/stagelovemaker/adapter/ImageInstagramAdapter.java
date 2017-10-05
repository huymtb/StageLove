package jp.stage.stagelovemaker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.CommonActivity;
import jp.stage.stagelovemaker.fragment.InstagramViewFragment;
import jp.stage.stagelovemaker.model.InstagramPhoto;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.OnSingleClickListener;

/**
 * Created by congn on 8/7/2017.
 */

public class ImageInstagramAdapter extends BaseAdapter {

    ArrayList<InstagramPhoto> arrayImage;
    int sizeButton;
    String userNameInstagram;
    private Context mContext;
    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View view) {
            InstagramViewFragment fragment = InstagramViewFragment.newInstance(arrayImage, ((Integer) view.getTag()),
                    userNameInstagram);
            CommonActivity activity = (CommonActivity) mContext;
            activity.addNoneSlideIn(fragment, "", true, true, R.id.flContainer);
        }
    };

    public ImageInstagramAdapter(Context c, ArrayList<InstagramPhoto> images, int sizeButton, String userName) {
        arrayImage = images;
        mContext = c;
        this.sizeButton = sizeButton;
        userNameInstagram = userName;
    }

    @Override
    public int getCount() {
        return arrayImage.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        com.makeramen.roundedimageview.RoundedImageView imageView;

        if (view == null) {
            imageView = new com.makeramen.roundedimageview.RoundedImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(sizeButton, sizeButton));
        } else {
            imageView = (com.makeramen.roundedimageview.RoundedImageView) view;
        }
        imageView.setCornerRadius(Utils.dip2px(mContext, 2));
        Utils.setAvatar(mContext, imageView, arrayImage.get(i).getThumbnail_url());
        //imageView.setTag(i);
        //imageView.setOnClickListener(mySingleListener);
        return imageView;
    }
}
