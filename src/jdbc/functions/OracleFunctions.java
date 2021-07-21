package jdbc.functions;

import notification.domain.Notification;
import notification.domain.NotificationChannel;
import notification.domain.NotificationStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OracleFunctions implements DbFunctions{


    //todo method for insert final values

    @Override
    public boolean ifExists( String table) {
        String sql="select COUNT(*) as c from tab " +
                "where  tname=?";
        table=table.toUpperCase(Locale.ROOT);
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;

        connection=JdbcUtils.open();
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,table);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getLong("c")==1){
                    return true;
                }else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

        return false;
    }

    @Override
    public void createNotificationQueue() {

        String  sql="\n" +
                "CREATE TABLE NOTIFICATION_QUEUE \n" +
                "(\n" +
                "  ID NUMBER NOT NULL \n" +
                ", NOTIFICATION_CHANNEL_ID NUMBER NOT NULL \n" +
                ", NOTIFICATION_STATUS_ID NUMBER NOT NULL \n" +
                ", SENDER VARCHAR2(200) NOT NULL \n" +
                ", RECEIVER VARCHAR2(200) NOT NULL \n" +
                ", BODY VARCHAR2(4000) NOT NULL \n" +
                ", SUBJECT VARCHAR2(200) \n" +
                ", INSERT_DATE TIMESTAMP NOT NULL \n" +
                ", PROCESS_DATE TIMESTAMP \n" +
                ", LOG_DATA VARCHAR2(4000) \n" +
                ", PRIMARY KEY (ID)\n" +
                ",CONSTRAINT fk1 FOREIGN key ( NOTIFICATION_CHANNEL_ID) REFERENCES NOTIFICATION_CHANNEL(id)\n" +
                ",CONSTRAINT fk2 FOREIGN key ( NOTIFICATION_STATUS_ID) REFERENCES NOTIFICATION_STATUS(id)\n" +
                ")\n" +
                "\n" +
                "\n";
        PreparedStatement preparedStatement=null;
        Connection connection=JdbcUtils.open();
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    @Override
    public void createNotificationChannel() {
        String sql="CREATE TABLE NOTIFICATION_CHANNEL\n" +
                "(\n" +
                "  ID NUMBER NOT NULL \n" +
                ", NAME VARCHAR2(20) NOT NULL \n" +
                ", CONSTRAINT NOTIFICATION_CHANNEL_PK PRIMARY KEY \n" +
                "  (\n" +
                "    ID \n" +
                "  )\n" +
                "  ENABLE \n" +
                ")";
        PreparedStatement preparedStatement=null;
        Connection connection=JdbcUtils.open();
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    @Override
    public void createNotificationStatus() {
        String sql="CREATE TABLE NOTIFICATION_STATUS \n" +
                "(\n" +
                "  ID NUMBER NOT NULL \n" +
                ", NAME VARCHAR2(20) NOT NULL \n" +
                ", CONSTRAINT NOTIFICATION_STATUS_PK PRIMARY KEY \n" +
                "  (\n" +
                "    ID \n" +
                "  )\n" +
                "  ENABLE \n" +
                ")";
        PreparedStatement preparedStatement=null;
        Connection connection=JdbcUtils.open();
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    @Override
    public void createTable() {
        if (!ifExists("NOTIFICATION_CHANNEL")){
            this.createNotificationChannel();
            System.out.println("cedvel yaradildi");
        }
        if (!ifExists("NOTIFICATION_STATUS")){
            this.createNotificationStatus();
            System.out.println("cedvel yaradildi");
        }
        if (!ifExists("NOTIFICATION_QUEUE")){
            this.createNotificationQueue();
            System.out.println("cedvel yaradildi");
        }
    }

    @Override
    public List<Notification> getNotifications(NotificationChannel channel, NotificationStatus status) {
        List<Notification> notifications=new ArrayList<>();
        PreparedStatement ps=null;
        Connection connection=null;
        ResultSet rs=null;
        try {
            connection=JdbcUtils.open();
//            String sql="select q.id, q.notification_channel_id, q.notification_status_id, q.sender, q.receiver, " +
//                    "    q.subject, q.body, q.insert_date, q.process_date, q.log_data " +
//                    "from notification_queue q join notification_channel c on q.notification_channel_id = c.id " +
//                    "    join notification_status s on q.notification_status_id = s.id " +
//                    "where q.notification_channel_id = nvl(?, q.notification_channel_id) " +
//                    "    and q.notification_status_id = nvl(?, q.notification_status_id) " +
//                    "order by q.id";
            String sql="select q.id, q.notification_channel_id, q.notification_status_id, q.sender, q.receiver, " +
                    "    q.subject, q.body, q.insert_date, q.process_date, q.log_data " +
                    "from notification_queue q join notification_channel c on q.notification_channel_id = c.id " +
                    "    join notification_status s on q.notification_status_id = s.id " +
                    "where q.notification_status_id = ? " +
                    "order by q.id";


            ps = connection.prepareStatement(sql);


            if(status != null) {
                ps.setLong(1, status.getId());
            } else {
                ps.setNull(1, Types.INTEGER);

            }



            rs=ps.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getLong("id"));
                notification.setStatus(NotificationStatus.from(rs.getInt("notification_status_id")));
                notification.setChannel(NotificationChannel.from(rs.getInt("notification_channel_id")));
                notification.setSender(rs.getString("sender"));
                notification.setReceiver(rs.getString("receiver"));
                notification.setBody(rs.getString("body"));
                notification.setSubject(rs.getString("subject"));
                notification.setInsertDate(rs.getTimestamp("insert_date").toLocalDateTime());

                if(rs.getTimestamp("process_date") != null) {
                    notification.setProcessDate(rs.getTimestamp("process_date").toLocalDateTime());
                }

                notification.setLogData(rs.getString("log_data"));
                notifications.add(notification);
                notification=null;

            }
            return notifications;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(rs,ps,connection);
        }

        return notifications;


    }

    @Override
    public Notification update(Notification notification) {
        String sql = "update notification_queue " +
                "set notification_channel_id = ?, " +
                "   notification_status_id = ?, " +
                "   sender = ?, " +
                "   receiver = ?, " +
                "   subject = ?, " +
                "   body = ?, " +
                "   process_date = ?, " +
                "   log_data = ? " +
                "where id = ?";

        Connection connection = JdbcUtils.open();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps=connection.prepareStatement(sql);
            ps.setLong(1,notification.getChannel().getId());
            ps.setLong(2,notification.getStatus().getId());
            ps.setString(3,notification.getSender());
            ps.setString(4,notification.getReceiver());
            ps.setString(5,notification.getSubject());
            ps.setString(6,notification.getBody());

            ps.setString(8,notification.getLogData());
            if (notification.getProcessDate()!=null){
                ps.setTimestamp(7, Timestamp.valueOf(notification.getProcessDate()));
            }

            ps.setLong(9,notification.getId());
            int count = ps.executeUpdate();

            if (count > 0) {
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        }
        return  notification;
    }
}
