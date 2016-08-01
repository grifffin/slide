# slide
A slide puzzle game

There are currently two interfaces for interacting with the game, one text and one graphical.
Once the graphical interface has all of the functionality of the text one, the text interface
will be removed.

For the text interface:
To use, first enter the dimensions of the puzzle, horizontal then vertical,
separated by a space. Creating a puzzle smaller than 3x2 could cause problems,
including the program crashing. To make a move, select the tile that you want to shift by
entering its coordinates, horizontal then vertical, separated by a space. The
coordinates start at zero, not one. These commands can also be used in any
order until the puzzle is solved: Undo - undoes your last action. You can use
this more than once to undo more than one action. Save - This saves the
current state of the puzzle and your previous moves to a file with the
extension .sld. The name of the file is prompted from you. Load - This
overwrites the current state of the board with the state serialized in an
.sld. You must create a new puzzle before you can load a saved one.

For the graphical interface:
Click File, New Puzzle any time to get a new, random puzzle. It's dimensions
will be prompted with a popup window. Make moves by pressing the buttons.
Undo moves with Edit, Undo.
