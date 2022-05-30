package map;

import com.sothawo.mapjfx.Projection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Main map class
 */
public class RouteSeeker extends Application implements Runnable{
    public void run() {
        launch();
    }

    /**
     * Overriden <code>start</code> method that gets the <code>FXML</code> for the project and creates
     * the scene based on it
     * @param primaryStage app stage
     * @throws IOException thrown in the event that the <code>FXML</code> config file is not found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent rootNode = fxmlLoader.load(new FileInputStream("src/main/resources/fxml/DemoApp.fxml"));

        final Controller controller = fxmlLoader.getController();
        final Projection projection = getParameters().getUnnamed().contains("wgs84")
                ? Projection.WGS_84 : Projection.WEB_MERCATOR;
        controller.initMapAndControls(projection);

        Scene scene = new Scene(rootNode);

        primaryStage.setTitle("Route Seeker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}