package gauss.jordan.elimination;

// Imports
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 *@author Sai Kiran Vadlamudi
 * 
 * Provides a way for outside classes to interact with the CloseController.
 */
public class CloseInterface {
    
    /**
     * Constructs and Displays a Close dialog to warn Users.
     * 
     * @param parentStage 
     */
    public CloseInterface(Stage parentStage){
        
        // Declare Parent to store FXML Model
        Parent closeModel = null;
        
        // Set Parent to FXML Model
        try {
            closeModel = FXMLLoader.load(getClass().getResource("CloseModel.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Show Close Dialog
        CloseController.initializeCloseDialog(closeModel, parentStage);
        
    }
    
}
