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
 * TODO docs
 * @author Aidan Copinga
 */
public class EnemyBullet extends Participant implements AsteroidDestroyer, OneUpDestroyer, ShipDestroyer, BulletDestroyer
{

    /**
     * TODO docs
     */
    private Shape bullet;

    /**
     * TODO docs
     */
    private ParticipantCountdownTimer bulletDuration;

    /**
     * TODO docs
     */
    public EnemyBullet(double posX, double posY, double theta)
    {
        this.setPosition(posX, posY);
        this.setVelocity(Constants.BULLET_SPEED, theta);
        this.bullet = new Ellipse2D.Double(0, 0, 1, 1);
        this.bulletDuration = new ParticipantCountdownTimer(this, Constants.BULLET_DURATION);
    }

    /**
     * TODO docs
     */
    @Override
    protected Shape getOutline ()
    {
        return this.bullet;
    }
    /**
     * TODO docs
     */
    public ParticipantCountdownTimer getDuration(){
        return bulletDuration;
    }

    /**
     * TODO docs
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
     * TODO docs
     */
    @Override
    public void countdownComplete(Object payload){
        Participant.expire(this);
    }

}