/* MachinePlayer.java */

package player;

import list.*;
import board.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  private Board myBoard; // The player's internal representation of the Board
  private int moves = 0; // The number of moves the Player has taken
  private int opponentMoves = 0; // The number of moves the opponent has taken
  private int playerColor; // The color of the Player's pieces
  private int opponentColor; // Color of the opponent's pieces
  private int maxSearchDepth; // The maximum search depth
  private final int MAX_SCORE = 1000; // The max score given to a board
  private final int MIN_SCORE = -1000; // The min score given to a board
  
  /** Creates a machine player with the given color.  Color is either 0 (black)
  * or 1 (white).  (White has the first move.) This defaults to a search depth of 3
  * 
  * @param color The given color
  */  
  public MachinePlayer(int color) {
    this(color, 3);
  }

  /**
  * Creates a machine player with the given color and search depth.  Color is
  * either 0 (black) or 1 (white).  (White has the first move.)
  * @param color The given color
  * @param searchDepth The search depth   
  */
  public MachinePlayer(int color, int searchDepth) {
    if (color == 0) {
        playerColor = Board.BLACK;
        opponentColor = Board.WHITE;
    } else {
        playerColor = Board.WHITE;
        opponentColor = Board.BLACK;
    }
    maxSearchDepth = searchDepth;
    myBoard = new Board();
  }

  /** Chooses and returns a new move by "this" player.  Internally records the move (updates
   *  the internal game board) as a move by "this" player.
   * @return the chosen move
   */
  public Move chooseMove() {
     Move m = searchGameTree(playerColor, Integer.MIN_VALUE, Integer.MAX_VALUE, maxSearchDepth).move;
     forceMove(m);
     return m;
  } 
  
  // Searches the game tree, and returns what it believes is the best move for the player
  // Uses alpha-beta pruning. White is the maximizing player, black is the minimizing player
  private Best searchGameTree(int color, int alpha, int beta, int depth) {
    // If both players have a network, then previous move created a network while unblocking another, so the current player should win
    // Small mini-hack: score a win in 1 move slightly better than a win in 3 moves
    // Depth is higher when fewer moves have been checked.
    int oppositeColor;
    if (color == playerColor) {
        oppositeColor = opponentColor;
    } else {
        oppositeColor = playerColor;
    }
    if (hasWonGame(color)) {
        if (color == Board.WHITE) {
            Best b = new Best();
            b.score = MAX_SCORE + depth;
            return b;
        } else {
            Best b = new Best();
            b.score = MIN_SCORE - depth;
            return b;
        }
    } else if (hasWonGame(oppositeColor)) {
        if (oppositeColor == Board.WHITE) {
            Best b = new Best();
            b.score = MAX_SCORE + depth;
            return b;
        } else {
            Best b = new Best();
            b.score = MIN_SCORE - depth;
            return b;
        }
    } 
    if (depth == 0) {
        Best b = new Best();
        b.score = evaluateBoard();
        return b;
    }
    DList possibleMoves = allValidMoves(color);
    DListNode curr = possibleMoves.front();
    Best currBest = new Best();
    if (color == Board.WHITE) {
        currBest.score = alpha;
    } else {
        currBest.score = beta;
    }
    Best reply;
    Move checkedMove;
    // Check moves
    while (curr != null) {
        checkedMove = (Move) curr.item;
        // apply move
        if (color == playerColor) {
            forceMove(checkedMove);
        } else {
            opponentMove(checkedMove);
        }
        reply = searchGameTree(oppositeColor, alpha, beta, depth - 1);
        // undo move
        undoMove(color, checkedMove);
        
       if ((color == Board.WHITE) &&
            (reply.score > currBest.score)) {
            currBest.move = checkedMove;
            currBest.score = reply.score;
            alpha = reply.score;
        } else if ((color == Board.BLACK) &&
            (reply.score < currBest.score)) {
            currBest.move = checkedMove;
            currBest.score = reply.score;
            beta = reply.score;
        }
        // Patch for odd bug that appeared
        if (alpha >= beta) {
            if (currBest.move == null) {
                if (possibleMoves.length() == 0) {
                    currBest.move = new Move();
                } else {
                    currBest.move = (Move) possibleMoves.front().item;
                }
            }
            return currBest;
        }
        curr = possibleMoves.next(curr);
    }
    if (currBest.move == null) {
        if (possibleMoves.length() == 0) {
            currBest.move = new Move();
        } else {
            currBest.move = (Move) possibleMoves.front().item;
        }
    }
    return currBest;
  }
  
  /**
  * Does a heuristic evaluation of the board. Returns a value from -1000 to 1000, 
  * where 1000 is a win for white, -1000 is a win for black, and 0 is an even game.
  */
  private int evaluateBoard() {
    // Number connections from white pieces - number connections from black pieces
    int numConnections = 0;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (myBoard.piece(i, j) == Board.WHITE) {
                numConnections += myBoard.connections(Board.WHITE, i, j).length();
            } else if (myBoard.piece(i,j) == Board.BLACK) {
                numConnections -= myBoard.connections(Board.BLACK, i, j).length();
            }
        }
    }
    return numConnections;
  }
  
  // Return all possible valid moves from the current game state
  private DList allValidMoves(int color) {
    DList moves = new DList();
    if (nextMoveType(color) == Move.ADD) {
        for (int i =0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Move m = new Move(i, j);
                if (isValidMove(color, m)) {
                    moves.insertBack(m);
                }
            }
        }
        return moves;
    } else {
        // Find pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (myBoard.piece(i, j) == color) {
                    // Check all 
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            Move m = new Move(x, y, i, j);
                            if (isValidMove(color, m)) {
                                moves.insertBack(m);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
     int color;
    if (playerColor == Board.WHITE)
        color = Board.BLACK;
    else
        color = Board.WHITE;
        
    if (isValidMove(color, m)) {
        if (m.moveKind == Move.ADD) {
            myBoard.add(color, m.x1, m.y1);
        } else {
            myBoard.remove(m.x2, m.y2);
            myBoard.add(color, m.x1, m.y1);
        }
        opponentMoves++;
        return true;
    } else {
        return false;
    }
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if (isValidMove(playerColor, m)) {
        if (m.moveKind == Move.ADD) {
            myBoard.add(playerColor, m.x1, m.y1);
        } else {
            myBoard.remove(m.x2, m.y2);
            myBoard.add(playerColor, m.x1, m.y1);
        }
        moves++;
        return true;
    } else {
        return false;
    }
  }
  
  /**
  * Undoes the move, modifying the internal state of this player.
  * This assumes that the move given has already been applied. It is called in the game tree search.
  */
  private void undoMove(int color, Move m) {
    if (m.moveKind == Move.ADD) {
        myBoard.remove(m.x1, m.y1);
    } else {
        myBoard.remove(m.x1, m.y1);
        myBoard.add(color, m.x2, m.y2);
    }
    if (color == playerColor) {
        moves--;
    } else {
        opponentMoves--;
    }
  }
  
  /**
  * nextMoveType() returns the type of the next move for the player of the given color
  * A move is either an add move (if fewer than 10 pieces have been placed) or a step move (after 10 pieces have been placed)
  *
  * @param color The color of the player
  * @return Returns either Move.ADD or Move.STEP.
  */
  public int nextMoveType(int color) {
    if (color == playerColor) {
        if (moves < 10) {
            return Move.ADD;
        } else {
            return Move.STEP;
        }
    } else {
        if (opponentMoves < 10) {
            return Move.ADD;
        } else {
            return Move.STEP;
        }
    }
  }
  
  /**
  * isValidMove returns whether the given move is a valid move on the Player's internal copy of the board.
  * An add move is a valid move if
  * 
  * 1) There is not a piece at that location
  * 2) The location is not one of the four corners
  * 3) The location is in the home rows for white only if the Player's color is white
  * 4) The location is in the home rows for black only if the Player's color is black
  * 5) Placing the piece will not create a cluster of 3 adjacent pieces of the same color 
  * 6) The player has taken < 10 moves
  * 
  * A step move is a valid move if
  *
  * 1) There is a piece of the Player's color at the first location
  * 2) Location one is not a corner, and location 2 is not a corner
  * 3) There is no piece at the second location
  * 4) The second location is in the home rows for a given color only if the Player's color is that color
  * 5) Placing the piece will not create a cluster of 3 adjacent pieces (after removing the piece in the first location)
  * 6) The player has taken >= 10 moves
  *
  * @param m The move to examine
  * @param color The color of the piece to add
  * @return Whether the given move is a valid move
  */
  public boolean isValidMove(int color, Move m) {
    if (color == playerColor) {
        if ((moves >= 10) && (m.moveKind == Move.ADD))
            return false;
        else if ((moves < 10) && (m.moveKind == Move.STEP))
            return false;
    } else {
        if ((opponentMoves >= 10) && (m.moveKind == Move.ADD))
            return false;
        else if ((opponentMoves < 10) && (m.moveKind == Move.STEP))
            return false;
    }
    if (m.moveKind == Move.ADD) {
        int x = m.x1;
        int y = m.y1;
        if (!myBoard.onBoard(x, y))
            return false;
        if (myBoard.isCorner(x, y))
            return false;
        if (myBoard.piece(x, y) != Board.EMPTY) {
            return false;
        }
        if (color == Board.WHITE) {
            if ((y == 0) || (y == 7))
                return false;
        }
        if (color == Board.BLACK) {
            if ((x == 0) || (x == 7))
                return false;
        }
        return !makesCluster(color, x, y);
    } else if (m.moveKind == Move.STEP) {
        int x1 = m.x1;
        int x2 = m.x2;
        int y1 = m.y1;
        int y2 = m.y2;
        if (!myBoard.onBoard(x1, y1) || !myBoard.onBoard(x2, y2))
            return false;
        if (myBoard.isCorner(x1, y1) || myBoard.isCorner(x2, y2))
            return false;
        if (myBoard.piece(x2, y2) != color)
            return false;
        if (myBoard.piece(x1, y1) != Board.EMPTY)
            return false;
         if (color == Board.WHITE) {
            if ((y1 == 0) || (y1 == 7))
                return false;
        }
        if (color == Board.BLACK) {
            if ((x1 == 0) || (x1 == 7))
                return false;
        }
        // check cluster after removing a piece, but put it back before method finishes
        myBoard.remove(x2, y2);
        boolean cluster = makesCluster(color, x1, y1);
        myBoard.add(color, x2, y2);
        return !cluster;
    } else {
        // Better error message here?
        System.err.println("Error: Unexpected move type to isValidMove()");
        return false;
    }
  }
  
  /**
  * Returns whether placing a piece at (x,y) will create a cluster of 3 adjacent chips
  * This method assumes that the location given is valid and empty.
  * 
  * @param x the x-coordinate
  * @param y the y-coordinate
  * @param color the color of the piece added
  * @return Whether placing a piece of the Player's color at that point will make a cluster
  */
  private boolean makesCluster(int color, int x, int y) {
    DList adj = adjacent(color, x, y);
    if (adj.length() >= 2)
        return true;
    if (adj.length() == 0)
        return false;
    int adj_x = ((Coordinate) adj.front().item).getX();
    int adj_y = ((Coordinate) adj.front().item).getY();
    DList twoAway = adjacent(color, adj_x, adj_y);
    if (twoAway.length() > 0)
        return true;
    return false;
  }
  
  /**
  * Returns a list of all cells adjacent to (x,y) of the given color
  * If an invalid coordinate is given, it returns an empty list.
  *
  * @param x the x-coordinate
  * @param y the y-coordinate
  * @return A list of all coordinates that are next to the given color
  */ 
  private DList adjacent(int color, int x, int y) {
    DList adj = new DList();
    for (int i = x - 1; i <= x + 1; i++) {
        for (int j = y - 1; j <= y + 1; j++) {
            if ((i == x) && (j == y))
                continue;
            else if (!myBoard.onBoard(i, j))
                continue;
            else if (myBoard.piece(i, j) == color) {
                adj.insertBack(new Coordinate(i, j));
            }
        }
    }
    return adj;
  }
  
  /**
  * Returns the color of the player's pieces.
  * 
  * @return The color of the player's pieces. Either Board.BLACK or Board.WHITE
  */
  public int getColor() {
    return playerColor;
  }

  /**
  * Returns whether the player of the given color has won the game (according to the internal game state of this player)
  *
  * @param color The color of pieces to check
  * @return Whether there is a network of the given color connecting the two end rows.
  */
  public boolean hasWonGame(int color) {
    if (color == Board.BLACK) {
        // Check the top row
        for (int x = 1; x <= 6; x++) {
            if (myBoard.piece(x, 0) == Board.BLACK) {
                DList networks = NetworkFinder.networksFromStart(x, 0, Board.BLACK, myBoard);
                DListNode n = networks.front();
                while (n != null) {
                    if (((Network) n.item).gameWinningNetwork()) {
                        return true;
                    }
                    n = networks.next(n);
                }
            }
        }
        return false;
    } else {
        // Check the left column
        for (int y = 1; y <= 6; y++) {
            if (myBoard.piece(0, y) == Board.WHITE) {
                DList networks = NetworkFinder.networksFromStart(0, y, Board.WHITE, myBoard);
                DListNode n = networks.front();
                while (n != null) {
                    if (((Network) n.item).gameWinningNetwork()) {
                        return true;
                    }
                    n = networks.next(n);
                }
            }
        }
        return false;
    }
  }
}

