package login;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import database_handler.checkLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.MainController;

public class LoginController extends Login{

@FXML
private Button SwitchScene;
@FXML
private TextField txt_username;
@FXML
private PasswordField txt_userpass;
@FXML
private Label login_status;
private String username="";
private String userpass="";
private boolean login_true=false;

checkLogin ch = new checkLogin();
    public void switchToMainScene(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {


          username = txt_username.getText().toString();
          userpass = txt_userpass.getText().toString();
        System.out.println(userpass);
          login_true = ch.login_confirm(username,userpass);
          if(login_true==true) {


              Parent pane = FXMLLoader.load(getClass().getResource("/main/main.fxml"));
              Scene mainScene = new Scene(pane);
              Stage windows = (Stage) ((Node) event.getSource()).getScene().getWindow();
              windows.setScene(mainScene);
              windows.setX(150);
              windows.setY(50);
              windows.show();
          }
             else
        {
          login_status.setText("Wrong Username Or Password Retry");
        }




    }




}
