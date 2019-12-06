package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.OneUpDestroyer;
import asteroids.destroyers.*;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.game.Constants;

/**
 * Bullet used by alienShip
 */
public class EnemyBullet extends Participant implements AsteroidDestroyer, OneUpDestroyer, ShipDestroyer, BulletDestroyer
{

    /**
     * Bullet shape
     */
    private Shape bullet;

    /**
     * Duration of bullet existence
     */
    private ParticipantCountdownTimer bulletDuration;

    /**
     * Generates bullet at given position and direction
     */
    public EnemyBullet(double posX, double posY, double theta)
    {
        this.setPosition(posX, posY);
        this.setVelocity(Constants.BULLET_SPEED, theta);
        this.bullet = new Ellipse2D.Double(0, 0, 1, 1);
        this.bulletDuration = new ParticipantCountdownTimer(this, Constants.BULLET_DURATION);
    }

    /**
     * Returns outline of bullet
     */
    @Override
    protected Shape getOutline ()
    {
        return this.bullet;
    }
    /**
     * Returns existence time of bullet
     */
    public ParticipantCountdownTimer getDuration(){
        return bulletDuration;
    }

    /**
     * Determines if bullet has collides with a particpant
     */
    @Override
    public void collidedWith (Participant p)
    {
        if(p instanceof EnemyBulletDestroyer)
        {
            Participant.expire(this);
        }
    }

    /**
     * Expires bullet after given time
     */
    @Override
    public void countdownComplete(Object payload){
        Participant.expire(this);
    }

}