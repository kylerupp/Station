/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class Server extends Application {
    
    private TextArea log;
    private ScrollPane pane;
    
    public void start(Stage stage) {
        log = new TextArea();
        Scene scene = new Scene(new ScrollPane(log), 450, 200);
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> System.exit(0));
    }
    
    public static void launch(String[] args) {
        Application.launch(args);
    }

}
