import java.util.ArrayList;
import java.util.Random;

public class Fly {
    private static Random random = new Random();
    private static final int MAXSIZE = 5;          // max size fly, can be (0-8)
    private static final int TURN_GEN = 4;
    private static final int MAX_FLIES = 10;
    private static ArrayList<Fly> flies = new ArrayList<>();

    private int locationRow;          //row of fly
    private int locationCol;          //column of fly
    private int vibrateEnergy;      //energy of the fly is based on web size, used in Web.vibrate()
    private int size;               //size of fly, larger = more energy given
    private static double struggleChance;  //percent per turn fly has to get out, linear chance based on webSize

    /**
     *
     * @param webSize - Size of the web being played on
     * @param locationRow_ - row of the web (i)
     * @param locationCol_ - column of the web (j)
     */
    public Fly(int webSize, int locationRow_, int locationCol_) {
        locationRow = locationRow_;
        locationCol = locationCol_;
        size = random.nextInt(MAXSIZE) + 1;
        vibrateEnergy = (int) (webSize / 2 + (size * 1.2));
        struggleChance = (double) 1 / webSize;
    }

    public int getRow() {
        return locationRow;
    }

    public int getCol() {
        return locationCol;
    }

    public int getEnergy() {
        return vibrateEnergy;
    }

    public int getSize() {
        return size;
    }

    public static ArrayList<Fly> getFlies() {
        return flies;
    }

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

    public static void update(Web web) {
        //attempt to generate a fly
        if (random.nextInt(TURN_GEN) == 0)
            generateFly(web);

        //test if a fly is under a spider, or if the fly can escape
        Fly fly;    //current fly
        for (int i = 0; i < flies.size(); i++) {
            fly = flies.get(i);

            if (fly.struggleFree())
                flies.remove(i);
        }
        //debug method
        if (Game.DEBUG)
            debug();
    }

    public boolean struggleFree() {
        //if the random number is lower than the struggleChance, then the fly is free
        if (random.nextDouble() < struggleChance) {
            System.out.println("The web seems to be vibrating less");
            return true;
        }
        return false;
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

        //gen random locations until empty spot is found, then add fly there (hard-coded limit of 50 flies in web)
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
     *  diagnostics on current Fly information
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
