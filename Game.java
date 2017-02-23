import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	private JFrame frame;
	private GameBoard board;
	private JLabel scoreKeeper; // score display
	private JLabel status = null; // status of game, also gives warnings

    private String instructions = "<html>To play Bejeweled, select two " + 
    		"adjacent jewels to switch their positions. Jewels will only " +
    		"switch if the switch forms a chain of three or more jewels of " +
    		"the same color. Such chains of jewels will disappear, causing " + 
    		"the rest of the board to move down and fill with more jewels. " + 
    		"The game ends when there are no possible moves left. </html>";
      
    private String filename = "high_scores.txt";
    private String highScores = ""; // string of high scores

    private boolean jewelSelected = false;
    private Point firstJewel = null;
            
    
    /** Runs game of Bejeweled
     **/
    public void run() {
        // top level frame
        frame = new JFrame("Bejeweled");
        frame.setLocation(200, 200);
        frame.setSize(610, 410);

        // title panel
        final JPanel titlePanel = new JPanel();
        frame.add(titlePanel, BorderLayout.NORTH);
        final JLabel title = new JLabel ("Bejeweled");
        title.setFont(new Font("Sans-Serif", Font.PLAIN, 32));
        titlePanel.add(title);

        // default panel
        final JPanel defaultPanel = new JPanel();
        frame.add(defaultPanel, BorderLayout.WEST);
        status = new JLabel("Come play a game!");
        defaultPanel.setVisible(true);
        defaultPanel.setPreferredSize(new Dimension(265, 610));
        
        defaultPanel.setLayout(new GridLayout(0, 1));
        defaultPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        
        // high scores panel
        final JPanel highScoresPanel = new JPanel();
        highScoresPanel.setLayout(new GridLayout(0, 1));
        highScoresPanel.setPreferredSize(new Dimension(265, 610));

        // label displaying high scores	
        final JLabel highScoresLabel = new JLabel(highScores);
        highScoresLabel.setBorder(
        		BorderFactory.createTitledBorder("High Scores"));

        // instructions panel
        final JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new GridLayout(0, 1));
        instructionsPanel.setPreferredSize(new Dimension(265, 610));

        // board (main playing area)
        board = new GameBoard();
        frame.add(board, BorderLayout.CENTER);

        // listen for mouse release events on the board
        Mouse mouseListener = new Mouse();
        board.addMouseListener(mouseListener);
        
        
        // default panel components:

        // new game button -> starts new game of Bejeweled
        final JButton newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(100, 10));
        defaultPanel.add(newGameButton);
        
        // instructions button -> displays instructions to screen
        final JButton instructionsButton = new JButton("Instructions");
        defaultPanel.add(instructionsButton);
        
        // high scores button -> displays high scores
        final JButton highScoresButton = new JButton("View High Scores");
        defaultPanel.add(highScoresButton);
        
        
        // instruction panel components: 
        
        // back button -> returns you to main page from instructions
        final JButton backButton = new JButton("Back");
        instructionsPanel.add(backButton);
        
        // instructions label
        final JLabel instructionsLabel = new JLabel(instructions);
        instructionsLabel.setBorder(
        		BorderFactory.createTitledBorder("Instructions"));
        instructionsPanel.add(instructionsLabel);
        
        
        // high scores panel components:
        
        // back button -> returns you to main page from high scores
        final JButton backScoresButton = new JButton("Back");
        highScoresPanel.add(backScoresButton);
        highScoresPanel.add(highScoresLabel);

        // status of game, also prints warning messages
        defaultPanel.add(status); 
        
        // current game score
        scoreKeeper = new JLabel("");
        defaultPanel.add(scoreKeeper);

        // starts a new game
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                status.setText("Game in progress...");
                board.resetScore(); // set score to zero
                scoreKeeper.setText("Score: "+ board.getScore());
                jewelSelected = false;
                firstJewel = null;
                board.newGameBoard();
            }
        });

        // returns from instructions panel to default panel
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultPanel.setVisible(true);
                instructionsPanel.setVisible(false);
                frame.add(defaultPanel, BorderLayout.WEST);
            }
        });
        
        // returns from high scores panel to default panel
        backScoresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultPanel.setVisible(true);
                highScoresPanel.setVisible(false);
                frame.add(defaultPanel, BorderLayout.WEST);
            }
        });

        // go from default panel to instructions panel
        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultPanel.setVisible(false);
                instructionsPanel.setVisible(true);
                frame.add(instructionsPanel, BorderLayout.WEST);
            }
        });
        
        // go from default panel to high scores panel
        highScoresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                String line = null;
              
                try {
                    FileReader fr = new FileReader(filename);
                    
                    // wrap in buffered reader so we can get line at a time
                    BufferedReader br = new BufferedReader(fr);
                    
                    highScores = "";
                    
                    int n = 1;
                    
                    // parse text file as high score list
                    while ((line = br.readLine()) != null) {
                        
                        if (n == 1) {
                            highScores += "<html>" + n + ") " + line;
                        } else {
                            highScores += "<html> <br> " + n + ") " + line;
                        }
                        n++;
                    }
                    
                    br.close(); // close file
                    
                } catch (FileNotFoundException e1) {
                    System.out.println("File '" + filename + "' not found");
                    
                } catch (IOException e2) {
                    System.out.println("Error in opening file '" + 
                    		filename + "'");
                }
                
                highScoresLabel.setText(highScores);
                
                defaultPanel.setVisible(false);
                highScoresPanel.setVisible(true);
                frame.add(highScoresPanel, BorderLayout.WEST);
            }
        });

        // Put the frame on the screen
        // frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // start game
        board.reset();

    }

    
    /** Handles any mouse clicks on game board
     **/
    public class Mouse extends MouseAdapter {

        /**
         * Code to execute when the button is released while the mouse is in the
         * canvas.
         * @param: arg0, MouseEvent
         */
        @Override
        public void mouseReleased(MouseEvent arg0) {

            if (board.inPlay()) {
                Point p = arg0.getPoint();

                // get coordinates of point within board
                double board_x = p.getX();
                double board_y = p.getY();
                
                if (GameBoard.inBounds(board_x, board_y)) {
                    
                    // first jewel selected
                    if (jewelSelected == false) {

                        jewelSelected = true;
                        firstJewel = p;
                        int x = GameBoard.getBoardCoord(board_x);
                        int y = GameBoard.getBoardCoord(board_y);
                        board.getJewel(x, y).setSelected();
                        
                    // second jewel selected
                    } else {
                        
                        jewelSelected = false;
                       
                        // find indices of two jewels currently selected
                        int x1 = GameBoard.getBoardCoord(board_x);
                        int y1 = GameBoard.getBoardCoord(board_y);
                        int x2 = GameBoard.getBoardCoord(firstJewel.getX());
                        int y2 = GameBoard.getBoardCoord(firstJewel.getY());
                        
                        board.getJewel(x2, y2).setUnselected();

                        board.switchJewels(x1, y1, x2, y2);
                            
                        // check for chains in either direction for each gem
                        // marks jewels for removal if necessary
                        if (board.checkLeftRightChain(x1, y1) || 
                        		board.checkUpDownChain(x1, y1) ||
                        		board.checkLeftRightChain(x2, y2) ||
                        		board.checkUpDownChain(x2, y2)) {
                        	
                        	status.setForeground(Color.BLACK);
                        	status.setText("You made a chain!");
                                                        
                            while (board.chainsExist()) {
                                board.removeChains(); // remove gems marked
                                board.moveDown(); // move down jewels on board
                                board.fillBoard(); // fill empty spaces
                            }
                            
                            // update score display
                            scoreKeeper.setText("Score: "+ board.getScore());

                        } else {
                            // if no chains are made, switch jewels back
                        	status.setForeground(Color.RED);
                        	status.setText("No chain was made!");
                        	
                            board.switchJewels(x1, y1, x2, y2);
                        }

                        firstJewel = null;

                    }
                } else {
                	status.setForeground(Color.RED);
                	status.setText("Please click within the board!");
                }
            }

            //board.repaint();
            
            if (!board.movesLeft()) { // if there are no possible moves left
            	board.clearBoard(); // clear board
                status.setForeground(Color.BLACK);
                status.setText("GAME OVER!");
                
                String line = null;
              
                try {
                    FileReader fr = new FileReader(filename);
                    
                    // wrap in buffered reader so we can get line at a time
                    BufferedReader br = new BufferedReader(fr);                    
                    
                    String[] lastLine = null;
                    
                    String[] highScoreArray = new String[5];
                    
                    int i = 0;
                    
                    while ((line = br.readLine()) != null) {
                        highScoreArray[i] = line;
                        i++;
                    }
                    
                    lastLine = highScoreArray[4].split(": ");
                    
                    String lowestScoreString = lastLine[1];
                    
                    int lowestScoreInt = Integer.parseInt(lowestScoreString);
                    
                    int userScore = board.getScore();
                    if (userScore >= lowestScoreInt) {
                    	
                    	// show pop up asking for user input
                        String username = (String) JOptionPane.showInputDialog(
                        		frame, "Please enter your name!", 
                        		"NEW HIGH SCORE!", JOptionPane.PLAIN_MESSAGE, 
                        		null, null, "");
                        
                        FileWriter fw = new FileWriter(filename);
                        
                        // wrap in buffered reader so we can get line at a time
                        BufferedWriter bw = new BufferedWriter(fw);

                        String[] currLine;
                        String currScoreString;
                        int currScoreInt;
                        int j = 0;
                        
                        while (j < 5) {
                        	
                            currLine = highScoreArray[j].split(": ");
                            currScoreString = currLine[1];
                            currScoreInt = Integer.parseInt(currScoreString);
                            
                            if (userScore >= currScoreInt) {
                                bw.write(username + ": " + userScore);
                                if (j < 4) {
                                	bw.write("\n");
                                }
                                
                                for (int n = j; n < 4; n++) {
                                    bw.write(highScoreArray[n]);

                                    if (j < 3) {
                                    	bw.write("\n");
                                    }
                                    j++;
                                }
                                
                                j++;
                                
                            } else {
                                bw.write(highScoreArray[j]);
                                
                                if (j < 4) {
                                	bw.write("\n");
                                }
                                j++;
                            }
                            
                        }
                        
                        bw.close(); // close file
                    }

                    br.close(); // close file
                    
                } catch (FileNotFoundException e1) {
                    System.out.println("File '" + filename + "' not found");
                    
                } catch (IOException e2) {
                    System.out.println("Error in opening file '" + filename + 
                    		"'");
                }
   
            }  
        }
    }
    
    
    /**
     * Main method run to start and run the game Initializes the GUI elements
     * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
     * this in the final submission of your game.
     **/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
