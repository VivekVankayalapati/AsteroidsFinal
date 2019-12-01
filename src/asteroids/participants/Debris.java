package asteroids.participants;

import asteroids.game.Constants;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

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

    /**
     * TODO Docs
     * @param posX
     * @param posY
     * @param maxSpeed
     * @param debrisDuration
     * @param hasLines
     */
    public Debris(double posX, double posY, double maxSpeed, int debrisDuration, boolean hasLines)
    {
        this.setPosition(posX, posY);

        if(Constants.RANDOM.nextInt(5) == 4 && hasLines) {
            this.debris = new Line2D.Double(0, 0, Constants.RANDOM.nextInt(15), Constants.RANDOM.nextInt(15));
            maxSpeed *= 0.5; // Slows the particle if it's a line (has lines).
        }
        else {
            this.debris = new Ellipse2D.Double(0, 0, 1, 1);
        }
        this.setVelocity(Constants.RANDOM.nextDouble() * maxSpeed, Constants.RANDOM.nextDouble()* 2 * Math.PI);
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

    @Override
    public void countdownComplete(Object payload){
        Participant.expire(this);
    }
}
