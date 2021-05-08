package database_handler;
import java.sql.*;
public class DBConnect {


    public Connection getConnection() throws ClassNotFoundException
    {
          Connection conn = null;
        try
        {

            conn = DriverManager.getConnection("jdbc:mysql://localhost/pos?" +"user=root&password=root");

            return conn;

        }catch(Exception e)
        {
            throw new RuntimeException(e);
        }




    }
}
