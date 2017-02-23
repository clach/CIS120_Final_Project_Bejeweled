
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    
    private static final int NUM_ROW_COL = 8;
    
    private static final int BOARD_WIDTH = 320;
    private static final int BOARD_HEIGHT = 320;
    
    private boolean playing = false; // whether the game is running
    
    // Update interval for timer, in milliseconds
    private static final int INTERVAL = 35;
    
    // 2D array representing game board
    private Jewel[][] board; 
    
    // current score
    private int score;
    
    
    /** Constructor for GameBoard class
     **/   
    public GameBoard() {
        board = new Jewel[NUM_ROW_COL][NUM_ROW_COL];
      
        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key
        // events will be handled by its key listener.
        setFocusable(true);
        
    }
    
    
    /** Returns if board is full or not
     * @out: boolean indicating whether board is currently full or not
     **/
    public boolean isFull() {
        boolean isFull = true;
        
        // run through each index of board, checking if they are null
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (board[i][j] == null) {
                    isFull = false;
                }
            }
        }
        
        return isFull;
    }
    
    
    /** Returns if board is empty or not
     * @out: boolean indicating whether board is currently empty or not
     **/
    public boolean isEmpty() {
        boolean isEmpty = true;
        
        // run through each index of board, checking if they are null
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (board[i][j] != null) {
                    isEmpty = false;
                }
            }
        }
        
        return isEmpty;
    }

    
    /** Fills any empty space in board with random Jewel
     **/
    public void fillBoard() {
        // run through each index of board, checking if they are null
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (board[i][j] == null) {
                    // fill with new random jewel object
                    Jewel newJewel = new Jewel();
                    board[i][j] = newJewel; 
                }
            }
        } 
    }
    
    
    /** Creates a new board for a new game of Bejeweled, initial board should 
     * have no chains in it
     **/
    public void newGameBoard() {
        
        clearBoard();
        fillBoard();
        
        // if chains exist in this board, make a new game board
        if (chainsExist()) {
            newGameBoard();
        }
        
        playing = true;
    }
    
    
    /** Checks if there is a chain of 3 or more jewels formed in the horizontal
     * direction, given a starter position
     * @param: x, int indicating x index into board
     * @param: y, int indicating y index into board
     * @out: boolean indicating if there is a chain formed
     **/
    public boolean checkLeftRightChain(int x, int y) {
        Set<Point> toDelete = new HashSet<Point>();
        
        Color c = board[x][y].getColor();
        
        toDelete.add(new Point(x, y));
        
        int xLeft = x;
        
        // first go left
        while (xLeft - 1 >= 0 && board[xLeft - 1][y].getColor() == c) {
            toDelete.add(new Point(xLeft - 1, y));
            xLeft--;
        }
        
        int xRight = x;
        
        // go right
        while (xRight + 1 < NUM_ROW_COL && 
        	board[xRight + 1][y].getColor() == c) {
            toDelete.add(new Point(xRight + 1, y));
            xRight++;
        }     
        
        // if we did not form a chain of 3 or more, do not delete anything
        if (toDelete.size() < 3) {
           return false;
        }
        
        
        // for each jewel in the set, mark it for removal
        for (Point p : toDelete) {
            board[(int) p.getX()][(int) p.getY()].setToRemove();
        }
        
        return true;
    }
    
    
    /** Checks if there is a chain of 3 or more jewels formed in the vertical
     * direction, given a starter position
     * @param: x, int indicating x index into board
     * @param: y, int indicating y index into board
     * @out: boolean indicating if there is a chain formed
     **/
    public boolean checkUpDownChain(int x, int y) {
        Set<Point> toDelete = new HashSet<Point>();
        
        Color c = board[x][y].getColor();
        
        // n is number of gems of the same color as initial gem
        int n = 1;
        
        int yDown = y;
        toDelete.add(new Point(x, y));
        
        // first go down the board
        while (yDown + 1 < NUM_ROW_COL && board[x][yDown + 1].getColor() == c) {
            n++;
            toDelete.add(new Point(x, yDown + 1));
            yDown++;
        }
        
        int yUp = y;
        
        // then go up the board
        while (yUp - 1 >= 0 && board[x][yUp - 1].getColor() == c) {
            n++;
            toDelete.add(new Point(x, yUp - 1));
            yUp--;
        }     
        
        // if we did not form a chain of 3, do not delete anything
        if (n < 3) {
            return false;
        }
        
        // for each jewel in the set, mark it for removal
        for (Point p : toDelete) {
            board[(int) p.getX()][(int) p.getY()].setToRemove();
        }
           
        return true;
    }
    
    
    /** Checks if there exists a chain of 3 or more jewels anywhere in the board
     * @out: boolean indicating if there is a chain anywhere in the board
     **/
    public boolean chainsExist() {
        boolean chains = false;
        
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (checkLeftRightChain(i, j)) {
                    chains = true;
                }
                if (checkUpDownChain(i, j)) {
                    chains = true;
                }
            }
        }
        
        return chains;
    }
       
    
    /** Removes any jewels in the board marked for removal, by setting the area
     * of the board to null
     **/
    public void removeChains() {
    	
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (board[i][j].isToBeRemoved()) {
                    board[i][j] = null;
                    incrementScore(); // increase score by 10 for each jewel
                }
            }
        }
        
    }
    
    
    /** If there are empty spaces in the board with jewels above them, move 
     * jewels down
     **/
    public void moveDown() {
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                // if there is a jewel
                if (board[i][j] != null) {
                    // with no jewel underneath it
                    if (j + 1 < NUM_ROW_COL) {
                        if (board[i][j + 1] == null) {
                            // move the jewel down
                            board[i][j + 1] = board[i][j];
                            board[i][j] = null;
                        }
                    }
                }

            }
        }
        
        // if there are still empty spaces with jewels above, move down again
        if (emptySpaces()) {
            moveDown();
        }
    }

    
    /** Determines if there are any empty spaces in the board with jewels above
     * them
     * @out: boolean indicating if there are any empty spaces
     **/
    public boolean emptySpaces() {
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                // if there is a jewel
                if (board[i][j] != null) {
                    // with no jewel underneath it
                    if (j + 1 < NUM_ROW_COL) {
                        if (board[i][j + 1] == null) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }
    
    
    /** Determines if there are any potential moves left in the game
     * @out: boolean indicating if there moves left
     **/
    public boolean movesLeft() {
        
        boolean chainsExist = false;
        
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (board[i][j] != null) {
                    
                    // check switch with left jewel
                    if (i - 1 >= 0) {
                        switchJewels(i, j, i - 1, j);
                        chainsExist = chainsExist();
                        switchJewels(i, j, i - 1, j);
                        
                        for (int m = 0; m < NUM_ROW_COL; m++) {
                            for (int n = 0; n < NUM_ROW_COL; n++) {
                                board[i][j].setToNotRemove();
                            }
                        }
                        
                        if (chainsExist) {
                            return chainsExist;
                        }
                    }

                    // check switch with right jewel
                    if (i + 1 < NUM_ROW_COL) {
                        switchJewels(i, j, i + 1, j);
                        chainsExist = chainsExist();
                        switchJewels(i, j, i + 1, j);
                        
                        for (int m = 0; m < NUM_ROW_COL; m++) {
                            for (int n = 0; n < NUM_ROW_COL; n++) {
                                board[m][n].setToNotRemove();
                            }
                        }
                        for (int m = 0; m < NUM_ROW_COL; m++) {
                            for (int n = 0; n < NUM_ROW_COL; n++) {
                                board[m][n].setToNotRemove();
                            }
                        }
                        if (chainsExist) {
                            return chainsExist;
                        }
                    }

                    // check switch with above jewel
                    if (j - 1 >= 0) {
                        switchJewels(i, j, i, j - 1);
                        chainsExist = chainsExist();
                        switchJewels(i, j, i, j - 1);
                        
                        for (int m = 0; m < NUM_ROW_COL; m++) {
                            for (int n = 0; n < NUM_ROW_COL; n++) {
                                board[m][n].setToNotRemove();
                            }
                        }
                        
                        if (chainsExist) {
                            return chainsExist;
                        }
                    }

                    // check switch with below jewel
                    if (j + 1 < NUM_ROW_COL) {
                        switchJewels(i, j, i, j + 1);
                        chainsExist = chainsExist();
                        switchJewels(i, j, i, j + 1);
                        
                        for (int m = 0; m < NUM_ROW_COL; m++) {
                            for (int n = 0; n < NUM_ROW_COL; n++) {
                                board[m][n].setToNotRemove();
                            }
                        }
                        
                        if (chainsExist) {
                            return chainsExist;
                        }
                    }
                    
                    
                }
            }
        }
        
        return chainsExist;
    }
    
    
    /** Clears the board of any jewels by setting them to null
     **/
    public void clearBoard() {
        // clear all jewels
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (board[i][j] != null) {
                    board[i][j] = null;
                }
            }
        }
    }
    
    
    /** Determines if a given move is valid, given two jewel positions on the 
     * board
     * @param: x1, int indicating x index of first jewel
     * @param: y1, int indicating y index of first jewel
     * @param: x2, int indicating x index of second jewel
     * @param: y2, int indicating y index of second jewel
     **/
    public boolean validMove(int x1, int y1, int x2, int y2) {
        // jewel2 is left, right, or same horizontal position as jewel1
        // AND is same vertical position
        // OR
        // jewel2 is above, below, or same vertical position as jewel1
        // AND is same horizontal position
        if ((x1 >= x2 - 1 && x1 <= x2 + 1 && y1 == y2) || (y1 >= y2 - 1 && y1 <= y2 + 1 && x1 == x2)) {
            return true;
        } else {
            return false;
        }
    }
    
    
    /** Switches the positions of two jewels on the board
     * @param: x1, int indicating x index of first jewel
     * @param: y1, int indicating y index of first jewel
     * @param: x2, int indicating x index of second jewel
     * @param: y2, int indicating y index of second jewel
     **/
    public void switchJewels(int x1, int y1, int x2, int y2) {  
        if (validMove(x1, y1, x2, y2)) {
            Jewel temp = board[x1][y1];
            board[x1][y1] = board[x2][y2];
            board[x2][y2] = temp;
        }
    }
    

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {        
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    
    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    private void tick() {
        if (playing) {
            // update the display
            repaint();
        }
    }
    
    
    /** Determines if game is currently in play
     * @out: boolean indicating if game is in play
     **/
    public boolean inPlay() {
        return playing;
    }
    
    
    /** Returns current user score
     * @out: int indicating score
     **/
    public int getScore() {
        return score;
    }
    
    
    /** Set score to zero
     **/
    public void resetScore() {
        score = 0;
    }
    
    
    /** Increase score by 10 points
     **/
    public void incrementScore() {
        score += 10;
    }
    
    
    /** Determines if given coordinates are within bounds of panel
     * @param: x, int indicating x coordinate
     * @param: y, int indicating y coordinate
     **/
    public static boolean inBounds(double x, double y) {
        return (x >= 10 & x <= BOARD_WIDTH + 10 & 
        		y >= 10 & y <= BOARD_HEIGHT + 10);
    } 
    
    
    /** Get index into game board (the 2D array) given panel coordinates
     * @param: n, double indicating coordinate within panel
     * @out: int indicating corresponding index into the 2D array board
     **/
    public static int getBoardCoord(double n) {
        if (n >= 10 && n < 50) {
            return 0;
        } else if (n >= 50 && n < 90) {
            return 1;
        } else if (n >= 90 && n < 130) {
            return 2;
        } else if (n >= 130 && n < 170) {
            return 3;
        } else if (n >= 170 && n < 210) {
            return 4;
        } else if (n >= 210 && n < 250) {
            return 5;
        } else if (n >= 250 && n < 290) {
            return 6;
        } else if (n >= 290 && n <= 330) {
            return 7;
        } else {
            return -1;
        }
    }
    
    
    /** Get index into game board (the 2D array) given panel coordinates
     * @param: n, int indicating index into 2D array game board
     * @out: int indicating corresponding panel coordinate
     **/
    public static int getDrawingCoord(int n) {
        switch (n) {
        case 0:
            return 15;
        case 1:
            return 55;
        case 2:
            return 95;
        case 3:
            return 135;
        case 4:
            return 175;
        case 5:
            return 215;
        case 6:
            return 255;
        case 7:
            return 295;
        }
        return -1;
    }
    
    
    /** Returns jewel at specific indices of game board
     * @param: x, int indicating x index into 2D array game board
     * @param: y, int indicating y index into 2D array game board
     * @out: Jewel at indices
     **/
    public Jewel getJewel(int x, int y) {
        return board[x][y];
    }
    
    
    /** Sets board[x, y] to a given jewel, used only for testing purposes 
     * @param: x, int indicating x coordinate
     * @param: y, int indicating y coordinate
     * @param: j, Jewel object
    **/
   public void setJewel(int x, int y, Jewel j) {
       board[x][y] = j;
   }
    
   
    @Override
    /** Draw game board and jewels within game board
     * @param: g, Graphics context
     **/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // draw background of board
        g.setColor(new Color(200, 200, 200, 200));
        g.fillRect(10, 10, BOARD_WIDTH, BOARD_HEIGHT);
        
        // draw gridlines of board
        g.setColor(Color.BLACK);
        int n = 10;
        while (n <= BOARD_WIDTH + 10) {
            // draw vertical line
            g.drawLine(n, 10, n, BOARD_HEIGHT + 10);
            
            // draw horizontal line
            g.drawLine(10, n, BOARD_WIDTH + 10, n);
            
            n += 40;
        }
        
        // draw jewels
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                if (board[i][j] != null) {
                    board[i][j].drawAt(g, i, j);
                }
            }
        }
    }
}
