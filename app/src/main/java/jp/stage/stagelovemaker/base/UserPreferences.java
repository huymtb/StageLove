package jp.stage.stagelovemaker.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;

import jp.stage.stagelovemaker.model.AvatarModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.utils.Constants;
import timber.log.Timber;

/**
 * Created by congn on 8/28/2017.
 */

public class UserPreferences {
    private UserPreferences() {
    }

    private static final String TAG = "UserPreferences";

    public static final String PREF_USER_ID = "prefUserId";
    public static final String PREF_USER_AVATAR1 = "prefUserAvatar1";
    public static final String PREF_USER_AVATAR2 = "prefUserAvatar2";
    public static final String PREF_USER_AVATAR3 = "prefUserAvatar3";
    public static final String PREF_USER_AVATAR4 = "prefUserAvatar4";
    public static final String PREF_USER_AVATAR5 = "prefUserAvatar5";
    public static final String PREF_USER_AVATAR6 = "prefUserAvatar6";

    public static final String PREF_USER_NOTIFY_NEW_MATCHES = "prefUserNotifyNewMatches";
    public static final String PREF_USER_NOTIFY_MESSAGES = "prefUserNotifyMessages";
    public static final String PREF_USER_NOTIFY_MESSAGE_LIKES = "prefUserNotifyMessageLikes";
    public static final String PREF_USER_NOTIFY_SUPER_LIKES = "prefUserNotifySuperLikes";

    public static final String PREF_USER_DISTANCE_UNIT = "prefUserDistanceUnit";
    public static final String PREF_USER_ACCESS_TOKEN = "prefUserAccessToken";
    public static final String PREF_USER_DATA = "prefUserData";

    private static Context context;
    private static SharedPreferences prefs;

    public static void init(@NonNull Context context) {
        Timber.d("Create new instance of UserPreferences");

        UserPreferences.context = context.getApplicationContext();
        UserPreferences.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getCurrentUserId() {
        return prefs.getInt(PREF_USER_ID, -1);
    }

    public static void setCurrentUserId(int id) {
        prefs.edit()
                .putInt(PREF_USER_ID, id)
                .apply();
    }

    public static String getPrefUserAvatar(int index) {
        switch (index) {
            case 0:
                return prefs.getString(PREF_USER_AVATAR1, null);
            case 1:
                return prefs.getString(PREF_USER_AVATAR2, null);
            case 2:
                return prefs.getString(PREF_USER_AVATAR3, null);
            case 3:
                return prefs.getString(PREF_USER_AVATAR4, null);
            case 4:
                return prefs.getString(PREF_USER_AVATAR5, null);
            case 5:
                return prefs.getString(PREF_USER_AVATAR6, null);
            default:
                return null;
        }
    }

    public static void setPrefUserAvatar(int index, String url) {
        switch (index) {
            case 0:
                prefs.edit().putString(PREF_USER_AVATAR1, url).apply();
                break;
            case 1:
                prefs.edit().putString(PREF_USER_AVATAR2, url).apply();
                break;
            case 2:
                prefs.edit().putString(PREF_USER_AVATAR3, url).apply();
                break;
            case 3:
                prefs.edit().putString(PREF_USER_AVATAR4, url).apply();
                break;
            case 4:
                prefs.edit().putString(PREF_USER_AVATAR5, url).apply();
                break;
            case 5:
                prefs.edit().putString(PREF_USER_AVATAR6, url).apply();
                break;
        }
    }

    public static boolean isNotifyNewMessage() {
        return prefs.getBoolean(PREF_USER_NOTIFY_MESSAGES, true);
    }

    public static void setNotifyNewMessage(boolean isNotify) {
        prefs.edit()
                .putBoolean(PREF_USER_NOTIFY_MESSAGES, isNotify)
                .apply();
    }

    public static boolean isNotifyNewMatch() {
        return prefs.getBoolean(PREF_USER_NOTIFY_NEW_MATCHES, true);
    }

    public static void setNotifyNewMatch(boolean isNotify) {
        prefs.edit()
                .putBoolean(PREF_USER_NOTIFY_NEW_MATCHES, isNotify)
                .apply();
    }

    public static boolean isNotifySuperLike() {
        return prefs.getBoolean(PREF_USER_NOTIFY_SUPER_LIKES, true);
    }

    public static void setNotifySuperLike(boolean isNotify) {
        prefs.edit()
                .putBoolean(PREF_USER_NOTIFY_SUPER_LIKES, isNotify)
                .apply();
    }

    public static boolean isNotifyLike() {
        return prefs.getBoolean(PREF_USER_NOTIFY_MESSAGE_LIKES, true);
    }

    public static void setNotifyLike(boolean isNotify) {
        prefs.edit()
                .putBoolean(PREF_USER_NOTIFY_MESSAGE_LIKES, isNotify)
                .apply();
    }

    public static String getPrefDistanceUnit() {
        return prefs.getString(PREF_USER_DISTANCE_UNIT, Constants.KM);
    }

    public static void setPrefDistanceUnit(String unit) {
        prefs.edit()
                .putString(PREF_USER_DISTANCE_UNIT, unit)
                .apply();
    }

    public static String getPrefUserAccessToken() {
        return prefs.getString(PREF_USER_ACCESS_TOKEN, null);
    }

    public static void setPrefUserAccessToken(String accessToken) {
        prefs.edit()
                .putString(PREF_USER_ACCESS_TOKEN, accessToken)
                .apply();
    }

    public static String getPrefUserData() {
        return prefs.getString(PREF_USER_DATA, null);
    }

    public static void setPrefUserData(UserInfoModel userInfoModel) {
        setCurrentUserId(userInfoModel.getId());
        ArrayList<String> lstAvatar = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lstAvatar.add(null);
        }
        if (userInfoModel.getAvatars() != null && !userInfoModel.getAvatars().isEmpty()) {
            for (AvatarModel avatarModel : userInfoModel.getAvatars()) {
                if (!TextUtils.isEmpty(avatarModel.getUrl())) {
                    lstAvatar.set(avatarModel.getNumberIndex(), avatarModel.getUrl());
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            setPrefUserAvatar(i, lstAvatar.get(i));
        }

        if (userInfoModel.getSetting().getNotifyMessages() != null) {
            setNotifyNewMessage(userInfoModel.getSetting().getNotifyMessages());
        } else {
            setNotifyNewMessage(false);
        }

        if (userInfoModel.getSetting().getNotifyMessageLikes() != null) {
            setNotifyLike(userInfoModel.getSetting().getNotifyMessageLikes());
        } else {
            setNotifyLike(false);
        }

        if (userInfoModel.getSetting().getNotifyNewMatches() != null) {
            setNotifyNewMatch(userInfoModel.getSetting().getNotifyNewMatches());
        } else {
            setNotifyNewMatch(false);
        }

        if (userInfoModel.getSetting().getNotifySuperLikes() != null) {
            setNotifySuperLike(userInfoModel.getSetting().getNotifySuperLikes());
        } else {
            setNotifySuperLike(false);
        }

        setPrefDistanceUnit(userInfoModel.getSetting().getDistanceUnit());
        prefs.edit()
                .putString(PREF_USER_DATA, userInfoModel.toString())
                .apply();
    }

    public static void clear(){
        prefs.edit().clear().apply();
    }
}
