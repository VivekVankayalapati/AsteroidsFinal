package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import javax.swing.*;
import asteroids.participants.Ship;

/**
 * Controls a game of Asteroids for strictly 2 players.
 */
public class Controller2p extends Controller
{

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship1;
    private Ship ship2;
    /**
     * Values that can track the last ship that was destroyed. Must be set when a ship is destroyed and created.
     */
    private boolean ship1Destroyed;
    private boolean ship2Destroyed;

    /** Number of lives left */
    private int lives1;
    private int lives2;

    /**
     * TODO Docs
     */
    private boolean turnLeft1, turnRight1, accelerate1, fire1;
    private boolean turnLeft2, turnRight2, accelerate2, fire2;

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip (Ship ship)
    {
        return ship;
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);

        // Reset rocket one values
        turnLeft1 = false;
        turnRight1 = false;
        fire1 = false;
        accelerate1 = false;

        // Reset rocket two values
        turnLeft2 = false;
        turnRight2 = false;
        fire2 = false;
        accelerate2 = false;
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip (int ship)
    {
        heartBeat.start();
        // Place a new ship
        if(ship == 1){
            if(lives1 > 0){
                Participant.expire(ship1);
                ship1 = new Ship(SIZE / 3, SIZE / 3, -Math.PI / 2, this);
                addParticipant(ship1);
                ship1Destroyed = false;
            }
        }
        else if(ship == 2){
            if(lives2 > 0){
                Participant.expire(ship2);
                ship2 = new Ship(2 * SIZE / 3, 2 * SIZE / 3, -Math.PI / 2, this);
                addParticipant(ship2);
                ship2Destroyed = false;
            }
        }

        // Reset rocket one values
        turnLeft1 = false;
        turnRight1 = false;
        fire1 = false;
        accelerate1 = false;

        // Reset rocket two values
        turnLeft2 = false;
        turnRight2 = false;
        fire2 = false;
        accelerate2 = false;

        display.setLegend("");
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    @Override
    protected void clear ()
    {
        pstate.clear();
        heartBeat.setDelay(INITIAL_BEAT);
        display.setLegend("");
        ship1 = null;
        ship2 = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    @Override
    protected void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids();

        // Reset statistics
        lives1 = 3;
        lives2 = 3;
        score = 0;
        level = 1;

        // Place the ships
        placeShip(1);
        placeShip(2);



        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * The ship has been destroyed
     */
    @Override
    public void shipDestroyed (Ship ship)
    {
        if(ship.equals(ship1)){
            ship1Destroyed();
        }
        else
        {
            ship2Destroyed();
        }
    }


    /**
     * TODO Docs
     */
    private void ship1Destroyed()
    {

        ship1 = null;
        ship1Destroyed = true;
        heartBeat.stop();

        // Decrement lives
        lives1--;

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * TODO Docs
     */
    private void ship2Destroyed() {
        ship2 = null;
        ship2Destroyed = true;
        heartBeat.stop();

        // Decrement lives
        lives2--;

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }


    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }
        else if(e.getSource() == heartBeat && beat == 1)
        {
            int beatDelay = Math.max(heartBeat.getDelay() - BEAT_DELTA, FASTEST_BEAT);
            heartBeat.setDelay(beatDelay);
            beat1.playSound();
            beat = 2;
        }
        else if(e.getSource() == heartBeat && beat == 2)
        {
            int beatDelay = Math.max(heartBeat.getDelay() - BEAT_DELTA, FASTEST_BEAT);
            heartBeat.setDelay(beatDelay);
            beat2.playSound();
            beat = 1;
        }
        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            if(turnLeft1 && ship1 != null){
                ship1.turnLeft();
            }
            if(turnRight1 && ship1 != null){
                ship1.turnRight();
            }
            if(fire1 && ship1 != null){
                ship1.shoot();
            }
            if(accelerate1 && ship1 != null){
                ship1.accelerate();
                
            }
            if(turnLeft2 && ship2 != null){
                ship2.turnLeft();
            }
            if(turnRight2 && ship2 != null){
                ship2.turnRight();
            }
            if(fire2 && ship2 != null){
                ship2.shoot();
            }
            if(accelerate2 && ship2 != null){
                ship2.accelerate();

            }
            
            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();
        }
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    @Override
    protected void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;
            
            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives1 + lives2 <= 0)
            {
                finalScreen();
            }
            else if(countAsteroids() > 0)
            {
                if(ship1Destroyed){
                    placeShip(1);
                }
                if(ship2Destroyed){
                    placeShip(2);
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
     */
    @Override
    protected void newLevel()
    {
        level += 1;
        placeShip(1);
        placeShip(2);
        heartBeat.setDelay(INITIAL_BEAT);
        beat = 1;
        placeAsteroids();
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship1 != null)
        {
            turnRight1 = true;

        }       
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship1 != null)
        {
            turnLeft1 = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship1 != null)
        {
            accelerate1 = true;

        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN && ship1 != null){
            fire1 = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_D && ship2 != null)
        {
            turnRight2 = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_A && ship2 != null)
        {
            turnLeft2 = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_W && ship2 != null)
        {
            accelerate2 = true;

        }
        if(e.getKeyCode() == KeyEvent.VK_S && ship2 != null){
            fire2 = true;
        }
    }

    @Override
    public void keyReleased (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship1 != null)
        {
            turnRight1 = false;

        }       
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship1 != null)
        {
            turnLeft1 = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship1 != null)
        {
            ship1.notAccelerating();
            accelerate1 = false;

        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN && ship1 != null){
            fire1 = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_D && ship2 != null)
        {
            turnRight2 = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_A && ship2 != null)
        {
            turnLeft2 = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_W && ship2 != null)
        {
            ship2.notAccelerating();
            accelerate2 = false;

        }
        if(e.getKeyCode() == KeyEvent.VK_S && ship2 != null){
            fire2 = false;
        }
    }

    /**
     * TODO Docs
     * @param ship
     * @return
     */
    public boolean getIsAccel(Ship ship)
    {
        if(ship.equals(ship1)){
            return accelerate1;
        }
        else{
            return accelerate2;
        }

    }

}
