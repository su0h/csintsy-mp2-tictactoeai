import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.application.*;
import javafx.event.*;

import java.util.Random;

public class Controller implements EventHandler<Event>, ChangeListener<String> {
    /**
     * The GUI of the game.
     */
    private View view;
    /**
     * The Model of the game.
     */
    private TicTacToe game;
    /**
     * Boolean value representing if game is over or not
     */
    private boolean gameOver;
    /**
     * Boolean value representing if it is AI's first move
     * this round.
     */
    private boolean AIFirstMove;
    /**
     * Boolean value representing the switching of turns
     * between the AI and the player.
     */
    private boolean AITurn;
    /**
     * Current score of the AI over the Player.
     */
    private int AIScore;
    /**
     * Current score of the Player over the AI.
     */
    private int playerScore;

    /**
     * Constructor for the Controller.
     * 
     * @param view the GUI to be manipulated.
     */
    public Controller (View view) {
        /* Set View */
        this.view = view;
        /* Set EventHandlers */
        this.view.setEventHandlers (this);
        /* Initialize TicTacToe instance */
        game = new TicTacToe ();
    }

    /**
     * Makes the AI place a piece on the board. Coordinates with the StackPane event 
     * in handler() as to whose move it is currently.
     */
    public void AIMove () {
        /* If it is the AI's turn AND the game is not over yet */
        if (AITurn && !gameOver) {
            /* Make AI select move based on intelligence level chosen by the user */
            game.pickAIMove();
            /* Update GUI board state */
            view.updateBoard (game.getBoard ());
            /* Switch turn to Player */
            AITurn = false;
            /* Check if game is over */
            isGameOver ();
        }

        /* If game over */
        if (gameOver) {
            /* Show final board state */
            view.updateBoard (game.getBoard ());
            
            /* If AI Won */ 
            if (game.evaluate() == Integer.MAX_VALUE) {
                view.playLose();
                view.showPlayAgainPrompt ("AI WON! You Lose!");
                AIScore += 1;
            /* If Player Won */
            } else if (game.evaluate() == Integer.MIN_VALUE) {
                view.playWin();
                view.showPlayAgainPrompt ("AI LOST! You Won!");
                playerScore += 1;
            /* If Tie */
            } else if (game.getMovesLeft () == 0) {
                view.playTie();
                view.showPlayAgainPrompt ("It's a Draw!");
            }

            /* Update score board */
            view.updateScore (AIScore, playerScore);
        }
    }

    @Override
    public void changed (ObservableValue<? extends String> ObservableValue, String prev, String curr) {
        /* Start Game Button Manipulation */
        if (isValidNumber (curr)) {
            int currLevelInput = Integer.parseInt (curr);

            if (currLevelInput < 0 || currLevelInput > 4)
                view.setDisableStartBtn (true);
            else view.setDisableStartBtn (false);
        } else view.setDisableStartBtn (true);

        /* TextField Limiter */
        if (view.getAILevelField ().getText ().length () > 1)
            view.getAILevelField ().setText (view.getAILevelField ().getText().substring(0, 1));

    }

    /**
     * Checks if the game is already over. Assigns true to gameOver attribute if game is over; Otherwise false.
     */
    public void isGameOver () {
        if (game.evaluate () == Integer.MAX_VALUE ||
            game.evaluate () == Integer.MIN_VALUE ||
            game.getMovesLeft () == 0)
            gameOver = true;
    }

    /**
     * Checks if a string is an integer within 0 to 4.  
     * 
     * @param   s the string to be checked
     * @return  true if the string is an integer and is within 8 to 64, otherwise false.
     */
    private boolean isValidNumber (String s) {
        int num;

        /* ATTEMPT TO PARSE STRING INTO AN INTEGER */
        try {
            num = Integer.parseInt (s);
        /* CATCH IF NOT AN INTEGER */
        } catch (NumberFormatException nfe) {
            return false;
        }

        /* IF AN INTEGER, CHECK IF WITHIN 8 TO 64 */
        if (num >= 0 && num <= 4)
            return true;
        else return false;
    }

    @Override
    public void handle (Event event) {
        /* IF BUTTON */
        if (event.getSource () instanceof Button) {
            switch (((Button) event.getSource ()).getId ()) {
                /* START GAME BUTTON */
                case "startGame":
                    view.playClick();
                    /* Set game to not over */
                    gameOver = false;

                    /* RNG to determine who will get first move between PLAYER and AI */
                    Random rand = new Random();
                    /* if random number is even, give first move and turn to AI for first round */
                    if (rand.nextInt(100) % 2 == 0) 
                        AIFirstMove = AITurn = true;
                    /* else, give first move and turn to PLAYER for first round */
                    else
                        AIFirstMove = AITurn = false;
        
                    game.resetBoard ();
                    game.setGamePieces (AIFirstMove);
                    game.setAILevel (Integer.parseInt (view.getAILevelField ().getText ()));
                    
                    /* Make the AI move */
                    AIMove ();
                    /* Update score board */
                    view.updateScore (AIScore, playerScore);

                    /* Show updated board state */
                    view.updateBoard (game.getBoard ());
                    /* Show in-game scene */
                    view.showInGame ();
                    break;
                /* PLAY AGIAN BUTTON */
                case "playAgain":
                    view.playClick();
                    /* Close play again prompt */
                    view.closePlayAgainPrompt ();
                    /* Revert first move */
                    AIFirstMove = !AIFirstMove;

                    if (AIFirstMove)
                        AITurn = true;
                    else AITurn = false;

                    /* Set game to not over */
                    gameOver = false;
        
                    /* reset board of TicTacToe instance and set first move */
                    game.resetBoard ();
                    game.setGamePieces (AIFirstMove);

                    /* To clear the board (of previous inputs) */
                    view.updateBoard (game.getBoard ());
                    /* Make AI move first (if AI has first move) */
                    AIMove ();
                    break;
                case "dontPlayAgain":
                    view.playClick();
                    /* Close prompt */
                    view.closePlayAgainPrompt ();
                    /* Go back to main menu scene */
                    view.showMainMenu ();
                    break;
                case "backToMainMenu":
                    view.playClick();
                    view.showMainMenu ();
                    break;
            }
        /* IF STACKPANE (IN-GAME) */
        } else if (event.getSource () instanceof StackPane) {
            boolean validMove = true;
            /* If game is not over just yet */
            if (!gameOver) {
                /* Get the ID of the clicked stack pane (0 0, 0 1, 0 2, etc.) */
                /* Parse StackPane ID */
                String[] parts = ((StackPane) event.getSource ()).getId ().trim ().split (" ");
                validMove = isValidMove (parts);

                if (validMove) {
                    if (!AIFirstMove)
                        view.playX();
                    else
                        view.playO();
                    /* Save player move to board */
                    game.getBoard()[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = game.getPlayer ();
                    /* Update board state */
                    view.updateBoard (game.getBoard ());
                    /* Return turn to AI */
                    AITurn = true;

                    /* Check if game is over */
                    isGameOver ();
                }
            }
            
            if (validMove) {
                /* Make AI move (if game not over) */
                AIMove ();
            }
        }
    }

    private boolean isValidMove (String[] move) {
        return game.getBoard ()[Integer.parseInt (move[0])][Integer.parseInt (move[1])] == TicTacToe.EMPTY;
    }
}
