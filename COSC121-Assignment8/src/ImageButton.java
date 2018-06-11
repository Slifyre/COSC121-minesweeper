import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends Button {

	protected int state = 0;
	private ImageView coverTile, blankTile, bombTile, redBombTile, flaggedTile, misflaggedTile;

	public ImageButton(int scaleX, int scaleY, boolean preserveRatio, boolean smoothing) {
		blankTile = new ImageView(new Image("file:res/reg_digits/0.png", scaleX, scaleY, preserveRatio, smoothing));
		coverTile = new ImageView(new Image("file:res/tiles/cover.png", scaleX, scaleY, preserveRatio, smoothing));
		bombTile = new ImageView(new Image("file:res/tiles/mine-grey.png", scaleX, scaleY, preserveRatio, smoothing));
		redBombTile = new ImageView(new Image("file:res/tiles/mine-red.png", scaleX, scaleY, preserveRatio, smoothing));
		flaggedTile = new ImageView(new Image("file:res/tiles/flag.png", scaleX, scaleY, preserveRatio, smoothing));
		misflaggedTile = new ImageView(
				new Image("file:res/tiles/mine-misflagged.png", scaleX, scaleY, preserveRatio, smoothing));

		setMinWidth(scaleX);
		setMaxWidth(scaleX);
		setMinHeight(scaleY);
		setMaxHeight(scaleY);

		blankTile.setFitWidth(scaleX);
		blankTile.setFitHeight(scaleY);

		bombTile.setFitWidth(scaleX);
		bombTile.setFitHeight(scaleY);

		redBombTile.setFitWidth(scaleX);
		redBombTile.setFitHeight(scaleY);

		flaggedTile.setFitWidth(scaleX);
		flaggedTile.setFitHeight(scaleY);

		misflaggedTile.setFitWidth(scaleX);
		misflaggedTile.setFitHeight(scaleY);

		coverTile.setFitWidth(scaleX);
		coverTile.setFitHeight(scaleY);

		setGraphic(coverTile);
	}

	public void setState(int state) {
		if (state == 0) {
			setGraphic(coverTile);
		} else if (state == 1) {
			setGraphic(blankTile);
		} else if (state == 2) {
			setGraphic(redBombTile);
		} else if (state == 3) {
			setGraphic(flaggedTile);
		} else if (state == 4) {
			setGraphic(misflaggedTile);
		} else if (state == 5) {
			setGraphic(bombTile);
		} else {
			// Default
			System.err.println("Error: No ImageButton graphic selected.");
			setGraphic(blankTile);
		}
	}

}
