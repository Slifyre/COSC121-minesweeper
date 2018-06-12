import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends Button {
	
	//v2 - Saves memory by loading img assets when needed instead of pre-loading all images for every tile

	private boolean clicked = false;
	private boolean flagged = false;
	private boolean isMine = false;
	private int state = 0;
	private int scaleX = 64;
	private int scaleY = 64;

	public Tile(int scaleX, int scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;

		setMinWidth(scaleX);
		setMaxWidth(scaleX);
		setMinHeight(scaleY);
		setMaxHeight(scaleY);

		setState(9);
	}
	
	public void setIsMine(boolean isMine) {
		this.isMine = isMine;
	}

	public void setClicked(boolean isClicked) {
		clicked = isClicked;
	}

	public boolean isClicked() {
		return clicked;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void checkFlag() {
		System.out.println("Flagged: " + flagged + "\tMine: " + isMine);
		
		if(getState() == 11) {
			setState(11);
		}else if(isMine && !flagged) {
			setState(10);
		}else if(!isMine && flagged) {
			setState(13);
		}
		
		
	}

	public boolean isFlagAndMine() {
		System.out.println("Flagged: " + flagged + "\tMine: " + isMine);
		if (isMine && flagged) {
			return true;
		}
		return false;
	}

	public void flag(boolean flagged) {
		this.flagged = flagged;
		if (flagged == true) {
			setState(12);
		} else {
			setState(9);
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int setState) {
		
		if(!flagged || setState == 9 || setState == 10 || setState == 11 || setState == 12 || setState == 13) {
			state = setState;
		}

		boolean preserveRatio = true;
		boolean smoothing = false;
		
		if (GameClient.debug) {
			System.out.println("[DEBUG] Setting tile to state: " + state);
		}
		
		clicked = true;

		switch(state) {
		
		case 0:
			setGraphic(new ImageView(new Image("file:res/reg_digits/0.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 1:
			setGraphic(new ImageView(new Image("file:res/reg_digits/1.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 2:
			setGraphic(new ImageView(new Image("file:res/reg_digits/2.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 3:
			setGraphic(new ImageView(new Image("file:res/reg_digits/3.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 4:
			setGraphic(new ImageView(new Image("file:res/reg_digits/4.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 5:
			setGraphic(new ImageView(new Image("file:res/reg_digits/5.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 6:
			setGraphic(new ImageView(new Image("file:res/reg_digits/6.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 7:
			setGraphic(new ImageView(new Image("file:res/reg_digits/7.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 8:
			setGraphic(new ImageView(new Image("file:res/reg_digits/8.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 9:
			setGraphic(new ImageView(new Image("file:res/tiles/cover.png", scaleX, scaleY, preserveRatio, smoothing)));
			clicked = false;
			break;
		case 10:
			setGraphic(new ImageView(new Image("file:res/tiles/mine-grey.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 11:
			setGraphic(new ImageView(new Image("file:res/tiles/mine-red.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 12:
			setGraphic(new ImageView(new Image("file:res/tiles/flag.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		case 13:
			setGraphic(new ImageView(new Image("file:res/tiles/mine-misflagged.png", scaleX, scaleY, preserveRatio, smoothing)));
			break;
		default:
			// Default
			System.err.println("Error: No ImageButton graphic selected.");
			setGraphic(new ImageView(new Image("file:res/reg_digits/0.png", scaleX, scaleY, preserveRatio, smoothing)));
		}
		
	}

}
