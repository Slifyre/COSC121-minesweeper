import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends Button {

	private boolean clicked = false;
	private boolean flagged = false;
	private boolean isMine = false;
	private int state = 0;

	private ImageView coverTile, blankTile, bombTile, redBombTile, flaggedTile, misflaggedTile, oneTile, twoTile,
			threeTile, fourTile, fiveTile, sixTile, sevenTile, eightTile;

	public Tile(int scaleX, int scaleY, boolean preserveRatio, boolean smoothing) {
		blankTile = new ImageView(new Image("file:res/reg_digits/0.png", scaleX, scaleY, preserveRatio, smoothing));
		coverTile = new ImageView(new Image("file:res/tiles/cover.png", scaleX, scaleY, preserveRatio, smoothing));
		bombTile = new ImageView(new Image("file:res/tiles/mine-grey.png", scaleX, scaleY, preserveRatio, smoothing));
		redBombTile = new ImageView(new Image("file:res/tiles/mine-red.png", scaleX, scaleY, preserveRatio, smoothing));
		flaggedTile = new ImageView(new Image("file:res/tiles/flag.png", scaleX, scaleY, preserveRatio, smoothing));
		misflaggedTile = new ImageView(
				new Image("file:res/tiles/mine-misflagged.png", scaleX, scaleY, preserveRatio, smoothing));
		oneTile = new ImageView(new Image("file:res/reg_digits/1.png", scaleX, scaleY, preserveRatio, smoothing));
		twoTile = new ImageView(new Image("file:res/reg_digits/2.png", scaleX, scaleY, preserveRatio, smoothing));
		threeTile = new ImageView(new Image("file:res/reg_digits/3.png", scaleX, scaleY, preserveRatio, smoothing));
		fourTile = new ImageView(new Image("file:res/reg_digits/4.png", scaleX, scaleY, preserveRatio, smoothing));
		fiveTile = new ImageView(new Image("file:res/reg_digits/5.png", scaleX, scaleY, preserveRatio, smoothing));
		sixTile = new ImageView(new Image("file:res/reg_digits/6.png", scaleX, scaleY, preserveRatio, smoothing));
		sevenTile = new ImageView(new Image("file:res/reg_digits/7.png", scaleX, scaleY, preserveRatio, smoothing));
		eightTile = new ImageView(new Image("file:res/reg_digits/8.png", scaleX, scaleY, preserveRatio, smoothing));

		setMinWidth(scaleX);
		setMaxWidth(scaleX);
		setMinHeight(scaleY);
		setMaxHeight(scaleY);

		setGraphic(coverTile);
	}

	public void clicked() {
		clicked = true;
	}

	public boolean isClicked() {
		return clicked;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void checkFlag() {
		if (isMine && flagged) {
			setGraphic(flaggedTile);
		} else if (flagged) {
			setGraphic(misflaggedTile);
		}
	}

	public boolean isFlagAndMine() {
		if (isMine && flagged) {
			return true;
		}
		return false;
	}

	public void flag(boolean flagged) {
		this.flagged = flagged;
		if (flagged == true) {
			setGraphic(flaggedTile);
		} else {
			setGraphic(coverTile);
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {

		this.state = state;

		if (GameClient.debug) {
			System.out.println("[DEBUG] Setting tile to state: " + state);
		}

		isMine = false;

		if (state == 0) {
			setGraphic(coverTile);
		} else if (state == 1) {
			setGraphic(oneTile);
		} else if (state == 2) {
			setGraphic(twoTile);
		} else if (state == 3) {
			setGraphic(threeTile);
		} else if (state == 4) {
			setGraphic(fourTile);
		} else if (state == 5) {
			setGraphic(fiveTile);
		} else if (state == 6) {
			setGraphic(sixTile);
		} else if (state == 7) {
			setGraphic(sevenTile);
		} else if (state == 8) {
			setGraphic(eightTile);
		} else if (state == 9) {
			setGraphic(blankTile);
		} else if (state == 10) {
			isMine = true;
			setGraphic(bombTile);
		} else if (state == 11) {
			setGraphic(redBombTile);
			isMine = true;
		} else if (state == 12) {
			setGraphic(flaggedTile);
			flagged = true;
		} else if (state == 13) {
			setGraphic(misflaggedTile);
		} else {
			// Default
			System.err.println("Error: No ImageButton graphic selected.");
			setGraphic(blankTile);
		}
	}

}
