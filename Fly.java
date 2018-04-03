//name: Penny Grant/Reece Sharp
//date:
//project:

import java.util.Random;

/**
 * simulates a fly stuck in a spider web, has the ability
 * to be struggle free, get eaten, and die
 */
public class Fly
{
    private int energy; //energy of the fly, if it reaches 0, fly dies
    private int size; 	//size of fly, larger = more energy given
    private double struggleChance;  //percent per turn fly has to get out
    private int locationX;          //row of fly
    private int locationY;          //column of fly



    public Fly(int webSize, int locationX_, int locationY_) {
        Random random = new Random();
        locationX = locationX_;
        locationY = locationY_;

        //use logarithmic graphs, or regression limits to determine escape chance, never higher than 1 (100%)
        //Math.log10(webSize);
        struggleChance = (double) 1 / webSize;
    }

    public int getX() {
        return locationX;
    }
    public int getY() {
        return locationY;
    }

    public boolean struggleFree(double value) {
        //if the random number is lower than the Stugglechance, then the fly is free
        if (value < struggleChance) {
            System.out.println("value = " + value + ", struggleChance = " + struggleChance);    //testing
            return true;
        }
        else
            return false;
    }

}//end class