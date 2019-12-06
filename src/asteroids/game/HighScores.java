package asteroids.game;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class HighScores
{
    private PrintWriter writer;

    private File highScoreList;

    private FileOutputStream highScores;

    /**
     * Creates a high score text file (if there isn't one defined with that name)
     */
    public HighScores(String s)
    {
        try
        {
            highScoreList = new File(s);
        }
        catch(Exception e)
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
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(highScoreList, true))))
        {
            writer.write(mark);
            writer.write("\n");
            
        }catch (Exception e) {
		    e.printStackTrace();
		}
    }

    
    public void sortFile(){
        sortFile(this.highScoreList);
    }
    private void sortFile(File file)
    {
        FileSorter.sort(this.highScoreList);
    }
}