package asteroids.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**Sorts high score file */
public class FileSorter {

	public static void sort(File highScoreList)
	{
		ArrayList<Score> scores = new ArrayList<>();

		try(BufferedReader reader = new BufferedReader(new FileReader(highScoreList)))
		{
			String currentLine = reader.readLine();

			while (currentLine != null)
			{
				String[] score = currentLine.split("\t");

				String name = score[0];

				int currentScore = Integer.valueOf(score[1]);

				scores.add(new Score(name, currentScore));

				currentLine = reader.readLine();
			}
		}catch(IOException e) 
		{
			
		}

		Collections.sort(scores, new marksCompare());
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(highScoreList)))
		{

			for (Score score : scores)
			{
				writer.write(score.name);

				writer.write("\t" + score.marks);

				writer.newLine();
			}

		}catch(Exception e){
			
		}
	}
	/**Manages scoring */
	static class Score
	{
       String name;

       int marks;

       public Score(String name, int score)
       {
              this.name = name;

              this.marks = score;
       }
	}
	/**Compares scores to determine high scores */
	static class marksCompare implements Comparator<Score>
{
		@Override
		public int compare(Score s1, Score s2) {
			return s2.marks - s1.marks;
			
		}
}

}
