import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class Fly {

    private static Random random = new Random();
    private static ArrayList<Fly> flies = new ArrayList<>();    //Fly array

    private static final int MAX_SIZE = 3;                      // max size fly, should be 0-8 (but can be any)
    private static final int TURN_GEN = 4;                      //
    private static final int MAX_FLIES = 10;                    //
    private static final int MAX_HEALTH = 10;                   //the maximum amount of health a fly can have

    private int locationRow;                //row of fly
    private int locationCol;                //column of fly
    private int vibrateEnergy;              //energy of the fly is based on web size, used in Web.vibrate()
    private int size;                       //size of fly, larger = more energy given
    private int health;                     //health value of the fly, if it reaches 0, fly dies
    private int struggleChance;             //percent per turn fly has to get out, linear chance based on webSize
    private int hungerConstant;             //constant that is taken from health each turn (higher for larger flies?)

    /**
     * @param webSize      - Size of the web being played on
     * @param locationRow_ - row of the web (i)
     * @param locationCol_ - column of the web (j)
     */
    public Fly(int webSize, int locationRow_, int locationCol_) {
        locationRow = locationRow_;                                 //new (i) coordinate
        locationCol = locationCol_;                                 //new (j) coordinate
        size = random.nextInt(MAX_SIZE) + 1;                        //gen random size up to Max
        vibrateEnergy = (webSize / 2 + (webSize / 50) * size);      //vibrate more the larger the size
        struggleChance = 10 - size;                                //larger flies have easier time escaping
        health = MAX_HEALTH - size;                                 //smaller flies live longer

    }

    /**
     * @return
     */
    public boolean alive() {
        return (health > 0);
    }

    /**
     *
     */
    public void hunger() {
        health -= hungerConstant;
    }

    /**
     * @return
     */
    public int getRow() {
        return locationRow;
    }

    /**
     * @return
     */
    public int getCol() {
        return locationCol;
    }

    /**
     * @return
     */
    public int getEnergy() {
        return vibrateEnergy;
    }

    /**
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * @return
     */
    public static ArrayList<Fly> getFlies() {
        return flies;
    }

    /**
     * used for formatting
     *
     * @return
     */
    public static int getMaxEnergy() {
        int maxEnergy = 0;
        for (Fly fly : flies) {
            //find max energy to format the web display
            if (maxEnergy < fly.getEnergy())
                maxEnergy = fly.getEnergy();
        }
        return maxEnergy;
    }

    /**
     * checks if current element (i, j) is a fly
     *
     * @param i - row of Web array
     * @param j - column of Web array
     * @return true if a Fly is there, else false
     */
    private static boolean checkFly(int i, int j) {
        //location matches a Fly in the flies array
        for (Fly fly : flies)
            if (i == fly.getRow() && j == fly.getCol())
                return true;
        return false;
    }

    /**
     * @param web
     */
    public static void update(Web web) {
        //attempt to generate a fly
        if (random.nextInt(TURN_GEN) == 0)
            generateFly(web);

        //test if a fly is under a spider, or if the fly can escape
        Fly fly;    //current fly
        for (int i = 0; i < flies.size(); i++) {
            fly = flies.get(i);
            fly.hunger();

            if (!fly.alive()) {
                System.out.println("The web vibrates less...");
                flies.remove(i);
            } else if (fly.struggleFree()) {
                System.out.println("The web jerks! And vibrates less...");
                flies.remove(i);
            }
        }

        //debug method
        if (Game.DEBUG)
            debug();
    }

    /**
     * @return
     */
    public boolean struggleFree() {
        //if the random number is lower than the struggleChance, then the fly is free
        return (random.nextInt(struggleChance) == 0);
    }

    /**
     * Generates a fly in a currently unoccupied space
     *
     * @param web - Web object, is used for it's array and checking capability
     */
    public static void generateFly(Web web) {
        int x;  //row
        int y;  //column

        boolean generated = false;

        //gen random locations until empty spot is found, then add fly there (hard-coded limit of MAX_FLIES in web)
        while (!generated && flies.size() < MAX_FLIES) {
            x = random.nextInt(web.getWebLength());
            y = random.nextInt(web.getWebLength());
            if (!checkFly(x, y)) {
                flies.add(new Fly(web.getWebLength(), x, y));
                generated = true;
            }
        }
    }

    /**
     * diagnostics on current Fly information
     */
    private static void debug() {
        System.out.println("DEBUG: Fly");
        for (int i = 0; i < Fly.flies.size(); i++) {    //struggle
            System.out.printf("#: %3d, ROW: %3d, COLUMN: %3d, SIZE: %d, ENERGY: %3d\n",
                    i,
                    flies.get(i).getRow(),
                    flies.get(i).getCol(),
                    flies.get(i).getSize(),
                    flies.get(i).getEnergy());
        }
        System.out.println("DEBUG: End Flies ------\n");
    }
}
