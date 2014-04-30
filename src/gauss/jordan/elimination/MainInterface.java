package gauss.jordan.elimination;

// Imports
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Acts as main(), Launches and maintains the application.
 * 
 * @author Sai Kiran Vadlamudi
 */
public class MainInterface extends Application {
    
    private static Stage mainStage;
	
    public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    /**
     * Construct and display the application window.
     */
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        stage.getIcons().add(new Image("/resources/images/icon.png"));
    	
    	Parent root = FXMLLoader.load(getClass().getResource("MainModel.fxml"));
        
        Scene scene = new Scene(root);        
        
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent t) {
                if(KeyCombos.kcESC.match(t)){
                    CloseInterface close = new CloseInterface(mainStage);
                }
            }            
        });
        
        mainStage.setTitle("Gauss-Jordan Elimination");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.centerOnScreen();
        mainStage.show();
        mainStage.requestFocus();        
    } 
    
    /**
     * Close the application window.
     */
    public static void close(){
    	mainStage.close();
    }
}