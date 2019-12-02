package asteroids.participants;

import static asteroids.game.Constants.*;

import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Constants;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.SoundManager;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer, OneUpDestroyer
{
    /** The outline of the ship */
    private Shape outline, outlineFlame;

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
     * TODO Docs
     */
    private SoundManager thrust;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);

        Path2D.Double polyFlame = new Path2D.Double();
        polyFlame.moveTo(21, 0);
        polyFlame.lineTo(-21, 12);
        polyFlame.lineTo(-14, 10);
        polyFlame.lineTo(-14, -10);
        polyFlame.lineTo(-25, 0);
        polyFlame.lineTo(-14, 10);
        polyFlame.lineTo(-14, -10);
        polyFlame.lineTo(-21, -12);
        polyFlame.closePath();
        outlineFlame = polyFlame;
        
        outline = shipIcon();


        firing = new SoundManager("/sounds/fire.wav");
        destroyed = new SoundManager("/sounds/bangShip.wav");
        thrust = new SoundManager("/sounds/thrust.wav");

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
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
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
        if (controller.getIsAccel(this))
        {
            return outlineFlame;
        }
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
        if(!thrust.isRunning()){
            thrust.playSound();
        }
        
    }

    /**
     * Fix to cancel the sound when the ship isn't accelerating (it would just continue otherwise).
     */
    public void notAccelerating(){
        if(thrust.isRunning()){
            thrust.stopSound();
        }
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

            // Adds the ship dust (with lines) when the ship is destroyed.
            for(int i = 0; i <= 15; i++)
            {
               controller.addParticipant(new Debris(this.getX(), this.getY(), this.getSpeed() * 0.25, Constants.DEBRIS_DURATION, true));
            }
            // Tell the controller2p the ship was destroyed
            controller.shipDestroyed(this);
        }
    }

    /**
     * TODO docs
     */
    public void shoot(){
        if(!controller.tooManyBullets())
        {
            Bullet bullet = new Bullet(getX(), getYNose(), getRotation(), this);
            controller.addParticipant(bullet);

            firing.playSound();

        }
        
    }
    private Path2D.Double shipIcon(){
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        return poly;
    }

    /**
     * TODO Docs
     */
    public void teleport() {
        setPosition(Constants.SIZE * Constants.RANDOM.nextDouble(),Constants.SIZE * Constants.RANDOM.nextDouble());
    }
}
