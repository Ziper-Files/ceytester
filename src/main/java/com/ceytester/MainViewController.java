package com.ceytester;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the main view of the CeyTester application.
 * Handles keyboard events and updates the UI accordingly.
 */
public class MainViewController {

    @FXML private GridPane keyboardGrid;
    @FXML private TextArea eventLog;
    @FXML private Label keyCodeLabel;
    @FXML private Label keyCharLabel;
    @FXML private Label modifiersLabel;
    @FXML private Label keyLocationLabel;
    @FXML private VBox keysContainer;
    @FXML private HBox statusBar;
    @FXML private Button clearButton;
    @FXML private CheckBox showKeycodeCheckbox;
    @FXML private CheckBox logEventsCheckbox;
    
    private boolean keyboardViewEnabled = true;
    
    private Map<KeyCode, Rectangle> keyVisuals = new HashMap<>();
    private Map<KeyCode, Long> keyPressTime = new HashMap<>();
    
    /**
     * Initializes the controller.
     */
    public void initialize() {
        // Set up the keyboard visualization
        setupKeyboardVisualization();
        
        // Set up event handlers
        clearButton.setOnAction(event -> clearEventLog());
        
        // Default settings
        showKeycodeCheckbox.setSelected(true);
        logEventsCheckbox.setSelected(true);
        keyboardViewEnabled = true;
        updateKeyboardVisibility();
    }
    
    /**
     * Toggles the keyboard view visibility.
     */
    public void toggleKeyboardView() {
        keyboardViewEnabled = !keyboardViewEnabled;
        updateKeyboardVisibility();
    }
    
    /**
     * Handles the toggle keyboard menu item action.
     */
    @FXML
    public void handleToggleKeyboard() {
        toggleKeyboardView();
    }
    
    /**
     * Handles the about menu item action.
     */
    @FXML
    public void handleAbout() {
        // Create an alert dialog
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("About CeyTester");
        alert.setHeaderText("CeyTester - Keyboard Testing Utility");
        alert.setContentText("Version: 1.0.1-SNAPSHOT\n\nA simple utility for testing keyboard functionality.\n\nDeveloped with JavaFX.");
        alert.showAndWait();
    }
    
    /**
     * Updates the keyboard visibility based on the current setting.
     */
    private void updateKeyboardVisibility() {
        keyboardGrid.setVisible(keyboardViewEnabled);
        keyboardGrid.setManaged(keyboardViewEnabled);
    }
    
    /**
     * Handles key pressed events.
     * 
     * @param event the key event
     */
    public void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        String character = event.getText();
        String modifiers = getModifiersString(event);
        String location = getKeyLocationString(event.getCode());
        
        // Update UI elements
        keyCodeLabel.setText("Key Code: " + keyCode);
        keyCharLabel.setText("Character: " + (character.isEmpty() ? "[NONE]" : character));
        modifiersLabel.setText("Modifiers: " + (modifiers.isEmpty() ? "[NONE]" : modifiers));
        keyLocationLabel.setText("Location: " + location);
        
        // Log the event
        if (logEventsCheckbox.isSelected()) {
            String logEntry = "PRESSED: " + keyCode + 
                    (character.isEmpty() ? "" : " (" + character + ")") + 
                    (modifiers.isEmpty() ? "" : " [" + modifiers + "]") + 
                    " - Location: " + location;
            appendToEventLog(logEntry);
        }
        
        // Update visual representation
        highlightKey(keyCode, true);
        
        // Record press time for key response testing
        keyPressTime.put(keyCode, System.currentTimeMillis());
        
        // Consume the event to prevent it from being processed by other handlers
        event.consume();
    }
    
    /**
     * Handles key released events.
     * 
     * @param event the key event
     */
    public void handleKeyReleased(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        
        // Calculate response time if key was previously pressed
        if (keyPressTime.containsKey(keyCode)) {
            long pressTime = keyPressTime.get(keyCode);
            long releaseTime = System.currentTimeMillis();
            long responseTime = releaseTime - pressTime;
            
            if (logEventsCheckbox.isSelected()) {
                String logEntry = "RELEASED: " + keyCode + " - Response time: " + responseTime + "ms";
                appendToEventLog(logEntry);
            }
            
            keyPressTime.remove(keyCode);
        } else if (logEventsCheckbox.isSelected()) {
            String logEntry = "RELEASED: " + keyCode;
            appendToEventLog(logEntry);
        }
        
        // Update visual representation
        highlightKey(keyCode, false);
        
        // Consume the event to prevent it from being processed by other handlers
        event.consume();
    }
    
    /**
     * Sets up the keyboard visualization grid.
     */
    private void setupKeyboardVisualization() {
        // This would be implemented to create a visual representation of a keyboard
        // For now, we'll create a simple grid of key rectangles
        
        // Standard keyboard layout rows
        String[] row1 = {"ESC", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"};
        String[] row2 = {"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "BACK_SPACE"};
        String[] row3 = {"TAB", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"};
        String[] row4 = {"CAPS", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "ENTER"};
        String[] row5 = {"SHIFT", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/", "SHIFT"};
        String[] row6 = {"CONTROL", "WINDOWS", "ALT", "SPACE", "ALT", "CONTEXT_MENU", "CONTROL"};
        
        // Add rows to the keyboard grid
        addKeyRow(row1, 0);
        addKeyRow(row2, 1);
        addKeyRow(row3, 2);
        addKeyRow(row4, 3);
        addKeyRow(row5, 4);
        addKeyRow(row6, 5);
    }
    
    /**
     * Adds a row of keys to the keyboard visualization.
     * 
     * @param keys the array of key names
     * @param rowIndex the row index in the grid
     */
    private void addKeyRow(String[] keys, int rowIndex) {
        for (int i = 0; i < keys.length; i++) {
            String keyName = keys[i];
            KeyCode keyCode = getKeyCodeFromName(keyName);
            
            Rectangle keyRect = new Rectangle(40, 40);
            keyRect.setFill(Color.LIGHTGRAY);
            keyRect.setStroke(Color.BLACK);
            
            Label keyLabel = new Label(keyName);
            keyLabel.setStyle("-fx-font-size: 10px;");
            
            VBox keyBox = new VBox(keyRect, keyLabel);
            keyBox.setSpacing(2);
            
            keyboardGrid.add(keyBox, i, rowIndex);
            
            if (keyCode != null) {
                keyVisuals.put(keyCode, keyRect);
            }
        }
    }
    
    /**
     * Converts a key name to a KeyCode.
     * 
     * @param keyName the name of the key
     * @return the corresponding KeyCode, or null if not found
     */
    private KeyCode getKeyCodeFromName(String keyName) {
        try {
            // Handle special cases
            if (keyName.equals("WINDOWS")) return KeyCode.WINDOWS;
            if (keyName.equals("CONTEXT_MENU")) return KeyCode.CONTEXT_MENU;
            if (keyName.equals("CAPS")) return KeyCode.CAPS;
            if (keyName.equals("BACK_SPACE")) return KeyCode.BACK_SPACE;
            
            // For single characters, use the character itself
            if (keyName.length() == 1) {
                return KeyCode.getKeyCode(keyName);
            }
            
            // For named keys, use the enum constant
            return KeyCode.valueOf(keyName);
        } catch (IllegalArgumentException e) {
            // If the key name doesn't match any KeyCode
            return null;
        }
    }
    
    /**
     * Highlights or unhighlights a key in the visualization.
     * 
     * @param keyCode the key code to highlight
     * @param isPressed whether the key is pressed or released
     */
    private void highlightKey(KeyCode keyCode, boolean isPressed) {
        Rectangle keyRect = keyVisuals.get(keyCode);
        if (keyRect != null) {
            keyRect.setFill(isPressed ? Color.LIGHTGREEN : Color.LIGHTGRAY);
        }
    }
    
    /**
     * Gets a string representation of the modifiers in a key event.
     * 
     * @param event the key event
     * @return a string containing the active modifiers
     */
    private String getModifiersString(KeyEvent event) {
        StringBuilder modifiers = new StringBuilder();
        
        if (event.isShiftDown()) modifiers.append("SHIFT ");
        if (event.isControlDown()) modifiers.append("CTRL ");
        if (event.isAltDown()) modifiers.append("ALT ");
        if (event.isMetaDown()) modifiers.append("META ");
        
        return modifiers.toString().trim();
    }
    
    /**
     * Gets a string representation of the key location.
     * 
     * @param keyCode the key code
     * @return a string describing the key location
     */
    private String getKeyLocationString(KeyCode keyCode) {
        // This is a simplified version - in a real app, we would use KeyEvent.getCode().getName()
        // and additional logic to determine if it's a numpad key, left/right modifier, etc.
        
        if (keyCode == KeyCode.SHIFT) {
            return "LEFT/RIGHT SHIFT";
        } else if (keyCode == KeyCode.CONTROL) {
            return "LEFT/RIGHT CONTROL";
        } else if (keyCode == KeyCode.ALT) {
            return "LEFT/RIGHT ALT";
        } else if (keyCode.name().startsWith("NUMPAD")) {
            return "NUMPAD";
        } else {
            return "STANDARD";
        }
    }
    
    /**
     * Appends a message to the event log.
     * 
     * @param message the message to append
     */
    private void appendToEventLog(String message) {
        eventLog.appendText(message + "\n");
        // Auto-scroll to the bottom
        eventLog.setScrollTop(Double.MAX_VALUE);
    }
    
    /**
     * Clears the event log.
     */
    private void clearEventLog() {
        eventLog.clear();
    }
}