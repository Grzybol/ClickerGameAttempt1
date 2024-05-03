module com.example.clickergameattempt1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.clickergameattempt1 to javafx.fxml;
    exports com.example.clickergameattempt1;
}