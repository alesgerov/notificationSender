package notification.domain;

import java.util.Arrays;

public enum NotificationStatus {
    PENDING(1),
    IN_PROGRESS(2),
    SUCCESS(3),
    ERROR(4);

    private  int id;

    NotificationStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static NotificationStatus from(int id){
        return Arrays.stream(values())
                .filter(notificationStatus -> notificationStatus.id==id)
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("Invalid status id"));
    }
}
