module com.joshuawgucapstone.questionanswerspace {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.joshuawgucapstone.questionanswerspace to javafx.fxml;
    exports com.joshuawgucapstone.questionanswerspace;
}