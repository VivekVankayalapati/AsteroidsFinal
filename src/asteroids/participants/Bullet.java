package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.destroyers.*;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.game.Constants;

/**
 * Bullet used by ship
 */
public class Bullet extends Participant implements AsteroidDestroyer, OneUpDestroyer, AlienShipDestroyer
{

    /**
     * Bullet shape
     */
    private Shape bullet;

    /**
     * Existence duration of bullet
     */
    private ParticipantCountdownTimer bulletDuration;

    /**
     * Intilizalizes the bullet in a given position and direction
     */
    public Bullet(double posX, double posY, double theta)
    {
        this.setPosition(posX, posY);
        this.setVelocity(Constants.BULLET_SPEED, theta);
        this.bullet = new Ellipse2D.Double(0, 0, 1, 1);
        this.bulletDuration = new ParticipantCountdownTimer(this, Constants.BULLET_DURATION);
    }

    /**
     * Outline of bullet
     * @return outline
     */
    @Override
    protected Shape getOutline ()
    {
        return this.bullet;
    }
    /**
     * Gets the duration time of bullet
     * @return bullet duration
     */
    public ParticipantCountdownTimer getDuration(){
        return bulletDuration;
    }

    /**
     * Called if bullet is collided
     */
    @Override
    public void collidedWith (Participant p)
    {
        if(p instanceof BulletDestroyer)
        {
            Participant.expire(this);
        }
        
    }

    /**
     *Called by the Particpant countdown timer
     */
    @Override
    public void countdownComplete(Object payload){
        Participant.expire(this);
    }

}