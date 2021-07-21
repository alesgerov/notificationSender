package jdbc.functions;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {




    public static Connection open(){
        Connection connection=null;
        try {
            Properties properties =new Properties();
            properties.load(new FileInputStream("db.properties"));
            String url=properties.getProperty("jdbc.url");
            String username=properties.getProperty("jdbc.username");
            String password=properties.getProperty("jdbc.password");
            Class.forName(properties.getProperty("jdbc.driver"));
            connection=DriverManager.getConnection(url,username,password);
            connection.setAutoCommit(false);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }



    public static void close(ResultSet resultSet, PreparedStatement preparedStatement,Connection connection){
        try {
            if (resultSet!=null){
                resultSet.close();
            }
            if (preparedStatement!=null){
                preparedStatement.close();
            }
            if (connection!=null){
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
