package edu.ncssm.chris.pset5;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Handles the interaction between graphical components
 * @author Brian Sea
 * @since PSet 3
 * @version 0.0.1
 */
public class WorkSpace extends BorderPane {

    private DrawingSpace activeSpace;

    /**
     * Graphical element which incorporates the other
     * graphical components
     */
    public WorkSpace(){

        Explorer explorer = new Explorer(this);
        explorer.prefWidthProperty().bind(this.widthProperty().multiply(.3f));

        setLeft(explorer);
    }

    /**
     * Changes the space in the center of the screen and updates the window title
     * @param s the space to switch to
     */
    public void setCenter(DrawingSpace s){
        super.setCenter(s);
        activeSpace = s;
        ((Stage)this.getScene().getWindow()).setTitle(s.getName());
    }

    public boolean handleKeyEvent(KeyEvent event ){
        if(activeSpace == null ){
            return false;
        }

        return activeSpace.handleKeyEvent(event);
    }

    public ObjectNode toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("type", this.getClass().getSimpleName());
        return root;
    }
}
