package serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import slide.Board;

/**
 * Utility class for the deserialization of Boards.
 *
 * @author Griffin
 */
public class Deserializer
{

    /**
     * Uses ObjectInputStream to byte deserialize a board from a file saved with
     * Serializer
     *
     * @param file The file to read from
     * @return The board object that was deserialized.
     */
    public static slide.Board deserializeBoard(File file)
    {
        slide.Board rtnBoard;
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            rtnBoard = (Board) in.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            rtnBoard = null;
        }
        return rtnBoard;
    }
    
    /**
     * Creates a board object based on an XML file.
     * 
     * @param file The XML file read from.
     * @return The created board.
     */
    public static slide.Board XMLDeserializeBoard(File file)
    {
        slide.Board rtnBoard = new Board(3, 3);
        try
        {
            Scanner input = new Scanner(file).useDelimiter("<|>");
            rtnBoard.fromXML(input);
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Deserializer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rtnBoard;
    }
}
