package gauss.jordan.elimination;

// Imports
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Provides a way for outside classes to interact with the InvalidController.
 * 
 * @author Sai Kiran Vadlamudi
 */
public class InvalidInterface {
    
    public InvalidInterface(){
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("InvalidModel.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(InvalidController.class.getName()).log(Level.SEVERE, "InvalidModel.fxml file not found!", ex);
        }
        
        InvalidController.initializeComponents(root);
    }
    
}
