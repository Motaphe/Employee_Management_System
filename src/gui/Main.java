package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/EmployeeGUI.fxml"));
        
        if (loader.getLocation() == null) {
            System.err.println("FXML file not found!");
            System.exit(1);
        }
        
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
