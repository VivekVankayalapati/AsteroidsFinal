package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

import asteroids.participants.Ship;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class Screen extends JPanel
{
    /** Legend that is displayed across the screen */
    private String legend;

    private String score, level = "";

    private int lives, maxLives;
    
    private int[] playerLives;

    private int players;

    /** Game controller2p */
    private Controller controller;

    

    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        maxLives = 0;
        players = 1;
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setBackground(Color.black);
        setForeground(Color.white);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        setFocusable(true);
    }

    /**
     * Set the legend
     */
    public void setLegend (String legend)
    {
        this.legend = legend;
    }

    public void setScore(int score){
        this.score = score + "";
    }

    public void setLevel(int level){
        this.level = level + "";
    }

    public void setPlayers(int players){
        this.players = players;
        
    }
    public void setLives(int lives, int player){
        if(lives > maxLives){
            this.maxLives = lives;
        }
        if(players == 1){
            this.lives = lives;
        }
        else
        {
            if(playerLives == null){
                playerLives = new int[players];
            }
            this.playerLives[player - 1] = lives;
        }
        
    }

    /**
     * Paint the participants onto this panel
     */
    @Override
    public void paintComponent (Graphics graphics)
    {
        // Use better resolution
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Do the default painting
        super.paintComponent(g);

        // Draw each participant in its proper place
        for (Participant p: controller)
        {
            p.draw(g);
        }

        // Draw the legend across the middle of the panel
        int size = g.getFontMetrics().stringWidth(legend);
        g.drawString(legend, (SIZE - size) / 2, SIZE / 2);



        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        int dist = 40; // represents the length of the display object from the edge of the screen

        // To avoid a null pointer exception when the application opens
        if(this.score != null && this.level != null)
        {
            int scoreSize = g.getFontMetrics().stringWidth(this.score);
            int offset = (int) ((dist + 5 + 14 * (double) maxLives / 2) - scoreSize / 2); // Centering the score above the ship live counter
            int levelSize = g.getFontMetrics().stringWidth(this.level);

            g.drawString(this.score, offset, 50);
            g.drawString(this.level, SIZE - dist - levelSize / 2, 50);
        }
        

        if(this.players > 1)
        {
            for(int p = 0; p < this.players; p++)
            {
                g.drawString((p + 1) + ": ", dist, 70 + 45* (p + 1));
                for(int i = 0; i < playerLives[p]; i++){
                    AffineTransform trans = AffineTransform.getTranslateInstance(dist + g.getFontMetrics().stringWidth((p + 1) + ":   ") + 14 * (maxLives - playerLives[p]) + 28 * i, 55 + 45* (p + 1));
                    trans.rotate( - Math.PI / 2);
                    Shape ship = Ship.shipIcon();
                    Shape newShip = trans.createTransformedShape(ship);
                    g.draw(newShip);
                }
            }
        }
        else if(players == 1){
            for(int i = 0; i < lives; i++){
                AffineTransform trans = AffineTransform.getTranslateInstance(dist + 14 * (maxLives - lives) + 28 * i, 100);
                trans.rotate( - Math.PI / 2);
                Shape ship = Ship.shipIcon();
                Shape newShip = trans.createTransformedShape(ship);
                g.draw(newShip);
            }
        }
        
        

    
    }


}
