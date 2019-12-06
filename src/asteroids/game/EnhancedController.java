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

    /**
     * TODO Docs
     */
    protected int lastScore;

    protected Timer powerupTimer;
    protected Timer invincibilityTimer;

    protected SoundManager newLife;

    public EnhancedController(){
        super();
        newLife = new SoundManager("/sounds/smb_1-up.wav");
        powerupTimer = new Timer(POWERUP_TIMER, this);
        powerupTimer.start();
        invincibilityTimer = new Timer(750, this);
    }

    /**
     * Sets things up and begins a new game.
     */
    protected void initialScreen ()
    {
        super.initialScreen();
        powerupTimer.stop();
        powerupTimer.setDelay(POWERUP_TIMER);
        powerupTimer.start();
        if(invincibilityTimer.isRunning()){
            invincibilityTimer.stop();
        }
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if(e.getSource() instanceof JButton || e.getSource() == heartBeat || e.getSource() == alienTimer) {
            super.actionPerformed(e);
        }
        else if(e.getSource() == invincibilityTimer && hasInvincibility())
        {
            invincibilityTimer.stop();
        }
        else if (e.getSource() == powerupTimer && level >= 2 && countAsteroids() > 0)
        {
            // Sets a powerup 60% of the time.
            if(RANDOM.nextInt(10) > 3 && lives > 0){
                
                OneUp oneup = new OneUp(RANDOM.nextDouble() * SIZE, RANDOM.nextDouble() * SIZE, POWERUP_DURATION, this);
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
            display.setLevel(this.level);
            display.setScore(this.score);
            display.setLives(this.lives);

                        
            if(getNewLife(EXTRA_LIFE_SCORE))
            {
                
                lives += 1;
                newLife.playSound();
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
    @Override
    protected void placeShip()
    {
        super.placeShip();
        invincibilityTimer.start();
    }

    /**
     * TODO Docs
     */
    protected boolean getNewLife(int score){

        return Math.abs(this.score - lastScore) >= score;
    }

    /**
     * TODO Docs
     * @param p
     */
    public void OneUpDestroyed(Participant p) 
    {
        if(p instanceof Ship && p.equals(this.ship))
        {
            lives += 1;
            newLife.playSound();
        }
	}

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e) {
        super.keyPressed(e);
        // Only refreshes through the keyPressed method because it's hard to time 1 frame of teleport.
        if(e.getKeyCode() == KeyEvent.VK_CONTROL && ship != null ||e.getKeyCode() == KeyEvent.VK_T && ship != null){
            ship.teleport();
        }
        // Gives the player 99 lives when pausebreak is pressed (only in single player enhanced)
        if(e.getKeyCode() == KeyEvent.VK_PAUSE){
            newLife.playSound();
            lives = 99;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT){
            newLevel();
        }

    }

    /**
     * TODO Docs
     */
    @Override
    public boolean hasInvincibility()
    {
        return invincibilityTimer.isRunning();
    }

}
