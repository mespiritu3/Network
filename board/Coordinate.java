package board;

public class Coordinate {

    protected int x;   //x-coordinate of piece
    protected int y;   //y-coordinate of piece

    /**
    * Constructs a coordinate
    * 
    * @param a The x-coordinate of the piece
    * @param b The y-coordinate of the piece
    */
    public Coordinate(int a, int b) {
        x = a;
        y = b;
    }
   
    /**
    * Returns the x-coordinate
    */
    public int getX() {
        return x;
    }

    /**
    * Returns the y-coordinate
    */
    public int getY() {
        return y;
    }

    /**
    * Sets the x-coordinate to @param newX
    * 
    * @param newX The new x-coordinate of the piece
    */
    public void setX(int newX) {
        x = newX;
    }

    /**
    * Sets the y-coordinate to @param newY
    * 
    * @param newY The new y-coordinate of the piece
    */
    public void setY(int newY) {
        y = newY;
    }

    /**
    * Returns a string version of class
    */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
