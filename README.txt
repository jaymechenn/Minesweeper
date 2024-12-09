=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Array: A game has two 2D arrays, one representing the hidden states of each field and another representing
     the visible states of each field. The fields' states are stored as integers:
        -1: bunny (mine, uncovered)
        -2: covered
        -3: flagged
        >0: uncovered, number of many adjacent bunnies

  2. Collection: A Stack of 2D Arrays stores the visible state of the board after each move. This allows for an undo
     button, which simply pops the most recently stored 2D array from the stack to restore the previous game state.

  3. Recursion: When a field with no adjacent bunnies (a "flower field") is revealed, the program searches through
     neighboring fields recursively to find and uncover all adjacent flower fields.

  4. JUnit testable component: The Gardensweeper Model handles all logic independently, while View and Run handle
     display. Thus, Model is fully runnable and testable, useful for ensuring that the game logic is operating
     correctly.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

Provide an overview of each of the classes in your code, and what their function is in the overall game.
    Model: Model handles the logic of the game.
        It begins the game by setting up the board with setUpBoard and countSurroundingMines.
        It updates the internal state of the game through methods such as playTurn, handleTile, and playFlag.
        It has methods reset and undo to allow for the reset and Undo buttons. Undo is made possible by storing a stack
        of previous game states as 2D arrays.
        Model can operate independently, allowing us to test the logic of the game directly without rendering anything.
    Run: Run handles the GUI.
        Run contains the top-level frames and widgets.
        It features JFrames, JLabels, and JPanels for the display.
    View: View handles communication between the user and model.
        View uses MouseAdapters and KeyAdapters to collect user input, which it feeds to Model to handle.
        After Model handles the logic, View repaints to reflect the new internal state of the game.

Were there any significant stumbling blocks while you were implementing your game (related to your design, or otherwise)?
    I originally planned on using only one 2D Array to store the game state, but I ran into issues because hidden mines
    would look identical to hidden normal plots. Eventually I created two 2D arrays, one representing the hidden state
    of the game and the other representing the visible state.
    I also struggled with implementing the undo button, which originally was not storing the correct board, then stored
    a shallow version that would continue updating, until the 2D array was stored correctly in Model.
    Finally, I wanted to implement the condition that the first plot the user uncovers is empty, with no adjacent mines.
    This was difficult because I was generating the board before the user made their first move. I had to rewrite
    setUpBoard in Model to take in the row and column of the first click, then generate a mines that avoid that plot.

Evaluate your design. Is there a good separation of functionality? How well is private state encapsulated? What would
you refactor, if given the chance?
    My design illustrates separation of functionality because it separates the game logic from the display. The private
    state is fully encapsulated inside Model, whose private variables, including the 2D arrays storing the state of the
    game, cannot be tampered with.
    Given the chance, I would love to refactor by creating objects for Board and Tile, which may allow for cleaner code.
    I could then try storing the entire game in one 2D Array, or Board, of Tiles, which could have booleans denoting
    whether they are uncovered.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
