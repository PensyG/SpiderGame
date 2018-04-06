import java.util.ArrayList;
import java.util.Random;

/**
 * Simulates a fly caught in a web, able to vibrate, and be eaten by a spider
 */
public class Fly
{

    private static Random random = new Random();
    private static ArrayList<Fly> flies = new ArrayList<>();    //Fly array

    private static double MAX_SMALL;       //maximum amount of small flies allowed in web
    private static double MAX_MEDIUM;      //maximum amount of medium flies
    private static double MAX_LARGE;       //maximum amount of large flies
    private static int currentSmall;    //current small flies
    private static int currentMedium;   //current medium flies
    private static int currentLarge;    //current large flies

    private int locationRow;            //row of fly
    private int locationCol;            //column of fly
    private int vibrateEnergy;          //energy of the Fly, based on FlySize enum type
    private int scoreValue;             //score value of the fly
    private int foodValue;              //food value of the fly, given to the Spider as life when eaten
    private FlySize size;               //size of fly, larger = bigger vibrations

    /**
     * This constructor creates a new fly in the web
     * @param locationRow_ - row of the web (i)
     * @param locationCol_ - column of the web (j)
     */
    public Fly(int locationRow_, int locationCol_, FlySize size_)
    {
        locationRow = locationRow_;                     //new (i) coordinate
        locationCol = locationCol_;                     //new (j) coordinate
        size = size_;                                   //FlySize
        vibrateEnergy = (size.ordinal() + 1) * 5;       //vibrate more the larger the size (10, 15, 20)
        foodValue = (size.ordinal() + 1) * 2;           //For each size, give 2 health when eaten
        scoreValue = (size.ordinal()+ 1) * 5;           //for each size, give 5 score when eaten
    }//end of Fly constructor

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
        }//end of FlySize method
    }//end of FlySize

    /**
     * The setConstants method sets up class variables before each game
     * @param webSize - Current diameter of the web (50, 100, 200, 400)
     */
    public static void setConstants(int webSize)
    {
        //number of 50x50 quadrants in web
        int numberQuadrants = (int) Math.pow((webSize / 50), 2);

        MAX_SMALL = (int) (6.5 * numberQuadrants);
        MAX_MEDIUM = (int) (3.33 * numberQuadrants);
        MAX_LARGE = (int) (1.2 * numberQuadrants);
    }//end of setConstants method

    /**
     * The adjustConstants method sets up adjust variables each turn
     * The game regenerates less flies as it continues
     */
    public static void adjustConstants()
    {
        double reductionConstant = .95;

        MAX_SMALL *= reductionConstant;
        MAX_MEDIUM *= reductionConstant;
        MAX_LARGE *= reductionConstant;
    }//end of adjustConstants method

    /**
     * The getFoodValue returns the Fly's food value for spider
     * @return - the amount of health the Fly is worth
     */
    public int getFoodValue()
    {
        return foodValue;
    }//end of getFoodValue method

    /**
     * The getScoreValue returns the fly's value
     * @return - The amount of Game.score the Fly is worth
     */
    public int getScoreValue()
    {
        return scoreValue;
    }//end of getScoreValue method

    /**
     * The getRow method returns the fly's row in the web
     * @return - The current row of the Fly (i)
     */
    public int getRow()
    {
        return locationRow;
    }//end of getRow method

    /**
     * The getCol method returns the fly's column in the web
     * @return - The current Column of the Fly (j)
     */
    public int getCol()
    {
        return locationCol;
    }//end of getCol method

    /**
     * The getEnergy method returns the value of the fly's vibration in the web
     * @return - The vibration value, 5, 10, or 15 based on current sizes
     */
    public int getEnergy()
    {
        return vibrateEnergy;
    }//end of getEnergy method

    /**
     * The getSize method returns the size of the fly
     * @return - Small, Medium, Large
     */
    public FlySize getSize()
    {
        return size;
    }//end of getSize method

    /**
     * The getFlies method returns the fly ArrayList
     * @return - the entire Fly array
     */
    public static ArrayList<Fly> getFlies()
    {
        return flies;
    }//end of getFlies method

    /**
     * The getMaxEnergy method sets the formatting of the web
     * used for formatting the display of vibration values (so it's all the same value)
     * @return - the fly
     */
    public static int getMaxEnergy()
    {
        int maxEnergy = 0;
        for (Fly fly : flies)
        {
            //find max energy to format the web display
            if (maxEnergy < fly.getEnergy())
                maxEnergy = fly.getEnergy();
        }//end of enhanced for loop to find max energy in array
        return maxEnergy;
    }//end of getMaxEnergy method

    /**
     * The getFly method checks if current element (i, j) is a fly
     *
     * @param i - row of Web array
     * @param j - column of Web array
     * @return the fly object in that location, or null if it's empty
     */
    public static Fly getFly(int i, int j)
    {
        //location matches a Fly in the flies array
        for (Fly fly : flies)
            if (i == fly.getRow() && j == fly.getCol())
                return fly;
        return null;
    }//end of getFly method

    /**
     * The getCurrentFlies method updates the amount of each size of Fly in the web, is called in
     * generateFlies to make sure the max amount of flies are in the web each turn
     */
    private static void getCurrentFlies()
    {
        currentSmall = 0;
        currentMedium = 0;
        currentLarge = 0;
        for (Fly fly : flies)
            switch (fly.getSize())
            {
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
                    System.out.println("Error in getCurrentFlies()");
                    break;
            }//end of fly.getSize switch
    }//end of getCurrentFlies method

    /**
     * The update method updates the current amount of Fly objects in the array
     * @param web - Web object, passed into the generateFlies function
     */
    public static void update(Web web)
    {
        Fly.getCurrentFlies();
        generateFlies(web);

        //debug method
        if (Game.DEBUG)
            debug();
    }//end of update method

    /**
     * The generateFlies method generates a fly in a currently unoccupied space while not
     * at max number of flies allowed for web
     * @param web - the Web object the fly is being generated in
     */
    private static void generateFlies(Web web)
    {
        int row;  //row
        int col;  //column

        boolean generated;
        FlySize newSize;
        
        //while there are still flies in the web to generate
        while (FlySize.newSize() != null)
        {
            newSize = FlySize.newSize();
            generated = false;
            
            //gen random locations until empty spot is found, then add fly there
            while (!generated)
            {
                row = random.nextInt(web.getWebLength());
                col = random.nextInt(web.getWebLength());
                //generates a new fly
                if (getFly(row, col) == null)
                {
                    flies.add(new Fly(row, col, newSize));
                    Fly.getCurrentFlies();
                    generated = true;
                }//end of if loop to generate new fly
            }//end of gen random location to find empty spot
        }//end of loop to run while there are still flies in the web to generate
    }//end of generateFlies method

    /**
     * The debug method offers diagnostics on current Fly information
     */
    private static void debug()
    {
        System.out.println("DEBUG: Fly");
        for (Fly fly : flies)
        {    //struggle
            System.out.printf("#: %3d, ", flies.indexOf(fly) + 1);
            dump(fly);
        }//end of enhanced for loop to display flies
        System.out.println("DEBUG: End Flies ------\n");
    }//end of debug method

    /**
     * The dump method dumps the current information about the fly
     * @param fly - current fly that information is needed
     */
    private static void dump(Fly fly)
    {
        System.out.printf("ROW: %3d, COLUMN: %3d, SIZE: %6s, ENERGY: %3d, SCORE: %2d%n",
                fly.locationRow,
                fly.locationCol,
                fly.size,
                fly.vibrateEnergy,
                fly.scoreValue);
    }//end of dump method
}//end of Fly class
