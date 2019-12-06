package asteroids.participants;

import static asteroids.game.Constants.*;

import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.game.SoundManager;


/**
 * Represents ships
 * 
 */
public class AlienShip extends Participant implements ShipDestroyer, AsteroidDestroyer, BulletDestroyer
{
    /** The outline of the ship */
    private Shape outline;

    /** Game controller2p */
    private Controller controller;

    /**
     * Sound Manager for the firing.
     */
    private SoundManager firing;

    /**
     * TODO Docs
     */
    private SoundManager destroyed;
    private SoundManager saucerSound;

    private Ship ship;

    private int level;

    /**TODO Docs
     * 
     */
    private int xi;

    
    


    /**
     * Constructs a alien ship at the specified coordinates that is pointed in the given direction.
     */
    public AlienShip (Controller controller,int level, Ship ship)
    {
        int location = RANDOM.nextInt(2);

        if(location == 0){
            xi = 5;
        }
        else if (location == 1){
            xi = 745;
            setDirection(Math.PI);
        }
        this.ship = ship;
        this.controller = controller;
        this.level = level;
        setPosition(xi, RANDOM.nextInt(750));
       
        setSpeed(2);

        outline = AlienIcon();

        
        firing = new SoundManager("/sounds/fire.wav");
        destroyed = new SoundManager("/sounds/bangAlienShip.wav");

        if(level == 2){
           saucerSound = new SoundManager("/sounds/saucerBig.wav"); 
        }
        else{
            saucerSound = new SoundManager("/sounds/saucerSmall.wav");
        }

        
        // Schedule a movement in one seconds
        new ParticipantCountdownTimer(this, "move", 1000);
    }



    @Override
    protected Shape getOutline ()
    {  
        if(!saucerSound.isRunning())
        {
            saucerSound.playSound();
        }
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    public void moveShip ()
    {
        
        if (xi == 5)
        {
            if (RANDOM.nextInt(3)==0)
            {
                setDirection(0);
                shoot();
            }
            else if (RANDOM.nextInt(3)==1)
            {
                setDirection(1);
            }
            else
            {
                setDirection(-1);
            }
        }

        else if (xi == 745)
        {
            System.out.println(xi);
            if (RANDOM.nextInt(3)==0)
            {
                setDirection(Math.PI);
                shoot();
            }
            else if (RANDOM.nextInt(3)==1)
            {
                setDirection(Math.PI+1);
            }
            else
            {
                setDirection(Math.PI-1);
            } 
        }
        
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof AlienShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

        
            for(int i = 0; i <= 15; i++)
            {
               controller.addParticipant(new Debris(this.getX(), this.getY(), this.getSpeed(), 1000, true));
            }

            destroyed.playSound();
            controller.AlienDestroyed(p);
            
        }
    }

    /**
     * TODO docs
     */
    public void shoot(){
            if (this.level >= 3 ){
                double angle = Math.atan2(this.ship.getY() - getY(), this.ship.getX() - getX());
                double offset = RANDOM.nextDouble() * (Math.PI / 18) - (Math.PI / 36);
                EnemyBullet bullet = new EnemyBullet(getX(), getY(), angle + offset);
               controller.addParticipant(bullet);
               firing.playSound();
            }
            else if (this.level < 3)
            {
                EnemyBullet bullet = new EnemyBullet(getX(), getY(), RANDOM.nextDouble()*2*Math.PI);
                controller.addParticipant(bullet);
                firing.playSound();               
            }
        
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // noves, then schedule another
        // burst for 10000 msecs from now.
        if (payload.equals("move"))
        {
            moveShip();
            
        new ParticipantCountdownTimer(this, "move", 1000);
        }
        
    }

    private Path2D.Double AlienIcon()
    {
        AffineTransform at = new AffineTransform();
        Path2D.Double poly = new Path2D.Double();
		poly.moveTo(-9, 0);
		poly.lineTo(-4, -4);
		poly.lineTo(4, -4);
		poly.lineTo(9, 0);
		poly.lineTo(-9, 0);
		poly.lineTo(-4, 4);
		poly.lineTo(-2, 8);
		poly.lineTo(2, 8);
		poly.lineTo(4, 4);
		poly.lineTo(-4, 4);
		poly.lineTo(4, 4);
		poly.lineTo(9, 0);
        poly.closePath();
        at.rotate(-Math.PI);
        if(this.level == 2){  
            at.scale(2,2);
        }
        poly.transform(at);
        return poly;
    }

}