package board;

import list.*;

/**
 *  Implements board of the game Network. 
 */

public class Board {

    public final static int EMPTY = 0;
    public final static int BLACK = 1;
    public final static int WHITE = 2;

    private int[][] board = new int[8][8];

    /**
    * Modifies the board to be empty 
    *
    */	
    public void clearBoard() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

     /**
      * Adds a certain color piece at location (x, y) of the board
      *
      * @param color, the color of the piece	
      * @param x The x-coordinate of the piece
      * @param y The y-coordinate of the piece
      */
    public void add(int color, int x, int y) {
        board[x][y] = color;
    }

     /**
      * Removes a piece at location (x, y) of board
      *
      * @param x The x-coordinate of the piece
      * @param y The y-coordinate of the piece
      */
    public void remove(int x, int y) {
        board[x][y] = EMPTY;
    }

     /**
      * Returns a piece at location (x, y) of board
      *
      * @param x The x-coordinate of the piece
      * @param y The y-coordinate of the piece
      * @return Piece at location (x, y) of board
      */
    public int piece(int x, int y) {
        return board[x][y];
    }
    
   /**
    * Returns whether the location (x, y) is in the leftmost column
    * 
    * @param x The x-coordinate of the piece
    * @param y The y-coordinate of the piece
    * @return Whether that piece lies in the left column
    */
    public boolean inLeftHomeRow(int x, int y) {
        return (x == 0);
    }
    
   /**
    * Returns whether the location (x, y) is in the rightmost column
    * 
    * @param x The x-coordinate of the piece
    * @param y The y-coordinate of the piece
    * @return Whether that piece lies in the right column
    */
    public boolean inRightHomeRow(int x, int y) {
        return (x == 7);
    }
    
     /**
    * Returns whether the location (x, y) is in the topmost row
    * 
    * @param x The x-coordinate of the piece
    * @param y The y-coordinate of the piece
    * @return Whether that piece lies in the top row
    */
    public boolean inTopHomeRow(int x, int y) {
        return (y == 0);
    }
    
     /**
    * Returns whether the location (x, y) is in the bottommost row
    * 
    * @param x The x-coordinate of the piece
    * @param y The y-coordinate of the piece
    * @return Whether that piece lies in the bottom row
    */
    public boolean inBottomHomeRow(int x, int y) {
        return (y == 7);
    }
    
  /**
  * Returns whether (x,y) is one of (0,0), (7,0), (0,7), or (7,7)
  * @param x, the x coordinate
  * @param y, the y coordinate
  * @return Whether the coordinate (x,y) is a corner of the board
  */
  public boolean isCorner(int x, int y) {
    if ((x == 0) && (y == 0))
        return true;
    else if ((x == 7) && (y == 0))
        return true;
    else if ((x == 0) && (y == 7))
        return true;
    else if ((x == 7) && (y == 7))
        return true;
    else
        return false;
  }
  
  /**
  * Returns whether (x,y) is a valid space of the board
  * @param x, the x coordinate
  * @param y, the y coordinate
  * @return Whether the coordinate (x,y) is a position on the board
  */
  public boolean onBoard(int x, int y) {
    if ((x < 0) || (y < 0))
        return false;
    else if ((x > 7) || (y > 7))
        return false;
    else 
        return true;
  }

      /**
      * Returns a DList of possible coordinates of pieces that can be directly connected 
      * to (x, y) but follows properties
      *
      * 1) directly connected means piece in line of sight of (x, y)
      * 2) if (x,y) is either the bottom homerow or the right homerow, it has no connections
      * 3) no connection of (x, y) can be in the top homerow or the left homerow	
      *
      * @param color The color of the piece
      * @param x The x-coordinate of the piece
      * @param y The y-coordinate of the piece
      * @return DList of coordinates
      */
    public DList connections(int color, int x, int y) {
        if (inBottomHomeRow(x, y) || inRightHomeRow(x, y)) {
            return new DList();
        }
        int oppColor;

        if (color == BLACK) {
            oppColor = WHITE;
        } else {
            oppColor = BLACK;
        }
        DList conn = new DList();
        
        //North
        if (!inLeftHomeRow(x, y)) {
            for(int i = y - 1; i > 0; i--) {
                if (board[x][i] == color) {
                    conn.insertBack(new Coordinate(x, i));
                    break;
                } else if (board[x][i] == oppColor) {
                    break;
                }
            }
        }
        //South
        if (!inLeftHomeRow(x, y)) {
            for (int i = y + 1; i <= 7; i++) {
                if (board[x][i] == color) {
                    conn.insertBack(new Coordinate(x, i));
                    break;
                } else if (board[x][i] == oppColor) {
                    break;
                }
            }
        }
         //West
        if (!inTopHomeRow(x, y)) {
            for(int i = x - 1; i > 0; i--) {
                if (board[i][y] == color) {
                    conn.insertBack(new Coordinate(i, y));
                    break;
                } else if (board[i][y] == oppColor) {
                    break;
                }
            }
        }
         //East
        if (!inTopHomeRow(x, y)) {
            for(int i = x + 1; i <= 7; i++) {
                if (board[i][y] == color) {
                    conn.insertBack(new Coordinate(i, y));
                    break;
                } else if (board[i][y] == oppColor) {
                    break;
                }
             }
        }
         //Northeast
         int i = x + 1;
         int j = y - 1;
         while((i <= 7) && (j > 0)) {
             if (board[i][j] == color) {
                 conn.insertBack(new Coordinate(i, j));
                 break;
             } else if (board[i][j] == oppColor) {
                 break;
             }
             i++;
             j--;
         }
         //Southeast
         i = x + 1;
         j = y + 1;
         while((i <= 7) && (j <= 7)) {
             if (board[i][j] == color) {
                 conn.insertBack(new Coordinate(i, j));
                 break;
             } else if (board[i][j] == oppColor) {
                 break;
             }
             i++;
             j++;
         }
         //Southwest
         i = x - 1;
         j = y + 1;
         while((i > 0) && (j <= 7)) {
             if (board[i][j] == color) {
                 conn.insertBack(new Coordinate(i, j));
                 break;
             } else if (board[i][j] == oppColor) {
                 break;
             }
             i--;
             j++;
         }
         //Northwest
         i = x - 1;
         j = y - 1;
         while((i > 0) && (j > 0)) {
             if (board[i][j] == color) {
                 conn.insertBack(new Coordinate(i, j));
                 break;
             } else if (board[i][j] == oppColor) {
                 break;
             }
             i--;
             j--;
         }
         return conn;
    }
    
    /**
    * Returns a string version of class
    * @return string representation of the board
    */
    public String toString() {
        String s = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (piece(j, i) == Board.BLACK) {
                    s += "B";
                } else if (piece(j, i) == Board.WHITE) {
                    s += "W";
                } else {
                    s += "0";
                }
            }
            s += "\n";
        }
        return s;
    }
    
   /**
    * Tests methods of this class
    *
    */
    public static void main(String[] args) {
        Board testBoard = new Board();
        testBoard.clearBoard();
        testBoard.add(BLACK,1,0);
        testBoard.add(BLACK,5,0);
        testBoard.add(BLACK,6,2);
        testBoard.add(BLACK,1,3);
        testBoard.add(BLACK,4,3);
        testBoard.add(BLACK,4,4);
        testBoard.add(BLACK,2,6);
        testBoard.add(BLACK,4,6);
        testBoard.add(BLACK,4,7);
        testBoard.add(BLACK,6,7);
        testBoard.add(WHITE,3,1);
        testBoard.add(WHITE,5,1);
        testBoard.add(WHITE,2,2);
        testBoard.add(WHITE,5,2);
        testBoard.add(WHITE,0,4);
        testBoard.add(WHITE,2,4);
        testBoard.add(WHITE,7,4);
        testBoard.add(WHITE,5,5);
        testBoard.add(WHITE,1,6);
        testBoard.add(WHITE,6,6);
        System.out.println(testBoard);
        System.out.println(testBoard.connections(BLACK,1,0));
        System.out.println(testBoard.connections(BLACK,5,0));
        System.out.println(testBoard.connections(BLACK,6,2));
        System.out.println(testBoard.connections(BLACK,1,3));
        System.out.println(testBoard.connections(BLACK,4,3));
        System.out.println(testBoard.connections(BLACK,4,4));
        System.out.println(testBoard.connections(BLACK,2,6));
        System.out.println(testBoard.connections(BLACK,4,6));
        System.out.println(testBoard.connections(BLACK,4,7));
        System.out.println(testBoard.connections(BLACK,6,7));
        System.out.println(testBoard.connections(WHITE,3,1));
        System.out.println(testBoard.connections(WHITE,5,1));
        System.out.println(testBoard.connections(WHITE,2,2));
        System.out.println(testBoard.connections(WHITE,5,2));
        System.out.println(testBoard.connections(WHITE,0,4));
        System.out.println(testBoard.connections(WHITE,2,4));
        System.out.println(testBoard.connections(WHITE,7,4));
        System.out.println(testBoard.connections(WHITE,5,5));
        System.out.println(testBoard.connections(WHITE,1,6));
        System.out.println(testBoard.connections(WHITE,6,6));
    }
}


