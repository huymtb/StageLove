package jp.stage.stagelovemaker;

import android.content.Context;

import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.utils.NetworkUtils;

/**
 * Created by congn on 8/28/2017.
 */

public class ClientConfig {
    private ClientConfig() {
    }

    private static boolean initialized = false;

    public static synchronized void initialize(Context context) {
        if (initialized) {
            return;
        }
        UserPreferences.init(context);
        NetworkUtils.init(context);
        initialized = true;
    }
}
