package jp.stage.stagelovemaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import jp.stage.stagelovemaker.MyApplication;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 8/11/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
                    intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isDestroyed()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyApplication app = Utils.getApplication(SplashActivity.this);
                            if (app != null && !TextUtils.isEmpty(UserPreferences.getPrefUserAccessToken())) {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(SplashActivity.this);
                            } else {
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(SplashActivity.this);
                            }
                        }
                    });
                }
            }
        }, 1000);
    }
}
