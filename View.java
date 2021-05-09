import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.media.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.collections.*; 
import javafx.event.EventHandler;

import java.io.*;
import java.net.URL;
import java.util.*;

public class View extends Application {
    /**
     * Stage for the main menu.
     */
    private Stage mainStage;
    /**
     * Scene for the main menu.
     */
    private Scene mainMenuScene;
    /**
     * Scene for in-game screen.
     */
    private Scene inGameScene;
    /**
     * Start game button in main menu screen.
     */
    private Button startBtn;
    /**
     * AI level input in the main menu screen.
     */
    private TextField AILevelInput;
    /**
     * In-game grid of tiles.
     */
    private ArrayList<StackPane> tiles;
    /**
     * In-game tile contents (text for X and O).
     */
    private ArrayList<Label> tileContents;
    /**
     * Main Menu button in the in-game screen.
     */
    private Button mainMenuBtn;
    /**
     * Score label for AI and Player score found in the in-game screen.
     */
    private Label scoreLabel;
    /**
     * Stage for the play again window.
     */
    private Stage playAgainStage;
    /** 
     * Scene for the play again window.
     */
    private Scene playAgainScene;
    /**
     * 'YES' button for the play again window.
     */
    private Button yesBtn;
    /**
     * 'NO' button for the play again window.
     */
    private Button noBtn;
    /**
     * Text shown inside the play again window.
     */
    private Label playAgainText;
    /**
     * Media player for background Music.
     */
    private MediaPlayer mpMusic;
    /**
     * Media player for sound effects.
     */
    private MediaPlayer mpEffect;
    
    @Override
    public void start (Stage stage) throws Exception {
        mainStage = stage;
        
        /* Initialize GUI elements */
        try {
            mainMenuSetup ();
            inGameSceneSetup ();
            playAgainSetup ();
        } catch (Exception e) {
            e.printStackTrace ();
            System.out.println ("An error occurred in accessing file resource/s! File/s may not exist!");
        }

        /* Assign Controller */
        new Controller (this);

        /* Setup Main Stage */
        mainStage.getIcons ().add (new Image (new FileInputStream ("images/logo.png")));
        mainStage.setTitle ("Watch me win this!");
        mainStage.setScene (mainMenuScene);
        mainStage.setResizable (false);
        mainStage.show ();

        /* Background Music */
        URL bgUrl = getClass().getResource("sounds/gura.mp3");
        String bgStringUrl = bgUrl.toExternalForm();
        mpMusic = new MediaPlayer(new Media(bgStringUrl));
        mpMusic.setAutoPlay(true);
    }

    /**
     * Initializes the GUI elements found in the Main Menu screen.
     * 
     * @throws FileNotFoundException if the needed file/s are not found.
     */
    public void mainMenuSetup () throws FileNotFoundException {
        /* Main Layouts */
        BorderPane mainMenuLayout = new BorderPane ();
        VBox inputLayout = new VBox ();
        mainMenuLayout.setPadding (new Insets (15, 5, 20, 5));
        mainMenuLayout.setStyle ("-fx-background-color: #2d6a4f");
        inputLayout.setPadding (new Insets (7, 0, 0, 5));

        /* Main Labels */
        Label mainMenuLabel = new Label ("tictactoe");
        mainMenuLabel.setFont (Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 75));
        mainMenuLabel.setStyle ("-fx-text-fill: #ffd166");
        Label AILevelLabel = new Label ("AI Level [0 - 4]:");
        AILevelLabel.setFont (Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 30));
        AILevelLabel.setStyle ("-fx-text-fill: #caffbf;");

        /* TextField */
        AILevelInput = new TextField ();
        AILevelInput.setMaxWidth (49);
        AILevelInput.setFont (Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 25));
        AILevelInput.setStyle ("-fx-background-color : transparent; -fx-text-fill: #caffbf");
        AILevelInput.setPromptText ("0");

        /* Start Button */
        startBtn = new Button ("START");
        startBtn.setId ("startGame");
        startBtn.setPrefSize (125, 30);
        startBtn.setPadding (new Insets (1, 3, 1, 3));
        startBtn.setFont(Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 35));
        startBtn.setStyle ("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #ffd166");
        startBtn.setDisable (true);
        
        /* Adding of children into main layouts */
        inputLayout.getChildren ().addAll (AILevelLabel, AILevelInput);
        inputLayout.setAlignment (Pos.TOP_CENTER);

        mainMenuLayout.setTop (mainMenuLabel);
        mainMenuLayout.setAlignment (mainMenuLabel, Pos.CENTER);
        mainMenuLayout.setCenter (inputLayout);
        mainMenuLayout.setAlignment (inputLayout, Pos.CENTER);
        mainMenuLayout.setBottom (startBtn);
        mainMenuLayout.setAlignment (startBtn, Pos.CENTER);

        mainMenuScene = new Scene (mainMenuLayout, 450, 280);
    }

    /**
     * Shows the Main Menu scene on the Main Window.
     */
    public void showMainMenu () {
        mainStage.setScene (mainMenuScene);
    }

    /**
     * Returns the AI level TextField itself.
     * 
     * @return the AI level TextField itself
     */
    public TextField getAILevelField () {
        return AILevelInput;
    }

    /**
     * Enables/disables the start button found in the Main Menu screen.
     * 
     * @param b true to disable button, otherwise false
     */
    public void setDisableStartBtn (boolean b) {
        startBtn.setDisable (b);
    }

    /**
     * Initializes the GUI elements found in the in-game screen.
     * 
     * @throws FileNotFoundException if the needed file/s are not found
     */
    public void inGameSceneSetup() throws FileNotFoundException {
        /* Main Layout */
        BorderPane inGameMainLayout = new BorderPane();
        inGameMainLayout.setPadding(new Insets(10, 10, 10, 10));

        /* Main Menu Button */
        mainMenuBtn = new Button("MAIN MENU");
        mainMenuBtn.setId("backToMainMenu");
        mainMenuBtn.setFont(Font.loadFont(new FileInputStream("fonts/pastel crayon.ttf"), 35));
        mainMenuBtn.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-text-fill: #ffd166");

        /* Score Label */
        scoreLabel = new Label("AI: 0   Player: 0");
        scoreLabel.setFont(Font.loadFont(new FileInputStream("fonts/pastel crayon.ttf"), 35));
        scoreLabel.setStyle("-fx-text-fill: #ffd166");

        /* Board */
        GridPane board;
        board = new GridPane();
        tiles = new ArrayList<>();
        tileContents = new ArrayList<>();

        /* BG TEST */
        BackgroundImage bg = new BackgroundImage(
                new Image(new FileInputStream("images/ingamebg.png")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(450, 450, false, false, false, false)
        );
        inGameMainLayout.setBackground(new Background(bg));

        int rowId, colId;
        rowId = colId = 0;
        /* Add StackPanes to GridPane */
        for (int i = 0; i < 9; i++) {
            tiles.add(new StackPane());
            // tiles.get (i).setStyle ("-fx-background-color: #2d6a4f; -fx-border-color:
            // #ffd166; -fx-border-width: 1px");
            tiles.get(i).setPrefWidth(85);
            tiles.get(i).setPrefHeight(85);

            tileContents.add(new Label());
            tileContents.get(i).setFont(Font.loadFont(new FileInputStream("fonts/crayon.ttf"), 75));
            tileContents.get(i).setStyle("-fx-text-fill: #ffd166");
            tiles.get(i).getChildren().add(tileContents.get(i));

            /* Set ID of Tiles */
            tiles.get(i).setId(rowId + " " + colId);
            if (colId > TicTacToe.SIZE - 2) {
                colId = 0;
                rowId += 1;
            } else
                colId += 1;
        }

        int k = 0;
        for (int row = 0; row < TicTacToe.SIZE; row++)
            for (int col = 0; col < TicTacToe.SIZE; col++) {
                board.setConstraints(tiles.get(k), col, row);
                board.getChildren().add(tiles.get(k));
                k++;
            }

        /* Board Layouting */
        board.setHgap(2);
        board.setVgap(2);
        board.setAlignment(Pos.CENTER);

        /* Adding of GUI elements to Layouts */
        inGameMainLayout.setCenter(board);
        inGameMainLayout.setTop(scoreLabel);
        inGameMainLayout.setBottom(mainMenuBtn);

        inGameMainLayout.setAlignment(board, Pos.CENTER);
        inGameMainLayout.setAlignment(scoreLabel, Pos.CENTER);
        inGameMainLayout.setAlignment(mainMenuBtn, Pos.CENTER);

        inGameScene = new Scene(inGameMainLayout, 450, 450);
    }

    /**
     * Updates the tiles in the in-game screen. Used whenever
     * a player (agent or opponent) makes a move.
     * 
     * @param board the non-GUI board of the game itself
     */
    public void updateBoard (char[][] board) {
        int k = 0;
        for (int row = 0; row < TicTacToe.SIZE; row++)
            for (int col = 0; col < TicTacToe.SIZE; col++) {
                tileContents.get (k).setText (board[row][col] + "");
                k++;
            }
    }

    /**
     * Updates the scores of the players (agent or opponent) in
     * the in-game screen. This is used whenever a player wins 
     * a certain game.
     * 
     * @param AIScore       the current score of the agent
     * @param playerScore   the current score of the opponent
     */
    public void updateScore (int AIScore, int playerScore) {
        scoreLabel.setText ("AI: " + AIScore + "   " + "Player: " + playerScore);
    }

    /**
     * Shows the in-game scene on the main window.
     */
    public void showInGame () {
        mainStage.setScene (inGameScene);
    }

    /**
     * Initializes the GUI elements found in the play again prompt window.
     * 
     * @throws FileNotFoundException if the needed file/s are not found
     */
    public void playAgainSetup () throws FileNotFoundException {
        /* Main Layout */
        VBox mainLayout = new VBox (20);
        HBox buttonLayout = new HBox (15);

        mainLayout.setPadding (new Insets (10, 10, 10, 10));
        mainLayout.setStyle ("-fx-border-color: #a56336; -fx-border-width: 8px; -fx-background-color: #2d6a4f");

        /* Labels */
        Label againText = new Label ("play again?");
        againText.setFont(Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 35));
        againText.setStyle ("-fx-text-fill: #edf2f4");

        playAgainText = new Label ();
        playAgainText.setFont(Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 35));
        playAgainText.setStyle ("-fx-text-fill: #edf2f4");

        /* Buttons */
        yesBtn = new Button ("YES");
        yesBtn.setId ("playAgain");
        yesBtn.setPrefSize (125, 30);
        yesBtn.setPadding (new Insets (1, 3, 1, 3));
        yesBtn.setFont(Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 28));
        yesBtn.setStyle ("-fx-border-color: transparent; -fx-background-color: transparent; -fx-text-fill: #caffbf");

        noBtn = new Button ("NO");
        noBtn.setId ("dontPlayAgain");
        noBtn.setPrefSize (125, 30);
        noBtn.setPadding (new Insets (1, 3, 1, 3));
        noBtn.setFont(Font.loadFont (new FileInputStream ("fonts/pastel crayon.ttf"), 28));
        noBtn.setStyle ("-fx-border-color: transparent; -fx-background-color: transparent; -fx-text-fill: #f28482");

        /* Adding of GUI elements into layouts */
        buttonLayout.getChildren ().addAll (yesBtn, noBtn);

        mainLayout.getChildren ().addAll (playAgainText, againText, buttonLayout);
        buttonLayout.setAlignment (Pos.CENTER);
        mainLayout.setAlignment (Pos.CENTER);

        playAgainScene = new Scene (mainLayout, 370, 250);

        playAgainStage = new Stage ();
        playAgainStage.initStyle (StageStyle.UNDECORATED);
        playAgainStage.setScene (playAgainScene);
    }

    /**
     * Shows the play again prompt window. Its message will vary
     * based on the parameter message to be passed into it.
     * 
     * @param message the message to be displayed
     */
    public void showPlayAgainPrompt (String message) {
        playAgainText.setText (message);
        playAgainStage.showAndWait ();
    }

    /**
     * Closes the play again prompt window.
     */
    public void closePlayAgainPrompt () {
        playAgainStage.close ();
    }

    /**
     * Plays a clicking sound. Used whenever a button is clicked.
     */
    public void playClick () {
        URL clickUrl = getClass().getResource("sounds/click.wav");
        String clickStringUrl = clickUrl.toExternalForm();
        mpEffect = new MediaPlayer(new Media(clickStringUrl));
        mpEffect.play();
    }

    /**
     * Plays a sound affiliated with the X piece. Used whenever
     * an X piece is placed onto the game board.
     */
    public void playX () {
        URL xUrl = getClass().getResource("sounds/draw_x.wav");
        String xStringUrl = xUrl.toExternalForm();
        mpEffect = new MediaPlayer(new Media(xStringUrl));
        mpEffect.play();
    }

    /**
     * Plays a sound affiliated with the O piece. Used whenever
     * an O piece is placed onto the game board.
     */
    public void playO () {
        URL oUrl = getClass().getResource("sounds/draw_o.wav");
        String oStringUrl = oUrl.toExternalForm();
        mpEffect = new MediaPlayer(new Media(oStringUrl));
        mpEffect.play();
    }

    /**
     * Plays a sound affiliated with winning. This is played 
     * whenever a player wins against the agent.
     */
    public void playWin () {
        URL wUrl = getClass().getResource("sounds/win.mp3");
        String wStringUrl = wUrl.toExternalForm();
        mpEffect = new MediaPlayer(new Media(wStringUrl));
        mpEffect.play();
    }

    /**
     * Plays a sound affiliated with losing. This is played 
     * whenever a player loses to the agent.
     */
    public void playLose () {
        URL lUrl = getClass().getResource("sounds/lose.wav");
        String lStringUrl = lUrl.toExternalForm();
        mpEffect = new MediaPlayer(new Media(lStringUrl));
        mpEffect.play();
    }

    /**
     * Plays a sound affiliated with ties. This is played 
     * whenever neither the agent nor the player wins in
     * a certain game.
     */
    public void playTie () {
        URL tUrl = getClass().getResource("sounds/tie.mp3");
        String tStringUrl = tUrl.toExternalForm();
        mpEffect = new MediaPlayer(new Media(tStringUrl));
        mpEffect.play();
    }

    /**
     * Sets the EventHandlers for various GUI elements.
     * 
     * @param controller the Controller managing the GUI elements
     */
    public void setEventHandlers (Controller controller) {
        startBtn.setOnAction ((EventHandler) controller);
        yesBtn.setOnAction ((EventHandler) controller);
        noBtn.setOnAction ((EventHandler) controller);
        mainMenuBtn.setOnAction ((EventHandler) controller);
        AILevelInput.textProperty ().addListener (controller);

        for (int i = 0; i < TicTacToe.SIZE * TicTacToe.SIZE; i++)
            tiles.get(i).setOnMouseClicked ((EventHandler) controller);
    }
}
