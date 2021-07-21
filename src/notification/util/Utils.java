package notification.util;

import notification.domain.Notification;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {

    private static Session newSession(String from){
        Properties properties=new Properties();
        properties.put("mail.smtp.ssl.trust","smtp.gmail.com");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.from",from);
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.port","587");

        return Session.getInstance(properties);
    }
    public static void sendMail(Notification notification)  {



        Properties properties=new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));

        Session session=newSession(notification.getSender());

        MimeMessage message=new MimeMessage(session);
        message.setText(notification.getBody());
        message.setSubject(notification.getSubject());
        message.setRecipients(Message.RecipientType.TO, notification.getReceiver());
        message.setFrom(notification.getSender());
        Transport transport=session.getTransport("smtp");
        transport.connect(properties.getProperty("user"),properties.getProperty("password"));
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

}
