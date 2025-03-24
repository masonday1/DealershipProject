package company.gui;


import javafiles.Key;
import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.domainfiles.Company;
import javafiles.domainfiles.Dealership;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import static company.gui.FXMLPath.*;

public class DealershipApp extends Application {

    @Override
    public void stop() {
        AppStateManager.writeToInventory();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // create and intialize company instance
        Company company = new Company();

        // just for testing purposes **************
        Dealership d1 = new Dealership("123","ICS372");
        d1.manualVehicleAdd("abc","Toyota","Prius",50000L,1742696542L,"Sedan");
        d1.manualVehicleAdd("cde","Toyota","Prius",50000L,1742696542L,"Sedan");
        d1.manualVehicleAdd("efg","Toyota","Prius",50000L,1742696542L,"Sedan");

        // test *******************
        AppStateManager.initializeCompany(company);

        FileIOBuilder.setupFileIOBuilders();
        System.out.println("initial launch");
        List<Map<Key, Object>> badDataMaps = AppStateManager.loadInitialFiles(); // TODO: decide what to do with bad maps
        ProfileManagementController.setCompany(company);

        AppStateManager.addADealership(d1);



        SceneManager sceneManger = SceneManager.getInstance(primaryStage);
        sceneManger.switchScene(MAIN_SCREEN);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}