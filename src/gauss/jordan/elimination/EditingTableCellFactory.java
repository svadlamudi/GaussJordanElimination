package gauss.jordan.elimination;

// Imports
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * @author Sai Kiran Vadlamudi
 * 
 * Provides a custom TableCell factory.
 */
public class EditingTableCellFactory implements Callback<TableColumn<String[], String>, TableCell<String[], String>>{
	
        final KeyCombination kcTab = new KeyCodeCombination(KeyCode.TAB);
    
	@Override
	public TableCell<String[], String> call(TableColumn<String[], String> arg0) {
		EditingTableCell cell = new EditingTableCell();
		cell.setFocusTraversable(true);
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent t) {
                        if(t.getClickCount() == 2){
                            EditingTableCell c = (EditingTableCell)t.getSource();
                            if(!c.isEditing())
                                c.startEdit();
                        }
                    }                    
                });
                
                return cell;
	}	
}