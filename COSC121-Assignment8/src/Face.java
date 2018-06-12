import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Face extends Button {

	protected int faceState = 0;
	protected ImageView deadFace, supriseFace, smileFace, winFace;

	public Face(int scaleX, int scaleY) {
		
		boolean preserveRatio = true;
		boolean smoothing = true;

		deadFace = new ImageView(new Image("file:res/faces/face-dead.png", scaleX, scaleY, preserveRatio, smoothing));
		supriseFace = new ImageView(new Image("file:res/faces/face-o.png", scaleX, scaleY, preserveRatio, smoothing));
		smileFace = new ImageView(new Image("file:res/faces/face-smile.png", scaleX, scaleY, preserveRatio, smoothing));
		winFace = new ImageView(new Image("file:res/faces/face-win.png", scaleX, scaleY, preserveRatio, smoothing));

		deadFace.setFitWidth(scaleX);
		deadFace.setFitHeight(scaleY);

		supriseFace.setFitWidth(scaleX);
		supriseFace.setFitHeight(scaleY);

		smileFace.setFitWidth(scaleX);
		smileFace.setFitHeight(scaleY);

		winFace.setFitWidth(scaleX);
		winFace.setFitHeight(scaleY);

		setMinWidth(scaleX);
		setMaxWidth(scaleX);
		setMinHeight(scaleY);
		setMaxHeight(scaleY);

		setGraphic(smileFace);

	}

	public void setState(int state) {
		faceState = state;

		if (faceState == 0) {
			setGraphic(smileFace);
			if (GameClient.debug) {
				System.out.println("[DEBUG] Face graphic set to: Smiling Face");
			}
		} else if (faceState == 1) {
			setGraphic(supriseFace);
			if (GameClient.debug) {
				System.out.println("[DEBUG] Face graphic set to: Suprised Face");
			}
		} else if (faceState == 2) {
			setGraphic(deadFace);
			if (GameClient.debug) {
				System.out.println("[DEBUG] Face graphic set to: Dead Face");
			}
		} else if (faceState == 3) {
			setGraphic(winFace);
			if (GameClient.debug) {
				System.out.println("[DEBUG] Face graphic set to: Sunglasses Face");
			}
		} else {
			System.err.println("Error: No Face graphic selected.");
		}

	}

}
