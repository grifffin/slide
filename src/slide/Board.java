package slide;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is a representation of the board and a list of the most recent
 * turns.
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
    private Integer[][] tiles;

    /**
     * This stack holds move objects after they are executed. This allows them
     * to be undone in the reverse order they were originally executed in. It
     * stores a stack of stacks so that one action from the user can encompass
     * several Moves.
     */
    private ArrayDeque<ArrayDeque<Move>> turns;

    /**
     * Instantiates a new 2d Integer array for tiles and a new Stack for turns.
     * tiles is initialized in its solved state. Use shuffle() to randomize it.
     *
     * @param width The width of the board
     * @param height The height of the board
     */
    public Board(int width, int height)
    {
        this.tiles = new Integer[width][height];
        for (int i = 1; i < width * height; ++i)
        {
            this.tiles[(i - 1) % width][(i - 1) / width] = i;
        }
        this.turns = new ArrayDeque<>();
    }

    /**
     * Switches two tiles.
     *
     * @param move Defines the two tiles being swapped
     */
    private void executeMove(Move move)
    {
        Integer temp = this.tiles[move.getX1()][move.getY1()];
        this.tiles[move.getX1()][move.getY1()] = this.tiles[move.getX2()][move.getY2()];
        this.tiles[move.getX2()][move.getY2()] = temp;
    }

    /**
     * Executes the turns required to move a group of tiles in the direction of
     * the blank space (the null tile). To work, the position of the blank space
     * and a designated tile are passed in. All the tiles between these two, and
     * the designated tile, move one position toward the blank space and the
     * blank space gets moved to the position of the designated tile. This
     * entire shift is returned as a stack of turns, to be added to turns, so
     * that this whole move can be undone with only one call to undo(). This
     * method will return an empty stack if the two passed in positions are on a
     * separate row and a separate column or they are the same tile. i.e. The
     * two passed in tiles must shared one and only one coordinate(logical XOR).
     *
     * @param x
     * @param y
     * @param blankX
     * @param blankY
     * @return A stack of every move performed
     */
    private ArrayDeque<Move> multiMove(int x, int y, int blankX, int blankY)
    {
        ArrayDeque<Move> rtnval = new ArrayDeque<>();
        if (x == blankX)
        {
            if (y < blankY)
            {
                for (int j = blankY; j > y; --j)
                {
                    Move m = new Move(x, j, x, j - 1);
                    executeMove(m);
                    rtnval.push(m);
                }
            } else
            {
                for (int j = blankY; j < y; ++j)
                {
                    Move m = new Move(x, j, x, j + 1);
                    executeMove(m);
                    rtnval.push(m);
                }
            }
        } else if (y == blankY)
        {
            if (x < blankX)
            {
                for (int i = blankX; i > x; --i)
                {
                    Move m = new Move(i, y, i - 1, y);
                    executeMove(m);
                    rtnval.push(m);
                }
            } else
            {
                for (int i = blankX; i < x; ++i)
                {
                    Move m = new Move(i, y, i + 1, y);
                    executeMove(m);
                    rtnval.push(m);
                }
            }
        }
        return rtnval;
    }

    /**
     * Shuffles the tiles on the Board. It re-shuffles if a solved board is
     * generated. Then uses checkSolvable() and swaps the first two non-null
     * tiles on the first row if checkSolvable() returns false. This saves time
     * because the board doesn't have to be shuffled anywhere from one to
     * infinity more times. However, I think swapping instead of re-shuffling
     * could give the player a slight edge. This method also clears turns,
     * because this method randomly swaps tiles with executeMove().
     */
    public void shuffle()
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
        /* This can throw an exception if the width of the board is less than 3.
         * I put a check in to reshuffle instead of swap if the width is less
         * than 3, but now it sometimes causes stack overflows. */
        if (!checkSolvable())
        {
            if (this.tiles[0][0] == null)
            {
                if (this.tiles.length < 3)
                {
                    shuffle();
                } else
                {
                    executeMove(new Move(1, 0, 2, 0));
                }
            } else if (this.tiles[1][0] == null)
            {
                if (this.tiles.length < 3)
                {
                    shuffle();
                } else
                {
                    executeMove(new Move(0, 0, 2, 0));
                }
            } else
            {
                executeMove(new Move(0, 0, 1, 0));
            }
        }
        this.turns = new ArrayDeque<>();
    }

    /**
     * Checks the equality of tiles of two Boards. Does not check turns.
     *
     * @param other The other board object
     * @return True if they are equal, false if not
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
     * @return True if this is in its solved state, false if not
     */
    public boolean checkSolved()
    {
        return this.equals(new Board(this.tiles.length, this.tiles[0].length));
    }

    /**
     * The basis of user interaction. A tile is passed in, and if it is next to
     * the empty space (the null tile) the two switch positions (the equivalent
     * of sliding a tile to the empty space in real life). If the queried tile
     * isn't next to the blank space, a check is made to see if multiMove can be
     * executed. Also, if a move is made, checkSolved() is called and its result
     * is returned.
     *
     * @param x The horizontal index of the tile
     * @param y The vertical index of the tile
     * @return True if a move was made that solved the puzzle, false if not
     */
    public boolean queryTile(int x, int y)
    {
        boolean rtnbool = false;
        if (x > 0 && this.tiles[x - 1][y] == null)
        {
            Move m = new Move(x, y, x - 1, y);
            executeMove(m);
            ArrayDeque<Move> s = new ArrayDeque();
            s.push(m);
            this.turns.push(s);
            rtnbool = checkSolved();
        } else if (y > 0 && this.tiles[x][y - 1] == null)
        {
            Move m = new Move(x, y, x, y - 1);
            executeMove(m);
            ArrayDeque<Move> s = new ArrayDeque();
            s.push(m);
            this.turns.push(s);
            rtnbool = checkSolved();
        } else if (x < this.tiles.length - 1 && this.tiles[x + 1][y] == null)
        {
            Move m = new Move(x, y, x + 1, y);
            executeMove(m);
            ArrayDeque<Move> s = new ArrayDeque();
            s.push(m);
            this.turns.push(s);
            rtnbool = checkSolved();
        } else if (y < this.tiles[0].length - 1 && this.tiles[x][y + 1] == null)
        {
            Move m = new Move(x, y, x, y + 1);
            executeMove(m);
            ArrayDeque<Move> s = new ArrayDeque();
            s.push(m);
            this.turns.push(s);
            rtnbool = checkSolved();
        } else
        {
            int blankX = -1;
            int blankY = -1;
            for (int j = 0; j < this.tiles[0].length; ++j)
            {
                for (int i = 0; i < this.tiles.length && blankX == -1; ++i)
                {
                    if (this.tiles[i][j] == null)
                    {
                        blankX = i;
                        blankY = j;
                    }
                }
            }
            if ((x == blankX) ^ (y == blankY))
            {
                this.turns.push(multiMove(x, y, blankX, blankY));
                rtnbool = checkSolved();
            }
        }
        return rtnbool;
    }

    /**
     * Executes the move on top of turns. i.e. undoes the most recent move, due
     * to turns being able to undo themselves. Because executeMoves() is what
     * pushes turns onto the stack, a move is popped, then pushed back on, and
     * must be popped again.
     */
    public void undo()
    {
        ArrayDeque<Move> undoDeque = this.turns.pop();
        while (!undoDeque.isEmpty())
        {
            executeMove(undoDeque.pop());
        }
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
        int numberWidth = Integer.toString((this.tiles.length * this.tiles[0].length) - 1).length();
        for (int j = 0; j < this.tiles[0].length; ++j)
        {
            for (int i = 0; i < this.tiles.length; ++i)
            {
                if (this.tiles[i][j] != null)
                {
                    rtnstr.append(String.format("%0" + numberWidth + "d ", this.tiles[i][j]));
                } else
                {
                    rtnstr.append(String.format("%" + numberWidth + "s ", " "));
                }
            }
            //adds a newline after each row execpt after the last row
            if (j != this.tiles[0].length - 1)
            {
                rtnstr.append("\n");
            }
        }
        return rtnstr.toString();
    }

    /**
     * Returns a string representation of this board in XML format. The tags
     * are: board - The board, tiles - The tiles, dimensions - The dimensions of
     * tiles, row - A row of tiles, turns - The turns, turn - A single turn,
     * move - One move, x1 - The x1 of a move, y1 - The y1 of a move, x2 - The
     * x2 of a move, y2 - The y2 of a move
     *
     * @return A XML formatted string representation of this board
     */
    public String toXML()
    {
        StringBuilder strbld = new StringBuilder();
        strbld.append("<board>\r\n");
        strbld.append("    <tiles>\r\n");
        strbld.append("        <dimensions>");
        strbld.append(this.tiles.length);
        strbld.append(",");
        strbld.append(this.tiles[0].length);
        strbld.append("</dimensions>\r\n");
        for (int j = 0; j < tiles[0].length; ++j)
        {
            strbld.append("        <row>");
            for (int i = 0; i < this.tiles.length; ++i)
            {
                strbld.append(this.tiles[i][j]);
                if (i < this.tiles.length - 1)
                {
                    strbld.append(",");
                }
            }
            strbld.append("</row>\r\n");
        }
        strbld.append("    </tiles>\r\n");
        strbld.append("    <turns>\r\n");
        ArrayDeque<ArrayDeque<Move>> newTurns = (ArrayDeque<ArrayDeque<Move>>) this.turns.clone();
        while (!newTurns.isEmpty())
        {
            ArrayDeque<Move> moves = newTurns.pop();
            strbld.append("        <turn>\r\n");
            while (!moves.isEmpty())
            {
                Move move = moves.pop();
                strbld.append("            <move>\r\n");
                strbld.append("                <x1>");
                strbld.append(move.getX1());
                strbld.append("</x1>\r\n");
                strbld.append("                <y1>");
                strbld.append(move.getY1());
                strbld.append("</y1>\r\n");
                strbld.append("                <x2>");
                strbld.append(move.getX2());
                strbld.append("</x2>\r\n");
                strbld.append("                <y2>");
                strbld.append(move.getY2());
                strbld.append("</y2>\r\n");
                strbld.append("            </move>\r\n");
            }
            strbld.append("        </turn>\r\n");
        }
        strbld.append("    </turns>\r\n");
        strbld.append("</board>");
        return strbld.toString();
    }

    /**
     * Edits the fields of this object based on an XML String.
     * 
     * @param input A Scanner scanning an XML formatted string.
     */
    public void fromXML(Scanner input)
    {
        while (input.hasNext())
        {
            if (input.next().equals("board"))
            {
                String next = input.next();
                while (!next.equals("/board"))
                {
                    switch (next)
                    {
                        case "tiles":
                            next = input.next();
                            int rowNum = 0;
                            while (!next.equals("/tiles"))
                            {
                                if (next.equals("dimensions"))
                                {
                                    String[] dimensions = input.next().split(",");
                                    this.tiles = new Integer[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
                                } else if (next.equals("row"))
                                {
                                    String[] numbers = input.next().split(",");
                                    for (int i = 0; i < numbers.length; ++i)
                                    {
                                        if (!numbers[i].equals("null"))
                                        {
                                            this.tiles[i][rowNum] = Integer.parseInt(numbers[i]);
                                        }
                                    }
                                    ++rowNum;
                                }
                                next = input.next();
                            }
                            break;
                        case "turns":
                            this.turns = new ArrayDeque();
                            next = input.next();
                            while (!next.equals("/turns"))
                            {
                                ArrayDeque<Move> moves = null;
                                if (next.equals("turn"))
                                {
                                    moves = new ArrayDeque();
                                    while (!next.equals("/turn"))
                                    {
                                        Move move = null;
                                        if (next.equals("move"))
                                        {
                                            int x1 = -1;
                                            int y1 = -1;
                                            int x2 = -1;
                                            int y2 = -1;
                                            while (!next.equals("/move"))
                                            {
                                                switch (next)
                                                {
                                                    case "x1":
                                                        x1 = Integer.parseInt(input.next());
                                                        break;
                                                    case "y1":
                                                        y1 = Integer.parseInt(input.next());
                                                        break;
                                                    case "x2":
                                                        x2 = Integer.parseInt(input.next());
                                                        break;
                                                    case "y2":
                                                        y2 = Integer.parseInt(input.next());
                                                        break;
                                                }
                                                if (x1 != -1 && y1 != -1 && x2 != -1 && y2 != -1)
                                                {
                                                    move = new Move(x1, y1, x2, y2);
                                                }
                                                next = input.next();
                                                if (move != null)
                                                {
                                                    moves.addLast(move);
                                                }
                                            }
                                        }
                                        next = input.next();
                                    }
                                }
                                if (moves != null)
                                {
                                    this.turns.addLast(moves);
                                }
                                next = input.next();
                            }
                            break;
                    }
                    next = input.next();
                }
            }
        }
    }

    /**
     * Checks the solvability of the board using
     * <a href="https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html">this
     * method.</a>
     *
     * @return True if tiles is solvable, false if not
     */
    private boolean checkSolvable()
    {
        int inversionCount = 0;
        //blankRow is the row with the blank on it
        int blankRow = -1;
        for (int j = 0; j < this.tiles[0].length; ++j)
        {
            for (int i = 0; i < this.tiles.length; ++i)
            {
                if (this.tiles[i][j] != null)
                {
                    //This loop checks the rest of the row tiles[i][j] is on
                    for (int m = i + 1; m < this.tiles.length; ++m)
                    {
                        if (this.tiles[m][j] != null && this.tiles[i][j].compareTo(this.tiles[m][j]) > 0)
                        {
                            ++inversionCount;
                        }
                    }
                    //These loops checks all the other rows
                    for (int n = j + 1; n < this.tiles[0].length; ++n)
                    {
                        for (int m = 0; m < this.tiles.length; ++m)
                        {
                            if (this.tiles[m][n] != null && this.tiles[i][j].compareTo(this.tiles[m][n]) > 0)
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
        if (this.tiles.length % 2 == 0 && (this.tiles[0].length - blankRow) % 2 == 0)
        {
            rtnbool = inversionCount % 2 == 1;
        } else
        {
            rtnbool = inversionCount % 2 == 0;
        }
        return rtnbool;
    }

    /**
     * Checks if turns is empty.
     *
     * @return True if turns is empty, false if not.
     */
    public boolean movesEmpty()
    {
        return this.turns.isEmpty();
    }

    public Integer[][] getTiles()
    {
        return this.tiles;
    }
}
