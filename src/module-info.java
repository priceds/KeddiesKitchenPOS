module PointOfSale
{
    opens login;
    opens main;
    opens entities;
    opens database_handler;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires pdfbox.app;

}