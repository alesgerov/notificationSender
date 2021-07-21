package notification.task;

import jdbc.functions.OracleFunctions;
import notification.domain.Notification;
import notification.domain.NotificationStatus;
import notification.util.Utils;

import java.time.LocalDateTime;

public class NotificationSender implements Runnable {


    private Notification notification;
    private OracleFunctions oracleFunctions;



    public NotificationSender(Notification notification, OracleFunctions oracleFunctions) {
        this.notification = notification;
        this.oracleFunctions = oracleFunctions;
    }

    @Override
    public  void run() {
        try {
            //set status to progress
            notification.setStatus(NotificationStatus.IN_PROGRESS);
            notification.setProcessDate(LocalDateTime.now());
            oracleFunctions.update(notification);
            //send notification

            Utils.sendMail(notification);





            notification.setStatus(NotificationStatus.SUCCESS);
            notification.setLogData("Success");
            notification.setProcessDate(LocalDateTime.now());


        } catch (Exception e) {
            notification.setStatus(NotificationStatus.ERROR);
            notification.setLogData(e.getMessage());
            notification.setProcessDate(LocalDateTime.now());
            e.printStackTrace();
        } finally {
            oracleFunctions.update(notification);
        }


    }


}
