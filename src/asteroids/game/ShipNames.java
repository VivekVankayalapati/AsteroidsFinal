package asteroids.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;


public class ShipNames extends JFrame implements ActionListener
{
  private static final long serialVersionUID = 1L;

  /**
   * TODO Docs
   */
  private JLabel shipLabel1;
    private JLabel shipLabel2;
    private JTextField ship1Field;
    private JTextField ship2Field;
    private String userInput1="ship1";
    private String userInput2="ship2";
    private JButton calcButton;   



    ShipNames(EnhancedController two)
    {
         // Used to specify GUI component layout
      GridBagConstraints layoutConst = null;

      
      // Set frame's title
      setTitle("User Name Input");
      


      // Create a "Enter Names" button
      calcButton = new JButton("Enter Names");
      
      // Use "this" class to handle button presses
      calcButton.addActionListener(this);
      // Use a GridBagLayout
      setLayout(new GridBagLayout());
      layoutConst = new GridBagConstraints();

      // Specify component's grid location
      layoutConst.gridx = 0;
      layoutConst.gridy = 0;

      // 10 pixels of padding around component
      layoutConst.insets = new Insets(10, 10, 10, 10);
      //Labels for the ship name input

      if(two instanceof Controller2p){
        shipLabel1 = new JLabel("Ship Name 1: ");
      
        //Entering text fields for ship 1
        ship1Field = new JTextField(15);
        ship1Field.setEditable(true);
        ship1Field.setText("ship1");

        // Add component using the specified constraints
        add(shipLabel1, layoutConst);

        layoutConst = new GridBagConstraints();
        layoutConst.gridx = 1;
        layoutConst.gridy = 0;
        layoutConst.insets = new Insets(10, 10, 10, 10);
        add(ship1Field, layoutConst);

        shipLabel2 = new JLabel("Ship Name 2: ");
         //Entering text fields for ship 2
        ship2Field = new JTextField(15);
        ship2Field.setEditable(true);
        ship2Field.setText("ship2");

        
      layoutConst = new GridBagConstraints();
      layoutConst.gridx = 0;
      layoutConst.gridy = 1;
      layoutConst.insets = new Insets(10, 10, 10, 10);
      add(shipLabel2, layoutConst);

      layoutConst = new GridBagConstraints();
      layoutConst.gridx = 1;
      layoutConst.gridy = 1;
      layoutConst.insets = new Insets(10, 10, 10, 10);
      add(ship2Field, layoutConst);
        
      }
      else
      {
        shipLabel1 = new JLabel("Ship Name 1: ");
      
        //Entering text fields for ship 1
        ship1Field = new JTextField(15);
        ship1Field.setEditable(true);
        ship1Field.setText("ship1");

        // Add component using the specified constraints
        add(shipLabel1, layoutConst);

        layoutConst = new GridBagConstraints();
        layoutConst.gridx = 1;
        layoutConst.gridy = 0;
        layoutConst.insets = new Insets(10, 10, 10, 10);
        add(ship1Field, layoutConst);
      }
      


      layoutConst.gridx = 0;
      layoutConst.gridy = 2;
      layoutConst.insets = new Insets(10, 10, 10, 10);
      add(calcButton, layoutConst);
   }

   /* Method is automatically called when an event 
    occurs (e.g, Enter key is pressed) */
   @Override
   public void actionPerformed(ActionEvent event) 
   {
           

      // Get user's input
      userInput1 = ship1Field.getText();
      if(ship2Field != null)
      {
        userInput2 = ship2Field.getText();
      }


      setVisible(false);
       
   }
   /**
    * Gets ship name 1
    */
   public String getUser1()
   {
       return userInput1;
   }
   /**
    * Gets ship name 2
    * 
    */
   public String getUser2()
   {
       return userInput2;
   }


   

    
}