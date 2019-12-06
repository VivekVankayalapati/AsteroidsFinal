package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import javax.swing.*;

import asteroids.participants.AlienShip;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;

/**
 * Controls a game of Asteroids for strictly 2 players.
 */
public class Controller2p extends EnhancedController
{

    /** The ships (if one is active) or null (otherwise) */
    private Ship ship1,ship2;

    /**
     * Score of each ship.
     */
    private int score1, score2;
    
    /**
     * Values that can track the last ship that was destroyed. Must be set when a ship is destroyed and created.
     */
    private boolean ship1Destroyed,ship2Destroyed;
    

    /** Number of lives left for each ship */
    private int lives1,lives2;
    

    /**
     * Records user input/direction for a two player game
     */
    private boolean turnLeft1, turnRight1, accelerate1, fire1,turnLeft2, turnRight2, accelerate2, fire2;
    

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
    @Override
    protected void finalScreen ()
    {
        highScoreList.log(name + "\t" + this.score);
        highScoreList.sortFile();
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
     * Places ships in the center of the screen. Remove any existing ships first.
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
        score1 = 0;
        score2 = 0;
        level = 1;

        // Place the ships
        placeShip(1);
        placeShip(2);
        //Displays on-screen display
        display.setLevel(this.level);
        display.setScore(this.score);
        display.setPlayers(2);
        display.setLives(lives1, 1);
        display.setLives(lives2, 2);



        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Returns lives left for a given ship
     */
    protected int getLives(Ship ship){
        if(ship.equals(ship1)){
            return lives1;
        }
        else{
            return lives2;
        }
    }

    /**
     * Creates an alien targeting one of the two ships, randomly chosen.
     */
    @Override
    protected void placeAliens(){
        // Place a new alien
        Ship ship;
        alienTimer.stop();
        if(RANDOM.nextInt(2) == 0)
                {
                    ship = this.ship1;
                }
                else
                {
                   ship = this.ship2;

                }
        if(level > 1){
            Participant.expire(alien);
            alien = new AlienShip(this, this.level, ship);
            
        
            addParticipant(alien);
        }
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
     * Records if ship 1 is destroyed
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
     * Records if ship 2 is destroyed
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
        if(e.getSource() instanceof JButton || e.getSource() == heartBeat || e.getSource() == powerupTimer || e.getSource() == alienTimer) {
            super.actionPerformed(e);
        }
        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            //Moves ships based on user input
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
            
            /**
             * If score is reached, gives a new life bonus to ships
             */
            if(getNewLife(2 * EXTRA_LIFE_SCORE)){
                
                System.out.println(score);
                lives1 += 1;
                lives2 += 1;
                lastScore = score; // zero's the score so new lives aren't infinitely added
            }
            //Displays score and level and lives
            display.setLevel(this.level);
            display.setScore(this.score1, 1);
            display.setScore(this.score2, 2);
            display.setLives(lives1, 1);
            display.setLives(lives2, 2);
            
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
     * Resets game for a new level
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
        if(e.getKeyCode() == KeyEvent.VK_CONTROL && ship1 != null){
            ship1.teleport();
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
        if(e.getKeyCode() == KeyEvent.VK_T && ship2 != null){
            ship2.teleport();
        }

    }

    public void AlienDestroyed (Participant p)
    {
        // Null out the ship
        this.alien = null;
        alienTimer.start();

        // Adds the score based off of the level (big or small alien ship)
        if((p instanceof Bullet && ((Bullet) p).getParent().equals(ship1)))
        {
            if(level == 2){
                score1 += ALIENSHIP_SCORE[1];
            }else
            {
                score1 += ALIENSHIP_SCORE[0];
            }  
        }
        else if((p instanceof Bullet && ((Bullet) p).getParent().equals(ship2)))
        {
            if(level == 2){
                score2 += ALIENSHIP_SCORE[1];
            }else
            {
                score2 += ALIENSHIP_SCORE[0];
            } 
        }
    }

    /**
     * An asteroid has been destroyed
     */
    @Override
    public void asteroidDestroyed (int size, Participant p)
    {
        if((p instanceof Bullet && ((Bullet) p).getParent().equals(ship1)))
        {
            score1 += Constants.ASTEROID_SCORE[size];
        }
        else if((p instanceof Bullet && ((Bullet) p).getParent().equals(ship2))){
            score2 += Constants.ASTEROID_SCORE[size];
        }
        
        // If all the asteroids are gone, schedule a transition
        if (countAsteroids() == 0)
        {
            scheduleTransition(END_DELAY);
        }
    }

    /** 
     * Records if key is released
    */
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
     * Acceleration of ship
     * @param ship
     * @return ship acceleration
     */
    @Override
    public boolean getIsAccel(Ship ship)
    {
        if(ship.equals(ship1)){
            return accelerate1;
        }
        else{
            return accelerate2;
        }

    }

    /**
     * Records if ship destroyed another ship, gives live bonus
     * @param ship
     */
    @Override
    public void OneUpDestroyed(Participant ship)
    {
        if(ship instanceof Ship && ship.equals(this.ship1))
        {
            lives1 += 1;
        }
        else if(ship instanceof Ship && ship.equals(this.ship2))
        {
            lives2 += 1;
        }
    }

    

}
