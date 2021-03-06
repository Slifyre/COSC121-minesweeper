import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameClient extends Application {

	// Initial Values
	private static int initialMines = 10;
	private static int remainingMines = 0;
	private static int correctFlags = 0;
	private static int boardSizeX = 8;
	private static int boardSizeY = 8;
	private static int timeElapsed = 0;
	private static int difficulty = 0;

	private static String difficultyString = "Beginner";

	private static boolean firstClick = true;
	private static boolean alive = true;
	protected static boolean debug = false;
	private static boolean win = false;
	private static boolean hsReset = false;
	private static boolean cheatMode = false;

	// Panes
	private static BorderPane mainPane; // Main window
	private static MenuBar menuPane; // holds options (mainPane Top)
	private static BorderPane topPane; // Holds counters and face (mainPane Center)
	private static GridPane gamePane; // Holds mines/buttons (mainPane Bottom)

	// Menu
	private static Menu difficultyMenu;
	private static Menu fileMenu;
	// private static MenuItem saveGame;
	// private static MenuItem loadGame;
	private static MenuItem highScores;
	private static MenuItem resetHighScores;
	private static MenuItem beginner; // 10 Mines. 8x8 board
	private static MenuItem intermediate; // 40 Mines. 16x16 board
	private static MenuItem expert; // 99 Mines. 32x16 board

	// Top Info Bar
	private static Display remainingMinesDisplay;
	private static Face gameFace;
	private static Display timeElapsedDisplay;
	private static Timeline animation;

	// Game Board
	private static int[][] board; // Holds states of all tiles
	private static Tile boardButtons[][]; // Holds actual button objects

	// High Score popup
	private static TextInputDialog hsPopup;
	private static Scene hsScene;
	
	private static Text hsTextB;
	private static Text hsTextI;
	private static Text hsTextE;
	
	private static String[] highScoresString;

	// Main method
	public static void main(String[] args) {
		launch(args);
	}

	// Start method
	@Override
	public void start(Stage window) {

		// Game Variables
		win = false;
		firstClick = true;
		alive = true;
		win = false;
		board = new int[boardSizeX][boardSizeY];
		correctFlags = 0;
		timeElapsed = 0;
		remainingMines = initialMines;
		

		// Main BorderPane
		mainPane = new BorderPane();

		// Menu Items
		menuPane = new MenuBar();
		difficultyMenu = new Menu("Difficulty");
		beginner = new MenuItem("Beginner");
		intermediate = new MenuItem("Intermediate");
		expert = new MenuItem("Expert");

		fileMenu = new Menu("Scores");
		highScores = new MenuItem("High Scores");
		resetHighScores = new MenuItem("Reset High Scores");
		// saveGame = new MenuItem("Save Game");
		// loadGame = new MenuItem("Load Game");
		fileMenu.getItems().add(highScores);
		fileMenu.getItems().add(resetHighScores);
		// fileMenu.getItems().add(loadGame);
		// fileMenu.getItems().add(saveGame);

		difficultyMenu.getItems().add(beginner);
		difficultyMenu.getItems().add(intermediate);
		difficultyMenu.getItems().add(expert);

		menuPane.getMenus().add(fileMenu);
		menuPane.getMenus().add(difficultyMenu);

		// Difficulty Options
		beginner.setOnAction(e -> {
			difficultyString = "Beginner";
			difficulty = 0;
			initialMines = 10;
			boardSizeX = 8;
			boardSizeY = 8;
			animation.stop();
			start(window);
		});

		intermediate.setOnAction(e -> {
			difficultyString = "Intermediate";
			difficulty = 1;
			initialMines = 40;
			boardSizeX = 16;
			boardSizeY = 16;
			animation.stop();
			start(window);
		});

		expert.setOnAction(e -> {
			difficultyString = "Expert";
			difficulty = 2;
			initialMines = 99;
			boardSizeX = 32;
			boardSizeY = 16;
			animation.stop();
			start(window);
		});

		mainPane.setTop(menuPane);

		// Top Info Bar
		topPane = new BorderPane();

		// Bombs Remaining Display
		remainingMinesDisplay = new Display(64, 64, true, true);
		remainingMinesDisplay.setDisplay(remainingMines);
		topPane.setLeft(remainingMinesDisplay);

		// Face
		gameFace = new Face(64, 64);
		gameFace.setOnAction(e -> {
			// Resets the game when clicked
			animation.stop();
			start(window);
		});
		topPane.setCenter(gameFace);

		// Time Elapsed Display
		timeElapsedDisplay = new Display(64, 64, true, true);
		timeElapsedDisplay.setDisplay(0);
		animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
			timeElapsed++;
			timeElapsedDisplay.setDisplay(timeElapsed);
		}));
		animation.setCycleCount(Timeline.INDEFINITE);
		topPane.setRight(timeElapsedDisplay);

		mainPane.setCenter(topPane);

		// Game Pane
		gamePane = new GridPane();
		gamePane.setAlignment(Pos.CENTER);
		mainPane.setBottom(gamePane);
		
		//High Score Window
		Stage hsWindow = new Stage();
		hsWindow.initOwner(window);
		GridPane hsGridPane = new GridPane();
		highScoresString = HighScores.getHighScores();
		Text hsTextBd = new Text("Beginner: ");
		Text hsTextId = new Text("Intermediate: ");
		Text hsTextEd = new Text("Expert: ");
		hsTextB = new Text(highScoresString[0]);
		hsTextI = new Text(highScoresString[1]);
		hsTextE = new Text(highScoresString[2]);
		Button hsClose = new Button("Close");
		hsTextBd.setFont(new Font(18));
		hsTextId.setFont(new Font(18));
		hsTextEd.setFont(new Font(18));
		hsTextB.setFont(new Font(16));
		hsTextI.setFont(new Font(16));
		hsTextE.setFont(new Font(16));
		
		hsGridPane.setPadding(new Insets(15,15,15,15));
		
		hsGridPane.add(hsTextBd,0,0);
		hsGridPane.add(hsTextB,1,0);
		
		hsGridPane.add(hsTextId,0,1);
		hsGridPane.add(hsTextI,1,1);
		
		hsGridPane.add(hsTextEd,0,2);
		hsGridPane.add(hsTextE,1,2);
		
		hsGridPane.add(hsClose,2,3);
		
		hsClose.setOnAction(e -> {
			hsWindow.close();
		});
		
		highScores.setOnAction(e -> {
			hsReset = true;
			animation.stop();
			start(window);
		});

		resetHighScores.setOnAction(e -> {
			HighScores.newScoreFile(true);
			animation.stop();
			start(window);
		});
		
		
		hsScene = new Scene(hsGridPane);
		hsWindow.setTitle("Fastest Mine Sweepers");
		hsWindow.getIcons().add(new Image("file:res/tiles/mine-grey.png"));
		hsWindow.setScene(hsScene);
		hsWindow.initModality(Modality.APPLICATION_MODAL);

		setBoardButtons();
		

		// Scene + Stage

		// CSS Styling
		mainPane.setStyle(
				"-fx-background-color: #bfbfbf;-fx-border-color: #fafafa #787878 #787878 #fafafa; -fx-border-width: 9; -fx-border-radius: 0.001;");
		topPane.setStyle(
				"-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width: 6; -fx-border-radius: 0.001;");

		window.setOnCloseRequest(e -> {
			if (debug) {
				System.out.println("[DEBUG] Closing Program.");
				hsWindow.close();
			}
		});

		window.getIcons().add(new Image("file:res/tiles/mine-grey.png"));
		window.setTitle("Minesweeper");
		window.setScene(new Scene(mainPane));
		window.show();
		
		if(hsReset) {
			//Allows High Score window to update
			hsWindow.show();
			hsWindow.close();
			hsWindow.show();
			hsReset = false;
		}
	}

	private static void newHighScore() {

		// Highscore popup
		hsPopup = new TextInputDialog();
		hsPopup.setX(400);
		hsPopup.setY(200);
		hsPopup.setTitle("New High Score! (" + difficultyString + ")");
		hsPopup.setHeaderText("Enter your name: ");
		hsPopup.setGraphic(new ImageView(new Image("file:res/faces/face-win.png")));
		try {
			Optional<String> hsGet = hsPopup.showAndWait();
			String hsName = hsGet.get();

			if (hsName.length() > 0) {
				HighScores.addHighScore(timeElapsed, difficulty, hsName);
				System.out.println(HighScores.getHighScores());
			}
		} catch (NoSuchElementException e) {

		}

	}

	private static void setBoardButtons() {

		boardButtons = new Tile[boardSizeX][boardSizeY];

		for (int i = 0; i < boardSizeX; i++) {
			int x = i;
			for (int j = 0; j < boardSizeY; j++) {
				int y = j;
				boardButtons[i][j] = new Tile(48, 48);

				Tile currentTile = boardButtons[i][j];

				gamePane.add(currentTile, i, j);

				// Set face graphic to face-o when mouse is pressed/not released
				currentTile.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
					if (alive && !win) {
						gameFace.setState(1);
					}
				});

				currentTile.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
					if (alive && !win) {
						gameFace.setState(0);
					}
				});

				currentTile.setOnMouseClicked(e -> {
					MouseButton mbutton = e.getButton();
					if (mbutton == MouseButton.PRIMARY && alive && !currentTile.isFlagged() && !win) {

						if (board[x][y] == 10) {

							alive = false;
							boardButtons[x][y].setState(11);
							gameFace.setState(2);
							animation.stop();

							// Show Mines and check if flags are correct
							for (int l = 0; l < boardSizeX; l++) {
								for (int k = 0; k < boardSizeY; k++) {
									boardButtons[l][k].setIsMine(board[l][k] == 10 ? true : false);
									boardButtons[l][k].checkFlag();
								}
							}

						} else if (firstClick == true) {
							animation.play();
							spawnMines(x, y);
							checkTiles();
							// seeTiles();
							firstClick = false;
							checkSurroundingTiles(x, y);
							boardButtons[x][y].setState(board[x][y]);
							if(cheatMode) {
								seeTiles();
							}
						} else {
							checkSurroundingTiles(x, y);
							boardButtons[x][y].setState(board[x][y]);
						}

					} else if (mbutton == MouseButton.SECONDARY && alive && !win) {

						boardButtons[x][y].setIsMine(board[x][y] == 10 ? true : false);

						if (boardButtons[x][y].isFlagged()) {
							boardButtons[x][y].flag(false);
							remainingMines++;
							if (boardButtons[x][y].isFlagAndMine()) {
								correctFlags--;
							}
							remainingMinesDisplay.setDisplay(remainingMines);
						} else if (remainingMines > 0) {
							boardButtons[x][y].flag(true);

							if (boardButtons[x][y].isFlagAndMine()) {
								correctFlags++;
								if(debug) {
									System.out.println("[DEBUG] - Flag and Mine!");
								}
							}

							remainingMines--;
							remainingMinesDisplay.setDisplay(remainingMines);
						}

					}

					if (correctFlags == initialMines) {
						gameFace.setState(3);
						animation.stop();
						win = true;
						
						if(timeElapsed < HighScores.getFastestTime(timeElapsed, difficulty) || HighScores.getFastestTime(timeElapsed, difficulty) == 0) {
							newHighScore();
						}
						
					}

				}); // End setOnMouseClicked

			} // End inner for

		} // End outer for
	}// end setBoardButtons method

	private static void spawnMines(int x, int y) {
		// Spawn mines excluding the space at [x,y] and surrounding tiles.
		int spawnedMines = 0;

		if (debug) {
			System.out.println("[DEBUG] - Spawning mines excluding [" + x + "][" + y + "], and surrounding spacex");
		}

		while (spawnedMines < initialMines) {

			int randX = (int) (Math.random() * boardSizeX);
			int randY = (int) (Math.random() * boardSizeY);

			int distance = (int) Math.sqrt(Math.pow(randX - x, 2) + Math.pow(randY - y, 2));
			if (debug) {
				System.out.println("Approx Distance: " + distance);
			}

			if (distance > 2 && board[randX][randY] != 10) {

				if (debug) {
					System.out.println("[DEBUG] - Randomized Mine at [" + randX + "][" + randY + "] #:" + spawnedMines);
				}

				board[randX][randY] = 10;
				spawnedMines++;
			}

		}
		if (debug) {
			System.out.println("[DEBUG] - Done setting mines.");
		}

	}

	private static void seeTiles() {
		for (int k = 0; k < boardSizeX; k++) {

			for (int l = 0; l < boardSizeY; l++) {
				boardButtons[k][l].setState(board[k][l]);
			}

		}
	}

	public static void checkSurroundingTiles(int x, int y) {

		// Checks if the surrounding tiles are blank spaces and recursively opens them
		// if they are

		if (board[x][y] != 10) {

			if (debug) {
				System.out.println("[DEBUG] - Tile Check method called at: [" + x + "] [" + y + "]");
			}

			// Above
			if (x + 1 < board.length && board[x + 1][y] == 0 && (boardButtons[x + 1][y].getState() != 0)) {
				boardButtons[x + 1][y].setState(0);
				checkSurroundingTiles(x + 1, y);
			}

			if (x + 1 < board.length && board[x + 1][y] != 10) {
				boardButtons[x + 1][y].setState(board[x + 1][y]);
			}

			// Below
			if (x > 0 && board[x - 1][y] == 0 && (boardButtons[x - 1][y].getState() != 0)) {
				boardButtons[x - 1][y].setState(0);
				checkSurroundingTiles(x - 1, y);
			}

			if (x > 0 && board[x - 1][y] != 10) {
				boardButtons[x - 1][y].setState(board[x - 1][y]);
			}

			// Above Left
			if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1] == 0
					&& (boardButtons[x + 1][y + 1].getState() != 0)) {
				boardButtons[x + 1][y + 1].setState(0);
				checkSurroundingTiles(x + 1, y + 1);
			}

			if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1] != 10) {
				boardButtons[x + 1][y + 1].setState(board[x + 1][y + 1]);
			}

			// Left
			if (y + 1 < board[0].length && board[x][y + 1] == 0 && (boardButtons[x][y + 1].getState() != 0)) {
				boardButtons[x][y + 1].setState(0);
				checkSurroundingTiles(x, y + 1);
			}

			if (y + 1 < board[0].length && board[x][y + 1] != 10) {
				boardButtons[x][y + 1].setState(board[x][y + 1]);
			}

			// Below Left
			if (x > 0 && y + 1 < board[0].length && board[x - 1][y + 1] == 0
					&& (boardButtons[x - 1][y + 1].getState() != 0)) {
				boardButtons[x - 1][y + 1].setState(0);
				checkSurroundingTiles(x - 1, y + 1);
			}

			if (x > 0 && y + 1 < board[0].length && board[x - 1][y + 1] != 10) {
				boardButtons[x - 1][y + 1].setState(board[x - 1][y + 1]);
			}

			// Above Right
			if (x + 1 < board.length && y > 0 && board[x + 1][y - 1] == 0
					&& (boardButtons[x + 1][y - 1].getState() != 0)) {
				boardButtons[x + 1][y - 1].setState(0);
				checkSurroundingTiles(x + 1, y - 1);
			}

			if (x + 1 < board.length && y > 0 && board[x + 1][y - 1] != 10) {
				boardButtons[x + 1][y - 1].setState(board[x + 1][y - 1]);
			}

			// Right
			if (y > 0 && board[x][y - 1] == 0 && (boardButtons[x][y - 1].getState() != 0)) {
				boardButtons[x][y - 1].setState(0);
				checkSurroundingTiles(x, y - 1);
			}

			if (y > 0 && board[x][y - 1] != 10) {
				boardButtons[x][y - 1].setState(board[x][y - 1]);
			}

			// Below Right
			if (x > 0 && y > 0 && board[x - 1][y - 1] == 0 && (boardButtons[x - 1][y - 1].getState() != 0)) {
				boardButtons[x - 1][y - 1].setState(0);
				checkSurroundingTiles(x - 1, y - 1);
			}

			if (x > 0 && y > 0 && board[x - 1][y - 1] != 10) {

				boardButtons[x - 1][y - 1].setState(board[x - 1][y - 1]);
			}

		}

	}

	private static void checkTiles() {
		// Checks the board for mines, and sets up the corresponding numbers around them

		if (debug) {
			System.out.println("[DEBUG] << Begin Checking Tiles >>");
		}

		for (int x = 0; x < boardSizeX; x++) {
			for (int y = 0; y < boardSizeY; y++) {

				int surroundingMineCount = 0;

				// Check if mine so they are not overwritten
				if (board[x][y] != 10) {

					// Above
					if (x + 1 < board.length && board[x + 1][y] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine above tile at [" + x + "][" + y + "]");
						}
					}
					// Below
					if (x > 0 && board[x - 1][y] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine below tile at [" + x + "][" + y + "]");
						}
					}
					// Above Left
					if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine above/left of tile at [" + x + "][" + y + "]");
						}
					}
					// Left
					if (y + 1 < board[0].length && board[x][y + 1] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine left of tile at [" + x + "][" + y + "]");
						}
					}
					// Below Left
					if (x > 0 && y + 1 < board[0].length && board[x - 1][y + 1] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine below/left of tile at [" + x + "][" + y + "]");
						}
					}
					// Above Right
					if (x + 1 < board.length && y > 0 && board[x + 1][y - 1] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine above/right of tile at [" + x + "][" + y + "]");
						}
					}
					// Right
					if (y > 0 && board[x][y - 1] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine right of tile at [" + x + "][" + y + "]");
						}
					}
					// Below Right
					if (x > 0 && y > 0 && board[x - 1][y - 1] == 10) {
						surroundingMineCount++;
						if (debug) {
							System.out.println("[DEBUG] Found mine above tile at [" + x + "][" + y + "]");
						}
					}
					// Set the board tile to the # of surrounding mines
					if (debug) {
						System.out.println("[DEBUG] Board[" + x + "][" + y + "] has " + surroundingMineCount
								+ " mines surrounding it.");
					}

					if (surroundingMineCount > 0) {
						// Set from 1-8 based on surroundingMineCount
						board[x][y] = surroundingMineCount;
					} else {
						board[x][y] = 0; // Set to blank tile
					}

				}

			}
		}

	}

}
