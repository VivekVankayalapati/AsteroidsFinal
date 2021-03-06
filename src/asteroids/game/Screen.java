package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import asteroids.participants.Ship;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class Screen extends JPanel
{
    /** Legend that is displayed across the screen */
    private String legend;
    /**Score to display on screen */
    private String score;
    /**Level to display on screen */
    private String level = "";
    /**Records and caps lives */
    private int lives, maxLives;
    /**stores lives and scores in array for 2p mode */
    private int[] playerLives;
    /** Records playersscores in array */
    private String[] playerScores;
    /**Records players */
    private int players;
    /**Records high score */
    private int highScore;

    /** Game controller */
    private Controller controller;

    /** 
     * TODO Docs
     */
    private boolean endGame;
    /**Records whether game is enhanced */
    private boolean enhanced;

    

    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        endGame = false;
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
    //These methods converts score, level, players, lives to strings for display

    public void setLevel(int level){
        this.level = level + "";
    }

    public void setPlayers(int players){
        this.players = players;
        
    }
    /**
     * Tracks and caps lives of players
     * @param lives
     * @param player
     */
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
    public void setScore(int score, int player){
        if(players == 1){
            this.score = score + "";
        }
        else{
            if(playerScores == null){
                playerScores = new String[players];
            }
            this.playerScores[player - 1] = score + "";
        }
    }

    public void showScores()
    {
        endGame = true;
        
    }
    
	public void removeScores() {
        endGame = false;
	}
    
    public void setHighScore(int highScore) 
    {
        enhanced = true;
        this.highScore = highScore;
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

        if(!endGame){
            

            // To avoid a null pointer exception when the application opens
            if(this.score != null && this.level != null)
            {
                int offset = 0;
                if(players == 1){
                    int scoreSize = g.getFontMetrics().stringWidth(this.score);
                    offset = (int) ((dist + 5 + 14 * (double) maxLives / 2) - scoreSize / 2);
                    g.drawString(this.score, offset, 50);
                }
                
                // Centering the score above the ship live counter
                int levelSize = g.getFontMetrics().stringWidth(this.level);

                String highScoreString = "This Session's High Score is: " + this.highScore;
                g.drawString(this.level, SIZE - dist - levelSize / 2, 50);

                
                if(enhanced){
                    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
                int highScoreSize = g.getFontMetrics().stringWidth(highScoreString);
                g.drawString(highScoreString, SIZE / 2 - highScoreSize / 2, 50);
                }
                
            }
            

            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
            //Constrains the display of score, lives, etc.
            if(this.players > 1)
            {
                for(int p = 0; p < this.players; p++)
                {
                    int scoreSize = g.getFontMetrics().stringWidth(this.playerScores[p]);
                    int offset = (int) ((dist + 5 + 14 * (double) maxLives / 2) - scoreSize / 2);
                    g.drawString(this.playerScores[p], offset, 85 + 40 * (p + 1) + 50 * p);

                    g.drawString(Controller2p.getName(p + 1) + ": ", dist, 30 + 50 * (p + 1) + 40 * p);
                    double stringLength = g.getFontMetrics().stringWidth(Controller2p.getName(p + 1) + ": ");
                    for(int i = 0; i < playerLives[p]; i++){
                        AffineTransform trans = AffineTransform.getTranslateInstance(dist + stringLength + 10 + 14 * (maxLives - playerLives[p]) + 28 * i, 20 +  50 * (p + 1) + 40 * p);
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
        else if(endGame)
        {
            ArrayList<String[]> scores = new ArrayList<>();

            try(BufferedReader reader = new BufferedReader(new FileReader(new File("highscoresenhanced.txt"))))
            {
                String currentLine = reader.readLine();
                int lineTotal = 0;
    
                while (lineTotal < 9 && currentLine != null)
                {
                    String[] lines = currentLine.split("\t");

                    scores.add(lines);
    
                    currentLine = reader.readLine();
                    lineTotal ++;
                }
            }catch(IOException e) 
            {
                
            }
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 65));
            g.drawString("High Scores!", 375 - g.getFontMetrics().stringWidth("High Scores!") / 2, 50);
            int i = 0;
            int wantedSize = 400;
            for(String[] s : scores){
                String drawnString = (i + 1) + ": " +  s[0];
                int scoreSize = g.getFontMetrics().stringWidth(drawnString + s[1]);
                while(scoreSize <= wantedSize){
                    drawnString += " ";
                    scoreSize = g.getFontMetrics().stringWidth(drawnString + s[1]);
                }
                drawnString += s[1];
                scoreSize = g.getFontMetrics().stringWidth(drawnString);
                int offset = (int) (SIZE / 2 - scoreSize / 2);
                g.drawString(drawnString, offset, 120 + 70 * i);
                i++;
            }
        }
        
        
        
        

    
    }




}
