module com.lemees.fxgrid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.reflections;
    requires jdk.xml.dom;


    opens com.lemees.fxgrid to javafx.fxml;
    exports com.lemees.fxgrid;
}