package serializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Utility class for the serialization of Boards.
 *
 * @author Griffin
 */
public class Serializer
{

    public static void serializeBoard(File file, slide.Board board)
    {
        try
        {
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(board);
            out.flush();
        } catch (IOException e)
        {
            System.out.println("Exception saving data: " + e.getMessage());
        }
    }

    public static void XMLSerializeBoard(File file, slide.Board board)
    {
        try
        {
            PrintWriter out = new PrintWriter(file);
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
            out.append(board.toXML());
            out.flush();
        } catch (IOException e)
        {
            System.out.println("Exception saving data: " + e.getMessage());
        }
    }
}
