import jdbc.functions.OracleFunctions;
import notification.domain.Notification;
import notification.domain.NotificationStatus;
import notification.task.NotificationSender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestMain {
    //todo mimemessage i notificationda ele
    public static void main(String[] args) {
        OracleFunctions functions=new OracleFunctions();
        functions.createTable();

        int cores=Runtime.getRuntime().availableProcessors();



        ScheduledExecutorService getPendingNotifications= Executors.newScheduledThreadPool(cores/2);
//        ScheduledExecutorService getPendingNotifications= Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService sendPendingNotifications= Executors.newScheduledThreadPool(cores/2);
//        ScheduledExecutorService sendPendingNotifications= Executors.newSingleThreadScheduledExecutor();



        getPendingNotifications.scheduleAtFixedRate(() -> {
            List<Notification> pendingNots=functions.getNotifications(null, NotificationStatus.PENDING);
            System.out.println("Number of pending notifications = " + pendingNots.size());



            pendingNots.forEach(notification -> {
                NotificationSender sender=new NotificationSender(notification,functions);
                sendPendingNotifications.submit(sender);
            });
        },10,15,TimeUnit.SECONDS);













//        shutdownAndAwaitTermination(getPendingNotifications);
//        shutdownAndAwaitTermination(sendPendingNotifications);
    }





































    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
