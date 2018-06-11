import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameClient extends Application {

	// Finals
	private static int maxBombs = 10;
	private static int boardSizeX = 8;
	private static int boardSizeY = 8;
	private static int nonBombTiles = (boardSizeX * boardSizeY) - maxBombs;
	private static int timeElapsed = 0;

	// Class variables
	private static int correctFlags;
	private static int bombsRemaining;
	private static int tileClicks;
	public static boolean running;
	public static boolean debug;
	public static boolean alive;
	public static boolean firstClick = true;
	private static boolean presetBoard = false;
	private static int count = 0;

	// Gameboard arrays
	private static int[][] board;
	private static Tile boardButtons[][] = new Tile[boardSizeX][boardSizeY];
	private static int[] tempExcluding = { 0, 0 };

	// Panes
	private static BorderPane mainPane;
	private static GridPane gamePane;
	private static HBox topBarPane;

	// Nodes
	private static MenuBar menuBar;
	private static MenuItem beginner;
	private static MenuItem intermediate;
	private static MenuItem expert;
	private static HBox topHBox;
	private static Menu difficultyMenu;
	private static Display bombsRemainingDisplay;
	private static Face gameFace;
	private static Display timeElapsedDisplay;
	private static Timeline animation;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage window) {

		// maxBombs = 10;
		// boardSizeX = 8;
		// boardSizeY = 8;
		// nonBombTiles = (boardSizeX * boardSizeY) - maxBombs;

		newGameBoard(tempExcluding);

		gamePane.setAlignment(Pos.CENTER);
		mainPane.setBottom(gamePane);
		topHBox = new HBox();
		topHBox.setAlignment(Pos.CENTER);

		bombsRemainingDisplay = new Display(64, 64, true, true);
		bombsRemainingDisplay.setDisplay(bombsRemaining);
		timeElapsedDisplay = new Display(64, 64, true, true);
		timeElapsedDisplay.setDisplay(0);
		menuBar = new MenuBar();
		difficultyMenu = new Menu("Difficulty");
		beginner = new MenuItem("Beginner");
		intermediate = new MenuItem("Intermediate");
		expert = new MenuItem("Expert");
		bombsRemainingDisplay.setAlignment(Pos.CENTER_LEFT);
		timeElapsedDisplay.setAlignment(Pos.CENTER_RIGHT);

		difficultyMenu.getItems().add(beginner);
		difficultyMenu.getItems().add(intermediate);
		difficultyMenu.getItems().add(expert);

		beginner.setOnAction(e -> {
			maxBombs = 10;
			boardSizeX = 8;
			boardSizeY = 8;
			start(window);
		});

		intermediate.setOnAction(e -> {
			maxBombs = 40;
			boardSizeX = 16;
			boardSizeY = 16;
			start(window);
		});

		expert.setOnAction(e -> {
			maxBombs = 99;
			boardSizeX = 16;
			boardSizeY = 32;
			start(window);
		});

		menuBar.getMenus().add(difficultyMenu);

		// topHBox.getChildren().add(menuBar);
		topHBox.getChildren().add(topBarPane);

		// topHBox.prefWidthProperty().bind(gamePane.widthProperty());
		mainPane.setCenter(topHBox);
		mainPane.setTop(menuBar);

		gameFace = new Face(64, 64, true, true);

		topBarPane.getChildren().add(bombsRemainingDisplay);
		topBarPane.getChildren().add(gameFace);
		topBarPane.getChildren().add(timeElapsedDisplay);
		topBarPane.setPadding(new Insets(12, 12, 12, 12));
		topBarPane.setAlignment(Pos.CENTER);

		Scene mainScene = new Scene(mainPane);

		gameFace.setOnAction(e -> {
			if (debug) {
				System.out.println("[DEBUG] Face clicked!");
			}
			animation.stop();
			// resetBoard();
			start(window);

		});

		window.setOnCloseRequest(e -> {
			if (debug) {
				System.out.println("[DEBUG] Closing Program.");
			}
		});

		mainPane.setStyle(
				"-fx-background-color: #bfbfbf;-fx-border-color: #fafafa #787878 #787878 #fafafa; -fx-border-width: 9; -fx-border-radius: 0.001;");
		topHBox.setStyle(
				"-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width: 6; -fx-border-radius: 0.001;");

		window.setTitle("Minesweeper");
		window.setScene(mainScene);
		window.show();

	}

	public static void checkFirstTile(int x, int y) {

		if (board[x][y] != 10) {

			if (debug) {
				System.out.println("[DEBUG] - Tile Check method callled at: [" + x + "] [" + y + "], Count: " + count);
				count++;
			}

			// Above
			if (x + 1 < board.length && board[x + 1][y] == 9 && (boardButtons[x + 1][y].getState() != 9)) {
				boardButtons[x + 1][y].setState(9);
				checkFirstTile(x + 1, y);
			}

			if (x + 1 < board.length && board[x + 1][y] != 10) {
				boardButtons[x + 1][y].setState(board[x + 1][y]);
			}

			// Below
			if (x > 0 && board[x - 1][y] == 9 && (boardButtons[x - 1][y].getState() != 9)) {
				boardButtons[x - 1][y].setState(9);
				checkFirstTile(x - 1, y);
			}

			if (x > 0 && board[x - 1][y] != 10) {
				boardButtons[x - 1][y].setState(board[x - 1][y]);
			}

			// Above Left
			if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1] == 9
					&& (boardButtons[x + 1][y + 1].getState() != 9)) {
				boardButtons[x + 1][y + 1].setState(9);
				checkFirstTile(x + 1, y + 1);
			}

			if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1] != 10) {
				boardButtons[x + 1][y + 1].setState(board[x + 1][y + 1]);
			}

			// Left
			if (y + 1 < board[0].length && board[x][y + 1] == 9 && (boardButtons[x][y + 1].getState() != 9)) {
				boardButtons[x][y + 1].setState(9);
				checkFirstTile(x, y + 1);
			}

			if (y + 1 < board[0].length && board[x][y + 1] != 10) {
				boardButtons[x][y + 1].setState(board[x][y + 1]);
			}

			// Below Left
			if (x > 0 && y + 1 < board[0].length && board[x - 1][y + 1] == 9
					&& (boardButtons[x - 1][y + 1].getState() != 9)) {
				boardButtons[x - 1][y + 1].setState(9);
				checkFirstTile(x - 1, y + 1);
			}

			if (x > 0 && y + 1 < board[0].length && board[x - 1][y + 1] != 10) {
				boardButtons[x - 1][y + 1].setState(board[x - 1][y + 1]);
			}

			// Above Right
			if (x + 1 < board.length && y > 0 && board[x + 1][y - 1] == 9
					&& (boardButtons[x + 1][y - 1].getState() != 9)) {
				boardButtons[x + 1][y - 1].setState(9);
				checkFirstTile(x + 1, y - 1);
			}

			if (x + 1 < board.length && y > 0 && board[x + 1][y - 1] != 10) {
				boardButtons[x + 1][y - 1].setState(board[x + 1][y - 1]);
			}

			// Right
			if (y > 0 && board[x][y - 1] == 9 && (boardButtons[x][y - 1].getState() != 9)) {
				boardButtons[x][y - 1].setState(9);
				checkFirstTile(x, y - 1);
			}

			if (y > 0 && board[x][y - 1] != 10) {
				boardButtons[x][y - 1].setState(board[x][y - 1]);
			}

			// Below Right
			if (x > 0 && y > 0 && board[x - 1][y - 1] == 9 && (boardButtons[x - 1][y - 1].getState() != 9)) {
				boardButtons[x - 1][y - 1].setState(9);
				checkFirstTile(x - 1, y - 1);
			}

			if (x > 0 && y > 0 && board[x - 1][y - 1] != 10) {

				boardButtons[x - 1][y - 1].setState(board[x - 1][y - 1]);
			}

		}

	}

	public static void checkFirstTile2(int x, int y) {

		if (board[x][y] != 10) {

			if (debug) {
				System.out.println("[DEBUG] - Tile Check method callled at: [" + x + "] [" + y + "], Count: " + count);
				count++;
			}

			// Above
			if (x + 1 < board.length && board[x + 1][y] == 9 && (boardButtons[x + 1][y].getState() != 9)) {
				boardButtons[x + 1][y].setState(board[x][y]);
				checkFirstTile(x + 1, y);
			}

			// Below
			if (x > 0 && board[x - 1][y] == 9 && (boardButtons[x - 1][y].getState() != 9)) {
				boardButtons[x - 1][y].setState(9);
				checkFirstTile(x - 1, y);
			}
			// Above Left
			if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1] == 9
					&& (boardButtons[x + 1][y + 1].getState() != 9)) {
				boardButtons[x + 1][y + 1].setState(9);
				checkFirstTile(x + 1, y + 1);
			}
			// Left
			if (y + 1 < board[0].length && board[x][y + 1] == 9 && (boardButtons[x][y + 1].getState() != 9)) {
				boardButtons[x][y + 1].setState(9);
				checkFirstTile(x, y + 1);
			}
			// Below Left
			if (x > 0 && y + 1 < board[0].length && board[x - 1][y + 1] == 9
					&& (boardButtons[x - 1][y + 1].getState() != 9)) {
				boardButtons[x - 1][y + 1].setState(9);
				checkFirstTile(x - 1, y + 1);
			}
			// Above Right
			if (x + 1 < board.length && y > 0 && board[x + 1][y - 1] == 9
					&& (boardButtons[x + 1][y - 1].getState() != 9)) {
				boardButtons[x + 1][y - 1].setState(9);
				checkFirstTile(x + 1, y - 1);
			}
			// Right
			if (y > 0 && board[x][y - 1] == 9 && (boardButtons[x][y - 1].getState() != 9)) {
				boardButtons[x][y - 1].setState(9);
				checkFirstTile(x, y - 1);
			}
			// Below Right
			if (x > 0 && y > 0 && board[x - 1][y - 1] == 9 && (boardButtons[x - 1][y - 1].getState() != 9)) {
				boardButtons[x - 1][y - 1].setState(9);
				checkFirstTile(x - 1, y - 1);
			}

		}

	}

	public static void spawnNewMineExcluding(int x, int y) {
		boolean foundSpot = false;

	}

	public static void newGameBoard(int excluding[]) {

		initializeBoard();
		checkTiles(excluding);
		updateBoard();

	}

	public static void spawnMines(int[] excluding) {
		int bombsPlaced = 0;

		while (bombsPlaced < maxBombs) {

			int x = (int) (Math.random() * boardSizeX);
			int y = (int) (Math.random() * boardSizeY);

			boolean mineInSurrounding = false;

			// Make sure the first spot and surrounding spaces do not contain mines
			if (board[x][y] != 10) {

				// Above
				if (x + 1 < board.length && board[x + 1][y] == 10) {
					mineInSurrounding = true;
				}
				// Below
				if (x > 0 && board[x - 1][y] == 10) {
					mineInSurrounding = true;
				}
				// Above Left
				if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1] == 10) {
					mineInSurrounding = true;
				}
				// Left
				if (y + 1 < board[0].length && board[x][y + 1] == 10) {
					mineInSurrounding = true;
				}
				// Below Left
				if (x > 0 && y + 1 < board[0].length && board[x - 1][y + 1] == 10) {
					mineInSurrounding = true;
				}
				// Above Right
				if (x + 1 < board.length && y > 0 && board[x + 1][y - 1] == 10) {
					mineInSurrounding = true;
				}
				// Right
				if (y > 0 && board[x][y - 1] == 10) {
					mineInSurrounding = true;
				}
				// Below Right
				if (x > 0 && y > 0 && board[x - 1][y - 1] == 10) {
					mineInSurrounding = true;
				}
				// Set the board tile to the # of surrounding mines
				if (debug && mineInSurrounding) {
					System.out.println(
							"[DEBUG] Attempted to spawn mine at " + x + ", " + y + " but it was in the start zone. ");
				}
			}

			if (!mineInSurrounding) {

				board[x][y] = 10;
				bombsPlaced++;
			}
		}

	}

	public static void initializeBoard() {

		// Class variables
		tileClicks = 0;

		running = true;
		debug = true;
		alive = true;
		correctFlags = 0;
		timeElapsed = 0;
		firstClick = true;

		// Gameboard arrays
		board = new int[boardSizeX][boardSizeY]; // Stores types of board on the gameboard

		nonBombTiles = (boardSizeX * boardSizeY) - maxBombs;

		mainPane = new BorderPane();
		gamePane = new GridPane();
		topBarPane = new HBox();

		boardButtons = new Tile[boardSizeX][boardSizeY];

		animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> {

			if (debug) {
				// System.out.println("[DEBUG] - Animation called! Time: " + timeElapsed);
			}
			timeElapsed++;
			timeElapsedDisplay.setDisplay(timeElapsed);

		}));

		animation.setCycleCount(Timeline.INDEFINITE);

		bombsRemaining = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = 0;
			}
		}

	}

	public static void updateBoard() {

		for (int i = 0; i < boardSizeX; i++) {
			for (int j = 0; j < boardSizeY; j++) {
				boardButtons[i][j] = new Tile(48, 48, true, true);
				Tile button = boardButtons[i][j];
				int boardNumI = i;
				int boardNumJ = j;

				if (board[i][j] == 10) {
					bombsRemaining++;
				}

				// button.setState(board[i][j]);

				button.setOnMouseClicked(e -> {
					MouseButton mbutton = e.getButton();
					if (mbutton == MouseButton.PRIMARY) {

						checkFirstTile(boardNumI, boardNumJ);

						if (firstClick == true) {
							animation.play();
							firstClick = false;
							tempExcluding[0] = boardNumI;
							tempExcluding[1] = boardNumJ;
							newGameBoard(tempExcluding);
						}

						if (debug && !boardButtons[boardNumI][boardNumJ].isFlagged()) {
							System.out.println("[DEBUG] Button Clicked at: " + boardNumI + ", " + boardNumJ
									+ ".\t Setting state to " + board[boardNumI][boardNumJ]);

							System.out.println("[DEBUG] isFlagged: " + boardButtons[boardNumI][boardNumJ].isFlagged());
						}

						// Check if all board other than mines have been uncovered

						if (!button.isClicked() && !boardButtons[boardNumI][boardNumJ].isFlagged()) {
							tileClicks++;
						}

						// Set the graphic of the Tile object based on the int in the board[][] array

						if (alive == true && !button.isClicked() && !boardButtons[boardNumI][boardNumJ].isFlagged()) {
							button.setState(board[boardNumI][boardNumJ]);
						}

						if (board[boardNumI][boardNumJ] == 10 && !boardButtons[boardNumI][boardNumJ].isFlagged()) {
							gameFace.setState(2);
							alive = false;
							animation.stop();

							for (int k = 0; k < boardSizeX; k++) {

								for (int l = 0; l < boardSizeY; l++) {

									boardButtons[k][l].setState(board[k][l]);

									boardButtons[k][l].checkFlag();

									boardButtons[k][l].setDisable(true);
								}

							}

							boardButtons[boardNumI][boardNumJ].setState(11);

						}
						if (!boardButtons[boardNumI][boardNumJ].isFlagged()) {
							button.clicked();
						}

						if (tileClicks >= nonBombTiles || correctFlags == maxBombs) {
							gameFace.setState(3);
						}

					} else if (mbutton == MouseButton.SECONDARY) {
						if (debug) {
							System.out.println("[DEBUG] Secondary mouse button clicked!");
						}

						if (boardButtons[boardNumI][boardNumJ].isFlagged()) {
							boardButtons[boardNumI][boardNumJ].flag(false);
							bombsRemaining++;
							if (boardButtons[boardNumI][boardNumJ].isFlagAndMine()) {
								correctFlags--;
							}
							bombsRemainingDisplay.setDisplay(bombsRemaining);
						} else if (bombsRemaining > 0) {
							boardButtons[boardNumI][boardNumJ].flag(true);

							if (boardButtons[boardNumI][boardNumJ].isFlagAndMine()) {
								correctFlags++;
							}

							bombsRemaining--;
							bombsRemainingDisplay.setDisplay(bombsRemaining);
						}

						if (tileClicks >= nonBombTiles || correctFlags == maxBombs) {
							gameFace.setState(3);
						}

					}
				});

				gamePane.add(button, j, i);

			}
		}
	}

	private static void checkTiles(int[] excluding) {

		// Add fixed location mines for testing

		if (!presetBoard) {
			spawnMines(excluding);
		} else {
			board[1][1] = 10;
			board[2][3] = 10;
			board[3][3] = 10;
			board[1][3] = 10;
		}

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
						board[x][y] = 9; // Set to blank tile
					}

				}

			}
		}

	}

}
