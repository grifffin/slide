package slide;

import java.io.Serializable;
import java.util.Stack;
import java.util.Random;

/**
 * This class is a representation of the board and a list of the most recent
 * moves.
 *
 * @author Griffin
 */
public class Board implements Serializable
{

    /**
     * Tiles is the representation of the board. It is Integer instead of int to
     * allow null to be used for the empty tile. Can be final because of
     * non-transitivity.
     */
    private final Integer[][] tiles;

    /**
     * This stack holds move objects after they are executed. This allows them
     * to be undone in the reverse order they were originally executed in.
     */
    private Stack<Move> moves;

    /**
     * Instantiates a new 2d Integer array for tiles and a new Stack for moves.
     * tiles is initialized in its solved state. Use shuffle() to randomize it.
     *
     * @param width - the width of the board.
     * @param height - the height of the board.
     */
    public Board(int width, int height)
    {
        tiles = new Integer[width][height];
        for (int i = 1; i < width * height; ++i)
        {
            tiles[(i - 1) % width][(i - 1) / width] = i;
        }
        moves = new Stack<>();
    }

    /**
     * Switches two tiles and adds the move to moves.
     *
     * @param move - defines the two tiles being swapped.
     */
    private void executeMove(Move move)
    {
        Integer temp = tiles[move.getX1()][move.getY1()];
        tiles[move.getX1()][move.getY1()] = tiles[move.getX2()][move.getY2()];
        tiles[move.getX2()][move.getY2()] = temp;
        moves.push(move);
    }

    /**
     * Shuffles the tiles on the Board. It re-shuffles if a solved board is
     * generated. Then uses checkSolvable() and swaps the first two non-null
     * tiles on the first row if checkSolvable() returns false. This saves time
     * because the board doesn't have to be shuffled anywhere from one to
     * infinity more times. However, I think swapping instead of re-shuffling
     * could give the player a slight edge. This method also clears moves,
     * because this method randomly swaps tiles with executeMove().
     */
    public void Shuffle()
    {
        while (checkSolved())
        {
            Random rand = new Random();
            for (int i = 0; i < this.tiles.length; ++i)
            {
                for (int j = 0; j < this.tiles[0].length; ++j)
                {
                    executeMove(new Move(
                            i,
                            j,
                            rand.nextInt(this.tiles.length),
                            rand.nextInt(this.tiles[0].length)));
                }
            }
        }
        //This can throw an exception if the width of the board is less than 3
        if (!checkSolvable())
        {
            if (tiles[0][0] == null)
            {
                executeMove(new Move(1, 0, 2, 0));
            } else if (tiles[0][1] == null)
            {
                executeMove(new Move(0, 0, 2, 0));
            } else
            {
                executeMove(new Move(0, 0, 1, 0));
            }
        }
        moves = new Stack<>();
    }

    /**
     * Checks the equality of tiles of two Boards. Does not check moves.
     *
     * @param other - the other board object
     * @return - true if they are equal, false if not
     */
    @Override
    public boolean equals(Object other)
    {
        boolean rtnbool = true;
        if (other instanceof Board)
        {
            for (int i = 0; i < this.tiles.length; ++i)
            {
                for (int j = 0; (j < this.tiles[0].length) && (rtnbool == true); ++j)
                {
                    if (this.tiles[i][j] != null)
                    {
                        if (!(this.tiles[i][j].equals(((Board) other).getTiles()[i][j])))
                        {
                            rtnbool = false;
                        }
                    } else if (((Board) other).getTiles()[i][j] != null)
                    {
                        rtnbool = false;
                    }
                }
            }
        } else
        {
            rtnbool = false;
        }
        return rtnbool;
    }

    /**
     * I only put this here so NetBeans would stop giving me a warning message.
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        return 0;
    }

    /**
     * Checks to see if the board is in its solved state. The solved state is,
     * starting with 1 in the top left corner, all of the numbers, in order,
     * left to right and top to bottom (like how text is read), with the empty
     * space (the null tile) in the bottom right corner.
     *
     * @return - true if this is in its solved state, false if not
     */
    public boolean checkSolved()
    {
        return this.equals(new Board(tiles.length, tiles[0].length));
    }

    /**
     * The basis of user interaction. A tile is passed in, and if it is next to
     * the empty space (the null tile) the two switch positions (the equivalent
     * of sliding a tile to the empty space in real life). Also, if a move is
     * made, checkSolved() is called and its result is returned.
     *
     * @param x - the horizontal index of the tile.
     * @param y - the vertical index of the tile.
     * @return - True if a move was made that solved the puzzle, false if not.
     */
    public boolean queryTile(int x, int y)
    {
        boolean rtnbool = false;
        if (x > 0 && tiles[x - 1][y] == null)
        {
            executeMove(new Move(x, y, x - 1, y));
            rtnbool = checkSolved();
        }
        if (y > 0 && tiles[x][y - 1] == null)
        {
            executeMove(new Move(x, y, x, y - 1));
            rtnbool = checkSolved();
        }
        if (x < tiles.length - 1 && tiles[x + 1][y] == null)
        {
            executeMove(new Move(x, y, x + 1, y));
            rtnbool = checkSolved();
        }
        if (y < tiles[0].length - 1 && tiles[x][y + 1] == null)
        {
            executeMove(new Move(x, y, x, y + 1));
            rtnbool = checkSolved();
        }
        return rtnbool;
    }

    /**
     * Executes the move on top of moves. i.e. undoes the most recent move, due
     * to moves being able to undo themselves. Because executeMoves() is what
     * pushes moves onto the stack, a move is popped, then pushed back on, and
     * must be popped again.
     */
    public void undo()
    {
        executeMove(moves.pop());
        moves.pop();
    }

    /**
     * Returns a string representation of the board. Every index is printed in
     * the shape of the board. Leading zeroes are used to align columns and the
     * empty space (the null tile) is represented by whitespace.
     *
     * @return
     */
    @Override
    public String toString()
    {
        StringBuilder rtnstr = new StringBuilder();
        int numberWidth = Integer.toString((tiles.length * tiles[0].length) - 1).length();
        for (int j = 0; j < tiles[0].length; ++j)
        {
            for (int i = 0; i < tiles.length; ++i)
            {
                if (tiles[i][j] != null)
                {
                    rtnstr.append(String.format("%0" + numberWidth + "d ", tiles[i][j]));
                } else
                {
                    rtnstr.append(String.format("%" + numberWidth + "s ", " "));
                }
            }
            //adds a newline after each row execpt after the last row
            if (j != tiles[0].length - 1)
            {
                rtnstr.append("\n");
            }
        }
        return rtnstr.toString();
    }

    /**
     * Checks the solvability of the board using
     * <a href="https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html">this
     * method.</a>
     *
     * @return - true if tiles is solvable, false if not
     */
    private boolean checkSolvable()
    {
        int inversionCount = 0;
        //blankRow is the row with the blank on it
        int blankRow = -1;
        for (int j = 0; j < tiles[0].length; ++j)
        {
            for (int i = 0; i < tiles.length; ++i)
            {
                if (tiles[i][j] != null)
                {
                    //This loop checks the rest of the row tiles[i][j] is on
                    for (int m = i + 1; m < tiles.length; ++m)
                    {
                        if (tiles[m][j] != null && tiles[i][j].compareTo(tiles[m][j]) > 0)
                        {
                            ++inversionCount;
                        }
                    }
                    //These loops checks all the other rows
                    for (int n = j + 1; n < tiles[0].length; ++n)
                    {
                        for (int m = 0; m < tiles.length; ++m)
                        {
                            if (tiles[m][n] != null && tiles[i][j].compareTo(tiles[m][n]) > 0)
                            {
                                ++inversionCount;
                            }
                        }
                    }
                } else
                {
                    blankRow = j;
                }
            }
        }
        boolean rtnbool;
        /* The row of the blank counting from the bottom starts the count at 1,
        meaning if the blank was on the bottom row, its on the "first" row
        from the bottom. That's okay because the length of an array is 1 more
        than its last index */
        if (tiles.length % 2 == 0 && (tiles[0].length - blankRow) % 2 == 0)
        {
            rtnbool = inversionCount % 2 == 1;
        } else
        {
            rtnbool = inversionCount % 2 == 0;
        }
        return rtnbool;
    }

    /**
     * Checks if moves is empty.
     *
     * @return - true if moves is empty, false if not.
     */
    public boolean movesEmpty()
    {
        return moves.empty();
    }

    public Integer[][] getTiles()
    {
        return tiles;
    }
}
