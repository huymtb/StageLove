package jp.stage.stagelovemaker.base;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jp.stage.stagelovemaker.model.NotificationModel;

/**
 * Created by congn on 8/29/2017.
 */

public class NotificationEvent {
    public enum Action {
        NEW_MESSAGE, NEW_LIKE, NEW_SUPER_LIKE, NEW_MATCH
    }

    public final Action action;
    public final NotificationModel notificationModel;

    private NotificationEvent(Action action, NotificationModel notificationModel) {
        this.action = action;
        this.notificationModel = notificationModel;
    }

    public static NotificationEvent newMessage(NotificationModel notificationModel) {
        return new NotificationEvent(Action.NEW_MESSAGE, notificationModel);
    }

    public static NotificationEvent newMatch(NotificationModel notificationModel) {
        return new NotificationEvent(Action.NEW_MATCH, notificationModel);
    }

    public static NotificationEvent newLike(NotificationModel notificationModel) {
        return new NotificationEvent(Action.NEW_LIKE, notificationModel);
    }

    public static NotificationEvent newSuperLike(NotificationModel notificationModel) {
        return new NotificationEvent(Action.NEW_SUPER_LIKE, notificationModel);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("action", action)
                .append("notification", notificationModel.getBody())
                .toString();
    }
}
