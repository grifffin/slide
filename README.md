# slide
A slide puzzle game

To play:
Click File, New Puzzle any time to get a new, random puzzle. It's dimensions
will be prompted with a popup window. 
Make moves by pressing the buttons.
Undo moves with Edit, Undo.

Games are seriazable as XML files or files with extension .sld (made up extension for byte serialization).
Not only is the board saved, but the recent moves are too. This allows for the user to undo after saving and
reloading a game.
To save a game, click File, Save Puzzle to .sld or File, Save Puzzle to XML.
Games can be loaded at any time from the file menu. Warning, this overwrites the current game.
