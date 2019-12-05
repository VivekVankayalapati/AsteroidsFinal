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

    


    /**
     * Constructs a alien ship at the specified coordinates that is pointed in the given direction.
     */
    public AlienShip (int x, int y, double direction, Controller controller,int level)
    {
        //super(x, y, direction, controller);

        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);
       
        

        outline = AlienIcon();

        if (level==2)
        {
            setSpeed(SPEED_LIMIT/3);
            setVelocity(getSpeed(), direction);
        }
        else
        {

        }
        

        




        


        firing = new SoundManager("/sounds/fire.wav");
        destroyed = new SoundManager("/sounds/Skrillex - Scary Monsters And Nice Sprites.mid");

        // Schedule a movement in ten seconds
        new ParticipantCountdownTimer(this, "move", 10000);
    }



    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the y-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {  
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        
        if (RANDOM.nextInt(3)==0)
        {
            setDirection(0);
            //shoot();
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
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
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

            destroyed.playSound();
            for(int i = 0; i <= 15; i++)
            {
               controller.addParticipant(new Debris(this.getX(), this.getY(), this.getSpeed(), 1000, true));
            }
            
        }
    }

    /**
     * TODO docs
     */
    public void shoot(int level, Ship ship){
        if(!controller.tooManyBullets())
        {
            if (level <3)
            {
               Bullet bullet = new Bullet(getX(), getY(), Math.atan2(ship.getY(),ship.getX()), this); //Randomize later
               controller.addParticipant(bullet);
               firing.playSound();
            }
            else if (level >= 3)
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
            move();
            //shoot();
            
            new ParticipantCountdownTimer(this, "move", 10000);
        }
    }

    private Path2D.Double AlienIcon()
    {
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
        return poly;
    }

}