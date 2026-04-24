package edu.ncssm.briansea.pset5;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * A graphical line element
 * @author Brian Sea
 * @version 0.0.1
 * @since PSet 2
 */
public class LineDrawlet extends Drawlet {
    // The points in the line
    // TODO: Refactor this if the line gets more complicated
    private List<Point2D> points;

    /**
     * Graphical elements of the line
     * canvas -- the custom drawing surface
     * selectionBorder -- the outline displayed when we are selected
     * selectedPoint -- the current point being moved
     * currentPress -- location of the mouse press (for moving the shape)
     * isBuilt -- whether the line has been constructed
     */
    private static final int POINT_SIZE = 15;
    private static final int KEY_MOVE_PX = 10;

    private final Canvas canvas;
    private final Border selectionBorder;
    private int selectedPoint;
    private Point2D currentPress;
    private boolean isBuilt;


    public LineDrawlet() {
        this(null);
    }

    /**
     * Create the graphical line element; All lines have at least one point
     * @param startingPoint the first point of the line (null means an empty line)
     */
    public LineDrawlet(Point2D startingPoint){

        isBuilt = false;
        selectedPoint = -1;

        points = new LList<>();
        if( startingPoint != null ) {
            points.add(startingPoint);
        }

        // The canvas shrinks and expands based on its parent
        canvas = new Canvas();
        canvas.widthProperty().bind(this.prefWidthProperty());
        canvas.heightProperty().bind(this.prefHeightProperty());

        // Set up the selection border for faster selection
        this.selectionBorder = new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        this.setPrefSize(POINT_SIZE, POINT_SIZE);

        this.getChildren().add(canvas);
    }

    @Override
    protected void layoutChildren(){
        super.layoutChildren();
        render();
    }
    @Override
    public String getName() {
        return "2DLine";
    }

    @Override
    public String getDescription() {
        return "A flexible 2D line";
    }

    @Override
    public Drawlet handleMouseEvent(MouseEvent m) {

        int nearPoint = 0;

        // Always consume the event on move type events
        if(!isBuilt ||
                m.getEventType() == MouseEvent.MOUSE_MOVED ||
                m.getEventType() == MouseEvent.MOUSE_EXITED_TARGET ||
                m.getEventType() == MouseEvent.MOUSE_ENTERED_TARGET ||
                m.getEventType() == MouseEvent.MOUSE_DRAGGED ){
            m.consume();
        }
        else if( isSelected() ){

            // Figure out the bounding box of our line
            double[] mins = new double[]{Double.MAX_VALUE, Double.MAX_VALUE};
            double[] maxs = new double[]{Double.MIN_VALUE,Double.MIN_VALUE};
            for(Point2D p : points){
                mins[0] = Math.min( mins[0], p.getX());
                mins[1] = Math.min( mins[1], p.getY());
                maxs[0] = Math.max( maxs[0], p.getX());
                maxs[1] = Math.max( maxs[1], p.getY());
            }

            // Within the Bounding Box or not a Release
            if( m.getX() >= mins[0] && m.getX() <= maxs[0] ){
                if( m.getY() >= mins[1] && m.getY() <= maxs[1] ) {
                    m.consume();
                }
            }
            if( m.getEventType() != MouseEvent.MOUSE_RELEASED) {
                m.consume();
            }
        }

        // Check to see which point we're intersecting (if any)
        // nearPoint will be equal the length if no intersection
        for( Point2D p : points ){
            if( p.distance(m.getX(), m.getY()) < POINT_SIZE){
                break;
            }
            nearPoint++;
        }

        // They pressed a point, so consume the event for selection
        if( nearPoint < points.size()){
            m.consume();
        }

        if(m.getEventType() == MouseEvent.MOUSE_RELEASED){
            // Allow Double Click and Shift to count as the same
            if( m.getClickCount() == 2 || m.isShiftDown() ){
                if( !isBuilt && nearPoint == points.size() - 1){
                    isBuilt = true;
                }
                // Double-click on point after being built, so we delete it
                else if( nearPoint < points.size() ){
                    points.remove(nearPoint);
                }
                render();
            }
            else if(!isBuilt && nearPoint != points.size() - 1){
                // Add a new point to the line
                // You're not allowed to place a point on top the last point
                // but you CAN overlap other points
                points.add(new Point2D(m.getX(), m.getY()));
                render();
            }

            selectedPoint = -1;
        }
        else if( m.getEventType() == MouseEvent.MOUSE_PRESSED){
            currentPress = new Point2D(m.getX(), m.getY());
            // If the user presses on a mid-point, then insert a new
            // point and allow them to drag it around
            Point2D prevPoint = null;
            int insertAt = 0;
            for( Point2D p : points ){
                if( prevPoint != null ){
                    Point2D midPoint = p.midpoint(prevPoint);
                    if(midPoint.distance(m.getX(), m.getY()) < POINT_SIZE){
                        points.add(insertAt, midPoint);
                        nearPoint = insertAt;
                        render();
                        break;
                    }
                }
                insertAt++;
                prevPoint = p;
            }

            selectedPoint = nearPoint;

        }
        else if( isBuilt && m.getEventType() == MouseEvent.MOUSE_DRAGGED ){
            // Move the points selected; Since Points2Ds are immutable,
            // we remove the point and replace it
            if( nearPoint < points.size() ) {
                points.remove(nearPoint);
                points.add(nearPoint, new Point2D(m.getX(), m.getY()));
                render();
            }
            else {
                double deltaX = m.getX() - currentPress.getX();
                double deltaY = m.getY() - currentPress.getY();

                if( deltaX != 0 || deltaY != 0 ){
                    List<Point2D> newLine = new LList<>();
                    for( Point2D p : this.points ){
                        newLine.add( new Point2D(p.getX()+deltaX, p.getY()+deltaY));
                    }
                    currentPress = new Point2D(m.getX(), m.getY());
                    this.points.clear();
                    this.points = newLine;
                    render();
                }
            }
        }

        // For now, we don't have an options palette
        return null;
    }

    @Override
    public Drawlet handleKeyEvent(KeyEvent k) {
        Drawlet rtn = null;
        int deltaX = 0;
        int deltaY = 0;
        if( k.getEventType() == KeyEvent.KEY_RELEASED ){
            if( k.getCode() == KeyCode.UP ){
                deltaY -= KEY_MOVE_PX;
            }
            if( k.getCode() == KeyCode.DOWN ){
                deltaY += KEY_MOVE_PX;
            }
            if( k.getCode() == KeyCode.LEFT){
                deltaX -= KEY_MOVE_PX;
            }
            if( k.getCode() == KeyCode.RIGHT ){
                deltaX += KEY_MOVE_PX;
            }

            if( deltaX != 0 || deltaY != 0 ){
                List<Point2D> newLine = new LList<>();
                for( Point2D p : points ){
                    newLine.add( new Point2D(p.getX()+deltaX, p.getY()+deltaY));
                }
                points.clear();
                this.points = newLine;
                rtn = this;
                render();
            }
        }

        return rtn;
    }

    @Override
    public Region getPalette() {
        return null;
    }

    @Override
    public boolean select(boolean isSelected){
        super.select(isSelected);
        if(isSelected){
            this.setBorder(selectionBorder);
        }
        else {
            this.setBorder(null);
        }

        return isSelected;
    }

    // Draws our line onto a cleared canvas
    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Draw the line relative with (0,0) being the top-left of our Region
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());

        // Don't Draw an empty line
        if( points.size() == 0 ){
            return;
        }

        Color paint = Color.BLACK;
        if( isSelected() ){
            paint = Color.BLUE;
        }

        // Figure out the bounding box of our line
        double[] mins = new double[]{Double.MAX_VALUE, Double.MAX_VALUE};
        double[] maxs = new double[]{Double.MIN_VALUE,Double.MIN_VALUE};
        for(Point2D p : points){
            mins[0] = Math.min( mins[0], p.getX());
            mins[1] = Math.min( mins[1], p.getY());
            maxs[0] = Math.max( maxs[0], p.getX());
            maxs[1] = Math.max( maxs[1], p.getY());
        }

        Point2D prevPoint = null;
        gc.setFill(paint);
        gc.beginPath();
        gc.moveTo(points.get(0).getX()-mins[0]+POINT_SIZE/2.0, points.get(0).getY() - mins[1]+POINT_SIZE/2.0);
        for( Point2D p : points){
            // point UI
            gc.fillOval(p.getX()-mins[0], p.getY()-mins[1], POINT_SIZE, POINT_SIZE);

            // graphical line between anchor points
            gc.lineTo(p.getX()-mins[0]+POINT_SIZE/2.0, p.getY()-mins[1]+POINT_SIZE/2.0);

            // Draw midpoints if selected and built
            if( isBuilt && isSelected() && prevPoint != null ){
                Color midPointColor = paint.deriveColor(0, 1, 1, 0.25);
                gc.setFill(midPointColor);

                Point2D midPoint = p.midpoint(prevPoint);
                gc.fillOval(midPoint.getX()-mins[0], midPoint.getY()-mins[1], POINT_SIZE, POINT_SIZE );
                gc.setFill(paint);
            }
            prevPoint = p;
        }
        gc.stroke();

        // Reset our bounding box and location (graphically)
        this.setLayoutX(mins[0]-POINT_SIZE/2.0);
        this.setLayoutY(mins[1]-POINT_SIZE/2.0);
        this.setPrefSize(maxs[0]-mins[0]+POINT_SIZE,maxs[1]-mins[1]+POINT_SIZE);
    }

    public ObjectNode toJSON(){
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode root = mapper.createObjectNode();
        root.put("type", this.getClass().getSimpleName());

        ArrayNode points = root.putArray("points");
        for( Point2D p : this.points ){
            ObjectNode point = mapper.createObjectNode();
            point.put("X", p.getX());
            point.put("Y", p.getY());
            points.add(point);
        }
        return root;
    }

    public LineDrawlet fromJSON(JsonNode json){
        this.points.clear();
        for(JsonNode pts : json.get("points")){
            Point2D point = new Point2D(pts.get("X").intValue(), pts.get("Y").intValue());
            this.points.add(point);
        }
        isBuilt = true;

        render();
        return this;
    }
}