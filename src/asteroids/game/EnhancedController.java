package asteroids.game;

import static asteroids.game.Constants.*;
import asteroids.participants.Ship;

/**
 * Controls a game of Asteroids.
 */
public class EnhancedController extends Controller
{

    protected int lastScore;

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Sets things up and begins a new game.
     */
    protected void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids();

        // Place the ship
        placeShip();
        // Reset statistics
        lives = 3;
        score = 0;
        lastScore = 0;
        level = 1;

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }


    /**
     * If the transition time has been reached, transition to a new state
     */
    protected void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                finalScreen();
            }
            else if(countAsteroids() > 0)
            {
                if(shipDestroyed)
                {
                    placeShip();
                }
                
            }
            else
            {
                newLevel();
            }
        }
    }

    /**
     * TODO Docs
     * @param ship
     * @return
     */
    public boolean getIsAccel(Ship ship)
    {
        return accelerate;
    }

}
