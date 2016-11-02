package board;

import list.*;

/**
 *  Holds all coordinates of a piece which together form a network
 */

public class Network {

    public int length = 0;                  //length of Network
    private DList list = new DList();   //holds all coordinates of pieces of a network

     /**
    * Adds a piece to the front of a network
    * 
    * @param c The coordinate of the piece
    */
    public void addToStart(Coordinate c) {
        list.insertFront(c);
        length = list.length();
    }

    
    /**
    * Adds a piece to the back of a network
    * 
    * @param c The coordinate of the piece
    */
    public void addToEnd(Coordinate c) {
        list.insertBack(c);
        length = list.length();
    }
    
     /**
    * Returns list of coordinates
    *
    * @return list
    */
    public DList getCoorList() {
        return list;
    }
    
      /**
    * Determines if network has 6 coordinates and connects from the homerow to endrow
    *
    * @return True or false
    */
    public boolean gameWinningNetwork() {
        if (length < 6) {
            return false;
        }
        Coordinate first = (Coordinate) list.front().item;
        Coordinate last = (Coordinate) list.back().item;
        if ((first.getX() == 0) && (last.getX() == 7)) {
            return true;
        }
        if ((first.getY() == 0) && (last.getY() == 7)) {
            return true;
        }
        return false;
    }
    
     /**
    * Returns a string version of class
    */
    public String toString() {
        return list.toString();
    }
}
