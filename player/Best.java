/* Best.java */

package player;

/**
* A class for holding information about a Move and the score it leads to. Only a container for data.
* Used in searching the game tree.
*/
class Best {
    protected Move move; // The move to apply to the board
    protected int score; // The score acheived by applying that move
}
