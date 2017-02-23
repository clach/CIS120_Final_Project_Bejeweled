
import java.awt.*;

public class Jewel {

	// width and height of a jewel
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    
    // all potential gem colors
    private Color[] colors = { Color.RED, Color.ORANGE, Color.YELLOW, 
    	Color.GREEN, Color.CYAN, Color.MAGENTA, Color.WHITE };
    
    private Color c; // jewel color
    
    private boolean selected; // whether jewel is currently selected or not
    
    private boolean toRemove; // whether the jewel is to be removed or not  
    
    
    /** Constructor for Jewel object, creates randomly colored jewel
     **/
    public Jewel() {
        // pick random color from colors array
        int rand = (int) Math.floor(Math.random() * 7);
        this.c = colors[rand];

        this.selected = false;
        this.toRemove = false;
    }
    
    
    /** Constructor for Jewel object, takes in color and sets Jewel to that 
     * color (used only for testing)
     **/
    public Jewel(Color c) {
    	this.c = c;
        this.selected = false;
        this.toRemove = false;
    }
    
    
    /** Returns color of jewel
     * @out: Color indicating color of jewel
     **/
    public Color getColor() {
        return this.c;
    }
    
    
    /** Sets the jewel as selected
     **/
    public void setSelected() {
        selected = true;
    }
    
    
    /** Sets the jewel as unselected
     **/
    public void setUnselected() {
        selected = false;
    }
    
    
    /** Sets the jewel as to be removed
     **/
    public void setToRemove() {
        toRemove = true;
    }
    
    
    /** Sets the jewel as to not be removed
     **/
    public void setToNotRemove() {
        toRemove = false;
    }
    
    
    /** Returns whether jewel is to be removed or not
     * @out: boolean indicating if jewel is to be removed or not
     **/
    public boolean isToBeRemoved() {
        return toRemove;
    }
    
    
    /** Draw jewel at the given x and y coordinates
     * @param: g, Graphics context 
     * @param: x, int x index into game board at which jewel is to be drawn
     * @param: y, int y index into game board at which jewel is to be drawn
     **/
    public void drawAt(Graphics g, int x, int y) {
        
        // translate these x and y (board indices) to positions on the board
        int xDraw = GameBoard.getDrawingCoord(x);
        int yDraw = GameBoard.getDrawingCoord(y);
        
        g.setColor(c);
        g.fillOval(xDraw, yDraw, WIDTH, HEIGHT); // jewel is colored circle
        
        // if the jewel is selected, draw white box around it
        if (selected) {
            int offset = 2;
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(4));
            g.setColor(Color.WHITE);
            g.drawRect(xDraw - offset, yDraw - offset, WIDTH + 2 * offset, 
            		HEIGHT + 2 * offset);
        }
    }
    
}
