/* NetworkFinder.java */

package board;

import list.*;
import player.*;

/**
* A class of static methods dedicated to finding paths in a given Board.
*/
public class NetworkFinder {

    /**
    * networksFromStart returns a DList of all Networks that orginate from the given coordinate (x,y)
    * The DList contains all possible networks in the deepest possible way. 
    * More formally, every network in the DList ends in a piece that has no more possible pieces which it could continue the network from.
    * This method assumes that there is a piece of the given color at point (x,y) in the given board
    *
    * @param x the x-coordinate
    * @param y the y-coordinate
    * @param color The color of the pieces to check
    * @param board the current game state
    * @return A DList where each item is a Network that starts at (x, y)
    */
    public static DList networksFromStart(int x, int y, int color, Board board) {
        boolean[][] flags = new boolean[8][8];
        // Initialize to false
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flags[i][j] = false;
            }
        }
        return networksFromStart(x, y, color, board, flags, Direction.NONE);
    }
    
    /** A helper method with additional arguments that help with recursion.
    *
    * @param x the x-coordinate
    * @param y the y-coordinate
    * @param p the Player whose pieces we check for the network
    * @param board the current game state
    * @param visited An array that represented which pieces have been visited previously
    * @pararm prevDirec The direction that was travelled to reach the current location
    * @return A DList where each item is a Network that starts at (x, y)
    */
    private static DList networksFromStart(int x, int y, int color, Board board, boolean[][] visited, Direction prevDirec) {
        visited[x][y] = true;
        DList next = board.connections(color, x, y);
        DList networks = new DList();
        DListNode node = next.front();
        while (node != null) {
            Coordinate xy = (Coordinate) node.item;
            Direction nextDirec = directionTo(x, y, xy.x, xy.y);
            // For each coordinate we want to check, compute subnetworks starting from that point
            if (!visited[xy.x][xy.y] && (nextDirec != prevDirec)) {
                DList networksFromNext = networksFromStart(xy.x, xy.y, color, board, visited, nextDirec);
                DListNode networkNode = networksFromNext.front();
                // Adding networks
                while (networkNode != null) {
                    Network n = (Network) networkNode.item;
                    n.addToStart(new Coordinate(x, y));
                    networks.insertFront(n);
                    networkNode = networksFromNext.next(networkNode);
                }
            }
            node = next.next(node);
        }
        // If no subnetworks, then this is a dead-end. Add a network containing just itself
        if (networks.isEmpty()) {
            Network n = new Network();
            n.addToStart(new Coordinate(x,y));
            networks.insertFront(n);
        }
        visited[x][y] = false;
        return networks;
    }
    
    /**
    * Returns the direction of the path from (x1, y1) to (x2, y2)
    * Assumes that the two points lie on a straight line, and this straight line is one of the 8 directions.
    * 
    * @param x1, The first x coordinate
    * @param y1, The first ycoordinate
    * @param x2, The second x coordinate
    * @param y2, The second y coordinate
    * @return One of the possible Directions
    */
    private static Direction directionTo(int x1, int y1, int x2, int y2) {
        if ((y2 < y1) && (x2 > x1))
            return Direction.NORTHEAST;
        if ((y2 > y1) && (x2 > x1))
            return Direction.SOUTHEAST;
        if ((y2 > y1) && (x2 < x1))
            return Direction.SOUTHWEST;
        if ((y2 < y1) && (x2 < x1))
            return Direction.NORTHWEST;
        if (y2 < y1)
            return Direction.NORTH;
        if (x2 > x1)
            return Direction.EAST;
        if (y2 > y1)
            return Direction.SOUTH;
        if (x2 < x1)
            return Direction.WEST;
        return Direction.NONE;
    }
    
    // Test code, can be ignored
    public static void main(String[] args) {
        Board testBoard = new Board();
        testBoard.clearBoard();
        testBoard.add(Board.BLACK,1,0);
        testBoard.add(Board.BLACK,5,0);
        testBoard.add(Board.BLACK,6,2);
        testBoard.add(Board.BLACK,1,3);
        testBoard.add(Board.BLACK,4,3);
        testBoard.add(Board.BLACK,4,4);
        testBoard.add(Board.BLACK,2,6);
        testBoard.add(Board.BLACK,4,6);
        testBoard.add(Board.BLACK,4,7);
        testBoard.add(Board.BLACK,6,7);
        testBoard.add(Board.WHITE,3,1);
        testBoard.add(Board.WHITE,5,1);
        testBoard.add(Board.WHITE,2,2);
        testBoard.add(Board.WHITE,5,2);
        testBoard.add(Board.WHITE,0,4);
        testBoard.add(Board.WHITE,2,4);
        testBoard.add(Board.WHITE,7,4);
        testBoard.add(Board.WHITE,5,5);
        testBoard.add(Board.WHITE,1,6);
        testBoard.add(Board.WHITE,6,6);
        System.out.println(testBoard);
        System.out.println("Networks from (0,4)");
        DList networks = networksFromStart(0,4,Board.WHITE, testBoard);
        DListNode n = networks.front();
        while (n != null) {
            System.out.println((Network) n.item);
            System.out.println("Is a game winning network: " + ((Network) n.item).gameWinningNetwork());
            n = networks.next(n);
        }
        System.out.println("\nNetworks from (1,0)");
        networks = networksFromStart(1,0,Board.BLACK, testBoard);
        n = networks.front();
        while (n != null) {
            System.out.println((Network) n.item);
            System.out.println("Is a game winning network: " + ((Network) n.item).gameWinningNetwork());
            n = networks.next(n);
        }
    }
}
