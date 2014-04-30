package gauss.jordan.elimination;

// Imports
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author Sai Kiran Vadlamudi
 * 
 * Handles the editing, committing, canceling, and storing TableCell and its value.
 */
public class EditingTableCell extends TableCell<String[], String> {

    private TextField textField;
    private String prevValue;
    private final static String patternChars = "[0-9]";
    
    /**
     * Sets the underlying TableCell.
     */
    public EditingTableCell() {
    	textField = new TextField();
        prevValue = "0";
    }
    
    /**
     * Transition TableCell from Non-Editing to Editing state.
     */
    @Override
    public void startEdit() {
        
        // Check to see cell is not Disabled
        if (!isEmpty()){
            // Set TableCell state to Editing
            super.startEdit();
            
            // Create TextField to record User Input
            createTextField();
            
            // Store text incase of Edit Cancels
            prevValue = textField.getText();
            
            // Set TextField Text
            if(textField.getText().equals("0")||textField.getText().equals("")){
                textField.setText("");            
            }
            
            // Focus on TextField to begin Editing
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    textField.requestFocus(); 
                    textField.selectAll();
                }
            });
            
            // Set TableCell Graphic to TextField
            setGraphic(textField);                   
        }       
    }

    /**
     * Transition TableCell from Editing to Non-Editing state.
     */
    @Override
    public void cancelEdit() {
        
        // Set TableCell to Non-Editing state
        super.cancelEdit();
        
        // Set TextField to text before Edit start
        if("".equals(getString())){
            setText(prevValue);
            setGraphic(null);
        }
        else{
            setText((String) getItem());
            setGraphic(null);
        }
        
    }

    /**
     * Update the TableCell and the underlying data model.
     * 
     * @param item
     * @param empty 
     */
    @Override
    public void updateItem(String item, boolean empty) {
        
        // Update the ObservableList<String[]> with the new value and call setOnEditCommit
        super.updateItem(item, empty);
        
        // If TableCell is Disabled
        if (empty) {
            setText(null);
            setGraphic(null);
        }
        // Update TableCell Graphic
        else {
            if (isEditing()) {
                if (textField != null) {
                    try{
                        Double.parseDouble(getString());
                        setText(getString());
                    }
                    catch(NumberFormatException e){
                        // Do Nothing here but indicate error using border later
                    }
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
        
    }
    
    /**
     * Create and add EventHandlers to new TextField with previous value.
     */
    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        
        // Add Focus listener to update when focus lost. Mostly when User uses Mouse
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if(!textField.getText().equals(""))
                        commitEdit(textField.getText());
                    else
                        commitEdit("0");
                }
            }
        });
        
        // Add a KeyEvent Handler to update and move to next cell on right when user hits 'ENTER' || 'TAB' || 'RIGHT'(Right arrow key)
        textField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent t) {
                if(KeyCombos.kcTab.match(t) || KeyCombos.kcEnter.match(t) || KeyCombos.kcRight.match(t)){
                    EditingTableCell currentEditingCell = EditingTableCell.this;
                    int currentCellRow = currentEditingCell.getTableRow().getTableView().getEditingCell().getRow();
                    int currentCellColumn = currentEditingCell.getTableRow().getTableView().getEditingCell().getColumn();
                    TableView currentTableView = currentEditingCell.getTableView();
                    
                    commitCurrent();
                    
                    if(currentCellColumn != MatrixInterface.columns-1)
                        currentTableView.edit(currentCellRow, (TableColumn) currentTableView.getColumns().get(currentCellColumn+1));
                    else if(currentCellRow == MatrixInterface.rows-1 && currentCellColumn == MatrixInterface.columns-1 && !KeyCombos.kcEnter.match(t))
                        currentTableView.edit(0, (TableColumn) currentTableView.getColumns().get(0));
                    else if((currentCellRow != MatrixInterface.rows-1 || currentCellColumn != MatrixInterface.columns-1) || !KeyCombos.kcEnter.match(t))
                        currentTableView.edit(currentCellRow+1, (TableColumn) currentTableView.getColumns().get(0));
                    else
                        MatrixInterface.calculateMatrix(currentTableView);
                }
            }
            
        });        
        
        // Add a KeyEvent Handler to update and move to previous cell when User hits 'SHIFT+TAB' || 'LEFT'(Left Arrow Key)
        textField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent t) {
                if(KeyCombos.kcShiftTab.match(t) || KeyCombos.kcLeft.match(t)){
                    EditingTableCell currentTableCell = EditingTableCell.this;
                    int currentCellRow = currentTableCell.getTableRow().getTableView().getEditingCell().getRow();
                    int currentCellColumn = currentTableCell.getTableRow().getTableView().getEditingCell().getColumn();
                    TableView currentTableView = currentTableCell.getTableView();
                    
                    commitCurrent();
                    
                    if(currentCellColumn != 0)
                        currentTableView.edit(currentCellRow, (TableColumn) currentTableView.getColumns().get(currentCellColumn-1));
                    else if(currentCellRow == 0 && currentCellColumn == 0)
                        currentTableView.edit(MatrixInterface.rows-1, (TableColumn) currentTableView.getColumns().get(MatrixInterface.columns-1));
                    else
                        currentTableView.edit(currentCellRow-1, (TableColumn) currentTableView.getColumns().get(MatrixInterface.columns-1));
                }
            }
            
        });
        
        // Add a KeyEvent Handler to update and move to top or bottom cell when user hits 'UP' || 'DOWN' respectively.
        textField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent userKey) {
                if(KeyCombos.kcUp.match(userKey)|| KeyCombos.kcDown.match(userKey)){
                    EditingTableCell currentTableCell = EditingTableCell.this;
                    int currentCellRow = currentTableCell.getTableRow().getTableView().getEditingCell().getRow();
                    int currentCellColumn = currentTableCell.getTableRow().getTableView().getEditingCell().getColumn();
                    TableView currentTableView = currentTableCell.getTableView();
                    
                    commitCurrent();
                    
                    if(KeyCombos.kcUp.match(userKey)){
                        if(currentCellRow == 0)
                            currentTableView.edit(0, (TableColumn) currentTableView.getColumns().get(currentCellColumn));
                        else 
                            currentTableView.edit(currentCellRow-1, (TableColumn) currentTableView.getColumns().get(currentCellColumn));
                    }
                    else if(KeyCombos.kcDown.match(userKey)){
                        if(currentCellRow == MatrixInterface.rows-1)
                            currentTableView.edit(MatrixInterface.rows-1, (TableColumn) currentTableView.getColumns().get(currentCellColumn));
                        else
                            currentTableView.edit(currentCellRow+1, (TableColumn) currentTableView.getColumns().get(currentCellColumn));
                    }
                }
            }
        });
        
    }
    
    /**
     * Used by EventHandlers to commit and validate the new value.
     * 
     * @param tableCell
     * @param row
     * @param column 
     */
    private void commitCurrent(){        
        if(textField.getText().equals(""))
            commitEdit(prevValue);
        else if(!textField.getText().matches(patternChars)){
            EditingTableCell.this.setStyle("-fx-background-color: white; -fx-border-color: orangered; -fx-border-width: 3;");
            //InvalidInterface invalid = new InvalidInterface();
            commitEdit(textField.getText());
        }
        else{
            EditingTableCell.this.setStyle("-fx-background-color: white");
            commitEdit(textField.getText());                        
        }
    }
    
    /**
     * Returns and validates the current TextField value.
     * 
     * @return String
     */
    private String getString() {
    	if(getItem() == null)
            return "";
    	else if(getItem().matches("^.*[a-zA-Z].*$")){
            this.setStyle("-fx-background-color: white; -fx-border-color: orangered; -fx-border-width: 3;");
            return getItem();
    	}
    	else{
            this.setStyle("-fx-background-color: white");
            return getItem();
    	}
    }
    
}
