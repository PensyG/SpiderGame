//name: Penny Grant/Reece Sharp
//date:
//project:

//import java.lang.Math; //used in struggle chance calculation?

import java.util.Random;

/**
 * simulates a fly stuck in a spider web, has the ability
 * to struggle free, get eaten, and die
 */
public class Fly {
    private static final int MAXSIZE = 5; // max size fly, can be (0-8)
    private static Random random = new Random(); //only need one instance of the random object

    private int energy; //energy of the fly is based on web size, used in Web.vibrate()
    private int life;   //life of the fly, based on the size of web
    private int size;    //size of fly, larger = more energy given
    private double struggleChance;  //percent per turn fly has to get out
    private int locationX;          //row of fly
    private int locationY;          //column of fly

    public Fly(int webSize, int locationX_, int locationY_, int size_) {
        size = random.nextInt(MAXSIZE);

        size = size_;   //larger flies have larger energies (bigger vibrations)
        life = webSize / 3;
        //energy is based off of the size of the bug, and size of the web
        //NOTE: size for each fly appears to change
        energy = (int) (webSize / 2 + (size * 1.2));
        locationX = locationX_;
        locationY = locationY_;

        //use logarithmic graphs, or regression limits to determine
        //escape chance, never higher than 1 (100%)
        //Math.log10(webSize);
        struggleChance = (double) 1 / webSize;
    }

    public int getX() {
        return locationX;
    }

    public int getY() {
        return locationY;
    }

    public int getEnergy() {
        return energy;
    }

    public int getSize() {
        return size;
    }

    static public int getMAXSIZE() {
        return MAXSIZE;
    }

    public boolean struggleFree() {
        //if the random number is lower than the struggleChance, then the fly is free
        return (random.nextDouble() < struggleChance);
    }
}//end class