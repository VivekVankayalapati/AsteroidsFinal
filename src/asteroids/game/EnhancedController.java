package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import javax.swing.*;

import asteroids.participants.Bullet;
import asteroids.participants.OneUp;
import asteroids.participants.Ship;


/**
 * Controls a game of Asteroids, but enhances
 */
public class EnhancedController extends Controller
{

    /**
     * Constrains score score lives aren't added continously
     */
    protected int lastScore;
    /**Sets timer for powerup reappearance*/
    protected Timer powerupTimer;
    /**Sets invincibility timer */
    protected Timer invincibilityTimer;
    /**Sound for a new life */
    protected SoundManager newLife;

    private String name;

    protected ShipNames myFrame;
    /*Controller with additional enhancements */

    /** */
    public EnhancedController(){
        super();
        myFrame = new ShipNames(this);
        myFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        myFrame.pack();
        myFrame.setVisible(true);
        highScoreList = new HighScores("highscoresenhanced.txt");
        newLife = new SoundManager("/sounds/smb_1-up.wav");
        powerupTimer = new Timer(POWERUP_TIMER, this);
        powerupTimer.start();
        invincibilityTimer = new Timer(750, this);
    }

    @Override
    protected void finalScreen(){
        highScoreList.log(name + "\t" + this.score);
        highScoreList.sortFile();
        display.removeKeyListener(this);

        display.showScores();

    }
    /**
     * Sets things up and begins a new game.
     */
    protected void initialScreen ()
    {
        super.initialScreen();
        name = myFrame.getUser1();
        powerupTimer.stop();
        powerupTimer.setDelay(POWERUP_TIMER);
        powerupTimer.start();
        if(invincibilityTimer.isRunning()){
            invincibilityTimer.stop();
        }
    }

    @Override
    public void AlienDestroyed (Participant p)
    {
        // Null out the ship
        this.alien = null;
        alienTimer.start();

        // Adds the score based off of the level (big or small alien ship)
        if(level == 2 && (p instanceof Bullet)){
            score += ALIENSHIP_SCORE[1];
        }else if(p instanceof Bullet)
        {
            score += ALIENSHIP_SCORE[0];
        }  
    }

    /**
     * An asteroid has been destroyed
     */
    @Override
    public void asteroidDestroyed (int size, Participant p)
    {
        if(p instanceof Bullet)
        {
            score += Constants.ASTEROID_SCORE[size];
        }
        
        // If all the asteroids are gone, schedule a transition
        if (countAsteroids() == 0)
        {
            scheduleTransition(END_DELAY);
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
        //Stops invincibility
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
            //Displays score
            display.setLevel(this.level);
            display.setScore(this.score);
            display.setLives(this.lives);

            //Adds life if score reached        
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
     * Places ship with an invincibility timer 
     */
    @Override
    protected void placeShip()
    {
        super.placeShip();
        invincibilityTimer.start();
    }

    /**
     * Determines if newlife is warranted
     */
    protected boolean getNewLife(int score){

        return Math.abs(this.score - lastScore) >= score;
    }

    /**
     * Determines if ship collides with another ship
     * @param p
     */
    public void OneUpDestroyed(Participant p) 
    {
        if(p instanceof Ship && p.equals(this.ship))
        {
            lives += 1;
            score += ONE_UP_SCORE;
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
     * Determines if ship currently has invincibility
     */
    @Override
    public boolean hasInvincibility()
    {
        return invincibilityTimer.isRunning();
    }

}
