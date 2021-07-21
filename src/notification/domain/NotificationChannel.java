package notification.domain;

import java.util.Arrays;

public enum NotificationChannel {
    EMAIL(1),
    SMS(2),
    TELEGRAM(3),
    WHATSAPP(4);

    private  int id;

    NotificationChannel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static NotificationChannel from(int id){
        return Arrays.stream(values())
                .filter(notificationStatus -> notificationStatus.id==id)
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("Invalid channel id"));
    }
}
