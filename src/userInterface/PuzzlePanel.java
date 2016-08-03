package userInterface;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import slide.Board;

/**
 * This panel represents the board. Moves can be made by clicking the buttons.
 * Undo can also be called.
 *
 * @author Griffin
 */
public class PuzzlePanel extends javax.swing.JPanel
{

    /**
     * The board being represented.
     */
    slide.Board board;

    /**
     * main is the SlideUI that is displaying this panel.
     */
    SlideUI main;

    /**
     * Creates new form PuzzlePanel
     *
     * @param main The SlideUI displaying this
     * @param board This board
     */
    public PuzzlePanel(SlideUI main, Board board)
    {
        this.main = main;
        this.board = board;
        setLayout(new java.awt.GridLayout(this.board.getTiles()[0].length,
                this.board.getTiles().length));
        for (int j = 0; j < this.board.getTiles()[0].length; ++j)
        {
            for (int i = 0; i < this.board.getTiles().length; ++i)
            {
                JButton btn = new JButton();
                btn.setActionCommand(i + " " + j);
                if (this.board.getTiles()[i][j] != null)
                {
                    btn.setText(this.board.getTiles()[i][j].toString());
                } else
                {
                    btn.setEnabled(false);
                }
                btn.addActionListener((ActionEvent evt)
                        -> 
                        {
                            buttonClicked(evt);
                });
                this.add(btn);
            }
        }
        main.setUndoEnabled(!checkUndo());
    }
    
    public void save(File file)
    {
        serializer.Serializer.serializeBoard(file, board);
    }
    
    public void XMLSave(File file)
    {
        serializer.Serializer.XMLSerializeBoard(file, board);
    }

    /**
     * Called when a button is clicked. The board is queried to see if a move
     * can be made. Also sets the undo button of main enabled if it can, and
     * calls main's displayVictory if a move was made that solved the puzzle
     *
     * @param evt The action command of each button's ActionEvent has been set
     * to the coordinates of the button.
     */
    private void buttonClicked(ActionEvent evt)
    {
        String[] coordinates = evt.getActionCommand().split(" ");
        boolean solved = board.queryTile(Integer.parseInt(coordinates[0]),
                Integer.parseInt(coordinates[1]));
        drawBoard();
        main.setUndoEnabled(!checkUndo());
        if (solved)
        {
            main.displayVictory();
        }
    }

    /**
     * Resets the text of all the buttons to their corresponding tiles on the
     * board. Disables the button corresponding the the blank space.
     */
    private void drawBoard()
    {
        Component[] buttons = this.getComponents();
        int count = 0;
        for (int j = 0; j < this.board.getTiles()[0].length; ++j)
        {
            for (int i = 0; i < this.board.getTiles().length; ++i)
            {
                JButton btn = (JButton) buttons[count];
                if (this.board.getTiles()[i][j] != null)
                {
                    btn.setText(this.board.getTiles()[i][j].toString());
                    btn.setEnabled(true);
                } else
                {
                    btn.setText("");
                    btn.setEnabled(false);
                }
                btn.setFocusPainted(false);
                ++count;
            }
        }
    }

    /**
     * Checks to see if a move can be undone.
     * 
     * @return True if a move can be done, false if not.
     */
    private boolean checkUndo()
    {
        return board.movesEmpty();
    }

    /**
     * Undoes the most recent move of the board.
     */
    public void undo()
    {
        board.undo();
        drawBoard();
        main.setUndoEnabled(!checkUndo());
    }
}
