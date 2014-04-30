package gauss.jordan.elimination;

// Imports
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sai Kiran Vadlamudi
 * 
 * FXML Controller for the Close warning dialog.
 */
public class CloseController implements Initializable{

    @FXML private Button proceed;
    @FXML private Button cancel;
    private static Stage closeStage;
    private static Parent closeModel;
    private static Scene closeScene;
    private static Stage mainStage;
    
    /**
     * Initialize and show the Close Dialog.
     * 
     * @param closeModelIn
     * @param mainStageIn 
     */
    public static void initializeCloseDialog(Parent closeModelIn, Stage mainStageIn){
        closeStage = null;
        closeModel = closeModelIn;
        closeScene = null;
        mainStage = mainStageIn;
        initializeComponents();
    }
    
    /**
     * Add EventHandlers to Buttons once the dialog is initialized.
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // EventHandler for "Proceed" button.
        proceed.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                proceedExit();
            }
        });
        
        // EventHandler for "Cancel" button.
        cancel.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                cancelExit();
            }            
        });
    }    
    
    /**
     * Create and Show the Close Dialog.
     */
    private static void initializeComponents() {
        
        // Create Scene
        closeScene = new Scene(closeModel);        
        
        // Create Stage and Set Scene
        closeStage = new Stage();
        closeStage.setScene(closeScene);
        
        // Set Stage Properties
        closeStage.setTitle("Warning!");        
        closeStage.initModality(Modality.APPLICATION_MODAL);
        closeStage.initStyle(StageStyle.UTILITY);
        closeStage.sizeToScene();
        closeStage.setResizable(false);
        closeStage.centerOnScreen();
        
        // Show Stage and Request Focus
        closeStage.show();
        closeStage.requestFocus();
        
    }
    
    /**
     * Close the dialog and proceed exiting of the Parent Stage.
     */
    private void proceedExit(){
        mainStage.close();
        closeStage.close();
    }
    
    /**
     * Close the dialog and cancel the exiting of the Parent Stage.
     */
    private void cancelExit(){
        closeStage.close();
    }
    
}
