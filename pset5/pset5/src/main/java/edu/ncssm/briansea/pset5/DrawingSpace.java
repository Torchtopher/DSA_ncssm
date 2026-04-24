package edu.ncssm.briansea.pset5;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * An area to allow drawing and use of tools
 * @author Brian Sea
 * @version 0.0.1
 * @since PSet 2
 */
public class DrawingSpace extends Pane {

    private String name;
    private final List<Drawlet> allDrawlets;
    private final List<Drawlet> selectedDrawlets;
    private final Tool activeTool;

    /**
     * Create a blank drawing space
     */
    public DrawingSpace(){
        name = "Untitled";
        allDrawlets = new ArrayList<>();
        selectedDrawlets = new ArrayList<>();

        // For now, the line tool is our only tool
        // TODO: Add a method of selecting between multiple tools
        activeTool = new LineTool();

        // Handle all the mouse events
        // TODO: We need to add handling of keyboard events
        MouseHandler mh = new MouseHandler();
        this.addEventFilter(MouseEvent.ANY, mh);

        // Setup and update out Clipping Region to stop
        // shapes escaping
        Rectangle rect = new Rectangle();
        rect.setX(0);
        rect.setY(0);

        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rect.setWidth(newValue.doubleValue());
                setClip(rect);
            }
        });

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rect.setHeight(newValue.doubleValue());
                setClip(rect);
            }
        });
    }

    public String getName(){
        return name;
    }

    public boolean setName( String n ){
        boolean rtn = false;
        if( n != null && !n.isEmpty()){
            name = n;
            rtn = true;
        }
        return rtn;
    }

    public boolean handleKeyEvent(KeyEvent event){
        if( selectedDrawlets.size() == 0){
            return false;
        }

        boolean rtn = true;
        if( event.getCode() == KeyCode.ESCAPE){
            for( Drawlet d : selectedDrawlets ){
                d.select(false);
            }
            selectedDrawlets.clear();
        }
        else if( event.getCode() == KeyCode.DELETE ||
                event.getCode() == KeyCode.BACK_SPACE ){
            for( Drawlet d : selectedDrawlets ){
                d.select(false);
                this.getChildren().remove(d);
                allDrawlets.remove(d);
            }
            selectedDrawlets.clear();
        }
        else {
            rtn = false;
            for( Drawlet d : selectedDrawlets ){
                rtn = rtn || (d.handleKeyEvent(event) != null );
            }
        }
        return rtn;
    }

    public ObjectNode toJSON(){
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root = mapper.createObjectNode();
        root.put("type", this.getClass().getSimpleName());
        root.put("name", getName());

        ArrayNode drawlets = root.putArray("drawlets");
        for( Drawlet d : this.allDrawlets ){
            drawlets.add(d.toJSON());
        }
        return root;
    }

    public DrawingSpace fromJSON(JsonNode tree) {
        name = tree.get("name").textValue();
        for(JsonNode let: tree.get("drawlets")){
            try {
                String className = let.get("type").textValue();
                if(!className.contains(".")){
                    className = this.getClass().getPackageName() + "." + className;
                }

                Class<?> c1 = Class.forName(className);
                Drawlet dl = (Drawlet) c1.getConstructor(null).newInstance();
                dl = dl.fromJSON(let);
                allDrawlets.add(dl);
                getChildren().add(dl);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }


    private class MouseHandler implements EventHandler<MouseEvent>{
        public void handle(MouseEvent e ){
            // Send the event to any selected Drawlets
            if( selectedDrawlets.size() > 0 ){
                for( Drawlet d : selectedDrawlets){
                    d.handleMouseEvent(e);
                }

                // Nothing consumed the event so the click is a deselection
                if(!e.isConsumed() && e.getEventType() == MouseEvent.MOUSE_RELEASED){
                    for( Drawlet d : selectedDrawlets) {
                        d.select(false);
                    }
                    selectedDrawlets.clear();
                }
            }
            else {

                // Check to see if a shape was selected
                // We only send the event to elements "under" the pointer
                // TODO: Right now, the first to respond wins... change it?
                if( e.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    for (Drawlet d : allDrawlets) {
                        if (d.getBoundsInParent().contains(e.getX(), e.getY())) {
                           d.handleMouseEvent(e);
                           if( e.isConsumed()){
                               selectedDrawlets.add(d);
                               d.select(true);
                               break;
                           }
                        }
                    }
                }

                // Not selecting a edu.ncssm.edu.briansea.pset5.Drawlet... use our active tool!
                if( selectedDrawlets.size() == 0 ) {
                    Drawlet d = activeTool.handleMouseEvent(e);
                    if (d != null) {
                        DrawingSpace.this.getChildren().add(d);
                        d.select(true);
                        selectedDrawlets.add(d);
                        allDrawlets.add(d);
                    }
                }
            }
        }
    }
}
