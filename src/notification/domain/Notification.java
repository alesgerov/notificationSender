package notification.domain;

import java.time.LocalDateTime;

public class Notification {
    private long id;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String sender;
    private String receiver;
    private String body;
    private String subject;
    private LocalDateTime insertDate;
    private LocalDateTime processDate;
    private String logData;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", channel=" + channel +
                ", status=" + status +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", body='" + body + '\'' +
                ", subject='" + subject + '\'' +
                ", insertDate=" + insertDate +
                ", processDate=" + processDate +
                ", logData='" + logData + '\'' +
                '}';
    }

    public LocalDateTime getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDateTime processDate) {
        this.processDate = processDate;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(LocalDateTime insertDate) {
        this.insertDate = insertDate;
    }
}
