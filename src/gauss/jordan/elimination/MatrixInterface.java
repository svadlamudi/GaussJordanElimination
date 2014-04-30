package gauss.jordan.elimination;

// Imports
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * Provides a way for outside classes to interact with the MatrixController.
 * 
 * @author Sai Kiran Vadlamudi
 */
public class MatrixInterface extends Application {
    
    private static Stage stage;
    private static TableView<String[]> matrixTable;
    private static String[][] matrixArray;
    private static StringBuilder result;
    public static int rows;
    public static int columns;
            
    @Override
    public void start(Stage stage) throws Exception {}
    
    /**
     * Create the TableView and underlying data model and add it to
     * the MatrixModel and show the stage.
     *
     * @param rowNum
     * @param columnNum
     */
    public void initializeMatrixTable(int rowNum, int columnNum) {
        
        try{
            // Initialize variables
            stage = new Stage();
            result = new StringBuilder();	    	
            rows = rowNum;
            columns = columnNum;	    	
            Parent root = FXMLLoader.load(getClass().getResource("MatrixModel.fxml"));
            matrixArray = new String[rows+1][columnNum];
            matrixTable = new TableView<>();
            initializeArray();
            initializeTable();

            // Create the underlying data model for the TableView
            ObservableList<String[]> matrix = FXCollections.observableArrayList(Arrays.asList(matrixArray));
            matrix.remove(0);

            // Add the data model to the TableView
            matrixTable.setItems(matrix);
            matrixTable.setEditable(true);
            matrixTable.getSelectionModel().setCellSelectionEnabled(true);
            matrixTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            
            // Aesthetic improvement and scrolling fix
            if(columns <= 4)
                matrixTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            else
                matrixTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);	        
            
            // Draw the TableView on the matrix window.
            MatrixController.setTableView(matrixTable);            

            // Set-up and Display Stage
            Scene scene = new Scene(root);
            
            // Add KeyEvent Handler to close the matrix window when 'ESC' Key is pressed.
            scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent t) {
                    if(KeyCombos.kcESC.match(t)){
                        CloseInterface close = new CloseInterface(stage);
                    }
                }                
            });
            
            // Set window properties
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setHeight(300);
            stage.setWidth(567);
            stage.setResizable(false);
            stage.setTitle("Matrix Input");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
        catch(IOException e){
            Logger.getLogger(MatrixInterface.class.getName()).log(Level.SEVERE, "Unable to open MatrixController.fxml", e);
        }
    }
    
    /**
     * 
     * @author Sai Kiran Vadlamudi
     *
     * Initialize the array with the first row as
     * column indexes and the rest as 0s.
     *
     */
    private static void initializeArray(){
    	// Column Indexes
        for(int i = 0; i < columns; i++)
            matrixArray[0][i] = String.format("%d", i+1);

        // Initialize Cells as 0s
        for(int i = 1; i < rows+1;i++){
            for(int j = 0; j < columns; j++){
                matrixArray[i][j] = String.format("%d", 0);
            }
        }
    }
    
    /**
     * 
     * @author Sai Kiran Vadlamudi
     *
     * Create the columns of the table and add the 
     * event handlers for each column.
     *
     */
    private void initializeTable() {
        // Create the columns and their handlers		
        for(int i = 0;  i < matrixArray[0].length; i++){
            // Create the Column
            TableColumn<String[], String> matrixColumn = new TableColumn<>(matrixArray[0][i]);
            final int colNo = i;

            // Handlers
            matrixColumn.setCellFactory(new EditingTableCellFactory());        	
            matrixColumn.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty(p.getValue()[colNo]);
                }                
            });

            matrixColumn.setOnEditCommit(new EventHandler<CellEditEvent<String[], String>>() {
                @Override
                public void handle(CellEditEvent<String[], String> t) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow())[t.getTablePosition().getColumn()] = t.getNewValue();
                }
            });

            // Column Properties
            matrixColumn.setPrefWidth(90);
            matrixTable.getColumns().add(matrixColumn);
        }
    }
	
    /**
     * 
     * @author Sai Kiran Vadlamudi
     *
     * Append the given string to the existing string in the resultsField.
     *
     * @param newResult
     */
    public static void appendResult(String newResult){
    	result.append(newResult);
    	MainController.resultsField.setText(result.toString());
    }
    
    /**
     * 
     * @author Sai Kiran Vadlamudi
     *
     * Calculate the solution to the given matrix if there is one.
     *
     * @param inputTable
     */
    public static void calculateMatrix(TableView<String[]> inputTable){
    	stage.close();
    	matrixTable = inputTable;
        Algorithm.startCalculate(matrixTable, rows, columns);
    }
}
