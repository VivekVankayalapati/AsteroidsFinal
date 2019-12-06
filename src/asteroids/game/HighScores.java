package asteroids.game;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class HighScores
{
    private PrintWriter writer;

    private FileWriter highScores;

    /**
     * Creates a high score text file (if there isn't one defined with that name)
     */
    public HighScores(String s)
    {
        try (FileWriter file = new FileWriter(s, true))
        {
            highScores = file;
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("That file is not available");
        }
    }

    /**
	 * Logs specific data
	 * @param mark is the object (string) that contains the data that you want to have logged
	 * @param isTitle is a boolean that describes whether or not the object is a section title (such as a subsystem name)
	 */
	public void log(String mark) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(highScores)))
        {
            writer.print(mark);
            this.sort();
        }catch (Exception e) {
		    e.printStackTrace();
		}
    }

    private void sort() {
    }
}