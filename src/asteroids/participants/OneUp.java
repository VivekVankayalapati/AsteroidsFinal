package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.*;

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
        poly.moveTo(30, 40);
        poly.lineTo(15, 55);
        poly.lineTo(0, 40);
        poly.lineTo(7.5, 32.5);
        poly.lineTo(15,40);
        poly.lineTo(22.5, 32.5);
        poly.closePath();

        this.outline = poly;

        this.spawnSound = new SoundManager("/sounds/smb_powerup_appears.wav");
        spawnSound.playSound();
        this.powerupDuration = new ParticipantCountdownTimer(this, time);
        
    }
    @Override
    protected Shape getOutline() {
        
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