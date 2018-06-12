import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameClient extends Application {
	
	//Initial Values
	private static int initialMines = 10;
	private static int remainingMines = 0;
	private static int correctFlags = 0;
	private static int boardSizeX = 8;
	private static int boardSizeY = 8;
	private static int tileClicks = 0;
	
	private static int timeElapsed = 0;
	
	private static boolean firstClick = true;
	private static boolean alive = true;
	protected static boolean debug = true;
	private static boolean win = false;

	//Panes
	private static BorderPane mainPane; //Main window
	private static MenuBar menuPane; // holds options (mainPane Top)
	private static HBox topPane; //Holds counters and face (mainPane Center)
	private static GridPane gamePane; //Holds mines/buttons (mainPane Bottom)

	//Menu
	private static Menu difficultyMenu;
	private static Menu fileMenu;
	private static MenuItem saveGame;
	private static MenuItem loadGame;
	private static MenuItem highScores;
	private static MenuItem beginner; // 10 Mines. 8x8 board
	private static MenuItem intermediate; // 40 Mines. 16x16 board
	private static MenuItem expert; // 99 Mines. 32x16 board
	
	//Top Info Bar
	private static Display remainingMinesDisplay;
	private static Face gameFace;
	private static Display timeElapsedDisplay;
	private static Timeline animation;
	
	//Game Board
	private static int[][] board; //Holds states of all tiles
	private static Tile boardButtons[][]; //Holds actual button objects
	
	//Main method
	public static void main(String[] args) {
		launch(args);
	}
	
	//Start method
	@Override
	public void start(Stage window) {
		
		//Game Variables
		win = false;
		firstClick = true;
		alive = true;
		win = false;
		board = new int[boardSizeX][boardSizeY];
		correctFlags = 0;
		timeElapsed = 0;
		tileClicks = 0;
		remainingMines = initialMines;
		
		//Main BorderPane
		mainPane = new BorderPane();
		
		//Menu Items
		menuPane = new MenuBar();
		difficultyMenu = new Menu("Difficulty");
		beginner = new MenuItem("Beginner");
		intermediate = new MenuItem("Intermediate");
		expert = new MenuItem("Expert");
		
		fileMenu = new Menu("File");
		highScores = new MenuItem("High Scores");
		saveGame = new MenuItem("Save Game");
		loadGame = new MenuItem("Load Game");
		fileMenu.getItems().add(highScores);
		fileMenu.getItems().add(loadGame);
		fileMenu.getItems().add(saveGame);
		
		difficultyMenu.getItems().add(beginner);
		difficultyMenu.getItems().add(intermediate);
		difficultyMenu.getItems().add(expert);
		
		menuPane.getMenus().add(fileMenu);
		menuPane.getMenus().add(difficultyMenu);
		
			//Difficulty Options
		beginner.setOnAction(e -> {
			initialMines = 10;
			boardSizeX = 8;
			boardSizeY = 8;
			animation.stop();
			start(window);
		});

		intermediate.setOnAction(e -> {
			initialMines = 40;
			boardSizeX = 16;
			boardSizeY = 16;
			animation.stop();
			start(window);
		});

		expert.setOnAction(e -> {
			initialMines = 99;
			boardSizeX = 32;
			boardSizeY = 16;
			animation.stop();
			start(window);
		});
		
		
		mainPane.setTop(menuPane);
		
		//Top Info Bar
		topPane = new HBox();
		topPane.setPadding(new Insets(12, 12, 12, 12));
		topPane.setAlignment(Pos.CENTER);
		
			//Bombs Remaining Display
		remainingMinesDisplay = new Display(64, 64, true, true);
		remainingMinesDisplay.setDisplay(remainingMines);
		remainingMinesDisplay.setAlignment(Pos.CENTER_LEFT);
		topPane.getChildren().add(remainingMinesDisplay);
		
			//Face
		gameFace = new Face(64, 64);
		gameFace.setOnAction(e -> {
			//Resets the game when clicked
			animation.stop();
			start(window);
		});
		topPane.getChildren().add(gameFace);
		
			//Time Elapsed Display
		timeElapsedDisplay = new Display(64, 64, true, true);
		timeElapsedDisplay.setDisplay(0);
		animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
			timeElapsed++;
			timeElapsedDisplay.setDisplay(timeElapsed);
		}));
		animation.setCycleCount(Timeline.INDEFINITE);
		topPane.getChildren().add(timeElapsedDisplay);
		timeElapsedDisplay.setAlignment(Pos.CENTER_RIGHT);
		
		mainPane.setCenter(topPane);
		
		//Game Pane
		gamePane = new GridPane();
		gamePane.setAlignment(Pos.CENTER);
		mainPane.setBottom(gamePane);
		
		setBoardButtons();
		
		//Scene + Stage
		
			//CSS Styling
		mainPane.setStyle(
				"-fx-background-color: #bfbfbf;-fx-border-color: #fafafa #787878 #787878 #fafafa; -fx-border-width: 9; -fx-border-radius: 0.001;");
		topPane.setStyle(
				"-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width: 6; -fx-border-radius: 0.001;");
		
		
		window.setOnCloseRequest(e -> {
			if (debug) {
				System.out.println("[DEBUG] Closing Program.");
			}
		});
		
		window.getIcons().add(new Image("file:res/tiles/mine-grey.png"));
		window.setTitle("Minesweeper");
		window.setScene(new Scene(mainPane));
		window.show();
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
				
				currentTile.setOnMouseClicked(e -> {
					MouseButton mbutton = e.getButton();
					if (mbutton == MouseButton.PRIMARY && alive && !currentTile.isFlagged() && !win) {
						
						if(board[x][y] == 10) {
							
							alive = false;
							boardButtons[x][y].setState(11);
							gameFace.setState(2);
							animation.stop();
							
							//Show Mines
							for(int l = 0; l < boardSizeX; l++) {
								for (int k = 0; k < boardSizeY; k++) {
									if(board[l][k] == 10) {
										boardButtons[l][k].setIsMine(board[l][k] == 10 ? true:false);
										boardButtons[l][k].checkFlag();
									}
								}
							}
							
						
						}else if(firstClick == true) {
							animation.play();
							spawnMines(x,y);
							checkTiles();
							//seeTiles();
							firstClick = false;							
							checkSurroundingTiles(x,y);
							boardButtons[x][y].setState(board[x][y]);
						}else {
							checkSurroundingTiles(x,y);
							boardButtons[x][y].setState(board[x][y]);
						}
						
						
						
						
					} else if (mbutton == MouseButton.SECONDARY && alive && !win) {
						
						boardButtons[x][y].setIsMine(board[x][y] == 10 ? true:false);
						
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
								System.out.println("[DEBUG] - Flag and Mine!");
							}

							remainingMines--;
							remainingMinesDisplay.setDisplay(remainingMines);
						}
						
					}
					
					if(correctFlags == initialMines) {
						gameFace.setState(3);
						animation.stop();
						win = true;
					}
					
				}); //End setOnMouseClicked
				
			}//End inner for
			
		}//End outer for
	}//end setBoardButtons method
	
	private static void spawnMines(int x, int y) {
		//Spawn mines excluding the space at [x,y] and surrounding tiles.
		int spawnedMines = 0;
		
		if(debug) {
			System.out.println("[DEBUG] - Spawning mines excluding ["+x+"]["+y+"], and surrounding spacex");
		}
		
		while(spawnedMines < initialMines) {
			
			int randX = (int) (Math.random() * boardSizeX);
			int randY = (int) (Math.random() * boardSizeY);
			
			int distance = (int) Math.sqrt(Math.pow(randX - x, 2) + Math.pow(randY - y, 2));
			System.out.println("Approx Distance: " + distance);
			
			if(distance > 2 && board[randX][randY] != 10) {
				
				
				if(debug) {
					System.out.println("[DEBUG] - Randomized Mine at ["+randX+"]["+randY+"] #:" + spawnedMines);
				}
				
				board[randX][randY] = 10;
				spawnedMines++;
			}
		
		
		}
		if(debug) {
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

		//Checks if the surrounding tiles are blank spaces and recursively opens them if they are
		
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
		//Checks the board for mines, and sets up the corresponding numbers around them

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
