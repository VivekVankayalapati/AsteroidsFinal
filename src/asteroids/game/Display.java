package asteroids.game;

import javax.swing.*;
import static asteroids.game.Constants.*;
import java.awt.*;

/**
 * Defines the top-level appearance of an Asteroids game.
 */
@SuppressWarnings("serial")
public class Display extends JFrame
{
    /** The area where the action takes place */
    private Screen screen;

    /**
     * Lays out the game and creates the controller2p
     */
    public Display (Controller controller)
    {
        // Title at the top
        setTitle(TITLE);

        // Default behavior on closing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main playing area and the controller2p
        screen = new Screen(controller);

        // This panel contains the screen to prevent the screen from being
        // resized
        
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());
        screenPanel.add(screen);
        

        // This panel contains buttons and labels
        JPanel controls = new JPanel();
        

        // The button that starts the game
        JButton startGame = new JButton(START_LABEL);
        controls.add(startGame);

        // Organize everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(screenPanel, "Center");
        mainPanel.add(controls, "North");
        setContentPane(mainPanel);
        pack();

        // Connect the controller2p to the start button
        startGame.addActionListener(controller);
    }

    /**
     * Called when it is time to update the screen display. This is what drives the animation.
     */
    public void refresh ()
    {
        screen.repaint();
    }

    /**
     * Sets the large legend
     */
    public void setLegend (String s)
    {
        screen.setLegend(s);
    }

    public void setScore(int score, int player){
        screen.setScore(score, player);
    }
    /**
     * Places score on screen
     */
    public void setScore(int score){
        screen.setScore(score, 1);
    }
    /**
     * Places level on screen
     */
    public void setLevel(int level){
        screen.setLevel(level);
    }
    /**
     * Places players on screen
     */
    public void setPlayers(int players){
        screen.setPlayers(players);
    }
    /**
     * Places lives on screen by player
     */
    public void setLives(int lives, int player){
        screen.setLives(lives, player);
    }
    /**
     * Places lives for single player
     */
    public void setLives(int lives){
        screen.setLives(lives, 1);
    }

    /**Shows scores */
    public void showScores()
    {
        screen.showScores();
	}
    /**Sets the high score on the screen */
	public void setHighScore(int highScore) {
        screen.setHighScore(highScore);
	}
    /**Clears display of scores */
	public void removeScores() {
        screen.removeScores();
	}
}
