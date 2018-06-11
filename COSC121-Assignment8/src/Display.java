import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Display extends HBox {

	private static ImageView hundredsDigit = new ImageView();
	private static ImageView tensDigit = new ImageView();
	private static ImageView onesDigit = new ImageView();

	int scaleX;
	int scaleY;
	boolean preserveRatio;
	boolean smoothing;

	public Display(int scaleX, int scaleY, boolean preserveRatio, boolean smoothing) {

		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.preserveRatio = preserveRatio;
		this.smoothing = smoothing;

	}

	public void setDisplay(int number) {

		int hundreds = number / 100;
		number -= 100 * hundreds;
		int tens = number / 10;
		number -= 10 * tens;
		int ones = number;

		if (GameClient.debug) {
			// System.out.println("[DEBUG] New display created with: " + hundreds + "
			// hundreds, " + tens + " tens, " + ones
			// + " ones.");
		}

		hundredsDigit = new ImageView(
				new Image("file:res/led_digits/" + hundreds + ".png", scaleX, scaleY, preserveRatio, smoothing));
		tensDigit = new ImageView(
				new Image("file:res/led_digits/" + tens + ".png", scaleX, scaleY, preserveRatio, smoothing));
		onesDigit = new ImageView(
				new Image("file:res/led_digits/" + ones + ".png", scaleX, scaleY, preserveRatio, smoothing));

		addToDisplay();
	}

	public void addToDisplay() {
		getChildren().clear();
		getChildren().add(hundredsDigit);
		getChildren().add(tensDigit);
		getChildren().add(onesDigit);
		setAlignment(Pos.CENTER);
	}

}