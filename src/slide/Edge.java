package slide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * A text based interface for the slide puzzle. To use, first enter the
 * dimensions of the puzzle, horizontal then vertical, separated by a space. To
 * make a move, select the tile that you want to shift by entering its
 * coordinates, horizontal then vertical, separated by a space. The coordinates
 * start at zero, not one. These commands can also be used in any order until
 * the puzzle is solved: Undo - undoes your last action. You can use this more
 * than once to undo more than one action. Save - This saves the current state
 * of the puzzle and your previous moves to a file with the extension .sld. The
 * name of the file is prompted from you. Load - This overwrites the current
 * state of the board with the state serialized in an .sld. You must create a
 * new puzzle before you can load a saved one.
 *
 * @author Griffin
 */
public class Edge
{

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the dimensions of the puzzle:");
        String[] dimensions = input.nextLine().split(" ");
        Board board = new Board(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
        board.Shuffle();
        System.out.println("\n" + board + "\n");
        boolean solved = false;
        while (!solved)
        {
            String nextLine = input.nextLine();
            switch (nextLine)
            {
                case "Undo":
                    if (board.movesEmpty())
                    {
                        System.out.println("Can't undo");
                    } else
                    {
                        board.undo();
                        System.out.println("\n" + board + "\n");
                    }
                    break;
                case "Save":
                    System.out.println("What filename?");
                    try
                    {
                        String filename = input.nextLine() + ".sld";
                        File file = new File(filename);
                        FileOutputStream fout = new FileOutputStream(file);
                        ObjectOutputStream out = new ObjectOutputStream(fout);
                        out.writeObject(board);
                        out.flush();
                    } catch (IOException e)
                    {
                        System.out.println("Exception saving data: " + e.getMessage());
                    }
                    System.out.println("Success");
                    break;
                case "Load":
                    System.out.println("What filename?");
                    try
                    {
                        String filename = input.nextLine() + ".sld";
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
                        board = (Board) in.readObject();
                        System.out.println("Success");
                        System.out.println(board + "\n");
                    } catch (IOException | ClassNotFoundException e)
                    {
                        System.out.println("Exception loading data: " + e.getMessage());
                    }
                    break;
                default:
                    dimensions = nextLine.split(" ");
                    solved = board.queryTile(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
                    System.out.println("\n" + board + "\n");
                    break;
            }
        }
        System.out.println("You solved it!");
    }
}
