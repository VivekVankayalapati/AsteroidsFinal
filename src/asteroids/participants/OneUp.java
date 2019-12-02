package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;

import asteroids.destroyers.OneUpDestroyer;
import asteroids.game.EnhancedController;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import asteroids.game.SoundManager;

public class OneUp extends Participant 
{
    /**
     * TODO Docs
     */
    private EnhancedController controller;

    /**
     * TODO Docs
     */
    private Shape outline;

    /**
     * TODO docs
     */
    private ParticipantCountdownTimer powerupDuration;


    private SoundManager spawnSound;
    /**
     * TODO Docs
     */
    public OneUp(double x, double y, int time, EnhancedController controller)
    {
        this.controller = controller;
        setPosition(x, y);

        Path2D.Double poly = new Path2D.Double();
        // TODO Make this poly not the ship
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();

        this.outline = poly;

        this.spawnSound = new SoundManager("/sounds/smb_powerup_appears.wav");

        this.powerupDuration = new ParticipantCountdownTimer(this, time);
        
    }
    @Override
    protected Shape getOutline() {
        spawnSound.playSound();
        return outline;
    }

    /**
     * TODO docs
     */
    @Override
    public void countdownComplete(Object payload){
        Participant.expire(this);
    }

    /**
     * TODO docs
     */
    @Override
    public void collidedWith(Participant p) {
        if(p instanceof OneUpDestroyer)
        {
            Participant.expire(this);
            controller.OneUpDestroyed(p);
        }

    }

}