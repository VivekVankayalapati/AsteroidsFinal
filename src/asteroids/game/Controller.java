package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;

import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;


/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener, Iterable<Participant>
{
    /** The state of all the Participants */
    protected ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    protected Ship ship;
    /**
     * The alienship
     */
    protected AlienShip alien;


    /** 
     * Lists high scores
     */
    protected HighScores highScoreList;
    

    /**
     * Tracks existence state of ship
     */
    protected boolean shipDestroyed;

    /** When this timer goes off, it is time to refresh the animation */
    protected Timer refreshTimer;

    /**
     * Tracks time for between alian appearances
     */
    protected Timer alienTimer;


    /**
     * Tracks score
     */
    protected int score;

    /**
     * Tracks game level
     */
    protected int level;

    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    protected long transitionTime;

    /** Number of lives left */
    protected int lives;

    /** The game display */
    protected Display display;

    /**
     * Manages sounds 
     */
    protected SoundManager beat1,beat2;
    
    protected int beat;
    protected Timer heartBeat;

    /**
     * Tracks motion input by user
     */
    protected boolean turnLeft, turnRight, accelerate, fire;

 

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the timers.
        refreshTimer = new Timer(FRAME_INTERVAL, this);
        heartBeat = new Timer(INITIAL_BEAT, this);
        alienTimer = new Timer(ALIEN_DELAY + RANDOM.nextInt(5001), this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Create the heartbeat
        beat1 = new SoundManager("/sounds/beat1.wav");
        beat2 = new SoundManager("/sounds/beat2.wav");
        beat = 1;

        highScoreList = new HighScores("highscores.txt");
        

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
    }

    /**
     * This makes it possible to use an enhanced for loop to iterate through the Participants being managed by a
     * Controller.
     */
    @Override
    public Iterator<Participant> iterator ()
    {
        return pstate.iterator();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip (Ship ship)
    {
        if(ship.equals(this.ship)){
            return this.ship;
        }
        else{
            throw new IllegalArgumentException("That ship isn't assigned to this controller");
        }

    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    protected void finalScreen ()
    {
        highScoreList.log("" + this.score);
        highScoreList.sortFile();
        display.removeKeyListener(this);

        display.setLegend("Game Over!");
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    protected void placeShip ()
    {
        heartBeat.start();
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);

        // Reset rocket values
        turnLeft = false;
        turnRight = false;
        fire = false;
        accelerate = false;

        shipDestroyed = false;
        display.setLegend("");
    }

    



    /**
     * Places an asteroid near each corner of the screen. Gives it a random velocity and rotation. Adds asteroids based on difficulty level
     */
    protected void placeAsteroids ()
    {
        addParticipant(new Asteroid(RANDOM.nextInt(4), 2, EDGE_OFFSET+RANDOM.nextInt(150)-75, EDGE_OFFSET+RANDOM.nextInt(150)-75, 3, this));
        addParticipant(new Asteroid(RANDOM.nextInt(4), 2, SIZE-EDGE_OFFSET+RANDOM.nextInt(150)-75, EDGE_OFFSET+RANDOM.nextInt(150)-75, 3, this));
        addParticipant(new Asteroid(RANDOM.nextInt(4), 2, EDGE_OFFSET+RANDOM.nextInt(150)-75, SIZE-EDGE_OFFSET+RANDOM.nextInt(150)-75, 3, this));
        addParticipant(new Asteroid(RANDOM.nextInt(4), 2, SIZE-EDGE_OFFSET+RANDOM.nextInt(150)-75, SIZE-EDGE_OFFSET+RANDOM.nextInt(150)-75, 3, this));
        for(int i = 1; i < level; i++)
        {
            addParticipant(new Asteroid(RANDOM.nextInt(4), 2, RANDOM.nextInt(750)-75, RANDOM.nextInt(750)-75, 3, this));
        }
    }

    /**
     * Places alien, removes alien if one exists
     */
    protected void placeAliens ()
    {
        alienTimer.stop();
        // Place a new alien
        
        if(level > 1){
            Participant.expire(alien);
            alien = new AlienShip(this, this.level, this.ship);
            
        
            addParticipant(alien);
        }
        
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    protected void clear ()
    {
        pstate.clear();
        ship = null;
        alien = null;
        heartBeat.setDelay(INITIAL_BEAT);
        display.setLegend("");
        
    }

    /**
     * Sets things up and begins a new game.
     */
    protected void initialScreen ()
    {
        // Clear the screen
        clear();

        // Reset statistics
        lives = 3;
        score = 0;
        level = 1;

        // Place asteroids
        placeAsteroids();

        // Place the ship
        placeShip();
        

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        display.setLevel(this.level);
        display.setScore(this.score);
        display.setLives(this.lives);


        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     * Has the new parameter ship in order to be able to be overridden without problem with the 2 player controller.
     */
    public void shipDestroyed (Ship ship)
    {
        this.ship = ship;
        // Null out the ship
        this.ship = null;

        heartBeat.stop();

        // Decrement lives
        lives--;
        shipDestroyed = true;
        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    public void AlienDestroyed (Participant p)
    {
        // Null out the ship
        this.alien = null;
        alienTimer.start();

        // Adds the score based off of the level (big or small alien ship)
        if(level == 2 && (p instanceof Bullet || p instanceof Ship)){
            score += ALIENSHIP_SCORE[1];
        }else if(p instanceof Ship || p instanceof Bullet)
        {
            score += ALIENSHIP_SCORE[0];
        }  
    }


    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed (int size, Participant p)
    {
        if(p instanceof Bullet || p instanceof Ship)
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
     * Caps number of bullets on screen
     * 
     */
    public boolean tooManyBullets() {
        int count = 0;
        for (Participant p : this)
        {
            if (p instanceof Bullet)
            {
                count++;
            }
        }
        return count >= Constants.BULLET_LIMIT;
    }

    /**
     * Schedules a transition m msecs in the future
     */
    protected void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * Gets number of lives
     */
    protected int getLives(){
        return lives;
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
            display.removeScores();
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
        else if (e.getSource() == alienTimer){
                placeAliens(); 
        }
        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            
            //Moves ship based on user input and ship existence
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
            //Displays scores
            display.setScore(this.score);
            display.setLevel(this.level);
            display.setLives(this.lives);

            // Move the participants to their new locations
            
            pstate.moveParticipants();

            

            // Refresh screen
            display.refresh();
        }
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
     * Sets game for a new level
     */
    protected void newLevel()
    {
        clear();
        level += 1;
        if(level >= 2 && !alienTimer.isRunning())
        {
            alienTimer.start();
        }
        heartBeat.setDelay(INITIAL_BEAT);
        placeShip();
        placeAsteroids();   
    }

    /**
     * Returns the number of asteroids that are active participants
     */
    protected int countAsteroids ()
    {
        int count = 0;
        for (Participant p : this)
        {
            if (p instanceof Asteroid)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null || e.getKeyCode() == KeyEvent.VK_D && ship != null) {
            turnRight = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null || e.getKeyCode() == KeyEvent.VK_A && ship != null) {
            turnLeft = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null || e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            accelerate = true;

        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN && ship != null || e.getKeyCode() == KeyEvent.VK_SPACE && ship != null || e.getKeyCode() == KeyEvent.VK_S && ship != null){
            fire = true;
        }
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }
    /**Records when keys are no longer pressed */
    @Override
    public void keyReleased (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null || e.getKeyCode() == KeyEvent.VK_D && ship != null)
        {
            turnRight = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null || e.getKeyCode() == KeyEvent.VK_A && ship != null)
        {
            turnLeft = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null || e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            ship.notAccelerating();
            accelerate = false;

        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN && ship != null || e.getKeyCode() == KeyEvent.VK_SPACE && ship != null || e.getKeyCode() == KeyEvent.VK_S && ship != null){
            fire = false;
        }
    }

    /**
     * Gets the acceleration of ship
     * @param ship
     * @return
     */
    public boolean getIsAccel(Ship ship)
    {
        return accelerate;
    }

}
