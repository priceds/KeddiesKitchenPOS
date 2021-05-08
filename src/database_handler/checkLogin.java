package database_handler;
import java.sql.SQLException;
import java.sql.*;
public class checkLogin {

   private String name ="";
   private String pass="";
public boolean login_confirm(String name,String pass) throws ClassNotFoundException, SQLException {
    boolean login_confirmation=false;
    this.name=name;
    this.pass=pass;
    DBConnect db = new DBConnect();
     Connection openConn = db.getConnection();


    ResultSet rs = null;
     if(openConn!=null)
     {
         System.out.println("Connected");
     }
    PreparedStatement stmt = openConn.prepareStatement("SELECT * FROM users WHERE user_name=? AND user_pass=?");
     stmt.setString(1,this.name);
     stmt.setString(2,this.pass);
    rs = stmt.executeQuery();

    // or alternatively, if you don't know ahead of time that
    // the query will be a SELECT...

 while(rs.next())
 {
     System.out.println(rs.getString("user_name")+" "+rs.getString("user_pass"));
     login_confirmation=true;



 }






   return login_confirmation;
}







}
