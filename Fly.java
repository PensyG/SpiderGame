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
    private int vibrateEnergy;          //energy of the Fly, based on FlySize enum type
    private int scoreValue;             //score value of the fly
    private int foodValue;              //food value of the fly, given to the Spider as life when eaten
    private FlySize size;               //size of fly, larger = more energy given

    /**
     * @param locationRow_ - row of the web (i)
     * @param locationCol_ - column of the web (j)
     */
    public Fly(int locationRow_, int locationCol_, FlySize size_) {
        locationRow = locationRow_;                                         //new (i) coordinate
        locationCol = locationCol_;                                         //new (j) coordinate
        size = size_;
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
     * @param webSize - Current diameter of the web (50, 100, 200, 400)
     */
    public static void setConstants(int webSize) {
        //number of 50x50 quadrants in web
        int numberQuadrants = (int) Math.pow((webSize / 50), 2);

        MAX_SMALL = (int) (5.5 * numberQuadrants);
        MAX_MEDIUM = (int) (3.33 * numberQuadrants);
        MAX_LARGE = (int) (1.2 * numberQuadrants);
    }

    /**
     *
     * @return - the amount of health the Fly is worth
     */
    public int getFoodValue() {
        return foodValue;
    }

    /**
     *
     * @return - The amount of Game.score the Fly is worth
     */
    public int getScoreValue() {
        return scoreValue;
    }

    /**
     * @return - The current row of the Fly (i)
     */
    public int getRow() {
        return locationRow;
    }

    /**
     * @return - The current Column of the Fly (j)
     */
    public int getCol() {
        return locationCol;
    }

    /**
     * @return - The vibration value, 5, 10, or 15 based on current sizes
     */
    public int getEnergy() {
        return vibrateEnergy;
    }

    /**
     * @return - Small, Medium, Large
     */
    public FlySize getSize() {
        return size;
    }

    /**
     * @return - the entire Fly array
     */
    public static ArrayList<Fly> getFlies() {
        return flies;
    }

    /**
     * used for formatting the display of vibration values (so it's all the same value)
     * @return - the fly
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
     * Updates the amount of each size of Fly in the web, is called in generateFlies to make sure the
     * max amount of flies are in the web each turn
     */
    private static void updateCurrentFlies() {
        currentSmall = 0;
        currentMedium = 0;
        currentLarge = 0;
        for (Fly fly : flies)
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

    /**
     * @param web - Web object, passed into the generateFlies function
     */
    public static void update(Web web) {
        Fly.updateCurrentFlies();
        generateFlies(web);

        //debug method
        if (Game.DEBUG)
            debug();
    }

    /**
     * Generates a fly in a currently unoccupied space
     * @param web - Web object, is used for it's array and checking capability
     */
    private static void generateFlies(Web web) {

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
     * @param fly - current fly that information is needed
     */
    private static void dump(Fly fly) {
        System.out.printf("ROW: %3d, COLUMN: %3d, SIZE: %6s, ENERGY: %3d, SCORE: %2d%n",
                fly.locationRow,
                fly.locationCol,
                fly.size,
                fly.vibrateEnergy,
                fly.scoreValue);
    }
}