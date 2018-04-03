//name: Penny Grant/Reece Sharp
//date:
//project:

/*
 * Couple ideas here: the fly should "choose" if it wants to struggle or not. If it chooses to struggle,
 * it sacrifices some of its life to increase it's chance of escape. If it doesn't struggle, then it's
 * chance to escape is the base of 50/50 or whatever we decide. It can sacrifice a max of, say 15% of
 * it's health to increase its chance of escape.
 * The "turn" method should do this
 */

/*
 * BUGS:
 *
 */

/*
 * IDEAS: if a fly escapes, say the cardinal direction it left in relative to the spider
 *
 */

import java.util.ArrayList;
import java.util.Random;

/**
 * simulates a fly stuck in a spider web, has the ability
 * to struggle free, get eaten, and die
 */
public class Fly {

    private static final int MAXSIZE = Calculate.FLY_SIZE;          // max size fly, can be (0-8)
    public static ArrayList<Fly> flies;                             // easier to call in functions
    private static Random random = new Random();

    private int locationX;          //row of fly
    private int locationY;          //column of fly
    private int vibrateEnergy;      //energy of the fly is based on web size, used in Web.vibrate()
    private int currentLifeEnergy;        //life of the fly, based on the size of web
    private int size;               //size of fly, larger = more energy given
    private static double struggleChance;  //percent per turn fly has to get out

    public Fly(int webSize, int locationX_, int locationY_) {
        locationX = locationX_;
        locationY = locationY_;
        size = random.nextInt(MAXSIZE) + 1;
        vibrateEnergy = (int) (webSize / 2 + (size * 1.2));
        currentLifeEnergy = webSize / 3;

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
        return vibrateEnergy;
    }

    public int getSize() {
        return size;
    }

    /**
     * finds the max energy in the array, used for formatting
     * @return the max vibration energy value in the array
     */
    public static int getMaxEnergy() {
        int maxEnergy = 0;
        for (int i = 0; i < Fly.flies.size(); i++) {
            //find max energy to format the web display
            if (maxEnergy < Fly.flies.get(i).getEnergy())
                maxEnergy = Fly.flies.get(i).getEnergy();
        }
        return maxEnergy;
    }

    /**
     * Is used to test if the fly still has health
     *
     * NOTE: might be refactored to work with a boolean hunger() function
     * that returns
     *
     * @return true if alive, false if dead and needs to removed
     */
    public boolean alive() {
        //INCOMPLETE

        return true;
    }

    public static void struggleFree() {
        //if the random number is lower than the struggleChance, then the fly is free

        for (int i = 0; i < Fly.flies.size(); i++) {    //struggle
            if (random.nextDouble() < struggleChance) {
                Fly.flies.remove(i);
                System.out.println("The web seems to be vibrating less");
            }
        }
    }

    /**
     * The turn method runs through a fly's turn - randomly deciding to sacrifice energy, attempting to escape, and losing some life
     */
    public void turn() {
        int strugBonus = 0; //bonus struggle based on sacrifice
        int struggle; //life sacrificed for higher escape chance
        int maxStrug; //max amount of life to sacrifice, based on total (or remaining?) life
        boolean sacrifice = random.nextBoolean(); //fly's decision to sacrifice energy for escape (random)

        //if loop for fly to sacrifice life for better chance of struggle
        if (sacrifice) {
            maxStrug = currentLifeEnergy / 10; //maxStrug dependent on life
            struggle = random.nextInt(maxStrug) + 1; //random number of struggle decided, up to max
            strugBonus = struggle / 2; //bonus of struggle half the number of health sacrificed
            currentLifeEnergy = currentLifeEnergy - struggle; //life decreased by life sacrificed
            struggleChance = struggleChance + strugBonus; //add bonus to struggleChance
        }//end of if loop

        struggleFree();

        currentLifeEnergy = currentLifeEnergy - 5; //arbitrary loss of life at end of each turn
    }// end of turn method
}//end class