package gauss.jordan.elimination;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Provides key combinations to be used with KeyEvent Handlers.
 * 
 * @author Sai Kiran Vadlamudi 
 */
public class KeyCombos {
    
    static final KeyCombination kcEnter = new KeyCodeCombination(KeyCode.ENTER);
    
    static final KeyCombination kcESC= new KeyCodeCombination(KeyCode.ESCAPE); 
    
    static final KeyCombination kcTab = new KeyCodeCombination(KeyCode.TAB);
    static final KeyCombination kcShiftTab = KeyCodeCombination.valueOf("SHIFT+TAB");    
    
    static final KeyCombination kcLeft = new KeyCodeCombination(KeyCode.LEFT);
    static final KeyCombination kcRight= new KeyCodeCombination(KeyCode.RIGHT);
    static final KeyCombination kcUp = new KeyCodeCombination(KeyCode.UP);
    static final KeyCombination kcDown = new KeyCodeCombination(KeyCode.DOWN);
    
}
