package asteroids.participants;

import asteroids.game.Constants;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Debris extends Participant
{

    /**
     * TODO docs
     */
    private Shape debris;

    /**
     * TODO docs
     */
    private ParticipantCountdownTimer debrisDuration;

    public Debris(double posX, double posY, double maxSpeed, int debrisDuration)
    {
        this.setPosition(posX, posY);
        this.setVelocity(Constants.RANDOM.nextDouble() * maxSpeed, Constants.RANDOM.nextDouble()* 2 * Math.PI);
        this.debris = new Ellipse2D.Double(0, 0, 1, 1);
        this.debrisDuration = new ParticipantCountdownTimer(this, debrisDuration);
        setInert(true);
    }

    @Override
    protected Shape getOutline() {
        return debris;
    }

    @Override
    public void collidedWith(Participant p) {

    }

    /**
     * TODO docs
     */
    @Override
    public void countdownComplete(Object payload){
        Participant.expire(this);
    }
}
