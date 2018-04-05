import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class Fly {

    private static Random random = new Random();
    private static ArrayList<Fly> flies = new ArrayList<>();    //Fly array

    private static int MAX_SMALL;       //maximum amount of small flies allowed in web
    private static int MAX_MEDIUM;      //maximum amount of medium flies
    private static int MAX_LARGE;       //maximum amount of large flies
    private static int currentSmall;    //current small flies
    private static int currentMedium;   //current medium flies
    private static int currentLarge;    //current large flies

    private int locationRow;            //row of fly
    private int locationCol;            //column of fly
    private int vibrateEnergy;          //energy of the fly is based on web size, used in Web.vibrate()
    private int health;                 //health value of the fly, if it reaches 0, fly dies
    private int struggleChance;         //percent per turn fly has to get out, linear chance based on webSize
    private int hungerConstant;         //constant that is taken from health each turn (higher for larger flies?)
    private int scoreValue;             //score value of the fly
    private int foodValue;
    private FlySize size;               //size of fly, larger = more energy given

    /**
     * @param locationRow_ - row of the web (i)
     * @param locationCol_ - column of the web (j)
     */
    public Fly(int locationRow_, int locationCol_, FlySize size_) {
        locationRow = locationRow_;                                         //new (i) coordinate
        locationCol = locationCol_;                                         //new (j) coordinate
        size = size_;
        health = 15;                    //smaller flies live longer
        struggleChance = 15;            //larger flies have easier time escaping
        hungerConstant = 1;
        vibrateEnergy = (size.ordinal() + 1) * 5;              //vibrate more the larger the size
        foodValue = (size.ordinal() + 1) * 2;
        scoreValue = (size.ordinal()+ 1) * 5;
    }

    /**
     * variable fly size, looks at the max amounts of each fly that can be created,
     * which are generated in setConstants each game
     */
    public enum FlySize {
        Small, Medium, Large;

        public static FlySize newSize() {
            if (currentSmall < MAX_SMALL)
                return Small;
            else if (currentMedium < MAX_MEDIUM)
                return Medium;
            else if (currentLarge < MAX_LARGE)
                return Large;
            else
                return null;
        }
    }

    /**
     * setup class variables before each game
     * @param webSize
     */
    public static void setConstants(int webSize) {
        //number of 50x50 quadrants in web
        int numberQuadrants = (int) Math.pow((webSize / 50), 2);

        MAX_SMALL = (int) (5.5 * numberQuadrants);
        MAX_MEDIUM = (int) (3.33 * numberQuadrants);
        MAX_LARGE = (int) (1.2 * numberQuadrants);
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
     *
     * @return
     */
    public int getFoodValue() {
        return foodValue;
    }

    /**
     *
     * @return
     */
    public int getScoreValue() {
        return scoreValue;
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
    public FlySize getSize() {
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
     *
     */
    private static void updateCurrentFlies() {
        currentSmall = 0;
        currentMedium = 0;
        currentLarge = 0;

        Fly fly;
        for (int i = 0; i < flies.size(); i++) {
            fly = flies.get(i);
            switch (fly.getSize()) {
                case Small:
                    currentSmall++;
                    break;
                case Medium:
                    currentMedium++;
                    break;
                case Large:
                    currentLarge++;
                    break;
                default:
                    System.out.println("Error in updateCurrentFlies()");
                    break;
            }
        }
    }

    /**
     * @param web
     */
    public static void update(Web web) {
        Fly.updateCurrentFlies();
        generateFlies(web);

        //debug method
        if (Game.DEBUG)
            debug();
    }

    /**
     * @return
     */
    public boolean struggleFree() {
        //if the random number out of StruggleChance is equal to 0
        return (random.nextInt(struggleChance) == 0);
    }

    /**
     * Generates a fly in a currently unoccupied space
     *
     * @param web - Web object, is used for it's array and checking capability
     */
    public static void generateFlies(Web web) {

        int row;  //row
        int col;  //column

        boolean generated;
        FlySize newSize;
        //while there are still flies in the web to generate
        while (FlySize.newSize() != null) {
            newSize = FlySize.newSize();
            generated = false;
            //gen random locations until empty spot is found, then add fly there
            while (!generated) {
                row = random.nextInt(web.getWebLength());
                col = random.nextInt(web.getWebLength());
                if (!checkFly(row, col)) {
                    flies.add(new Fly(row, col, newSize));
                    Fly.updateCurrentFlies();
                    generated = true;
                }
            }
        }
    }

    /**
     * diagnostics on current Fly information
     */
    private static void debug() {
        System.out.println("DEBUG: Fly");
        for (Fly fly : flies) {    //struggle
            System.out.printf("#: %3d, ", flies.indexOf(fly) + 1);
            dump(fly);
        }
        System.out.println("DEBUG: End Flies ------\n");
    }

    /**
     * dump the current information about the fly
     * @param fly
     */
    private static void dump(Fly fly) {
        System.out.printf("ROW: %3d, COLUMN: %3d, SIZE: %6s, ENERGY: %3d, SCORE: %2d, HEALTH: %2d%n",
                fly.locationRow,
                fly.locationCol,
                fly.size,
                fly.vibrateEnergy,
                fly.scoreValue,
                fly.health);
    }
}
