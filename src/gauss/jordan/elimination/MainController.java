package gauss.jordan.elimination;

// Imports
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * FXML Controller for the main application window.
 * 
 * @author Sai Kiran Vadlamudi
 */
public class MainController implements Initializable {
    
    private final static String pattern = "^([1-9]|[1-9][0-9]|[1-9][0-9][0-9])$";    
	
    private final Image errorRow = new Image(getClass().getResourceAsStream("/resources/images/errorRow.jpg"));
    private final ImageView errorRowView = new ImageView(errorRow);
    
    private final Image errorColumn = new Image(getClass().getResourceAsStream("/resources/images/errorColumn.jpg"));
    private final ImageView errorColumnView = new ImageView(errorColumn);
    
    private final Image acceptRow = new Image(getClass().getResourceAsStream("/resources/images/acceptRow.jpg"));
    private final ImageView acceptRowView = new ImageView(acceptRow);
    
    private final Image acceptColumn = new Image(getClass().getResourceAsStream("/resources/images/acceptColumn.jpg"));
    private final ImageView acceptColumnView = new ImageView(acceptColumn);
    
    public static File matrixInput;
    private static File prevFile = null;
    
    @FXML private Button readFile;
    @FXML private MenuBar mainMenu;
    @FXML private Menu file;
    @FXML private MenuItem closeButton;
    @FXML private Menu view;
    @FXML private Menu help;
    @FXML private static TextField rowNum;
    @FXML private Label rowError;
    @FXML private static TextField columnNum;
    @FXML private Label columnError;
    @FXML private MenuItem aboutButton;
    @FXML private Button createMatrix;
    @FXML public static TextArea resultsField;
    public ToggleGroup outputToggle;
    @FXML public RadioMenuItem simple;
    @FXML public RadioMenuItem advanced;
            
    /**
     * Add EventHandlers after the window components are initialized.
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    	if(rowNum != null && columnNum != null){
            
            // Add KeyEvent Handler to provide increment and decrement in the row field using the arrow keys.
            rowNum.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){

                @Override
                public void handle(KeyEvent k) {
                    // Handler for 'UP' Key to Increment.
                    if(KeyCombos.kcUp.match(k)){
                        try{
                            if(Integer.parseInt(rowNum.getText()) <= 998){
                                rowNum.positionCaret(rowNum.getText().length());
                                rowNum.setText(Integer.toString((Integer.parseInt(rowNum.getText())+1)));
                                valueValidation();
                            }
                        }catch(NumberFormatException e){
                            rowNum.positionCaret(rowNum.getText().length());
                            rowNum.setText("3");
                        }
                    }
                    // Handler for 'DOWN' Key to Decrement.
                    else if(KeyCombos.kcDown.match(k)){
                        try{
                            if(Integer.parseInt(rowNum.getText()) >= 2){
                                rowNum.positionCaret(rowNum.getText().length());
                                rowNum.setText(Integer.toString((Integer.parseInt(rowNum.getText())-1)));
                                valueValidation();
                            }
                        }catch(NumberFormatException e){
                            rowNum.positionCaret(rowNum.getText().length());
                            rowNum.setText("3");
                        }
                    }
                    // Handler to commit and move to next field.
                    else if(KeyCombos.kcEnter.match(k)){
                        columnNum.requestFocus();
                    }
                }

            });
            
            // Add KeyEvent Handler to provide increment and decrement in the column field using the arrows
            columnNum.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){

                @Override
                public void handle(KeyEvent k) {
                    // Handler for 'UP' Key to Increment.
                    if(KeyCombos.kcUp.match(k)){
                        try{
                            if(Integer.parseInt(columnNum.getText()) <= 998){
                                columnNum.positionCaret(columnNum.getText().length());
                                columnNum.setText(Integer.toString((Integer.parseInt(columnNum.getText())+1)));
                                valueValidation();
                            }
                        }catch(NumberFormatException e){
                            columnNum.positionCaret(columnNum.getText().length());
                            columnNum.setText("3");
                        }
                    }
                    // Handler for 'DOWN' Key to Decrement.
                    else if(KeyCombos.kcDown.match(k)){
                        try{
                            if(Integer.parseInt(columnNum.getText()) >= 2){
                                columnNum.positionCaret(columnNum.getText().length());
                                columnNum.setText(Integer.toString((Integer.parseInt(columnNum.getText())-1)));
                                valueValidation();
                            }
                        }catch(NumberFormatException e){
                            columnNum.positionCaret(columnNum.getText().length());    
                            columnNum.setText("3");
                        }
                    }
                }

            });
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    rowNum.requestFocus();
                }
            });
            
        }
        
    }    
    
    /**
     * Getter for rowNum TextField.
     * 
     * @return 
     */
    public int getRowNum(){
        return Integer.parseInt(rowNum.getText());
    }
    
    /**
     * Getter for columnNum TextField.
     * 
     * @return 
     */
    public int getColumnNum(){
        return Integer.parseInt(columnNum.getText());
    }
    
    @FXML
    /**
     * Show the About Dialog.
     */
    private void aboutClicked() throws Exception{
        new AboutInterface().initializePopup();
    }
    
    @FXML
    /**
     * Clear this resultsField.
     */
    private void clearFieldClicked(){
        resultsField.setText("");
    }
    
    @FXML
    /**
     * Show the matrix window for User Input.
     */
    private void createMatrixClicked(){        
    	try{
            new MatrixInterface().initializeMatrixTable(Integer.parseInt(rowNum.getText()), Integer.parseInt(columnNum.getText()));
    	}catch(NumberFormatException e){
            new MatrixInterface().initializeMatrixTable(2, 2);
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Invalid Matrix Dimensions!", e);
    	}
    }
    
    @FXML
    /**
     * Validate the number of rows and columns entered by User.
     */
    private void valueValidation(){
    	if(rowNum.getText().matches(pattern) == true && columnNum.getText().matches(pattern) == true){
			rowError.setGraphic(acceptRowView);
			columnError.setGraphic(acceptColumnView);
			createMatrix.setDisable(false);
		}
		else if(rowNum.getText().matches(pattern) == true && columnNum.getText().matches(pattern) == false){
			rowError.setGraphic(acceptRowView);
			columnError.setGraphic(errorColumnView);
			createMatrix.setDisable(true);
		}
		else if(rowNum.getText().matches(pattern) == false && columnNum.getText().matches(pattern) == true){
			rowError.setGraphic(errorRowView);
			columnError.setGraphic(acceptColumnView);
			createMatrix.setDisable(true);
		}
		else if(rowNum.getText().matches(pattern) == false && columnNum.getText().matches(pattern) == false){
			rowError.setGraphic(errorRowView);
			columnError.setGraphic(errorColumnView);
			createMatrix.setDisable(true);
		}
    	else{
    		rowError.setGraphic(null);
    		columnError.setGraphic(null);
    		createMatrix.setDisable(true);
    	}    		
    }
    
    @FXML
    /**
     * Close the application.
     */
    private void closeClicked(){
    	MainInterface.close();
    }
    
    @FXML
    /**
     * Open file chooser and start algorithm for file reading.
     */
    private void readFileClicked(){
    	FileChooser fileChooser = new FileChooser();
        File matrixFile = null;
    	fileChooser.setTitle("Open Matrix Input File");
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    	Window stage = null;
        
        if(prevFile == null){
            matrixFile = fileChooser.showOpenDialog(stage);
            prevFile = matrixFile.getParentFile();
        }
        else{
            fileChooser.setInitialDirectory(prevFile);
            matrixFile = fileChooser.showOpenDialog(stage);
            prevFile = matrixFile.getParentFile();            
        }       
        
        try {
            ReadInMatrix.readAndCalculate(matrixFile.getCanonicalPath());
        } catch (IOException e) {			
            Logger.getLogger(MainController.class.getName(), null).log(Level.SEVERE, "No File Selected", e.toString());
        } catch (NullPointerException n){
            Logger.getLogger(MainController.class.getName(), null).log(Level.SEVERE, "Null File Pointer", n.toString());
        }	
    }
}
