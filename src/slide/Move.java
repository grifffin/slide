package slide;

import java.io.Serializable;

/**
 * This class represents a move in the game. It only represents the positions of
 * the two tiles that switched places to form the move. Because of this, if one
 * move object is executed twice, the board stays the same.
 *
 * @author Griffin
 */
public class Move implements Serializable
{

    /**
     * X1 and Y1 are the coordinates of the first tile, and X2 and Y2 are the
     * coordinates of the second tile.
     */
    private final int X1, Y1, X2, Y2;

    /**
     * Initializes the fields.
     * @param x1 Horizontal index of the first tile
     * @param y1 Vertical index of the first tile
     * @param x2 Horizontal index of the second tile
     * @param y2 Vertical index of the second tile
     */
    public Move(int x1, int y1, int x2, int y2)
    {
        this.X1 = x1;
        this.Y1 = y1;
        this.X2 = x2;
        this.Y2 = y2;
    }

    public int getX1()
    {
        return X1;
    }

    public int getY1()
    {
        return Y1;
    }

    public int getX2()
    {
        return X2;
    }
    
    public int getY2()
    {
        return Y2;
    }
}
