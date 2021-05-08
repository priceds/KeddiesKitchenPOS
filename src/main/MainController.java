package main;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import database_handler.DBConnect;
import database_handler.DBHandler;
import entities.Dish;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceColorSpace;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class MainController {
    @FXML
    private Button btn_Switch;
    @FXML
    private Label user_logged;

    @FXML
    private Label current_time;

    @FXML
    private TextField txt_searchbar;

    @FXML
    private TextField txt_qty;

    @FXML
    private TextField total_amt;
    @FXML
    private Button btn_add;
    @FXML
    private Button btn_print;

    @FXML
    private Button btn_exit;

    @FXML
    private TableView<Dish> CartTable;

    @FXML
    private TableColumn<Dish,Integer> col_dishid;

    @FXML
    private TableColumn<Dish,String> col_dishname;

    @FXML
    private TableColumn<Dish,Integer> col_dishprice;

    @FXML
    private TableColumn<Dish, Integer> col_dishquant;
    @FXML
    private TableColumn<Dish, Integer> col_totalamt;
    private final AtomicInteger counter = new AtomicInteger(0);


    int total_amount=0;
    int bill_no=0;

    int dish_id=0;
    HashMap<Integer,String> dishInfo = new HashMap<Integer,String>();

    ArrayList<String> Suggestions  =  new ArrayList<String>();
    ObservableList<Dish> oblist =FXCollections.observableArrayList();






    public void initialize()
    {
        showNotification();

//        Showing Connected Notifiction

//    Here the method call is to fetch all dish names from table
        fetchDishNames();


//    Now Here We Convert ArrayList of DishNames To String Array

        String suggestions[]=new String[Suggestions.size()];
        for(int i=0;i<Suggestions.size();i++)
        {
            suggestions[i]=Suggestions.get(i);
        }

//    And Finally We Bind The String Array With Respective TextField
        TextFields.bindAutoCompletion(txt_searchbar,suggestions);



    }

    public void showNotification()
    {
      Image img = new Image("/main/link.png");
      Notifications notificationBuilder = Notifications.create()
              .title("Connected To Database")
              .text("Login Successfull Connected To Database")
              .graphic(new ImageView(img))

              .hideAfter(Duration.seconds(15))
              .position(Pos.BOTTOM_RIGHT)
              .onAction(new EventHandler<ActionEvent>(){
                  @Override
                  public void handle(ActionEvent event)
                  {
                      System.out.println("Clicked On Notification");
                  }
              });
              notificationBuilder.darkStyle();
              notificationBuilder.show();


    }
    public void showprintNotification()
    {
        Image img = new Image("/main/receipt.png");
        Notifications notificationBuilder = Notifications.create()
                .title("Transaction Successfull")
                .text("Bill PDF Generated At C drive")
                .graphic(new ImageView(img))

                .hideAfter(Duration.seconds(15))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event)
                    {
                        try {
                           // Runtime.getRuntime().exec("explorer /select, <C://POSBillNo0.pdf>");
                            Runtime.getRuntime().exec(new String[] {
                                    "explorer.exe",
                                    "/open,",
                                    "\"C:\\POSBillNo0.pdf\""
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });
        notificationBuilder.darkStyle();
        notificationBuilder.show();


    }
    public void switchToScene1(ActionEvent event) throws IOException {

        Parent pane = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
        Scene loginscene = new Scene(pane);
        Stage windows = (Stage) ((Node)event.getSource()).getScene().getWindow();
        windows.setScene(loginscene);
        windows.show();



    }

    public void getTime()
    {
        DateTimeFormatter DTF  =  DateTimeFormatter.ofPattern("HH:mm  dd/MM/yyyy  ");

        LocalDateTime now = LocalDateTime.now();
        String getTime = DTF.format(now);
        current_time.setText("Time : "+getTime);
    }


    public void getPrice() throws SQLException {
        int temp_dval=0;
        int qty=Integer.parseInt(txt_qty.getText());
        String dish_name = txt_searchbar.getText();
        for(Map.Entry<Integer,String> entry :dishInfo.entrySet())
        {

          if(dish_name.equals(entry.getValue()))
          {
              dish_id= entry.getKey();

          }

        }
       // dish_id=Integer.parseInt(txt_searchbar.getText());
        int dishTotalPrice=0;

        DBHandler dhv = new DBHandler();
        temp_dval=dhv.getDishPrice(dish_id);
        dishTotalPrice=temp_dval*qty;
        total_amount+=dishTotalPrice;
        total_amt.setText(String.valueOf(total_amount));

        fetchData(dish_id,qty,dishTotalPrice);


    }

    public void fetchData(int dish_id,int qty,int dishTotaPrice)
    {
        try
        {
            DBConnect db = new DBConnect();
            Connection Linkr = db.getConnection();

            PreparedStatement pst = Linkr.prepareStatement("SELECT * FROM dish WHERE dish_id=?");
            pst.setInt(1,dish_id);
            ResultSet rs = pst.executeQuery();
            while(rs.next())
            {
                oblist.add(new Dish(rs.getInt("dish_id"),rs.getInt("dish_price"), rs.getString("dish_name"),rs.getString("dish_type"),qty,dishTotaPrice));
                CartTable.setItems(oblist);
            }

        }catch(SQLException|ClassNotFoundException ex)
        {
            System.out.println("Failed Becasue of"+ex);
        }
        col_dishid.setCellValueFactory(new PropertyValueFactory<Dish,Integer>("dish_id"));
        col_dishname.setCellValueFactory(new PropertyValueFactory<Dish,String>("dish_name"));
        col_dishprice.setCellValueFactory(new PropertyValueFactory<Dish,Integer>("dish_price"));
        col_dishquant.setCellValueFactory(new PropertyValueFactory<Dish,Integer>("quantity"));
        col_totalamt.setCellValueFactory(new PropertyValueFactory<Dish,Integer>("total"));
    }

    public void fetchDishNames()
    {

        try
        {
            DBConnect db = new DBConnect();
            Connection Link = db.getConnection();
            String sql = "SELECT * FROM dish";
            PreparedStatement ps = Link.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
               int dish_id = rs.getInt("dish_id");
               String dish_name = rs.getString("dish_name");
               dishInfo.put(dish_id,dish_name);
                Suggestions.add(rs.getString("dish_name"));
            }

         for(Map.Entry<Integer,String>entry : dishInfo.entrySet())
         {
             System.out.println("Key :"+entry.getKey()+" Value"+entry.getValue());

         }
        }
        catch(SQLException|ClassNotFoundException ex)
        {
            System.out.println("Failed Because of : "+ex);
        }







    }


    public void printBill() throws IOException {
      int current_AtomicVal = counter.get();
      String fileName="POSBillNo"+current_AtomicVal;
        PDDocument document = new PDDocument();
      PDPage billPage = new PDPage();
      document.addPage(billPage);
      PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document,page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA,12);
        contentStream.newLineAtOffset(25,500);
        String startLine ="________KEDDIE KITCHEN_______";
        contentStream.showText(startLine);

        String text_to_Add = "Your Total Bill Is : "+total_amount;
        contentStream.showText(text_to_Add);

        String endLine ="________Thanks Visit Again_______";
      contentStream.showText(endLine);
      contentStream.endText();
      contentStream.close();
        document.save(new File("C:/"+fileName+".pdf"));
        document.close();
showprintNotification();
       counter.addAndGet(1);

    }


}
