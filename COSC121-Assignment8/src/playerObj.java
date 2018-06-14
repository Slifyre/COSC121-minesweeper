import java.io.Serializable;

@SuppressWarnings("serial")
public class playerObj implements Serializable {

	private int score;
	private String name;

	public playerObj(int score, String name) {
		setScore(score);
		setName(name);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
