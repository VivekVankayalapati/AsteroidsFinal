package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import javax.swing.*;

import asteroids.participants.OneUp;
import asteroids.participants.Ship;


/**
 * Controls a game of Asteroids.
 */
public class EnhancedController extends Controller
{

    protected int lastScore;

    protected Timer powerupTimer;

    public EnhancedController(){
        super();
        powerupTimer = new Timer(10000, this);
        powerupTimer.start();
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
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if(e.getSource() instanceof JButton || e.getSource() == heartBeat) {
            super.actionPerformed(e);
        }
        else if (e.getSource() == powerupTimer && level >= 2)
        {
            // Sets a powerup 60% of the time.
            if(RANDOM.nextInt(10) > 3 && lives > 0){
                
                OneUp oneup = new OneUp(SIZE/2, SIZE/2, POWERUP_DURATION, this);
                // Increases the time between powerups between each placement of the powerup
                powerupTimer.setDelay(POWERUP_TIMER + POWERUP_DELTA);
                addParticipant(oneup);
            }
        }
        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            if(turnLeft && ship != null){
                ship.turnLeft();
            }
            if(turnRight && ship != null){
                ship.turnRight();
            }
            if(fire && ship != null){
                ship.shoot();
            }
            if(accelerate && ship != null){
                ship.accelerate();

            }
                        
            if(getNewLife(EXTRA_LIFE_SCORE))
            {
                
                lives += 1;
                lastScore = score; // zero's the score so new lives aren't infinitely added
            }
            

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();
        }
    }

    /**
     * TODO Docs
     */
    protected boolean getNewLife(int score){

        return Math.abs(this.score - lastScore) >= score;
    }

    /**
     * TODO Docs
     * @param ship
     */
    public void OneUpDestroyed(Participant ship) 
    {
        if(ship instanceof Ship && ship.equals(this.ship))
        {
            lives += 1;
        }
	}

}
