package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import jp.stage.stagelovemaker.R;

/**
 * Created by congn on 7/11/2017.
 */

public class MainTabBar extends RelativeLayout implements View.OnClickListener {
    public static final int TAB_PROFILE = 1;
    public static final int TAB_STAGE = 2;
    public static final int TAB_CHAT = 3;

    View rootView;

    RelativeLayout profileLayout;
    ImageView profileImageView;

    RelativeLayout stageLayout;
    ImageView stageImageView;

    RelativeLayout chatLayout;
    ImageView chatImageView;

    int currentTab = TAB_PROFILE;

    MainTabBarCallback callback;

    public MainTabBar(Context context) {
        super(context);
        init(context);
    }

    public MainTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_main_tabbar, this);

        profileLayout = (RelativeLayout) rootView.findViewById(R.id.profile_layout);
        profileImageView = (ImageView) rootView.findViewById(R.id.profile_img);

        stageLayout = (RelativeLayout) rootView.findViewById(R.id.stage_layout);
        stageImageView = (ImageView) rootView.findViewById(R.id.stage_img);

        chatLayout = (RelativeLayout) rootView.findViewById(R.id.chat_layout);
        chatImageView = (ImageView) rootView.findViewById(R.id.chat_img);

        profileLayout.setOnClickListener(this);
        stageLayout.setOnClickListener(this);
        chatLayout.setOnClickListener(this);

        changeTab(currentTab);
    }

    public void changeTab(int index) {
        if (callback != null) {
            callback.onTabChanged(index);
        }
        if (index == TAB_PROFILE) {
            profileImageView.setImageResource(R.mipmap.ic_main_pink);
        } else {
            profileImageView.setImageResource(R.mipmap.ic_main_gray);
        }

        if (index == TAB_STAGE) {
            stageImageView.setImageResource(R.mipmap.ic_flame_pink);
        } else {
            stageImageView.setImageResource(R.mipmap.ic_flame_gray);
        }

        if (index == TAB_CHAT) {
            chatImageView.setImageResource(R.mipmap.ic_chat_pink);
        } else {
            chatImageView.setImageResource(R.mipmap.ic_chat_gray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_layout:
                changeTab(TAB_PROFILE);
                break;
            case R.id.stage_layout:
                changeTab(TAB_STAGE);
                break;
            case R.id.chat_layout:
                changeTab(TAB_CHAT);
                break;
        }
    }

    public void setCallback(MainTabBarCallback callback) {
        this.callback = callback;
    }

    public interface MainTabBarCallback {
        void onTabChanged(int index);
    }
}
