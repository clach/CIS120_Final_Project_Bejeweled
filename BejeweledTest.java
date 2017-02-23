
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.*;


public class BejeweledTest {
	
	@Test
    public void newGameFillsBoard() {
    	GameBoard b = new GameBoard();
    	assertTrue("initial board is empty", b.isEmpty());
    	b.newGameBoard();
    	assertTrue("new game board is full", b.isFull());
    }
	
	@Test
    public void fillBoardFillsBoard() {
    	GameBoard b = new GameBoard();
    	assertTrue("initial board is empty", b.isEmpty());
    	b.fillBoard();
    	assertTrue("new game board is full", b.isFull());
    }
	
	@Test
    public void clearBoardClearsBoard() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	assertTrue("new board is full", b.isFull());
    	b.clearBoard();
    	assertTrue("cleared board is empty", b.isEmpty());
    }
	
	@Test
    public void checkLeftRightChain() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(2, 1, j2);
    	b.setJewel(3, 1, j3);
    	assertTrue("horizontal chain exists 1", b.checkLeftRightChain(1, 1));
    	assertTrue("horizontal chain exists 2", b.checkLeftRightChain(2, 1));
    	assertTrue("horizontal chain exists 3", b.checkLeftRightChain(3, 1));
    }
	
	@Test
    public void checkUpDownChain() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(1, 2, j2);
    	b.setJewel(1, 3, j3);
    	assertTrue("vertical chain exists 1", b.checkUpDownChain(1, 1));
    	assertTrue("vertical chain exists 2", b.checkUpDownChain(1, 2));
    	assertTrue("vertical chain exists 3", b.checkUpDownChain(1, 3));
    }
	
	@Test
    public void chainsExist() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	assertFalse("initial board has no chains", b.chainsExist());
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(1, 2, j2);
    	b.setJewel(1, 3, j3);
    	assertTrue("chain now exists", b.chainsExist());
    }
	
	@Test
    public void switchJewels() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.RED);
    	b.setJewel(1, 1, j1);
    	b.setJewel(1, 2, j2);
    	b.switchJewels(1, 1, 1, 2);
    	assertEquals("jewels switched properly 1", b.getJewel(1, 1), j2);
    	assertEquals("jewels switched properly 1", b.getJewel(1, 2), j1);
    }
	
	@Test
    public void invalidAndValidMoves() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	assertFalse("switching non-adjacent jewels is not valid", 
    			b.validMove(1, 1, 5, 6));
    	assertFalse("switching diagonal jewels is not valid", 
    			b.validMove(1, 1, 2, 2));
    	assertFalse("out of bound switches are not valid", 
    			b.validMove(1, 1, 8, 6));
    	assertTrue("switching jewels horizontally is valid", 
    			b.validMove(1, 2, 2, 2));
    	assertTrue("switching jewels vertically is valid", 
    			b.validMove(1, 2, 1, 3));
    }
	
	@Test
    public void removeVerticalChainLength3() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(1, 2, j2);
    	b.setJewel(1, 3, j3);
    	assertTrue("chains exist", b.chainsExist());
    	b.removeChains();
    	assertEquals("removed jewel 1 is null", b.getJewel(1, 1), null);
    	assertEquals("removed jewel 2 is null", b.getJewel(1, 2), null);
    	assertEquals("removed jewel 3 is null", b.getJewel(1, 3), null);
    }
	
	@Test
    public void removeHorizontalChainLength3() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(2, 1, j2);
    	b.setJewel(3, 1, j3);
    	assertTrue("chains exist", b.chainsExist());
    	b.removeChains();
    	assertEquals("removed jewel 1 is null", b.getJewel(1, 1), null);
    	assertEquals("removed jewel 2 is null", b.getJewel(2, 1), null);
    	assertEquals("removed jewel 3 is null", b.getJewel(3, 1), null);
    }
	
	@Test
    public void removeVerticalChainLength4() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	Jewel j4 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(1, 2, j2);
    	b.setJewel(1, 3, j3);
    	b.setJewel(1, 4, j4);
    	assertTrue("chains exist", b.chainsExist());
    	b.removeChains();
    	assertEquals("removed jewel 1 is null", b.getJewel(1, 1), null);
    	assertEquals("removed jewel 2 is null", b.getJewel(1, 2), null);
    	assertEquals("removed jewel 3 is null", b.getJewel(1, 3), null);
    	assertEquals("removed jewel 4 is null", b.getJewel(1, 4), null);
    }
	
	@Test
    public void removeHorizontalChainLength4() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	Jewel j4 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(2, 1, j2);
    	b.setJewel(3, 1, j3);
    	b.setJewel(4, 1, j4);
    	assertTrue("chains exist", b.chainsExist());
    	b.removeChains();
    	assertEquals("removed jewel 1 is null", b.getJewel(1, 1), null);
    	assertEquals("removed jewel 2 is null", b.getJewel(2, 1), null);
    	assertEquals("removed jewel 3 is null", b.getJewel(3, 1), null);
    	assertEquals("removed jewel 4 is null", b.getJewel(4, 1), null);
    }
	
	@Test
    public void removeVerticalChainLength5() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	Jewel j4 = new Jewel(Color.YELLOW);
    	Jewel j5 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(1, 2, j2);
    	b.setJewel(1, 3, j3);
    	b.setJewel(1, 4, j4);
    	b.setJewel(1, 5, j5);
    	assertTrue("chains exist", b.chainsExist());
    	b.removeChains();
    	assertEquals("removed jewel 1 is null", b.getJewel(1, 1), null);
    	assertEquals("removed jewel 2 is null", b.getJewel(1, 2), null);
    	assertEquals("removed jewel 3 is null", b.getJewel(1, 3), null);
    	assertEquals("removed jewel 4 is null", b.getJewel(1, 4), null);
    	assertEquals("removed jewel 5 is null", b.getJewel(1, 5), null);
    }
	
	@Test
    public void removeHorizontalChainLength5() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.YELLOW);
    	Jewel j3 = new Jewel(Color.YELLOW);
    	Jewel j4 = new Jewel(Color.YELLOW);
    	Jewel j5 = new Jewel(Color.YELLOW);
    	b.setJewel(1, 1, j1);
    	b.setJewel(2, 1, j2);
    	b.setJewel(3, 1, j3);
    	b.setJewel(4, 1, j4);
    	b.setJewel(5, 1, j5);
    	assertTrue("chains exist", b.chainsExist());
    	b.removeChains();
    	assertEquals("removed jewel 1 is null", b.getJewel(1, 1), null);
    	assertEquals("removed jewel 2 is null", b.getJewel(2, 1), null);
    	assertEquals("removed jewel 3 is null", b.getJewel(3, 1), null);
    	assertEquals("removed jewel 4 is null", b.getJewel(4, 1), null);
    	assertEquals("removed jewel 5 is null", b.getJewel(5, 1), null);
    }
	
	@Test
    public void removeEntireBoard() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			b.setJewel(i, j, new Jewel(Color.YELLOW));
    		}
    	}
    	assertTrue("chains exist", b.chainsExist());
    	b.removeChains();
    	assertTrue("entire board removed", b.isEmpty());
    }
	
	@Test
    public void noChainsRemoved() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	assertFalse("no chains exist", b.chainsExist());
    	b.removeChains();
    	assertTrue("no chains to remove", b.isFull());
    }
	
	@Test
    public void moveDown() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j = new Jewel(Color.YELLOW);
    	b.setJewel(1, 0, j);
    	b.setJewel(1, 1, null);
    	b.moveDown();
    	assertEquals("jewel spot now empty", b.getJewel(1, 0), null);
    	assertEquals("jewel moved down", b.getJewel(1, 1), j);
    }
	
	@Test
    public void emptySpaces() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	Jewel j1 = new Jewel(Color.YELLOW);
    	Jewel j2 = new Jewel(Color.RED);
    	Jewel j3 = new Jewel(Color.BLUE);
    	b.setJewel(1, 1, j1);
    	b.setJewel(2, 1, j2);
    	b.setJewel(3, 1, j3);
    	b.setJewel(1, 2, null);
    	b.setJewel(2, 2, null);
    	b.setJewel(3, 2, null);
    	assertTrue("empty spaces exist", b.emptySpaces());
    }
	
	@Test
    public void noEmptySpacesOnNewBoard() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	assertFalse("no empty spaces", b.emptySpaces());
	}
	
	@Test
    public void noEmptySpacesExceptOnTop() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	b.setJewel(1, 0, null);
    	b.setJewel(2, 0, null);
    	b.setJewel(3, 0, null);
    	assertFalse("no empty spaces", b.emptySpaces());
    }
	
	@Test
    public void noMovesLeft() {
    	GameBoard b = new GameBoard();
    	b.newGameBoard();
    	
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			Jewel jewel = new Jewel(new Color(i, j, 255));
    			b.setJewel(i, j, jewel);
    		}
    	}
    	
    	assertFalse("no moves left", b.movesLeft());
    }
	

}