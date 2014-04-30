package gauss.jordan.elimination;

// Imports
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Sai Kiran Vadlamudi
 */
public class InvalidController implements Initializable {

    @FXML private AnchorPane mainPane;
    private static Stage invalidStage = new Stage();
    private static Scene invalidScene = null;    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPane.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent t) {
                if(KeyCombos.kcESC.match(t)){
                    invalidStage.close();
                }
            }            
        });
    }    
    
    /**
     * Construct and Display a warning dialog about Invalid entries in the Matrix.
     * 
     * @param root 
     */
    public static void initializeComponents(Parent root) {
        
        invalidScene = new Scene(root);
        
        invalidStage.setScene(invalidScene);
        
        invalidStage.setTitle("Error!");
        invalidStage.initModality(Modality.APPLICATION_MODAL);
        invalidStage.initStyle(StageStyle.UTILITY);
        invalidStage.sizeToScene();
        invalidStage.setResizable(false);
        
        invalidStage.show();
        invalidStage.requestFocus();
    }
    
}
