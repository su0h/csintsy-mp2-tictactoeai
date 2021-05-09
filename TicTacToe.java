import java.util.*;

public class TicTacToe {
    /**
     * Row and column length (3x3) for TicTacToe board
     */
    public static final int SIZE = 3;
    /**
     * Symbol for empty tile
     */
    public static final char EMPTY = ' ';
    /**
     * Depth counter (used in behavior level 4)
     */
    private static int ctr;
    /**
     * Behavior level value
     */
    private int AILevel;
    /**
     * Symbol for Player tile
     */
    private char PLAYER;
    /**
     * Symbol for AI tile
     */
    private char AI;
    /**
     * TicTacToe board
     */
    private char[][] board;
    /**
     * Indicator of first move 
     * AI - true | Player - false
     */
    private boolean AIFirstMove;

    public TicTacToe () {
        board = new char[SIZE][SIZE];
        
        for (int j = 0; j < SIZE; j++)
            for (int k = 0; k < SIZE; k++)
                board[j][k] = EMPTY;
    }

    /**
     * Gets the symbol (piece) of AI
     * 
     * @return the character symbol indicating the AI's piece
     */
    public char getAI () {
        return AI;
    }

    /**
     * Gets the symbol (piece) of Player
     * 
     * @return the character symbol indicating the Player's piece
     */
    public char getPlayer () {
        return PLAYER;
    }

    /**
     * Gets the number of empty tiles left in the TicTacToe board
     * 
     * @return the number of empty tiles left
     */
    public int getMovesLeft () {
        int moves = 0; 

        // get all empty spaces 
        for (int j = 0; j < SIZE; j++)
            for (int k = 0; k < SIZE; k++)
                if (board[j][k] == EMPTY)
                    moves++;

        return moves; 
    }

    /**
     * Gets the TicTacToe board
     * 
     * @return a 2D array representation of the TicTacToe board
     */
    public char[][] getBoard () {
        return board;
    }

    /**
     * For the AI to choose a move when it has the turn, choice varies depending
     * on behavior level set
     */
    public void pickAIMove () {
        // temporary move set values
        int[] moves = {-1, -1};

        switch (AILevel) {
            /* RANDOM BEHAVIOR AI */
            case 0:
                // indicator of a valid tile to occupy
                boolean validMove = false;
                // random number generator for row and col positions in the 3x3 SIZE state
                Random rand = new Random ();

                // get a valid tile for the agent to randomly occupy
                while (!validMove) {
                    moves[0] = rand.nextInt (3);
                    moves[1] = rand.nextInt (3);

                    // if tile randomly chosen is empty, then it is a valid move
                    if (board[moves[0]][moves[1]] == EMPTY)
                        validMove = true;
                }
                break;
            /* HARD-CODED BEHAVIOR AI */
            case 1:
                // check if AI has a chance to win
                boolean stop = false; 
                // check row and column lines
                for (int k = 0; k < SIZE && !stop; k++) {
                    // ROW: first and second tiles are occupied, third tile is empty
                    if (board[k][0] == board[k][1] && board[k][1] == AI && board[k][2] == EMPTY) {
                        board[k][2] = AI;
                        stop = true; 
                    }
                    // ROW: first and third tiles are occupied, second tile is empty
                    else if (board[k][0] == board[k][2] && board[k][2] == AI && board[k][1] == EMPTY) {
                        board[k][1] = AI;
                        stop = true; 
                    }
                    // ROW: second and third tiles are occupied, first tile is empty
                    else if (board[k][1] == board[k][2] && board[k][2] == AI && board[k][0] == EMPTY) {
                        board[k][0] = AI;
                        stop = true;
                    }
                    // COLUMN: first and second tiles are occupied, third tile is empty 
                    else if (board[0][k] == board[1][k] && board[1][k] == AI && board[2][k] == EMPTY) {
                        board[2][k] = AI;
                        stop = true; 
                    }
                    // COLUMN: first and third tiles are occupied, second tile is empty 
                    else if (board[0][k] == board[2][k] && board[2][k] == AI && board[1][k] == EMPTY) {
                        board[1][k] = AI;
                        stop = true;
                    }
                    // COLUMN: second and third tiles are occupied, first tile is empty 
                    else if (board[1][k] == board[2][k] && board[2][k] == AI && board[0][k] == EMPTY) {
                        board[0][k] = AI;
                        stop = true;
                    }
                }
                // check left diagonal 
                // first and second tiles are occupied, third tile is empty
                if (board[0][0] == board[1][1] && board[1][1] == AI && board[2][2] == EMPTY && !stop) {
                    board[2][2] = AI;
                    stop = true;
                }
                // first and third tiles are occupied, second tile is empty
                else if (board[0][0] == board[2][2] && board[2][2] == AI && board[1][1] == EMPTY && !stop) {
                    board[1][1] = AI;
                    stop = true; 
                }
                // second and third tiles are occupied, first tile is empty 
                else if (board[1][1] == board[2][2] && board[2][2] == AI && board[0][0] == EMPTY && !stop) {
                    board[0][0] = AI;
                    stop = true;
                }
                // check right diagonal
                // first and second tiles are occupied, third tile is empty 
                else if (board[0][2] == board[1][1] && board[1][1] == AI && board[2][0] == EMPTY && !stop) {
                    board[2][0] = AI;
                    stop = true; 
                }
                // first and third tiles are occupied, second tile is empty 
                else if (board[0][2] == board[2][2] && board[2][2] == AI && board[1][1] == EMPTY && !stop) {
                    board[1][1] = AI;
                    stop = true; 
                }
                // second and third tiles are occupied, first tile is empty
                else if (board[1][1] == board[2][0] && board[2][0] == AI && board[0][2] == EMPTY && !stop) {
                    board[0][2] = AI;
                    stop = true; 
                }
                // if AI has no chance of winning yet
                if (!stop) {
                    // if player is attempting to create a fork, block it 
                    if (((board[0][0] == board[2][2] && board[0][0] == PLAYER) || 
                         (board[0][2] == board[2][0] && board[0][2] == PLAYER)) &&
                        board[1][1] == AI && board[0][1] == board[1][0] && board[1][0] == board[1][2] && 
                        board[1][2] == board[2][1] && board[0][1] == EMPTY) {
                        // occupy middle tiles
                        if (board[0][1] == EMPTY) 
                            board[0][1] = AI;
                        else if (board[2][1] == EMPTY) 
                            board[2][1] = AI;
                        else if (board[1][0] == EMPTY) 
                            board[1][0] = AI;
                        else 
                            board[1][2] = AI;
                    }
                    // else, check if player has a chance of winning 
                    // if player will win at either the first row, first column, or left diagonal
                    // and top left corner tile is empty, occupy top left corner tile
                    else if (board[0][0] == EMPTY && ((board[0][1] == PLAYER && board[0][2] == PLAYER)
                            || (board[1][0] == PLAYER && board[2][0] == PLAYER)
                            || (board[2][2] == PLAYER && board[1][1] == PLAYER))) 
                        board[0][0] = AI;
                    // if player will win at either third row, first column, or right diagonal
                    // and bottom left corner tile is empty, occupy bottom left corner tile
                    else if (board[2][0] == EMPTY && ((board[1][0] == PLAYER && board[0][0] == PLAYER)
                            || (board[0][2] == PLAYER && board[1][1] == PLAYER)
                            || (board[2][1] == PLAYER && board[2][2] == PLAYER))) 
                        board[2][0] = AI;
                    // if player will win at either the first row, third column, or right diagonal
                    // and top right corner tile is empty, occupy top right corner tile
                    else if (board[0][2] == EMPTY && ((board[0][0] == PLAYER && board[0][1] == PLAYER)
                            || (board[2][0] == PLAYER && board[1][1] == PLAYER)
                            || (board[2][2] == PLAYER && board[1][2] == PLAYER))) 
                        board[0][2] = AI;
                    // if player will win at either the third row, third column, or left diagonal,
                    // and bottom right corner tile is empty, occupy bottom right corner tile
                    else if (board[2][2] == EMPTY && ((board[0][2] == PLAYER && board[1][2] == PLAYER)
                            || (board[1][1] == PLAYER && board[0][0] == PLAYER)
                            || (board[2][0] == PLAYER && board[2][1] == PLAYER))) 
                        board[2][2] = AI;
                    // if player will win at either the second row or first column,
                    // and left middle tile is empty, occupy left middle tile
                    else if (board[1][0] == EMPTY && ((board[0][0] == PLAYER && board[2][0] == PLAYER)
                            || (board[1][2] == PLAYER && board[1][1] == PLAYER))) 
                        board[1][0] = AI;
                    // if player will win at either the second row, second column, left diagonal, or
                    // right diagonal, and the center tile is empty, occupy center tile
                    else if (board[1][1] == EMPTY && ((board[0][0] == PLAYER && board[2][2] == PLAYER)
                            || (board[2][0] == PLAYER && board[0][2] == PLAYER)
                            || (board[1][0] == PLAYER && board[1][2] == PLAYER)
                            || (board[0][1] == PLAYER && board[2][1] == PLAYER))) 
                        board[1][1] = AI;
                    // if player will win at either the first row or second column,
                    // and the top center tile is empty, occupy top center tile
                    else if (board[0][1] == EMPTY && ((board[0][0] == PLAYER && board[0][2] == PLAYER)
                            || (board[2][1] == PLAYER && board[1][1] == PLAYER))) 
                        board[0][1] = AI;
                    // if player will win at either the second row or third column,
                    // and the right middle tile is empty, occupy right middle tile
                    else if (board[1][2] == EMPTY && ((board[0][2] == PLAYER && board[2][2] == PLAYER)
                            || (board[1][0] == PLAYER && board[1][1] == PLAYER))) 
                        board[1][2] = AI;
                    // if player will win at either the third row or second column,
                    // and the bottom middle tile is empty, occupy bottom middle tile
                    else if (board[2][1] == EMPTY && ((board[0][1] == PLAYER && board[1][1] == PLAYER)
                            || (board[2][2] == PLAYER && board[2][0] == PLAYER))) 
                        board[2][1] = AI;
                    // if player has no chance of winning yet, attempt to create a fork 
                    // center case
                    else if (board[1][1] == board[0][1] && board[0][1] == board[2][1] && board[2][1] == board[1][0]
                            && board[1][0] == board[1][2] && board[1][1] == EMPTY)
                        board[1][1] = AI;
                    // corner case
                    else if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[2][0] == board[0][1]
                            && board[0][1] == board[0][2] && board[0][0] == EMPTY)
                        board[0][0] = AI;
                    else if (board[2][0] == board[0][0] && board[0][0] == board[1][0] && board[1][0] == board[2][1]
                            && board[2][1] == board[2][2] && board[2][0] == EMPTY)
                        board[2][0] = AI;
                    else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[2][2] == board[0][0]
                            && board[0][0] == board[0][1] && board[0][2] == EMPTY)
                        board[0][2] = AI;
                    else if (board[2][2] == board[0][2] && board[0][2] == board[1][2] && board[1][2] == board[2][0]
                            && board[2][0] == board[2][1] && board[2][2] == EMPTY)
                        board[2][2] = AI;
                    // middle case
                    else if (board[1][0] == board[2][0] && board[2][0] == board[0][0] && board[0][0] == board[1][1]
                            && board[1][1] == board[1][2] && board[1][0] == EMPTY)
                        board[1][0] = AI;
                    else if (board[1][2] == board[2][2] && board[2][2] == board[0][2] && board[0][2] == board[1][0]
                            && board[1][0] == board[1][1] && board[1][2] == EMPTY)
                        board[1][2] = AI;
                    else if (board[2][1] == board[1][1] && board[1][1] == board[0][1] && board[0][1] == board[2][0]
                            && board[2][0] == board[2][2] && board[2][1] == EMPTY)
                        board[2][1] = AI;
                    else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[2][1] == board[0][0]
                            && board[0][0] == board[0][2] && board[0][0] == EMPTY)
                        board[0][1] = AI;
                    // if cant create an fork, occupy empty tiles based on priority
                    // center 
                    else if (board[1][1] == EMPTY)
                        board[1][1] = AI;
                    // opposite corners, if player is in a corner, occupy opposite corner
                    else if (board[0][0] == PLAYER && board[2][2] == EMPTY)
                        board[2][2] = AI;
                    else if (board[0][0] == EMPTY && board[2][2] == PLAYER)
                        board[0][0] = AI;
                    else if (board[0][2] == PLAYER && board[2][0] == EMPTY)
                        board[2][0] = AI;
                    else if (board[0][2] == EMPTY && board[2][0] == PLAYER)
                        board[0][2] = AI;
                    // empty corners
                    else if (board[0][0] == EMPTY)
                        board[0][0] = AI; 
                    else if (board[2][2] == EMPTY) 
                        board[2][2] = AI;
                    else if (board[0][2] == EMPTY)
                        board[0][2] = AI;
                    else if (board[2][0] == EMPTY)
                        board[2][0] = AI;
                    // empty middles
                    else if (board[0][1] == EMPTY)
                        board[0][1] = AI;
                    else if (board[1][2] == EMPTY)
                        board[1][2] = AI;
                    else if (board[2][1] == EMPTY)
                        board[2][1] = AI;
                    else if (board[1][0] == EMPTY)
                        board[1][0] = AI;
                }
                break;
            /* DEPTH 1 SEARCH AI BEHAVIOR */
            case 2:
                int best = Integer.MIN_VALUE;
                // traverse all empty spaces to get the best move for AI 
                for (int j = 0; j < SIZE; j++)
                    for (int k = 0; k < SIZE; k++)
                        // if empty space, occupy and check if best move 
                        if (board[j][k] == EMPTY) {
                            board[j][k] = AI;
                            int eval = evaluate();
                            board[j][k] = EMPTY;
                            // if current move has greater evaluated score than best 
                            if (eval > best) {
                                best = eval; 
                                moves[0] = j; 
                                moves[1] = k;
                            }
                        }
                break;
            /* MINIMAX AI */
            case 3:
                best = Integer.MIN_VALUE;
                // traverse all empty spaces to get the best move for AI 
                for (int j = 0; j < SIZE; j++)
                    for (int k = 0; k < SIZE; k++)
                        // if empty space, occupy and check if best move 
                        if (board[j][k] == EMPTY) {
                            board[j][k] = AI;
                            int eval = minimax(getMovesLeft(), Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                            board[j][k] = EMPTY;
                            // if current move has greater evaluated score than best 
                            if (eval > best) {
                                best = eval;
                                moves[0] = j; 
                                moves[1] = k;
                            }
                        }
                break;
            /* CTR-BASED AI */
            case 4:
                int prevCtr = Integer.MAX_VALUE;
                best = Integer.MIN_VALUE;
                // traverse all empty spaces to get the best move for AI 
                for (int j = 0; j < SIZE; j++)
                    for (int k = 0; k < SIZE; k++)
                        // if empty space, occupy and check if best move 
                        if (board[j][k] == EMPTY) {
                            board[j][k] = AI;
                            ctr = 0;
                            int eval = minimax(getMovesLeft(), Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                            // int eval = evaluate();
                            board[j][k] = EMPTY;
                            // if current move has greater evaluated score than best 
                            if (eval > best || (eval >= best && ctr < prevCtr)) {
                                best = eval; 
                                prevCtr = ctr;
                                moves[0] = j; 
                                moves[1] = k;
                            }
                        }
                break;
        }
        
        // if move set has been changed from Levels 0, 2, 3, and 4, assign AI move to best move set
        if (moves[0] != -1 && moves[1] != -1)
            board[moves[0]][moves[1]] = AI;
    }

    /**
     * For level 3-4, analyzes and evaluates the current board state using
     * the minimax algorithm
     * 
     * @param   depth   the current depth of the board state according to the 
     *                  state-space graph
     * @param   alpha   the current greatest score value, for search pruning
     * @param   beta    the current lowest score value, for search pruning
     * @param   isMax   indicator of whose turn during the board state analysis
     * 
     * @return  an integer value representing the score of the board state based 
     *          from evaluation
     */
    public int minimax (int depth, int alpha, int beta, boolean isMax) {
        ctr += 1;
        // no more moves left, therefore return the evaluated score
        if (depth == 0)
            return evaluate(); // to be coded 
        
        int score = evaluate();
        
        // in the current board state, AI or PLAYER has won
        if (score == Integer.MAX_VALUE || score == Integer.MIN_VALUE)
            return score;
        
        // if AI's turn
        if (isMax) {
            int eval = Integer.MIN_VALUE;

            // traverse all empty spaces in the current board state
            for (int j = 0; j < SIZE; j++)
                for (int k = 0; k < SIZE; k++) 
                    // if empty space, occupy temporarily and check evaluated score
                    if (board[j][k] == EMPTY) {
                        // temporarily occupy
                        board[j][k] = AI;
                        int maxEval = minimax(depth - 1, alpha, beta, false); // DFS
                        eval = Math.max(eval, maxEval);
                        alpha = Math.max (alpha, maxEval);
                        // unoccupy space
                        board[j][k] = EMPTY;
                        if (beta <= alpha)
                            return eval;
                    }
            
            return eval; 
        }
        // Player's turn 
        else {
            int eval = Integer.MAX_VALUE;

            // traverse all empty spaces in the current board state
            for (int j = 0; j < SIZE; j++)
                for (int k = 0; k < SIZE; k++)
                    if (board[j][k] == EMPTY) {
                        board[j][k] = PLAYER; 
                        int minEval = minimax(depth - 1, alpha, beta, true); // DFS 
                        eval = Math.min(eval, minEval);
                        beta = Math.min(beta, minEval);
                        board[j][k] = EMPTY;
                        if (beta <= alpha)
                            return eval;
                    }
            
            return eval;
        }
    }

    /**
     * Checks if the current board state already shows already a winning (goal) state
     * for either Player or AI. If the current board state does not yet depict a 
     * winning state, proceeds to evaluating the current board state using a static
     * evaluation function.
     * 
     * @return the maximum integer value indicating that the AI won in the board state,
     *         the minimum integer value indicating that the Player won in the board state,
     *         otherwise an evaluated score given by the static evaluation function.
     */
    public int evaluate () {
        // check if Player or AI won in one of the rows 
        for (int row = 0; row < SIZE; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == AI)
                    return Integer.MAX_VALUE;
                else if (board[row][0] == PLAYER)
                    return Integer.MIN_VALUE; 
            }
        }
        // check if Player or AI won in one of the columns 
        for (int col = 0; col < SIZE; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == AI)
                    return Integer.MAX_VALUE; 
                else if (board[0][col] == PLAYER)
                    return Integer.MIN_VALUE; 
            }
        }
        // check if Player or AI won in the left diagonal 
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == AI)
                return Integer.MAX_VALUE; 
            else if (board[0][0] == PLAYER)
                return Integer.MIN_VALUE;
        }
        // check if Player or AI won in the right diagonal 
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == AI)
                return Integer.MAX_VALUE;
            else if (board[0][2] == PLAYER)
                return Integer.MIN_VALUE;
        }

        // else if no one won yet, execute static evaluation function
        return evalFunction ();
    }

    /**
     * Assuming a standard 3x3 TicTacToe board, this method returns an evaluation 
     * of the board state based on function parameters:
     * pAI      --> Number of possible wins for AI
     * cAI      --> Number of AI pieces in corners
     * oAI      --> Number of AI pair pieces placed in opposite corners
     * dPlayer  --> Number of adjacent Player pieces that can win
     * pPlayer  --> Number of possible wins for Player
     * 
     * Evaluation Function in Math Form:
     * f(n) = (pAI + 2cAI + oAI) - (3dPlayer + pPlayer);
     * 
     * @return  an integer representing the evaluation of a certain board state.
     */
    public int evalFunction () {
        int result = 0;
        int pAI = 0;
        int cAI = 0;
        int oAI = 0;
        int dPlayer = 0;
        int pPlayer = 0;
        int AIPcs, playerPcs, emptySpaces;

        /*  count pAI (# possible wins for AI)
            count pPlayer (# possible wins for Player)
         */
        // pAI | pPlayer : ROW
        for (int row = 0; row < SIZE; row++) {
            AIPcs = playerPcs = emptySpaces = 0;

            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == AI)
                    AIPcs += 1;
                else if (board[row][col] == PLAYER)
                    playerPcs += 1;
                else if (board[row][col] == EMPTY)
                    emptySpaces += 1;
            }
            if (AIPcs + emptySpaces == SIZE &&
                AIPcs > 0)
                pAI += 1;
            else if (playerPcs + emptySpaces == SIZE &&
                playerPcs > 0)
                pPlayer += 1;
        }

        // pAI | pPlayer : COLUMN
        for (int col = 0; col < SIZE; col++) {
            AIPcs = playerPcs = emptySpaces = 0;

            for (int row = 0; row < SIZE; row++) {
                if (board[row][col] == AI)
                    AIPcs += 1;
                else if (board[row][col] == PLAYER)
                    playerPcs += 1;
                else if (board[row][col] == EMPTY)
                    emptySpaces += 1;
            }

            if (AIPcs + emptySpaces == SIZE &&
                AIPcs > 0)
                pAI += 1;
            else if (playerPcs + emptySpaces == SIZE &&
                     playerPcs > 0)
                pPlayer += 1;
        }

        // pAI | pPlayer : L TO R DIAGONAL
        AIPcs = playerPcs = emptySpaces = 0;
        for (int diag = 0; diag < SIZE; diag++) {
            if (board[diag][diag] == AI)
                AIPcs += 1;
            else if (board[diag][diag] == PLAYER)
                playerPcs += 1;
            else if (board[diag][diag] == EMPTY)
                emptySpaces += 1;
        }

        if (AIPcs + emptySpaces == SIZE &&
            AIPcs > 0)
            pAI += 1;
        else if (playerPcs + emptySpaces == SIZE &&
                 playerPcs > 0)
            pPlayer += 1;

        // pAI | pPlayer : R TO L DIAGONAL
        AIPcs = playerPcs = emptySpaces = 0;
        for (int diag1 = 2, diag2 = 0; diag2 < SIZE; diag1--, diag2++) {
            if (board[diag1][diag2] == AI)
                AIPcs += 1;
            else if (board[diag1][diag2] == PLAYER)
                playerPcs += 1;
            else if (board[diag1][diag2] == EMPTY)
                emptySpaces += 1;
        }

        if (AIPcs + emptySpaces == SIZE &&
            AIPcs > 0)
            pAI += 1;
        else if (playerPcs + emptySpaces == SIZE &&
                 playerPcs > 0)
            pPlayer += 1;


        /*  COUNT cAI (AI PIECES IN CORNERS)
            X O X
            _ O O
            X _ X
         */
        for (int i = 0; i < 3; i+=2)
            for (int j = 0; j < 3; j+=2)
                if (board[i][j] == AI)
                    cAI += 1;

        /*  COUNT oAI (AI PAIRS IN OPPOSITE CORNERS)
            X _ _   _ _ X
            _ O _   O _ _
            _ _ X   X _ _
         */
        if (board[0][0] == AI &&
            board[2][2] == AI)
            oAI += 1;
        if (board[0][2] == AI &&
            board[2][0] == AI)
            oAI += 1;
        
        /*  COUNT dPlayer (ADJACENT PLAYER PAIR PIECES THAT CAN WIN) 
            _ O O   O _ _   _ O _
            _ _ _   X O _   _ O _
            X _ _   _ _ _   X _ _
         */
        // dPlayer : ROW
        for (int row = 0; row < SIZE; row++) {
            playerPcs = emptySpaces = 0;

            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == PLAYER)
                    playerPcs += 1;
                else if (board[row][col] == EMPTY)
                    emptySpaces += 1;
            }

            if (playerPcs == 2 &&
                emptySpaces == 1)
                dPlayer += 1;
        }

        // dPlayer : COL
        for (int col = 0; col < SIZE; col++) {
            playerPcs = emptySpaces = 0;

            for (int row = 0; row < SIZE; row++) {
                if (board[row][col] == PLAYER)
                    playerPcs += 1;
                else if (board[row][col] == EMPTY)
                    emptySpaces += 1;
            }

            if (playerPcs == 2 &&
                emptySpaces == 1)
                dPlayer += 1;
        }

        // dPlayer : L TO R DIAGONAL
        playerPcs = emptySpaces = 0;
        for (int diag = 0; diag < SIZE; diag++) {
            if (board[diag][diag] == PLAYER)
                playerPcs += 1;
            else if (board[diag][diag] == EMPTY)
                emptySpaces += 1;
        }

        if (playerPcs == 2 &&
            emptySpaces == 1)
            dPlayer += 1;

        // dPlayer : R TO L DIAGONAL
        playerPcs = emptySpaces = 0;
        for (int diag1 = 2, diag2 = 0; diag2 < SIZE; diag1--, diag2++) {
            if (board[diag1][diag2] == PLAYER)
                playerPcs += 1;
            else if (board[diag1][diag2] == EMPTY)
                emptySpaces += 1;
        }

        if (playerPcs == 2 &&
            emptySpaces == 1)
            dPlayer += 1;

        // result = (PX + 2 * C + O) - (3 * D + PO);
        result = (pAI + 2 * cAI + oAI) - (3 * dPlayer + pPlayer);

        return result;
    }

    /**
     * Clears the TicTacToe board, sets all tiles to empty
     */
    public void resetBoard () {
        for (int j = 0; j < SIZE; j++)
            for (int k = 0; k < SIZE; k++)
                board[j][k] = EMPTY;
    }

    /**
     * Sets the symbols of both Player and AI according to who has
     * the first move
     * 
     * @param   AIFirstMove     indicator of who has the first move
     */
    public void setGamePieces (boolean AIFirstMove) {
        this.AIFirstMove = AIFirstMove;

        if (AIFirstMove) {
            AI = 'X';
            PLAYER = 'O';
        } else {
            AI = 'O';
            PLAYER = 'X';
        }
    }

    /**
     * Sets the behavior level of the AI
     * 
     * @param   AILevel     behavior level value
     */
    public void setAILevel (int AILevel) {
        this.AILevel = AILevel;
    }
}
