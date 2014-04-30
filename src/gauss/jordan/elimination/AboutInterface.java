package gauss.jordan.elimination;

// Imports
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Sai Kiran Vadlamudi
 * 
 * Provides a way for outside classes to interact with the About dialog
 * and with its FXML components.
 */
public class AboutInterface extends Application{

    @Override
    public void start(Stage stage) throws Exception {}
    
    /**
     * Calling this method will create and show the About dialog.
     */
    public void initializePopup(){
        
        final Stage stage = new Stage();
       
        stage.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent t) {
                if(KeyCombos.kcESC.match(t)){
                    stage.close();
                }
            }            
        });
        
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("AboutModel.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(AboutInterface.class.getName()).log(Level.SEVERE, "AboutModel.fxml not found", ex);
        }
       
        Scene scene = new Scene(root);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.setWidth(620);
        stage.setHeight(420);
        stage.show();
    }    
    
}
