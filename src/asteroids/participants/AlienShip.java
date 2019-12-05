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
public class AlienShip extends Participant implements ShipDestroyer, AsteroidDestroyer
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

    private Ship ship;

    private int level;

    


    /**
     * Constructs a alien ship at the specified coordinates that is pointed in the given direction.
     */
    public AlienShip (int x, int y, double direction, Controller controller,int level, Ship ship)
    {
        this.ship = ship;
        this.controller = controller;
        this.level = level;
        setPosition(x, y);
        setRotation(direction);
       
        setSpeed(2);

        outline = AlienIcon();

        
        firing = new SoundManager("/sounds/fire.wav");

        // Schedule a movement in ten seconds
        new ParticipantCountdownTimer(this, "move", 1000);
    }



    @Override
    protected Shape getOutline ()
    {  
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    public void moveShip ()
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
        
        super.move();
        
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

        
            for(int i = 0; i <= 15; i++)
            {
               controller.addParticipant(new Debris(this.getX(), this.getY(), this.getSpeed(), 1000, true));
            }
            
        }
    }

    /**
     * TODO docs
     */
    public void shoot(){
        if(!controller.tooManyBullets())
        {
            if (this.level < 3)
            {
               Bullet bullet = new Bullet(getX(), getY(), Math.atan2(-this.ship.getY() + getY(),-this.ship.getX()) + getX(), this); //Randomize later
               controller.addParticipant(bullet);
               firing.playSound();
            }
            else if (this.level <= 3)
            {
                Bullet bullet = new Bullet(getX(), getY(), 2*Math.PI, this);
                controller.addParticipant(bullet);
                firing.playSound();               
            }
            
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
            //shoot();
            
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
        at.rotate(Math.PI/2);
        if(this.level == 2){  
            at.scale(2,2);
        }
        poly.transform(at);
        return poly;
    }

}