import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class HighScores {

	static ArrayList<playerObj> hsl = new ArrayList<playerObj>(3);

	public static void newScoreFile(boolean overwrite) {
		File f = new File("sav/highScores.dat");

		if (!f.exists() || overwrite) {
			try {
				f.createNewFile();
				hsl.clear();

				for (int i = 0; i < 3; i++) {
					hsl.add(new playerObj(0, "---"));
				}

				// hsl.set(0, new playerObj(0, "---"));
				// hsl.set(1, new playerObj(0, "---"));
				// hsl.set(2, new playerObj(0, "---"));

				FileOutputStream fOut = new FileOutputStream(f);
				ObjectOutputStream objOut = new ObjectOutputStream(fOut);

				objOut.writeObject(hsl);

				fOut.close();
				objOut.close();

			} catch (IOException e) {
				System.out.println("[ERROR] - Error creating new HighScore.dat file.");
			}
		}

	}

	public static String[] getHighScores() {

		File f = new File("sav/highScores.dat");

		if (f.exists()) {
			loadHighScores();
		} else {
			newScoreFile(true);
		}

		String[] output = new String[3];

		for (int i = 0; i < 3; i++) {
//
//			if (i == 0) {
//				output += "Beginner \t";
//			} else if (i == 1) {
//				output += "Intermediate \t";
//			} else {
//				output += "Expert \t";
//			}

			output[i] = (hsl.get(i).getScore() + " seconds.\t" + hsl.get(i).getName());

		}

		return output;
	}
	
	public static int getFastestTime(int time, int difficulty) {

		File f = new File("sav/highScores.dat");

		if (f.exists()) {
			loadHighScores();
		} else {
			newScoreFile(true);
		}
		
		if(difficulty == 0) {
			return hsl.get(0).getScore();
		}else if(difficulty == 1) {
			return hsl.get(1).getScore();
		}else if(difficulty == 2) {
			return hsl.get(2).getScore();
		}
		

		return 0;
	}
	
	

	@SuppressWarnings("unchecked")
	public static void loadHighScores() {

		File f = new File("sav/highScores.dat");

		if (f.exists()) {
			try {

				FileInputStream fIn = new FileInputStream(f);
				ObjectInputStream objIn = new ObjectInputStream(fIn);

				try {

					hsl = (ArrayList<playerObj>) objIn.readObject();

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (IOException e) {
				System.out.println("[ERROR] - Error reading new HighScore.dat file.");
			}
		}

	}

	public static void addHighScore(int score, int difficulty, String name) {

		File f = new File("sav/highScores.dat");

		if (f.exists()) {
			loadHighScores();
		} else {
			newScoreFile(true);
		}

		switch (difficulty) {

		case 0:
			hsl.set(0, new playerObj(score, name));
			break;
		case 1:
			hsl.set(1, new playerObj(score, name));
			break;
		case 2:
			hsl.set(2, new playerObj(score, name));
			break;
		}

		if (f.exists()) {
			try {

				FileOutputStream fOut = new FileOutputStream(f);
				ObjectOutputStream objOut = new ObjectOutputStream(fOut);

				objOut.writeObject(hsl);

				fOut.close();
				objOut.close();

			} catch (IOException e) {
				System.out.println("[ERROR] - Error creating new HighScore.dat file.");
			}
		}

	}

}
