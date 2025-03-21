package company.gui;


import javafx.application.Application;
import javafx.stage.Stage;

import static company.gui.FXMLPaths.*;

public class DealershipApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
       SceneManager sceneManger = SceneManager.getInstance(primaryStage);
       sceneManger.switchScene(MAIN_SCREEN);
       primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}