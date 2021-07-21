package jdbc.functions;

import notification.domain.Notification;
import notification.domain.NotificationChannel;
import notification.domain.NotificationStatus;

import java.sql.Connection;
import java.util.List;

public interface DbFunctions {

    boolean ifExists(String table);

    void createNotificationQueue();

    void createNotificationChannel();

    void createNotificationStatus();

    void createTable();

    List<Notification> getNotifications(NotificationChannel channel, NotificationStatus status);

    Notification update(Notification notification);



}
