package gauss.jordan.elimination;

// Imports
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller for the matrix window.
 * 
 * @author Sai Kiran Vadlamudi
 */
public class MatrixController implements Initializable{
    
    private static TableView<String[]> matrixTable;
    @FXML private static AnchorPane mainPane;
    @FXML private static SplitPane mainSplit;
    @FXML private static StackPane matrixPane;
    @FXML private static ScrollPane matrixScrollPane;
    @FXML private static Button calculate;
        
    @Override
    /**
     * Request focus for first TableCell in the matrix window.
     */
    public void initialize(URL arg0, ResourceBundle arg1) {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
                matrixTable.edit(0, matrixTable.getColumns().get(0));
            }
        });        
    }
    
    /**
     * Getter for this matrixTable.
     * 
     * @return 
     */
    public static TableView<String[]> getTableView(){
        return matrixTable;
    }
    
    /**
     * Setter for this matrixTable.
     * 
     * @param newTable 
     */
    public static void setTableView(TableView<String[]> newTable){
    	matrixTable = newTable;
    	matrixTable.getStylesheets().add("/resources/CSS/Matrix.css");
        matrixTable.requestFocus();
    	matrixPane.getChildren().add(matrixTable);
    	matrixPane.setPrefSize(matrixTable.getPrefWidth(), matrixTable.getPrefHeight());
    	mainSplit.autosize();
    	mainPane.autosize();
    }

    @FXML
    /**
     * Run given matrix through algorithm.
     */
    public void calculateClicked(){
    	MatrixInterface.calculateMatrix(matrixTable);
    }
}
