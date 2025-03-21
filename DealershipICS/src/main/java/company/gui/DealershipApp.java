package company.gui;


import javafx.application.Application;
import javafx.stage.Stage;

public class DealershipApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
       SceneManager sceneManger = SceneManager.getInstance(primaryStage);

       // load initial scene
        sceneManger.loadScene("mainScene", "/MainScreen.fxml");

        GuiUtility.setScreenSize(primaryStage,0.8,0.8);

        // set the first scene
        sceneManger.switchScene("mainScene");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}