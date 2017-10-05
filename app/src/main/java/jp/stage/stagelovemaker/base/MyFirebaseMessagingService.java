package jp.stage.stagelovemaker.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import jp.stage.stagelovemaker.MyApplication;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.MainActivity;
import jp.stage.stagelovemaker.model.NotificationModel;
import jp.stage.stagelovemaker.utils.Constants;

/**
 * Created by congnguyen on 8/28/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData());
        }
    }

    private void sendNotification(Map<String, String> messageBody) {
        NotificationModel model = new NotificationModel();
        model.setBody(messageBody.get("body"));
        model.setTitle(messageBody.get("title"));
        model.setType(messageBody.get("type"));
        model.setSenderId(messageBody.get("sender_id"));

        switch (model.getType()) {
            case Constants.NOTIFY_NEW_MATCHES:
                EventBus.getDefault().post(NotificationEvent.newMatch(model));
                break;
            case Constants.NOTIFY_MESSAGES:
                EventBus.getDefault().post(NotificationEvent.newMessage(model));
                break;
            case Constants.NOTIFY_MESSAGE_LIKES:
                EventBus.getDefault().post(NotificationEvent.newLike(model));
                break;
            case Constants.NOTIFY_SUPER_LIKES:
                EventBus.getDefault().post(NotificationEvent.newSuperLike(model));
                break;
        }
        MyApplication app = (MyApplication) this.getApplicationContext();
        int id = UserPreferences.getCurrentUserId();
        if (model.getSenderId() != id) {
//            if (app.getAppShow() == null || !app.getAppShow()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.NOTI_DATA, model);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(model.getTitle())
                    .setContentText(model.getBody())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                RemoteViews notificationView = new RemoteViews(
                        this.getPackageName(),
                        R.layout.item_notification);
                builder.setSmallIcon(R.mipmap.ic_flame_gray);
                builder.setContent(notificationView);
                notificationView.setImageViewResource(R.id.icon_notification_image, R.mipmap.ic_flame_gray);
                notificationView.setTextViewText(R.id.title_textview, model.getTitle());
                notificationView.setTextViewText(R.id.content_textview, model.getBody());
                String time = "";
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
                dateFormatter.setTimeZone(TimeZone.getDefault());
                time = dateFormatter.format(calendar.getTime());
                notificationView.setTextViewText(R.id.time_textview, time);
            } else {
                builder.setSmallIcon(R.mipmap.ic_flame_gray);
            }
            // Notification action buttons

            int notificationId;
            if (model.getType().equals(Constants.NOTIFY_MESSAGES)) {
                notificationId = model.getSenderId();
            } else {
                notificationId = (int) (System.currentTimeMillis() % 10000);
            }
            Notification notification = builder.build();
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_SOUND;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(notificationId, notification);
//            }
        }
    }
}
